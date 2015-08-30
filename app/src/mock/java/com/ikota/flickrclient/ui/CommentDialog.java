package com.ikota.flickrclient.ui;


import android.app.Dialog;
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
import android.widget.TextView;

import com.ikota.flickrclient.R;


public class CommentDialog extends DialogFragment {

    private static final String EXTRA_TITLE = "title";
    private OnFinishListener mCallback;

    public interface OnFinishListener {
        void onFinish(String comment);
    }

    public void setOnFinishListener(OnFinishListener listener) {
        mCallback = listener;
    }

    public static CommentDialog newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TITLE, title);
        CommentDialog dialog = new CommentDialog();
        dialog.setArguments(bundle);
        return dialog;
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

    public void setupViews(final Dialog dialog) {
        Bundle args = getArguments();
        TextView title = (TextView)dialog.findViewById(R.id.title);
        title.setText(args.getString(EXTRA_TITLE));
        dialog.findViewById(R.id.ic_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit = (EditText)dialog.findViewById(R.id.comment_edit);
                String comment = edit.getText().toString();
                mCallback.onFinish(comment);
                dismiss();
            }
        });
    }

}
