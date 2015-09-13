package com.ikota.flickrclient.ui;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.ikota.flickrclient.data.model.ListData;

public class SearchFragment extends ImageListFragment {

    public interface OnSearchClickCallback extends ImageAdapter.OnClickCallback {
        void onTagClicked(String title);
    }

    @Override
    protected ImageAdapter.OnClickCallback getItemClickListener() {
        final ImageAdapter.OnClickCallback super_callback = super.getItemClickListener();
        return new OnSearchClickCallback() {
            @Override
            public void onTagClicked(String title) {
                Toast.makeText(getActivity(), title, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClick(View v, ListData.Photo data) {
                super_callback.onClick(v, data);
            }
        };
    }

    @Override
    protected RecyclerView.OnScrollListener getScrollListener() {
        return new RecyclerView.OnScrollListener() {};
    }

}
