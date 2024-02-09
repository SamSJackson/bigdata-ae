package uk.ac.gla.dcs.bigdata.studentstructures;

import java.io.Serializable;
import java.util.Map;

public class DPHQueryScoredArticles implements Serializable{

    private static final long serialVersionID = 2385679;

    private String docid;
    private String title;
    private Double queryScore;
    private Integer queryid;

    public DPHQueryScoredArticles(String docid, String title, Double queryScore, Integer queryid) {
        this.docid = docid;
        this.title = title;
        this.queryScore = queryScore;
        this.queryid = queryid;
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

    public Double getQueryScore() {
        return queryScore;
    }

    public void setQueryScore(Double queryScore) {
        this.queryScore = queryScore;
    }

    public Integer getQueryid() {
        return queryid;
    }

    public void setQueryid(Integer queryid) {
        this.queryid = queryid;
    }
}

