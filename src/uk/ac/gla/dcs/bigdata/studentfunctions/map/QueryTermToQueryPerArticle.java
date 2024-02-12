package uk.ac.gla.dcs.bigdata.studentfunctions.map;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.broadcast.Broadcast;
import uk.ac.gla.dcs.bigdata.providedstructures.Query;
import uk.ac.gla.dcs.bigdata.studentstructures.DPHQueryScoredArticle;
import uk.ac.gla.dcs.bigdata.studentstructures.DPHTermScoredArticle;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class QueryTermToQueryPerArticle implements FlatMapFunction<DPHTermScoredArticle, DPHQueryScoredArticle> {

    Broadcast<List<Query>> queriesBV;

    @Override
    public Iterator<DPHQueryScoredArticle> call(DPHTermScoredArticle value) throws Exception {
        List<Query> queries = queriesBV.value();
        Map<String, Double> dphTermScore = value.getDphTermScore();

        Map<Query, Double> queryScores = new HashMap<>();

        for (Query query : queries){
            Double count = 0.0;
            Double scoreSum = 0.0;
            for (String term: query.getQueryTerms()){
                count = count++;
                scoreSum = scoreSum + dphTermScore.get(term);
            }
            queryScores.put(query, scoreSum/count);
        }
        Iterator<DPHQueryScoredArticle> dphQueryScoredArticle = new DPHQueryScoredArticle(value.getDocid(), value.getTitle(), );
        return dphQueryScoredArticle;
    }
}
