package uk.ac.gla.dcs.bigdata.studentstructures;

import uk.ac.gla.dcs.bigdata.providedstructures.ContentItem;
import uk.ac.gla.dcs.bigdata.providedstructures.NewsArticle;
import uk.ac.gla.dcs.bigdata.providedutilities.TextPreProcessor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class TokenizedArticle implements Serializable {

    private final TextPreProcessor textProcessor = new TextPreProcessor();
    private static final long serialVersionID = -2520998L;

    private String docid;

    private List<String> tokenizedText;

    /**
     * Default constructor
     */
    public TokenizedArticle() {}

    /**
     * Constructor for converting NewsArticle to CleanArticle
     * @param article
     */
    public TokenizedArticle(NewsArticle article) {
        this.docid = article.getId();
        this.tokenizedText = textProcessor.process(
                article.getTitle() + " " + this.parseContents(article.getContents())
        );
    }

    /**
     * Parse the useful contentItems - of subtype paragraph, at most first 5.
     * @param contents
     * @return contentCappedString
     */
    private String parseContents(List<ContentItem> contents) {
        List<String> paragraphContents = contents.stream()
                .filter(cItem -> {
                    return (cItem.getSubtype() != null && cItem.getSubtype().equals("paragraph"));
                }).map(c -> {
                    return c.getContent();
                }).collect(Collectors.toList());

        String contentCappedString  = paragraphContents.stream()
                .limit(Math.min(5, paragraphContents.size()))
                .collect(Collectors.joining(" "));

        return contentCappedString;
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
