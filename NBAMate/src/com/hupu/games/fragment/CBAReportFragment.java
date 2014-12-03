package com.hupu.games.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hupu.games.R;
import com.hupu.games.activity.WebViewActivity;
import com.hupu.games.data.game.basketball.SingleCBAData;

public class CBAReportFragment extends BaseFragment {

	private View mProgressBar;

	Activity mAct;

	WebView webView;

	private SingleCBAData data;
	private static final String mimeType = "text/html";
	private static final String encoding = "utf-8";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_cba_report, container,
				false);
		mAct = getActivity();
		mProgressBar = v.findViewById(R.id.probar_1);
		webView = (WebView) v.findViewById(R.id.web_content);
		webView.getSettings().setBuiltInZoomControls(false); // 设置显示缩放按钮
//		ws.setSupportZoom(true); // 支持缩放
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Intent in = new Intent(mAct, WebViewActivity.class);
				in.putExtra("url", url);
				startActivity(in);
				return true;
			}
		});
		return v;
	}

	public void setData(SingleCBAData d) {
		data = d;
		mProgressBar.setVisibility(View.GONE);
		if (data != null) {
			webView.loadDataWithBaseURL(null, data.str_content, mimeType,
					encoding, null);
		}
	}
}
