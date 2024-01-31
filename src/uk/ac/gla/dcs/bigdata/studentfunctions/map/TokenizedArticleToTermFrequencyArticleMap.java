package uk.ac.gla.dcs.bigdata.studentfunctions.map;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.broadcast.Broadcast;
import uk.ac.gla.dcs.bigdata.studentstructures.TermFrequencyArticle;
import uk.ac.gla.dcs.bigdata.studentstructures.TokenizedArticle;

import java.util.Set;

public class TokenizedArticleToTermFrequencyArticleMap implements MapFunction<TokenizedArticle, TermFrequencyArticle> {
    Broadcast<Set<String>> queryTermsBV;

    public TokenizedArticleToTermFrequencyArticleMap(Broadcast<Set<String>> queryTermsBV) {
        this.queryTermsBV = queryTermsBV;
    }

    @Override
    public TermFrequencyArticle call(TokenizedArticle value) throws Exception {
        Set<String> queryTerms = queryTermsBV.value();
        return new TermFrequencyArticle(value, queryTerms);
    }
}
