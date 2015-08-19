package com.ikota.flickrclient.ui;

import android.app.Application;

import com.ikota.flickrclient.network.retrofit.FlickrService;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;

/**
 * Created by kota on 2015/08/19.
 *
 */
public class MainApplication extends Application {

    private static final String TAG = MainApplication.class.getSimpleName();

    private ObjectGraph objectGraph = null;

    @Inject FlickrService flickrService;

    @Override
    public void onCreate() {
        super.onCreate();
        if(objectGraph == null) {
            List<FlickrAPIModule> modules = Collections.singletonList(new FlickrAPIModule());
            objectGraph = ObjectGraph.create(modules.toArray());
        }
    }

    // used to set ObjectGraph for test
    public void setObjectGraph(ObjectGraph graph) {
        objectGraph = graph;
    }

    public ObjectGraph objectGraph() {
        return objectGraph;
    }

    public FlickrService api() {
        return flickrService;
    }

}
