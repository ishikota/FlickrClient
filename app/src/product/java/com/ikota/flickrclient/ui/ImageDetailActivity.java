package com.ikota.flickrclient.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.ListData;


public class ImageDetailActivity extends BaseActivity{
    public static final String EXTRA_CONTENT = "content";
    public static final String EXTRA_CACHE_SIZE = "cache_size";

    public static void launch(Activity activity, ListData.Photo data, ImageView transitionView, String size) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,
                transitionView,
                activity.getString(R.string.trans_list2detail));

        Gson gson = new Gson();
        String parsed_json = gson.toJson(data);

        Intent intent = new Intent(activity, ImageDetailActivity.class);
        intent.putExtra(EXTRA_CONTENT, parsed_json);
        intent.putExtra(EXTRA_CACHE_SIZE, size);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        //noinspection ConstantConditions
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);

        if (savedInstanceState == null) {
            String tag = ImageDetailFragment.class.getSimpleName();
            String json = getIntent().getStringExtra(EXTRA_CONTENT);
            String size = getIntent().getStringExtra(EXTRA_CACHE_SIZE);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, ImageDetailFragment.newInstance(json, size), tag)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            supportFinishAfterTransition();
        } else if(id == R.id.action_download){
            return true;
        } else if(id == R.id.action_share) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
