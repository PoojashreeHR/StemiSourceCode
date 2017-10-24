package com.stemi.stemiapp.activity;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.stemi.stemiapp.R;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

       String sHtmlTemplate = "<html><head></head><body><center><img src=\"file:///android_asset/bmi_chart.png\" align=\"middle\"></center></body></html>";
        WebView myWebView = (WebView) findViewById(R.id.webView);
        myWebView.setVerticalScrollBarEnabled(false);
        myWebView.setVisibility(View.VISIBLE);
        myWebView.requestFocusFromTouch();
        myWebView.setInitialScale(50);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setDisplayZoomControls(false);
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setSupportZoom(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
      //  wb.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.bmi_chart));
        myWebView.loadUrl("file:///android_asset/index.html");
       // myWebView.loadDataWithBaseURL(null, sHtmlTemplate, "text/html", "utf-8",null);
    }
}
