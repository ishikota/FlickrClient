package com.ikota.flickrclient.util;


import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static org.hamcrest.core.Is.is;

public class CustomMatchers {

    /**
     * Check if child count of target RecyclerView matches to expected one
     * @param expected_count expected item count in target RecyclerView
     */
    public static Matcher<View> withChildCount(final int expected_count) {
        final Matcher<Integer> matcher = is(expected_count);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {

            @Override
            protected boolean matchesSafely(RecyclerView recyclerView) {
                Log.i("withChildCount", "item num is " + (recyclerView.getAdapter().getItemCount()));
                return matcher.matches(recyclerView.getAdapter().getItemCount());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with childCount: ");
                matcher.describeTo(description);
            }
        };
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
