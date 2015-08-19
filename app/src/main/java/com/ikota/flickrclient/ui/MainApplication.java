package com.ikota.flickrclient.ui;

import android.app.Application;
import android.util.Log;

import com.ikota.flickrclient.network.retrofit.FlickrService;
import com.ikota.flickrclient.network.retrofit.FlickrURL;

import retrofit.RestAdapter;

/**
 * Created by kota on 2015/08/19.
 *
 */
public class MainApplication extends Application {

    private static final String TAG = MainApplication.class.getSimpleName();

    //private static Gson GSON = new Gson();

    private static RestAdapter REST_ADAPTER =
            new RestAdapter.Builder().setEndpoint(FlickrURL.END_POINT).build();

    public static FlickrService API = REST_ADAPTER.create(FlickrService.class);

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "CustomApplication class is used");
    }

}
