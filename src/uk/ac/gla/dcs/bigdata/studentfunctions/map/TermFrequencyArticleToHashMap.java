package uk.ac.gla.dcs.bigdata.studentfunctions.map;

import org.apache.spark.api.java.function.MapFunction;
import uk.ac.gla.dcs.bigdata.studentstructures.TermFrequencyArticle;
import uk.ac.gla.dcs.bigdata.studentstructures.TermFrequencyHashMap;

import java.util.HashMap;
import java.util.Map;

public class TermFrequencyArticleToHashMap implements MapFunction<TermFrequencyArticle, TermFrequencyHashMap> {
    @Override
    public TermFrequencyHashMap call(TermFrequencyArticle value) throws Exception {
        /** This conversion probably has quite poor performance.
         * Room to improve it.*/
        Map<String, Integer> tfIntConverted = new HashMap<>();
        value.getTermFrequencyCount().forEach((k, v) -> tfIntConverted.put(k, (int)v));

        return new TermFrequencyHashMap(tfIntConverted);
    }
}
