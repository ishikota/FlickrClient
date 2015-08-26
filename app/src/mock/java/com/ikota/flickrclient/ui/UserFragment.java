package com.ikota.flickrclient.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.ListData;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;


public class UserFragment extends Fragment{

    private Context mAppContext;
    private Toolbar mToolbar;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<ListData.Photo> mItemList;

    private View mEmptyView;
    private ProgressBar mProgress;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private AtomicBoolean busy = new AtomicBoolean(false);

    private static final String[] COLORS = {
            "#FAFAFA", "#F5F5F5", "#EEEEEE", "#E0E0E0",
            "#BDBDBD", "#9E9E9E", "#757575", "#616161", "#424242", "#212121"
    };

    private TabLayout mTabLayout;
    private int mActionBarHeight;
    private int mTabHeight;
    private int mHeaderPadding;

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

            GridLayoutManager layoutManager = (GridLayoutManager)recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

            // avoid nullpo
            if(totalItemCount == 0) return;

            //load next item
            if (!busy.get() && totalItemCount - firstVisibleItem <= 24) {
                if(isAdded()) updateList(false);
            }

            // if it's loading and reached to bottom of the list, then show loading animation.
            if (busy.get() && firstVisibleItem + visibleItemCount == totalItemCount) {
                mProgress.setVisibility(View.VISIBLE);
            }

            // scroll TabLayout with RecyclerView scroll
            int headPos = mRecyclerView.getChildAt(0).getTop();
            float y = mTabLayout.getY();
            if (y == 0) return;  // ignore setup phase callback
            if (dy >= 0) {  // if up scroll
                mTabLayout.setY(Math.max(mActionBarHeight, headPos-mTabHeight));    // tab stops below actionbar
            } else {    // if down scroll
                if(headPos >= y+mTabHeight) {  // do not down TabLayout until list head is revealed
                    mTabLayout.setY(Math.min(headPos-mTabHeight, mHeaderPadding));
                }
            }

            // change actionbar alpha with RecyclerView scroll
            if(mToolbar!=null) {  // nullpo occured when config changes
                int alpha = (int) (255 * (1 - (y - mActionBarHeight) / (mHeaderPadding - mTabHeight - mActionBarHeight)));
                mToolbar.getBackground().setAlpha(alpha);
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

        mTabHeight = (int)getResources().getDimension(R.dimen.user_tab_height);
        mHeaderPadding = (int)getResources().getDimension(R.dimen.user_header_padding);
        TypedValue tv = new TypedValue();
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            mActionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tab_list, container, false);

        // set mock data
        Resources r = getResources();
        ((TextView)root.findViewById(R.id.user_name)).setText(r.getString(R.string.username));
        ((TextView)root.findViewById(R.id.sub_text)).setText(r.getString(R.string.user_info));

        mRecyclerView = (RecyclerView) root.findViewById(android.R.id.list);
        mEmptyView = root.findViewById(android.R.id.empty);
        mProgress = (ProgressBar) root.findViewById(R.id.progress);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh);
        mTabLayout = (TabLayout)root.findViewById(R.id.tab_layout);

        mToolbar = ((BaseActivity)getActivity()).mActionBarToolbar;
        if(mToolbar!=null) mToolbar.getBackground().setAlpha(0);  // nullpo occured when config changes

        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(mAppContext, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(scroll_lister);

        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.swipe_color_1),
                getResources().getColor(R.color.swipe_color_2),
                getResources().getColor(R.color.swipe_color_3),
                getResources().getColor(R.color.swipe_color_4)
        );

        mTabLayout.addTab(mTabLayout.newTab().setText("ABOUTS"));
        mTabLayout.addTab(mTabLayout.newTab().setText("POST"));
        mTabLayout.addTab(mTabLayout.newTab().setText("PHOTOS"));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateList(true);
            }
        });
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
        if (refresh_list && !mSwipeRefreshLayout.isRefreshing()) {
            mProgress.setVisibility(View.VISIBLE);  // do not show double progress
        }

        if (!isAdded()) return;
        if (refresh_list) mItemList.clear();

        // load list items
        for(int i=0;i<24;i++) {
            ListData.Photo photo = new ListData.Photo();
            photo.farm = getThumbColor();
            mItemList.add(photo);
        }

        mEmptyView.setVisibility(mItemList.isEmpty() ? View.VISIBLE : View.GONE);

        if (refresh_list) {
            mAdapter = new ImageAdapter(
                    mAppContext, mItemList, mItemClickListener, 2);
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
        Random  random = new Random();
        return Color.parseColor(COLORS[random.nextInt(COLORS.length)]);
    }


}
