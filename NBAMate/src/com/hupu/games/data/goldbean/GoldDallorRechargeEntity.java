package com.hupu.games.data.goldbean;

import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class GoldDallorRechargeEntity extends BaseEntity{

	public String orderNo;
	public String url;
	public int is_login;
	public int status;
	@Override
	public void paser(JSONObject json) throws Exception {
		JSONObject result=json.getJSONObject("result");
		is_login = json.optInt("is_login");
		orderNo= result.optString("orderNo");		
		url= result.optString("url");	
		status = result.optInt("status");
		
	}
	
}
