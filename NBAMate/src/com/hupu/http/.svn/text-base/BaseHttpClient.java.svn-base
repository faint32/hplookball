package com.hupu.http;

import android.content.Context;

import com.pyj.http.RequestParams;

public abstract class BaseHttpClient {
	/**
	 * 退出给定context的http连接
	 * */
	public abstract void cancelRequests(Context context, boolean b);
	
	/**
	 * 虎扑看球的需求，UA中需要设置每次请求的网络类型
	 * */
	public void updateUserAgent(int networkType){}
	
	
	public abstract void get(Context context, String url,RequestParams params,BaseHttpResponseHandler responseHandler);
	
	public abstract void post(Context context, String url,RequestParams params,BaseHttpResponseHandler responseHandler);
	
	public String basicUA;

	/**
	 * Sets the User-Agent header to be sent with each request. By default,
	 * "Android Asynchronous Http Client/VERSION is used.
	 * 
	 * @param userAgent
	 *            the string to use in the User-Agent header.
	 */
	public void setUserAgent(String userAgent) {
		basicUA = userAgent;
	}

	public long today; 
}
