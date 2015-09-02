package com.ikota.flickrclient.ui;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.CommentList;

import java.util.List;
import java.util.Random;

/**
 * Created by kota on 2015/09/02.
 */
public class CommentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<CommentList.Comment> mDataSet;
    private final ImageAdapter.OnClickCallback mClickCallback;
    private final LayoutInflater mInflater;
    private final Resources r;
    private final Random rand = new Random();
    private final int[] com = {R.string.comment1, R.string.comment2, R.string.title, R.string.description};

    public CommentListAdapter(Context context, List<CommentList.Comment> myDataset,
                              ImageAdapter.OnClickCallback listener) {
        mDataSet = myDataset;
        mClickCallback = listener;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        r = context.getResources();
    }

    @Override
    public int getItemViewType(int position) {
        return Math.min(1,position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 0) {
            View v = mInflater.inflate(R.layout.dialog_comment, parent, false);
            HeaderVH vh = new HeaderVH(v);
            vh.edit_parent.setVisibility(View.GONE);
            vh.pre_title.setTextColor(r.getColor(android.R.color.secondary_text_light));
            vh.textView.setTextColor(r.getColor(android.R.color.secondary_text_light));
            return vh;
        } else {
            View v = mInflater.inflate(R.layout.row_comment, parent, false);
            return new CommentVH(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position == 0) {
            ((HeaderVH)holder).textView.setText(r.getString(R.string.title));
        } else {
            String mes = r.getString(com[rand.nextInt(4)]);
            ((CommentVH)holder).textView.setText(mes);
        }

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public static class HeaderVH extends RecyclerView.ViewHolder {
        public ImageView imageview;
        public TextView textView, pre_title;
        public View edit_parent;

        public HeaderVH(View v) {
            super(v);
            imageview = (ImageView) v.findViewById(R.id.image);
            textView = (TextView) v.findViewById(R.id.title);
            edit_parent = v.findViewById(R.id.edit_parent);
            pre_title = (TextView)v.findViewById(R.id.title_prefix);
        }
    }

    public static class CommentVH extends RecyclerView.ViewHolder {
        public ImageView imageview;
        public TextView textView;

        public CommentVH(View v) {
            super(v);
            imageview = (ImageView) v.findViewById(R.id.user_icon);
            textView = (TextView) v.findViewById(R.id.comment);
        }
    }
}
