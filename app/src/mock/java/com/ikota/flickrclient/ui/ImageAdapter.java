package com.ikota.flickrclient.ui;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.ListData;

import java.util.List;

/**
 * Created by kota on 2015/08/11.
 * This adapter is used in ImageListFragment
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private List<ListData.Photo> mDataSet;
    private final OnClickCallback mClickCallback;
    private final LayoutInflater mInflater;
    private final int view_size;

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


    public ImageAdapter(Context context, List<ListData.Photo> myDataset,
                        OnClickCallback listener, int column) {
        mDataSet = myDataset;
        mClickCallback = listener;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // init grid size (grid size = display_width/column_num).
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        view_size = size.x / column;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = mInflater.inflate(R.layout.row_image_list, viewGroup, false);

        // set view size here
        ViewGroup.LayoutParams params = v.getLayoutParams();
        params.height = view_size;
        v.setLayoutParams(params);

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
        holder.imageview.setBackgroundColor(mDataSet.get(position).farm);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


}
