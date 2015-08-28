package com.ikota.flickrclient.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.ikota.flickrclient.data.model.ListData;

import java.util.ArrayList;


public class UserTimelineFragment extends UserBaseFragment{

    @Override
    RecyclerView.Adapter getAdapter(Context context, ArrayList<ListData.Photo> data, ImageAdapter.OnClickCallback callback) {
        return new TimeLineAdapter(context, data, null);
    }

    @Override
    RecyclerView.LayoutManager getLayoutManager(Context context, int column) {
        return new StaggeredGridLayoutManager(column,StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    int findFirstVisibleItemPosition(RecyclerView.LayoutManager manager) {
        return ((StaggeredGridLayoutManager)manager).findFirstVisibleItemPositions(new int[3])[0];
    }

    @Override
    int getPortraitColNum() {
        return 1;
    }

    @Override
    int getHorizontalColNum() {
        return 2;
    }

    @Override
    boolean getHasFixedSize() {
        return false;
    }

}
