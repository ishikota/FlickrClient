package com.ikota.flickrclient.ui;

import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Configuration;
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
import com.ikota.flickrclient.data.model.ListData;
import com.ikota.flickrclient.di.ListAdapterGenerator;
import com.ikota.flickrclient.di.LoadMethod;
import com.ikota.flickrclient.util.NetUtils;
import com.ikota.flickrclient.util.NetworkReceiver;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.inject.Named;

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
public class ImageListFragment extends Fragment {

    @Inject @Named("portrait col") public int PORTRAIT_COL_NUM;
    @Inject @Named("horizontal col") public int HORIZONTAL_COL_NUM;
    @Inject @Named("item per page") public int ITEM_PER_PAGE;
    @Inject public boolean ADD_PADDING;
    @Inject public LoadMethod LOAD_METHOD;
    @Inject public ListAdapterGenerator ADAPTER_GENERATOR;

    protected Context mAppContext;

    protected RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<ListData.Photo> mItemList;

    protected ProgressBar mProgress;
    private View mEmptyView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    protected boolean end_flg;
    protected AtomicBoolean busy = new AtomicBoolean(false);

    private NetworkReceiver mReceiver;
    private NetworkReceiver.OnNetworkStateChangedListener mListener =
            new NetworkReceiver.OnNetworkStateChangedListener() {
        @Override
        public void changedToWifi() {
            if(mAdapter!=null) {
                ((ListAdapterImpl) mAdapter).setIfWifiConnected(true);
            }
        }

        @Override
        public void changedToMobile() {
            if(mAdapter!=null) {
                ((ListAdapterImpl) mAdapter).setIfWifiConnected(false);
            }
        }

        @Override
        public void changedToOffline() {
            if(mAdapter!=null) {
                ((ListAdapterImpl) mAdapter).setIfWifiConnected(false);
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

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
        int display_mode = getResources().getConfiguration().orientation;
        int col_num = display_mode == Configuration.ORIENTATION_PORTRAIT ? PORTRAIT_COL_NUM : HORIZONTAL_COL_NUM;
        GridLayoutManager layoutManager = new GridLayoutManager(mAppContext, col_num);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(getScrollListener());

        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.swipe_color_1),
                getResources().getColor(R.color.swipe_color_2),
                getResources().getColor(R.color.swipe_color_3),
                getResources().getColor(R.color.swipe_color_4)
        );

        if (ADD_PADDING) addPaddingToTop(root);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateList(0, true);
            }
        });
        initList();

        return root;
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

        // this condition is occur when this fragment is re-created.
        // (like created by PagerAdapter again, orientation change occurred)
        if (mItemList != null && !mItemList.isEmpty()) {
            mRecyclerView.setAdapter(mAdapter);
            return;
        }

        mItemList = new ArrayList<>();
        // if cache found then display it.
        String json = null; //mCacheUtil.getCacheJson(getActivity(), CONTENT_TYPE, CONTENT_PARAM1);
        if (json != null && !json.isEmpty()) {
            Gson gson = new Gson();
            ListData item = gson.fromJson(json, ListData.class);
            //cache_top_id = mItemList.isEmpty() ? "-1" : mItemList.get(0).id;
            for(ListData.Photo photo : item.photos.photo) {
                mItemList.add(photo);
            }
            mAdapter = ADAPTER_GENERATOR.generateAdapter(mAppContext, mItemList, getItemClickListener());
            if(isAdded()) ((ListAdapterImpl)mAdapter).setViewSize(calcViewSize());
            mRecyclerView.setAdapter(mAdapter);

        }
        // if cache found (json!=null) then do not need to refresh list
        if(isAdded()) updateList(0, json == null || json.isEmpty());
    }

    private int calcViewSize() {
        AndroidApplication app = (AndroidApplication)getActivity().getApplication();
        return app.SCREEN_WIDTH / Math.min(PORTRAIT_COL_NUM, HORIZONTAL_COL_NUM);
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

        LOAD_METHOD.loadItem((AndroidApplication)getActivity().getApplication(), page + 1, new Callback<ListData>() {
            @Override
            public void success(ListData listData, Response response) {
                if (!isAdded()) return;
                //mCacheUtil.putCacheJson(getActivity(), CONTENT_TYPE, CONTENT_PARAM1, response);
                if (refresh_list) mItemList.clear();

                for(ListData.Photo photo : listData.photos.photo) {
                    mItemList.add(photo);
                }

                mEmptyView.setVisibility(mItemList.isEmpty() ? View.VISIBLE : View.GONE);

                if (refresh_list) {
                    mAdapter = ADAPTER_GENERATOR.generateAdapter(
                            mAppContext, mItemList, getItemClickListener());
                    if(isAdded()) ((ListAdapterImpl)mAdapter).setViewSize(calcViewSize());
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.addOnScrollListener(getScrollListener());
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

    protected ImageAdapter.OnClickCallback getItemClickListener() {
        return new ImageAdapter.OnClickCallback() {
            @Override
            public void onClick(View v, ListData.Photo data) {
                boolean is_wifi = NetUtils.isWifiConnected(getActivity());
                String size = ListData.Photo.getProperSize(calcViewSize(), is_wifi);
                ImageDetailActivity.launch(getActivity(), data, (ImageView) v, size);
            }
        };
    }

    protected RecyclerView.OnScrollListener getScrollListener() {
        return new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager layoutManager = (GridLayoutManager)recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                //load next item
                if (!end_flg && !busy.get() && totalItemCount - firstVisibleItem <= ITEM_PER_PAGE) {
                    int page = totalItemCount / ITEM_PER_PAGE;
                    if(isAdded()) updateList(page, false);
                }

                // if it's loading and reached to bottom of the list, then show loading animation.
                if (busy.get() && firstVisibleItem + visibleItemCount == totalItemCount) {
                    mProgress.setVisibility(View.VISIBLE);
                }
            }
        };
    }

}
