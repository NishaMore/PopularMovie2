package projects.nanodegree.udacity.nisha.com.popularmoviestage1.rest.model;

/**
 * Created by Nisha on 3/16/16.
 *
 * Model for Review details
 */
public class ReviewDetails {

    private String author;
    private String content;

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
