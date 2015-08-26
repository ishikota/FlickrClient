package com.ikota.flickrclient.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ikota.flickrclient.R;


public class ImageDetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_image_detail, container, false);

        Resources r = getResources();
        ((TextView) root.findViewById(R.id.title)).setText(r.getString(R.string.title));
        ((TextView) root.findViewById(R.id.user_name)).setText(r.getString(R.string.username));
        ((TextView) root.findViewById(R.id.description)).setText(r.getString(R.string.description));
        ((TextView) root.findViewById(R.id.date_text)).setText(r.getString(R.string.date));

        root.findViewById(R.id.user_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

}
