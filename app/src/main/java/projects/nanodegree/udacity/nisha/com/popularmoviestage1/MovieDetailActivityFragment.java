package projects.nanodegree.udacity.nisha.com.popularmoviestage1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    private static final String OUTOFRATE = "/10";
    @Bind(R.id.title_textView)
    TextView title_textView;

    @Bind(R.id.movie_ratingBar)
    RatingBar ratingBar;

    @Bind(R.id.rating_textView)
    TextView vote_avg_textView;

    @Bind(R.id.poster_imageView)
    ImageView movie_thumbnail_imageView;

    @Bind(R.id.overView_textView)
    TextView overview_textView;

    @Bind(R.id.release_date_textView)
    TextView release_date_textView;

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

            ratingBar.setRating(Float.parseFloat(mMovieDetails.getVote_average()));

            vote_avg_textView.setText(Float.parseFloat(mMovieDetails.getVote_average()) + OUTOFRATE);

            Picasso.with(getContext()).load(POSTER_BASE_URL + mMovieDetails.getBackdrop_path())
                    //.placeholder(contact_pics[position])
                    .error(R.mipmap.ic_launcher)
                    .into(movie_thumbnail_imageView);

            overview_textView.setText(mMovieDetails.getPlot_synopsis().toString());
            release_date_textView.setText(mMovieDetails.getRelease_date().toString());
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

        ratingBar.setRating(Float.parseFloat(movieDetails.getVote_average()));

        vote_avg_textView.setText(Float.parseFloat(movieDetails.getVote_average()) + OUTOFRATE);

        Picasso.with(getContext()).load(POSTER_BASE_URL + movieDetails.getPoster_path())
                //.placeholder(contact_pics[position])
                .error(R.mipmap.ic_launcher)
                .into(movie_thumbnail_imageView);

        overview_textView.setText(movieDetails.getPlot_synopsis().toString());
        release_date_textView.setText(movieDetails.getRelease_date().toString());
    }
}
