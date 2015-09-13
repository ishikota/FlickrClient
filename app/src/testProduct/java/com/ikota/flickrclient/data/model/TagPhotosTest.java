package com.ikota.flickrclient.data.model;

import com.google.gson.Gson;
import com.ikota.flickrclient.data.DataHolder;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by kota on 2015/08/17.
 * List item bean test
 */
@RunWith(RobolectricTestRunner.class)
public class TagPhotosTest extends TestCase {

    @Test
    public void readItem() throws Exception{
        Gson gson = new Gson();
        ListData data = gson.fromJson(DataHolder.PHOTOS_BY_TAG, ListData.class);
        ListData.Photo item = data.photos.photo.get(0);
        assertEquals("21296232326", item.id);
        assertEquals("133982212@N04", item.owner);
        assertEquals("11292471cb", item.secret);
        assertEquals("5662", item.server);
        assertEquals(6, item.farm);
        assertEquals("sexy", item.title);
        assertEquals(1, item.ispublic);
        assertEquals(0, item.isfamily);
        assertEquals(0, item.isfamily);
    }


}