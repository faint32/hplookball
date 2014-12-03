package com.hupu.games.data;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

public class LineupEntity extends BaseEntity {

	public int player_id;
	public int type;
	public int number;
	public int rating_oid;
	public int mark;
	public String player_name;
	public String player_header;
	public String position;
	public String rating;
	public LinkedList<EventEntity> eventInfo;
	@Override
	public void paser(JSONObject json) throws Exception {
		player_id =json.optInt("player_id");
		type =json.optInt("type");
		number =json.optInt("number");
		rating_oid =json.optInt("rating_oid");
		mark =json.optInt("mark");
		player_name= json.optString("player_name");
		player_header= json.optString("player_header");
		position= json.optString("position");
		rating= json.optString("rating");
		JSONArray array = json.optJSONArray("event");
		if (array != null) {
			int size = array.length();
			eventInfo = new LinkedList<EventEntity>();
			EventEntity temp;
			for (int i = 0; i < size; i++) {
				temp = new EventEntity();
				temp.paser(array.getJSONObject(i));
				eventInfo.add(temp);
			}
		}
	}
	
	public class EventEntity extends BaseEntity{
		
		public int type;
		public String time;

		@Override
		public void paser(JSONObject json) throws Exception {
			// TODO Auto-generated method stub
			type = json.optInt("type");
			time = json.optString("time");
		}
		
	}
}
