package com.hupu.games.hupudollor.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hupu.games.R;
import com.hupu.games.activity.ContactsActivity;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.view.HupuWebView;
import com.mato.sdk.proxy.Proxy;

/**
 * @author zhenhua虎扑币明细；
 */
public class HupuDollorLogActivity extends HupuBaseActivity {


//	private String url;
	private HupuWebView coinInfoWebView;
	View progressbar;
//	TextView titleTextView;
//	int reqType = -1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_hupudollor_info);
//		titleTextView = (TextView) findViewById(R.id.txt_title);
		coinInfoWebView = (HupuWebView) findViewById(R.id.coin_info_webview);
		Proxy.supportWebview(this);
		progressbar = findViewById(R.id.probar);
		
		coinInfoWebView.getSettings().setBlockNetworkImage(true);
		coinInfoWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
		coinInfoWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		coinInfoWebView.getSettings().setJavaScriptEnabled(true);
		
		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.btn_contacts);
//		reqType = getIntent().getIntExtra("info_type", HuPuRes.REQ_METHOD_GET_COIN_INFO);
		coinInfoWebView.loadUrl(getCoinUrl());
		
		coinInfoWebView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				progressbar.setVisibility(View.GONE);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				Uri uri = Uri.parse(url);
				if (!"".equals(uri.getQueryParameter("mobile"))) {
					
				}
				return super.shouldOverrideUrlLoading(view, url);
			}
			
			
		});
	}
	
	private String getCoinUrl(){
		return HuPuRes.getUrl(HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_INFO)+"?token=" + (mToken == null ? "0" : mToken) +"&client="+mDeviceId;
		
	}
	
	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);

		switch (id) {
		case R.id.btn_back:
			goBack();
			break;
		case R.id.btn_contacts:
			Intent contaces = new Intent(this, ContactsActivity.class);
			startActivity(contaces);
			break;
		}
	}
	
	private void goBack(){
		 if (coinInfoWebView.canGoBack())
         {
//			 if (reqType == HuPuRes.REQ_METHOD_CHANGE_MOBILE)
//				 this.finish();
//			 else
				 coinInfoWebView.goBack();
         }else {
             this.finish();
         }
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {  
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            goBack();
            return true;
        }
        
        return super.onKeyDown(keyCode, event);
       
    }

}
