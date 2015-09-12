package com.ikota.flickrclient.ui;

import android.content.Context;
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
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<ListData.Photo> mDataSet;
    private final OnClickCallback mClickCallback;
    private final LayoutInflater mInflater;

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


    public SearchAdapter(Context context, List<ListData.Photo> myDataset,
                         OnClickCallback listener) {
        mDataSet = myDataset;
        mClickCallback = listener;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = mInflater.inflate(R.layout.search_category_row, viewGroup, false);

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
