package projects.nanodegree.udacity.nisha.com.popularmoviestage1.rest;

import projects.nanodegree.udacity.nisha.com.popularmoviestage1.rest.service.TMDBServices;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by shani on 3/13/16.
 */
public class RestClient {

    private static String BASE_URL = "http://api.themoviedb.org/";
    private final Retrofit retrofit;

    public RestClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public TMDBServices getTMDBServices(){
        TMDBServices trailerService = null;

        if(retrofit!=null){
            trailerService = retrofit.create(TMDBServices.class);
        }

        return trailerService;
    }


}
