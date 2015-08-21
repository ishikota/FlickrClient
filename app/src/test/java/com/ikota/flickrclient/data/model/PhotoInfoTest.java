package com.ikota.flickrclient.data.model;

import com.google.gson.Gson;
import com.ikota.flickrclient.data.DataHolder;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by kota on 2015/08/19.
 *
 */
@RunWith(RobolectricTestRunner.class)
public class PhotoInfoTest extends TestCase{
    @Test
    public void readItem() throws Exception{
        Gson gson = new Gson();
        PhotoInfo photoInfo = gson.fromJson(DataHolder.DETAIL_JSON, PhotoInfo.class);
        assertEquals("20623135501", photoInfo.photo.id);
        assertEquals("30179751@N06", photoInfo.photo.owner.nsid);
        assertEquals("Cole Chase Photography", photoInfo.photo.owner.username);
        assertTrue(photoInfo.photo.owner.realname.isEmpty());
        assertEquals("Iowa City, USA", photoInfo.photo.owner.location);
        assertEquals("3919", photoInfo.photo.owner.iconserver);
        assertEquals("4", photoInfo.photo.owner.iconfarm);
        assertEquals("cannon_s5_is", photoInfo.photo.owner.path_alias);
        assertEquals("Sundown on the Oregon Coast", photoInfo.photo.title._content);
        assertEquals("I was treated to this brilliant sunset on my recent trip to Oregon. " +
                " This photo was captured in Ecola State Park which is one of my favorite places to" +
                " visit on the coast. The deactivated Tillamook Rock Lighthouse is seen on the small" +
                " island in the distance.  Comments &amp; views are always appreciated. Thanks for" +
                " looking &amp; have a wonderful Sunday!", photoInfo.photo.description._content);
        assertEquals("2015-08-02 22:40:28", photoInfo.photo.dates.taken);
        assertEquals(9, photoInfo.photo.tags.tag.size());
        PhotoInfo.Tag tag = photoInfo.photo.tags.tag.get(0);
        assertEquals("30134429-20623135501-32576", tag.id);
        assertEquals("30179751@N06", tag.author);
        assertEquals("Cole Chase Photography", tag.authorname);
        assertEquals("Cannon Beach", tag.raw);
        assertEquals("cannonbeach", tag._content);
        assertEquals("0", tag.machine_tag);
    }
}
