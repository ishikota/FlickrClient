package com.ikota.flickrclient.ui;


import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ikota.flickrclient.R;

public class UserBaseFragment extends ImageListFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        int padding = (int)getResources().getDimension(R.dimen.user_header_padding);
        mRecyclerView.setPadding(0, padding, 0, 0);
        return root;
    }

    @Override
    protected RecyclerView.OnScrollListener getScrollListener() {
        return new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager layoutManager = (GridLayoutManager)recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if(totalItemCount == 0) return;  // avoid nullpo

                //load next item
                if (!end_flg && !busy.get() && totalItemCount - firstVisibleItem <= ITEM_PER_PAGE) {
                    int page = totalItemCount / ITEM_PER_PAGE;
                    if(isAdded()) updateList(page, false);
                }

                // if it's loading and reached to bottom of the list, then show loading animation.
                if (busy.get() && firstVisibleItem + visibleItemCount == totalItemCount) {
                    mProgress.setVisibility(View.VISIBLE);
                }

                // notify scroll event to host Activity
                UserActivity.sTabEventBus.post(new UserActivity
                        .ScrollEvent(mRecyclerView.getChildAt(0).getTop(), dy));
            }
        };

    }

    public void notifyListState() {
        UserActivity.sTabEventBus.post(new UserActivity.ScrollEvent(mRecyclerView.getChildAt(0).getTop(), 1));
    }

}
