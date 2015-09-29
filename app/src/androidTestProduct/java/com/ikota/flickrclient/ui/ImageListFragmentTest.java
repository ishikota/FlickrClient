package com.ikota.flickrclient.ui;

import android.content.Intent;
import android.os.SystemClock;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;

import com.ikota.flickrclient.R;
import com.ikota.flickrclient.di.PopularListModule;
import com.ikota.flickrclient.network.retrofit.FlickrURL;
import com.ikota.flickrclient.util.IdlingResource.ListCountIdlingResource;
import com.ikota.flickrclient.util.IdlingResource.LoadingIdlingResource;
import com.ikota.flickrclient.util.IdlingResource.TimingIdlingResource;
import com.ikota.flickrclient.util.TestUtil;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.ikota.flickrclient.util.OrientationChangeAction.orientationLandscape;
import static com.ikota.flickrclient.util.OrientationChangeAction.orientationPortrait;


@RunWith(AndroidJUnit4.class)
public class ImageListFragmentTest extends ActivityInstrumentationTestCase2<PopularListActivity> {

    public ImageListFragmentTest() {
        super(PopularListActivity.class);
    }

    @Rule
    public ActivityTestRule<PopularListActivity> activityRule = new ActivityTestRule<>(
            PopularListActivity.class,
            true,     // initialTouchMode
            false);   // launchActivity. False so we can customize the intent per test method

    @Test
    public void testProgress_show() {
        TestUtil.setupMockServer(null);
        PopularListActivity activity = activityRule.launchActivity(new Intent());
        ImageListFragment fragment = (ImageListFragment)activity.getSupportFragmentManager()
                .findFragmentByTag(ImageListFragment.class.getSimpleName());
        @SuppressWarnings("ConstantConditions")
        RecyclerView recyclerView = (RecyclerView)fragment.getView().findViewById(android.R.id.list);

        onView(ViewMatchers.withId(R.id.progress)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        IdlingResource idlingResource = new LoadingIdlingResource(recyclerView);
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(android.R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, scrollTo()));
        Espresso.unregisterIdlingResources(idlingResource);
        onView(withId(R.id.progress)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    // TODO : add test to check if toast is displayed.
    @Test
    public void testEmptyView_show() {
        HashMap<String, String> map = new HashMap<>();
        String empty_response = "{\"photos\":{\"page\":1,\"pages\":25,\"perpage\":20,\"total\":500,\"photo\":[],\"stat\":\"ok\"}";
        map.put(FlickrURL.POPULAR, empty_response);
        TestUtil.setupMockServer(map);
        activityRule.launchActivity(new Intent());

        onView(withId(android.R.id.empty)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.progress)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        TimingIdlingResource idlingResource = new TimingIdlingResource(1000);
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(android.R.id.empty)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void loadNextItems() {
        TestUtil.setupMockServer(null);
        PopularListActivity activity = activityRule.launchActivity(new Intent());
        ImageListFragment fragment = (ImageListFragment)activity.getSupportFragmentManager()
                .findFragmentByTag(ImageListFragment.class.getSimpleName());
        @SuppressWarnings("ConstantConditions")
        RecyclerView recyclerView = (RecyclerView)fragment.getView().findViewById(android.R.id.list);

        ListCountIdlingResource idlingResource_20 = new ListCountIdlingResource(recyclerView, 20);
        Espresso.registerIdlingResources(idlingResource_20);
        onView(withId(android.R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(14, scrollTo()));
        Espresso.unregisterIdlingResources(idlingResource_20);

        ListCountIdlingResource idlingResource_40 = new ListCountIdlingResource(recyclerView, 40);
        Espresso.registerIdlingResources(idlingResource_40);
        onView(withId(android.R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(28, scrollTo()));
        Espresso.unregisterIdlingResources(idlingResource_40);

        ListCountIdlingResource idlingResource_60 = new ListCountIdlingResource(recyclerView, 60);
        Espresso.registerIdlingResources(idlingResource_60);
        onView(withId(android.R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, scrollTo()));
        assertEquals(60, recyclerView.getAdapter().getItemCount());
    }

    /**
     * Reference URL of how to change configuration.
     * https://gist.github.com/Skipants/1001366/874b8d4b51bcd41ea95de408b9558e3cc557fe4f
     */
    @Test
    public void checkColumnNumInDifferentOrientation() {
        TestUtil.setupMockServer(null);
        PopularListActivity activity = activityRule.launchActivity(new Intent());
        ImageListFragment fragment = (ImageListFragment)activity.getSupportFragmentManager()
                .findFragmentByTag(ImageListFragment.class.getSimpleName());
        @SuppressWarnings("ConstantConditions")
        RecyclerView recyclerView = (RecyclerView)fragment.getView().findViewById(android.R.id.list);

        // assertion in  portrait mode
        ListCountIdlingResource idlingResource1 = new ListCountIdlingResource(recyclerView, 1);
        Espresso.registerIdlingResources(idlingResource1);
        onView(withId(android.R.id.list)).perform(swipeLeft());
        Espresso.unregisterIdlingResources(idlingResource1);
        assertEquals(PopularListModule.PORTRAIT_COL,
                ((GridLayoutManager) recyclerView.getLayoutManager()).getSpanCount());
        onView(isRoot()).perform(orientationLandscape());
        SystemClock.sleep(2000);
        recyclerView = (RecyclerView)fragment.getView().findViewById(android.R.id.list);
        ListCountIdlingResource idlingResource2 = new ListCountIdlingResource(recyclerView, 1);
        Espresso.registerIdlingResources(idlingResource2);
        assertEquals(PopularListModule.HORIZONTAL_COL,
                ((GridLayoutManager) recyclerView.getLayoutManager()).getSpanCount());
        Espresso.unregisterIdlingResources(idlingResource2);
        onView(isRoot()).perform(orientationPortrait());
        SystemClock.sleep(2000);
    }

}