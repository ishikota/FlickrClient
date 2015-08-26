package com.ikota.flickrclient.network.retrofit;

import com.ikota.flickrclient.data.model.ListData;
import com.ikota.flickrclient.data.model.PeopleInfo;
import com.ikota.flickrclient.data.model.PhotoInfo;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

import static com.ikota.flickrclient.network.retrofit.FlickrURL.APIKEY_SEARCH_STRING;
import static com.ikota.flickrclient.network.retrofit.FlickrURL.APISIG_STRING;
import static com.ikota.flickrclient.network.retrofit.FlickrURL.AUTO_TOKEN_STRING;
import static com.ikota.flickrclient.network.retrofit.FlickrURL.FORMAT_JSON;
import static com.ikota.flickrclient.network.retrofit.FlickrURL.JSON_CALLBACK;
import static com.ikota.flickrclient.network.retrofit.FlickrURL.METHOD_PREFIX;


public interface FlickrService {

    @SuppressWarnings("unused")
    @POST(METHOD_PREFIX+"flickr.test.echo" + FORMAT_JSON + JSON_CALLBACK + APIKEY_SEARCH_STRING + APISIG_STRING)
    void testEcho(@Query("hoge") String fuga, Callback<Response> cb);

    @GET(METHOD_PREFIX + "flickr.interestingness.getList" + FORMAT_JSON + JSON_CALLBACK + APIKEY_SEARCH_STRING + AUTO_TOKEN_STRING + APISIG_STRING)
    void getPopularPhotos(@Query("page") int page, @Query("per_page") int per_page, Callback<ListData> cb);

    @GET(METHOD_PREFIX + "flickr.photos.getInfo" + FORMAT_JSON + JSON_CALLBACK + APIKEY_SEARCH_STRING + AUTO_TOKEN_STRING + APISIG_STRING)
    void getPhotoInfo(@Query("photo_id") String id, Callback<PhotoInfo> cb);

    @GET(METHOD_PREFIX+"flickr.people.getInfo"+FORMAT_JSON+JSON_CALLBACK+APIKEY_SEARCH_STRING+AUTO_TOKEN_STRING+APISIG_STRING)
    void getPeopleInfo(@Query("user_id") String user_id, Callback<PeopleInfo> cb);

    @GET(METHOD_PREFIX+"flickr.people.getPublicPhotos"+FORMAT_JSON+JSON_CALLBACK+APIKEY_SEARCH_STRING+AUTO_TOKEN_STRING+APISIG_STRING)
    void getPeoplePublicPhotos(@Query("user_id") String user_id, @Query("page") int page, @Query("per_page") int per_page, Callback<ListData> cb);

}
