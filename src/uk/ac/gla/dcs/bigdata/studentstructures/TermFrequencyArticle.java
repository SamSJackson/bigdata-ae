package uk.ac.gla.dcs.bigdata.studentstructures;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TermFrequencyArticle implements Serializable {

    private static final long serialVersionID = -2520997L;

    private Map<String, Integer> termFrequencyCount = new HashMap<>();

    private String docid;
    private List<String> tokenizedText;

    public TermFrequencyArticle() {}

    /**
     * Compute term frequencies of query terms
     * @param article
     * @param queryTerms
     */
    public TermFrequencyArticle(TokenizedArticle article, Set<String> queryTerms) {
        this.docid = article.getDocid();
        this.tokenizedText = article.getTokenizedText();

        List<String> tokenizedWords = article.getTokenizedText();
        for (String term : queryTerms) { this.termFrequencyCount.put(term, 0); }

        // O(n) runtime since containsKey is O(1) amortized
        for (String word : tokenizedWords) {
            if (!this.termFrequencyCount.containsKey(word)) { continue; }
            this.termFrequencyCount.put(word, this.termFrequencyCount.get(word) + 1);
        }
    }

    public Map<String, Integer> getTermFrequencyCount() {
        return termFrequencyCount;
    }

    public void setTermFrequencyCount(Map<String, Integer> termFrequencyCount) {
        this.termFrequencyCount = termFrequencyCount;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public List<String> getTokenizedText() {
        return tokenizedText;
    }

    public void setTokenizedText(List<String> tokenizedText) {
        this.tokenizedText = tokenizedText;
    }
}
