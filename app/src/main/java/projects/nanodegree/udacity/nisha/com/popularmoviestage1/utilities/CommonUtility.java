package projects.nanodegree.udacity.nisha.com.popularmoviestage1.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by shani on 3/15/16.
 */
public class CommonUtility {
    public static boolean isNetwrokAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork!=null && activeNetwork.isConnectedOrConnecting();
    }

    public static void showProgressDialog(ProgressDialog mProgressDialog) {

        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

    }
}
