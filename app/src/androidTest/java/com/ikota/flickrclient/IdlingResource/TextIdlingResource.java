package com.ikota.flickrclient.IdlingResource;

import android.support.test.espresso.IdlingResource;
import android.widget.TextView;

/**
 * Wait next action until text is set on passed view.
 */
public class TextIdlingResource implements IdlingResource {

    private ResourceCallback resourceCallback;
    private TextView textView;

    public TextIdlingResource(TextView textView) {
        this.textView = textView;
    }

    @Override
    public String getName() {
        return TextIdlingResource.class.getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        boolean idle = !textView.getText().toString().isEmpty();
        if (idle && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }

}
