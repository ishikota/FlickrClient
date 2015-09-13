package com.ikota.flickrclient.network.retrofit;

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
    public static final String TEST_ECHO ="flickr.test.echo";
    public static final String POPULAR = "flickr.interestingness.getList";
    public static final String PHOTO_INFO ="flickr.photos.getInfo";
    public static final String PEOPLE_INFO ="flickr.people.getInfo";
    public static final String PUBLIC_PHOTO ="flickr.people.getPublicPhotos";
    public static final String PEOPLE_FAVORITE = "flickr.favorites.getPublicList";
    public static final String COMMENT_LIST ="flickr.photos.comments.getList";
    public static final String HOT_TAG_LIST = "flickr.tags.getHotList";
}
