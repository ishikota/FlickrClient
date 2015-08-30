package com.ikota.flickrclient.ui;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ikota.flickrclient.R;

public class WebViewActivity extends BaseActivity{

    public static void launch(Activity activity, String title, String url) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_URL, url);
        activity.startActivity(intent);
    }

    private static final String EXTRA_TITLE = "title";
    private static final String EXTRA_URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();
        String title = intent.getStringExtra(EXTRA_TITLE);
        String url = intent.getStringExtra(EXTRA_URL);

        mActionBarToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mActionBarToolbar.setTitle(title);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.loading));
        dialog.show();

        WebView webView = (WebView)findViewById(R.id.web);
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                dialog.dismiss();
            }
        });
        webView.loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                //overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
