package com.ikota.flickrclient.ui;


import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import com.google.gson.Gson;
import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.DataHolder;
import com.ikota.flickrclient.data.model.ListData;
import com.ikota.flickrclient.util.TestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.ikota.flickrclient.util.CustomMatchers.withChildCount;

@RunWith(AndroidJUnit4.class)
public class CommentListActivityTest extends ActivityInstrumentationTestCase2<CommentListActivity> {

    private Intent intent;

    public CommentListActivityTest() {
        super(CommentListActivity.class);
    }

    @Rule
    public ActivityTestRule<CommentListActivity> activityRule = new ActivityTestRule<>(
            CommentListActivity.class,
            true,     // initialTouchMode
            false);   // launchActivity. False so we can customize the intent per test method

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        Context context = instrumentation.getTargetContext();
        Gson gson = new Gson();
        ListData data = gson.fromJson(DataHolder.LIST_JSON, ListData.class);
        String url = data.photos.photo.get(0).generatePhotoURL("");
        String title = data.photos.photo.get(0).title;
        intent = CommentListActivity.createIntent(context, url, title, DataHolder.COMMENT_LIST_JSON);
    }

    @Test
    public void setupComments() {
        TestUtil.setupMockServer(null);
        activityRule.launchActivity(intent);
        onView(withId(R.id.title)).check(matches(withText("Sundown on the Oregon Coast")));
        SystemClock.sleep(2000);
        onView(withId(android.R.id.list)).check(matches(withChildCount(35+1)));
    }

}
