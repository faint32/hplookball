package com.hupu.games.data;

import org.json.JSONObject;

/**聊天数据*/
public class ContactsEntity extends BaseEntity {

	
	public String msg;
	public String num;
	@Override
	public void paser(JSONObject json) throws Exception {
		json=json.getJSONObject("result");
		msg =json.optString("msg");
		num =json.optString("num");
	}

}
