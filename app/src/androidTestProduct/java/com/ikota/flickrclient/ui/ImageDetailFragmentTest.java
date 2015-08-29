package com.ikota.flickrclient.ui;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.ikota.flickrclient.R;
import com.ikota.flickrclient.IdlingResource.TextIdlingResource;
import com.ikota.flickrclient.data.DataHolder;
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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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
        setupMockServer(null);
        ImageDetailActivity activity = activityRule.launchActivity(intent);
        ImageDetailFragment fragment = (ImageDetailFragment)activity.getSupportFragmentManager().findFragmentByTag(
                ImageDetailFragment.class.getSimpleName());
        //noinspection ConstantConditions
        TextView use_name = (TextView)fragment.getView().findViewById(R.id.user_name);
        onView(withId(R.id.title)).check(matches(withText("A Quiet Evening")));  // we know its title from list
        onView(withId(R.id.user_name)).check(matches(withText("")));
        onView(withId(R.id.description)).check(matches(withText("")));
        onView(withId(R.id.date_text)).check(matches(withText("")));
        TextIdlingResource idlingResource = new TextIdlingResource(use_name);
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(R.id.title)).check(matches(withText("A Quiet Evening")));
        onView(withId(R.id.user_name)).check(matches(withText("Cole Chase Photography")));
        onView(withId(R.id.description)).check(matches(withText("I was treated to this brilliant sunset on my recent trip to Oregon.  This photo was captured in Ecola State Park which is one of my favorite places to visit on the coast. The deactivated Tillamook Rock Lighthouse is seen on the small island in the distance.  Comments &amp; views are always appreciated. Thanks for looking &amp; have a wonderful Sunday!")));
        onView(withId(R.id.date_text)).check(matches(withText("2015-08-02 22:40:28")));
    }

}