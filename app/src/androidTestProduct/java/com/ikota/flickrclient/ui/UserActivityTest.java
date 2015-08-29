package com.ikota.flickrclient.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ikota.flickrclient.IdlingResource.ListCountIdlingResource;
import com.ikota.flickrclient.IdlingResource.TextIdlingResource;
import com.ikota.flickrclient.IdlingResource.TimingIdlingResource;
import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.DataHolder;
import com.ikota.flickrclient.data.model.PhotoInfo;
import com.ikota.flickrclient.di.DummyAPIModule;
import com.ikota.flickrclient.network.Util;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import dagger.ObjectGraph;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkNotNull;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.ikota.flickrclient.OrientationChangeAction.orientationLandscape;
import static com.ikota.flickrclient.OrientationChangeAction.orientationPortrait;
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

    private void setupMockServer(HashMap<String, String> override_map) {
        HashMap<String, String> map = Util.RESPONSE_MAP;

        if(override_map!=null) {
            for (String key : override_map.keySet()) {
                map.put(key, override_map.get(key));
            }
        }

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        AndroidApplication app =
                (AndroidApplication) instrumentation.getTargetContext().getApplicationContext();

        // setup objectGraph to inject Mock API
        List modules = Collections.singletonList(new DummyAPIModule(map));
        ObjectGraph graph = ObjectGraph.create(modules.toArray());
        app.setObjectGraph(graph);
        app.getObjectGraph().inject(app);
    }

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

    /** setupViews failed if you don not use TextIdlingResource */
//    @Test
//    public void setupViews() {
//        activityRule.launchActivity(intent);
//        onView(withId(R.id.user_name)).check(matches(withText("Cole Chase Photography")));
//        onView(withId(R.id.toolbar_actionbar)).check(matches(withAlpha(is(0))));
//        onView(withId(android.R.id.list)).check(matches(isDisplayed()));
//    }

    @Test
    public void setupViews() {
        setupMockServer(null);
        UserActivity activity = activityRule.launchActivity(intent);
        TextIdlingResource idlingResource = new TextIdlingResource((TextView)activity.findViewById(R.id.user_name));
        onView(withId(R.id.user_name)).check(matches(withText("Cole Chase Photography")));
        //onView(withId(R.id.toolbar_actionbar)).check(matches(withAlpha(is(0))));
        onView(allOf(withId(android.R.id.list), isDisplayed())).check(matches(isDisplayed()));
        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void loadPeopleInfo() {
        setupMockServer(null);
        UserActivity activity = activityRule.launchActivity(intent);
        TextIdlingResource idlingResource = new TextIdlingResource((TextView)activity.findViewById(R.id.sub_text));
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(R.id.sub_text)).check(matches(withText("988 followers | 988 views")));
        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void actionbarAlphaChange() {
        setupMockServer(null);
        UserActivity activity = activityRule.launchActivity(intent);
        ListCountIdlingResource idlingResource = new ListCountIdlingResource((RecyclerView)activity.findViewById(android.R.id.list), 1);
        Espresso.registerIdlingResources(idlingResource);
        onView(allOf(withId(android.R.id.list), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(12, scrollTo()));
        Espresso.unregisterIdlingResources(idlingResource);
        onView(withId(R.id.toolbar_actionbar)).check(matches(withAlpha(is(255))));
    }

    @Test
    public void tab_supportOrientationChange() {
        setupMockServer(null);
        UserActivity activity = activityRule.launchActivity(intent);
        ListCountIdlingResource idlingResource = new ListCountIdlingResource((RecyclerView)activity.findViewById(android.R.id.list), 1);
        Espresso.registerIdlingResources(idlingResource);
        onView(allOf(withId(android.R.id.list), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(9, scrollTo()));
        Espresso.unregisterIdlingResources(idlingResource);

        // change orientation to landscape
        onView(isRoot()).perform(orientationLandscape());

        onView(withId(R.id.toolbar_actionbar)).check(matches(withAlpha(is(255))));

        onView(isRoot()).perform(orientationPortrait());
        SystemClock.sleep(2000);

    }

    @Test
    public void tab_Go_detail_orientation_change_back() {
        setupMockServer(null);
        // setup
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(ImageDetailActivity.class.getName(),null, false);

        // go list and tap to go detail
        UserActivity activity = activityRule.launchActivity(intent);
        ListCountIdlingResource idlingResource = new ListCountIdlingResource((RecyclerView)activity.findViewById(android.R.id.list), 1);
        Espresso.registerIdlingResources(idlingResource);
        onView(allOf(withId(android.R.id.list), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(12, scrollTo()));
        Espresso.unregisterIdlingResources(idlingResource);
        onView(withId(R.id.toolbar_actionbar)).check(matches(withAlpha(is(255))));
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
        onView(withId(R.id.toolbar_actionbar)).check(matches(withAlpha(is(255))));
        onView(isRoot()).perform(orientationPortrait());
        SystemClock.sleep(2000);
    }

    @Test
    public void tab_goDetailKeepTabHeight() {
        setupMockServer(null);
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
        onView(withId(R.id.tab_layout)).check(matches(withY(is(expected))));
        Espresso.unregisterIdlingResources(ti);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static Matcher<View> withAlpha(final Matcher<Integer> alphaMatcher) {
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static Matcher<View> withY(final Matcher<Float> yMatcher) {
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
