package uk.ac.gla.dcs.bigdata.studentfunctions.map;

import org.apache.spark.api.java.function.MapFunction;

import uk.ac.gla.dcs.bigdata.providedstructures.NewsArticle;
import uk.ac.gla.dcs.bigdata.studentstructures.CleanArticle;

public class NewsArticleToCleanArticleMap implements MapFunction<NewsArticle, CleanArticle> {
    @Override
    public CleanArticle call(NewsArticle value) throws Exception {
        return new CleanArticle(value);
    }
}
