package com.ikota.flickrclient.di;


import com.ikota.flickrclient.data.model.ListData;
import com.ikota.flickrclient.ui.AndroidApplication;
import com.ikota.flickrclient.ui.ImageListFragment;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit.Callback;

@Module(
        injects = ImageListFragment.class,
        library = true
)
public class LoadPopularListModule {

    @Provides
    @Named("portrait col")
    public int providePortraitColumn() {
        return 2;
    }

    @Provides @Named("horizontal col")
    public int provideHorizontalColumn() {
        return 3;
    }

    @Provides @Named("item per page")
    public int provideItemPerPage() {
        return 24;
    }

    @Provides
    public boolean provideAddPadding() {
        return true;
    }

    @Provides
    public LoadMethod provideLoadMethod() {
        return new LoadMethod() {
            @Override
            public void loadItem(AndroidApplication app, int page, Callback<ListData> cb) {
                    app.api().getPopularPhotos(page, cb);
            }
        };
    }
}
