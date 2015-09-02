package com.ikota.flickrclient.ui;


import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.DataHolder;
import com.ikota.flickrclient.data.model.ListData;
import com.ikota.flickrclient.di.DummyAPIModule;
import com.ikota.flickrclient.network.Util;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
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
import static org.hamcrest.core.Is.is;

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
        Gson gson = new Gson();
        ListData data = gson.fromJson(DataHolder.LIST_JSON, ListData.class);
        String url = data.photos.photo.get(0).generatePhotoURL("");
        String title = data.photos.photo.get(0).title;
        intent = CommentListActivity.createIntent(context, url, title, DataHolder.COMMENT_LIST_JSON);
    }

    @Test
    public void setupComments() {
        setupMockServer(null);
        activityRule.launchActivity(intent);
        onView(withId(R.id.title)).check(matches(withText("Sundown on the Oregon Coast")));
        SystemClock.sleep(2000);
        onView(withId(android.R.id.list)).check(matches(withChildCount(35+1)));
    }

    public static Matcher<View> withChildCount(final int num) {
        final Matcher<Integer> matcher = is(num);
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                Log.i("CommentListActivityTest", "item num "+((RecyclerView)item).getAdapter().getItemCount());
                return ((RecyclerView)item).getAdapter().getItemCount() == num;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with childCount: ");
                matcher.describeTo(description);
            }
        };
    }

}
