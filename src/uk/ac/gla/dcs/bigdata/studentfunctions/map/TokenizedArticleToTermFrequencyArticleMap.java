package uk.ac.gla.dcs.bigdata.studentfunctions.map;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.broadcast.Broadcast;
import uk.ac.gla.dcs.bigdata.studentstructures.TermFrequencyAccumulator;
import uk.ac.gla.dcs.bigdata.studentstructures.TermFrequencyArticle;
import uk.ac.gla.dcs.bigdata.studentstructures.TokenizedArticle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokenizedArticleToTermFrequencyArticleMap implements MapFunction<TokenizedArticle, TermFrequencyArticle> {
    Broadcast<List<String>> queryTermsBV;
    TermFrequencyAccumulator tfAccumulator;

    public TokenizedArticleToTermFrequencyArticleMap(Broadcast<List<String>> queryTermsBV, TermFrequencyAccumulator tfAccumulator) {
        this.queryTermsBV = queryTermsBV;
        this.tfAccumulator = tfAccumulator;
    }

    @Override
    public TermFrequencyArticle call(TokenizedArticle value) throws Exception {
        List<String> queryTerms = queryTermsBV.value();
        List<String> tokenizedWords = value.getTokenizedText();

        Map<String, Short> termFrequencyCount = new HashMap<>();
        for (String term : queryTerms) { termFrequencyCount.put(term, (short) 0); }

        // O(n) runtime since containsKey is O(1) amortized
        for (String word : tokenizedWords) {
            if (!termFrequencyCount.containsKey(word)) { continue; }
            termFrequencyCount.put(word, (short) (termFrequencyCount.get(word) + 1));
        }

        tfAccumulator.add(termFrequencyCount);

        return new TermFrequencyArticle(value, termFrequencyCount);
    }
}
