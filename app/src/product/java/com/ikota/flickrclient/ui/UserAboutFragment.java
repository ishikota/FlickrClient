package com.ikota.flickrclient.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.PeopleInfo;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserAboutFragment extends Fragment implements UserTabImpl {

    private static final String EXTRA_USER_ID = "user_id";
    protected Context mAppContext;

    protected RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<PeopleInfo> mItemList;

    protected ProgressBar mProgress;
    private View mEmptyView;

    protected boolean end_flg;
    protected AtomicBoolean busy = new AtomicBoolean(false);

    private RecyclerView.OnScrollListener getScrollListener() {
        return new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // notify scroll event to host Activity
                UserActivity.sTabEventBus.post(new UserActivity
                        .ScrollEvent(mRecyclerView.getChildAt(0).getTop(), dy));
            }
        };
    }

    public static UserAboutFragment newInstance(String user_id) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_USER_ID, user_id);
        UserAboutFragment fragment = new UserAboutFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAppContext = getActivity().getApplicationContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_about, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(android.R.id.list);
        mEmptyView = root.findViewById(android.R.id.empty);
        mProgress = (ProgressBar) root.findViewById(R.id.progress);

        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(mAppContext, 1);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(getScrollListener());

        initList();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        // onResume() is called even when this fragment is hidden.
        // So prevent to call nofifyListState() if this fragment is hidden.
        if(mRecyclerView.getChildAt(0)!=null && isVisible()) notifyListState();
    }

    private void initList() {
        // this condition is occur when this fragment is re-created.
        // (like created by PagerAdapter again, orientation change occurred)
        if (mItemList != null && !mItemList.isEmpty()) {
            mRecyclerView.setAdapter(mAdapter);
            return;
        }

        mItemList = new ArrayList<>();
        if(isAdded()) updateList();
    }

    void updateList() {
        busy.set(true);
        mProgress.setVisibility(View.VISIBLE);
        String user_id = getArguments().getString(EXTRA_USER_ID);
        ((AndroidApplication)getActivity().getApplication()).api().getPeopleInfo(user_id, new Callback<PeopleInfo>() {
            @Override
            public void success(PeopleInfo item, Response response) {
                if (!isAdded()) return;
                mItemList.add(item);
                mEmptyView.setVisibility(mItemList.isEmpty() ? View.VISIBLE : View.GONE);

                mAdapter = new UserAboutAdapter(mAppContext, mItemList, new UserAboutAdapter.OnClickCallback() {
                    @Override
                    public void onFlickrClicked(View view, String url) {
                        WebViewActivity.launch(getActivity(), "Flickr Client", url);
                    }

                    @Override
                    public void onLocationClicked(View view, String location) {

                    }
                });
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.addOnScrollListener(getScrollListener());

                busy.set(false);
                mProgress.setVisibility(View.GONE);
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isAdded()) return;
                error.printStackTrace();
                mEmptyView.setVisibility(View.VISIBLE);
                String message = getResources().getString(R.string.network_problem_message);
                Toast.makeText(mAppContext, message, Toast.LENGTH_SHORT).show();
                mProgress.setVisibility(View.GONE);
                end_flg = true;
                busy.set(false);
            }
        });
    }

    @Override
    public void notifyListState() {
        if(mRecyclerView.getChildAt(0) != null) {
            UserActivity.sTabEventBus.post(new UserActivity.ScrollEvent(mRecyclerView.getChildAt(0).getTop(), 1));
        } else {  // before list item is loaded
            int head = (int)getResources().getDimension(R.dimen.user_header_padding);
            UserActivity.sTabEventBus.post(new UserActivity.ScrollEvent(head, 1));
        }
    }

    public void scrollList(final int y) {
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.scrollBy(0, y);
            }
        }, 30);  // delay is needed to scroll when display new tab
    }

}
