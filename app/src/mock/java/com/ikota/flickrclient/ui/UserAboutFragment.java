package com.ikota.flickrclient.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.ListData;
import com.ikota.flickrclient.data.model.PeopleInfo;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


public class UserAboutFragment extends UserBaseFragment{

    @Override
    RecyclerView.Adapter getAdapter(Context context, ArrayList<ListData.Photo> data, ImageAdapter.OnClickCallback callback) {
        return new AboutAdapter(context, null, null);
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
        return 1;
    }

    @Override
    int getHorizontalColNum() {
        return 1;
    }

    @Override
    boolean getHasFixedSize() {
        return true;
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<PeopleInfo> mItemList;

    private View mEmptyView;
    private ProgressBar mProgress;
    private AtomicBoolean busy = new AtomicBoolean(false);

    protected RecyclerView.OnScrollListener scroll_lister = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // notify scroll event to host Activity
            if(mRecyclerView.getChildAt(0) != null) {  // nullpo occur when swipe refresh staggered
                UserActivity.sTabEventBus.post(new UserActivity
                        .ScrollEvent(mRecyclerView.getChildAt(0).getTop(), dy));
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_about, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(android.R.id.list);
        mEmptyView = root.findViewById(android.R.id.empty);
        mProgress = (ProgressBar) root.findViewById(R.id.progress);

        mRecyclerView.setHasFixedSize(getHasFixedSize());
        int display_mode = getResources().getConfiguration().orientation;
        int col_num = display_mode == Configuration.ORIENTATION_PORTRAIT ?
                getPortraitColNum() : getHorizontalColNum();
        RecyclerView.LayoutManager layoutManager = getLayoutManager(mAppContext, col_num);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(scroll_lister);

        initList();

        return root;
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
        if (refresh_list) {
            mProgress.setVisibility(View.VISIBLE);
        }

        if (!isAdded()) return;
        if (refresh_list) mItemList.clear();

        // load list items
        mItemList.add(new PeopleInfo());

        mEmptyView.setVisibility(mItemList.isEmpty() ? View.VISIBLE : View.GONE);

        if (refresh_list) {
            mAdapter = new AboutAdapter(mAppContext, mItemList, new AboutAdapter.OnClickCallback() {
                @Override
                public void onFlickrClicked(View view) {
                    WebViewActivity.launch(getActivity(), "Flickr", "https:\\/\\/www.flickr.com\\/people\\/131498071@N04\\/");
                }

                @Override
                public void onLocationClicked(View view) {

                }
            });
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addOnScrollListener(scroll_lister);
        } else if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        busy.set(false);
        mProgress.setVisibility(View.GONE);

    }

}
