package com.ikota.flickrclient.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.ListData;
import com.ikota.flickrclient.data.model.PhotoInfo;
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
        ImageDetailAdapter adapter = new ImageDetailAdapter(mAppContext, mItemList, mData, size,
                new ImageDetailAdapter.OnClickCallback() {
                    @Override
                    public void onUserClicked(View v, PhotoInfo.Owner owner) {
                        Intent intent = UserActivity.createIntent(getActivity(), owner);
                        startActivity(intent);
                    }

                    @Override
                    public void onCommentClicked(String url, String title, String json) {
                        startActivity(CommentListActivity.createIntent(mAppContext, url, title, json));
                    }}
        );
        GridLayoutManager manager = new GridLayoutManager(mAppContext, 2);
        manager = addSpanSizeLookup(mAppContext, manager,adapter);

        View root = inflater.inflate(R.layout.fragment_image_detail, container, false);
        RecyclerView mRecyclerView = (RecyclerView) root.findViewById(android.R.id.list);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ImageDetailActivity.sTabEventBus.post(new ImageDetailActivity.ScrollEvent(dy));
            }
        });

        return root;
    }

    private GridLayoutManager addSpanSizeLookup(
            Context context, GridLayoutManager original, final RecyclerView.Adapter adapter) {

        GridLayoutManager manager = new GridLayoutManager(context, original.getSpanCount());
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case ImageDetailAdapter.TYPE_HEADER:
                        return 2;
                    case ImageDetailAdapter.TYPE_ITEM:
                        return 1;
                    default:
                        return -1;
                }
            }
        });
        return manager;
    }

    @Subscribe
    public void receiveRelatedImageData(RelatedTagEvent e) {
        mAppContext.api().getPhotosByTag(0, 24, e.tag, new Callback<ListData>() {
            @Override
            public void success(ListData listData, Response response) {
                for(ListData.Photo photo : listData.photos.photo) {
                    mItemList.add(photo);
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    static final class RelatedTagEvent {
        final String tag;
        public RelatedTagEvent(String tag) {
            this.tag = tag;
        }
    }

}
