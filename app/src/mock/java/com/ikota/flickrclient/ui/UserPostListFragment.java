package com.ikota.flickrclient.ui;


import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ikota.flickrclient.data.model.ListData;

import java.util.ArrayList;

public class UserPostListFragment extends UserBaseFragment{

    @Override
    RecyclerView.Adapter getAdapter(Context context, ArrayList<ListData.Photo> data, ImageAdapter.OnClickCallback callback) {
        return new ImageAdapter(context, data, callback);
    }

    @Override
    RecyclerView.LayoutManager getLayoutManager(Context context, int column) {
        return new GridLayoutManager(context, column);
    }

    @Override
    int findFirstVisibleItemPosition(RecyclerView.LayoutManager manager) {
        return ((GridLayoutManager)manager).findFirstVisibleItemPosition();
    }

    @Override
    int getPortraitColNum() {
        return 2;
    }

    @Override
    int getHorizontalColNum() {
        return 3;
    }

    @Override
    boolean getHasFixedSize() {
        return true;
    }
}
