package com.hupu.games.data;

import org.json.JSONObject;

public class CheckBag extends BaseEntity {

	//1 有权限，-1 未登陆，-2 无权限，-3无权限且余额不够购买
	public int result;
	@Override
	public void paser(JSONObject json) throws Exception {
		result =json.optInt(KEY_RESULT, 0);
	}

}
