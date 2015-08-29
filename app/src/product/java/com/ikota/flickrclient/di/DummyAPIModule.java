package com.ikota.flickrclient.di;

import com.ikota.flickrclient.network.retrofit.FlickrService;
import com.ikota.flickrclient.network.retrofit.FlickrURL;
import com.ikota.flickrclient.network.retrofit.MockClient;
import com.ikota.flickrclient.ui.AndroidApplication;

import java.util.HashMap;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;


@Module(
        injects = AndroidApplication.class,
        library = true
)
public class DummyAPIModule {

    public HashMap<String, String> RESPONSE_MAP;

    public DummyAPIModule(HashMap<String, String> map) {
        this.RESPONSE_MAP = map;
    }

    @Provides
    @Singleton
    public FlickrService provideFlickrService() {
        return new RestAdapter
                .Builder()
                .setEndpoint(FlickrURL.END_POINT)
                .setClient(new MockClient(RESPONSE_MAP))
                .build()
                .create(FlickrService.class);
    }

}
