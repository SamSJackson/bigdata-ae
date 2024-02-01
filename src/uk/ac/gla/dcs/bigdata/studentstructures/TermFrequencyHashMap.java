package uk.ac.gla.dcs.bigdata.studentstructures;

import java.io.Serializable;
import java.util.Map;

public class TermFrequencyHashMap implements Serializable {

    private static final long serialVersionID = -2520990L;

    private Map<String, Integer> termFrequencyCount;

    public TermFrequencyHashMap(Map<String, Integer> tfCount) {
        this.termFrequencyCount = tfCount;
    }

    public Map<String, Integer> getTermFrequencyCount() {
        return termFrequencyCount;
    }

    public void setTermFrequencyCount(Map<String, Integer> termFrequencyCount) {
        this.termFrequencyCount = termFrequencyCount;
    }
}
