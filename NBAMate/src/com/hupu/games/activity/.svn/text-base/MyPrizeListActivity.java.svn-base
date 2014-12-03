/**
 * 
 */
package com.hupu.games.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.RenderPriority;
import android.widget.ListView;

import com.hupu.games.R;
import com.hupu.games.adapter.MyPrizeListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.BalanceReq;
import com.hupu.games.data.CommitExchangeReq;
import com.hupu.games.data.LiveEntity.Answer;
import com.hupu.games.data.MyPrizeResp;
import com.hupu.games.pay.PhoneInputActivity;
import com.hupu.games.view.HupuWebView;
import com.hupu.http.HupuHttpHandler;
import com.mato.sdk.proxy.Proxy;
import com.pyj.http.RequestParams;

/**
 * @author 
 */
@SuppressLint("NewApi")
public class MyPrizeListActivity extends HupuBaseActivity {

	private String url;
	private HupuWebView myPrizeWebView;
	View progressbar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_my_prize_list);
		myPrizeWebView = (HupuWebView) findViewById(R.id.my_prize_webview);
		Proxy.supportWebview(this);
		

//		myPrizeWebView.getSettings().setBlockNetworkImage(true);
		myPrizeWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
		myPrizeWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		
		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.btn_contacts);
		myPrizeWebView.loadUrl(getCoinUrl());
		
	}
	
	private String getCoinUrl(){
		url = HuPuRes.getUrl(HuPuRes.REQ_METHOD_GET_MY_PRIZE)+"?token=" + (mToken == null ? "0" : mToken) +"&client="+mDeviceId;
		return url;
	}
	
	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);

		switch (id) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_contacts:
			sendUmeng(HuPuRes.UMENG_EVENT_MALL, HuPuRes.UMENG_KEY_MY_AWARD, HuPuRes.UMENG_VALUE_CONTACE_SERVICE);
			
			Intent myPrize = new Intent(this, ContactsActivity.class);
			startActivity(myPrize);
			break;
		}
	}
}
