package com.ikota.flickrclient.ui;


import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ikota.flickrclient.IdlingResource.TextIdlingResource;
import com.ikota.flickrclient.IdlingResource.TimingIdlingResource;
import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.DataHolder;
import com.ikota.flickrclient.data.model.PhotoInfo;
import com.ikota.flickrclient.di.DummyAPIModule;
import com.ikota.flickrclient.network.Util;
import com.ikota.flickrclient.network.retrofit.FlickrURL;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import dagger.ObjectGraph;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.Is.is;

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
        setupMockServer(null);
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
        setupMockServer(map);
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

    public static Matcher<View> startsWith(final String expectString) {
        final Matcher<String> textMatcher = is(expectString);
        return new BoundedMatcher<View, TextView>(TextView.class) {

            @Override
            protected boolean matchesSafely(TextView textView) {
                CharSequence text = textView.getText();
                if (text == null) {
                    return null == expectString;
                }
                int len = expectString.length();
                return textMatcher.matches(textView.getText().toString().substring(0, len));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("start text : ");
                textMatcher.describeTo(description);
            }

        };
    }

}
