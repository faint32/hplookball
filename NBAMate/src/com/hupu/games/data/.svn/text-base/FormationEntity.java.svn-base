package com.hupu.games.data;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

public class FormationEntity extends BaseEntity {

	public int player_id;
	public int number;
	public String player_name;
	public int coordinateX;
	public int coordinateY;
	public int rating_oid;
	@Override
	public void paser(JSONObject json) throws Exception {
		player_id =json.optInt("player_id");
		player_name= json.optString("player_name");
		number =json.optInt("number");
		rating_oid =json.optInt("rating_oid");
		JSONArray array = json.optJSONArray("coordinate");
		if (array != null) {
			if (array.length() > 1) {
				coordinateX = array.optInt(0);
				coordinateY = array.optInt(1);
			}
		}
	}
}
