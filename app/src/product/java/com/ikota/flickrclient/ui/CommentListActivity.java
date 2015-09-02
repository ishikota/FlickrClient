package com.ikota.flickrclient.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.ikota.flickrclient.R;

public class CommentListActivity extends BaseActivity{

    static final String EXTRA_URL ="url";
    static final String EXTRA_TITLE ="title";
    static final String EXTRA_DATA = "data";

    public static Intent createIntent(Context context, String url, String title, String json) {
        Intent intent = new Intent(context, CommentListActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_DATA, json);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_back_arrow);

        String url = getIntent().getStringExtra(EXTRA_URL);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        String json = getIntent().getStringExtra(EXTRA_DATA);

        // find the retained fragment on activity restarts
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(CommentListFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = CommentListFragment.newInstance(url, title, json);
            String tag = CommentListFragment.class.getSimpleName();
            fm.beginTransaction().add(R.id.container, fragment, tag).commit();
        }
    }


}
