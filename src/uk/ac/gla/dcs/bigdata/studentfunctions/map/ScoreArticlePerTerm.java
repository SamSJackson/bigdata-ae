package uk.ac.gla.dcs.bigdata.studentfunctions.map;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.broadcast.Broadcast;
import uk.ac.gla.dcs.bigdata.providedutilities.DPHScorer;
import uk.ac.gla.dcs.bigdata.studentstructures.DPHTermScoredArticle;
import uk.ac.gla.dcs.bigdata.studentstructures.TermFrequencyArticle;

import java.util.HashMap;
import java.util.Map;

public class ScoreArticlePerTerm implements MapFunction<TermFrequencyArticle, DPHTermScoredArticle> {

    private Broadcast<Map<String, Integer>> totalTermCountsBV;
    private double avgDocLength;
    private long numDocs;

    public ScoreArticlePerTerm(Broadcast<Map<String, Integer>> totalTermCountsBV, Broadcast<Double> avgDocLength, Broadcast<Long> numDocs) {
        this.totalTermCountsBV = totalTermCountsBV;
        this.avgDocLength = avgDocLength.value();
        this.numDocs = numDocs.value();
    }

    @Override
    public DPHTermScoredArticle call(TermFrequencyArticle value) throws Exception {
        Map<String, Integer> totalTermCounts = totalTermCountsBV.value();
        Map<String, Short> tfsInDoc = value.getTermFrequencyCount();
        int docLength = value.getDocLength();

        Map<String, Double> dphPerTermMap = new HashMap<>();
        for (String term : totalTermCounts.keySet()) {
            short tfInDoc = tfsInDoc.get(term);
            int totalTermCount = totalTermCounts.get(term);

            if (tfInDoc == 0) { dphPerTermMap.put(term, 0.0); continue; }

            double dphScore = DPHScorer.getDPHScore(
                    tfInDoc,
                    totalTermCount,
                    docLength,
                    this.avgDocLength,
                    this.numDocs
            );

            dphPerTermMap.put(term, dphScore);
        }

        return new DPHTermScoredArticle(value, dphPerTermMap);
    }
}
