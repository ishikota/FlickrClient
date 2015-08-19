package com.ikota.flickrclient.ui;

import com.ikota.flickrclient.model.Interestingness;

import retrofit.Callback;


/**
 * Created by kota on 2015/08/11.
 * ImageFragment which displays popular images.
 */
public class PopularListFragment extends BaseImageListFragment {

    @Override
    public void loadByContentType(int page, Callback<Interestingness> callback) {
        ((MainApplication)getActivity().getApplication()).api().getPopularPhotos(page, callback);
    }

    @Override
    public int getColumnNum() {
        return 2;
    }

    @Override
    public boolean addPadding() {
        return true;
    }

    @Override
    public int getItemPerPage() {
        return 20;
    }
}
