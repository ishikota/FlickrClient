package com.ikota.flickrclient.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.ikota.flickrclient.R;


public class MainActivity extends BaseActivity {

    PopularListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find the retained fragment on activity restarts
        FragmentManager fm = getSupportFragmentManager();
        fragment = (PopularListFragment) fm.findFragmentByTag(PopularListFragment.class.getSimpleName());

        if (fragment == null) {
            String tag = PopularListFragment.class.getSimpleName();
            fm.beginTransaction().add(R.id.container, new PopularListFragment(), tag).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
