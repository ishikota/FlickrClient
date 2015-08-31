package com.ikota.flickrclient.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ikota.flickrclient.R;

/**
 * Created by kota on 2015/08/13.
 * This class does these chores.
 * - Inflates Toolbar if found in xml. ( So you must set id of toolbar as "R.id.toolbar_actionbar" )
 */
public class BaseActivity extends AppCompatActivity{
    protected Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AndroidApplication)getApplication())
                .getObjectGraph()
                .inject(getApplication());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // cleanup comment draft
        SharedPreferences com_pref = getSharedPreferences(CommentDialog.PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = com_pref.edit();
        editor.clear().apply();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            supportFinishAfterTransition();
        }

        return super.onOptionsItemSelected(item);
    }

    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            if (mActionBarToolbar != null) {
                mActionBarToolbar.setTitle("");
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }

}
