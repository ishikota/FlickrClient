package com.ikota.flickrclient.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.ikota.flickrclient.R;

public class CommentListActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_back_arrow);

        // find the retained fragment on activity restarts
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(CommetListFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = new CommetListFragment();
            String tag = CommetListFragment.class.getSimpleName();
            fm.beginTransaction().add(R.id.container, fragment, tag).commit();
        }
    }

}
