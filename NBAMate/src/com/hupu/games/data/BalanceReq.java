/**
 * 
 */
package com.hupu.games.data;

import org.json.JSONObject;

/**
 * @author panyongjun
 * 查询余额
 */
public class BalanceReq extends BaseEntity {

	public int balance;
	@Override
	public void paser(JSONObject json) throws Exception {
		balance= json.optInt(KEY_RESULT,0);			
	}

}
