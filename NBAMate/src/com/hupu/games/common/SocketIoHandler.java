package com.hupu.games.common;



import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.hupu.games.handler.ISocketCallBack;
import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.DisconnectCallback;
import com.koushikdutta.async.http.socketio.EventCallback;
import com.koushikdutta.async.http.socketio.JSONCallback;
import com.koushikdutta.async.http.socketio.ReconnectCallback;
import com.koushikdutta.async.http.socketio.SocketIOException;
import com.koushikdutta.async.http.socketio.StringCallback;

public class SocketIoHandler implements StringCallback,EventCallback,JSONCallback,DisconnectCallback,ReconnectCallback{
	private Handler handler;
	private static final int SUCCESS_MESSAGE = 0;
	private static final int FAILURE_MESSAGE = 1;
	private static final int CONNECT_MESSAGE = 2;
	private static final int DISCONNECT_MESSAGE = 3;
	private static final int RECONNECT_MESSAGE = 4;

	public final String TAG="HUPUAPP";
	public SocketIoHandler(ISocketCallBack call) {
		callBack = call;
		if (Looper.myLooper() != null) {
			handler = new Handler() {
				public void handleMessage(Message msg) {
					SocketIoHandler.this.handleMessage(msg);
				}
			};
		}
	}

	private ISocketCallBack callBack;

	protected void handleMessage(Message msg) {
		if (callBack == null)
			return;
		switch (msg.what) {
		case SUCCESS_MESSAGE:
			callBack.onSocketResp((JSONArray) msg.obj);
			break;
		case FAILURE_MESSAGE:
			callBack.onSocketError((SocketIOException)msg.obj);
			break;
		case CONNECT_MESSAGE:
			callBack.onSocketConnect();
			break;
		case DISCONNECT_MESSAGE:
			callBack.onSocketDisconnect();
			break;
		case RECONNECT_MESSAGE:
			callBack.onReconnect();
			
			break;
		}
	}

	protected Message obtainMessage(int responseMessage, Object response) {
		Message msg = null;
		if (handler != null) {
			msg = this.handler.obtainMessage(responseMessage, response);
		} else {
			msg = new Message();
			msg.what = responseMessage;
			msg.obj = response;
		}
		return msg;
	}

	protected void sendMessage(Message msg) {
		if (handler != null) {
			handler.sendMessage(msg);
		} else {
			handleMessage(msg);
		}
	}


	@Override
	public void onString(String string, Acknowledge acknowledge) {
		HupuLog.e(TAG, "io onString   >>>>>>:::::"+string);
		sendMessage(obtainMessage(SUCCESS_MESSAGE, string));
		
	}

	@Override
	public void onEvent(JSONArray argument, Acknowledge acknowledge) {
//		HupuLog.e(TAG, "io onEvent   >>>>>>:::::"+argument.toString());
		sendMessage(obtainMessage(SUCCESS_MESSAGE, argument));
	}

	@Override
	public void onJSON(JSONObject json, Acknowledge acknowledge) {
//		HupuLog.e(TAG, "io onJSON   >>>>>>:::::"+json.toString());
		sendMessage(obtainMessage(SUCCESS_MESSAGE, json));
	}

	@Override
	public void onDisconnect(Exception e) {
		HupuLog.d(TAG, "io onJSON   >>>>>>::::: onDisconnect");
		 sendMessage(obtainMessage(DISCONNECT_MESSAGE, e));
	}

	@Override
	public void onReconnect() {		
		HupuLog.d(TAG, " io onJSON   >>>>>>:::::onReconnect");
		sendMessage(obtainMessage(RECONNECT_MESSAGE, 0));
	}
}
