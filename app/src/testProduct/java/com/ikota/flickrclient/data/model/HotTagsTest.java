package com.ikota.flickrclient.data.model;

import com.google.gson.Gson;
import com.ikota.flickrclient.data.DataHolder;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

/**
 * Created by kota on 2015/08/19.
 *
 */
@RunWith(RobolectricTestRunner.class)
public class HotTagsTest extends TestCase{
    @Test
    public void readItem() throws Exception{
        Gson gson = new Gson();
        HotTagList info = gson.fromJson(DataHolder.HOT_TAGS, HotTagList.class);
        List<HotTagList.Tag> tags = info.hottags.tag;
        assertEquals(info.stat, "ok");
        assertEquals(info.hottags.period, "day");
        assertEquals(info.hottags.count, 20);
        assertEquals(tags.get(0).score, 100);
        assertEquals(tags.get(0)._content, "feb23");
        assertEquals(tags.get(19).score, 75);
        assertEquals(tags.get(19)._content, "leicammonochrom");
    }
}
