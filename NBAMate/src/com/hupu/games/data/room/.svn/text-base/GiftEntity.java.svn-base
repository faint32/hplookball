package com.hupu.games.data.room;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class GiftEntity extends BaseEntity {

	public int gift_id;   			//礼物ID
	public String gift_name;  		//礼物名
	public int price;   		//价格
	public int count;   		//总数
	public int push_count;//push过来的送礼总数
	public int push_count_interadd;//push的送礼总数每次累加的间隔

	
	@Override
	public void paser(JSONObject json) throws Exception {
		// TODO Auto-generated method stub
		gift_id = json.optInt("gift_id");
		price = json.optInt("price");
		count = json.optInt("count");
		gift_name = json.optString("gift_name");
	}

}
