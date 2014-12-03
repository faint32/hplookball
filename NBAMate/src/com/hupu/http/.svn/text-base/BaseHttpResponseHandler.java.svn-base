/**
 * 
 */
package com.hupu.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * @author panyongjun
 *
 * 为了方便更换http的传输层，解耦当前的http框架
 */
public abstract class BaseHttpResponseHandler {

	protected static final int SUCCESS_MESSAGE = 0;
	protected static final int FAILURE_MESSAGE = 1;
	protected static final int START_MESSAGE = 2;
	protected static final int FINISH_MESSAGE = 3;

	protected Handler handler;
	public int reqCode;
	/**
	 * Creates a new AsyncHttpResponseHandler
	 */
	public BaseHttpResponseHandler(int code) {
		// Set up a handler to post events back to the correct thread if
		// possible
		this();
		reqCode =code;
	}

	public BaseHttpResponseHandler()
	{
		if (Looper.myLooper() != null) {
			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {BaseHttpResponseHandler.this.handleMessage(msg);
				}
			};
		}
	}

	public void setUserAgent(String ua)
	{
		
	}
	/**
	 * Fired when a request returns successfully, override to handle in your own
	 * code
	 * 
	 * @param content
	 *            the body of the HTTP response from the server
	 */
	public abstract void onSuccess(String content, int reqType) ;


	/**
	 * Fired when a request fails to complete, override to handle in your own
	 * code
	 * 
	 * @param error
	 *            the underlying cause of the failure
	 */
	public abstract void onFailure(Throwable error, int reqType) ;


	protected void sendSuccessMessage(String responseBody ,int reqType) {
	
	}

	protected void sendFailureMessage(Throwable e, int reqType) {
		
	}

	/**Handler and Message methods***/
	protected  void handleMessage(Message msg) 
	{
		
	}

	protected void sendMessage(Message msg) {
		if (handler != null) {
			handler.sendMessage(msg);
		} else {
			handleMessage(msg);
		}
	}

	/**
	 * 
	 * */
	protected Message obtainMessage(int responseMessage, Object response) {
		Message msg = null;
		if (handler != null) {
			msg = handler.obtainMessage(responseMessage, response);
		} else {
			msg = Message.obtain();
			msg.what = responseMessage;
			msg.obj = response;
		}
		return msg;
	}
}
