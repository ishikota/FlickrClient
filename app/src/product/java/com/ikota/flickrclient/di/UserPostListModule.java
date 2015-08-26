package com.ikota.flickrclient.di;


import com.ikota.flickrclient.data.model.ListData;
import com.ikota.flickrclient.ui.AndroidApplication;
import com.ikota.flickrclient.ui.UserBaseFragment;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit.Callback;

@Module(
        injects = UserBaseFragment.class,
        library = true
)
public class UserPostListModule {
    public static final int PORTRAIT_COL = 3;
    public static final int HORIZONTAL_COL = 4;
    public static final int ITEM_PER_PAGE = 24;

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
                    app.api().getPopularPhotos(page, ITEM_PER_PAGE, cb);
            }
        };
    }
}
