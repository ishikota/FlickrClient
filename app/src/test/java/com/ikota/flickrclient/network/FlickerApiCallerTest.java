package com.ikota.flickrclient.network;

import com.ikota.flickrclient.model.Interestingness;
import com.ikota.flickrclient.model.PhotoInfo;
import com.ikota.flickrclient.ui.MainApplication;

import junit.framework.TestCase;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by kota on 2015/08/19.
 *
 */
@RunWith(RobolectricTestRunner.class)
public class FlickerApiCallerTest extends TestCase {


    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testTestEcho() throws Exception {
        MainApplication.API.testEcho("fuga", new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String str = new String(((TypedByteArray) response.getBody()).getBytes());
                try {
                    JSONObject jo = new JSONObject(str);
                    assertEquals("ok", jo.getString("stat"));
                    assertEquals("fuga", jo.getString("hoge"));
                } catch (Exception e) {
                    fail("json error on testEcho API");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                fail("Error on testEcho API");
            }
        });

    }

    @Test
    public void testGetImageList() throws Exception {
        MainApplication.API.getPopularPhotos(1, new Callback<Interestingness>() {

            @Override
            public void success(Interestingness popularPhotos, Response response) {
                assertEquals("ok", popularPhotos.stat);
                assertEquals(1, popularPhotos.photos.page);
                assertEquals(20, popularPhotos.photos.perpage);
                assertEquals(20, popularPhotos.photos.photo.size());
            }

            @Override
            public void failure(RetrofitError error) {
                fail("Error on getImageList API");
            }
        });
    }

    @Test
    public void testGetDetailInfo() throws Exception {
        MainApplication.API.getPhotoInfo("20623135501", new Callback<PhotoInfo>() {
            @Override
            public void success(PhotoInfo photoInfo, Response response) {
                assertEquals("20623135501", photoInfo.photo.id);
                assertEquals("30179751@N06", photoInfo.photo.owner.nsid);
            }

            @Override
            public void failure(RetrofitError error) {
                fail("Error on getDetailInfo API");
            }
        });
    }
}