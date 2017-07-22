package com.adrenalinelife;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.adrenalinelife.custom.CustomActivity;
import com.adrenalinelife.utils.Log;

/**
 * The main Browser screen, launched for user to make the payment for the Events
 * which are not Free. This screen simply shows a Webview that load the payment
 * page url which allow user to make payment.
 */
@SuppressLint("SetJavaScriptEnabled")
public class Browser extends CustomActivity
{
	/** The web. */
	private WebView web;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web);

		getActionBar().setTitle("Discover Events");

		String extra = getIntent().getExtras().getString("url");
		Log.e("Intent = " + extra);

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

		web.loadUrl(extra);
		Log.e("URL = " + extra);
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
