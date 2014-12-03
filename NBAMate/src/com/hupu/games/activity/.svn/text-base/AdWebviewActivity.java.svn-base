/**
 * 
 */
package com.hupu.games.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.HupuScheme;
import com.hupu.games.view.HupuWebView;
import com.mato.sdk.proxy.Proxy;

/**
 * @author
 */
@SuppressLint("NewApi")
public class AdWebviewActivity extends HupuBaseActivity {

	private String url;
	private HupuWebView adWebView;
	View progressbar;
	Intent in;
	private String title;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_adview);
		adWebView = (HupuWebView) findViewById(R.id.ad_webview);
		Proxy.supportWebview(this);


		// myPrizeWebView.getSettings().setBlockNetworkImage(true);
		adWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
		adWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.btn_share);
		in = getIntent();
		// ���ù��ͷ�� title //ֻ�����������вŴ�����title
		if (in.getStringExtra("ad_title") != null) {
			title = in.getStringExtra("ad_title");
		}else {
			title = this.getString(R.string.app_name);
		}
		if (title != null) {
			((TextView) findViewById(R.id.txt_title)).setText(title);
		}
		url = in.getStringExtra("ad_url");

		adWebView.loadUrl(url);

		adWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				sendUmeng(HuPuRes.UMENG_EVENT_PROMOTION,
						HuPuRes.UMENG_KEY_SPLASH,
						HuPuRes.UMENG_VALUE_PAGEFINISHED);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Uri data = Uri.parse(url);
				if (data != null && "kanqiu".equalsIgnoreCase(data.getScheme())) {
					// ����ǿ���scheme
					HupuScheme scheme = new HupuScheme();
					scheme.paser(data);
					Intent intent = new Intent(AdWebviewActivity.this,
							HupuHomeActivity.class);
					intent.putExtra("scheme", (HupuScheme) scheme);
					startActivity(intent);
					AdWebviewActivity.this.finish();
				} else
					view.loadUrl(url);
				return true;
			}

		});
	}

	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);

		switch (id) {
		case R.id.btn_back:
			startToHome();
			break;
		case R.id.btn_share:
			showShareView("���˿���", "http://kanqiu.hupu.com/s?u=" + url, title,
					true);
			break;
		}
	}

	@Override
	protected void openWebBrowser() {
		closeDialog();
		Uri uri = Uri.parse(url);
		if (uri.getScheme() != null) {
			Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(viewIntent);
		} else
			startToHome();
	}

	private void startToHome() {
		if (title != null) {
			Intent intent = new Intent(this, HupuHomeActivity.class);
			Object scheme = in.getSerializableExtra("scheme");
			if (scheme != null) {
				// ���֪ͨ�����
				intent.putExtra("scheme", (HupuScheme) scheme);
			}
			startActivity(intent);
		}
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (adWebView.canGoBack()) {
				adWebView.goBack();
			} else {
				startToHome();

			}

			return true;
		}

		return super.onKeyDown(keyCode, event);

	}

	@Override
	protected void onDestroy() {

		if (adWebView != null) {
			adWebView.stopLoading();
			adWebView.clearHistory();
			adWebView.setVisibility(View.GONE);
			adWebView.destroy();
		}
		super.onDestroy();
	}
}
