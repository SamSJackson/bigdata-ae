package uk.ac.gla.dcs.bigdata.studentstructures;

import uk.ac.gla.dcs.bigdata.providedstructures.ContentItem;
import uk.ac.gla.dcs.bigdata.providedstructures.NewsArticle;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class CleanArticle implements Serializable {

    private static final long serialVersionID = -2520998L;

    private String docid;
    private String title;
    private String contentsText;

    public CleanArticle() {}

    public CleanArticle(NewsArticle article) {
        this.docid = article.getId();
        this.title = article.getTitle();
        this.contentsText = this.parseContents(article.getContents());
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getContentsText() {
        return contentsText;
    }

    public void setContentsText(String contentsText) {
        this.contentsText = contentsText;
    }
}
