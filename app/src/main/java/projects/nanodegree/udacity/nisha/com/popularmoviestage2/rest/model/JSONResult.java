package projects.nanodegree.udacity.nisha.com.popularmoviestage2.rest.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shani on 3/13/16.
 */
public class JSONResult {
    @SerializedName("results")
    private ArrayList<MovieDetails> mResults = new ArrayList<>();

    public List<MovieDetails> getmResults() {
        return mResults;
    }

    public void setmResults(ArrayList<MovieDetails> mResults) {
        this.mResults = mResults;
    }
}
