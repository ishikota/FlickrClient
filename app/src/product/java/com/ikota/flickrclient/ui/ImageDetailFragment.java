package com.ikota.flickrclient.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.ListData;
import com.ikota.flickrclient.data.model.PhotoInfo;
import com.ikota.flickrclient.util.NetUtils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ImageDetailFragment extends Fragment {

    public static ImageDetailFragment newInstance(String json, String cache_size) {
        ImageDetailFragment fragment = new ImageDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ImageDetailActivity.EXTRA_CONTENT, json);
        bundle.putString(ImageDetailActivity.EXTRA_CACHE_SIZE, cache_size);
        fragment.setArguments(bundle);
        return fragment;
    }

    private AndroidApplication mAppContext;
    private ArrayList<ListData.Photo> mItemList = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;

    private String mRelatedTag;
    private boolean busy = false;
    private final int ITEM_PER_PAGE = 24;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAppContext = (AndroidApplication)activity.getApplication();
    }

    @Override
    public void onResume() {
        super.onResume();
        ImageDetailActivity.sTabEventBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ImageDetailActivity.sTabEventBus.unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // setup content
        Gson gson = new Gson();
        String json = getArguments().getString(ImageDetailActivity.EXTRA_CONTENT);
        String size = getArguments().getString(ImageDetailActivity.EXTRA_CACHE_SIZE);
        ListData.Photo mData = gson.fromJson(json, ListData.Photo.class);

        // list related
        if(mAdapter == null) {
            mAdapter = new ImageDetailAdapter(mAppContext, mItemList, mData, size,
                    new ImageDetailAdapter.OnClickCallback() {
                        @Override
                        public void onClick(View v, ListData.Photo data) {
                            boolean is_wifi = NetUtils.isWifiConnected(getActivity());
                            String size = ListData.Photo.getProperSize(mAppContext.SCREEN_WIDTH / 2, is_wifi);
                            ImageDetailActivity.launch(getActivity(), data, (ImageView) v, size);
                        }

                        @Override
                        public void onUserClicked(View v, PhotoInfo.Owner owner) {
                            Intent intent = UserActivity.createIntent(getActivity(), owner);
                            startActivity(intent);
                        }

                        @Override
                        public void onCommentClicked(String url, String title, String json) {
                            startActivity(CommentListActivity.createIntent(mAppContext, url, title, json));
                        }
                    }
            );
        }

        int column_num = getProperColumnNum(getResources());
        GridLayoutManager manager = new GridLayoutManager(mAppContext, column_num);
        manager = addSpanSizeLookup(mAppContext, manager,mAdapter);

        View root = inflater.inflate(R.layout.fragment_image_detail, container, false);
        RecyclerView mRecyclerView = (RecyclerView) root.findViewById(android.R.id.list);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // load next related images page
                GridLayoutManager layoutManager = (GridLayoutManager)recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount()-1;
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (!busy && totalItemCount - firstVisibleItem <= ITEM_PER_PAGE) {
                    int page = totalItemCount / ITEM_PER_PAGE;
                    if(isAdded()) loadRelatedImages(page);
                }

                // change actionbar alpha
                ImageDetailActivity.sTabEventBus.post(new ImageDetailActivity.ScrollEvent(dy));
            }
        });

        return root;
    }

    private int getProperColumnNum(Resources r) {
        int display_mode = r.getConfiguration().orientation;
        return display_mode == Configuration.ORIENTATION_PORTRAIT ? 2 : 3;
    }

    private GridLayoutManager addSpanSizeLookup(
            Context context, GridLayoutManager original, final RecyclerView.Adapter adapter) {

        GridLayoutManager manager = new GridLayoutManager(context, original.getSpanCount());
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case ImageDetailAdapter.TYPE_HEADER:
                        return getProperColumnNum(getResources());
                    case ImageDetailAdapter.TYPE_ITEM:
                        return 1;
                    default:
                        return -1;
                }
            }
        });
        return manager;
    }

    private void loadRelatedImages(int page) {
        if(mRelatedTag == null) return;
        // start loading
        busy = true;
        mAppContext.api().getPhotosByTag(page, ITEM_PER_PAGE, mRelatedTag, new Callback<ListData>() {
            @Override
            public void success(ListData listData, Response response) {
                for(ListData.Photo photo : listData.photos.photo) {
                    mItemList.add(photo);
                }
                busy = false;
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Subscribe
    public void receiveRelatedImageData(RelatedTagEvent e) {
        mRelatedTag = e.tag;
        loadRelatedImages(0);
    }

    static final class RelatedTagEvent {
        final String tag;
        public RelatedTagEvent(String tag) {
            this.tag = tag;
        }
    }

}
