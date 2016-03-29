package projects.nanodegree.udacity.nisha.com.popularmoviestage2.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import projects.nanodegree.udacity.nisha.com.popularmoviestage2.R;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.rest.model.MovieDetails;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.ui.fragments.MovieDetailActivityFragment;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {


            //set arguments coming from intent
            Bundle args = new Bundle();
            MovieDetails selectedMovieDetails = getIntent().getParcelableExtra("movieDetails");
            MovieDetailActivityFragment detailActivityFragment = MovieDetailActivityFragment.newInstance(selectedMovieDetails);
            //String sort_criteria = getIntent().getStringExtra("sort_criteria");
            //args.putString("sort_criteria",sort_criteria);
            args.putParcelable("movieDetails", selectedMovieDetails);
            detailActivityFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().add(R.id.movie_detail_container, detailActivityFragment).commit();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle(Html.fromHtml("<font face=serif><b>&nbsp;&nbsp;Movie Details</b></font>"));
        //getSupportActionBar().setIcon(R.drawable.ic_action_logo);


    }

}
