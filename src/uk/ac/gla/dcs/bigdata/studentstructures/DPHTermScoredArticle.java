package uk.ac.gla.dcs.bigdata.studentstructures;

import java.io.Serializable;
import java.util.Map;

public class DPHTermScoredArticle implements Serializable {

    private static final long serialVersionID = -2520980L;

    private String docid;
    private String title;
    private Map<String, Double> dphPerTermMap;
    private Map<String, Short> termFrequencyCount;


    public DPHTermScoredArticle(TermFrequencyArticle article, Map<String, Double> dphPerTermMap) {
        this.docid = article.getDocid();
        this.title = article.getDocid();
        this.termFrequencyCount = article.getTermFrequencyCount();
        this.dphPerTermMap = dphPerTermMap;
    }

    public Map<String, Double> getDphTermScore() {
        return dphPerTermMap;
    }

    public void setDphTermScore(Map<String, Double> dphTermScore) {
        this.dphPerTermMap = dphTermScore;
    }

    public Map<String, Short> getTermFrequencyCount() {
        return termFrequencyCount;
    }

    public void setTermFrequencyCount(Map<String, Short> termFrequencyCount) {
        this.termFrequencyCount = termFrequencyCount;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
