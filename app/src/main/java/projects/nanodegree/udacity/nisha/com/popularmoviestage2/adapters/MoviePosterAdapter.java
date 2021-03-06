package projects.nanodegree.udacity.nisha.com.popularmoviestage2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.R;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.rest.model.MovieDetails;

/**
 * Created by shani on 2/24/16.
 */
public class MoviePosterAdapter extends ArrayAdapter<MovieDetails> {

    private ArrayList<MovieDetails> mMovieDetails;
    private Context mContext;
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    public MoviePosterAdapter(Context context, int resource, ArrayList<MovieDetails> movieDetails) {
        super(context, resource);
        mContext = context;
        mMovieDetails = movieDetails;

    }

    static class ViewHolder {

        @Bind(R.id.movie_poster_imageView)
        ImageView moviePosterImageView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);

        }
    }

    @Override
    public int getCount() {
        return mMovieDetails.size();
    }

    @Override
    public MovieDetails getItem(int position) {
        return mMovieDetails.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.row_movie_poster, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(mContext).load(POSTER_BASE_URL + mMovieDetails.get(position).getPoster_path())
                //.placeholder(contact_pics[position])
                //.error(R.mipmap.ic_launcher)
                .into(viewHolder.moviePosterImageView);

        return convertView;


    }
}
