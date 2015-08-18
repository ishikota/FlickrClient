package com.ikota.flickrclient.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.ikota.flickrclient.R;
import com.ikota.flickrclient.model.FlickerListItem;
import com.ikota.flickrclient.model.FlickerPhotoInfo;
import com.ikota.flickrclient.network.volley.BaseApiCaller;
import com.ikota.flickrclient.network.volley.FlickerApiCaller;
import com.ikota.flickrclient.network.volley.MySingleton;
import com.ikota.flickrclient.util.NetUtils;

import org.json.JSONException;

/**
 * Created by kota on 2015/08/14.
 * Detail page fragment
 */
public class ImageDetailFragment extends Fragment {

    //private Context mAppContext;
    private DisplayMetrics mDisplayInfo = new DisplayMetrics();

    private ImageView mItemImage, mUserImage;
    private TextView mTitleText, mUserText, mDateText, mDescriptText;

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
        mTitleText = (TextView) root.findViewById(R.id.title);
        mUserImage = (ImageView) root.findViewById(R.id.user_icon);
        mUserText = (TextView) root.findViewById(R.id.user_name);
        mDateText = (TextView) root.findViewById(R.id.date_text);
        mDescriptText = (TextView) root.findViewById(R.id.description);

        // get content
        Gson gson = new Gson();
        String json = getArguments().getString(ImageDetailActivity.EXTRA_CONTENT);
        FlickerListItem content = gson.fromJson(json, FlickerListItem.class);

        setupContent(content);
        loadDetailInfo(content);

        return root;
    }

    private void setupContent(FlickerListItem content) {
        // set item information which already we know
        mTitleText.setText(content.title);
        String url = content.generatePhotoURL(NetUtils.isWifiConnected(getActivity()) ? "z" : "q");
        ImageLoader imageLoader = MySingleton.getInstance(getActivity()).getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap cached_image = response.getBitmap();
                if (cached_image != null) {
                    setCachedImage(cached_image);
                    adjustColorScheme(cached_image);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }

    private void setCachedImage(Bitmap image) {
        adjustViewHeight(mItemImage, mDisplayInfo.widthPixels,
                image.getWidth(), image.getHeight());
        mItemImage.setImageBitmap(image);
    }

    private void adjustViewHeight(View target, int disp_w, int img_w, int img_h) {
        if(img_w == 0 || img_h == 0) return;
        target.getLayoutParams().width = disp_w;
        target.getLayoutParams().height = (int) (disp_w * (double)img_h/img_w);
    }

    private void adjustColorScheme(Bitmap image) {
        Palette.from(image).generate(
                new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        Palette.Swatch vibrant = palette.getVibrantSwatch();
                        if (vibrant != null) {
                            mTitleText.setBackgroundColor(vibrant.getRgb());
                            mTitleText.setTextColor(vibrant.getTitleTextColor());
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                Window window = getActivity().getWindow();
//                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                                window.setStatusBarColor(vibrant.get());
//                            }
                        }
                    }
                }
        );
    }

    private void loadDetailInfo(FlickerListItem content) {

        FlickerApiCaller.getInstance().getDetailInfo(getActivity(), content.id, new BaseApiCaller.ApiListener() {
            @Override
            public void onPostExecute(String response) {
                try {
                    // check if this fragment attached to Activity
                    if (isAdded()) {
                        FlickerPhotoInfo info = new FlickerPhotoInfo(response);
                        setDetailInfo(info);
                    }
                } catch (JSONException e) {
                    // server error
                    if (isAdded()) {
                        Toast.makeText(
                                getActivity(),
                                getResources().getString(R.string.network_problem_message),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onErrorListener(VolleyError error) {
                error.printStackTrace();
            }
        });

    }

    /**
     * Read json data and set image
     * @param bean : photo info holder object
     * @throws org.json.JSONException : caused by unexpected server response
     */
    private void setDetailInfo(FlickerPhotoInfo bean) throws JSONException{
        // this is image
        String original_img_url = bean.generatePhotoURL("b");
        ImageLoader imageLoader = MySingleton.getInstance(getActivity()).getImageLoader();
        imageLoader.get(original_img_url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if(response.getBitmap()!=null) {
                    Bitmap bmp = response.getBitmap();
                    adjustViewHeight(mItemImage, mDisplayInfo.widthPixels, bmp.getWidth(), bmp.getHeight());
                    mItemImage.setImageBitmap(bmp);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
//        int comment_num = Integer.valueOf(bean.comments);
//        mLikeNum.setText(comment_num + " like");
//        mLikeNum.setTag(comment_num);
//        setTag(bean.tags);
        ImageLoader.ImageListener listener =
                ImageLoader.getImageListener(mUserImage,
                        R.drawable.loading_default, R.drawable.ic_image_broken);
        mUserImage.setTag(imageLoader.get(bean.generateOwnerIconURL(), listener));
        mUserText.setText(bean.owner.username);
        mDateText.setText(bean.dates);
        mDescriptText.setText(bean.description);
    }


}
