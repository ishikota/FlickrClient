package com.ikota.flickrclient.ui;

import com.ikota.flickrclient.network.retrofit.FlickrService;
import com.ikota.flickrclient.network.retrofit.FlickrURL;

import javax.inject.Singleton;

import dagger.Provides;
import retrofit.RestAdapter;

/**
 * Created by kota on 2015/08/19.
 *
 */
@dagger.Module(
        injects = MainApplication.class,
        library = true
)
public class FlickrAPIModule {

    private static RestAdapter REST_ADAPTER =
            new RestAdapter.Builder().setEndpoint(FlickrURL.END_POINT).build();

    public static FlickrService API = REST_ADAPTER.create(FlickrService.class);

    @Provides @Singleton
    public FlickrService provideFlickrService() {
        return API;
    }

}
