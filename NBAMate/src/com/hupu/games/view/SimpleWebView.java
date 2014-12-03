package com.hupu.games.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hupu.games.common.HupuLog;
import com.hupu.games.common.HupuSchemeProccess;
import com.hupu.games.handler.IWebViewClientEvent;

public class SimpleWebView extends WebView {

	protected IWebViewClientEvent mIWebViewClientEvent;

	public SimpleWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWebViewClient();
	}

	public SimpleWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWebViewClient();
	}

	public SimpleWebView(Context context) {
		super(context);
		initWebViewClient();
	}

	public void setWebViewClientEventListener(IWebViewClientEvent i) {
		mIWebViewClientEvent = i;
	}

	/**
	 * 是否由调用类自己处理URL跳转事务
	 * */
	boolean mBoolTreatURL;

	public void setWebViewClientEventListener(IWebViewClientEvent i,
			boolean bTreatURL) {
		mIWebViewClientEvent = i;
		mBoolTreatURL = bTreatURL;
	}


	public void switchActivity(Uri uri) {

		HupuSchemeProccess.proccessScheme(getContext(), uri);
	}

	void initWebViewClient() {
		CookieManager.getInstance().setAcceptCookie(true);
		setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				HupuLog.e("SimpleWebView ", "shouldOverrideUrlLoading="+url);
				Uri uri = Uri.parse(url);
				if (uri.getScheme().equalsIgnoreCase("kanqiu")) {
					switchActivity(uri);
					if (mIWebViewClientEvent != null)
						mIWebViewClientEvent.shouldOverrideUrlLoading(view,
								url, true);
				} else {
					if (!mBoolTreatURL)
						view.loadUrl(url);
					if (mIWebViewClientEvent != null)
						return mIWebViewClientEvent.shouldOverrideUrlLoading(
								view, url, false);

				}
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if (mIWebViewClientEvent != null)
					mIWebViewClientEvent.onPageFinished(view, url);
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				if (mIWebViewClientEvent != null)
					mIWebViewClientEvent.onReceivedError(view, errorCode,
							description, failingUrl);
			}

		});
	}
}
