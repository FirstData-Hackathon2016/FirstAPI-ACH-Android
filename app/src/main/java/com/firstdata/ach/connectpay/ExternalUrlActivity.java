package com.firstdata.ach.connectpay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.firstdata.rashmi.rashmidemo.R;


public class ExternalUrlActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_url);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        setTitle("Loading...");

        WebView webview = (WebView) findViewById(R.id.externalUrlWebView);
        webview.getSettings().setJavaScriptEnabled(true);

        final Activity self  = this;

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                self.setTitle((view.getTitle()));
            }
        });

        webview.loadUrl(intent.getStringExtra("url"));

    }
}
