package uk.ac.gla.dcs.bigdata.studentfunctions.reduce;

import org.apache.spark.api.java.function.ReduceFunction;
import uk.ac.gla.dcs.bigdata.studentstructures.QueryTermList;

import java.util.ArrayList;
import java.util.HashSet;

public class ReduceQueryTermToDistinctSingleList implements ReduceFunction<QueryTermList> {
    @Override
    public QueryTermList call(QueryTermList v1, QueryTermList v2) throws Exception {
        HashSet<String> queryTerms = new HashSet<>();

        for (String qTerm : v1.getQueryTerms()) {
            if (queryTerms.contains(qTerm)) { continue; }
            queryTerms.add(qTerm);
        }

        for (String qTerm : v2.getQueryTerms()) {
            if (queryTerms.contains(qTerm)) { continue; }
            queryTerms.add(qTerm);
        }

        return new QueryTermList(new ArrayList<>(queryTerms));
    }
}
