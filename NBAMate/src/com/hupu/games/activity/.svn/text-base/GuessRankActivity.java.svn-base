/**
 * 
 */
package com.hupu.games.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
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
import com.hupu.games.view.HupuWebView;
import com.mato.sdk.proxy.Proxy;

/**
 * @author papa 竞猜排行榜  需要lid和gid
 */
public class GuessRankActivity extends HupuBaseActivity {


	private String url;
	private String baseUrl;
	Intent in;
	private HupuWebView rankWebView;
	private SimpleDateFormat format;
	private TextView titleTextView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		format = new SimpleDateFormat("yyyyMMdd");
		setContentView(R.layout.layout_guess_rank);
		rankWebView = (HupuWebView) findViewById(R.id.rank_webview);
		Proxy.supportWebview(this);

		titleTextView = (TextView) findViewById(R.id.txt_title);
		in = getIntent();
		roomid = in.getIntExtra("roomid", 0);
		
		
		//rankWebView.getSettings().setBlockNetworkImage(true);
		rankWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
		rankWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		rankWebView.getSettings().setJavaScriptEnabled(true);
		//rankWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		
		if (in.getIntExtra("rank_type", 0) == 0) {
			rankWebView.loadUrl(getRankUrl());
		}else {
			rankWebView.loadUrl(getAllRankUrl());
			findViewById(R.id.btn_all_rank).setVisibility(View.GONE);
		}
		
		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.btn_all_rank);
		
	}
	
	private String getRankUrl(){
		titleTextView.setText(getString(R.string.title_guess_rank));
		baseUrl = HuPuRes.getUrl(HuPuRes.REQ_METHOD_GET_GUESS_RANK);
		url = baseUrl+"?pid=" + in.getIntExtra("gid", 0)+"&lid=" + in.getIntExtra("lid", 1) + "&token=" + (mToken == null ? "0" : mToken) +"&client="+mDeviceId+"&roomid="+roomid;
		return url;
	}
	
	private String getAllRankUrl(){
		titleTextView.setText(getString(R.string.title_all_guess_rank));
		baseUrl = HuPuRes.getUrl(HuPuRes.REQ_METHOD_GET_GUESS_ALL_RANK);
		url = baseUrl +"?today="+format.format(new Date())+"&token=" +(mToken == null ? "0" : mToken) +"&client="+mDeviceId + "&roomid="+roomid;
		return url;
	}
	
	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);

		switch (id) {
		case R.id.btn_back:
			if (in.getIntExtra("rank_type", 0) == 0 && findViewById(R.id.btn_all_rank).getVisibility() == View.GONE) {
				rankWebView.loadUrl(getRankUrl());
				findViewById(R.id.btn_all_rank).setVisibility(View.VISIBLE);
				//转圈一下
			}else {
				finish();
			}
			
			break;
		case R.id.btn_all_rank:
			//转圈一下
			rankWebView.loadUrl(getAllRankUrl());
			findViewById(R.id.btn_all_rank).setVisibility(View.GONE);
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (in.getIntExtra("rank_type", 0) == 0 && findViewById(R.id.btn_all_rank).getVisibility() == View.GONE) {
				rankWebView.loadUrl(getRankUrl());
				findViewById(R.id.btn_all_rank).setVisibility(View.VISIBLE);
			}else{
				finish();
			}	
		}else if (keyCode == KeyEvent.KEYCODE_MENU
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
		}
		return false;
	}

}
