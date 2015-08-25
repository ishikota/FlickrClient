package com.ikota.flickrclient.ui;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.ikota.flickrclient.R;

public class UserActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //noinspection ConstantConditions
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);

        FragmentManager fm = getSupportFragmentManager();
        UserFragment fragment = (UserFragment) fm.findFragmentByTag(UserFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = new UserFragment();
            String tag = UserFragment.class.getSimpleName();
            fm.beginTransaction()
                    .add(R.id.container, fragment, tag)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            supportFinishAfterTransition();
        }

        return super.onOptionsItemSelected(item);
    }

}
