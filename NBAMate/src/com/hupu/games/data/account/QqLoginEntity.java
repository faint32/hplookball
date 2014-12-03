package com.hupu.games.data.account;

import org.json.JSONObject;

public class QqLoginEntity {

	public String openId;// 授权ID
	public String access_token;// 授权token
	public String expires_in;// 过期时间

	public void paser(JSONObject json){
		openId = json.optString("openid");
		access_token = json.optString("access_token");
		expires_in = json.optString("expires_in");
	}

}
