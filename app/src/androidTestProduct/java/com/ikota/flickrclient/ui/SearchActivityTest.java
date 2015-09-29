package com.ikota.flickrclient.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.view.inputmethod.EditorInfo;

import com.ikota.flickrclient.util.IdlingResource.TimingIdlingResource;
import com.ikota.flickrclient.util.TestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasImeAction;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SearchActivityTest extends ActivityInstrumentationTestCase2<SearchActivity>{

    public SearchActivityTest() {
        super(SearchActivity.class);
    }

    @Rule
    public ActivityTestRule<SearchActivity> activityRule = new ActivityTestRule<>(
            SearchActivity.class,
            true,     // initialTouchMode
            false);   // launchActivity. False so we can customize the intent per test method

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        injectInstrumentation(instrumentation);
    }

    @Test
    public void preConditions() throws Exception {
        TestUtil.setupMockServer(null);
        activityRule.launchActivity(new Intent());
        TimingIdlingResource idlingResource = new TimingIdlingResource(3000);
        Espresso.registerIdlingResources(idlingResource);
        onView(withText("feb23")).check(matches(withText("feb23")));
        Espresso.unregisterIdlingResources(idlingResource);
        onView(withId(android.R.id.list)).perform(RecyclerViewActions.scrollToPosition(19));
        onView(withText("leicammonochrom")).check(matches(withText("leicammonochrom")));
    }

    @Test
    public void searchAndGo() {
        TestUtil.setupMockServer(null);
        activityRule.launchActivity(new Intent());

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(TagListActivity.class.getName(),null, false);

        onView(withId(android.support.v7.appcompat.R.id.search_src_text)).perform(typeText("hogehoge"));
        onView(hasImeAction(EditorInfo.IME_ACTION_GO)).perform(pressImeActionButton());

        Activity activity = receiverActivityMonitor.waitForActivityWithTimeout(1000);
        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);
        assertNotNull("TagListActivity is not null", activity);
        assertEquals("Launched Activity is not TagListActivity", TagListActivity.class, activity.getClass());

        Intent intent = activity.getIntent();
        String tag = intent.getStringExtra(TagListActivity.EXTRA_TAG);
        assertEquals("hogehoge", tag);
    }

}
