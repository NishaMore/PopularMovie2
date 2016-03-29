package projects.nanodegree.udacity.nisha.com.popularmoviestage2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import projects.nanodegree.udacity.nisha.com.popularmoviestage2.data.FavMovieContract.FavMovieEntry;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.data.FavMovieContract.ReviewEntry;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.data.FavMovieContract.TrailerEntry;

/**
 * Created by shani on 3/20/16.
 */
public class FavMovieDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "movies.db";
    private static final int DB_VERSION = 1;

    private static final String CREATE_TABLE_FAV_MOVIE = "CREATE TABLE " + FavMovieEntry.TABLE_NAME +
            "(" + FavMovieEntry.COLUMN_MOVIE_ID + " TEXT PRIMARY KEY ," +
            FavMovieEntry.COLUMN_TITLE + " TEXT NOT NULL," +
            FavMovieEntry.COLUMN_POSETER_PATH + " TEXT NOT NULL," +
            FavMovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL," +
            FavMovieEntry.COLUMN_VOTE_AVG + " TEXT NOT NULL," +
            FavMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
            FavMovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL);";

    private static final String CREATE_TABLE_REVIEWS = "CREATE TABLE " + ReviewEntry.TABLE_NAME +
            "( " + ReviewEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
            ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
            ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
            "FOREIGN KEY ( " + ReviewEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
            FavMovieEntry.TABLE_NAME + " ( " + FavMovieEntry.COLUMN_MOVIE_ID + "));";

    private static final String CREATE_TABLE_TRAILERS = "CREATE TABLE " + TrailerEntry.TABLE_NAME +
            "( " + TrailerEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
            TrailerEntry.COLUMN_KEY + " TEXT NOT NULL, " +
            "FOREIGN KEY ( " + TrailerEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
            FavMovieEntry.TABLE_NAME + " ( " + FavMovieEntry.COLUMN_MOVIE_ID + "));";


    public FavMovieDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FAV_MOVIE);
        db.execSQL(CREATE_TABLE_REVIEWS);
        db.execSQL(CREATE_TABLE_TRAILERS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavMovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TrailerEntry.TABLE_NAME);

        onCreate(db);
    }


}
