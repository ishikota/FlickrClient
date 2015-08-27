package com.ikota.flickrclient.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.ListData;
import com.ikota.flickrclient.util.NetUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kota on 2015/08/11.
 * This adapter is used in ImageListFragment
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Context mContext;
    private List<ListData.Photo> mDataSet;
    private final OnClickCallback mClickCallback;
    private final LayoutInflater mInflater;
    private boolean is_wifi_connected;

    public void setIfWifiConnected(boolean wifi_connected) {
        is_wifi_connected = wifi_connected;
    }

    public interface OnClickCallback {
        void onClick(View v, ListData.Photo data);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageview;

        public ViewHolder(View v) {
            super(v);
            imageview = (ImageView) v.findViewById(R.id.image);
        }
    }


    public ImageAdapter(Context context, List<ListData.Photo> myDataset, OnClickCallback listener) {
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
        View v = mInflater.inflate(R.layout.row_image_list, viewGroup, false);

        ImageView imageview = (ImageView)v.findViewById(R.id.image);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag(R.integer.list_pos_key);
                if(mClickCallback != null) {
                    mClickCallback.onClick(v, mDataSet.get(position));
                }
            }
        });
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // set list position as tag
        holder.imageview.setTag(R.integer.list_pos_key, position);

        // change image quality by checking network state
        String quality_flg = is_wifi_connected ? "z": "q";

        ListData.Photo item = mDataSet.get(position);

        String url = item.generatePhotoURL(quality_flg);
        Picasso.with(mContext)
                .load(url)
                .placeholder(R.drawable.loading_default)
                .error(R.drawable.ic_image_broken)
                .into(holder.imageview);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


}
