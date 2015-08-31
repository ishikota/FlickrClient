package com.ikota.flickrclient.ui;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.PeopleInfo;
import com.ikota.flickrclient.data.model.PhotoInfo;
import com.ikota.flickrclient.di.UserPostListModule;
import com.ikota.flickrclient.di.UserTimelineModule;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import dagger.ObjectGraph;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserActivity extends BaseActivity{

    public static final String EXTRA_CONTENT = "content";

    static Bus sTabEventBus = new Bus();

    private static final String KEY_FRAGMENT_TAG = "tag";
    private static final String KEY_SELECTED_TAB = "pos";
    private static final String TAG_FRAGMENT_1 = "f1";
    private static final String TAG_FRAGMENT_2 = "f2";
    private static final String TAG_FRAGMENT_3 = "f3";

    private Fragment fragment1;
    private Fragment fragment2;
    private Fragment fragment3;
    private Fragment mDisplayingFragment;
    private String mTag = "";

    private TabLayout mTabLayout;
    private int mSelectedTabPos = 0;

    private int mActionBarHeight;
    private int mTabHeight;
    private int mHeaderPadding;

    private int mActionbarAlpha = 255;

    View mContainer;

    public static Intent createIntent(Activity activity, PhotoInfo.Owner owner) {
        Gson gson = new Gson();
        String parsed_json = gson.toJson(owner);

        Intent intent = new Intent(activity, UserActivity.class);
        intent.putExtra(EXTRA_CONTENT, parsed_json);
        return intent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_FRAGMENT_TAG, mTag);
        outState.putInt(KEY_SELECTED_TAB, mSelectedTabPos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mContainer = findViewById(R.id.container);

        // get content
        Gson gson = new Gson();
        String json = getIntent().getStringExtra(EXTRA_CONTENT);
        PhotoInfo.Owner content = gson.fromJson(json, PhotoInfo.Owner.class);

        getViewSize();
        setupActionBar();
        setupViews(content);
        loadUserInfo(content.nsid);
        registerFragment(content.nsid);

        if(savedInstanceState!=null) {
            mTag = savedInstanceState.getString(KEY_FRAGMENT_TAG);
            mSelectedTabPos = savedInstanceState.getInt(KEY_SELECTED_TAB);
            // restore selected tab
            mTabLayout.setOnTabSelectedListener(null);
            mTabLayout.getTabAt(mSelectedTabPos).select();
            mTabLayout.setOnTabSelectedListener(new MyTabListener());
        }

        mDisplayingFragment = getSupportFragmentManager().findFragmentByTag(mTag);
        if(mDisplayingFragment == null) {
            mTag = TAG_FRAGMENT_1;
            mDisplayingFragment = fragment1;
        } else {
            // the case when orientation change occurred
            adjustTabPosition((UserTabImpl)mDisplayingFragment);
        }

        getSupportFragmentManager().beginTransaction().show(mDisplayingFragment).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        sTabEventBus.register(this);
        if(mActionBarToolbar!=null) mActionBarToolbar.getBackground().setAlpha(mActionbarAlpha);
    }

    @Override
    public void onPause() {
        super.onPause();
        sTabEventBus.unregister(this);
        if(mActionBarToolbar!=null) mActionBarToolbar.getBackground().setAlpha(255);
    }

    private void getViewSize() {
        mTabHeight = (int)getResources().getDimension(R.dimen.user_tab_height);
        mHeaderPadding = (int)getResources().getDimension(R.dimen.user_header_padding);
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)){
            mActionBarHeight = TypedValue.
                    complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
    }

    private void setupActionBar() {
        //noinspection ConstantConditions
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
    }

    private void setupViews(PhotoInfo.Owner content) {

        // nullpo occurred when config changes
        mActionbarAlpha = 0;  // we need to manually set initial visibility.
        if(mActionBarToolbar!=null) mActionBarToolbar.getBackground().setAlpha(mActionbarAlpha);

        Resources r = getResources();
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.addTab(mTabLayout.newTab().setText(r.getString(R.string.tab_title_1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(r.getString(R.string.tab_title_2)));
        mTabLayout.addTab(mTabLayout.newTab().setText(r.getString(R.string.tab_title_3)));
        mTabLayout.setOnTabSelectedListener(new MyTabListener());

        ImageView cover = (ImageView)findViewById(R.id.cover);
        ImageView icon = (ImageView)findViewById(R.id.user_icon);
        TextView name = (TextView)findViewById(R.id.user_name);

        name.setText(content.username);
        Picasso.with(UserActivity.this)
                .load(content.generateOwnerIconURL())
                .into(icon);
        Picasso.with(UserActivity.this)
                .load("https://d13yacurqjgara.cloudfront.net/users/44585/screenshots/1544389/book-pattern.png")
                .into(cover);
    }

    private void loadUserInfo(String nsid) {
        ((AndroidApplication)getApplication()).api().getPeopleInfo(nsid, new Callback<PeopleInfo>() {
            @Override
            public void success(PeopleInfo peopleInfo, Response response) {
                TextView info = (TextView)findViewById(R.id.sub_text);
                int count = peopleInfo.person.photos.count._content;
                info.setText(count+" followers | "+count+" views");
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(
                        UserActivity.this,
                        getResources().getString(R.string.network_problem_message),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerFragment(String nsid) {
        FragmentManager fm = getSupportFragmentManager();
        fragment1 = fm.findFragmentByTag(TAG_FRAGMENT_1);
        if(fragment1 == null) {
            fragment1 = new UserBaseFragment();
            ObjectGraph graph = ObjectGraph.create(new UserPostListModule(nsid));
            graph.inject(fragment1);
            fm.beginTransaction().add(R.id.container, fragment1, TAG_FRAGMENT_1).commit();
            fm.beginTransaction().hide(fragment1).commit();
        }
        fragment2 = fm.findFragmentByTag(TAG_FRAGMENT_2);
        if(fragment2 == null) {
            fragment2 = new UserTimelineFragment();
            ObjectGraph graph = ObjectGraph.create(new UserTimelineModule(nsid));
            graph.inject(fragment2);
            fm.beginTransaction().add(R.id.container, fragment2, TAG_FRAGMENT_2).commit();
            fm.beginTransaction().hide(fragment2).commit();
        }
        fragment3 = fm.findFragmentByTag(TAG_FRAGMENT_3);
        if(fragment3 == null) {
            fragment3 = UserAboutFragment.newInstance(nsid);
            fm.beginTransaction().add(R.id.container, fragment3, TAG_FRAGMENT_3).commit();
            fm.beginTransaction().hide(fragment3).commit();
        }

    }

    private void adjustTabPosition(final UserTabImpl fragment) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                fragment.notifyListState();
            }
        };
        mTabLayout.post(runnable);
        mActionBarToolbar.post(runnable);
    }

    @Subscribe
    public void receiveGridViewEvent(ScrollEvent event) {
        // Log.i("EventBus", "TabEvent Received (list_top:" + event.list_top + ",dy:" + event.dy + ")");

        // scroll TabLayout with RecyclerView scroll
        float y = mTabLayout.getY();
        if (y == 0) return;  // ignore setup phase callback
        if (event.dy >= 0) {  // if up scroll
            mTabLayout.setY(Math.max(mActionBarHeight, event.list_top-mTabHeight));    // tab stops below actionbar
        } else {    // if down scroll
            if(event.list_top >= y+mTabHeight) {  // do not down TabLayout until list head is revealed
                mTabLayout.setY(Math.min(event.list_top-mTabHeight, mHeaderPadding));
            }
        }

        // change actionbar alpha with RecyclerView scroll
        if(mActionBarToolbar!=null) {  // nullpo occured when config changes
            mActionbarAlpha = (int) (255 *
                    (1 - (mTabLayout.getY() - mActionBarHeight) / (mHeaderPadding - mTabHeight - mActionBarHeight)));
            mActionBarToolbar.getBackground().setAlpha(mActionbarAlpha);
        }
    }

    static class ScrollEvent {
        final int list_top;
        final int dy;
        public ScrollEvent(int list_top, int dy) {
            this.list_top = list_top;
            this.dy = dy;
        }
    }

    private class MyTabListener implements TabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            mSelectedTabPos = tab.getPosition();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().hide(mDisplayingFragment).commit();
            switch (mSelectedTabPos) {
                case 0:
                    mDisplayingFragment = fragment1;
                    mTag = TAG_FRAGMENT_1;
                    break;
                case 1:
                    mDisplayingFragment = fragment2;
                    mTag = TAG_FRAGMENT_2;
                    break;
                case 2:
                    mDisplayingFragment = fragment3;
                    mTag = TAG_FRAGMENT_3;
                    break;
            }
            fm.beginTransaction().show(mDisplayingFragment).commit();

            // adjust tab position for next displaying list
            adjustTabPosition((UserTabImpl)mDisplayingFragment);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {}

        @Override
        public void onTabReselected(TabLayout.Tab tab) {}
    }

}
