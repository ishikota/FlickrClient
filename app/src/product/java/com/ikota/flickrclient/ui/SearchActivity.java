package com.ikota.flickrclient.ui;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.ikota.flickrclient.R;
import com.ikota.flickrclient.data.model.HotTagList;
import com.ikota.flickrclient.di.SearchModule;

import dagger.ObjectGraph;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SearchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup toolbar
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_back_arrow);

        // find the retained fragment on activity restarts
        final FragmentManager fm = getSupportFragmentManager();
        SearchFragment fragment = (SearchFragment)
                fm.findFragmentByTag(SearchFragment.class.getSimpleName());

        if (fragment == null) {
            ((AndroidApplication)getApplication()).api().getHotTagList(new Callback<HotTagList>() {
                @Override
                public void success(HotTagList hotTagList, Response response) {
                    SearchFragment fragment = new SearchFragment();
                    ObjectGraph graph = ObjectGraph.create(new SearchModule(hotTagList.hottags.tag));
                    graph.inject(fragment);
                    String tag = SearchFragment.class.getSimpleName();
                    fm.beginTransaction().add(R.id.container, fragment, tag).commitAllowingStateLoss();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(SearchActivity.this,
                            getResources().getString(R.string.network_problem_message),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        // Get the SearchView and set the searchable configuration.
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = new SearchView(this);
        searchView.onActionViewExpanded();  // initially expand searchView
        EditText search_edit = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        search_edit.setTextColor(getResources().getColor(R.color.white_dull));
        search_edit.setHintTextColor(getResources().getColor(R.color.white_dull));
        MenuItemCompat.setShowAsAction(searchItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        MenuItemCompat.setActionView(searchItem, searchView);
        ComponentName componentName = new ComponentName(getPackageName(), SearchableActivity.class.getName());
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) {
                if(!query.isEmpty()) {
//                    Intent intent = new Intent(SearchActivity.this, TagListActivity.class);
//                    intent.putExtra(TagListActivity.EXTRA_TAG, query);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.slide_in_from_right, R.anim.fade_out_depth );
                    Log.i("SearchActivity", query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("menu_search view", "changed to " + newText);
                return true;
            }
        });

        return true;
    }


}
