package com.ikota.flickrclient.ui;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.util.TypedValue;
import android.widget.TextView;

import com.ikota.flickrclient.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class UserActivity extends BaseActivity{

    static Bus sTabEventBus = new Bus();

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
        UserBaseFragment mFragment = (UserPostListFragment)
                fm.findFragmentByTag(UserPostListFragment.class.getSimpleName());
        if (mFragment == null) {
            mFragment = new UserPostListFragment();
            String tag = UserPostListFragment.class.getSimpleName();
            fm.beginTransaction().add(R.id.container, mFragment, tag).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sTabEventBus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
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
        mTabLayout.addTab(mTabLayout.newTab().setText("POST"));
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


}
