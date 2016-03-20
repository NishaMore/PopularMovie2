package projects.nanodegree.udacity.nisha.com.popularmoviestage1.rest.model;

import java.util.ArrayList;

/**
 * Created by shani on 3/15/16.
 */
public class JSONTrailerResult {
    private ArrayList<TrailerKeys> results;

    public ArrayList<TrailerKeys> getResults() {
        return results;
    }

    public void setResults(ArrayList<TrailerKeys> results) {
        this.results = results;
    }
}
