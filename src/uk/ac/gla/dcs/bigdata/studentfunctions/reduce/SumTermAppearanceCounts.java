package uk.ac.gla.dcs.bigdata.studentfunctions.reduce;

import org.apache.spark.api.java.function.ReduceFunction;
import org.apache.spark.broadcast.Broadcast;
import uk.ac.gla.dcs.bigdata.studentstructures.TermFrequencyHashMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SumTermAppearanceCounts implements ReduceFunction<TermFrequencyHashMap> {

    Broadcast<List<String>> queryTermsBV;

    public SumTermAppearanceCounts(Broadcast<List<String>> queryTermsBV) {
        this.queryTermsBV = queryTermsBV;
    }

    @Override
    public TermFrequencyHashMap call(TermFrequencyHashMap v1, TermFrequencyHashMap v2) throws Exception {
        List<String> queryTerms = queryTermsBV.value();
        Map<String, Integer> summedMap = new HashMap<>();
        Map<String, Integer> v1Map = v1.getTermFrequencyCount();
        Map<String, Integer> v2Map = v2.getTermFrequencyCount();

        for (String term : queryTerms) {
            summedMap.put(term, v1Map.getOrDefault(term, 0) + v2Map.getOrDefault(term, 0));
        }
        return new TermFrequencyHashMap(summedMap);
    }
}
