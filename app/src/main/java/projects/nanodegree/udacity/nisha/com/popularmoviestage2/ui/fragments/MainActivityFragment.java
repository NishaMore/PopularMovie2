package projects.nanodegree.udacity.nisha.com.popularmoviestage2.ui.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.R;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.adapters.MoviePosterAdapter;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.data.FavMovieContract.FavMovieEntry;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.rest.RestClient;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.rest.model.JSONResult;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.rest.model.MovieDetails;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.rest.service.TMDBServices;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.utilities.CommonUtility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {


    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    private static final String FAVORITE = "favorite";

    @Bind(R.id.movie_poster_gridView)
    GridView mMovie_poster_gridView;

    private String param;
    private MoviePosterAdapter mMoviePosterAdapter;
    private static int mPosition;

    private int mActivatedPosition = GridView.INVALID_POSITION;


    //Define callback interface for fragment communication

    public interface OnGridViewSelectedListener {
        void onGridItemSelected(MovieDetails position);
    }

    OnGridViewSelectedListener gridViewSelectedListener;


    public MainActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onStart() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        param = sharedPreferences.getString(getString(R.string.sorting_criteria_key), getString(R.string.sorting_criteria_default_val));
        if (param.equals(FAVORITE)) {
            updateFavoriteMoviePosters();
        } else {
            updateMoviePosters(param);
        }
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().invalidateOptionsMenu();

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            //mMovie_poster_gridView.setSelection(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
            //mMovie_poster_gridView.smoothScrollToPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
            mPosition = savedInstanceState.getInt(STATE_ACTIVATED_POSITION);
        }

        return rootView;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        if (savedInstanceState != null && mPosition != GridView.INVALID_POSITION) {
            mMovie_poster_gridView.setSelection(mPosition);
            mMovie_poster_gridView.smoothScrollToPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));

        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortBy_popular:
                param = POPULAR;
                updateMoviePosters(param);
                saveSortingCriteria();
                item.setChecked(true);
                return true;
            case R.id.sortBy_top_rated:
                param = TOP_RATED;
                updateMoviePosters(param);
                saveSortingCriteria();
                item.setChecked(true);
                return true;
            case R.id.sortBy_favorite:
                param = FAVORITE;
                updateFavoriteMoviePosters();
                saveSortingCriteria();
                item.setChecked(true);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void saveSortingCriteria() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.sorting_criteria_key), param);
        editor.commit();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String param = sharedPreferences.getString(getString(R.string.sorting_criteria_key), POPULAR);
        if (param.equals(POPULAR)) {
            menu.findItem(R.id.sortBy_popular).setChecked(true);
        } else if (param.equals(TOP_RATED)) {
            menu.findItem(R.id.sortBy_top_rated).setChecked(true);
        } else if (param.equals(FAVORITE)) {
            menu.findItem(R.id.sortBy_favorite).setChecked(true);
        }
        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);

    } /*{

        int index = 0;
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = (MenuItem) menu.findItem(R.id.sorting_spinner);

        Spinner sortingSpinner = (Spinner) MenuItemCompat.getActionView(menuItem);

        String[] sortingCriteria = getResources().getStringArray(R.array.sorting_criteria_array);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.sort_spinner, sortingCriteria);
        sortingSpinner.setAdapter(spinnerAdapter);
        sortingSpinner.setBackgroundColor(Color.BLACK);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        param = sharedPreferences.getString(getString(R.string.sorting_criteria_key),getString(R.string.sorting_criteria_default_val));
        if(param!=null){
            if(param.equals(POPULAR)) {
                    index = 0;
                }
                else if(param.equals(TOP_RATED)){
                    index = 1;
                }
                else{
                    index = 2;
                }
            }

            sortingSpinner.setSelection(index);
            sortingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> adapterView,
                                           View view, int i, long l) {
                    if (i == 0) {
                        //Sorting by popular movies
                        param = POPULAR;

                    } else if (i == 1) {
                        param = TOP_RATED;
                    } else {
                        param = FAVORITE;
                    }
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getString(R.string.sorting_criteria_key), param);
                    editor.commit();
                    if (param.equals(FAVORITE)) {
                        updateFavoriteMoviePosters();
                    } else {
                        updateMoviePosters(param);
                    }


                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }


            });

            super.onCreateOptionsMenu(menu, inflater);
        }*/


    private void updateFavoriteMoviePosters() {
        Cursor favMovieCursor = getContext().getContentResolver().query(FavMovieEntry.CONTENT_URI, null, null, null, null);

        ArrayList<MovieDetails> movieDetailsList = new ArrayList<>();


        if (favMovieCursor.getCount() > 0) {
            favMovieCursor.moveToFirst();
            do {
                MovieDetails movieDetails = new MovieDetails();
                movieDetails.setId(favMovieCursor.getString(0));
                movieDetails.setTitle(favMovieCursor.getString(1));
                movieDetails.setPoster_path(favMovieCursor.getString(2));
                movieDetails.setPlot_synopsis(favMovieCursor.getString(3));
                movieDetails.setVote_average(favMovieCursor.getString(4));
                movieDetails.setRelease_date(favMovieCursor.getString(5));
                movieDetails.setBackdrop_path(favMovieCursor.getString(6));

                movieDetailsList.add(movieDetails);

            } while (favMovieCursor.moveToNext());

        }
        mMoviePosterAdapter = new MoviePosterAdapter(getContext(), R.layout.row_movie_poster, (ArrayList) movieDetailsList);
        mMovie_poster_gridView.setAdapter(mMoviePosterAdapter);
    }

    @OnItemClick(R.id.movie_poster_gridView)
    public void gridViewItemClick(int position) {
        mActivatedPosition = position;
        //String title = mMoviePosterAdapter.getItem(position).getTitle();
        gridViewSelectedListener = (OnGridViewSelectedListener) getActivity();
        gridViewSelectedListener.onGridItemSelected(mMoviePosterAdapter.getItem(position));
        mPosition = position;
    }


    private void updateMoviePosters(String param) {


        if (CommonUtility.isNetwrokAvailable(getContext())) {

            final ProgressDialog mProgressDialog = new ProgressDialog(getContext());
            CommonUtility.showProgressDialog(mProgressDialog);

            RestClient restClient = new RestClient();
            TMDBServices tmdbService = restClient.getTMDBServices();
            Call<JSONResult> jsonResults = tmdbService.getMovieDetails(param, CommonUtility.MY_API_KEY);
            jsonResults.enqueue(new Callback<JSONResult>() {
                @Override
                public void onResponse(Call<JSONResult> call, Response<JSONResult> response) {
                    JSONResult results = response.body();
                    if(results!=null) {
                        List<MovieDetails> movieDetailsList = results.getmResults();
                        mMoviePosterAdapter = new MoviePosterAdapter(getContext(), R.layout.row_movie_poster, (ArrayList) movieDetailsList);
                        mMovie_poster_gridView.setAdapter(mMoviePosterAdapter);
                    }
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<JSONResult> call, Throwable t) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                }
            });

        } else {
            mMoviePosterAdapter = new MoviePosterAdapter(getContext(), R.layout.row_movie_poster, new ArrayList<MovieDetails>());
            mMoviePosterAdapter.clear();
            mMovie_poster_gridView.setAdapter(mMoviePosterAdapter);

            Toast.makeText(getContext(), "Please check the internet connection and try again", Toast.LENGTH_LONG).show();
        }

    }


}
