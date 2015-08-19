package com.ikota.flickrclient.network.retrofit;

/**
 * Created by kota on 2015/08/19.
 *
 */
@SuppressWarnings("unused")
public class FlickrURL {
    public static final String END_POINT = "https://api.flickr.com/services";
    public static final String METHOD_PREFIX = "/rest/?method=";
    public static final String APIKEY_SEARCH_STRING = "&api_key=84434e44ac54eb2853b6b4492daf863e";
    public static final String AUTO_TOKEN_STRING = "";//"&auth_token=72157654253064570-56682a94e3637460";
    public static final String APISIG_STRING = "";//"&api_sig=8ae2c2242e3836f0bbbac273694a4706";
    public static final String FORMAT_JSON = "&format=json";
    public static final String JSON_CALLBACK = "&nojsoncallback=1";
    public static final String PER_PAGE = "&per_page=";
    public static final String PAGE = "&page=";
}
