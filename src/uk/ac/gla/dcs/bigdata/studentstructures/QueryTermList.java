package uk.ac.gla.dcs.bigdata.studentstructures;

import java.io.Serializable;
import java.util.List;

public class QueryTermList implements Serializable {

    private static final long serialVersionUID = -202021893L;

    private List<String> queryTerms;

    public QueryTermList(List<String> qList) {
        this.queryTerms = qList;
    }

    public List<String> getQueryTerms() {
        return queryTerms;
    }

    public void setQueryTerms(List<String> queryTerms) {
        this.queryTerms = queryTerms;
    }
}
