package com.ikota.flickrclient.util;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Matcher;

public class CustomActions {

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click child view with id:";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View child_view = view.findViewById(id);
                if(child_view!=null) {
                    child_view.performClick();
                } else {
                    throw new IllegalStateException("Specified child view was not found!!");
                }
            }
        };

    }

}
