package uk.ac.gla.dcs.bigdata.studentfunctions.map;

import org.apache.spark.api.java.function.MapFunction;
import uk.ac.gla.dcs.bigdata.providedstructures.Query;
import uk.ac.gla.dcs.bigdata.studentstructures.QueryTermList;

import java.util.List;

public class QueryToQueryTermList implements MapFunction<Query, QueryTermList> {
    @Override
    public QueryTermList call(Query value) throws Exception {
        List<String> qList = value.getQueryTerms();
        return new QueryTermList(qList);
    }
}
