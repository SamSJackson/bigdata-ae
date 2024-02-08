package uk.ac.gla.dcs.bigdata.studentfunctions.map;

import java.util.List;

import org.apache.spark.api.java.function.MapFunction;

import uk.ac.gla.dcs.bigdata.providedstructures.Query;
import uk.ac.gla.dcs.bigdata.studentstructures.QueryTermList;

public class QueryToQueryTermList implements MapFunction<Query, QueryTermList> {
    @Override
    public QueryTermList call(Query value) throws Exception {
        List<String> qList = value.getQueryTerms();
        return new QueryTermList(qList);
    }
}
