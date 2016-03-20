package projects.nanodegree.udacity.nisha.com.popularmoviestage1.rest.service;

import projects.nanodegree.udacity.nisha.com.popularmoviestage1.rest.model.JSONResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by shani on 3/13/16.
 */
public interface TMDBService {

//   /* @GET("/3/discover/movie/{sorting_criteria}")
//    Call<JSONResult> getMovieDetails(@Path("sorting_criteria") String sorting_criteria,@Query("api_key") String API_KEY);*/
@GET("/3/movie/{sort}")
Call<JSONResult> getMovieDetails(@Path("sort") String sort_criteria, @Query("api_key") String APIKey);
}
