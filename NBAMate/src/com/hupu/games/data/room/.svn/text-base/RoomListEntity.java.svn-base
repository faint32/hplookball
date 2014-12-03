package com.hupu.games.data.room;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class RoomListEntity extends BaseEntity {
	
	public ArrayList<RoomEntity> roomList;

	@Override
	public void paser(JSONObject json) throws Exception {
		// TODO Auto-generated method stub

		JSONArray roomArr = json.optJSONArray("room_list");
		if (roomArr != null) {
			roomList = new ArrayList<RoomEntity>();
			RoomEntity roomEntity;
			for (int i = 0; i < roomArr.length(); i++) {
				roomEntity = new RoomEntity();
				roomEntity.paser(roomArr.optJSONObject(i));
				roomList.add(roomEntity);
			}
		}
		
	}

	
}
