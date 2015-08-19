package com.ikota.flickrclient.model;

import java.util.List;

/**
 * Created by kota on 2015/08/19.
 * Model of flickr.interestingness.getList return item.
 */
public class Interestingness {
    public Photos photos;
    public String stat;

    public static class Photos {
        public int page;
        public int pages;
        public int perpage;
        public int total;
        public List<Photo> photo;
    }

    public static class Photo {
        public String id;
        public String owner;
        public String secret;
        public String server;
        public int farm;
        public String title;
        public int ispublic;
        public int isfriend;
        public int isfamily;
    }
}
