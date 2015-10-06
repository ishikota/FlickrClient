package com.ikota.flickrclient.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.DataHolder;
import com.ikota.flickrclient.data.model.PhotoInfo;
import com.ikota.flickrclient.network.retrofit.FlickrURL;
import com.ikota.flickrclient.util.IdlingResource.ListCountIdlingResource;
import com.ikota.flickrclient.util.IdlingResource.TimingIdlingResource;
import com.ikota.flickrclient.util.OrientationChangeAction;
import com.ikota.flickrclient.util.TestUtil;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.ikota.flickrclient.util.CustomMatchers.startsWith;
import static org.hamcrest.core.Is.is;

@RunWith(AndroidJUnit4.class)
public class ImageDetailFragmentTest extends ActivityInstrumentationTestCase2<ImageDetailActivity> {

    private Intent intent;

    public ImageDetailFragmentTest() {
        super(ImageDetailActivity.class);
    }

    @Rule
    public ActivityTestRule<ImageDetailActivity> activityRule = new ActivityTestRule<>(
            ImageDetailActivity.class,
            true,     // initialTouchMode
            false);   // launchActivity. False so we can customize the intent per test method

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        injectInstrumentation(instrumentation);  // need to use ActivityMonitor
        Context context = instrumentation.getTargetContext();
        intent = new Intent(context, ImageDetailActivity.class);
        intent.putExtra(ImageDetailActivity.EXTRA_CONTENT, DataHolder.LIST_ITEM_JSON);
        intent.putExtra(ImageDetailActivity.EXTRA_CACHE_SIZE, "");
    }

    @Test
    public void preConditions() {
        ImageDetailActivity activity = activityRule.launchActivity(intent);
        onView(withId(R.id.container)).check(matches(ViewMatchers.withContentDescription(R.string.detail_container)));
        String args = activity.getIntent().getStringExtra(ImageDetailActivity.EXTRA_CONTENT);
        assertEquals(args, DataHolder.LIST_ITEM_JSON);
    }

    /** TODO: Passed List data and detail data is wrong item */
    @Test
    public void checkDetailInfo_isSet() {
        TestUtil.setupMockServer(null);
        activityRule.launchActivity(intent);
        onView(withId(R.id.title)).check(matches(withText("A Quiet Evening")));  // we know its title from list
        onView(withId(R.id.user_name)).check(matches(withText("")));
        onView(withId(R.id.description)).check(matches(withText("")));
        onView(withId(R.id.date_text)).check(matches(withText("")));
        TimingIdlingResource idlingResource = new TimingIdlingResource(3000);  // wait dummy network operation
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(R.id.title)).check(matches(withText("Sundown on the Oregon Coast")));
        onView(withId(R.id.user_name)).check(matches(withText("Cole Chase Photography")));
        onView(withId(R.id.description)).check(matches(startsWith("I was treated to this")));
        onView(withId(R.id.date_text)).check(matches(withText("Posted on 2015-08-02 22:40:28")));
        onView(withText("C'est magnifique !")).check(matches(isDisplayed()));
        onView(withText("A wonderful moment")).check(matches(withText("A wonderful moment")));
        onView(withText("beautiful capture Cole")).check(matches(withText("beautiful capture Cole")));
        onView(withText("See All 35 Comments")).check(matches(withText("See All 35 Comments")));
        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void testDescriptionExpand() {
        TestUtil.setupMockServer(null);
        activityRule.launchActivity(intent);
        TimingIdlingResource idlingResource = new TimingIdlingResource(3000);  // wait dummy network operation
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(R.id.description)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.description_parent)).perform(click());
        onView(withId(R.id.description)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void goUserPageFromComment() {
        TestUtil.setupMockServer(null);
        activityRule.launchActivity(intent);
        TimingIdlingResource idlingResource = new TimingIdlingResource(3000);  // wait dummy network operation
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(R.id.comment_parent)).perform(clickComment());
        Espresso.unregisterIdlingResources(idlingResource);
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                InstrumentationRegistry.getInstrumentation().addMonitor(UserActivity.class.getName(), null, false);
        Activity activity = receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertEquals("Launched Activity is not UserActivity", UserActivity.class, activity.getClass());
        onView(withText("¡! Nature B■x !¡")).check(matches(isDisplayed()));
        pressBack();
        SystemClock.sleep(2000);
    }

    @Test
    public void nullCommentHandling() {
        HashMap<String,String> map = new HashMap<>();
        map.put(FlickrURL.COMMENT_LIST, "{\"comments\":{\"photo_id\":\"21043155476\"},\"stat\":\"ok\"}");
        TestUtil.setupMockServer(map);
        activityRule.launchActivity(intent);
        SystemClock.sleep(3000);
        onView(withContentDescription("No Comment")).check(matches(withText(R.string.no_comment)));
    }

    @Test
    public void clickRelatedImageAndGoDetail() throws Exception {
        TestUtil.setupMockServer(null);
        ImageDetailActivity activity = activityRule.launchActivity(intent);
        ImageDetailFragment fragment = (ImageDetailFragment)activity.getSupportFragmentManager()
                .findFragmentByTag(ImageDetailFragment.class.getSimpleName());
        @SuppressWarnings("ConstantConditions")
        RecyclerView recyclerView = (RecyclerView)fragment.getView().findViewById(android.R.id.list);

        ListCountIdlingResource idlingResource = new ListCountIdlingResource(recyclerView, 2);
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(android.R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(5, scrollTo()));
        Espresso.unregisterIdlingResources(idlingResource);

        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(ImageDetailActivity.class.getName(), null, false);
        onView(withId(android.R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        Activity detail_activity = receiverActivityMonitor.waitForActivityWithTimeout(1000);
        getInstrumentation().removeMonitor(receiverActivityMonitor);
        assertNotNull("Failed to go detail page of related item", detail_activity);

        String args = detail_activity.getIntent().getStringExtra(ImageDetailActivity.EXTRA_CONTENT);
        JSONObject jo = new JSONObject(args);
        assertEquals("20921309510", jo.getString("id"));

    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void checkColumnNumInDifferentOrientation() {
        TestUtil.setupMockServer(null);
        ImageDetailActivity activity = activityRule.launchActivity(intent);
        ImageDetailFragment fragment = (ImageDetailFragment)activity.getSupportFragmentManager()
                .findFragmentByTag(ImageDetailFragment.class.getSimpleName());
        RecyclerView recyclerView = (RecyclerView)fragment.getView().findViewById(android.R.id.list);

        ListCountIdlingResource idlingResource = new ListCountIdlingResource(recyclerView, 2);
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(android.R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(5, scrollTo()));
        Espresso.unregisterIdlingResources(idlingResource);

        int portrait_column = ((GridLayoutManager)recyclerView.getLayoutManager()).getSpanCount();
        assertEquals(2, portrait_column);

        onView(isRoot()).perform(OrientationChangeAction.orientationLandscape());
        SystemClock.sleep(3000);
        fragment = (ImageDetailFragment)activity.getSupportFragmentManager()
                .findFragmentByTag(ImageDetailFragment.class.getSimpleName());
        recyclerView = (RecyclerView)fragment.getView().findViewById(android.R.id.list);

        int landscape_column = ((GridLayoutManager)recyclerView.getLayoutManager()).getSpanCount();
        assertEquals(3, landscape_column);

        onView(isRoot()).perform(OrientationChangeAction.orientationPortrait());
        SystemClock.sleep(2000);
    }

    @Test
    public void when_no_tag_attached() {
        Gson gson = new Gson();
        PhotoInfo info = gson.fromJson(DataHolder.DETAIL_JSON, PhotoInfo.class);
        info.photo.tags.tag = new ArrayList<>();
        String no_tag_detail = gson.toJson(info);
        HashMap<String, String> map = new HashMap<>();
        map.put(FlickrURL.PHOTO_INFO, no_tag_detail);

        TestUtil.setupMockServer(map);
        activityRule.launchActivity(intent);
        onView(withId(R.id.related_progress)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        SystemClock.sleep(5000);
        onView(withId(R.id.related_progress)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.related_title)).check(matches(withText(R.string.no_related_images)));
    }

    public static Matcher<View> withLines(final int lines) {
        final Matcher<Integer> lineMatcher = is(lines);
        return new BoundedMatcher<View, TextView>(TextView.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("with lines : ");
                lineMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(TextView textView) {
                return lines == textView.getLayout().getLineCount();
            }
        };
    }

    public static ViewAction clickComment() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(LinearLayout.class);
            }

            @Override
            public String getDescription() {
                return "click comment:";
            }

            @Override
            public void perform(UiController uiController, View view) {
                LinearLayout parent = (LinearLayout)view;
                parent.getChildAt(0).performClick();
            }
        };
    }

}