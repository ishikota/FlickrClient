package com.ikota.flickrclient.network;

import android.content.Context;

import com.android.volley.VolleyError;
import com.ikota.flickrclient.network.volley.BaseApiCaller;
import com.ikota.flickrclient.network.volley.FlickerApiCaller;

import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

/**
 * Created by kota on 2015/08/19.
 *
 */
@RunWith(RobolectricTestRunner.class)
public class FlickerApiCallerTest extends TestCase {

    private Context context;
    private FlickerApiCaller api;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        api = FlickerApiCaller.getInstance();
        context = RuntimeEnvironment.application.getApplicationContext();
    }

    @Test
    public void testGetInstance() throws Exception {
        FlickerApiCaller api2 = FlickerApiCaller.getInstance();
        assertEquals(api, api2);
    }

    @Test
    public void testTestEcho() throws Exception {
        FlickerApiCaller.getInstance().testEcho(context, "hoge", "fuga", new BaseApiCaller.ApiListener() {
            @Override
            public void onPostExecute(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    assertEquals("ok", jo.getString("stat"));
                    assertEquals("fuga", jo.getString("hoge"));
                } catch (Exception e) {
                    fail("json error on testEcho API");
                }
            }

            @Override
            public void onErrorListener(VolleyError error) {
                fail("Error on testEcho API");
            }
        });
    }

    @Test
    public void testGetImageList() throws Exception {
        FlickerApiCaller.getInstance().getImageList(context, 0, new BaseApiCaller.ApiListener() {
            @Override
            public void onPostExecute(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    JSONObject photos = root.getJSONObject("photos");
                    JSONArray photo = photos.getJSONArray("photo");
                    assertEquals(1, photos.getInt("page"));
                    assertEquals("ok", root.getString("stat"));
                    assertEquals(20, photo.length());
                } catch (Exception e) {
                    fail("json error on getIamgeList API");
                }
            }

            @Override
            public void onErrorListener(VolleyError error) {
                fail("Error on getImageList API");
            }
        });
    }

    @Test
    public void testGetDetailInfo() throws Exception {
        FlickerApiCaller.getInstance().getDetailInfo(context, "20623135501", new BaseApiCaller.ApiListener() {
            @Override
            public void onPostExecute(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    JSONObject photo = root.getJSONObject("photo");
                    JSONObject owner = root.getJSONObject("owner");
                    assertEquals("20623135501", photo.getString("id"));
                    assertEquals("30179751@N06", owner.getString("nsid"));
                } catch (Exception e) {
                    fail("json error on getDetailInfo API");
                }
            }

            @Override
            public void onErrorListener(VolleyError error) {
                fail("Error on getDetailInfo API");
            }
        });
    }
}