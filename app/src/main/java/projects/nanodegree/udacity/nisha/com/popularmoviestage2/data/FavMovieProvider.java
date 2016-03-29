package projects.nanodegree.udacity.nisha.com.popularmoviestage2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import projects.nanodegree.udacity.nisha.com.popularmoviestage2.data.FavMovieContract.FavMovieEntry;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.data.FavMovieContract.ReviewEntry;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.data.FavMovieContract.TrailerEntry;

/**
 * Created by shani on 3/20/16.
 */
public class FavMovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavMovieDbHelper dbHelper;
    static final int FAV_MOVIE = 100;
    static final int REVIEW = 200;
    static final int TRAILER = 300;

    private static final String selectionReviewByMovieId = ReviewEntry.TABLE_NAME + "." + ReviewEntry.COLUMN_MOVIE_ID + " = ?";
    private static final String selectionTrailerById = TrailerEntry.TABLE_NAME + "." + TrailerEntry.COLUMN_MOVIE_ID + " = ?";


    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = FavMovieContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, FavMovieContract.PATH_FAV_MOVIE, FAV_MOVIE);

        uriMatcher.addURI(authority, FavMovieContract.PATH_REVIEWS, REVIEW);

        uriMatcher.addURI(authority, FavMovieContract.PATH_TRAILERS, TRAILER);


        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new FavMovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAV_MOVIE:
                qb.setTables(FavMovieEntry.TABLE_NAME);
                break;
            case REVIEW:
                qb.setTables(ReviewEntry.TABLE_NAME);
                selection = selectionReviewByMovieId;
                break;
            case TRAILER:
                qb.setTables(TrailerEntry.TABLE_NAME);
                selection = selectionTrailerById;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cursor = qb.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAV_MOVIE:
                return FavMovieContract.FavMovieEntry.CONTENT_TYPE;
            case REVIEW:
                return ReviewEntry.CONTENT_TYPE;
            case TRAILER:
                return TrailerEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        long id = 0;
        Uri CONTENT_URI;
        Uri returnUri = null;
        switch (uriType) {
            case FAV_MOVIE:
                id = sqlDB.insert(FavMovieEntry.TABLE_NAME, null, values);
                CONTENT_URI = FavMovieEntry.CONTENT_URI;
                break;
            case REVIEW:
                id = sqlDB.insert(ReviewEntry.TABLE_NAME, null, values);
                CONTENT_URI = ReviewEntry.CONTENT_URI;
                break;
            case TRAILER:
                id = sqlDB.insert(TrailerEntry.TABLE_NAME, null, values);
                CONTENT_URI = TrailerEntry.CONTENT_URI;
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        if (id > 0) {
            returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(returnUri, null);
        }
        return returnUri;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted = 0;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case FAV_MOVIE:
                rowsDeleted = db.delete(FavMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REVIEW:
                rowsDeleted = db.delete(ReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TRAILER:
                rowsDeleted = db.delete(TrailerEntry.TABLE_NAME, selection, selectionArgs);
                break;
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
