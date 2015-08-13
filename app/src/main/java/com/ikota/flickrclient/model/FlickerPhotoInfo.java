package com.ikota.flickrclient.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kota on 2015/06/17.
 * holder object of detail page information
 */
@SuppressWarnings("unused")
public class FlickerPhotoInfo {
    public Photo photo;
    public Owner owner;
    public String description, dates, comments;
    public ArrayList<Tag> tags;

    public FlickerPhotoInfo(String json) {
        try {
            init(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String generatePhotoURL(String size) {
        return "https://farm"+this.photo.farm+".staticflickr.com/"
                + this.photo.server+"/"+this.photo.id+"_"+this.photo.secret+"_"+size+".jpg";
    }

    public String generateOwnerIconURL() {
        return "https://flickr.com/buddyicons/"+this.owner.nsid+".jpg";
    }

    private void init(String json) throws JSONException{
        Gson gson = new Gson();
        JSONObject root = new JSONObject(json);
        JSONObject photo_json = root.getJSONObject("photo");
        this.photo = gson.fromJson(photo_json.toString(), Photo.class);
        this.owner = gson.fromJson(photo_json.getJSONObject("owner").toString(), Owner.class);
        this.description = photo_json.getJSONObject("description").getString("_content");
        this.dates = photo_json.getJSONObject("dates").getString("taken");
        this.comments = photo_json.getJSONObject("comments").getString("_content");
        JSONArray tag_array = photo_json.getJSONObject("tags").getJSONArray("tag");
        Type listType = new TypeToken<List<Tag>>() {}.getType();
        tags = gson.fromJson(tag_array.toString(), listType);
    }

    public static class Photo {
        public String id, secret, server, farm, dateuploaded, isfavorite,
                license, safety_level, rotation, originalsecret, originalformat;
    }

    public static class Owner {
        public String nsid, username, realname, location, iconserver, iconfarm, path_alias;
    }

    public static class Tag {
        public String id, author, authorname, raw, _content, machine_tag;
    }

}
