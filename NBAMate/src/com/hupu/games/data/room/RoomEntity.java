package com.hupu.games.data.room;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class RoomEntity extends BaseEntity {

	public int id;   			//房间ID
	public String name;  		//房间名
	public String anchor_list;  //主播列表
	public int count;   		//在线人数
	
	@Override
	public void paser(JSONObject json) throws Exception {
		// TODO Auto-generated method stub
		id = json.optInt("id");
		count = json.optInt("count");
		name = json.optString("name");
		anchor_list = json.optString("anchor_list");
	}

}
