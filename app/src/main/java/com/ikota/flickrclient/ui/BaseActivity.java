package com.ikota.flickrclient.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ikota.flickrclient.R;

/**
 * Created by kota on 2015/08/13.
 * This class does these chores.
 * - Inflates Toolbar if found in xml. ( So you must set id of toolbar as "R.id.toolbar_actionbar" )
 */
public class BaseActivity extends AppCompatActivity{
    public static final String KEY_REAL_SERVER = "real_server";
    private Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AndroidApplication)getApplication())
                .getObjectGraph()
                .inject(getApplication());
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

    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }

}
