package com.ikota.flickrclient.network.retrofit;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.client.Client;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;


public class MockClient implements Client{

    private static final int HTTP_OK_STATUS = 200;
    private static final Pattern PATTERN_METHOD = Pattern.compile("method=(.*)&format");

    private final HashMap<String, String> RESPONSE_MAP;

    public MockClient(HashMap<String, String> map) {
        this.RESPONSE_MAP = map;
    }

    @Override
    public Response execute(Request request) throws IOException {
        try {
            Thread.sleep(1000);  // simulates server access
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String key = retrieveMethod(request.getUrl());
        String json = RESPONSE_MAP.get(key);

        return createResponseWithCodeAndJson(HTTP_OK_STATUS, json);
    }

    public String retrieveMethod(String url) {
        Matcher m = PATTERN_METHOD.matcher(url);
        if(m.find()){
            return m.group(1);
        } else {
            return null;
        }
    }

    private Response createResponseWithCodeAndJson(int responseCode, String json) {
        return new Response("",responseCode, "nothing", Collections.EMPTY_LIST,
                new TypedByteArray("application/json", json.getBytes()));
    }
}
