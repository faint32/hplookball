package com.hupu.games.data;





import java.util.HashMap;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class OrderPacEntity extends BaseEntity {
	public String id;
	public String title;
	public int recharge;
	public int give;
	public int total;
	public String channel;
	public String coinBalance;
	public String walletBalance;
	public LinkedList<OrderPacEntity> list;
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
			coinBalance = result.optString("coin_balance");
			walletBalance = result.optString("wallet_balance");
			
			JSONArray array = result.optJSONArray("events");
			if (array != null) {
				int size = array.length();
				list=new LinkedList<OrderPacEntity>();
				for (int i = 0; i < size; i++) {
					OrderPacEntity temp = new OrderPacEntity();
					temp.paser(array.getJSONObject(i));
					list.add(temp);
				}
			}
		}
		
	}
	
}
