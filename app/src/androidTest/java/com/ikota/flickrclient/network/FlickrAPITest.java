package com.ikota.flickrclient.network;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import com.ikota.flickrclient.data.model.Interestingness;
import com.ikota.flickrclient.data.model.PhotoInfo;
import com.ikota.flickrclient.ui.AndroidApplication;
import com.ikota.flickrclient.ui.MainActivity;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;


@RunWith(AndroidJUnit4.class)
public class FlickrAPITest extends ActivityInstrumentationTestCase2<MainActivity> {

    AndroidApplication app;
    private CountDownLatch lock;

    public FlickrAPITest() {
        super(MainActivity.class);
    }

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(
            MainActivity.class,
            true,     // initialTouchMode
            true);   // launchActivity. False so we can customize the intent per test method

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        injectInstrumentation(instrumentation);
        MainActivity activity = activityRule.launchActivity(new Intent());
        app = (AndroidApplication)activity.getApplication();
        lock = new CountDownLatch(1);
    }

    @Test
    public void testTestEcho() throws Exception {
        app.api().testEcho("fuga", new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String str = new String(((TypedByteArray) response.getBody()).getBytes());
                try {
                    JSONObject jo = new JSONObject(str);
                    assertEquals("ok", jo.getString("stat"));
                    JSONObject hoge = new JSONObject(jo.getString("hoge"));
                    assertEquals("fuga", hoge.getString("_content"));
                } catch (Exception e) {
                    fail("json error on testEcho API");
                }
                lock.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail("Error on testEcho API");
            }
        });
        lock.await(10000, TimeUnit.MILLISECONDS);
        assertEquals(0,lock.getCount());
    }

    @Test
    public void popularPhotoList() throws Exception {
        app.api().getPopularPhotos(1, new Callback<Interestingness>() {

            @Override
            public void success(Interestingness popularPhotos, Response response) {
                assertEquals("ok", popularPhotos.stat);
                assertEquals(1, popularPhotos.photos.page);
                assertEquals(20, popularPhotos.photos.perpage);
                assertEquals(20, popularPhotos.photos.photo.size());
                lock.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail("Error on getImageList API");
            }
        });
        lock.await(10000, TimeUnit.MILLISECONDS);
        assertEquals(0, lock.getCount());
    }

    @Test
    public void getDetailInfo() throws Exception {
        app.api().getPhotoInfo("20623135501", new Callback<PhotoInfo>() {
            @Override
            public void success(PhotoInfo photoInfo, Response response) {
                assertEquals("20623135501", photoInfo.photo.id);
                assertEquals("30179751@N06", photoInfo.photo.owner.nsid);
                lock.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail("Error on getDetailInfo API");
            }
        });
        lock.await(10000, TimeUnit.MILLISECONDS);
        assertEquals(0, lock.getCount());
    }
}