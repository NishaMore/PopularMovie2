package projects.nanodegree.udacity.nisha.com.popularmoviestage2.rest.model;

/**
 * Created by Nisha on 3/16/16.
 * <p/>
 * Model for Review details
 */
public class ReviewDetails {
    private String id;
    private String author;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;

    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
