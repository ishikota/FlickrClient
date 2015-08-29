package com.ikota.flickrclient.data.model;

import android.util.Log;
import android.util.SparseArray;

import java.util.List;

/**
 * Created by kota on 2015/08/19.
 * Model of flickr.interestingness.getList return item.
 */
@SuppressWarnings("unused")
public class ListData {
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
            String size_prefix = size.isEmpty() ? "" : "_";
            return "https://farm" + this.farm + ".staticflickr.com/"
                    + this.server + "/" + this.id + "_" + this.secret + size_prefix + size + ".jpg";
        }

        /** Calculate proper photo size by using view size and network state */
        public String generatePhotoURL(int view_size, boolean is_wifi) {
            return generatePhotoURL(getProperSize(view_size, is_wifi));
        }

        public String generateOwnerIconURL() {
            return "https://flickr.com/buddyicons/"+this.owner+".jpg";
        }


        private static final SparseArray<String> SIZE_MAP = new SparseArray<String>() {
            {put(0, "q");}
            {put(1, "m");}
            {put(1<<1, "n");}
            {put(1<<2, "");}
            {put(1<<3, "z");}
            {put(1<<4, "c");}
            {put(1<<5, "b");}
            {put(1<<6, "h");}
        };

        public static String getProperSize(int view_size, boolean is_wifi) {
            int flg;
            if(view_size >= 1600) {
                flg = 1<<6;  // size h
            } else if(view_size >= 1024) {
                flg = 1<<5;  // size b
            } else if(view_size >= 800) {
                flg = 1<<4;  // size c
            } else if(view_size >= 640) {
                flg = 1<<3;  // size z
            } else if(view_size >= 500) {
                flg = 1<<2;  // size -
            } else if(view_size >= 320) {
                flg = 1<<1;  // size n
            } else if(view_size >= 240) {
                flg = 1;     // size m
            } else {
                flg = 0;     // size q
            }

            // if wifi is unavailable, down scale image size
            if(!is_wifi) flg >>= 1;
            Log.i("size", "view_size:"+view_size+",size:"+SIZE_MAP.get(flg));
            return SIZE_MAP.get(flg);
        }
    }
}
