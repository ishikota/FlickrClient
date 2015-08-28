package com.ikota.flickrclient.ui;


import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.ikota.flickrclient.data.model.ListData;

import java.util.ArrayList;

public class UserPostListFragment extends UserBaseFragment{

    @Override
    RecyclerView.Adapter getAdapter(Context context, ArrayList<ListData.Photo> data, ImageAdapter.OnClickCallback callback) {
        return new ImageAdapter(context, data, callback);
    }
}
