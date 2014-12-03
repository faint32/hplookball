package com.hupu.games.data.personal.box;

import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class BoxBalanceEntity extends BaseEntity {

	public String memoGold;
	public int countGold;
	public String memoSliver;
	public int countSliver;
	public String memoCorpper;
	public int countCorpper;
	public int balance;
	public String boxMemo;
	@Override
	public void paser(JSONObject json) throws Exception {
		JSONObject obj =json.optJSONObject(KEY_RESULT);
		
		balance=obj.optInt("balance");
		boxMemo=obj.optString("box_memo","");
		
		obj = obj.optJSONObject("box_detail");
		
		JSONObject box =obj.getJSONObject("1");
		memoCorpper =box.optString("memo","");
		countCorpper =box.optInt("count");
		
		box =obj.getJSONObject("2");
		memoSliver =box.optString("memo","");
		countSliver =box.optInt("count");
		
		box =obj.getJSONObject("3");
		memoGold =box.optString("memo","");
		countGold =box.optInt("count");


	}


}
