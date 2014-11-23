package com.example.maksim.lesson6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

public class WebActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String description = getIntent().getStringExtra("description");
        setContentView(R.layout.web);
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.loadData(description, "text/html; charset=utf-8", null);
    }

    public void onClick(View v) {
        String url = getIntent().getStringExtra("url");
        setContentView(R.layout.web2);
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

}