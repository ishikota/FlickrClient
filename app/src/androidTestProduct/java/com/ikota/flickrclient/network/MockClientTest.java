package com.ikota.flickrclient.network;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import com.ikota.flickrclient.data.model.CommentList;
import com.ikota.flickrclient.data.model.ListData;
import com.ikota.flickrclient.data.model.PeopleInfo;
import com.ikota.flickrclient.data.model.PhotoInfo;
import com.ikota.flickrclient.di.DummyAPIModule;
import com.ikota.flickrclient.network.retrofit.FlickrURL;
import com.ikota.flickrclient.network.retrofit.MockClient;
import com.ikota.flickrclient.ui.AndroidApplication;
import com.ikota.flickrclient.ui.PopularListActivity;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import dagger.ObjectGraph;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

import static com.ikota.flickrclient.network.retrofit.FlickrURL.APIKEY_SEARCH_STRING;
import static com.ikota.flickrclient.network.retrofit.FlickrURL.APISIG_STRING;
import static com.ikota.flickrclient.network.retrofit.FlickrURL.AUTO_TOKEN_STRING;
import static com.ikota.flickrclient.network.retrofit.FlickrURL.FORMAT_JSON;
import static com.ikota.flickrclient.network.retrofit.FlickrURL.JSON_CALLBACK;
import static com.ikota.flickrclient.network.retrofit.FlickrURL.METHOD_PREFIX;
import static com.ikota.flickrclient.network.retrofit.FlickrURL.PEOPLE_INFO;
import static com.ikota.flickrclient.network.retrofit.FlickrURL.PHOTO_INFO;
import static com.ikota.flickrclient.network.retrofit.FlickrURL.PUBLIC_PHOTO;


@RunWith(AndroidJUnit4.class)
public class MockClientTest extends ActivityInstrumentationTestCase2<PopularListActivity> {

    public MockClientTest() {
        super(PopularListActivity.class);
    }

    AndroidApplication app;
    private CountDownLatch lock;

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
        app = (AndroidApplication) instrumentation.getTargetContext().getApplicationContext();
        // setup objectGraph to inject Mock API
        List modules = Collections.singletonList(new DummyAPIModule(Util.RESPONSE_MAP));
        ObjectGraph graph = ObjectGraph.create(modules.toArray());
        app.setObjectGraph(graph);
        app.getObjectGraph().inject(app);
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
                fail("Error on testEcho API: message=" + error.getMessage());
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
                ListData.Photo photo = popularPhotos.photos.photo.get(0);
                assertEquals("20623135501", photo.id);
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
                assertEquals(58, photoInfo.photo.comments._content);
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
                assertEquals("30179751@N06", info.person.id);
                assertEquals("30179751@N06", info.person.nsid);
                assertEquals(1, info.person.ispro);
                assertEquals(0, info.person.can_buy_pro);
                assertEquals("3919", info.person.iconserver);
                assertEquals(4, info.person.iconfarm);
                // assertEquals("null", info.person.path_alias); cause assertion error
                assertEquals("1", info.person.has_stats);
                assertEquals("Cole Chase Photography", info.person.username._content);
                assertEquals("Iowa City, USA", info.person.location._content);
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
    public void getFavorites() throws Exception {
        app.api().getPeopleFavorites("133363540@N06", 1, 24, new Callback<ListData>() {
            @Override
            public void success(ListData item, Response response) {
                assertEquals("ok", item.stat);
                assertEquals(1, item.photos.page);
                assertEquals(24, item.photos.perpage);
                assertEquals(24, item.photos.photo.size());
                assertEquals(33379, item.photos.total);
                ListData.Photo photo = item.photos.photo.get(0);
                assertEquals("5565562390", photo.id);
                lock.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail("Error on getPeopleInfo API");
            }
        });
        lock.await(10000, TimeUnit.MILLISECONDS);
        assertEquals(0, lock.getCount());
    }

    @Test
    public void getCommentList() throws Exception {
        app.api().getCommentList("20623135501", new Callback<CommentList>() {
            @Override
            public void success(CommentList item, Response response) {
                assertEquals("20623135501", item.comments.photo_id);
                assertEquals("ok", item.stat);
                CommentList.Comment comment = item.comments.comment.get(0);
                assertEquals("30134429-20623135501-72157657254905896", comment.id);
                assertEquals("43287538@N00", comment.author);
                assertEquals("¡! Nature B■x !¡", comment.authorname);
                assertEquals("2918", comment.iconserver);
                assertEquals(3, comment.iconfarm);
                assertEquals("1439709894", comment.datecreate);
                assertEquals("https://www.flickr.com/photos/cannon_s5_is/20623135501/#comment72157657254905896", comment.permalink);
                assertEquals("", comment.path_alias);
                assertEquals("", comment.realname);
                assertEquals("C'est magnifique !", comment._content);
                lock.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail("Error om getCommentList API");
            }
        });
        lock.await(10000, TimeUnit.MILLISECONDS);
        assertEquals(0, lock.getCount());

    }


    @Test
    public void test_regex() {
        MockClient client = new MockClient(null);
        String res = client.retrieveMethod(METHOD_PREFIX+"flickr.test.echo" + FORMAT_JSON + JSON_CALLBACK + APIKEY_SEARCH_STRING + APISIG_STRING);
        assertEquals(FlickrURL.TEST_ECHO, res);
        res = client.retrieveMethod(METHOD_PREFIX + "flickr.interestingness.getList" + FORMAT_JSON + JSON_CALLBACK + APIKEY_SEARCH_STRING + AUTO_TOKEN_STRING + APISIG_STRING);
        assertEquals(FlickrURL.POPULAR, res);
        res = client.retrieveMethod(METHOD_PREFIX + "flickr.photos.getInfo" + FORMAT_JSON + JSON_CALLBACK + APIKEY_SEARCH_STRING + AUTO_TOKEN_STRING + APISIG_STRING);
        assertEquals(PHOTO_INFO, res);
        res = client.retrieveMethod(METHOD_PREFIX+"flickr.people.getInfo"+FORMAT_JSON+JSON_CALLBACK+APIKEY_SEARCH_STRING+AUTO_TOKEN_STRING+APISIG_STRING);
        assertEquals(PEOPLE_INFO, res);
        res = client.retrieveMethod(METHOD_PREFIX+"flickr.people.getPublicPhotos"+FORMAT_JSON+JSON_CALLBACK+APIKEY_SEARCH_STRING+AUTO_TOKEN_STRING+APISIG_STRING);
        assertEquals(PUBLIC_PHOTO, res);
    }

}
