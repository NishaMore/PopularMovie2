package projects.nanodegree.udacity.nisha.com.popularmoviestage2.rest.model;

import java.util.ArrayList;

/**
 * Created by shani on 3/16/16.
 * POJO for review json result: Retrofit will link the json result to this pojo for Review details.
 */
public class JSONReviewResult {

    private ArrayList<ReviewDetails> results;

    public ArrayList<ReviewDetails> getResults() {
        return results;
    }

    public void setResults(ArrayList<ReviewDetails> results) {
        this.results = results;
    }
}
