package com.ikota.flickrclient.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.CommentList;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CommentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final Context mContext;
    private final LayoutInflater mInflater;
    private final ImageAdapter.OnClickCallback mClickCallback;

    private final String URL, TITLE;
    private List<CommentList.Comment> mDataSet;

    public CommentListAdapter(Context context, ImageAdapter.OnClickCallback listener,
                              String url, String  title, List<CommentList.Comment> myDataset) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mClickCallback = listener;
        URL = url;
        TITLE = title;
        mDataSet = myDataset;
    }

    @Override
    public int getItemViewType(int position) {
        return Math.min(1,position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 0) {  // row of header
            View v = mInflater.inflate(R.layout.dialog_comment, parent, false);
            HeaderVH vh = new HeaderVH(v);
            vh.edit_parent.setVisibility(View.GONE);
            vh.textView.setText(TITLE);
            return vh;
        } else {  // row of comment
            View v = mInflater.inflate(R.layout.row_comment, parent, false);
            return new CommentVH(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position == 0) {
            HeaderVH vh = (HeaderVH)holder;
            Picasso.with(mContext).load(URL).into(vh.imageview);
        } else if(position != mDataSet.size()){
            CommentVH vh = (CommentVH)holder;
            vh.text_username.setText(mDataSet.get(position).authorname);
            vh.text_comment.setText(mDataSet.get(position)._content);
            Picasso.with(mContext).load(generateOwnerIconURL(mDataSet.get(position).author)).into(vh.imageview);
        }

    }

    private String generateOwnerIconURL(String nsid) {
        return "https://flickr.com/buddyicons/"+nsid+".jpg";
    }

    @Override
    public int getItemCount() {
        return mDataSet.size()+1;
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
        public TextView text_comment, text_username;

        public CommentVH(View v) {
            super(v);
            imageview = (ImageView) v.findViewById(R.id.user_icon);
            text_comment = (TextView) v.findViewById(R.id.comment);
            text_username = (TextView) v.findViewById(R.id.user_name);
        }
    }
}
