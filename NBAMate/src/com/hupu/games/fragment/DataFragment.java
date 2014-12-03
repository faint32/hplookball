package com.hupu.games.fragment;


import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hupu.games.R;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.activity.NBAPlayerInfoActivity;
import com.hupu.games.activity.WebViewActivity;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.HupuScheme;
import com.hupu.games.handler.IWebViewClientEvent;
import com.hupu.games.view.HupuWebView;
import com.mato.sdk.proxy.Proxy;

public class DataFragment extends BaseFragment implements IWebViewClientEvent{

	private HupuWebView mWebView;


	String url;
	public int mode;
	public String mTag;
	
	public void setUrl(String s)
	{
		url =s;
		HupuLog.d("ul="+s);
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTag = getArguments().getString("tag");
		mode = getArguments().getInt("mode");
	}


	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v =inflater.inflate(R.layout.fragment_data, null);
		mWebView=(HupuWebView)v.findViewById(R.id.web_content);
		mWebView.setWebViewClientEventListener(this);
		Proxy.supportWebview(getActivity());

		mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
		mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		mWebView.loadUrl(url);

		return v;
	}
	
	@Override
	public void onPageFinished(WebView view, String url) {
	}
	
	@Override
	public  boolean shouldOverrideUrlLoading(WebView view, String url,boolean isScheme) {
		if(isScheme)
			((HupuBaseActivity) getActivity()).sendUmeng(HuPuRes.UMENG_EVENT_NBAPLAYERS, HuPuRes.UMENG_KEY_ENTRANCE,HuPuRes.UMENG_VALUE_NBA_STATS);
		return true;
	}
	
	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
	}
	@Override
	public void onReceivedTitle(WebView view, String title) {
		// TODO Auto-generated method stub
		
	}
	
	

}
