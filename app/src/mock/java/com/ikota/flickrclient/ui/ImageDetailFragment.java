package com.ikota.flickrclient.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.ListData;


public class ImageDetailFragment extends Fragment{

    private Context mAppContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAppContext = activity.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_image_detail, container, false);

        RecyclerView mRecyclerView = (RecyclerView) root.findViewById(android.R.id.list);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mAppContext));
        mRecyclerView.setAdapter(new ImageDetailAdapter(mAppContext, new ImageAdapter.OnClickCallback() {
            @Override
            public void onClick(View v, ListData.Photo data) {
                Intent intent = new Intent(getActivity(), UserActivity.class);
                startActivity(intent);
            }
        }));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ImageDetailActivity.sTabEventBus.post(new ImageDetailActivity.ScrollEvent(dy));
            }
        });

        return root;
    }

}
