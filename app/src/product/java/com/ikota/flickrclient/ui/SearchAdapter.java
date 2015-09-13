package com.ikota.flickrclient.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.HotTagList;

import java.util.List;


public class SearchAdapter
        extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements ListAdapterImpl {

    private final LayoutInflater mInflater;
    private final ImageAdapter.OnClickCallback mClickCallback;
    private final List<HotTagList.Tag> mData;

    public SearchAdapter(Context context, List<HotTagList.Tag> data, ImageAdapter.OnClickCallback listener) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mClickCallback = listener;
        mData = data;
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.search_category_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        vh.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickCallback != null) {
                    String tag = ((TextView)v).getText().toString();
                    ((SearchFragment.OnSearchClickCallback)mClickCallback).onTagClicked(tag);
                }
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(mData.get(position)._content);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void setIfWifiConnected(boolean wifi_connected) {

    }

    @Override
    public void setViewSize(int size) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View v) {
            super(v);
            textView = (TextView)v.findViewById(R.id.text);
        }
    }

}
