package com.ikota.flickrclient.di;


import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.ikota.flickrclient.data.model.ListData;
import com.ikota.flickrclient.ui.AndroidApplication;
import com.ikota.flickrclient.ui.ImageAdapter;
import com.ikota.flickrclient.ui.UserTimelineAdapter;
import com.ikota.flickrclient.ui.UserTimelineFragment;

import java.util.ArrayList;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit.Callback;

@Module(
        injects = UserTimelineFragment.class,
        library = true
)
public class UserTimelineModule {
    public static final int PORTRAIT_COL = 1;
    public static final int HORIZONTAL_COL = 3;
    public static final int ITEM_PER_PAGE = 24;
    public final String USER_ID;

    public UserTimelineModule(String user_id) {
        this.USER_ID = user_id;
    }

    @Provides
    @Named("portrait col")
    public int providePortraitColumn() {
        return PORTRAIT_COL;
    }

    @Provides @Named("horizontal col")
    public int provideHorizontalColumn() {
        return HORIZONTAL_COL;
    }

    @Provides @Named("item per page")
    public int provideItemPerPage() {
        return ITEM_PER_PAGE;
    }

    @Provides
    public boolean provideAddPadding() {
        return false;
    }

    @Provides
    public LoadMethod provideLoadMethod() {
        return new LoadMethod() {
            @Override
            public void loadItem(AndroidApplication app, int page, Callback<ListData> cb) {
                    app.api().getPeopleFavorites(USER_ID, page, ITEM_PER_PAGE, cb);
            }
        };
    }

    @Provides
    public ListAdapterGenerator provideAdapterGenerator() {
        return new ListAdapterGenerator() {
            @Override
            public RecyclerView.Adapter generateAdapter(
                    Context context,
                    ArrayList<ListData.Photo> data,
                    ImageAdapter.OnClickCallback callback) {
                return new UserTimelineAdapter(context, data, callback);
            }
        };
    }

}
