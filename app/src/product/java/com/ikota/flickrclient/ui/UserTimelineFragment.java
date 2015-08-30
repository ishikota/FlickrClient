package com.ikota.flickrclient.ui;


import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.ikota.flickrclient.data.model.ListData;

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
                                    Toast.makeText(getActivity(),
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
                // todo: implement
            }
        };
    }

}
