package com.ikota.flickrclient.ui;

import android.content.Context;

import com.ikota.flickrclient.network.BaseApiCaller;
import com.ikota.flickrclient.network.FlickerApiCaller;


/**
 * Created by kota on 2015/08/11.
 * ImageFragment which displays popular images.
 */
public class PopularListFragment extends BaseImageListFragment {

    @Override
    public void loadByContentType(Context context, int page, BaseApiCaller.ApiListener callback) {
        FlickerApiCaller.getInstance().getImageList(context, page, callback);
    }

    @Override
    public int getColumnNum() {
        return 2;
    }

    @Override
    public boolean addPadding() {
        return false;
    }

    @Override
    public int getItemPerPage() {
        return 20;
    }
}
