package projects.nanodegree.udacity.nisha.com.popularmoviestage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.OnGridViewSelectedListener {

    private boolean mTwoPane;
    private String DETAIL_FRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(Html.fromHtml("<font face=serif><b>&nbsp;&nbsp;Popular Movies</b></font>"));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_action_logo);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;

            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.movie_detail_container, new MovieDetailActivityFragment(), DETAIL_FRAGMENT_TAG);
            transaction.commit();
        } else {
            mTwoPane = false;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    Handle callback interface method for fragment communication in both one pane and two pane mode
     */
    @Override
    public void onGridItemSelected(MovieDetails movieDetails) {



        if (mTwoPane) {

            MovieDetailActivityFragment detailActivityFragment = (MovieDetailActivityFragment) getSupportFragmentManager().findFragmentByTag(DETAIL_FRAGMENT_TAG);

            detailActivityFragment.updateUI(movieDetails);

        } else {
            //Phone

            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra("movieDetails", movieDetails);
            startActivity(intent);
        }
    }
}
