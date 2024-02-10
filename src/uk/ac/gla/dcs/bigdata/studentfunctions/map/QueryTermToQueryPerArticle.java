package uk.ac.gla.dcs.bigdata.studentfunctions.map;

import org.apache.spark.api.java.function.FlatMapFunction;
import uk.ac.gla.dcs.bigdata.providedstructures.NewsArticle;
import uk.ac.gla.dcs.bigdata.studentstructures.DPHQueryScoredArticle;
import uk.ac.gla.dcs.bigdata.studentstructures.DPHTermScoredArticle;
import uk.ac.gla.dcs.bigdata.studentstructures.TokenizedArticle;

import java.util.Iterator;

public class QueryTermToQueryPerArticle implements FlatMapFunction<DPHTermScoredArticle, DPHQueryScoredArticle> {

    @Override
    public Iterator<DPHQueryScoredArticle> call(DPHTermScoredArticle dphTermScoredArticle) throws Exception {
        Iterator<DPHQueryScoredArticle> dphQueryScoredArticle = new DPHQueryScoredArticle
        return dphQueryScoredArticle;
    }
}
