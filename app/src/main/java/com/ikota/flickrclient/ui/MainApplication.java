package com.ikota.flickrclient.ui;

import android.app.Application;
import android.util.Log;

import com.ikota.flickrclient.network.retrofit.FlickrService;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;

/**
 * Created by kota on 2015/08/19.
 *
 */
public class MainApplication extends Application {

    private static final String TAG = MainApplication.class.getSimpleName();

    private List<Object> modules;
    @Inject FlickrService flickrService;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "CustomApplication class is used");
        setModules(new FlickrAPIModule());
        ObjectGraph objectGraph = ObjectGraph.create(modules.toArray());
        objectGraph.inject(this);
    }

    public void setModules(Object module) {
        this.modules = Arrays.<Object>asList(module);
    }

    public FlickrService api() {
        return flickrService;
    }

}
