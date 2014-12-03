package com.hupu.games.data.goldbean;

import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class GoldEntity extends BaseEntity {

	public String gold_num = ""; //  领取奖励返回状态
	
	public String islogin = ""; //领取返回的提示信息
	
	@Override
	public void paser(JSONObject mJson) throws Exception {
		// TODO Auto-generated method stub
		
		gold_num = mJson.has("result")?mJson.optString("result"):"";
		islogin = mJson.has("is_login")?mJson.optString("is_login"):"";

	}

}
