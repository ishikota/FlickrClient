package com.ikota.flickrclient.network;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import com.ikota.flickrclient.data.model.ListData;
import com.ikota.flickrclient.data.model.PeopleInfo;
import com.ikota.flickrclient.data.model.PhotoInfo;
import com.ikota.flickrclient.ui.AndroidApplication;
import com.ikota.flickrclient.ui.PopularListActivity;

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
public class FlickrAPITest extends ActivityInstrumentationTestCase2<PopularListActivity> {

    AndroidApplication app;
    private CountDownLatch lock;

    public FlickrAPITest() {
        super(PopularListActivity.class);
    }

    @Rule
    public ActivityTestRule<PopularListActivity> activityRule = new ActivityTestRule<>(
            PopularListActivity.class,
            true,     // initialTouchMode
            true);   // launchActivity. False so we can customize the intent per test method

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        injectInstrumentation(instrumentation);
        PopularListActivity activity = activityRule.launchActivity(new Intent());
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
        app.api().getPopularPhotos(1, 20, new Callback<ListData>() {

            @Override
            public void success(ListData popularPhotos, Response response) {
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

    @Test
    public void getPeopleInfo() throws Exception {
        app.api().getPeopleInfo("133363540@N06", new Callback<PeopleInfo>() {
            @Override
            public void success(PeopleInfo info, Response response) {
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
                assertEquals("I like Giraffe !!!", info.person.description._content);
                lock.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail("Error o getPeopleInfo API");
            }
        });
        lock.await(10000, TimeUnit.MILLISECONDS);
        assertEquals(0, lock.getCount());
    }

    @Test
    public void peoplePublicPhoto() throws Exception {
        app.api().getPeoplePublicPhotos("133363540@N06", 1, 24, new Callback<ListData>() {

            @Override
            public void success(ListData data, Response response) {
                assertEquals("ok", data.stat);
                assertEquals(1, data.photos.page);
                assertEquals(24, data.photos.perpage);
                assertEquals(24, data.photos.photo.size());
                assertEquals(48, data.photos.total);
                lock.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail("Error on peoplePublicPhoto API");
            }
        });
        lock.await(10000, TimeUnit.MILLISECONDS);
        assertEquals(0, lock.getCount());
    }

    @Test
    public void getFavorites() throws Exception {
        app.api().getPeopleFavorites("30179751@N06", 1, 24, new Callback<ListData>() {

            @Override
            public void success(ListData data, Response response) {
                assertEquals("ok", data.stat);
                assertEquals(1, data.photos.page);
                assertEquals(24, data.photos.perpage);
                assertEquals(24, data.photos.photo.size());
                lock.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail("Error on getFavorites API");
            }
        });
        lock.await(10000, TimeUnit.MILLISECONDS);
        assertEquals(0, lock.getCount());
    }
}