package com.adrenalinelife;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.adrenalinelife.DiscoverEvents;

import com.adrenalinelife.custom.CustomActivity;
import com.adrenalinelife.utils.Log;


@SuppressLint("SetJavaScriptEnabled")
public class DiscoverBrowser extends CustomActivity {

    /** The web. */
    private WebView web;


    /* (non-Javadoc)
     * @see com.food.custom.CustomActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);

        getActionBar().setTitle(R.string.book_ticket);

        String urlYoga = "http://www.AdrenalineLife.org/events/categories/yoga/";
        Bundle extra = getIntent().getExtras();
        String Name;

        final ProgressBar pBar = (ProgressBar) findViewById(R.id.progress);
        web = (WebView) findViewById(R.id.web);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setDomStorageEnabled(true);
        web.getSettings().setSupportZoom(true);
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        web.getSettings().setSupportMultipleWindows(true);
        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                super.onProgressChanged(view, newProgress);
                pBar.setProgress(newProgress);
                pBar.setVisibility(newProgress == 100 ? View.GONE
                        : View.VISIBLE);
            }

        });


        if (extra != null) {
            Name = extra.getString("name");
            // and get whatever type user account id is
            if (Name == "yoga")
                web.loadUrl(urlYoga);
            else
                web.loadUrl("http://www.google.com");
        }



    }

    @Override
    public void onBackPressed()
    {
        if (web.canGoBack())
            web.goBack();
        else
            super.onBackPressed();
    }
}