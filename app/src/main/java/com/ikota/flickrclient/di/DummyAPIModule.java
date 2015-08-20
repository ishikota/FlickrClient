package com.ikota.flickrclient.di;

import com.ikota.flickrclient.network.retrofit.FlickrService;
import com.ikota.flickrclient.network.retrofit.FlickrURL;
import com.ikota.flickrclient.network.retrofit.MockClient;
import com.ikota.flickrclient.ui.AndroidApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;


@Module(
        injects = AndroidApplication.class,
        library = true
)
public class DummyAPIModule {

    public String mock_response;

    public DummyAPIModule(String mock_response) {
        this.mock_response = mock_response;
    }

    @Provides
    @Singleton
    public FlickrService provideFlickrService() {
        return new RestAdapter
                .Builder()
                .setEndpoint(FlickrURL.END_POINT)
                .setClient(new MockClient(mock_response))
                .build()
                .create(FlickrService.class);
    }

}
