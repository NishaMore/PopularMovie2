package projects.nanodegree.udacity.nisha.com.popularmoviestage1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {

            MovieDetailActivityFragment detailActivityFragment = new MovieDetailActivityFragment();

            //set arguments coming from intent
            Bundle args = new Bundle();
            MovieDetails selectedMovieDetails = getIntent().getParcelableExtra("movieDetails");
            args.putParcelable("movieDetails", selectedMovieDetails);
            detailActivityFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().add(R.id.movie_detail_container, detailActivityFragment).commit();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font face=serif><b>&nbsp;&nbsp;Movie Details</b></font>"));
        getSupportActionBar().setIcon(R.drawable.ic_action_logo);


    }

}
