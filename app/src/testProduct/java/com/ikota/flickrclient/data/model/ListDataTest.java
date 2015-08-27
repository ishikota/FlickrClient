package com.ikota.flickrclient.data.model;

import com.google.gson.Gson;
import com.ikota.flickrclient.data.DataHolder;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;


@RunWith(RobolectricTestRunner.class)
public class ListDataTest extends TestCase {
    Gson gson = new Gson();

    @Test
    public void readItem() throws Exception{
        ListData info = gson.fromJson(DataHolder.LIST_JSON_ERROR, ListData.class);
        assertEquals(23, info.photos.photo.size());
    }

    @Test
    public void generatePhotoURL() throws Exception {
        ListData info = gson.fromJson(DataHolder.LIST_JSON_ERROR, ListData.class);
        String expected = "https://farm1.staticflickr.com/642/20682344538_ef33f246c3_q.jpg";
        assertEquals(expected, info.photos.photo.get(0).generatePhotoURL("q"));
        expected = "https://farm1.staticflickr.com/642/20682344538_ef33f246c3.jpg";
        assertEquals(expected, info.photos.photo.get(0).generatePhotoURL(""));
    }

    @Test
    public void calcPhotoSize() throws Exception {
        ListData info = gson.fromJson(DataHolder.LIST_JSON_ERROR, ListData.class);
        ListData.Photo photo = info.photos.photo.get(0);
        String expected = "https://farm1.staticflickr.com/642/20682344538_ef33f246c3_h.jpg";
        assertEquals(expected, photo.generatePhotoURL(1600, true));
        expected = "https://farm1.staticflickr.com/642/20682344538_ef33f246c3_b.jpg";
        assertEquals(expected, photo.generatePhotoURL(1600, false));
        assertEquals(expected, photo.generatePhotoURL(1024, true));
        expected = "https://farm1.staticflickr.com/642/20682344538_ef33f246c3_c.jpg";
        assertEquals(expected, photo.generatePhotoURL(1024, false));
        assertEquals(expected, photo.generatePhotoURL(800, true));
        expected = "https://farm1.staticflickr.com/642/20682344538_ef33f246c3_z.jpg";
        assertEquals(expected, photo.generatePhotoURL(900, false));
        assertEquals(expected, photo.generatePhotoURL(640, true));
        expected = "https://farm1.staticflickr.com/642/20682344538_ef33f246c3.jpg";
        assertEquals(expected, photo.generatePhotoURL(700, false));
        assertEquals(expected, photo.generatePhotoURL(500, true));
        expected = "https://farm1.staticflickr.com/642/20682344538_ef33f246c3_n.jpg";
        assertEquals(expected, photo.generatePhotoURL(500, false));
        assertEquals(expected, photo.generatePhotoURL(320, true));
        expected = "https://farm1.staticflickr.com/642/20682344538_ef33f246c3_m.jpg";
        assertEquals(expected, photo.generatePhotoURL(320, false));
        assertEquals(expected, photo.generatePhotoURL(240, true));
        expected = "https://farm1.staticflickr.com/642/20682344538_ef33f246c3_q.jpg";
        assertEquals(expected, photo.generatePhotoURL(319, false));
        assertEquals(expected, photo.generatePhotoURL(239, true));

    }
}
