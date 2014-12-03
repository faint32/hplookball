package com.hupu.games.handler;



import org.json.JSONArray;
import org.json.JSONObject;

import com.koushikdutta.async.http.socketio.SocketIOException;

public interface ISocketCallBack {

	public void onSocketConnect() ;

	public void onSocketDisconnect() ;

	public void onReconnect();
	
	public void onSocketError(Exception socketIOException);
	
	
	public void onSocketResp(JSONArray obj);

}
