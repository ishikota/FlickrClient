package com.ikota.flickrclient.di;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.ikota.flickrclient.data.model.HotTagList;
import com.ikota.flickrclient.data.model.ListData;
import com.ikota.flickrclient.ui.AndroidApplication;
import com.ikota.flickrclient.ui.ImageAdapter;
import com.ikota.flickrclient.ui.SearchAdapter;
import com.ikota.flickrclient.ui.SearchFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit.Callback;

@Module(
        injects = SearchFragment.class,
        library = true
)
public class SearchModule {
    public static final int PORTRAIT_COL = 1;
    public static final int HORIZONTAL_COL = 1;
    public static final int ITEM_PER_PAGE = 25;

    public final List<HotTagList.Tag> dataset;

    public SearchModule(List<HotTagList.Tag> dataset) {
        this.dataset = dataset;
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
        return true;
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

    @Provides
    public ListAdapterGenerator provideAdapterGenerator() {
        return new ListAdapterGenerator() {
            @Override
            public RecyclerView.Adapter generateAdapter(
                    Context context,
                    ArrayList<ListData.Photo> data,
                    ImageAdapter.OnClickCallback callback) {
                return new SearchAdapter(context, dataset, callback);
            }
        };
    }
}
