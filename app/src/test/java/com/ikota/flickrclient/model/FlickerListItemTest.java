package com.ikota.flickrclient.data;

import com.google.gson.Gson;
import com.ikota.flickrclient.data.model.FlickerListItem;

import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by kota on 2015/08/17.
 * List item bean test
 */
@RunWith(RobolectricTestRunner.class)
public class FlickerListItemTest extends TestCase {

    @Test
    public void readItem() throws Exception{
        JSONObject root_jo = new JSONObject(DataHolder.LIST_JSON);
        JSONObject photos_jo = root_jo.getJSONObject("photos");
        JSONArray itemArray = photos_jo.getJSONArray("photo");
        int load_item_num = itemArray.length();
        Gson gson = new Gson();
        assertTrue(load_item_num != 0);
        JSONObject json = itemArray.getJSONObject(0);
        FlickerListItem item = gson.fromJson(json.toString(), FlickerListItem.class);
        assertEquals("20623135501", item.id);
        assertEquals("30179751@N06", item.owner);
        assertEquals("f755f50f34", item.secret);
        assertEquals("686", item.server);
        assertEquals(1, item.farm);
        assertEquals("Sundown on the Oregon Coast", item.title);
        assertEquals(1, item.ispublic);
        assertEquals(0, item.isfamily);
        assertEquals(1, item.ispublic);
    }


}