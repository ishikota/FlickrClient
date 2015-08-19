package com.ikota.flickrclient;

import com.ikota.flickrclient.network.retrofit.FlickrService;

import org.mockito.Mockito;

import dagger.Provides;

/**
 * Created by kota on 2015/08/19.
 *
 */
public class MockFlickrAPIModule {
    @Provides
    public FlickrService provideFlickrService() {
        return Mockito.mock(FlickrService.class);
    }

}
