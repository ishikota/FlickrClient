package com.ikota.flickrclient.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by kota on 2015/08/12.
 *
 * Receive the event of network state changes
 * and notifies it to passed callback.
 */
public class NetworkReceiver extends BroadcastReceiver{
    private static final String TAG = NetworkReceiver.class.getSimpleName();

    public interface OnNetworkStateChangedListener {
        void changedToWifi();
        void changedToMobile();
        void changedToOffline();
    }
    private OnNetworkStateChangedListener mCallback;

    // Empty constructor wolud be needed for the case
    // declares this receiver in manifest file.
    @SuppressWarnings("unused")
    public NetworkReceiver() {}

    public NetworkReceiver(OnNetworkStateChangedListener listener) {
        this.mCallback = listener;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conn = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            if(mCallback!=null) mCallback.changedToWifi();
            Log.i(TAG, "Network state has changed to WIFI");
        } else if (networkInfo != null) {
            if(mCallback!=null) mCallback.changedToMobile();
            Log.i(TAG, "Network state has changed to MOBILE");
        } else {
            if(mCallback!=null) mCallback.changedToOffline();
            Log.i(TAG, "I have no idea...");
        }
    }

}
