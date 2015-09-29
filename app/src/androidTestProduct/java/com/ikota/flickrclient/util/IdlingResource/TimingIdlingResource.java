package com.ikota.flickrclient.util.IdlingResource;

import android.support.test.espresso.IdlingResource;
import android.support.v7.widget.RecyclerView;

/**
 * Wait next action until specified time is passed.
 */
public class TimingIdlingResource implements IdlingResource {

    private ResourceCallback resourceCallback;
    private final long wait_time;
    private final long start_time;

    public TimingIdlingResource(long wait_by_milli) {
        this.wait_time = wait_by_milli;
        this.start_time = System.currentTimeMillis();
    }

    @Override
    public String getName() {
        return TimingIdlingResource.class.getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        // wait until wait_time is passed
        boolean idle = wait_time < (System.currentTimeMillis() - start_time);
        if (idle && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }

    private boolean isItemLoaded(RecyclerView list) {
        return list.getAdapter()!=null && list.getAdapter().getItemCount() != 0;
    }

}
