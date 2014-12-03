package com.hupu.games.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.NBAGameActivity;
import com.hupu.games.activity.WebViewActivity;
import com.hupu.games.data.Recap;
import com.hupu.games.handler.IWebViewClientEvent;
import com.hupu.games.view.SimpleWebView;
import com.mato.sdk.proxy.Proxy;

public class ReportFragment extends BaseFragment implements
IWebViewClientEvent{

	private SimpleWebView mWebView;
	private TextView mTxtTitle;
	
	View mProgressBar;
	
	private Recap data;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v =inflater.inflate(R.layout.fragment_report, null);
		mWebView=(SimpleWebView)v.findViewById(R.id.web_content);
		mWebView.setWebViewClientEventListener(this, true);
		mWebView.getSettings().setBuiltInZoomControls(false); // 设置显示缩放按钮
		Proxy.supportWebview(getActivity());
		mTxtTitle=(TextView)v.findViewById(R.id.txt_report_title);
		mProgressBar=v.findViewById(R.id.probar);
		if(data!=null)
			setData(data);
		return v;
	}
	
	private static final String mimeType = "text/html";
	private static final String encoding = "utf-8";

	public void setData(Recap d)
	{
		data=d;
		if(mProgressBar!=null )
			mProgressBar.setVisibility(View.GONE);
		if(data!=null )
		{
			if(mTxtTitle!=null)
			{				
				if(data.str_title ==null )
				{
					mTxtTitle.setVisibility(View.GONE);
				}
				else
				{
					mTxtTitle.setText(data.str_title);
				}
			}
			mWebView.loadDataWithBaseURL(null, getContent(data.str_content), mimeType,
					encoding, null);
		}
	}
	
	private String getContent(String c)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">"
				+ "<head>"
				+"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"				
				+ "<title></title>"
				+"</head>" + "<body>"
				+c
			    +"</body></html>");
		return sb.toString();
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url,
			boolean isScheme) {
		if(!isScheme)
		{
			//app://nba/nba/ratings/{$gid
			if(url.indexOf("app://") >-1)
			{
				((NBAGameActivity)getActivity()).switchToRating();
			}
			else
			{
				Intent in =new Intent(getActivity(),WebViewActivity.class);
				in.putExtra("url", url);
				startActivity(in);
			}
		}
		return true;
	}

	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReceivedTitle(WebView view, String title) {
		// TODO Auto-generated method stub
		
	}
	

}
