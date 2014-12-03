/**
 * 
 */
package com.hupu.games.data;

import org.json.JSONObject;

/**
 * @author panyongjun
 * 查询余额
 */
public class BitCoinReq extends BaseEntity {

	public int balance=-1;
	public int betCoins;
	@Override
	public void paser(JSONObject json) throws Exception {
		JSONObject obj =json.optJSONObject(KEY_RESULT);
		balance= obj.optInt("balance",0);		
		betCoins = obj.optInt("bet_coins",0);	
	}

}
