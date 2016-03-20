package projects.nanodegree.udacity.nisha.com.popularmoviestage1.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import projects.nanodegree.udacity.nisha.com.popularmoviestage1.R;
import projects.nanodegree.udacity.nisha.com.popularmoviestage1.adapters.ReviewAdapter;
import projects.nanodegree.udacity.nisha.com.popularmoviestage1.adapters.TrailerAdapter;
import projects.nanodegree.udacity.nisha.com.popularmoviestage1.rest.RestClient;
import projects.nanodegree.udacity.nisha.com.popularmoviestage1.rest.model.JSONReviewResult;
import projects.nanodegree.udacity.nisha.com.popularmoviestage1.rest.model.JSONTrailerResult;
import projects.nanodegree.udacity.nisha.com.popularmoviestage1.rest.model.MovieDetails;
import projects.nanodegree.udacity.nisha.com.popularmoviestage1.rest.model.ReviewDetails;
import projects.nanodegree.udacity.nisha.com.popularmoviestage1.rest.model.TrailerKeys;
import projects.nanodegree.udacity.nisha.com.popularmoviestage1.rest.service.TMDBServices;
import projects.nanodegree.udacity.nisha.com.popularmoviestage1.utilities.CommonUtility;
import projects.nanodegree.udacity.nisha.com.popularmoviestage1.utilities.RecyclerTouchListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    private static final String OUTOFRATE = "/10";
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


    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/";
    private MovieDetails mMovieDetails;
    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        ButterKnife.bind(this, rootView);


        Bundle bundle = getArguments();
        if (bundle != null) {

            mMovieDetails = bundle.getParcelable("movieDetails");

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


            if (CommonUtility.isNetwrokAvailable(getContext())) {
                final ProgressDialog mProgressDialog = new ProgressDialog(getContext());
                CommonUtility.showProgressDialog(mProgressDialog);

                RestClient restClient = new RestClient();
                TMDBServices trailerService = restClient.getTMDBServices();


                Call<JSONReviewResult> jsonReviewResultCall = trailerService.getReviewResults(mMovieDetails.getId(), "6451f3ced62e3e5be33679f3ea493a41");
                jsonReviewResultCall.enqueue(new Callback<JSONReviewResult>() {
                    @Override
                    public void onResponse(Call<JSONReviewResult> call, Response<JSONReviewResult> response) {
                        JSONReviewResult jsonReviewResult= response.body();
                        ArrayList<ReviewDetails> reviewDetailsList = jsonReviewResult.getResults();
                        ReviewAdapter reviewAdapter = new ReviewAdapter(reviewDetailsList);
                        review_recyclerView.setAdapter(reviewAdapter);

                    }

                    @Override
                    public void onFailure(Call<JSONReviewResult> call, Throwable t) {
                        Log.v("Review",t.getMessage());
                    }
                });






                Call<JSONTrailerResult> jsonTrailerResultCall = trailerService.getTrailerResults(mMovieDetails.getId(),"6451f3ced62e3e5be33679f3ea493a41");
                jsonTrailerResultCall.enqueue(new Callback<JSONTrailerResult>() {

                    @Override
                    public void onResponse(Call<JSONTrailerResult> call, Response<JSONTrailerResult> response) {
                        JSONTrailerResult jsonTrailerResult = response.body();
                        ArrayList<TrailerKeys> trailerKeyses = jsonTrailerResult.getResults();
                        TrailerAdapter trailerAdapter = new TrailerAdapter(trailerKeyses, getContext());
                        trailer_recyclerView.setAdapter(trailerAdapter);

                        setOnItemClickListner(trailer_recyclerView,trailerKeyses);


                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }

                    }

                    private void setOnItemClickListner(RecyclerView recyclerView, final ArrayList<TrailerKeys> trailerKeyses) {
                        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                final String YOUTUBE_BASE_URL = "http://www.youtube.com";
                                Intent viewIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(YOUTUBE_BASE_URL+"/watch?v="+trailerKeyses.get(position).getKey()));
                                startActivity(viewIntent);
                            }

                        }));
                    }

                    @Override
                    public void onFailure(Call<JSONTrailerResult> call, Throwable t) {
                        Log.v("MD", t.getMessage());
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                    }
                });
            }else{
                Toast.makeText(getContext(), "Please check the internet connection and try again", Toast.LENGTH_LONG).show();
            }

            }
            if(savedInstanceState!=null){
                mMovieDetails = savedInstanceState.getParcelable("movieDetails");
                updateUI(mMovieDetails);
            }
            return rootView;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            outState.putParcelable("movieDetails",mMovieDetails);
            super.onSaveInstanceState(outState);
        }

    public void updateUI(MovieDetails movieDetails) {

        mMovieDetails = movieDetails;

        title_textView.setText(movieDetails.getTitle());


        vote_avg_textView.setText(Float.parseFloat(movieDetails.getVote_average()) + OUTOFRATE);

        Picasso.with(getContext()).load(POSTER_BASE_URL + movieDetails.getPoster_path())
                //.placeholder(contact_pics[position])
                .error(R.mipmap.ic_launcher)
                .into(movie_thumbnail_imageView);

        overview_textView.setText(movieDetails.getPlot_synopsis().toString());
        release_date_textView.setText(movieDetails.getRelease_date().toString());
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


}
