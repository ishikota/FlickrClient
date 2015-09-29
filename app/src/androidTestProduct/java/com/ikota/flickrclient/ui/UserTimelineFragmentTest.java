package com.ikota.flickrclient.ui;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;

import com.google.gson.Gson;
import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.DataHolder;
import com.ikota.flickrclient.data.model.PhotoInfo;
import com.ikota.flickrclient.util.IdlingResource.ListCountIdlingResource;
import com.ikota.flickrclient.util.TestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.ikota.flickrclient.util.CustomActions.clickChildViewWithId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;

@RunWith(AndroidJUnit4.class)
public class UserTimelineFragmentTest extends ActivityInstrumentationTestCase2<UserActivity>{

    Context context;
    Intent intent;

    public UserTimelineFragmentTest() {
        super(UserActivity.class);
    }

    @Rule
    public ActivityTestRule<UserActivity> activityRule = new ActivityTestRule<>(
            UserActivity.class,
            true,     // initialTouchMode
            false);   // launchActivity. False so we can customize the intent per test method

    @Before
    public void setUp() throws Exception{
        super.setUp();
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        context = instrumentation.getTargetContext();
        injectInstrumentation(instrumentation);

        Gson gson = new Gson();
        PhotoInfo info = gson.fromJson(DataHolder.DETAIL_JSON, PhotoInfo.class);
        String json = gson.toJson(info.photo.owner);
        intent = new Intent(context, UserActivity.class);
        intent.putExtra(UserActivity.EXTRA_CONTENT, json);
    }

    /** This test sometimes fails. I seems that some other test affect this test result. */
    @Test
    public void setupViews() {
        TestUtil.setupMockServer(null);
        UserActivity activity = activityRule.launchActivity(intent);
        String favorite = context.getResources().getString(R.string.tab_title_2);
        onView(withText(favorite)).perform(click());
        // wait page loading
        UserTimelineFragment fragment = (UserTimelineFragment)activity.getSupportFragmentManager().findFragmentByTag("f2");
        //noinspection ConstantConditions
        RecyclerView rv = (RecyclerView)fragment.getView().findViewById(android.R.id.list);
        ListCountIdlingResource ti = new ListCountIdlingResource(rv, 1);
        Espresso.registerIdlingResources(ti);
        onView(allOf(hasSibling(withText("Evening mirror")), withId(R.id.date_text))).check(matches(withText("2015-04-29 15:22:34")));
        Espresso.unregisterIdlingResources(ti);
    }

    private void startCommentDialog(UserActivity activity) {
        String favorite = context.getResources().getString(R.string.tab_title_2);
        onView(withText(favorite)).perform(click());
        // wait page loading
        UserTimelineFragment fragment = (UserTimelineFragment)activity.getSupportFragmentManager().findFragmentByTag("f2");
        //noinspection ConstantConditions
        RecyclerView rv = (RecyclerView)fragment.getView().findViewById(android.R.id.list);
        ListCountIdlingResource ti = new ListCountIdlingResource(rv, 1);
        Espresso.registerIdlingResources(ti);
        onView(allOf(hasSibling(withText("Evening mirror")), withId(R.id.date_text))).check(matches(withText("2015-04-29 15:22:34")));
        onView(allOf(withId(android.R.id.list), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.ic_comment)));
        Espresso.unregisterIdlingResources(ti);
        SystemClock.sleep(1000); // wait CommentDialog start
    }

    @Test
    public void commentPost() {
        TestUtil.setupMockServer(null);
        UserActivity activity = activityRule.launchActivity(intent);
        startCommentDialog(activity);
        onView(withId(R.id.title)).check(matches(withText("Evening mirror")));
        onView(withId(R.id.comment_edit)).perform(typeText("Great"));
        SystemClock.sleep(1000);
        onView(withId(R.id.ic_comment)).perform(click());
        SystemClock.sleep(2100);
        onView(withText(R.string.posted)).check(matches(withText(R.string.posted)));
    }

    @Test(expected = NoMatchingViewException.class)
    public void emptyCommentPost() {
        TestUtil.setupMockServer(null);
        UserActivity activity = activityRule.launchActivity(intent);
        startCommentDialog(activity);
        onView(withId(R.id.title)).check(matches(withText("Evening mirror")));
        SystemClock.sleep(1000);
        onView(withId(R.id.ic_comment)).perform(click());
        SystemClock.sleep(2100);
        onView(withText(R.string.posted)).check(matches(withText(R.string.posted)));
    }

    @Test
    public void commentSave() {
        TestUtil.setupMockServer(null);
        UserActivity activity = activityRule.launchActivity(intent);
        String target1 = "hoge hoge";
        String target2 = "Hoge hoge";
        startCommentDialog(activity);
        onView(withId(R.id.comment_edit)).perform(typeText(target2));
        pressBack();pressBack();  // close IME, finish dialog
        SystemClock.sleep(2000);
        startCommentDialog(activity);
        onView(withId(R.id.comment_edit)).check(matches(anyOf(withText(target1), withText(target2))));
    }

    @Test
    public void commentPost_draftDelete() {
        TestUtil.setupMockServer(null);
        UserActivity activity = activityRule.launchActivity(intent);
        String target = "hoge hoge";
        startCommentDialog(activity);
        onView(withId(R.id.comment_edit)).perform(typeText(target));
        onView(withId(R.id.ic_comment)).perform(click());
        SystemClock.sleep(2000);
        startCommentDialog(activity);
        onView(withId(R.id.comment_edit)).check(matches(withText("")));
    }

}
