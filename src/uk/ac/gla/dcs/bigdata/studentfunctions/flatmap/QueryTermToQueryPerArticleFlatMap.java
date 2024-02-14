package uk.ac.gla.dcs.bigdata.studentfunctions.flatmap;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.broadcast.Broadcast;
import uk.ac.gla.dcs.bigdata.providedstructures.Query;
import uk.ac.gla.dcs.bigdata.studentstructures.DPHQueryScoredArticle;
import uk.ac.gla.dcs.bigdata.studentstructures.DPHTermScoredArticle;

import java.util.*;

public class QueryTermToQueryPerArticleFlatMap implements FlatMapFunction<DPHTermScoredArticle, DPHQueryScoredArticle> {

    Broadcast<List<Query>> queriesBV;

    public QueryTermToQueryPerArticleFlatMap(Broadcast<List<Query>> queriesBV) {
        this.queriesBV = queriesBV;
    }

    @Override
    public Iterator<DPHQueryScoredArticle> call(DPHTermScoredArticle value) throws Exception {
        List<Query> queries = queriesBV.value();
        Map<String, Double> dphTermScore = value.getDphTermScore();
        List<DPHQueryScoredArticle> dphArticles = new ArrayList<>();

        Map<Query, Double> queryScores = new HashMap<>();

        for (Query query : queries){
            List<String> queryTerms = query.getQueryTerms();
            int length = queryTerms.size();
            double scoreSum = 0.0;
            for (String term: query.getQueryTerms()){
                scoreSum = scoreSum + dphTermScore.get(term);
            }
            DPHQueryScoredArticle article = new DPHQueryScoredArticle(value.getDocid(), value.getTitle(), scoreSum/length, query);
            dphArticles.add(article);
        }
        return dphArticles.iterator();
    }
}
