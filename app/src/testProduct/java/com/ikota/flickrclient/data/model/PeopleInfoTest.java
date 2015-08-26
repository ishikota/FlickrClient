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
        assertEquals("30179751@N06", info.person.id);
        assertEquals("30179751@N06", info.person.nsid);
        assertEquals(1, info.person.ispro);
        assertEquals(0, info.person.can_buy_pro);
        assertEquals("3919", info.person.iconserver);
        assertEquals(4, info.person.iconfarm);
        assertEquals("cannon_s5_is", info.person.path_alias);
        assertEquals("1", info.person.has_stats);
        assertEquals("Cole Chase Photography", info.person.username._content);
        //assertEquals("Kota Ishimoto", info.person.realname._content);
        assertEquals("Iowa City, USA", info.person.location._content);
        //assertEquals("", info.person.description._content);
        // below 2 test value would change
        // assertEquals(53, info.person.photos.count._content);
        // assertEquals("0", info.person.photos.views._content);
    }
}
