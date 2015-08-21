package com.ikota.flickrclient;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;

import com.google.gson.Gson;
import com.ikota.flickrclient.data.model.FlickerListItem;
import com.ikota.flickrclient.ui.ImageDetailActivity;
import com.ikota.flickrclient.ui.MainActivity;
import com.ikota.flickrclient.ui.PopularListFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by kota on 2015/08/15.
 * First Espresso test.
 */
@RunWith(AndroidJUnit4.class)
public class EspressoSampleTest extends ActivityInstrumentationTestCase2<MainActivity>{

    private PopularListFragment fragment;
    private RecyclerView recyclerView;

    public EspressoSampleTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        injectInstrumentation(instrumentation);
        MainActivity activity = getActivity();
        fragment = (PopularListFragment)activity.getSupportFragmentManager().
                findFragmentByTag(PopularListFragment.class.getSimpleName());
        recyclerView = (RecyclerView)fragment.getView().findViewById(android.R.id.list);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testPreconditions() {
        assertNotNull("fragment is null", fragment);
        assertNotNull("recyclerView is null", recyclerView);
    }

    @Test
    public void clickList_go_detail() {
        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(ImageDetailActivity.class.getName(),null, false);

        // wait until list item is loaded
        IdlingResource idlingResource = new LoadingIdlingResource(recyclerView);
        Espresso.registerIdlingResources(idlingResource);

        // start DetailActivity by clicking RecyclerView item.
        Espresso.onView(ViewMatchers.withId(android.R.id.list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));

        // unregister idling resources
        Espresso.unregisterIdlingResources(idlingResource);

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);

        // assertion to DetailActivity
        Activity activity = receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("DetailActivity is not null", activity);
        assertEquals("Launched Activity is not ImageDetailActivity", ImageDetailActivity.class, activity.getClass());

        Intent intent = activity.getIntent();
        String json = intent.getStringExtra(ImageDetailActivity.EXTRA_CONTENT);
        Gson gson = new Gson();
        FlickerListItem content = gson.fromJson(json, FlickerListItem.class);
        assertNotNull(content.id);
        assertNotNull(content.owner);
        assertNotNull(content.secret);
        assertNotNull(content.server);
    }

}
