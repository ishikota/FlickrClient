package com.ikota.flickrclient.ui;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.ListData;
import com.ikota.flickrclient.util.ImageUtils;
import com.ikota.flickrclient.util.NetUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserTimelineAdapter
        extends RecyclerView.Adapter<UserTimelineAdapter.ViewHolder> implements ListAdapterImpl {

    private Context mContext;
    private List<ListData.Photo> mDataSet;
    private final ImageAdapter.OnClickCallback mClickCallback;
    private final LayoutInflater mInflater;
    private boolean is_wifi_connected;
    private int view_size;

    public void setViewSize(int size) {
        view_size = size;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView item_image, user_image, ic_fav, ic_com, ic_share;
        public TextView user_text, date_text, fav_text, com_text, share_text;

        public ViewHolder(View v) {
            super(v);
            item_image = (ImageView) v.findViewById(R.id.image);
            ic_fav = (ImageView) v.findViewById(R.id.ic_favorite);
            ic_com = (ImageView) v.findViewById(R.id.ic_comment);
            ic_share = (ImageView) v.findViewById(R.id.ic_share);
            user_image = (ImageView) v.findViewById(R.id.user_icon);
            user_text = (TextView) v.findViewById(R.id.user_name);
            date_text = (TextView) v.findViewById(R.id.date_text);
            fav_text = (TextView) v.findViewById(R.id.favorite_num);
            com_text = (TextView) v.findViewById(R.id.comment_num);
            share_text = (TextView) v.findViewById(R.id.share_num);
        }
    }


    public UserTimelineAdapter(Context context, List<ListData.Photo> myDataset, ImageAdapter.OnClickCallback listener) {
        mContext = context;
        mDataSet = myDataset;
        mClickCallback = listener;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // check network state
        is_wifi_connected = NetUtils.isWifiConnected(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = mInflater.inflate(R.layout.row_timeline, viewGroup, false);

        ImageView imageview = (ImageView)v.findViewById(R.id.image);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag(R.integer.list_pos_key);
                if (mClickCallback != null) {
                    mClickCallback.onClick(v, mDataSet.get(position));
                }
            }
        });
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final ListData.Photo item = mDataSet.get(position);

        // set list position as tag
        holder.item_image.setTag(R.integer.list_pos_key, position);
        holder.user_text.setText(item.title);
        holder.date_text.setText("2015-04-29 15:22:34");

        // set click listener
        setFavoIcon(holder.ic_fav, item.isfriend == 1);
        holder.ic_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFavoIcon((ImageView) view, item.isfriend == 0);
                item.isfriend = (item.isfriend + 1) % 2;
            }
        });
        holder.ic_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((UserTimelineFragment.OnTimelineClickCallback)mClickCallback)
                        .onCommentClicked(
                                item.id,
                                item.title,
                                item.generatePhotoURL(view_size, is_wifi_connected));
            }
        });
        holder.ic_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((UserTimelineFragment.OnTimelineClickCallback)mClickCallback)
                        .onShareClicked(
                                item.title,
                                item.generatePhotoURL(view_size, is_wifi_connected));
            }
        });

        // load image
        Picasso.with(mContext)
                .load(item.generateOwnerIconURL())
                .placeholder(R.drawable.loading_default)
                .error(R.drawable.ic_image_broken)
                .into(holder.user_image);

        Picasso.with(mContext)
                .load(item.generatePhotoURL(view_size, is_wifi_connected))
                .placeholder(R.drawable.loading_default)
                .error(R.drawable.ic_image_broken)
                .into(holder.item_image);
    }

    private void setFavoIcon(ImageView v, boolean pressed) {
        Drawable src = v.getDrawable().mutate();
        if(pressed) {
            v.setImageDrawable(ImageUtils.tintImage(mContext, src, R.color.accent));
            v.setAlpha(0.9f);
        } else {
            v.setImageDrawable(ImageUtils.tintImage(mContext, src, R.color.primary));
            v.setAlpha(0.5f);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public void setIfWifiConnected(boolean wifi_connected) {
        is_wifi_connected = wifi_connected;
    }

}
