package uk.ac.gla.dcs.bigdata.studentstructures;

import org.apache.spark.util.AccumulatorV2;

import java.util.HashMap;
import java.util.Map;

public class TermFrequencyAccumulator extends AccumulatorV2<HashMap<String, Integer>, HashMap<String, Integer>> {

    private HashMap<String, Integer> outputHashMap;

    public TermFrequencyAccumulator() {
        this.outputHashMap = new HashMap<>();
    }

    @Override
    public boolean isZero() {
        return this.outputHashMap.size() == 0;
    }

    @Override
    public AccumulatorV2<HashMap<String, Integer>, HashMap<String, Integer>> copy() {
        TermFrequencyAccumulator accumulatorCopy = new TermFrequencyAccumulator();
        accumulatorCopy.merge(this);
        return accumulatorCopy;
    }

    @Override
    public void reset() {
        this.outputHashMap = new HashMap<>();
    }

    @Override
    public void add(HashMap<String, Integer> v) {
        v.forEach((key, value) -> {
            this.outputHashMap.merge(key, value, (oldValue, newValue) -> oldValue + newValue);
        });
    }

    @Override
    public void merge(AccumulatorV2<HashMap<String, Integer>, HashMap<String, Integer>> other) {
        other.value().forEach((key, value) -> {
            this.outputHashMap.merge(key, value, (oldValue, newValue) -> oldValue + newValue);
        });
    }

    @Override
    public HashMap<String, Integer> value() {
        return this.outputHashMap;
    }

    public void add(Map<String, Short> v) {
        v.forEach((key, value) -> {
            this.outputHashMap.merge(key, (int) value, (oldValue, newValue) -> (oldValue + newValue));
        });
    }
}
