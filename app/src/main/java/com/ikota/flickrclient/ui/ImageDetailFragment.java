package com.ikota.flickrclient.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.ikota.flickrclient.R;
import com.ikota.flickrclient.model.FlickerListItem;
import com.ikota.flickrclient.network.MySingleton;
import com.ikota.flickrclient.util.NetUtils;

/**
 * Created by kota on 2015/08/14.
 * Detail page fragment
 */
public class ImageDetailFragment extends Fragment {

    //private Context mAppContext;
    private DisplayMetrics mDisplayInfo = new DisplayMetrics();

    private ImageView mItemImage;

    public static ImageDetailFragment newInstance(String json) {
        ImageDetailFragment fragment = new ImageDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ImageDetailActivity.EXTRA_CONTENT, json);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //mAppContext = activity.getApplicationContext();
        // save display sizes in mDisplayInfo
        activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayInfo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_image_detail, container, false);
        mItemImage = (ImageView) root.findViewById(R.id.image);

        // get content
        Gson gson = new Gson();
        String json = getArguments().getString(ImageDetailActivity.EXTRA_CONTENT);
        FlickerListItem content = gson.fromJson(json, FlickerListItem.class);
        setupContent(content);

        return root;
    }

    private void setupContent(FlickerListItem content) {
        // set item information which already we know
        String url = content.generatePhotoURL(NetUtils.isWifiConnected(getActivity()) ? "z":"q");
        ImageLoader imageLoader = MySingleton.getInstance(getActivity()).getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap cached_image = response.getBitmap();
                if (cached_image != null) {
                    adjustViewHeight(mItemImage, mDisplayInfo.widthPixels,
                            cached_image.getWidth(), cached_image.getHeight());
                    mItemImage.setBackgroundDrawable(new BitmapDrawable(cached_image));
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }

    private void adjustViewHeight(View target, int disp_w, int img_w, int img_h) {
        if(img_w == 0 || img_h == 0) return;
        target.getLayoutParams().width = disp_w;
        target.getLayoutParams().height = (int) (disp_w * (double)img_h/img_w);
    }


}
