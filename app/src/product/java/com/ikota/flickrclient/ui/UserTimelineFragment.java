package com.ikota.flickrclient.ui;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.ListData;
import com.ikota.flickrclient.util.ImageUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

public class UserTimelineFragment extends UserBaseFragment{

    public interface OnTimelineClickCallback extends ImageAdapter.OnClickCallback {
        void onCommentClicked(String title, String img_url);
        void onShareClicked(String title, String img_url);
    }

    @Override
    protected ImageAdapter.OnClickCallback getItemClickListener() {
        final ImageAdapter.OnClickCallback super_callback = super.getItemClickListener();
        return new OnTimelineClickCallback() {
            @Override
            public void onClick(View v, ListData.Photo data) {
                super_callback.onClick(v, data);
            }

            @Override
            public void onCommentClicked(String title, String img_url) {
                final Handler handler = new Handler();
                CommentDialog dialog = CommentDialog.newInstance(title, img_url);
                dialog.setOnFinishListener(new CommentDialog.OnFinishListener() {
                    @Override
                    public void onFinish(String comment) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(isAdded()) {
                                    Toast.makeText(mAppContext,
                                            "Comment Created", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, 2000);
                    }
                });
                dialog.show(getChildFragmentManager(), "dialog");
            }

            @Override
            public void onShareClicked(String title, String img_url) {
                Picasso.with(mAppContext)
                        .load(img_url)
                        .into(IMG_LOAD_HANDLER);
            }
        };
    }

    private final Target IMG_LOAD_HANDLER = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Resources r = getResources();
            File dist = ImageUtils.savePicture(mAppContext, bitmap, true);
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
                    Toast.makeText(mAppContext,r.getString(R.string.share_failed),Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mAppContext,r.getString(R.string.share_failed),Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Toast.makeText(mAppContext,
                    getResources().getString(R.string.share_failed),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            // do nothing
        }
    };

}
