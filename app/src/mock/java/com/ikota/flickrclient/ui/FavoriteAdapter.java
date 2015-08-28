package com.ikota.flickrclient.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.ListData;

import java.util.List;

/**
 * Created by kota on 2015/08/11.
 * This adapter is used in ImageListFragment
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private Context mContext;
    private List<ListData.Photo> mDataSet;
    private final ImageAdapter.OnClickCallback mClickCallback;
    private final LayoutInflater mInflater;

    private final Bitmap small_iamge;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageview;
        public TextView user_name, date, fav_num, com_num, share_num;

        public ViewHolder(View v) {
            super(v);
            imageview = (ImageView) v.findViewById(R.id.image);
            user_name = (TextView) v.findViewById(R.id.user_name);
            date = (TextView) v.findViewById(R.id.date_text);
            fav_num = (TextView) v.findViewById(R.id.favorite_num);
            com_num = (TextView) v.findViewById(R.id.comment_num);
            share_num = (TextView) v.findViewById(R.id.share_num);
        }
    }

    public FavoriteAdapter(Context context, List<ListData.Photo> myDataset,
                           ImageAdapter.OnClickCallback listener) {
        mContext = context;
        mDataSet = myDataset;
        mClickCallback = listener;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        small_iamge = Bitmap.createBitmap(1080, 540, Bitmap.Config.ARGB_8888);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = mInflater.inflate(R.layout.row_timeline, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageview.setImageBitmap(small_iamge);
        holder.imageview.setBackgroundColor(mDataSet.get(position).farm);
        holder.user_name.setText("Ishikota");
        holder.date.setText("2015-06-04 11:30:21");
        holder.fav_num.setText("3");
        holder.com_num.setText("20");
        holder.share_num.setText("1");
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


}
