package com.hupu.games.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyCaipiaoResp extends BaseEntity {

	public ArrayList<MyCaipiaoListResp> mList;
	public int total;
	public int current;
	
	public String walletBalance;

	@Override
	public void paser(JSONObject json) throws Exception {
		// TODO Auto-generated method stub
		JSONObject result = json.optJSONObject(KEY_RESULT);
		if (result != null) {
			//难道分页信息
			walletBalance = result.optString("wallet_balance");
			if (result.optJSONObject("pages") !=null ) {
				total = result.optJSONObject("pages").optInt("all");
				current = result.optJSONObject("pages").optInt("current");
			}
			
			JSONArray data = result.optJSONArray("data");
			if (data != null) {
				mList = new ArrayList<MyCaipiaoListResp>();
				MyCaipiaoListResp myCaipiao;
				for (int i = 0; i < data.length(); i++) {
					myCaipiao = new MyCaipiaoListResp();
					myCaipiao.paser(data.optJSONObject(i));
					mList.add(myCaipiao);
				}
			}
		}
		
	}

}
