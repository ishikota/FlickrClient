package com.ikota.flickrclient;

import android.support.test.espresso.IdlingResource;
import android.support.v7.widget.RecyclerView;


public class LoadingIdlingResource implements IdlingResource {

    private ResourceCallback resourceCallback;
    private RecyclerView recyclerView;

    public LoadingIdlingResource(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public String getName() {
        return LoadingIdlingResource.class.getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        // check if images are set to RecyclerView
        boolean idle = isItemLoaded(recyclerView);
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
