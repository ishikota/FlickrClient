package com.ikota.flickrclient.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.ListData;
import com.ikota.flickrclient.util.ImageUtils;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;


public class ImageDetailActivity extends BaseActivity{

    public static final String EXTRA_CONTENT = "content";

    static Bus sTabEventBus = new Bus();

    private int mToolbarAlpha = 0;

    public static void launch(Activity activity, ListData.Photo data, ImageView transitionView) {
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

        //noinspection ConstantConditions
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);

        if (savedInstanceState == null) {
            String tag = ImageDetailFragment.class.getSimpleName();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ImageDetailFragment(), tag)
                    .commit();
        }

        TextView favo_text = (TextView)findViewById(R.id.favorite_num);
        favo_text.setText("56");
        favo_text.setTag(56);
        findViewById(R.id.ic_favorite).setTag(false);
        findViewById(R.id.ic_favorite).setOnClickListener(favo_click_listener);
        findViewById(R.id.ic_comment).setClickable(true);
        findViewById(R.id.ic_share).setClickable(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        sTabEventBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        sTabEventBus.unregister(this);
    }

    @Subscribe
    public void receiveGridViewEvent(ScrollEvent event) {
        // change actionbar alpha with RecyclerView scroll
        if(mActionBarToolbar!=null) {  // nullpo occured when config changes
            mToolbarAlpha += event.dy;
            mToolbarAlpha = Math.max(0, Math.min(255, mToolbarAlpha));
            mActionBarToolbar.getBackground().setAlpha(mToolbarAlpha);
        }
    }

    static class ScrollEvent {
        final int dy;
        public ScrollEvent(int dy) {
            this.dy = dy;
        }
    }

    private final View.OnClickListener favo_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView favo_text = (TextView)findViewById(R.id.favorite_num);
            boolean pressed = (boolean) view.getTag();
            pressed = !pressed;
            setFavoIcon((ImageView) view, pressed);
            view.setTag(pressed);
            int favo_num = (int) favo_text.getTag();
            favo_num = pressed ? favo_num+1 : favo_num-1;
            favo_text.setText(String.valueOf(favo_num));
            favo_text.setTag(favo_num);
        }
    };

    private void setFavoIcon(ImageView v, boolean pressed) {
        Drawable src = v.getDrawable().mutate();
        if(pressed) {
            v.setImageDrawable(ImageUtils.tintImage(ImageDetailActivity.this, src, R.color.accent));
            v.setAlpha(1.0f);
        } else {
            v.setImageDrawable(ImageUtils.tintImage(ImageDetailActivity.this, src, R.color.primary));
            v.setAlpha(0.5f);
        }
    }

}
