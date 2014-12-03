package com.hupu.games.data.room;

import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;
/**
 * 
 * @author zhehua
 *
 */
public class GiftRespResultEntity  extends BaseEntity {
	public int status;
	public int balance;//虎扑币余额
	public int interval;//请求间隔(毫秒)
	public void paser(JSONObject json) throws Exception {
		JSONObject result=json.getJSONObject("result");
		// TODO Auto-generated method stub
		status = result.optInt("id");
		balance = result.optInt("balance");
		interval = result.optInt("interval");
	}
	
}
