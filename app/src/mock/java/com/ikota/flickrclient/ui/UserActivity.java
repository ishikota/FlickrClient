package com.ikota.flickrclient.ui;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ikota.flickrclient.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class UserActivity extends BaseActivity{

    static Bus sTabEventBus = new Bus();

    private Fragment mDisplayingFragment;
    private TabLayout mTabLayout;

    private int mActionBarHeight;
    private int mTabHeight;
    private int mHeaderPadding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setupActionBar();
        setupViews();
        getViewSize();

        FragmentManager fm = getSupportFragmentManager();
        mDisplayingFragment = fm.findFragmentByTag(UserPostListFragment.class.getSimpleName());
        if (mDisplayingFragment == null) {
            mDisplayingFragment = new UserPostListFragment();
            String tag = UserPostListFragment.class.getSimpleName();
            fm.beginTransaction().add(R.id.container, mDisplayingFragment, tag).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_comment) {
            CommentDialog dialog = CommentDialog.newInstance(getResources().getString(R.string.title));
            dialog.setOnFinishListener(new CommentDialog.OnFinishListener() {
                @Override
                public void onFinish(String comment) {
                    Toast.makeText(UserActivity.this,
                            "comment : "+comment, Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show(getSupportFragmentManager(), "dialog");
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        sTabEventBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        sTabEventBus.unregister(this);
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

    private void setupViews() {
        Resources r = getResources();
        ((TextView)findViewById(R.id.user_name)).setText(r.getString(R.string.username));
        ((TextView) findViewById(R.id.sub_text)).setText(r.getString(R.string.user_info));

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.addTab(mTabLayout.newTab().setText("ABOUTS"));
        mTabLayout.setOnTabSelectedListener(new MyTabListener(new UserPostListFragment()));
        mTabLayout.addTab(mTabLayout.newTab().setText("POST"));
        mTabLayout.setOnTabSelectedListener(new MyTabListener(new UserTimelineFragment()));
        mTabLayout.addTab(mTabLayout.newTab().setText("PHOTOS"));

        if(mActionBarToolbar!=null)  // nullpo occurred when config changes
            mActionBarToolbar.getBackground().setAlpha(0);
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
            int alpha = (int) (255 *
                    (1 - (y - mActionBarHeight) / (mHeaderPadding - mTabHeight - mActionBarHeight)));
            mActionBarToolbar.getBackground().setAlpha(alpha);
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
        private final Fragment mFragment;

        MyTabListener(Fragment fragment) {
            this.mFragment = fragment;
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            getSupportFragmentManager().beginTransaction()
                    .detach(mDisplayingFragment)
                    .commit();
            switch (tab.getPosition()) {
                case 0:
                    mDisplayingFragment = new UserPostListFragment();
                    break;
                case 1:
                    mDisplayingFragment = new UserFavoriteFragment();
                    break;
                case 2:
                    mDisplayingFragment = new UserAboutFragment();
                    break;
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, mDisplayingFragment)
                    .commit();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .detach(mFragment).commit();
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }


}
