package com.ikota.flickrclient.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
        public ImageView imageview, ic_fav, ic_com, ic_share;
        public TextView user_name, date, fav_num, com_num, share_num;

        public ViewHolder(View v) {
            super(v);
            imageview = (ImageView) v.findViewById(R.id.image);
            ic_fav = (ImageView) v.findViewById(R.id.ic_favorite);
            ic_com = (ImageView) v.findViewById(R.id.ic_comment);
            ic_share = (ImageView) v.findViewById(R.id.ic_share);
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ListData.Photo item = mDataSet.get(position);
        holder.imageview.setImageBitmap(small_iamge);
        holder.imageview.setBackgroundColor(item.farm);
        holder.user_name.setText("Ishikota");
        holder.date.setText("2015-06-04 11:30:21");
        holder.fav_num.setText("3");
        holder.com_num.setText("20");
        holder.share_num.setText("1");
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
                // TODO implements
            }
        });
        holder.ic_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO implements
            }
        });
    }

    private void setFavoIcon(ImageView v, boolean pressed) {
        Drawable src = v.getDrawable().mutate();
        if(pressed) {
            v.setImageDrawable(tintImage(mContext, src, R.color.accent));
            v.setAlpha(1.0f);
        } else {
            v.setImageDrawable(tintImage(mContext, src, R.color.primary));
            v.setAlpha(0.5f);
        }
    }


    private Drawable tintImage(Context context, Drawable src, int res_color) {
        int tint = context.getResources().getColor(res_color);
        PorterDuff.Mode mode = PorterDuff.Mode.SRC_ATOP;
        src.setColorFilter(tint, mode);
        return src;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


}
