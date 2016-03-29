package projects.nanodegree.udacity.nisha.com.popularmoviestage2.rest.model;

import java.util.ArrayList;

/**
 * Created by shani on 3/15/16.
 */
public class JSONTrailerResult {
    private ArrayList<TrailerDetails> results;

    public ArrayList<TrailerDetails> getResults() {
        return results;
    }

    public void setResults(ArrayList<TrailerDetails> results) {
        this.results = results;
    }
}
