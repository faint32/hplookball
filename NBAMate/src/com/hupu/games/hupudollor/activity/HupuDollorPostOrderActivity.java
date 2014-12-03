package com.hupu.games.hupudollor.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.SSLKey;
import com.hupu.games.hupudollor.data.HupuDollorRechargeReq;
import com.hupu.http.HupuHttpHandler;
/**
 * @author zhenhua 网页支付，充值成功会自动关闭该页面。
 * */
public class HupuDollorPostOrderActivity extends HupuBaseActivity {

	private WebView mWebView;

	View progressbar;
	private int methodId;
	
	private TextView txtTitle;

	@TargetApi(5)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_hupudollor_post_order);
		progressbar =findViewById(R.id.probar);
		txtTitle = (TextView)findViewById(R.id.txt_title);
		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				progressbar.setVisibility(View.GONE);
				if(url.contains("result=success")){
					isSuccess(url.contains("result=success"));
				}
			}
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				showToast("加载出错！");
			}
		});
		WebSettings ws = mWebView.getSettings();
		ws.setBuiltInZoomControls(true); // 设置显示缩放按钮
		ws.setSupportZoom(true); // 支持缩放
		ws.setJavaScriptEnabled(true);
		ws.setAllowFileAccess(true);
		ws.setUseWideViewPort(true);
		ws.setSupportMultipleWindows(true);
		ws.setRenderPriority(RenderPriority.HIGH);
		ws.setLoadsImagesAutomatically(true);
		ws.setCacheMode(WebSettings.LOAD_DEFAULT);// 设置缓存模式
		// post order data
		Intent in = getIntent();
		String token = in.getStringExtra("token");
		String type = in.getStringExtra("type");
		String evnet = in.getStringExtra("event");
		String charge = in.getStringExtra("charge");
		methodId = in.getIntExtra("method_id", HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_RECHARGE);
		if (methodId != HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_RECHARGE) {
			txtTitle.setText(getString(R.string.submit_post));
		}
		initParameter();
		mParams.put("token", token);
		mParams.put("type", type);
		mParams.put("event", evnet);
		mParams.put("charge", charge);
		mParams.put("agent", ws.getUserAgentString());
		String sign = SSLKey.getSSLSign(mParams, SharedPreferencesMgr.getString("sugar", ""));//salt 值由init中的sugar给出。必须要有的。
		mParams.put("sign", sign);
		
		sendRequest(methodId, mParams,
				new HupuHttpHandler(this), false);
		setOnClickListener(R.id.btn_back);
		mApp.doPost=true;
	}
	@Override
	public void onReqResponse(Object o, int payMethodId) {
		super.onReqResponse(o, methodId);
		if (payMethodId == methodId) {
			HupuDollorRechargeReq req=(HupuDollorRechargeReq) o;
			if(req.url!=null)
				mWebView.loadUrl(req.url);
		}
	}  
	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);
		switch (id) {
		case R.id.btn_back:
			setResult(RESULT_CANCELED);
			finish();		
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			setResult(RESULT_CANCELED);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	void isSuccess(boolean s)
	{
		Intent in =new Intent();
		if(s){
			in.putExtra("success", 1);
			mApp.doPost=false;
			setResult(RESULT_OK,in);
		}
		else{
			setResult(RESULT_OK);
		}
		this.finish();
	}
	


}
