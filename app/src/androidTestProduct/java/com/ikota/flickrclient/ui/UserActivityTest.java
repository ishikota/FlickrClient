package com.ikota.flickrclient.ui;

import android.annotation.TargetApi;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.DataHolder;
import com.ikota.flickrclient.data.model.PhotoInfo;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkNotNull;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.ikota.flickrclient.OrientationChangeAction.orientationLandscape;
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
        UserActivity activity = activityRule.launchActivity(intent);
        TextIdlingResource idlingResource = new TextIdlingResource((TextView)activity.findViewById(R.id.user_name));
        onView(withId(R.id.user_name)).check(matches(withText("Cole Chase Photography")));
        //onView(withId(R.id.toolbar_actionbar)).check(matches(withAlpha(is(0))));
        onView(withId(android.R.id.list)).check(matches(isDisplayed()));
        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void loadPeopleInfo() {
        UserActivity activity = activityRule.launchActivity(intent);
        TextIdlingResource idlingResource = new TextIdlingResource((TextView)activity.findViewById(R.id.sub_text));
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(R.id.sub_text)).check(matches(withText("988 followers | 988 views")));
        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void actionbarAlphaChange() {
        UserActivity activity = activityRule.launchActivity(intent);
        ListCountIdlingResource idlingResource = new ListCountIdlingResource((RecyclerView)activity.findViewById(android.R.id.list), 1);
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(android.R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(12, scrollTo()));
        Espresso.unregisterIdlingResources(idlingResource);
        onView(withId(R.id.toolbar_actionbar)).check(matches(withAlpha(is(255))));
    }

    @Test
    public void tab_supportOrientationChange() {
        UserActivity activity = activityRule.launchActivity(intent);
        ListCountIdlingResource idlingResource = new ListCountIdlingResource((RecyclerView)activity.findViewById(android.R.id.list), 1);
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(android.R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(9, scrollTo()));
        Espresso.unregisterIdlingResources(idlingResource);

        // change orientation to landscape
        onView(isRoot()).perform(orientationLandscape());

        onView(withId(R.id.toolbar_actionbar)).check(matches(withAlpha(is(255))));

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
}
