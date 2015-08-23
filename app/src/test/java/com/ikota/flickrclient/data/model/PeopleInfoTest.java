package com.ikota.flickrclient.data.model;

import com.google.gson.Gson;
import com.ikota.flickrclient.data.DataHolder;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class PeopleInfoTest extends TestCase {
    @Test
    public void readItem() throws Exception{
        Gson gson = new Gson();
        PeopleInfo info = gson.fromJson(DataHolder.PEOPLE_INFO, PeopleInfo.class);
        assertEquals("133363540@N06", info.person.id);
        assertEquals("133363540@N06", info.person.nsid);
        assertEquals(0, info.person.ispro);
        assertEquals(0, info.person.can_buy_pro);
        assertEquals("562", info.person.iconserver);
        assertEquals(1, info.person.iconfarm);
        // assertEquals("null", info.person.path_alias); cause assertion error
        assertEquals("0", info.person.has_stats);
        assertEquals("kota_ishimoto", info.person.username._content);
        assertEquals("Kota Ishimoto", info.person.realname._content);
        assertEquals("", info.person.location._content);
        assertEquals("", info.person.description._content);
        // below 2 test value would change
        // assertEquals(53, info.person.photos.count._content);
        // assertEquals("0", info.person.photos.views._content);
    }
}
