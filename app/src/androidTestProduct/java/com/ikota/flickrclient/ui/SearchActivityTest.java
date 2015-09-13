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

import com.ikota.flickrclient.IdlingResource.TimingIdlingResource;
import com.ikota.flickrclient.di.DummyAPIModule;
import com.ikota.flickrclient.network.Util;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import dagger.ObjectGraph;

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
        injectInstrumentation(instrumentation);
    }

    @Test
    public void preConditions() throws Exception {
        setupMockServer(null);
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
        setupMockServer(null);
        activityRule.launchActivity(new Intent());

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(TagListActivity.class.getName(),null, false);

        onView(withId(android.support.v7.appcompat.R.id.search_src_text)).perform(typeText("hogehoge"));
        onView(hasImeAction(EditorInfo.IME_ACTION_GO)).perform(pressImeActionButton());

        // assertion to DetailActivity
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
