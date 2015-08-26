package com.ikota.flickrclient.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.PeopleInfo;
import com.ikota.flickrclient.data.model.PhotoInfo;
import com.ikota.flickrclient.di.UserPostListModule;
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

    private TabLayout mTabLayout;

    private int mActionBarHeight;
    private int mTabHeight;
    private int mHeaderPadding;

    public static Intent createIntent(Activity activity, PhotoInfo.Owner owner) {
        Gson gson = new Gson();
        String parsed_json = gson.toJson(owner);

        Intent intent = new Intent(activity, UserActivity.class);
        intent.putExtra(EXTRA_CONTENT, parsed_json);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // get content
        Gson gson = new Gson();
        String json = getIntent().getStringExtra(EXTRA_CONTENT);
        PhotoInfo.Owner content = gson.fromJson(json, PhotoInfo.Owner.class);

        getViewSize();
        setupActionBar();
        setupViews(content);
        loadUserInfo(content);


        FragmentManager fm = getSupportFragmentManager();
        UserBaseFragment mFragment = (UserBaseFragment)
                fm.findFragmentByTag(UserBaseFragment.class.getSimpleName());
        if (mFragment == null) {
            mFragment = new UserBaseFragment();
            ObjectGraph graph = ObjectGraph.create(new UserPostListModule());
            graph.inject(mFragment);
            String tag = UserBaseFragment.class.getSimpleName();
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

    private void setupViews(PhotoInfo.Owner content) {

        // nullpo occurred when config changes
        if(mActionBarToolbar!=null) mActionBarToolbar.getBackground().setAlpha(0);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.addTab(mTabLayout.newTab().setText("ABOUTS"));
        mTabLayout.addTab(mTabLayout.newTab().setText("POST"));
        mTabLayout.addTab(mTabLayout.newTab().setText("PHOTOS"));

        ImageView cover = (ImageView)findViewById(R.id.cover);
        ImageView icon = (ImageView)findViewById(R.id.user_icon);
        TextView name = (TextView)findViewById(R.id.user_name);
        Picasso.with(UserActivity.this)
                .load(content.generateOwnerIconURL())
                .into(icon);
        Picasso.with(UserActivity.this)
                .load("https://d13yacurqjgara.cloudfront.net/users/44585/screenshots/1544389/book-pattern.png")
                .into(cover);
        name.setText(content.username);
    }

    private void loadUserInfo(PhotoInfo.Owner content) {
        ((AndroidApplication)getApplication()).api().getPeopleInfo(content.nsid, new Callback<PeopleInfo>() {
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
