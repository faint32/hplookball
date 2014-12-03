package com.hupu.games.fragment;

import java.lang.reflect.InvocationTargetException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.hupu.games.R;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.activity.HupuHomeActivity;
import com.hupu.games.common.HupuLog;
import com.hupu.games.handler.IWebViewClientEvent;
import com.hupu.games.view.HupuWebView;
import com.mato.sdk.proxy.Proxy;

@SuppressLint({ "ValidFragment", "NewApi" })
public class WebViewFragment extends BaseFragment implements
		IWebViewClientEvent {

	private HupuWebView mWebView;

	/** 当前的所连接的url */
	private String mStrUrl;

	private ImageButton mBtnPre;

	private ImageButton mBtnNext;

	View layoutToolBar;
	/** 两种模式，一种需要导航栏，一种不需要 */
	int mMode;

	public WebViewFragment(int m) {
		mMode = m;
	}

	public void setUrl(String url) {
		mStrUrl = url;
		HupuLog.e("url", url);
	}

	class Click implements OnClickListener {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.btn_fresh:// 刷新
				mWebView.reload();
				break;
			case R.id.btn_next:
				goForward();
				break;
			case R.id.btn_pre:
				goBack();
				break;
//			case R.id.btn_browser:// 跳转到手机自带的浏览器
//				Intent viewIntent = new Intent(Intent.ACTION_VIEW,
//						Uri.parse(mStrUrl));
//				startActivity(viewIntent);
//				break;
			}

		}

	}

	/** 浏览历史往后 */
	private void goBack() {
		if (mWebView.canGoBack())
			mWebView.goBack();
		setState();

	}

	/** 浏览历史往前 */
	private void goForward() {
		if (mWebView.canGoForward())
			mWebView.goForward();
		setState();
	}

	/** 设定底部导航的不同ui显示 */
	private void setState() {
		if (mWebView.canGoBack())
			// mBtnPre.setImageResource(R.drawable.btn_pre);
			mBtnPre.setEnabled(true);
		else
			mBtnPre.setEnabled(false);
		// mBtnPre.setImageResource(R.drawable.btn_pre_disable);
		if (mWebView.canGoForward())
			// mBtnNext.setImageResource(R.drawable.btn_next);
			mBtnNext.setEnabled(true);
		else
			// mBtnNext.setImageResource(R.drawable.btn_next_disable);
			mBtnNext.setEnabled(false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_webview, container, false);
		mWebView = (HupuWebView) v.findViewById(R.id.webview);
		mWebView.setWebViewClientEventListener(this);
		Proxy.supportWebview(getActivity());
		if (android.os.Build.VERSION.SDK_INT > 10) {
			mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		CookieManager.getInstance().setAcceptCookie(true);
		((HupuBaseActivity)getActivity()).setShareEnable(false);
		mWebView.loadUrl(mStrUrl);

		mBtnPre = (ImageButton) v.findViewById(R.id.btn_pre);
		mBtnNext = (ImageButton) v.findViewById(R.id.btn_next);
		layoutToolBar = v.findViewById(R.id.layout_tool_bar);
		Click click = new Click();
		mBtnPre.setOnClickListener(click);
		mBtnNext.setOnClickListener(click);
		///v.findViewById(R.id.btn_browser).setOnClickListener(click);
		v.findViewById(R.id.btn_fresh).setOnClickListener(click);
		return v;
	}

	/**
	 * 由其他的页面切换进入
	 * */
	public void entry(String url) {
		HupuLog.d("load urlentry =" + mStrUrl);

		if (mWebView != null && mStrUrl != url) {
			mWebView.clearCache(false);
			((HupuBaseActivity)getActivity()).setShareEnable(false);
			mWebView.loadUrl(url);
		}
		mStrUrl = url;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mWebView != null) {
			try {
				mWebView.getClass().getMethod("onResume")
						.invoke(mWebView, (Object[]) null);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
	}

	public void onPause() {
		super.onPause();

		if (mWebView != null) {
			try {
				mWebView.getClass().getMethod("onPause")
						.invoke(mWebView, (Object[]) null);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onDestroy() {

		if (mWebView != null) {
			mWebView.stopLoading();
			mWebView.clearHistory();
			mWebView.setVisibility(View.GONE);
			mWebView.destroy();
		}
		super.onDestroy();
	}

	
	@Override
	public void onPageFinished(WebView view, String url) {
		setState();
		((HupuBaseActivity)getActivity()).setShareEnable(true);
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url,
			boolean isScheme) {
		mStrUrl =url;
		((HupuBaseActivity)getActivity()).setShareEnable(false);
		return true;
	}

	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
	}

	String mWebTitle;
	@Override
	public void onReceivedTitle(WebView view, String title) {
		mWebTitle =title;			
	}

	public String getShareUrl()
	{
		return mStrUrl;
	}
	
	public String getShareContent()
	{
		return mWebTitle;
	}
}
