package uk.ac.gla.dcs.bigdata.studentstructures;

import uk.ac.gla.dcs.bigdata.providedstructures.Query;

import java.io.Serializable;

public class DPHQueryScoredArticle implements Serializable{

    private static final long serialVersionID = 2385679;

    private String docid;
    private String title;
    private Double queryScore;
    private Query query;


    public DPHQueryScoredArticle(String docid, String title, Double queryScore, Query query) {
        this.docid = docid;
        this.title = title;
        this.queryScore = queryScore;
        this.query = query;
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

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }
}

