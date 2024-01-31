package uk.ac.gla.dcs.bigdata.studentfunctions.map;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.util.LongAccumulator;
import uk.ac.gla.dcs.bigdata.providedstructures.NewsArticle;
import uk.ac.gla.dcs.bigdata.studentstructures.TokenizedArticle;

public class NewsArticleToTokenizedArticleMap implements MapFunction<NewsArticle, TokenizedArticle> {

    LongAccumulator documentLengthsAccumulator;

    public NewsArticleToTokenizedArticleMap(LongAccumulator documentLengthsAccumulator) {
        this.documentLengthsAccumulator = documentLengthsAccumulator;
    }

    @Override
    public TokenizedArticle call(NewsArticle value) throws Exception {
        TokenizedArticle tokenizedArticle = new TokenizedArticle(value);
        documentLengthsAccumulator.add(tokenizedArticle.getTokenizedText().size());
        return tokenizedArticle;
    }
}
