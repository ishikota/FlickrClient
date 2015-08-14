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
import com.ikota.flickrclient.model.FlickerListItem;

/**
 * Created by kota on 2015/08/14.
 * Activity of detail page.
 */
public class ImageDetailActivity extends BaseActivity{
    public static final String EXTRA_CONTENT = "content";

    public static void launch(Activity activity, FlickerListItem data, ImageView transitionView) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,
                transitionView,
                activity.getString(R.string.trans_list2detail));

        Gson gson = new Gson();
        String parsed_json = gson.toJson(data);

        Intent intent = new Intent(activity, ImageDetailActivity.class);
        intent.putExtra(EXTRA_CONTENT, parsed_json);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);

        if (savedInstanceState == null) {
            String json = getIntent().getStringExtra(EXTRA_CONTENT);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, ImageDetailFragment.newInstance(json))
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
