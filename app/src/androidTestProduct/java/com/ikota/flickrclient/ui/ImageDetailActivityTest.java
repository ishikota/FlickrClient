package com.ikota.flickrclient.ui;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.DataHolder;
import com.ikota.flickrclient.util.TestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ImageDetailActivityTest extends ActivityInstrumentationTestCase2<ImageDetailActivity>{

    private Intent intent;

    public ImageDetailActivityTest() {
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
        Context context = instrumentation.getTargetContext();
        intent = new Intent(context, ImageDetailActivity.class);
        intent.putExtra(ImageDetailActivity.EXTRA_CONTENT, DataHolder.LIST_ITEM_JSON);
        intent.putExtra(ImageDetailActivity.EXTRA_CACHE_SIZE, "");
    }

    @Test
    public void clickFavo() {
        TestUtil.setupMockServer(null);
        activityRule.launchActivity(intent);
        onView(withId(R.id.favorite_num)).check(matches(withText("644")));
        onView(withId(R.id.ic_favorite)).perform(click());
        onView(withId(R.id.favorite_num)).check(matches(withText("645")));
    }

    @Test
    public void clickComment() {
        TestUtil.setupMockServer(null);
        activityRule.launchActivity(intent);
        onView(withId(R.id.ic_comment)).perform(click());
        onView(withId(R.id.title)).check(matches(withText("A Quiet Evening")));
    }

    @Test
    public void clickDownload() {
        TestUtil.setupMockServer(null);
        activityRule.launchActivity(intent);
        onView(withId(R.id.ic_download)).perform(click());
        onView(withText("Loading...")).check(matches(withText("Loading...")));
    }

}
