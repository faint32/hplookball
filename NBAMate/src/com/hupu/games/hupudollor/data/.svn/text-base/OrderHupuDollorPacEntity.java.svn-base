package com.hupu.games.hupudollor.data;
/**
 * 
 */


import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class OrderHupuDollorPacEntity extends BaseEntity {
	public String id;
	public String channel;//充值渠道
	public String title;

	public int recharge;//充值金额
	public int give;//充值赠送的虎扑币数
	public int total;// 总虎扑币数

	public String coin_balance;//         金豆余额
	public String hupuDollor_balance;//  虎扑币余额;
	public String wallet_balance;//       钱包余额
	public LinkedList<OrderHupuDollorPacEntity> list;
	@Override
	public void paser(JSONObject json) throws Exception {
		id = json.optString("id");
		title = json.optString("title");
		recharge = json.optInt("recharge");
		give = json.optInt("give");
		channel = json.optString("channel");
		total = json.optInt("total");
		
		JSONObject result = json.optJSONObject(KEY_RESULT);
		if (result != null) {
			coin_balance = result.optString("coin_balance");
			hupuDollor_balance = result.optString("hupuDollor_balance");
			wallet_balance = result.optString("wallet_balance");
			
			JSONArray array = result.optJSONArray("events");
			if (array != null) {
				int size = array.length();
				list=new LinkedList<OrderHupuDollorPacEntity>();
				for (int i = 0; i < size; i++) {
					OrderHupuDollorPacEntity temp = new OrderHupuDollorPacEntity();
					temp.paser(array.getJSONObject(i));
					list.add(temp);
				}
			}
		}
		
	}
	
}
