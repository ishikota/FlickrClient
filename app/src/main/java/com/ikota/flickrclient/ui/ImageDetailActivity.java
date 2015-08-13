package com.ikota.flickrclient.ui;

import android.os.Bundle;

import com.ikota.flickrclient.R;

/**
 * Created by kota on 2015/08/14.
 * Activity of detail page.
 */
public class ImageDetailActivity extends BaseActivity{
    public static final String EXTRA_CONTENT = "content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);



        if (savedInstanceState == null) {
            String json = getIntent().getStringExtra(EXTRA_CONTENT);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, ImageDetailFragment.newInstance(json))
                    .commit();
        }
    }

}
