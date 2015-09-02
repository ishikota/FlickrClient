package com.ikota.flickrclient.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.ListData;
import com.ikota.flickrclient.data.model.PhotoInfo;


public class ImageDetailFragment extends Fragment {

    public static ImageDetailFragment newInstance(String json, String cache_size) {
        ImageDetailFragment fragment = new ImageDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ImageDetailActivity.EXTRA_CONTENT, json);
        bundle.putString(ImageDetailActivity.EXTRA_CACHE_SIZE, cache_size);
        fragment.setArguments(bundle);
        return fragment;
    }

    private AndroidApplication mAppContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAppContext = (AndroidApplication)activity.getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // setup content
        Gson gson = new Gson();
        String json = getArguments().getString(ImageDetailActivity.EXTRA_CONTENT);
        String size = getArguments().getString(ImageDetailActivity.EXTRA_CACHE_SIZE);
        ListData.Photo mData = gson.fromJson(json, ListData.Photo.class);

        View root = inflater.inflate(R.layout.fragment_image_detail, container, false);
        RecyclerView mRecyclerView = (RecyclerView) root.findViewById(android.R.id.list);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mAppContext));
        mRecyclerView.setAdapter(new ImageDetailAdapter(mAppContext, mData, size,
                new ImageDetailAdapter.OnClickCallback() {
                    @Override
                    public void onUserClicked(View v, PhotoInfo.Owner owner) {
                        Intent intent = UserActivity.createIntent(getActivity(), owner);
                        startActivity(intent);
                    }
                    @Override
                    public void onCommentClicked(String url, String title, String json) {
                        startActivity(CommentListActivity.createIntent(mAppContext, url, title, json));
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
