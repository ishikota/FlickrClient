package com.ikota.flickrclient.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.CommentList;
import com.ikota.flickrclient.data.model.ListData;
import com.ikota.flickrclient.data.model.PhotoInfo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ImageDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public interface OnClickCallback extends ImageAdapter.OnClickCallback{
        void onUserClicked(View v, PhotoInfo.Owner owner);
        void onCommentClicked(String url, String title, String json);
    }

    private AndroidApplication mAppContext;
    private List<ListData.Photo> mDataset;
    private ListData.Photo mSimpleData;
    private PhotoInfo mDetailData;
    private String mCacheSize;
    private LayoutInflater mInflater;
    private OnClickCallback mCallback;

    static final int TYPE_HEADER = 0;
    static final int TYPE_ITEM = 1;

    public ImageDetailAdapter(AndroidApplication app, List<ListData.Photo> myDataset, ListData.Photo data, String cache_size,
                              OnClickCallback listener) {
        mAppContext = app;
        mDataset = myDataset;
        mSimpleData = data;
        mCacheSize = cache_size;
        mInflater = (LayoutInflater) mAppContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCallback = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case TYPE_HEADER:
                v = mInflater.inflate(R.layout.row_image_detail, parent, false);
                return new ContentViewHolder(v);
            case TYPE_ITEM:
                v = mInflater.inflate(R.layout.row_image_list, parent, false);
                return new ImageAdapter.ViewHolder(v);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position == 0) {
            ContentViewHolder vh = (ContentViewHolder)holder;
            if(mDetailData==null) {
                setSimpleData(vh, mSimpleData);
                loadComments(vh, mSimpleData.id);
                loadDetailData(vh);
            } else {
                setDetailData(vh, mDetailData);
            }
        } else {
            ImageAdapter.ViewHolder vh = (ImageAdapter.ViewHolder)holder;
            vh.imageview.setTag(R.integer.list_pos_key, position-1);

            ListData.Photo item = mDataset.get(position-1);
            String url = item.generatePhotoURL(vh.imageview.getWidth(), false);
            Picasso.with(mAppContext)
                    .load(url)
                    .placeholder(R.drawable.loading_default)
                    .error(R.drawable.ic_image_broken)
                    .into(vh.imageview);
            vh.imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (Integer) v.getTag(R.integer.list_pos_key);
                    if (mCallback != null) {
                        mCallback.onClick(v, mDataset.get(position));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.isEmpty() ? 1 : mDataset.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    private void setSimpleData(final ContentViewHolder vh, final ListData.Photo data) {
        // load cached image and change title color
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bmp, Picasso.LoadedFrom from) {
                adjustViewHeight(vh.image_main, getScreenWidth(), bmp.getWidth(), bmp.getHeight());
                vh.image_main.setImageBitmap(bmp);
                vh.text_title.setText(data.title);
                adjustColorScheme(vh.text_title, bmp);
            }

            @Override public void onBitmapFailed(Drawable errorDrawable) {}

            @Override public void onPrepareLoad(Drawable placeHolderDrawable) {}
        };
        String url = data.generatePhotoURL(mCacheSize);
        Picasso.with(mAppContext).load(url).into(target);
        vh.text_title.setText(data.title);
    }

    private int getScreenWidth() {
        int display_mode = mAppContext.getResources().getConfiguration().orientation;
        if(display_mode == Configuration.ORIENTATION_PORTRAIT) {
            return mAppContext.SCREEN_WIDTH;
        } else {
            int statusbar_height = (int) mAppContext.getResources().getDimension(R.dimen.status_bar_height);
            return mAppContext.SCREEN_HEIGHT + statusbar_height;
        }
    }

    private void adjustViewHeight(View target, int disp_w, int img_w, int img_h) {
        if(img_w == 0 || img_h == 0) return;
        target.getLayoutParams().width = disp_w;
        target.getLayoutParams().height = (int) (disp_w * (double)img_h/img_w);
    }

    private void adjustColorScheme(final TextView title, Bitmap image) {
        Palette.PaletteAsyncListener listener = new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();
                if (vibrant != null) {
                    title.setBackgroundColor(vibrant.getRgb());
                    title.setTextColor(vibrant.getTitleTextColor());
                }
                Palette.Swatch muted = palette.getMutedSwatch();
                if(muted!=null) {
                    ImageDetailActivity.sTabEventBus
                            .post(new ImageDetailActivity.PaletteEvent(muted.getRgb()));
                }
            }
        };
        Palette.from(image).generate(listener);
    }

    private void loadDetailData(final ContentViewHolder vh) {
        mAppContext.api().getPhotoInfo(mSimpleData.id, new Callback<PhotoInfo>() {

            @Override
            public void success(PhotoInfo photoInfo, Response response) {
                mDetailData = photoInfo;
                setDetailData(vh, mDetailData);
            }

            @Override
            public void failure(RetrofitError error) {
                String message = mAppContext.getResources().getString(R.string.network_problem_message);
                Toast.makeText(mAppContext, message, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void setDetailData(final ContentViewHolder vh, final PhotoInfo data) {
        // load item and user images
        Target target = new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bmp, Picasso.LoadedFrom from) {
                adjustViewHeight(vh.image_main, getScreenWidth(), bmp.getWidth(), bmp.getHeight());
                vh.image_main.setImageBitmap(bmp);  // prevent load animation again
            }

            @Override public void onBitmapFailed(Drawable errorDrawable) {}

            @Override public void onPrepareLoad(Drawable placeHolderDrawable) {}

        };
        Picasso.with(mAppContext).load(data.photo.generatePhotoURL("b")).into(target);
        Picasso.with(mAppContext).load(data.photo.owner.generateOwnerIconURL()).into(vh.image_user);

        // set text data
        vh.text_title.setText(data.photo.title._content);
        vh.text_date.setText("Posted on " + data.photo.dates.taken);
        vh.text_description.setText(data.photo.description._content);
        vh.text_username.setText(data.photo.owner.username);
        vh.image_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onUserClicked(view, data.photo.owner);
            }
        });
        if(!data.photo.description._content.isEmpty()) {
            vh.ic_ellip.setVisibility(View.VISIBLE);
            vh.description_parent.setTag(false);  // if expanded
            vh.description_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean expanded = (boolean) view.getTag();
                    view.setTag(!expanded);
                    vh.text_description.setVisibility(expanded ? View.GONE : View.VISIBLE);
                    vh.ic_ellip.setImageResource(expanded ?
                            R.drawable.ic_arrow_drop_down_black_18dp : R.drawable.ic_arrow_drop_up_black_18dp);
                }
            });
        }

        // notify related tag to Fragment and start loading images
        if(data.photo.tags.tag.isEmpty()) {
            vh.text_related.setText(mAppContext.getResources().getString(R.string.no_related_images));
            vh.related_progress.setVisibility(View.GONE);
        } else {
            String tag = data.photo.tags.tag.get(0)._content;
            ProgressBar progress = vh.related_progress;
            ImageDetailActivity.sTabEventBus.post(new ImageDetailFragment.RelatedTagEvent(tag, progress));
        }
    }

    private void loadComments(final ContentViewHolder vh, String photo_id) {
        mAppContext.api().getCommentList(photo_id, new Callback<CommentList>() {

            @Override
            public void success(final CommentList commentList, Response response) {
                if(commentList.comments.comment == null) return;  // no-comment case
                int size = commentList.comments.comment.size();
                if (size > 0) {
                    vh.comment_parent.removeAllViews();  // remove no-comment view
                    for (int i = 0; i < Math.min(3, size); i++) {
                        CommentList.Comment comment = commentList.comments.comment.get(i);
                        View child = createCommentView(vh.comment_parent, comment);
                        vh.comment_parent.addView(child);
                    }
                }
                if (size > 3) {
                    View v = createSummaryView(vh.comment_parent, size);
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Gson gson = new Gson();
                            String url = mSimpleData.generatePhotoURL(mCacheSize);
                            String title = mSimpleData.title;
                            String json = gson.toJson(commentList);
                            mCallback.onCommentClicked(url, title, json);
                        }
                    });
                    vh.comment_parent.addView(v);
                }
            }

            @Override public void failure(RetrofitError error) {}

        });
    }

    private View createCommentView(LinearLayout parent, final CommentList.Comment comment) {
        final View v = mInflater.inflate(R.layout.comment_row, parent, false);

        ((TextView) v.findViewById(R.id.comment_text)).setText(comment._content);

        Picasso.with(mAppContext)
                .load(generateOwnerIconURL(comment.author))
                .into((ImageView) v.findViewById(R.id.user_icon));

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoInfo.Owner owner = new PhotoInfo.Owner();
                owner.nsid = comment.author;
                owner.iconserver = comment.iconserver;
                owner.iconfarm = String.valueOf(comment.iconfarm);
                owner.username = comment.authorname;
                mCallback.onUserClicked(view, owner);
            }
        });

        return v;
    }

    private View createSummaryView(LinearLayout parent, int size) {
        Resources r = mAppContext.getResources();
        View v = mInflater.inflate(R.layout.comment_row, parent, false);
        ImageView icon = (ImageView)v.findViewById(R.id.user_icon);
        icon.setImageResource(R.drawable.ic_trending_flat_black_18dp);
        icon.setAlpha(0.5f);
        String summary = r.getString(R.string.comment_summary_prefix)+" "+size+" "+r.getString(R.string.comment_summary_suffix);
        ((TextView)v.findViewById(R.id.comment_text)).setText(summary);
        return v;
    }

    private String generateOwnerIconURL(String nsid) {
        return "https://flickr.com/buddyicons/"+nsid+".jpg";
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_main, image_user, ic_ellip;
        public TextView text_title, text_date, text_description, text_username, text_related;
        public LinearLayout description_parent, comment_parent;
        public ProgressBar related_progress;
        public ContentViewHolder(View v) {
            super(v);
            image_main = (ImageView)v.findViewById(R.id.image);
            image_user = (ImageView)v.findViewById(R.id.user_icon);
            ic_ellip = (ImageView)v.findViewById(R.id.ic_ellipsize);
            text_title = (TextView)v.findViewById(R.id.title);
            text_date = (TextView)v.findViewById(R.id.date_text);
            text_description = (TextView)v.findViewById(R.id.description);
            text_username = (TextView)v.findViewById(R.id.user_name);
            text_related = (TextView)v.findViewById(R.id.related_title);
            description_parent = (LinearLayout)v.findViewById(R.id.description_parent);
            comment_parent = (LinearLayout)v.findViewById(R.id.comment_parent);
            related_progress = (ProgressBar)v.findViewById(R.id.related_progress);
        }
    }

}
