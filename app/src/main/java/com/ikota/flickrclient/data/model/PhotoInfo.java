package com.ikota.flickrclient.data.model;

import java.util.List;

/**
 * Created by kota on 2015/08/19.
 * Model of flickr.photos.getInfo return item.
 */
public class PhotoInfo {
    public Photo photo;
    public String stat;

    public static class Photo {
        public String id;
        public String secret;
        public String server;
        public String farm;
        public String isfavorite;
        public Owner owner;
        public Title title;
        public Description description;
        public Dates dates;
        public int views;
        public Comments comments;
        public Tags tags;

        public String generatePhotoURL(String size) {
            return "https://farm"+farm+".staticflickr.com/"
                    + server+"/"+id+"_"+secret+"_"+size+".jpg";
        }
    }

    public static class Owner {
        public String nsid;
        public String username;
        public String realname;
        public String location;
        public String iconserver;
        public String iconfarm;
        public String path_alias;

        public String generateOwnerIconURL() {
            return "https://flickr.com/buddyicons/"+nsid+".jpg";
        }
    }

    public static class Title {
        public String _content;
    }

    public static class Description {
        public String _content;
    }

    public static class Dates {
        public String posted;
        public String taken;
        public String takengranularity;
        public String lastupdate;
    }

    public static class Comments {
        public int _content;
    }

    public static class Tags {
        public List<Tag> tag;
    }

    public static class Tag {
        public String id;
        public String author;
        public String authorname;
        public String raw;
        public String _content;
        public String machine_tag;
    }

}
