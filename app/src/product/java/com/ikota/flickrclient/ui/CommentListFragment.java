package com.ikota.flickrclient.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.CommentList;

import java.util.List;


public class CommentListFragment extends Fragment{

    private Context mAppContext;

    public static CommentListFragment newInstance(String url, String title, String json) {
        Bundle bundle = new Bundle();
        bundle.putString(CommentListActivity.EXTRA_URL, url);
        bundle.putString(CommentListActivity.EXTRA_TITLE, title);
        bundle.putString(CommentListActivity.EXTRA_DATA, json);
        CommentListFragment fragment = new CommentListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAppContext = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_about, container, false);

        RecyclerView mRecyclerView = (RecyclerView) root.findViewById(android.R.id.list);
        mRecyclerView.setPadding(0,0,0,0);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mAppContext);
        mRecyclerView.setLayoutManager(layoutManager);

        // construct object
        Gson gson = new Gson();
        String json = getArguments().getString(CommentListActivity.EXTRA_DATA);
        String url = getArguments().getString(CommentListActivity.EXTRA_URL);
        String title = getArguments().getString(CommentListActivity.EXTRA_TITLE);
        CommentList data = gson.fromJson(json, CommentList.class);
        List<CommentList.Comment> mItemList = data.comments.comment;
        mRecyclerView.setAdapter(new CommentListAdapter(mAppContext, null, url, title, mItemList));

        // add padding top
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        root.setPadding(0, actionBarHeight, 0, 0);

        return root;
    }

}
