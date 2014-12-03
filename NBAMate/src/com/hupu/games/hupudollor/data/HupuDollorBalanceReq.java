package com.hupu.games.hupudollor.data;

import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

/**
 * @author zhenhua 
 * 虎扑币余额查询
 */
public class HupuDollorBalanceReq extends BaseEntity {

	public int balance;
	public int is_login;
	@Override
	public void paser(JSONObject json) throws Exception {
		balance= json.optInt(KEY_RESULT,0);		
		is_login= json.optInt("is_login",0);		
	}

}
