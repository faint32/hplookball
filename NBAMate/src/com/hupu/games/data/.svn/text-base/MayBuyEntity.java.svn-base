package com.hupu.games.data;

import org.json.JSONObject;


public class MayBuyEntity extends BaseEntity {

	
	public String pid;
	public String title;
	public String price;
	public String desc;
	
	@Override
	public void paser(JSONObject json) throws Exception {
		json=json.getJSONObject("result");
		pid =json.optString("pid");
		title =json.optString("title");
		price =json.optString("price");
		desc=json.optString("price");
	}

}
