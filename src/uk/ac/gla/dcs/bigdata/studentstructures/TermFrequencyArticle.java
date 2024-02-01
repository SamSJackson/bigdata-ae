package uk.ac.gla.dcs.bigdata.studentstructures;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TermFrequencyArticle implements Serializable {

    private static final long serialVersionID = -2520997L;

    private Map<String, Short> termFrequencyCount = new HashMap<>();

    private String docid;
    private String title;
    private List<String> tokenizedText;

    private int docLength;

    public TermFrequencyArticle() {}

    /**
     * Compute term frequencies of query terms
     * @param article
     * @param queryTerms
     */
    public TermFrequencyArticle(TokenizedArticle article, List<String> queryTerms) {
        this.docid = article.getDocid();
        this.tokenizedText = article.getTokenizedText();
        this.title = article.getTitle();
        this.docLength = this.tokenizedText.size();

        List<String> tokenizedWords = article.getTokenizedText();
        for (String term : queryTerms) { this.termFrequencyCount.put(term, (short) 0); }

        // O(n) runtime since containsKey is O(1) amortized
        for (String word : tokenizedWords) {
            if (!this.termFrequencyCount.containsKey(word)) { continue; }
            this.termFrequencyCount.put(word, (short) (this.termFrequencyCount.get(word) + 1));
        }
    }

    public Map<String, Short> getTermFrequencyCount() {
        return termFrequencyCount;
    }

    public void setTermFrequencyCount(Map<String, Short> termFrequencyCount) {
        this.termFrequencyCount = termFrequencyCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDocLength() {
        return docLength;
    }

    public void setDocLength(int docLength) {
        this.docLength = docLength;
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
