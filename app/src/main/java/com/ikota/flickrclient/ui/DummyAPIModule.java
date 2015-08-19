package com.ikota.flickrclient.ui;

import com.ikota.flickrclient.model.DataHolder;
import com.ikota.flickrclient.network.retrofit.FlickrService;
import com.ikota.flickrclient.network.retrofit.FlickrURL;
import com.ikota.flickrclient.network.retrofit.MockClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;


@Module(
        injects = MainApplication.class,
        library = true
)
public class DummyAPIModule {

    @Provides
    @Singleton
    public FlickrService provideFlickrService() {
        return new RestAdapter
                .Builder()
                .setEndpoint(FlickrURL.END_POINT)
                .setClient(new MockClient(DataHolder.LIST_JSON))
                .build()
                .create(FlickrService.class);
    }

}
