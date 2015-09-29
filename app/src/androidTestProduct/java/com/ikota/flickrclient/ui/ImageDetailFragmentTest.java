package com.ikota.flickrclient.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ikota.flickrclient.util.IdlingResource.TimingIdlingResource;
import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.DataHolder;
import com.ikota.flickrclient.network.retrofit.FlickrURL;
import com.ikota.flickrclient.util.TestUtil;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.ikota.flickrclient.util.CustomMatchers.startsWith;
import static org.hamcrest.core.Is.is;

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
        TestUtil.setupMockServer(null);
        activityRule.launchActivity(intent);
        onView(withId(R.id.title)).check(matches(withText("A Quiet Evening")));  // we know its title from list
        onView(withId(R.id.user_name)).check(matches(withText("")));
        onView(withId(R.id.description)).check(matches(withText("")));
        onView(withId(R.id.date_text)).check(matches(withText("")));
        TimingIdlingResource idlingResource = new TimingIdlingResource(3000);  // wait dummy network operation
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(R.id.title)).check(matches(withText("Sundown on the Oregon Coast")));
        onView(withId(R.id.user_name)).check(matches(withText("Cole Chase Photography")));
        onView(withId(R.id.description)).check(matches(startsWith("I was treated to this")));
        onView(withId(R.id.date_text)).check(matches(withText("Posted on 2015-08-02 22:40:28")));
        onView(withText("C'est magnifique !")).check(matches(isDisplayed()));
        onView(withText("A wonderful moment")).check(matches(withText("A wonderful moment")));
        onView(withText("beautiful capture Cole")).check(matches(withText("beautiful capture Cole")));
        onView(withText("See All 35 Comments")).check(matches(withText("See All 35 Comments")));
        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void testDescriptionExpand() {
        TestUtil.setupMockServer(null);
        activityRule.launchActivity(intent);
        TimingIdlingResource idlingResource = new TimingIdlingResource(3000);  // wait dummy network operation
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(R.id.description)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.description_parent)).perform(click());
        onView(withId(R.id.description)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void goUserPageFromComment() {
        TestUtil.setupMockServer(null);
        activityRule.launchActivity(intent);
        TimingIdlingResource idlingResource = new TimingIdlingResource(3000);  // wait dummy network operation
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(R.id.comment_parent)).perform(clickComment());
        Espresso.unregisterIdlingResources(idlingResource);
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                InstrumentationRegistry.getInstrumentation().addMonitor(UserActivity.class.getName(), null, false);
        Activity activity = receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertEquals("Launched Activity is not UserActivity", UserActivity.class, activity.getClass());
        onView(withText("¡! Nature B■x !¡")).check(matches(isDisplayed()));
        pressBack();
        SystemClock.sleep(2000);
    }

    @Test
    public void nullCommentHandling() {
        HashMap<String,String> map = new HashMap<>();
        map.put(FlickrURL.COMMENT_LIST, "{\"comments\":{\"photo_id\":\"21043155476\"},\"stat\":\"ok\"}");
        TestUtil.setupMockServer(map);
        activityRule.launchActivity(intent);
        SystemClock.sleep(3000);
        onView(withContentDescription("No Comment")).check(matches(withText(R.string.no_comment)));
    }

    public static Matcher<View> withLines(final int lines) {
        final Matcher<Integer> lineMatcher = is(lines);
        return new BoundedMatcher<View, TextView>(TextView.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("with lines : ");
                lineMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(TextView textView) {
                return lines == textView.getLayout().getLineCount();
            }
        };
    }

    public static ViewAction clickComment() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(LinearLayout.class);
            }

            @Override
            public String getDescription() {
                return "click comment:";
            }

            @Override
            public void perform(UiController uiController, View view) {
                LinearLayout parent = (LinearLayout)view;
                parent.getChildAt(0).performClick();
            }
        };
    }

}