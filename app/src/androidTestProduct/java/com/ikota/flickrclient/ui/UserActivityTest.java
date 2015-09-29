package com.ikota.flickrclient.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.DataHolder;
import com.ikota.flickrclient.data.model.PhotoInfo;
import com.ikota.flickrclient.network.retrofit.FlickrURL;
import com.ikota.flickrclient.util.IdlingResource.ListCountIdlingResource;
import com.ikota.flickrclient.util.IdlingResource.TextIdlingResource;
import com.ikota.flickrclient.util.IdlingResource.TimingIdlingResource;
import com.ikota.flickrclient.util.TestUtil;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkNotNull;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.ikota.flickrclient.util.OrientationChangeAction.orientationLandscape;
import static com.ikota.flickrclient.util.OrientationChangeAction.orientationPortrait;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.Is.is;


@RunWith(AndroidJUnit4.class)
public class UserActivityTest extends ActivityInstrumentationTestCase2<UserActivity>{

    private Intent intent;

    public UserActivityTest() {
        super(UserActivity.class);
    }

    @Rule
    public ActivityTestRule<UserActivity> activityRule = new ActivityTestRule<>(
            UserActivity.class,
            true,     // initialTouchMode
            false);   // launchActivity. False so we can customize the intent per test method

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        Context context = instrumentation.getTargetContext();
        injectInstrumentation(instrumentation);

        Gson gson = new Gson();
        PhotoInfo info = gson.fromJson(DataHolder.DETAIL_JSON, PhotoInfo.class);
        String json = gson.toJson(info.photo.owner);

        intent = new Intent(context, UserActivity.class);
        intent.putExtra(UserActivity.EXTRA_CONTENT, json);
    }

    @Test
    public void setupViews() {
        TestUtil.setupMockServer(null);
        UserActivity activity = activityRule.launchActivity(intent);
        TextIdlingResource idlingResource = new TextIdlingResource((TextView)activity.findViewById(R.id.user_name));
        onView(withId(R.id.user_name)).check(matches(withText("Cole Chase Photography")));
        onView(withId(R.id.toolbar_actionbar)).check(matches(withAlpha(0)));
        onView(allOf(withId(android.R.id.list), isDisplayed())).check(matches(isDisplayed()));
        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void loadPeopleInfo() {
        TestUtil.setupMockServer(null);
        UserActivity activity = activityRule.launchActivity(intent);
        TextIdlingResource idlingResource = new TextIdlingResource((TextView)activity.findViewById(R.id.sub_text));
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(R.id.sub_text)).check(matches(withText("988 followers | 988 views")));
        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void actionbarAlphaChange() {
        TestUtil.setupMockServer(null);
        UserActivity activity = activityRule.launchActivity(intent);
        ListCountIdlingResource idlingResource = new ListCountIdlingResource((RecyclerView)activity.findViewById(android.R.id.list), 1);
        Espresso.registerIdlingResources(idlingResource);
        onView(allOf(withId(android.R.id.list), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(12, scrollTo()));
        Espresso.unregisterIdlingResources(idlingResource);
        onView(withId(R.id.toolbar_actionbar)).check(matches(withAlpha(255)));
    }

    @Test
    public void tab_supportOrientationChange() {
        TestUtil.setupMockServer(null);
        UserActivity activity = activityRule.launchActivity(intent);
        ListCountIdlingResource idlingResource = new ListCountIdlingResource((RecyclerView)activity.findViewById(android.R.id.list), 1);
        Espresso.registerIdlingResources(idlingResource);
        onView(allOf(withId(android.R.id.list), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(9, scrollTo()));
        Espresso.unregisterIdlingResources(idlingResource);

        // change orientation to landscape
        onView(isRoot()).perform(orientationLandscape());

        onView(withId(R.id.toolbar_actionbar)).check(matches(withAlpha(255)));

        onView(isRoot()).perform(orientationPortrait());
        SystemClock.sleep(2000);

    }

    @Test
    public void tab_Go_detail_orientation_change_back() {
        TestUtil.setupMockServer(null);
        // setup
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(ImageDetailActivity.class.getName(),null, false);

        // go list and tap to go detail
        UserActivity activity = activityRule.launchActivity(intent);
        ListCountIdlingResource idlingResource = new ListCountIdlingResource((RecyclerView)activity.findViewById(android.R.id.list), 1);
        Espresso.registerIdlingResources(idlingResource);
        onView(allOf(withId(android.R.id.list), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(12, scrollTo()));
        Espresso.unregisterIdlingResources(idlingResource);
        onView(withId(R.id.toolbar_actionbar)).check(matches(withAlpha(255)));
        onView(allOf(withId(android.R.id.list), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(12, click()));

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);

        // assertion to DetailActivity
        Activity detail_activity = receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("DetailActivity is not null", detail_activity);

        // rotate device
        onView(isRoot()).perform(orientationLandscape());

        // back to list
        pressBack();
        onView(withId(R.id.toolbar_actionbar)).check(matches(withAlpha(255)));
        onView(isRoot()).perform(orientationPortrait());
        SystemClock.sleep(2000);
    }

    @Test
    public void tab_goDetailKeepTabHeight() {
        TestUtil.setupMockServer(null);
        // setup
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(ImageDetailActivity.class.getName(),null, false);

        // Wait until list is displayed
        UserActivity activity = activityRule.launchActivity(intent);
        ListCountIdlingResource idlingResource = new ListCountIdlingResource((RecyclerView)activity.findViewById(android.R.id.list), 1);
        Espresso.registerIdlingResources(idlingResource);
        onView(allOf(withId(android.R.id.list), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(18, scrollTo()));
        Espresso.unregisterIdlingResources(idlingResource);
        float expected = activity.findViewById(R.id.tab_layout).getY();

        // Switch tab and go detail
        Resources r = activity.getResources();
        onView(withText(r.getString(R.string.tab_title_3))).perform(click());
        onView(withText(r.getString(R.string.tab_title_1))).perform(click());
        onView(allOf(withId(android.R.id.list), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(18, click()));

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);

        // assertion to DetailActivity
        Activity detail_activity = receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("DetailActivity is not null", detail_activity);

        // back to list
        pressBack();
        TimingIdlingResource ti = new TimingIdlingResource(2000);
        Espresso.registerIdlingResources(ti);
        onView(withId(R.id.tab_layout)).check(matches(withY(expected)));
        Espresso.unregisterIdlingResources(ti);
    }

    @Test
    public void noTextEmptyView() {
        HashMap<String, String> map = new HashMap<>();
        String empty_response = "{\"photos\":{\"page\":1,\"pages\":25,\"perpage\":20,\"total\":500,\"photo\":[],\"stat\":\"ok\"}";
        map.put(FlickrURL.PEOPLE_FAVORITE, empty_response);
        map.put(FlickrURL.PUBLIC_PHOTO, DataHolder.PEOPLE_PUBLIC_PHOTO);
        TestUtil.setupMockServer(map);
        // go list and tap to go detail
        activityRule.launchActivity(intent);
        onView(withText(R.string.tab_title_2)).perform(click());
        TimingIdlingResource idlingResource = new TimingIdlingResource(3000);
        Espresso.registerIdlingResources(idlingResource);
        onView(allOf(withId(android.R.id.empty), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).check(matches(withText("")));
        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void keepTabHeightWhenTabSwitch() {
        TestUtil.setupMockServer(null);
        UserActivity activity = activityRule.launchActivity(intent);
        TimingIdlingResource idlingResource = new TimingIdlingResource(3000);
        Espresso.registerIdlingResources(idlingResource);
        onView(allOf(withId(android.R.id.list), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(RecyclerViewActions.scrollToPosition(12));
        Espresso.unregisterIdlingResources(idlingResource);
        float expected = activity.findViewById(R.id.tab_layout).getY();
        onView(withText("FAVORITE")).perform(click());
        TimingIdlingResource idlingResource1 = new TimingIdlingResource(100);
        Espresso.registerIdlingResources(idlingResource1);
        onView(withId(R.id.tab_layout)).check(matches(withY(expected)));
        Espresso.unregisterIdlingResources(idlingResource1);
    }

    private static Matcher<View> withAlpha(final int expected_alpha) {
        final Matcher<Integer> alphaMatcher = is(expected_alpha);
        checkNotNull(alphaMatcher);
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("with alpha: ");
                alphaMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(View view) {
                return alphaMatcher.matches(view.getBackground().getAlpha());
            }
        };
    }

    private static Matcher<View> withY(final Float expected_y) {
        final Matcher<Float> yMatcher = is(expected_y);
        checkNotNull(yMatcher);
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("with y: ");
                yMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(View view) {
                return yMatcher.matches(view.getY());
            }
        };
    }
}
