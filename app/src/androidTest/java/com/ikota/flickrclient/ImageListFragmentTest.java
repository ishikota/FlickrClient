package com.ikota.flickrclient;

import android.content.Intent;
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

import com.ikota.flickrclient.ui.BaseActivity;
import com.ikota.flickrclient.ui.FlickrAPIModule;
import com.ikota.flickrclient.ui.MainActivity;
import com.ikota.flickrclient.ui.MainApplication;
import com.ikota.flickrclient.ui.PopularListFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by kota on 2015/08/20.
 *
 */
@RunWith(AndroidJUnit4.class)
public class ImageListFragmentTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyView;

    private MainApplication app;

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
    }

    @Test
    public void testProgress_show() {
        Intent intent = new Intent();
        intent.putExtra(BaseActivity.KEY_REAL_SERVER, false);
        MainActivity activity = activityRule.launchActivity(intent);
        activity.injectApiModule(new FlickrAPIModule());

        PopularListFragment fragment = (PopularListFragment)activity.getSupportFragmentManager()
                .findFragmentByTag(PopularListFragment.class.getSimpleName());
        recyclerView = (RecyclerView)fragment.getView().findViewById(android.R.id.list);
        progressBar = (ProgressBar)fragment.getView().findViewById(R.id.progress);
        emptyView = (TextView)fragment.getView().findViewById(android.R.id.empty);

        assertEquals(View.VISIBLE, progressBar.getVisibility());
        IdlingResource idlingResource = new LoadingIdlingResource(recyclerView);
        Espresso.registerIdlingResources(idlingResource);
        Espresso.onView(ViewMatchers.withId(android.R.id.list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.swipeLeft()));
        Espresso.unregisterIdlingResources(idlingResource);
        assertEquals(View.GONE, progressBar.getVisibility());
        assertEquals(View.GONE, emptyView.getVisibility());
    }

    private class LoadingIdlingResource implements IdlingResource {

        private ResourceCallback resourceCallback;
        private RecyclerView recyclerView;

        private LoadingIdlingResource(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        public String getName() {
            return LoadingIdlingResource.class.getSimpleName();
        }

        @Override
        public boolean isIdleNow() {
            // check if recyclerView is set loaded items
            boolean idle = isItemLoaded(recyclerView);
            if (idle && resourceCallback != null) {
                resourceCallback.onTransitionToIdle();
            }
            return idle;
        }

        @Override
        public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
            this.resourceCallback = resourceCallback;
        }

        private boolean isItemLoaded(RecyclerView list) {
            return list.getAdapter()!=null && list.getAdapter().getItemCount() != 0;
        }
    }

}