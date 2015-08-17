package com.ikota.flickrclient.model;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by kota on 2015/08/17.
 * Detail Item bean test
 */
@RunWith(RobolectricTestRunner.class)
public class FlickerPhotoInfoTest extends TestCase{

    @Test
    public void readItem() throws Exception{
        FlickerPhotoInfo item = new FlickerPhotoInfo(DataHolder.DETAIL_JSON);
        assertEquals("20623135501", item.photo.id);
        assertEquals("30179751@N06", item.owner.nsid);
        assertEquals("Cole Chase Photography", item.owner.username);
        assertTrue(item.owner.realname.isEmpty());
        assertEquals("Iowa City, USA", item.owner.location);
        assertEquals("3919", item.owner.iconserver);
        assertEquals("4", item.owner.iconfarm);
        assertEquals("cannon_s5_is", item.owner.path_alias);
        assertEquals("Sundown on the Oregon Coast", item.title);
        assertEquals("I was treated to this brilliant sunset on my recent trip to Oregon. " +
                " This photo was captured in Ecola State Park which is one of my favorite places to" +
                " visit on the coast. The deactivated Tillamook Rock Lighthouse is seen on the small" +
                " island in the distance.  Comments &amp; views are always appreciated. Thanks for" +
                " looking &amp; have a wonderful Sunday!", item.description);
        assertEquals("2015-08-02 22:40:28", item.dates);
        assertEquals(9, item.tags.size());
        FlickerPhotoInfo.Tag tag = item.tags.get(0);
        assertEquals("30134429-20623135501-32576", tag.id);
        assertEquals("30179751@N06", tag.author);
        assertEquals("Cole Chase Photography", tag.authorname);
        assertEquals("Cannon Beach", tag.raw);
        assertEquals("cannonbeach", tag._content);
        assertEquals("0", tag.machine_tag);
    }
}
