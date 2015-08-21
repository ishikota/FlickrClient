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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ImageDetailFragmentTest extends ActivityInstrumentationTestCase2<ImageDetailActivity> {

    private Context context;
    private Intent intent;

    public ImageDetailFragmentTest() {
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
        context = instrumentation.getTargetContext();
        intent = new Intent(context, ImageDetailActivity.class);
        intent.putExtra(ImageDetailActivity.EXTRA_CONTENT, DataHolder.LIST_ITEM_JSON);
    }

    @Test
    public void preConditions() {
        ImageDetailActivity activity = activityRule.launchActivity(intent);
        onView(withId(R.id.container)).check(matches(ViewMatchers.withContentDescription(R.string.detail_container)));
        String args = activity.getIntent().getStringExtra(ImageDetailActivity.EXTRA_CONTENT);
        assertEquals(args, DataHolder.LIST_ITEM_JSON);
    }



    @Test
    public void checkDetailInfo_isSet() {
        ImageDetailActivity activity = activityRule.launchActivity(intent);
        ImageDetailFragment fragment = (ImageDetailFragment)activity.getSupportFragmentManager().findFragmentByTag(
                ImageDetailFragment.class.getSimpleName());
        TextView use_name = (TextView)fragment.getView().findViewById(R.id.user_name);
        onView(withId(R.id.title)).check(matches(withText("A Quiet Evening")));  // we know its title from list
        onView(withId(R.id.user_name)).check(matches(withText("")));
        onView(withId(R.id.description)).check(matches(withText("")));
        onView(withId(R.id.date_text)).check(matches(withText("")));
        TextIdlingResource idlingResource = new TextIdlingResource(use_name);
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(R.id.title)).check(matches(withText("A Quiet Evening")));
        onView(withId(R.id.user_name)).check(matches(withText("Jens Haggren")));
        onView(withId(R.id.description)).check(matches(withText("Thank you very much for your views, faves and comments. I appreciate it a lot!")));
        onView(withId(R.id.date_text)).check(matches(withText("2015-06-21 21:18:57")));
    }

}