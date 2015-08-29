package com.ikota.flickrclient.di;


import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.ikota.flickrclient.data.model.ListData;
import com.ikota.flickrclient.ui.ImageAdapter;

import java.util.ArrayList;

public interface ListAdapterGenerator {
    RecyclerView.Adapter generateAdapter(
            Context context,
            ArrayList<ListData.Photo> data,
            ImageAdapter.OnClickCallback callback
    );
}
