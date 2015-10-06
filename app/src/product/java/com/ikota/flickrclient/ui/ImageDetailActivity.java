package com.ikota.flickrclient.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.ListData;
import com.ikota.flickrclient.util.ImageUtils;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;


public class ImageDetailActivity extends BaseActivity{

    public static void launch(Activity activity, ListData.Photo data, ImageView transitionView, String size) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,
                transitionView,
                activity.getString(R.string.trans_list2detail));

        String parsed_json = GSON.toJson(data);

        Intent intent = new Intent(activity, ImageDetailActivity.class);
        intent.putExtra(EXTRA_CONTENT, parsed_json);
        intent.putExtra(EXTRA_CACHE_SIZE, size);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private static final Gson GSON = new Gson();
    public static final String EXTRA_CONTENT = "content";
    public static final String EXTRA_CACHE_SIZE = "cache_size";

    static Bus sTabEventBus = new Bus();

    private int mToolbarAlpha = 255;
    private ListData.Photo mData;
    private ImageView mFavoIcon;
    private TextView mFavoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        // construct data object
        String json = getIntent().getStringExtra(EXTRA_CONTENT);
        mData = GSON.fromJson(json, ListData.Photo.class);

        // setup toolbar
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_back_arrow);

        setupViews();
        setClickListener();

        if (savedInstanceState == null) {
            String tag = ImageDetailFragment.class.getSimpleName();
            String size = getIntent().getStringExtra(EXTRA_CACHE_SIZE);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, ImageDetailFragment.newInstance(json, size), tag)
                    .commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sTabEventBus.register(this);
        if(mActionBarToolbar!=null) mActionBarToolbar.getBackground().mutate().setAlpha(mToolbarAlpha);
    }

    @Override
    public void onPause() {
        super.onPause();
        sTabEventBus.unregister(this);
        if(mActionBarToolbar!=null) {
            mActionBarToolbar.getBackground().mutate().setAlpha(255);
        }
    }

    @Subscribe
    public void receiveGridViewEvent(ScrollEvent event) {
        // change actionbar alpha with RecyclerView scroll
        if(mActionBarToolbar!=null) {  // nullpo occured when config changes
            mToolbarAlpha += event.dy;
            mToolbarAlpha = Math.max(0, Math.min(200, mToolbarAlpha));
            mActionBarToolbar.getBackground().mutate().setAlpha(mToolbarAlpha);
        }
    }

    static class ScrollEvent {
        final int dy;
        public ScrollEvent(int dy) {
            this.dy = dy;
        }
    }

    @Subscribe
    public void receiveToolbarColor(PaletteEvent event) {
    }

    static class PaletteEvent {
        final int color;
        public PaletteEvent(int color) {
            this.color = color;
        }
    }

    private void setupViews() {
        // nullpo occurred when config changes
        mToolbarAlpha = 0;  // we need to manually set initial visibility.
        if(mActionBarToolbar!=null) mActionBarToolbar.getBackground().mutate().setAlpha(mToolbarAlpha);

        // find views
        mFavoIcon = (ImageView)findViewById(R.id.ic_favorite);
        mFavoText = (TextView)findViewById(R.id.favorite_num);

        // set contents
        mData.ispublic = Integer.valueOf(mData.server);  // use server as dummy favorite num
        mFavoText.setText(String.valueOf(mData.ispublic));
        setFavoIcon(mFavoIcon, mData.isfriend == 1);
    }

    private void setClickListener() {
        final Context context = ImageDetailActivity.this;
        final View parent_layout = findViewById(android.R.id.content);

        // update favorite state
        findViewById(R.id.ic_favorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean pressed = mData.isfriend == 1;
                mData.isfriend = (mData.isfriend + 1) % 2;
                mData.ispublic = !pressed ? mData.ispublic + 1 : mData.ispublic - 1;
                setFavoIcon(mFavoIcon, !pressed);
                mFavoText.setText(String.valueOf(mData.ispublic));
            }
        });

        // show comment dialog
        findViewById(R.id.ic_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = mData.generatePhotoURL(getIntent().getStringExtra(EXTRA_CACHE_SIZE));
                CommentDialog dialog = CommentDialog.newInstance(mData.id, mData.title, url);
                final Handler handler = new Handler();
                dialog.setOnFinishListener(new CommentDialog.OnFinishListener() {
                    @Override
                    public void onFinish(final String comment) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(!comment.isEmpty()) {
                                    Snackbar.make(parent_layout, R.string.posted, Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        }, 2000);  // dummy network communication
                    }
                });
                dialog.show(getSupportFragmentManager(), "DIALOG");
            }
        });

        // save image into device
        findViewById(R.id.ic_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.with(context).load(mData.generatePhotoURL("b")).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        boolean success = ImageUtils.savePicture(context, bitmap, true).first;
                        if(success) {
                            Snackbar.make(parent_layout, "Saved", Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(parent_layout, "Save Failed", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        Snackbar.make(parent_layout, "Save Failed", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        Snackbar.make(parent_layout, "Loading...", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // show chooser dialog for share
        findViewById(R.id.ic_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.with(context)
                        .load(mData.generatePhotoURL("b"))
                        .into(IMG_LOAD_HANDLER);
            }
        });
    }

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

    private final Target IMG_LOAD_HANDLER = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            final View parent_layout = findViewById(android.R.id.content);
            Context context = ImageDetailActivity.this;
            Resources r = getResources();
            File dist = ImageUtils.savePicture(context, bitmap, true).second;
            if (dist != null) {
                Uri uri = Uri.fromFile(dist);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpg");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    startActivity(Intent.createChooser(intent,r.getString(R.string.share_intent_title)));
                } catch (ActivityNotFoundException e) {
                    Snackbar.make(parent_layout, r.getString(R.string.share_failed), Snackbar.LENGTH_SHORT).show();
                }
            } else {
                Snackbar.make(parent_layout, r.getString(R.string.share_failed), Snackbar.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Toast.makeText(ImageDetailActivity.this,
                    getResources().getString(R.string.share_failed),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            final View parent_layout = findViewById(android.R.id.content);
            Snackbar.make(parent_layout, "Loading...",Snackbar.LENGTH_SHORT);
        }
    };

}
