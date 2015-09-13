package com.ikota.flickrclient.ui;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.ikota.flickrclient.R;
import com.ikota.flickrclient.di.TagListModule;

import dagger.ObjectGraph;

public class TagListActivity extends BaseActivity{

    public static final String EXTRA_TAG = "tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup toolbar
        String query = getIntent().getStringExtra(EXTRA_TAG);  // get query string
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        mActionBarToolbar.setTitle(query);

        // find the retained fragment on activity restarts
        FragmentManager fm = getSupportFragmentManager();
        ImageListFragment fragment = (ImageListFragment) fm.findFragmentByTag(ImageListFragment.class.getSimpleName());

        if (fragment == null) {
            fragment = new ImageListFragment();
            ObjectGraph graph = ObjectGraph.create(new TagListModule(query));
            graph.inject(fragment);
            String tag = ImageListFragment.class.getSimpleName();
            fm.beginTransaction().add(R.id.container, fragment, tag).commit();
        }
    }

}
