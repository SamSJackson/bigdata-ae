package uk.ac.gla.dcs.bigdata.apps;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.*;

import org.apache.spark.util.LongAccumulator;
import uk.ac.gla.dcs.bigdata.providedfunctions.NewsFormaterMap;
import uk.ac.gla.dcs.bigdata.providedfunctions.QueryFormaterMap;
import uk.ac.gla.dcs.bigdata.providedstructures.DocumentRanking;
import uk.ac.gla.dcs.bigdata.providedstructures.NewsArticle;
import uk.ac.gla.dcs.bigdata.providedstructures.Query;
import uk.ac.gla.dcs.bigdata.studentfunctions.flatmap.QueryTermToQueryPerArticleFlatMap;
import uk.ac.gla.dcs.bigdata.studentfunctions.reduce.ReduceQueryTermToDistinctSingleList;
import uk.ac.gla.dcs.bigdata.studentfunctions.map.*;
import uk.ac.gla.dcs.bigdata.studentstructures.*;

/**
 * This is the main class where your Spark topology should be specified.
 * 
 * By default, running this class will execute the topology defined in the
 * rankDocuments() method in local mode, although this may be overriden by
 * the spark.master environment variable.
 * @author Richard
 *
 */
public class AssessedExercise {

	
	public static void main(String[] args) {
		
		File hadoopDIR = new File("resources/hadoop/"); // represent the hadoop directory as a Java file so we can get an absolute path for it
		System.setProperty("hadoop.home.dir", hadoopDIR.getAbsolutePath()); // set the JVM system property so that Spark finds it
		
		// The code submitted for the assessed exerise may be run in either local or remote modes
		// Configuration of this will be performed based on an environment variable
		String sparkMasterDef = System.getenv("spark.master");
		if (sparkMasterDef==null) sparkMasterDef = "local[4]"; // default is local mode with two executors
		
		String sparkSessionName = "BigDataAE"; // give the session a name
		
		// Create the Spark Configuration 
		SparkConf conf = new SparkConf()
				.setMaster(sparkMasterDef)
				.setAppName(sparkSessionName);
		
		// Create the spark session
		SparkSession spark = SparkSession
				  .builder()
				  .config(conf)
				  .getOrCreate();
	
		
		// Get the location of the input queries
		String queryFile = System.getenv("bigdata.queries");
		if (queryFile==null) queryFile = "data/queries.list"; // default is a sample with 3 queries
		
		// Get the location of the input news articles
		String newsFile = System.getenv("bigdata.news");
//		if (newsFile==null) newsFile = "data/TREC_Washington_Post_collection.v2.jl.fix.json"; // Full file - 509511 articles
		if (newsFile==null) newsFile = "data/TREC_Washington_Post_collection.v3.example.json"; // default is a sample of 5000 news articles

		// Call the student's code
		List<DocumentRanking> results = rankDocuments(spark, queryFile, newsFile);
		
		// Close the spark session
		spark.close();
		
		// Check if the code returned any results
		if (results==null) System.err.println("Topology return no rankings, student code may not be implemented, skiping final write.");
		else {
			
			// We have set of output rankings, lets write to disk
			
			// Create a new folder 
			File outDirectory = new File("results/"+System.currentTimeMillis());
			if (!outDirectory.exists()) outDirectory.mkdir();
			
			// Write the ranking for each query as a new file
			for (DocumentRanking rankingForQuery : results) {
				rankingForQuery.write(outDirectory.getAbsolutePath());
			}
		}
	}

	
	public static List<DocumentRanking> rankDocuments(SparkSession spark, String queryFile, String newsFile) {
		
		// Load queries and news articles
		Dataset<Row> queriesjson = spark.read().text(queryFile);
		Dataset<Row> newsjson = spark.read().text(newsFile); // read in files as string rows, one row per article

		// Perform an initial conversion from Dataset<Row> to Query and NewsArticle Java objects
		Dataset<Query> queries = queriesjson.map(new QueryFormaterMap(), Encoders.bean(Query.class)); // this converts each row into a Query
		Dataset<NewsArticle> news = newsjson.map(new NewsFormaterMap(), Encoders.bean(NewsArticle.class)); // this converts each row into a NewsArticle

		//----------------------------------------------------------------
		// Your Spark Topology should be defined here
		//----------------------------------------------------------------

		List<Query> queryList = queries.collectAsList();

		Dataset<QueryTermList> queryTermListDataset = queries.map(new QueryToQueryTermList(), Encoders.bean(QueryTermList.class));
		List<String> queryTermList = queryTermListDataset.reduce(new ReduceQueryTermToDistinctSingleList()).getQueryTerms();

		Broadcast<List<String>> queryTermsBV = JavaSparkContext
				.fromSparkContext(spark.sparkContext())
				.broadcast(queryTermList);

		LongAccumulator documentLengthAccumulator = spark.sparkContext().longAccumulator();
		NewsArticleToTokenizedArticleMap articleTokenizationMap = new NewsArticleToTokenizedArticleMap(documentLengthAccumulator);

		Dataset<TokenizedArticle> tokenizedNews = news.map(articleTokenizationMap, Encoders.bean(TokenizedArticle.class));

		long numTokenizedArticles = tokenizedNews.count();
		long summedDocLength = documentLengthAccumulator.value();
		double averageDocumentLength = summedDocLength / numTokenizedArticles;

		System.out.println("Number of articles: " + numTokenizedArticles);

		TermFrequencyAccumulator tfAccumulator = new TermFrequencyAccumulator();
		spark.sparkContext().register(tfAccumulator);

		TokenizedArticleToTermFrequencyArticleMap tokenizeToTFArticleMap = new TokenizedArticleToTermFrequencyArticleMap(queryTermsBV, tfAccumulator);
		Dataset<TermFrequencyArticle> tfArticles = tokenizedNews.map(tokenizeToTFArticleMap, Encoders.bean(TermFrequencyArticle.class));

		tfArticles.count(); // Forcing spark action to get value.
		Map<String, Integer> summedCounts = tfAccumulator.value();

		Broadcast<Map<String, Integer>> summedCountsBV = JavaSparkContext
				.fromSparkContext(spark.sparkContext())
				.broadcast(summedCounts);

		Broadcast<Long> numTokenizedArticlesBV = JavaSparkContext
				.fromSparkContext(spark.sparkContext())
				.broadcast(numTokenizedArticles);

		Broadcast<Double> averageDocumentLengthBV = JavaSparkContext
				.fromSparkContext(spark.sparkContext())
				.broadcast(averageDocumentLength);

		ScoreArticlePerTerm articleScoringMap = new ScoreArticlePerTerm(summedCountsBV, averageDocumentLengthBV, numTokenizedArticlesBV);

		Dataset<DPHTermScoredArticle> dphTermScoredArticles = tfArticles
				.map(articleScoringMap, Encoders.bean(DPHTermScoredArticle.class));

		Broadcast<List<Query>> queryBV = JavaSparkContext
				.fromSparkContext(spark.sparkContext())
				.broadcast(queryList);

		QueryTermToQueryPerArticleFlatMap queryTermToQueryPerArticle = new QueryTermToQueryPerArticleFlatMap(queryBV);

		Dataset<DPHQueryScoredArticle> dphQueryScoredArticles = dphTermScoredArticles
				.flatMap(queryTermToQueryPerArticle, Encoders.bean(DPHQueryScoredArticle.class))
				.sort((new Column("queryScore")).desc());

		dphQueryScoredArticles.show();

		return null; // replace this with the list of DocumentRanking output by your topology
	}
}
