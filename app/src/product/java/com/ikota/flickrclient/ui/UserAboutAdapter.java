package com.ikota.flickrclient.ui;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.PeopleInfo;

import java.util.List;

public class UserAboutAdapter extends RecyclerView.Adapter<UserAboutAdapter.ViewHolder>{

    private List<PeopleInfo> mDataSet;
    private final LayoutInflater mInflater;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View flickr_view;
        public TextView description, location;

        public ViewHolder(View v) {
            super(v);
            flickr_view = v.findViewById(R.id.flickr_info_parent);
            description = (TextView)v.findViewById(R.id.description);
            location = (TextView)v.findViewById(R.id.location);
        }
    }

    public UserAboutAdapter(Context context, List<PeopleInfo> myDataSet) {
        mDataSet = myDataSet;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = mInflater.inflate(R.layout.row_user_about, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PeopleInfo item = mDataSet.get(position);
        if(item.person.location == null || item.person.location._content.isEmpty()) {
            holder.location.setVisibility(View.GONE);
        } else {
            holder.location.setVisibility(View.VISIBLE);
            holder.location.setText(item.person.location._content);
        }
        if(item.person.description._content.isEmpty()) {
            holder.description.setVisibility(View.GONE);
        } else {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(item.person.description._content);
        }

        holder.flickr_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mContext.startActivity();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}
