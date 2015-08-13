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

import com.android.volley.toolbox.ImageLoader;
import com.ikota.flickrclient.R;
import com.ikota.flickrclient.model.FlickerListItem;
import com.ikota.flickrclient.network.MySingleton;
import com.ikota.flickrclient.util.NetUtils;

import java.util.List;

/**
 * Created by kota on 2015/08/11.
 * This adapter is used in BaseImageListFragment
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private List<FlickerListItem> mDataSet;
    private final OnClickCallback mClickCallback;
    private final LayoutInflater mInflater;
    private final ImageLoader mImageLoader;
    private final int view_size;
    private boolean is_wifi_connected;

    public void setIfWifiConnected(boolean wifi_connected) {
        is_wifi_connected = wifi_connected;
    }

    public interface OnClickCallback {
        void onClick(View v, FlickerListItem data);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageview;

        public ViewHolder(View v) {
            super(v);
            imageview = (ImageView) v.findViewById(R.id.image);
        }
    }


    public ImageAdapter(Context context, List<FlickerListItem> myDataset,
                        OnClickCallback listener, int column) {
        mDataSet = myDataset;
        mClickCallback = listener;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageLoader = MySingleton.getInstance(context).getImageLoader();

        // init grid size (grid size = display_width/column_num).
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        view_size = size.x / column;

        // check network state
        is_wifi_connected = NetUtils.isWifiConnected(context);
    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
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
        return new ImageAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // set list position as tag
        holder.imageview.setTag(R.integer.list_pos_key, position);

        // if last load request is alive, cancel it.
        ImageLoader.ImageContainer imageContainer =
                (ImageLoader.ImageContainer) holder.imageview.getTag();
        if (imageContainer != null) {
            imageContainer.cancelRequest();
        }

        // change image quality by checking network state
        String quality_flg = is_wifi_connected ? "z": "q";

        FlickerListItem item = mDataSet.get(position);
        String url = item.generatePhotoURL(quality_flg);

        ImageLoader.ImageListener listener =
                ImageLoader.getImageListener(holder.imageview,
                        R.drawable.loading_default, R.drawable.ic_image_broken);
        holder.imageview.setTag(mImageLoader.get(url, listener));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


}
