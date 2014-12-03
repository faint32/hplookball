package com.hupu.games.data;

import org.json.JSONObject;

import android.util.Log;

/**聊天数据*/
public class ReqSmsEntity extends BaseEntity {

	public String order_id;
	public String mobile;
	
	@Override
	public void paser(JSONObject json) throws Exception {
		json=json.getJSONObject("result");
		mobile =json.optString("mobile");
		order_id =json.optString("order_id");
	}

}
