package projects.nanodegree.udacity.nisha.com.popularmoviestage2.ui.fragments;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.R;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.adapters.ReviewAdapter;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.adapters.TrailerAdapter;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.data.FavMovieContract.FavMovieEntry;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.data.FavMovieContract.ReviewEntry;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.data.FavMovieContract.TrailerEntry;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.rest.RestClient;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.rest.model.JSONReviewResult;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.rest.model.JSONTrailerResult;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.rest.model.MovieDetails;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.rest.model.ReviewDetails;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.rest.model.TrailerDetails;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.rest.service.TMDBServices;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.utilities.CommonUtility;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.utilities.RecyclerTouchListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment containing a details of selected movie like movie poster, title , release date ,...
 */


public class MovieDetailActivityFragment extends Fragment {

    private static final String OUTOFRATE = "/10";

    @Bind(R.id.main_layout)
    LinearLayout main_layout;

    @Bind(R.id.title_textView)
    TextView title_textView;

    @Bind(R.id.rating_textView)
    TextView vote_avg_textView;

    @Bind(R.id.backdrop_imageView)
    ImageView backdrop_imageView;


    @Bind(R.id.poster_imageView)
    ImageView movie_thumbnail_imageView;

    @Bind(R.id.overView_textView)
    TextView overview_textView;

    @Bind(R.id.release_date_textView)
    TextView release_date_textView;

    @Bind(R.id.trailer_recyclerView)
    RecyclerView trailer_recyclerView;

    @Bind(R.id.review_recyclerView)
    RecyclerView review_recyclerView;

    @Bind(R.id.trailer_label_tv)
    TextView label_trailer;

    @Bind(R.id.review_lable_tv)
    TextView label_review;

    @Bind(R.id.reviewStatus)
    TextView reviewStatusTextview;


    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/";
    private MovieDetails mMovieDetails;
    ArrayList<String> titleList = new ArrayList<>();
    private ArrayList<ReviewDetails> reviewDetailsList;
    private ArrayList<TrailerDetails> trailerDetailsList;
    private boolean isFavorite;

    public MovieDetailActivityFragment() {
    }

    public static MovieDetailActivityFragment newInstance(MovieDetails movieDetails) {
        MovieDetailActivityFragment detailFragment = new MovieDetailActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("movieDetails", movieDetails);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        ButterKnife.bind(this, rootView);

        Bundle bundle = getArguments();
        if (bundle != null) {

            mMovieDetails = bundle.getParcelable("movieDetails");
        } else if (savedInstanceState != null) {
            mMovieDetails = savedInstanceState.getParcelable("movieDetails");
        }

        if (mMovieDetails != null) {
            showMovieDetails(mMovieDetails);
        }

        setHasOptionsMenu(true);
        return rootView;
    }

    private void showMovieDetails(MovieDetails mMovieDetails) {

        main_layout.setVisibility(View.VISIBLE);

        title_textView.setText(mMovieDetails.getTitle());

        vote_avg_textView.setText(Float.parseFloat(mMovieDetails.getVote_average()) + OUTOFRATE);

        Picasso.with(getContext()).load(POSTER_BASE_URL + mMovieDetails.getBackdrop_path())
                //.placeholder(contact_pics[position])
                .error(R.mipmap.ic_launcher)
                .into(backdrop_imageView);

        Picasso.with(getContext()).load(POSTER_BASE_URL + mMovieDetails.getPoster_path())
                //.placeholder(contact_pics[position])
                .error(R.mipmap.ic_launcher)
                .into(movie_thumbnail_imageView);

        overview_textView.setText(mMovieDetails.getPlot_synopsis().toString());
        release_date_textView.setText(mMovieDetails.getRelease_date().toString());

        //1. Set the layout manager to recycler view
        LinearLayoutManager trailer_llm = new LinearLayoutManager(getContext());
        trailer_llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        trailer_recyclerView.setLayoutManager(trailer_llm);
        trailer_recyclerView.setItemAnimator(new DefaultItemAnimator());

        label_trailer.setText(getResources().getString(R.string.label_trailer));

        LinearLayoutManager review_llm = new LinearLayoutManager(getContext());
        review_recyclerView.setLayoutManager(review_llm);
        review_recyclerView.setItemAnimator(new DefaultItemAnimator());

        label_review.setText(getResources().getString(R.string.label_review));

        Cursor favMovieCursor = getContext().getContentResolver().query(FavMovieEntry.CONTENT_URI, null, FavMovieEntry.COLUMN_MOVIE_ID + " = ?", new String[]{mMovieDetails.getId()}, null);
        favMovieCursor.moveToFirst();
        if (favMovieCursor.getCount() > 0) {
            fetchReviewDetails();
            fetchTrailerDetails();
        } else {
            if (CommonUtility.isNetwrokAvailable(getContext())) {
                final ProgressDialog mProgressDialog = new ProgressDialog(getContext());
                CommonUtility.showProgressDialog(mProgressDialog);
                RestClient restClient = new RestClient();
                TMDBServices trailerService = restClient.getTMDBServices();


                Call<JSONReviewResult> jsonReviewResultCall = trailerService.getReviewResults(mMovieDetails.getId(), CommonUtility.MY_API_KEY);
                jsonReviewResultCall.enqueue(new Callback<JSONReviewResult>() {
                    @Override
                    public void onResponse(Call<JSONReviewResult> call, Response<JSONReviewResult> response) {
                        JSONReviewResult jsonReviewResult = response.body();
                        reviewDetailsList = jsonReviewResult.getResults();
                        if (reviewDetailsList == null || reviewDetailsList.size() == 0) {
                            reviewStatusTextview.setText(getActivity().getResources().getString(R.string.msg_reviews_status));
                        } else {
                            ReviewAdapter reviewAdapter = new ReviewAdapter(reviewDetailsList);
                            review_recyclerView.setAdapter(reviewAdapter);
                        }

                    }

                    @Override
                    public void onFailure(Call<JSONReviewResult> call, Throwable t) {
                    }
                });


                Call<JSONTrailerResult> jsonTrailerResultCall = trailerService.getTrailerResults(mMovieDetails.getId(), CommonUtility.MY_API_KEY);
                jsonTrailerResultCall.enqueue(new Callback<JSONTrailerResult>() {

                    @Override
                    public void onResponse(Call<JSONTrailerResult> call, Response<JSONTrailerResult> response) {
                        JSONTrailerResult jsonTrailerResult = response.body();
                        trailerDetailsList = jsonTrailerResult.getResults();
                        TrailerAdapter trailerAdapter = new TrailerAdapter(trailerDetailsList, getContext());
                        trailer_recyclerView.setAdapter(trailerAdapter);

                        setOnItemClickListner(trailer_recyclerView, trailerDetailsList);
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<JSONTrailerResult> call, Throwable t) {
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                    }
                });
            } else {

                Toast.makeText(getContext(), "Please check the internet connection and try again", Toast.LENGTH_LONG).show();
            }
        }

    }


    private void setOnItemClickListner(RecyclerView recyclerView, final ArrayList<TrailerDetails> trailerKeyses) {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final String YOUTUBE_BASE_URL = "http://www.youtube.com";
                Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + "/watch?v=" + trailerKeyses.get(position).getKey()));
                startActivity(viewIntent);
            }

        }));
    }

    private int fetchTrailerDetails() {
        Cursor trailerCursor = getContext().getContentResolver().query(TrailerEntry.CONTENT_URI, null, null, new String[]{mMovieDetails.getId()}, null);
        trailerDetailsList = new ArrayList<>();

        if (trailerCursor.getCount() > 0) {
            trailerCursor.moveToFirst();
            do {
                TrailerDetails trailerDetails = new TrailerDetails();
                trailerDetails.setId(trailerCursor.getString(0));
                trailerDetails.setKey(trailerCursor.getString(1));
                trailerDetailsList.add(trailerDetails);
            } while (trailerCursor.moveToNext());
            TrailerAdapter trailerAdapter = new TrailerAdapter(trailerDetailsList, getContext());
            setOnItemClickListner(trailer_recyclerView, trailerDetailsList);

            trailer_recyclerView.setAdapter(trailerAdapter);

        }
        return trailerCursor.getCount();
    }

    private int fetchReviewDetails() {
        Cursor reviewCursor = getContext().getContentResolver().query(ReviewEntry.CONTENT_URI, null, null, new String[]{mMovieDetails.getId()}, null);
        ArrayList<ReviewDetails> reviewDetailsList = new ArrayList<>();

        if (reviewCursor.getCount() > 0) {
            reviewCursor.moveToFirst();
            do {
                ReviewDetails reviewDetails = new ReviewDetails();
                reviewDetails.setId(reviewCursor.getString(0));
                reviewDetails.setAuthor(reviewCursor.getString(1));
                reviewDetails.setContent(reviewCursor.getString(2));
                reviewDetailsList.add(reviewDetails);
            } while (reviewCursor.moveToNext());
            ReviewAdapter reviewAdapter = new ReviewAdapter(reviewDetailsList);
            review_recyclerView.setAdapter(reviewAdapter);
        } else {
            reviewStatusTextview.setVisibility(View.VISIBLE);
            reviewStatusTextview.setText(getActivity().getResources().getString(R.string.msg_reviews_status));
        }
        return reviewCursor.getCount();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_movie_detail, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                if (mMovieDetails != null) {
                    insertFavoriteMovieDetails();
                    if (isFavorite) {
                        item.setIcon(R.drawable.ic_action_favorite);
                    } else {
                        item.setIcon(R.drawable.ic_action_unfavorite);
                    }
                } else {
                    Toast.makeText(getContext(), "Please select some movie poster to add in favorite list", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.action_share:
                if (mMovieDetails != null) {
                    shareFirstTrailer();
                } else {
                    Toast.makeText(getContext(), "Please select some movie poster to share it's first trailer", Toast.LENGTH_LONG).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem favMenuItem = menu.findItem(R.id.action_favorite);
        if (mMovieDetails == null) {
            favMenuItem.setIcon(R.drawable.ic_action_unfavorite);
            //isFavorite = false;

        } else {
            Cursor favMovieCursor = getContext().getContentResolver().query(FavMovieEntry.CONTENT_URI,
                    new String[]{FavMovieEntry.COLUMN_MOVIE_ID}, FavMovieEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{mMovieDetails.getId()}, null);
            if (favMovieCursor.moveToFirst()) {
                favMenuItem.setIcon(R.drawable.ic_action_favorite);
                isFavorite = true;
            } else {
                favMenuItem.setIcon(R.drawable.ic_action_unfavorite);
                isFavorite = false;
            }

            favMovieCursor.close();
        }

        super.onPrepareOptionsMenu(menu);
    }

    private void shareFirstTrailer() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "First Trailer URL");
        share.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + trailerDetailsList.get(0).getKey());

        startActivity(Intent.createChooser(share, "Share Trailer URL!"));
    }

    private void insertFavoriteMovieDetails() {

        if (isFavorite) {
            int trailerRowsDeleted = getContext().getContentResolver().delete(TrailerEntry.CONTENT_URI, TrailerEntry.COLUMN_MOVIE_ID + " = ? ", new String[]{mMovieDetails.getId()});
            int reviewRowsDeleted = getContext().getContentResolver().delete(ReviewEntry.CONTENT_URI, ReviewEntry.COLUMN_MOVIE_ID + " = ? ", new String[]{mMovieDetails.getId()});
            int rowsDeleted = getContext().getContentResolver().delete(FavMovieEntry.CONTENT_URI, FavMovieEntry.COLUMN_MOVIE_ID + " = ? ", new String[]{mMovieDetails.getId()});

            Toast.makeText(getContext(), "Removed from favorite movies list", Toast.LENGTH_LONG).show();
            isFavorite = false;
        } else {
            insertFavMovieDetails();
            insertFavMovieReviewDetails();
            insertFavMovieTrailerDetails();
            Toast.makeText(getContext(), "Added to favorite movies list", Toast.LENGTH_LONG).show();
            isFavorite = true;

        }
    }

    private void insertFavMovieTrailerDetails() {
        if (trailerDetailsList != null) {
            for (int i = 0; i < trailerDetailsList.size(); i++) {
                ContentValues trailerContentValues = new ContentValues();
                trailerContentValues.put(TrailerEntry.COLUMN_MOVIE_ID, mMovieDetails.getId());
                trailerContentValues.put(TrailerEntry.COLUMN_KEY, trailerDetailsList.get(i).getKey());
                Uri trailerUri = getContext().getContentResolver().insert(TrailerEntry.CONTENT_URI, trailerContentValues);
            }
        }
    }

    private void insertFavMovieReviewDetails() {
        if (reviewDetailsList != null) {
            for (int i = 0; i < reviewDetailsList.size(); i++) {
                ContentValues reviewContentValues = new ContentValues();
                reviewContentValues.put(ReviewEntry.COLUMN_MOVIE_ID, mMovieDetails.getId());
                reviewContentValues.put(ReviewEntry.COLUMN_AUTHOR, reviewDetailsList.get(i).getAuthor());
                reviewContentValues.put(ReviewEntry.COLUMN_CONTENT, reviewDetailsList.get(i).getContent());
                Uri reviewUri = getContext().getContentResolver().insert(ReviewEntry.CONTENT_URI, reviewContentValues);
            }
        }

    }

    private void insertFavMovieDetails() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavMovieEntry.COLUMN_MOVIE_ID, mMovieDetails.getId());
        contentValues.put(FavMovieEntry.COLUMN_TITLE, mMovieDetails.getTitle());
        contentValues.put(FavMovieEntry.COLUMN_POSETER_PATH, mMovieDetails.getPoster_path());
        contentValues.put(FavMovieEntry.COLUMN_OVERVIEW, mMovieDetails.getPlot_synopsis());
        contentValues.put(FavMovieEntry.COLUMN_VOTE_AVG, mMovieDetails.getVote_average());
        contentValues.put(FavMovieEntry.COLUMN_RELEASE_DATE, mMovieDetails.getRelease_date());
        contentValues.put(FavMovieEntry.COLUMN_BACKDROP_PATH, mMovieDetails.getBackdrop_path());
        Uri movieUri = getContext().getContentResolver().insert(FavMovieEntry.CONTENT_URI, contentValues);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("movieDetails", mMovieDetails);
        super.onSaveInstanceState(outState);
    }



  /*  public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
*/

}
