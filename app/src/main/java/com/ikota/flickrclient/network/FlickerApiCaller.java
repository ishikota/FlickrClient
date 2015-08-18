package com.ikota.flickrclient.network;

import android.content.Context;

/**
 * Created by kota on 2014/10/05.
 * Utility class which encapslate Flicker API methods.
 */
public class FlickerApiCaller extends BaseApiCaller {
    //private static String TAG = FlickerApiCaller.class.getSimpleName();
    public static final String END_POINT = "https://api.flickr.com/services";
    public static final String BASE_URL = END_POINT + "/rest/?method=";
    private static final String APIKEY_SEARCH_STRING = "&api_key=84434e44ac54eb2853b6b4492daf863e";
    private static final String AUTO_TOKEN_STRING = "";//"&auth_token=72157654253064570-56682a94e3637460";
    private static final String APISIG_STRING = "";//"&api_sig=8ae2c2242e3836f0bbbac273694a4706";
    private static final String FORMAT_JSON = "&format=json";
    private static final String JSON_CALLBACK = "&nojsoncallback=1";
    private static final String PER_PAGE = "&per_page=";
    private static final String PAGE = "&page=";

    /**
     * Singleton instance
     */
    private static FlickerApiCaller mInstance;

    /**
     * private constructor for singleton pattern
     */
    private FlickerApiCaller() {
    }

    /**
     * static factory method.
     *
     * @return singleton object of ApiCaller
     */
    public static FlickerApiCaller getInstance() {
        if (mInstance == null) {
            mInstance = new FlickerApiCaller();
        }
        return mInstance;
    }


    @SuppressWarnings("unused")
    public void testEcho(Context context, String key, String val, ApiListener listener) {
        String[] keys = {key};
        String[] vals = {val};
        post(context, BASE_URL + "flickr.test.echo" + FORMAT_JSON + JSON_CALLBACK + APIKEY_SEARCH_STRING + APISIG_STRING, keys, vals, listener);
    }

    public void getImageList(Context context, int page, ApiListener listener) {
        // Flicker Api is using 1-index in page counting.
        String method = "flickr.interestingness.getList";
        String url = BASE_URL + method + PAGE + (page + 1) + PER_PAGE + 20 + FORMAT_JSON + JSON_CALLBACK + APIKEY_SEARCH_STRING + AUTO_TOKEN_STRING + APISIG_STRING;
        get(context, url, null, null, listener);
    }

    public void getDetailInfo(Context context, String id, ApiListener listener) {
        String method = "flickr.photos.getInfo";
        String SEARCH_PHOTO_ID = "&photo_id="+id;
        String url = BASE_URL + method + SEARCH_PHOTO_ID + FORMAT_JSON + JSON_CALLBACK + APIKEY_SEARCH_STRING + AUTO_TOKEN_STRING + APISIG_STRING;
        get(context, url,null, null, listener);
    }


}
