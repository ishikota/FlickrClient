package com.ikota.flickrclient.network.retrofit;

import java.io.IOException;
import java.util.Collections;

import retrofit.client.Client;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;


public class MockClient implements Client{
    private static final int HTTP_OK_STATUS = 200;
    private final String LIST_RESPONSE;

    public MockClient(String responce_json) {
        this.LIST_RESPONSE = responce_json;
    }

    @Override
    public Response execute(Request request) throws IOException {
        try {
            Thread.sleep(2000);  // simulates server access
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return createResponseWithCodeAndJson(HTTP_OK_STATUS, LIST_RESPONSE);
    }

    private Response createResponseWithCodeAndJson(int responseCode, String json) {
        return new Response("",responseCode, "nothing", Collections.EMPTY_LIST,
                new TypedByteArray("application/json", json.getBytes()));
    }
}
