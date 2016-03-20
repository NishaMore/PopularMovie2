package projects.nanodegree.udacity.nisha.com.popularmoviestage1.ui.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import projects.nanodegree.udacity.nisha.com.popularmoviestage1.R;
import projects.nanodegree.udacity.nisha.com.popularmoviestage1.adapters.MoviePosterAdapter;
import projects.nanodegree.udacity.nisha.com.popularmoviestage1.rest.RestClient;
import projects.nanodegree.udacity.nisha.com.popularmoviestage1.rest.model.JSONResult;
import projects.nanodegree.udacity.nisha.com.popularmoviestage1.rest.model.MovieDetails;
import projects.nanodegree.udacity.nisha.com.popularmoviestage1.rest.service.TMDBServices;
import projects.nanodegree.udacity.nisha.com.popularmoviestage1.utilities.CommonUtility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String KEY_SCROLL_POSITION = "scroll_position";
    private static final String MY_API_KEY = "6451f3ced62e3e5be33679f3ea493a41";
    @Bind(R.id.movie_poster_gridView)
    GridView mMovie_poster_gridView;

    private ArrayList<MovieDetails> mMovieDetailsList = new ArrayList<>();
    private String param;
    private MoviePosterAdapter mMoviePosterAdapter;
    private int mPosition;


    //Define callback interface for fragment communication

    public interface OnGridViewSelectedListener {
        void onGridItemSelected(MovieDetails position);
    }

    OnGridViewSelectedListener gridViewSelectedListener;


    public MainActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCROLL_POSITION, mPosition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

      /*  mMoviePosterAdapter = new MoviePosterAdapter(getContext(), R.layout.row_movie_poster, mMovieDetailsList);
        mMovie_poster_gridView.setAdapter(mMoviePosterAdapter);
        if(savedInstanceState!=null){
            mPosition = savedInstanceState.getInt(KEY_SCROLL_POSITION);
            mMovie_poster_gridView.setSelection(mPosition);
        }*/

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        int index;
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = (MenuItem) menu.findItem(R.id.sorting_spinner);

        Spinner sortingSpinner = (Spinner) MenuItemCompat.getActionView(menuItem);

        String[] sortingCriteria = getResources().getStringArray(R.array.sorting_criteria_array);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.sort_spinner, sortingCriteria);
        sortingSpinner.setAdapter(spinnerAdapter);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        param = sharedPreferences.getString(getString(R.string.sorting_criteria_key),getString(R.string.sorting_criteria_default_val));

        if(param!=null && param.equals("top_rated")){
            index = 1;
        }
        else{
            index = 0;
        }

        sortingSpinner.setSelection(index);
        sortingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {
                if (i == 0) {
                    //Sorting by popular movies
                    param = "popular";

                } else {
                    param = "top_rated";
                }
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.sorting_criteria_key), param);
                editor.commit();
                updateMoviePosters(param);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }


        });

        super.onCreateOptionsMenu(menu, inflater);
    }



    @OnItemClick(R.id.movie_poster_gridView)
    public void gridViewItemClick(int position) {

        //String title = mMoviePosterAdapter.getItem(position).getTitle();
        gridViewSelectedListener = (OnGridViewSelectedListener)getActivity();
        gridViewSelectedListener.onGridItemSelected(mMoviePosterAdapter.getItem(position));
        mPosition = position;
    }





    private void updateMoviePosters(String param) {


        if (CommonUtility.isNetwrokAvailable(getContext())) {

            final ProgressDialog mProgressDialog = new ProgressDialog(getContext());
            CommonUtility.showProgressDialog(mProgressDialog);

            RestClient restClient = new RestClient();
            TMDBServices tmdbService = restClient.getTMDBServices();
            Call<JSONResult> jsonResults = tmdbService.getMovieDetails(param, MY_API_KEY);
            jsonResults.enqueue(new Callback<JSONResult>() {
                @Override
                public void onResponse(Call<JSONResult> call, Response<JSONResult> response) {
                    JSONResult results = response.body();
                    List<MovieDetails> movieDetailsList = results.getmResults();
                    Log.e("PP", movieDetailsList.get(0).getTitle());
                    mMoviePosterAdapter = new MoviePosterAdapter(getContext(), R.layout.row_movie_poster, (ArrayList) movieDetailsList);
                    mMovie_poster_gridView.setAdapter(mMoviePosterAdapter);

                    if(mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<JSONResult> call, Throwable t) {
                    Log.e("PP", t.getMessage());
                    if(mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }
                }
            });
        }
        else {
            Toast.makeText(getContext(),"Please check the internet connection and try again",Toast.LENGTH_LONG).show();
        }
    }




}
