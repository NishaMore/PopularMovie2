package projects.nanodegree.udacity.nisha.com.popularmoviestage1;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String KEY_SCROLL_POSITION = "scroll_position";
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
        outState.putInt(KEY_SCROLL_POSITION,mPosition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        mMoviePosterAdapter = new MoviePosterAdapter(getContext(), R.layout.row_movie_poster, mMovieDetailsList);
        mMovie_poster_gridView.setAdapter(mMoviePosterAdapter);
        if(savedInstanceState!=null){
            mPosition = savedInstanceState.getInt(KEY_SCROLL_POSITION);
            mMovie_poster_gridView.setSelection(mPosition);
        }
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
                if(i==0){
                    //Sorting by popular movies
                    param = "popular";

                }else{
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

        String title = mMoviePosterAdapter.getItem(position).getTitle();
        gridViewSelectedListener = (OnGridViewSelectedListener)getActivity();
        gridViewSelectedListener.onGridItemSelected(mMoviePosterAdapter.getItem(position));
        mPosition = position;
    }


    private void updateMoviePosters(String param) {

        FetchMoviePosterTask moviePosterTask = new FetchMoviePosterTask();
        moviePosterTask.execute(param);

    }

    private class FetchMoviePosterTask extends AsyncTask<String, Integer, MovieDetails[]> {

        private String movieJsonStr;
        private String TAG = "MoviePosterTask";
        private ProgressDialog progressDialog;
        private int j = 0;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getContext(),null, null, true,false);
            progressDialog.setContentView(R.layout.spinning_wheel);

        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }
        @Override
        protected MovieDetails[] doInBackground(String... params) {
            HttpURLConnection httpURLConnection;
            BufferedReader reader;

            try {
                publishProgress(j++);
                //Define query parameter constants
                //full api url = http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=6451f3ced62e3e5be33679f3ea493a41
                final String BASE_URL = "http://api.themoviedb.org/3/movie/"+ params[0] + "?";
                //final String TMDB_SORT_BY = "sort_by";
                final String TMDB_API_KEY = "api_key";


                Uri uri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(TMDB_API_KEY, "MY_API_KEY")
                        .build();

                URL buildUrl = new URL(uri.toString());

                //Open HTTP URL connection on buildUrl
                httpURLConnection = (HttpURLConnection) buildUrl.openConnection();

                //Set http request method
                httpURLConnection.setRequestMethod("GET");

                //Connect
                httpURLConnection.connect();


                //Read JSON string from inputstream

                InputStream inputStream = httpURLConnection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer = new StringBuffer();

                if (reader != null) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                } else {
                    return null;
                }

                if (buffer != null) {
                    movieJsonStr = buffer.toString();
                    return getMovieDetails(movieJsonStr);
                } else {
                    return null;
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(MovieDetails[] movieDetails) {
            super.onPostExecute(movieDetails);

            mMoviePosterAdapter.clear();

            if (movieDetails != null) {
                for (MovieDetails tmpMovieDetails : movieDetails) {
                    //ew  mMoviePosterAdapter.notifyDataSetChanged();
                    Log.v(FetchMoviePosterTask.class.getSimpleName(), tmpMovieDetails.getPoster_path());
                    mMoviePosterAdapter.add(tmpMovieDetails);
                }
                Log.v(TAG, mMoviePosterAdapter.getCount() + "");
                if(progressDialog!=null) {
                    progressDialog.dismiss();
                }
            }
        }

        private MovieDetails[] getMovieDetails(String movieJsonStr) {
            MovieDetails movieDetails[] = null;
            mMovieDetailsList.clear();
            try {
                //Define constants for extracting fields which will come from json string
                final String TMDB_FIELD_MOVIE_ID = "id";
                final String TMDB_FIELD_TITLE = "title";
                final String TMDB_FIELD_RELEASE_DATE = "release_date";
                final String TMDB_FIELD_POSTER_PATH = "poster_path";
                final String TMDB_FIELD_VOTE_AVERAGE = "vote_average";
                final String TMDB_FIELD_PLOT_SYNOPSIS = "overview";
                final String TMDB_FIELD_BACKDROP_PATH = "backdrop_path";

                //Get root object from JSONStr
                JSONObject jsonObject = new JSONObject(movieJsonStr);

                //Get results array
                JSONArray resultsArray = jsonObject.getJSONArray("results");

                //traverse through array
                for (int i = 0; i < resultsArray.length(); i++) {
                    //Extract required fields : Title , release date, poster path, vote average , overview/plot synopsis , movie id,
                    String movie_id = resultsArray.getJSONObject(i).getString(TMDB_FIELD_MOVIE_ID);
                    String title = resultsArray.getJSONObject(i).getString(TMDB_FIELD_TITLE);
                    String release_date = resultsArray.getJSONObject(i).getString(TMDB_FIELD_RELEASE_DATE);
                    String poster_path = resultsArray.getJSONObject(i).getString(TMDB_FIELD_POSTER_PATH);
                    String vote_avg = resultsArray.getJSONObject(i).getString(TMDB_FIELD_VOTE_AVERAGE);
                    String plot_synopsis = resultsArray.getJSONObject(i).getString(TMDB_FIELD_PLOT_SYNOPSIS);
                    String backdrop_path = resultsArray.getJSONObject(i).getString(TMDB_FIELD_BACKDROP_PATH);

                    MovieDetails movieDetail = new MovieDetails();
                    movieDetail.setId(movie_id);
                    movieDetail.setTitle(title);
                    movieDetail.setRelease_date(release_date);
                    movieDetail.setPoster_path(poster_path);
                    movieDetail.setVote_average(vote_avg);
                    movieDetail.setPlot_synopsis(plot_synopsis);
                    movieDetail.setBackdrop_path(backdrop_path);

                    mMovieDetailsList.add(movieDetail);

                }
                movieDetails = mMovieDetailsList.toArray(new MovieDetails[mMovieDetailsList.size()]);


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return movieDetails;
        }


    }
}
