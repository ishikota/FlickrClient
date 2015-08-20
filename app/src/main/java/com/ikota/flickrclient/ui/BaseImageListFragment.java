package com.ikota.flickrclient.ui;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ikota.flickrclient.R;
import com.ikota.flickrclient.model.Interestingness;
import com.ikota.flickrclient.util.NetworkReceiver;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kota on 2015/08/11.
 * <p/>
 * UI of basic image list page.
 * <p/>
 * This fragment would be used in various pages like
 * tag list page, user upload list, popular item list ...
 * <p/>
 * you can adjust,
 * - whether to add padding on top for ActionBar
 * - the number of column
 * - the base url to fetch json data of images
 */
public abstract class BaseImageListFragment extends Fragment {

    private Context mAppContext;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Interestingness.Photo> mItemList;

    private View mEmptyView;
    private ProgressBar mProgress;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    private boolean end_flg;
    private AtomicBoolean busy = new AtomicBoolean(false);

    private NetworkReceiver mReceiver;
    private NetworkReceiver.OnNetworkStateChangedListener mListener =
            new NetworkReceiver.OnNetworkStateChangedListener() {
        @Override
        public void changedToWifi() {
            if(mAdapter!=null) {
                ((ImageAdapter) mAdapter).setIfWifiConnected(true);
            }
        }

        @Override
        public void changedToMobile() {
            if(mAdapter!=null) {
                ((ImageAdapter) mAdapter).setIfWifiConnected(false);
            }
        }

        @Override
        public void changedToOffline() {
            if(mAdapter!=null) {
                ((ImageAdapter) mAdapter).setIfWifiConnected(false);
            }
        }
    };

    private ImageAdapter.OnClickCallback mItemClickListener = new ImageAdapter.OnClickCallback() {
        @Override
        public void onClick(View v, Interestingness.Photo data) {
            ImageDetailActivity.launch(getActivity(), data, (ImageView)v);
        }
    };

    private RecyclerView.OnScrollListener scroll_lister = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            GridLayoutManager layoutManager = (GridLayoutManager)recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

            //load next item
            if (!end_flg && !busy.get() && totalItemCount - firstVisibleItem <= getItemPerPage()) {
                int page = totalItemCount / getItemPerPage();
                updateList(page, false);
            }

            // if it's loading and reached to bottom of the list, then show loading animation.
            if (busy.get() && firstVisibleItem + visibleItemCount == totalItemCount) {
                mProgress.setVisibility(View.VISIBLE);
            }
        }
    };

    /**
     * Concrete class must implements this method.
     * In this method, concrete class just calls proper api method
     * by using passed parameters
     *
     * @param page     the page to request
     * @param callback callback method which is called after loading data
     */
    public abstract void loadByContentType(
            int page,
            Callback<Interestingness> callback
    );

    /**
     * return the number of column of list
     */
    public abstract int getColumnNum();

    /**
     * return if need top padding for ActionBar
     */
    public abstract boolean addPadding();

    /**
     * return item num per one page
     */
    public abstract int getItemPerPage();

    @Override
    public void onResume() {
        super.onResume();
        if(isAdded()) {
            // Registers BroadcastReceiver to track network connection changes.
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            mReceiver = new NetworkReceiver(mListener);
            getActivity().registerReceiver(mReceiver, filter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isAdded() && mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAppContext = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_image_list, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(android.R.id.list);
        mEmptyView = root.findViewById(android.R.id.empty);
        mProgress = (ProgressBar) root.findViewById(R.id.progress);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh);

        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(mAppContext, getColumnNum());
        mRecyclerView.setLayoutManager(layoutManager);

        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.swipe_color_1),
                getResources().getColor(R.color.swipe_color_2),
                getResources().getColor(R.color.swipe_color_3),
                getResources().getColor(R.color.swipe_color_4)
        );

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (addPadding()) addPaddingToTop(view);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateList(0, true);
            }
        });
        initList();
    }

    /**
     * Creates a space on the top for ActionBar
     */
    private void addPaddingToTop(View root) {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        root.setPadding(0, actionBarHeight, 0, 0);
    }

    /**
     * Initialize UI component and listener.
     * And starts download task of list item.
     */
    private void initList() {

        // this condition is occur when this fragment is re-created.(like created by PagerAdapter again)
        if (mItemList != null && !mItemList.isEmpty()) {
            mRecyclerView.setAdapter(mAdapter);
            return;
        }
        mItemList = new ArrayList<>();

        // if cache found then display it.
        String json = null; //mCacheUtil.getCacheJson(getActivity(), CONTENT_TYPE, CONTENT_PARAM1);
        if (json != null && !json.isEmpty()) {
            Gson gson = new Gson();
            Interestingness item = gson.fromJson(json, Interestingness.class);
            //cache_top_id = mItemList.isEmpty() ? "-1" : mItemList.get(0).id;
            for(Interestingness.Photo photo : item.photos.photo) {
                mItemList.add(photo);
            }
            mAdapter = new ImageAdapter(mAppContext, mItemList, mItemClickListener, getColumnNum());
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addOnScrollListener(scroll_lister);
        }
        // if cache found (json!=null) then do not need to refresh list
        updateList(0, json == null || json.isEmpty());
    }

    /**
     * Download list content of specified page and update listView.
     *
     * @param page         : the page of item list to download.
     * @param refresh_list : if true then remove all item in list and add new item.
     */
    void updateList(final int page, final boolean refresh_list) {
        busy.set(true);
        if (refresh_list && !mSwipeRefreshLayout.isRefreshing()) {
            mProgress.setVisibility(View.VISIBLE);  // do not show double progress
        }

        loadByContentType(page + 1, new Callback<Interestingness>() {
            @Override
            public void success(Interestingness interestingness, Response response) {
                if (!isAdded()) return;
                //mCacheUtil.putCacheJson(getActivity(), CONTENT_TYPE, CONTENT_PARAM1, response);
                if (refresh_list) mItemList.clear();

                for(Interestingness.Photo photo : interestingness.photos.photo) {
                    mItemList.add(photo);
                }

                mEmptyView.setVisibility(mItemList.isEmpty() ? View.VISIBLE : View.GONE);

                if (refresh_list) {
                    mAdapter = new ImageAdapter(
                            mAppContext, mItemList, mItemClickListener, getColumnNum());
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.addOnScrollListener(scroll_lister);
                    mSwipeRefreshLayout.setRefreshing(false);
                } else if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
                busy.set(false);
                mProgress.setVisibility(View.GONE);
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isAdded()) return;
                error.printStackTrace();
                if (page == 0 && mItemList.size() == 0)
                    mEmptyView.setVisibility(View.VISIBLE);
                String message = getResources().getString(R.string.network_problem_message);
                Toast.makeText(mAppContext, message, Toast.LENGTH_SHORT).show();
                mProgress.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                end_flg = true;
                busy.set(false);
            }
        });
    }

}
