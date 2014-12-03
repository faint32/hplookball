/**
 * 
 */
package com.hupu.games.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hupu.games.R;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.handler.IWebViewClientEvent;
import com.hupu.games.view.HupuWebView;
import com.mato.sdk.proxy.Proxy;

/**
 * @author 
 */
@SuppressLint("NewApi")
public  class TaskActivity extends HupuBaseActivity implements
IWebViewClientEvent{

	private String url;
	private HupuWebView taskWebView;
	View progressbar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_task);
		taskWebView = (HupuWebView) findViewById(R.id.my_task_webview);
		Proxy.supportWebview(this);
		progressbar = findViewById(R.id.probar);
		
		taskWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
		taskWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		
		setOnClickListener(R.id.btn_back);
		taskWebView.loadUrl(getCoinUrl());
		
		taskWebView.setWebViewClientEventListener(this, false);
	}
	
	private String getCoinUrl(){
		url = HuPuRes.getUrl(HuPuRes.REQ_METHOD_TASK)+"?token=" + (mToken == null ? "0" : mToken) +"&client="+mDeviceId+"&id="+getIntent().getIntExtra("id", 0);
		return url;
	}
	
	
	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);

		switch (id) {
		case R.id.btn_back:
			finish();
			break;
		}
	}
	
	@Override
	public void onPageFinished(WebView view, String url) {
		progressbar.setVisibility(View.GONE);
	}

	String mUrl;

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url,
			boolean isScheme) {
		return true;
	}

	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		
	}

	@Override
	public void onReceivedTitle(WebView view, String title) {
		
	}
}
