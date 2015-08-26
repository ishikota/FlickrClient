package com.ikota.flickrclient.data.model;

import com.google.gson.Gson;
import com.ikota.flickrclient.data.DataHolder;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;


@RunWith(RobolectricTestRunner.class)
public class ListDataTest extends TestCase {

    @Test
    public void readItem() throws Exception{
        Gson gson = new Gson();
        ListData info = gson.fromJson(DataHolder.LIST_JSON_ERROR, ListData.class);
        assertEquals(23, info.photos.photo.size());
    }
}
