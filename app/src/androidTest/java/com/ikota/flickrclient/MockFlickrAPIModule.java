package com.ikota.flickrclient;

import com.ikota.flickrclient.network.retrofit.FlickrService;
import com.ikota.flickrclient.network.retrofit.FlickrURL;
import com.ikota.flickrclient.ui.MainActivity;

import java.io.IOException;
import java.util.Collections;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by kota on 2015/08/19.
 *
 */
@Module(
        injects = MainActivity.class,
        library = true
)
public class MockFlickrAPIModule {

    private Client client;

    public MockFlickrAPIModule(Client client) {this.client = client;}

    @Provides @Singleton
    public FlickrService provideFlickrService() {
        return new RestAdapter.Builder()
                .setEndpoint(FlickrURL.END_POINT)
                .setClient(client)
                .build()
                .create(FlickrService.class);
    }

    public class MockClient implements Client {

        private static final int HTTP_OK_STATUS = 200;
        private final String LIST_RESPONSE;

        MockClient(String responce_json) {
            this.LIST_RESPONSE = responce_json;
        }

        @Override
        public Response execute(Request request) throws IOException {
            return createResponseWithCodeAndJson(HTTP_OK_STATUS, LIST_RESPONSE);
        }

        private Response createResponseWithCodeAndJson(int responseCode, String json) {
            return new Response("",responseCode, "nothing", Collections.EMPTY_LIST,
                    new TypedByteArray("application/json", json.getBytes()));
        }

    }

}
