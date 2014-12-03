/**
 * 
 */
package com.hupu.games.activity;

import android.R.integer;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.view.HupuWebView;
import com.mato.sdk.proxy.Proxy;

/**
 * @author
 */
public class FAQActivity extends HupuBaseActivity {


	private String url;
	private HupuWebView FAQWebView;
	View progressbar;
	private TextView titleTextView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_faq_info);
		titleTextView = (TextView) findViewById(R.id.txt_title);
		FAQWebView = (HupuWebView) findViewById(R.id.faq_webview);
		Proxy.supportWebview(this);
		progressbar = findViewById(R.id.probar);
		
		FAQWebView.getSettings().setBlockNetworkImage(true);
		FAQWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
		FAQWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		FAQWebView.getSettings().setJavaScriptEnabled(true);
		
		setOnClickListener(R.id.btn_back);
		if (getIntent().getStringExtra("url").equals(HuPuRes.getUrl(HuPuRes.REQ_METHOD_CAIPIAO_AGREEMENT))) {
			titleTextView.setText(getString(R.string.txt_agreement_link));
		}
		FAQWebView.loadUrl(getIntent().getStringExtra("url"));
		
		FAQWebView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				progressbar.setVisibility(View.GONE);
			}
		});
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

}
