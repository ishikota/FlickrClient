package com.ikota.flickrclient.ui;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


/**
 * Created by kota on 2014/12/26.
 * This class handles menu_search query from menu_search view.
 */
public class SearchableActivity extends ListActivity {
    private static final String TAG = SearchableActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i(TAG, "received query (" + query + ")");
        }
    }

}
