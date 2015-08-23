package com.ikota.flickrclient.di;


import com.ikota.flickrclient.data.model.ListData;
import com.ikota.flickrclient.ui.AndroidApplication;

import retrofit.Callback;

public interface LoadMethod {
    void loadItem(AndroidApplication app, int page, Callback<ListData> cb);
}
