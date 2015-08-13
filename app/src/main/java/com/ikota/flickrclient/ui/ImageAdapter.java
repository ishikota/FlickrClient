package com.ikota.flickrclient.ui;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
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
public class ImageAdapter extends ArrayAdapter<FlickerListItem> {
    private final LayoutInflater mInflater;
    private final ImageLoader mImageLoader;
    private final int view_size;
    private boolean is_wifi_connected;

    public void setIfWifiConnected(boolean wifi_connected) {
        is_wifi_connected = wifi_connected;
    }


    public ImageAdapter(Context context, List<FlickerListItem> objects, int column) {
        super(context, 0, objects);
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
    public View getView(int position, View convertView,
                        ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_image_list, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            // init image view size
            ViewGroup.LayoutParams params = holder.imageView.getLayoutParams();
            params.height = view_size;
            holder.imageView.setLayoutParams(params);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // if last load request is alive, cancel it.
        ImageLoader.ImageContainer imageContainer =
                (ImageLoader.ImageContainer) holder.imageView.getTag();
        if (imageContainer != null) {
            imageContainer.cancelRequest();
        }

        // load image with ImageLoader
        FlickerListItem item = getItem(position);

        // change image quality by checking network state
        String quality_flg = is_wifi_connected ? "z": "q";
        String url = item.generatePhotoURL(quality_flg);

        ImageLoader.ImageListener listener =
                ImageLoader.getImageListener(holder.imageView,
                        R.drawable.loading_default, R.drawable.ic_image_broken);
        holder.imageView.setTag(mImageLoader.get(url, listener));

        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
    }
}
