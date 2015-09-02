package com.ikota.flickrclient.ui;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ikota.flickrclient.R;


public class ImageDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final Context mContext;
    private final LayoutInflater mInflater;
    private final ImageAdapter.OnClickCallback mCallback;

    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_main, image_user;
        public TextView text_title, text_description, text_username, text_date;
        public LinearLayout comment_parent;
        public DetailViewHolder(View v) {
            super(v);
            image_main = (ImageView)v.findViewById(R.id.image);
            image_user = (ImageView)v.findViewById(R.id.user_icon);
            text_title = (TextView)v.findViewById(R.id.title);
            text_description = (TextView)v.findViewById(R.id.description);
            text_username = (TextView)v.findViewById(R.id.user_name);
            text_date = (TextView)v.findViewById(R.id.date_text);
            comment_parent = (LinearLayout)v.findViewById(R.id.comment_parent);
        }
    }

    public static class SomeViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public SomeViewHolder(View v) {
            super(v);
            image = (ImageView)v.findViewById(R.id.image);
        }
    }

    public ImageDetailAdapter(Context context, ImageAdapter.OnClickCallback listener) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCallback = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) return position;
        return 1;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        switch (viewType) {
            case 0:
                v = mInflater.inflate(R.layout.row_image_detail, viewGroup, false);
                return new DetailViewHolder(v);
            default:
                v = mInflater.inflate(R.layout.row_image_list, viewGroup, false);
                return new SomeViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Resources r = mContext.getResources();
        if(position == 0) {
            DetailViewHolder vh = (DetailViewHolder)holder;

            // set content
            vh.text_title.setText(r.getString(R.string.title));
            vh.text_username.setText(r.getString(R.string.username));
            vh.text_date.setText("Posted on "+r.getString(R.string.date));
            vh.text_description.setText(r.getString(R.string.description));

            // set comment
            vh.comment_parent.removeAllViews();
            vh.comment_parent.addView(createCommentView(vh.comment_parent, r.getString(R.string.comment1)));
            vh.comment_parent.addView(createCommentView(vh.comment_parent, r.getString(R.string.comment2)));
            vh.comment_parent.addView(createCommentView(vh.comment_parent, r.getString(R.string.comment3)));
            vh.comment_parent.addView(createSummaryView(vh.comment_parent));

            // set listener
            vh.text_description.setTag(false);
            vh.text_description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean expanded = (boolean) view.getTag();
                    TextView tv = (TextView) view;
                    if (expanded) {
                        tv.setMaxLines(3);
                    } else {
                        tv.setMaxLines(100);
                    }
                    view.setTag(!expanded);
                }
            });
            vh.image_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onClick(view, null);
                }
            });
        } else {
            //SomeViewHolder vh = (SomeViewHolder)holder;
            Log.i("ImageDetailAdapter", "someViewHolder called");
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private View createCommentView(LinearLayout parent, String message) {
        View v = mInflater.inflate(R.layout.comment_row, parent, false);
        ((TextView)v.findViewById(R.id.comment_text)).setText(message);
        return v;
    }

    private View createSummaryView(LinearLayout parent) {
        Resources r = mContext.getResources();
        View v = mInflater.inflate(R.layout.comment_row, parent, false);
        ImageView icon = (ImageView)v.findViewById(R.id.user_icon);
        icon.setImageResource(R.drawable.ic_trending_flat_black_18dp);
        icon.setAlpha(0.5f);
        String summary = r.getString(R.string.comment_summary_prefix)+" 56 "+r.getString(R.string.comment_summary_suffix);
        ((TextView)v.findViewById(R.id.comment_text)).setText(summary);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setTag("GO CommentActivity!!");
                mCallback.onClick(view, null);
            }
        });
        return v;
    }
}
