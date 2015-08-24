package com.ikota.flickrclient.ui;


import android.os.Bundle;
import android.view.MenuItem;

import com.ikota.flickrclient.R;

public class UserActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);

        if (savedInstanceState == null) {
            String tag = UserFragment.class.getSimpleName();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new UserFragment(), tag)
                    .commit();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
