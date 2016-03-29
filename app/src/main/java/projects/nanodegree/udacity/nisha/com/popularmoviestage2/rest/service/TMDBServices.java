package projects.nanodegree.udacity.nisha.com.popularmoviestage2.rest.service;

import projects.nanodegree.udacity.nisha.com.popularmoviestage2.rest.model.JSONResult;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.rest.model.JSONReviewResult;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.rest.model.JSONTrailerResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by shani on 3/15/16.
 */
public interface TMDBServices {

    @GET("/3/movie/{sort}")
    Call<JSONResult> getMovieDetails(@Path("sort") String sort_criteria, @Query("api_key") String APIKey);


    @GET("/3/movie/{id}/videos")
    Call<JSONTrailerResult> getTrailerResults(@Path("id") String movieId,
                                              @Query("api_key") String API_KEY);

    @GET("/3/movie/{id}/reviews")
    Call<JSONReviewResult> getReviewResults(@Path("id") String movieId,
                                            @Query("api_key") String API_KEY);


}
