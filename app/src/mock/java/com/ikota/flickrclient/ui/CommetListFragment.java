package com.ikota.flickrclient.ui;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.CommentList;
import com.ikota.flickrclient.data.model.ListData;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommetListFragment extends Fragment{

    private Context mAppContext;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<CommentList.Comment> mItemList;

    private View mEmptyView;
    private ProgressBar mProgress;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private AtomicBoolean busy = new AtomicBoolean(false);

    private static final String[] COLORS = {
            "#FAFAFA", "#F5F5F5", "#EEEEEE", "#E0E0E0",
            "#BDBDBD", "#9E9E9E", "#757575", "#616161", "#424242", "#212121"
    };

    private ImageAdapter.OnClickCallback mItemClickListener = new ImageAdapter.OnClickCallback() {
        @Override
        public void onClick(View v, ListData.Photo data) {
            ImageDetailActivity.launch(getActivity(), data, (ImageView)v);
        }
    };

    private RecyclerView.OnScrollListener scroll_lister = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

            //load next item
            if (!busy.get() && totalItemCount - firstVisibleItem <= 24) {
                if(isAdded()) updateList(false);
            }

            // if it's loading and reached to bottom of the list, then show loading animation.
            if (busy.get() && firstVisibleItem + visibleItemCount == totalItemCount) {
                mProgress.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAppContext = activity.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_image_list, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(android.R.id.list);
        mEmptyView = root.findViewById(android.R.id.empty);
        mProgress = (ProgressBar) root.findViewById(R.id.progress);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setEnabled(false);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mAppContext);
        mRecyclerView.setLayoutManager(layoutManager);

        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.swipe_color_1),
                getResources().getColor(R.color.swipe_color_2),
                getResources().getColor(R.color.swipe_color_3),
                getResources().getColor(R.color.swipe_color_4)
        );

        addPaddingToTop(root);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateList(true);
            }
        });
        initList();

        return root;
    }

    private void addPaddingToTop(View root) {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        root.setPadding(0, actionBarHeight, 0, 0);
    }

    private void initList() {

        // this condition is occur when this fragment is re-created.(like created by PagerAdapter again)
        if (mItemList != null && !mItemList.isEmpty()) {
            mRecyclerView.setAdapter(mAdapter);
            return;
        }
        mItemList = new ArrayList<>();
        if(isAdded()) updateList(true);
    }

    void updateList(final boolean refresh_list) {
        busy.set(true);
        if (refresh_list && !mSwipeRefreshLayout.isRefreshing()) {
            mProgress.setVisibility(View.VISIBLE);  // do not show double progress
        }

        if (!isAdded()) return;
        if (refresh_list) mItemList.clear();

        // load list items
        for(int i=0;i<24;i++) {
            CommentList.Comment comment = new CommentList.Comment();
            mItemList.add(comment);
        }

        mEmptyView.setVisibility(mItemList.isEmpty() ? View.VISIBLE : View.GONE);

        if (refresh_list) {
            mAdapter = new CommentListAdapter(
                    mAppContext, mItemList, mItemClickListener);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addOnScrollListener(scroll_lister);
            mSwipeRefreshLayout.setRefreshing(false);
        } else if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        busy.set(false);
        mProgress.setVisibility(View.GONE);

    }

    private int getThumbColor() {
        Random random = new Random();
        return Color.parseColor(COLORS[random.nextInt(COLORS.length)]);
    }


}
