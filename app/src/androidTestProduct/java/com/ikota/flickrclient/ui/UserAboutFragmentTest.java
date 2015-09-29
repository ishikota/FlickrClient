package com.ikota.flickrclient.ui;


import android.app.Activity;
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

import com.google.gson.Gson;
import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.DataHolder;
import com.ikota.flickrclient.data.model.PhotoInfo;
import com.ikota.flickrclient.network.retrofit.FlickrURL;
import com.ikota.flickrclient.util.IdlingResource.TextIdlingResource;
import com.ikota.flickrclient.util.IdlingResource.TimingIdlingResource;
import com.ikota.flickrclient.util.TestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.ikota.flickrclient.util.CustomMatchers.startsWith;

@RunWith(AndroidJUnit4.class)
public class UserAboutFragmentTest extends ActivityInstrumentationTestCase2<UserActivity>{

    Context context;
    Intent intent;

    public UserAboutFragmentTest() {
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

    @Test
    public void setupViews() {
        TestUtil.setupMockServer(null);
        UserActivity activity = activityRule.launchActivity(intent);
        String about = context.getResources().getString(R.string.tab_title_3);
        onView(withText(about)).perform(click());
        // wait page loading
        UserAboutFragment fragment = (UserAboutFragment)activity.getSupportFragmentManager().findFragmentByTag("f3");
        //noinspection ConstantConditions
        TextIdlingResource ti = new TextIdlingResource((TextView)fragment.getView().findViewById(R.id.description));
        Espresso.registerIdlingResources(ti);
        onView(withId(R.id.description)).check(matches(startsWith("Thank")));
        onView(withId(R.id.location)).check(matches(withText("Iowa City, USA")));
        onView(withContentDescription("Flickr")).check(matches(withText("Flickr")));
        Espresso.unregisterIdlingResources(ti);
    }

    @Test
    public void testVisibility() {
        HashMap<String, String> map = new HashMap<>();
        map.put(FlickrURL.PEOPLE_INFO, DataHolder.PEOPLE_NO_INFO);
        TestUtil.setupMockServer(map);
        activityRule.launchActivity(intent);
        String about = context.getResources().getString(R.string.tab_title_3);
        onView(withText(about)).perform(click());
        // wait page loading
        TimingIdlingResource ti = new TimingIdlingResource(3);
        Espresso.registerIdlingResources(ti);
        onView(withId(R.id.description)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.location)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withContentDescription("Flickr")).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        Espresso.unregisterIdlingResources(ti);
    }

    @Test
    public void startWebActivity() {
        HashMap<String, String> map = new HashMap<>();
        map.put(FlickrURL.PEOPLE_INFO, DataHolder.PEOPLE_NO_INFO);
        TestUtil.setupMockServer(map);
        activityRule.launchActivity(intent);
        String about = context.getResources().getString(R.string.tab_title_3);
        onView(withText(about)).perform(click());
        onView(withContentDescription("Flickr")).check(matches(withText("Flickr")));

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(WebViewActivity.class.getName(), null, false);
        onView(withContentDescription("Flickr")).perform(click());
        Activity web_activity = receiverActivityMonitor.waitForActivityWithTimeout(1000);
        getInstrumentation().removeMonitor(receiverActivityMonitor);

        Intent intent = web_activity.getIntent();
        assertEquals("Flickr Client", intent.getStringExtra("title"));
        assertEquals("https://www.flickr.com/people/131498071@N04/", intent.getStringExtra("url"));

    }
}
