package projects.nanodegree.udacity.nisha.com.popularmoviestage2.rest.service;

import projects.nanodegree.udacity.nisha.com.popularmoviestage2.rest.model.JSONReviewResult;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by shani on 3/16/16.
 */
public interface ReviewService {

    @GET("")
    Call<JSONReviewResult> getReviewResults();
}
