package com.ikota.flickrclient.network;


import com.ikota.flickrclient.data.DataHolder;
import com.ikota.flickrclient.network.retrofit.FlickrURL;

import java.util.HashMap;

public class Util {

    public static final HashMap<String, String> RESPONSE_MAP = new HashMap<String, String>(){
        {put(FlickrURL.TEST_ECHO, DataHolder.ECHO_JSON);}
        {put(FlickrURL.POPULAR, DataHolder.LIST_JSON);}
        {put(FlickrURL.PHOTO_INFO, DataHolder.DETAIL_JSON);}
        {put(FlickrURL.PEOPLE_INFO, DataHolder.PEOPLE_INFO);}
        {put(FlickrURL.PUBLIC_PHOTO, DataHolder.PEOPLE_PUBLIC_PHOTO);}
        {put(FlickrURL.PEOPLE_FAVORITE, DataHolder.PEOPLE_FAVORITE);}
        {put(FlickrURL.COMMENT_LIST, DataHolder.COMMENT_LIST_JSON);}
        {put(FlickrURL.HOT_TAG_LIST, DataHolder.HOT_TAGS);}
        {put(FlickrURL.PHOTO_SEARCH, DataHolder.PHOTOS_BY_TAG);}
    };

}
