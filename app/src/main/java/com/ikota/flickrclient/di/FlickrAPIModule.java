package com.ikota.flickrclient.di;

import com.ikota.flickrclient.network.retrofit.FlickrService;
import com.ikota.flickrclient.network.retrofit.FlickrURL;
import com.ikota.flickrclient.ui.AndroidApplication;

import javax.inject.Singleton;

import dagger.Provides;
import retrofit.RestAdapter;

@dagger.Module(
        injects = AndroidApplication.class,
        library = true
)
public class FlickrAPIModule {

    @Provides @Singleton
    public FlickrService provideFlickrService() {
        return new RestAdapter.Builder()
                .setEndpoint(FlickrURL.END_POINT)
                .build()
                .create(FlickrService.class);
    }

}
