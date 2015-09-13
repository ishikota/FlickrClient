package com.ikota.flickrclient.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.PhotoInfo;
import com.ikota.flickrclient.di.PopularListModule;
import com.ikota.flickrclient.util.FlickrUtil;

import dagger.ObjectGraph;


public class PopularListActivity extends BaseActivity {

    ImageListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find the retained fragment on activity restarts
        FragmentManager fm = getSupportFragmentManager();
        fragment = (ImageListFragment) fm.findFragmentByTag(ImageListFragment.class.getSimpleName());

        if (fragment == null) {
            fragment = new ImageListFragment();
            ObjectGraph graph = ObjectGraph.create(new PopularListModule());
            graph.inject(fragment);
            String tag = ImageListFragment.class.getSimpleName();
            fm.beginTransaction().add(R.id.container, fragment, tag).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_my_page) {
            PhotoInfo.Owner owner = FlickrUtil.getKota();
            Intent intent = UserActivity.createIntent(PopularListActivity.this, owner);
            startActivity(intent);
        } else if(item.getItemId() == R.id.action_search) {
            Intent intent = new Intent(PopularListActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
