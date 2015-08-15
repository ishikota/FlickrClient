package com.ikota.flickrclient;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;

import com.ikota.flickrclient.ui.ImageDetailActivity;
import com.ikota.flickrclient.ui.MainActivity;
import com.ikota.flickrclient.ui.PopularListFragment;

import org.junit.After;
import org.junit.Before;

/**
 * Created by kota on 2015/08/15.
 * First Espresso test.
 */
public class MyEspressoTest extends ActivityInstrumentationTestCase2<MainActivity>{

    private PopularListFragment fragment;
    private RecyclerView recyclerView;

    public MyEspressoTest() {
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

    public void testPreconditions() {
        assertNotNull("fragment is null", fragment);
        assertNotNull("recyclerView is null", recyclerView);
    }

    public void testRecyclerView_click_after_load() {
        // TODO : Fix to wait until list item is loaded.(wait 3 seconds may cause error.)
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNotNull("adapter is null", recyclerView.getAdapter());
        assertTrue("list item is empty", recyclerView.getAdapter().getItemCount() != 0);

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(ImageDetailActivity.class.getName(),null, false);

        // start DetailActivity by clicking list item.
        Espresso.onView(ViewMatchers.withId(android.R.id.list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));

        // do assertion for DetailActivity
        Activity activity = receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("DetailActivity is not null", activity);
        assertEquals("Launched Activity is not ImageDetailActivity", ImageDetailActivity.class, activity.getClass());
    }

}
