package com.hupu.games.handler;

import android.webkit.WebView;

/**
 * 为了在webview中支持scheme的跳转，将webview封装成公共类，
 * 使用Hupuwebivew的类可以通过实现这个接口来处理webclient的回调事件
 * */
public interface IWebViewClientEvent {
	void onPageFinished(WebView view, String url);

	boolean shouldOverrideUrlLoading(WebView view, String url,boolean isScheme);

	void onReceivedError(WebView view, int errorCode, String description,
			String failingUrl);
	void onReceivedTitle(WebView view, String title) ;
}
