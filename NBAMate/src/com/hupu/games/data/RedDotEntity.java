package com.hupu.games.data;

import org.json.JSONObject;

/**小红点数据*/
public class RedDotEntity extends BaseEntity {

	public String balance;
	public String rankInfo;
	public String walletBalance;
	
	@Override
	public void paser(JSONObject json) throws Exception {
		super.paser(json);
	}
}
