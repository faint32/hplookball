package com.hupu.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.hupu.games.HuPuApp;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.BaseEntity;
import com.hupu.games.data.JsonPaserFactory;
import com.pyj.activity.BaseActivity;
import com.pyj.http.AsyncHttpClient;
import com.pyj.http.AsyncHttpResponseHandler;

public class HupuHttpHandler extends AsyncHttpResponseHandler {

	public BaseActivity a;
	public int messageID1;
	public int messageID2;
	
	public HupuHttpHandler(BaseActivity act)
	{
		a=act;
	}

	

	@Override
	public void onSuccess(int statusCode, Header[] headers, HttpEntity content,
			int reqType) {
		// TODO Auto-generated method stub
		super.onSuccess(statusCode, headers, content, reqType);
		
		//将所有cookie 存储到cookieManager 中
		for (Header header:headers) {
			if ("set-cookie".compareToIgnoreCase(header.getName()) == 0) 
				HuPuApp.syncCookiesToAppCookieManager(header,reqType);
		}
	}



	@Override
	public void onSuccess(String content , int reqType) {
		if(a!=null && !a.isFinishing())
		{
			BaseEntity o=JsonPaserFactory.paserObj(content, reqType);
			if(o!=null && o.err!=null)
			{				
//				a.onErrResponse(new HupuHttpException(o.err), reqType);
				a.onErrMsg(o.err, reqType);
				return;
			}
			if(messageID1!=0)
				a.onReqResponse(o, reqType,messageID1,messageID2);
			else
				a.onReqResponse(o, reqType);
//			if(a instanceof HupuBaseActivity)//为了okhttp新增逻辑
//				((HupuBaseActivity)a).setToday(today);
		}
	}

	
	@Override
	public void onFailure(Throwable error, int reqType) {
		if(a!=null && !a.isFinishing())
		{
			a.onErrResponse(error,reqType);
		}
	}


	
}
