package com.ikota.flickrclient.data.model;

import java.util.List;

/**
 * Created by kota on 2015/08/19.
 * Model of flickr.interestingness.getList return item.
 */
@SuppressWarnings("unused")
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

        /**
         * You can specify image size and quality by passing arguments.
         *  flg list
         *  - s small square 75x75
         *  - q large square 150x150
         *  - m 240 on longest side
         *  - n 320 on longest side
         *  - - 500 on longest side
         *  - z 640, 640 on longest side
         *  - o original image
         *
         * @param size the flg to specify image quality
         * @return url of image of specified quality
         */
        public String generatePhotoURL(String size) {
            return "https://farm" + this.farm + ".staticflickr.com/"
                    + this.server + "/" + this.id + "_" + this.secret + "_" + size + ".jpg";
        }
    }
}
