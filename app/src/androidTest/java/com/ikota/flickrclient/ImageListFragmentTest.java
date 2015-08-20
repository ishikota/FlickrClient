package com.ikota.flickrclient;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ikota.flickrclient.di.DummyAPIModule;
import com.ikota.flickrclient.ui.AndroidApplication;
import com.ikota.flickrclient.ui.MainActivity;
import com.ikota.flickrclient.ui.PopularListFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import dagger.ObjectGraph;
import retrofit.client.Client;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;


@RunWith(AndroidJUnit4.class)
public class ImageListFragmentTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public ImageListFragmentTest() {
        super(MainActivity.class);
    }

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(
            MainActivity.class,
            true,     // initialTouchMode
            false);   // launchActivity. False so we can customize the intent per test method

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        AndroidApplication app =
                (AndroidApplication) instrumentation.getTargetContext().getApplicationContext();

        // set objectGraph to inject Mock API
        List modules = Collections.singletonList(new DummyAPIModule());
        ObjectGraph graph = ObjectGraph.create(modules.toArray());
        app.setObjectGraph(graph);
        app.objectGraph().inject(app);
    }

    @Test
    public void testProgress_show() {
        // start Activity manually
        MainActivity activity = activityRule.launchActivity(new Intent());

        PopularListFragment fragment = (PopularListFragment)activity.getSupportFragmentManager()
                .findFragmentByTag(PopularListFragment.class.getSimpleName());
        @SuppressWarnings("ConstantConditions")
        RecyclerView recyclerView = (RecyclerView)fragment.getView().findViewById(android.R.id.list);
        ProgressBar progressBar = (ProgressBar)fragment.getView().findViewById(R.id.progress);
        TextView emptyView = (TextView)fragment.getView().findViewById(android.R.id.empty);

        assertEquals(View.VISIBLE, progressBar.getVisibility());
        IdlingResource idlingResource = new LoadingIdlingResource(recyclerView);
        Espresso.registerIdlingResources(idlingResource);
        Espresso.onView(ViewMatchers.withId(android.R.id.list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.scrollTo()));
        Espresso.unregisterIdlingResources(idlingResource);
        assertEquals(View.GONE, progressBar.getVisibility());
        assertEquals(View.GONE, emptyView.getVisibility());
    }

    public class MockClient implements Client {

        private static final int HTTP_OK_STATUS = 200;
        private final String RESPONSE_JSON;

        MockClient(String responce_json) {
            RESPONSE_JSON = responce_json;
        }

        @Override
        public Response execute(Request request) throws IOException {
            return createResponseWithCodeAndJson(request.getUrl(), HTTP_OK_STATUS, RESPONSE_JSON);
        }

        @SuppressWarnings("unchecked")
        private Response createResponseWithCodeAndJson(String url, int responseCode, String json) {
            return new Response(url,responseCode, "nothing", Collections.EMPTY_LIST,
                    new TypedByteArray("application/json", json.getBytes()));
        }

    }

}