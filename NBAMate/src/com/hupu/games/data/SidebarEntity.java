package com.hupu.games.data;

import org.json.JSONObject;

/**侧边栏数据*/
public class SidebarEntity extends BaseEntity {

	public String balance;
	public String rankInfo;
	public String walletBalance;
	public String hupuDollor_balance;
	
	@Override
	public void paser(JSONObject json) throws Exception {
		super.paser(json);
		JSONObject object = json.optJSONObject(KEY_RESULT);
		balance = object.optString("balance");
		rankInfo = object.optString("rank");
		walletBalance = object.optString("wallet_balance");
		hupuDollor_balance = object.optString("hupuDollor_balance");
	}
}
