package com.hupu.games.data.room;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class RoomListResp extends BaseEntity {

	public int default_room_id;
	public int is_enter;
	
	@Override
	public void paser(JSONObject json) throws Exception {
		// TODO Auto-generated method stub
		JSONObject mJson = json.optJSONObject(KEY_RESULT);
		
		JSONArray rooms = json.optJSONArray("room");
		JSONArray gifts = json.optJSONArray("giftlist");
		if(mJson!=null)
		{
			
		}

	}

}
