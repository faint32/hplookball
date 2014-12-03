package com.hupu.games.data.account;

import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class UserWalletResp extends BaseEntity {

	public String result;// "返回结果",

	@Override
	public void paser(JSONObject json) throws Exception {

		result = json.optString(KEY_RESULT, null);
	}

}
