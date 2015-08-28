package com.ikota.flickrclient.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.ListData;

import java.util.List;

/**
 * Created by kota on 2015/08/11.
 * This adapter is used in ImageListFragment
 */
public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder> {
    private List<ListData.Photo> mDataSet;
    private final ImageAdapter.OnClickCallback mClickCallback;
    private final LayoutInflater mInflater;

    private final Bitmap big_image;
    private final Bitmap small_iamge;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageview;

        public ViewHolder(View v) {
            super(v);
            imageview = (ImageView) v.findViewById(R.id.image);
        }
    }

    public TimeLineAdapter(Context context, List<ListData.Photo> myDataset,
                           ImageAdapter.OnClickCallback listener) {
        mDataSet = myDataset;
        mClickCallback = listener;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        big_image = Bitmap.createBitmap(1080, 1080, Bitmap.Config.ARGB_8888);
        small_iamge = Bitmap.createBitmap(1080, 540, Bitmap.Config.ARGB_8888);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = mInflater.inflate(R.layout.row_timeline, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // set list position as tag
        switch (mDataSet.get(position).isfamily) {
            case 0:
                holder.imageview.setImageBitmap(big_image);
                break;
            case 1:
                holder.imageview.setImageBitmap(small_iamge);
                break;
        }
        holder.imageview.setBackgroundColor(mDataSet.get(position).farm);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


}
