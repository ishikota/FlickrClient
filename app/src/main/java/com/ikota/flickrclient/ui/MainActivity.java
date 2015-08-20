package com.ikota.flickrclient.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ikota.flickrclient.R;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            String tag = PopularListFragment.class.getSimpleName();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PopularListFragment(), tag)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
