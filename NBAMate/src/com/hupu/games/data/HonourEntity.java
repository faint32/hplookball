package com.hupu.games.data;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

public class HonourEntity extends BaseEntity {

	public LinkedList<HonourEntity> mList;
	public String rank;
	public String name;
	public int socre;
	@Override
	public void paser(JSONObject json) throws Exception {
		//json =json.optJSONObject("result");
		JSONArray array = json.optJSONArray("result");
		if (array != null) {
			int size = array.length();
			mList = new LinkedList<HonourEntity>();
			HonourEntity temp;
			for (int i = 0; i < size; i++) {
				temp = new HonourEntity();
				temp.paser(array.getJSONObject(i));
				mList.add(temp);
			}
		}
		socre =json.optInt("socre");
		name= json.optString("name");
		rank= json.optString("rank");
	}
}
