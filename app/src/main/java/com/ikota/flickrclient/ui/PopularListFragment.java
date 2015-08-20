package com.ikota.flickrclient.ui;

import com.ikota.flickrclient.model.Interestingness;

import retrofit.Callback;


/**
 * Created by kota on 2015/08/11.
 * Start screen page.
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
