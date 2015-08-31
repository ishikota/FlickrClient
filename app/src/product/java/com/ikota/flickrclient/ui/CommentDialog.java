package com.ikota.flickrclient.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ikota.flickrclient.R;
import com.squareup.picasso.Picasso;


public class CommentDialog extends DialogFragment{

    static final String PREF_NAME = "CommentDialog";
    /** draft comment is saved with key comment_{item_id} */
    private static final String PREF_KEY_PREFIX = "comment_";

    private static final String EXTRA_ID = "id";
    private static final String EXTRA_TITLE = "title";
    private static final String EXTRA_IMAGE_URL = "url";

    private OnFinishListener mCallback;
    private String id;
    private EditText mEdit;

    public interface OnFinishListener {
        void onFinish(String comment);
    }

    public void setOnFinishListener(OnFinishListener listener) {
        mCallback = listener;
    }

    public static CommentDialog newInstance(String id, String title, String img_url) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ID, id);
        bundle.putString(EXTRA_TITLE, title);
        bundle.putString(EXTRA_IMAGE_URL, img_url);
        CommentDialog dialog = new CommentDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getString(EXTRA_ID);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.CommentDialog);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog.setContentView(R.layout.dialog_comment);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setupViews(dialog);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Dialog dialog = getDialog();
        String draft = getDraft(id);
        if(dialog!=null && draft!=null) {
            EditText edit = (EditText)dialog.findViewById(R.id.comment_edit);
            edit.setText(draft);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mEdit!=null) {
            String draft = mEdit.getText().toString();
            saveDraft(id, draft);
        }
    }

    public void setupViews(final Dialog dialog) {
        mEdit = (EditText)dialog.findViewById(R.id.comment_edit);
        ImageView image = (ImageView)dialog.findViewById(R.id.image);
        TextView title = (TextView)dialog.findViewById(R.id.title);
        Bundle args = getArguments();
        title.setText(args.getString(EXTRA_TITLE));

        if(isAdded()) {
            Picasso.with(getActivity())
                    .load(args.getString(EXTRA_IMAGE_URL))
                    .into(image);
        }

        dialog.findViewById(R.id.ic_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = mEdit.getText().toString();
                mEdit.setText("");
                mCallback.onFinish(comment);
                dismiss();
            }
        });
    }

    private String getDraft(String id) {
        if(getActivity()==null || id==null) return null;
        SharedPreferences prefs =
                getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(PREF_KEY_PREFIX + id, null);
    }

    private void saveDraft(String id, String draft) {
        if(getActivity()==null || id==null) return;
        SharedPreferences prefs =
                getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_KEY_PREFIX + id, draft);
        editor.apply();
    }
}
