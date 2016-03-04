package projects.nanodegree.udacity.nisha.com.popularmoviestage1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shani on 2/24/16.
 */
public class MovieDetails implements Parcelable {

    private String id;
    private String title;
    private String release_date;
    private String poster_path;
    private String vote_average;
    private String plot_synopsis;
    private String backdrop_path;



    public MovieDetails() {
    }

    protected MovieDetails(Parcel in) {
        id = in.readString();
        title = in.readString();
        release_date = in.readString();
        poster_path = in.readString();
        vote_average = in.readString();
        plot_synopsis = in.readString();
        backdrop_path = in.readString();
    }

    public static final Creator<MovieDetails> CREATOR = new Creator<MovieDetails>() {
        @Override
        public MovieDetails createFromParcel(Parcel in) {
            return new MovieDetails(in);
        }

        @Override
        public MovieDetails[] newArray(int size) {
            return new MovieDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getId());
        dest.writeString(getTitle());
        dest.writeString(getRelease_date());
        dest.writeString(getPoster_path());
        dest.writeString(getVote_average());
        dest.writeString(getPlot_synopsis());
        dest.writeString(getBackdrop_path());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getPlot_synopsis() {
        return plot_synopsis;
    }

    public void setPlot_synopsis(String plot_synopsis) {
        this.plot_synopsis = plot_synopsis;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }
}
