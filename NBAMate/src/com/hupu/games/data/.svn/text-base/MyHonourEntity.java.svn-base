package com.hupu.games.data;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyHonourEntity extends BaseEntity {

	public LinkedList<MyHonourEntity> myList;
	public String gid;
	public String home_name;
	public String away_name;
	public String match_date;
	public LinkedList<HonourEntity> hList;
	
	@Override
	public void paser(JSONObject json) throws Exception {
		//json =json.optJSONObject("result");
		JSONArray array = json.optJSONArray("result");
		if (array != null) {
			int size = array.length();
			myList = new LinkedList<MyHonourEntity>();
			MyHonourEntity temp;
			for (int i = 0; i < size; i++) {
				temp = new MyHonourEntity();
				temp.paser(array.getJSONObject(i));
				myList.add(temp);
			}
		}
		JSONArray ranArray = json.optJSONArray("rank_list");
		if(ranArray!=null){
			hList=new LinkedList<HonourEntity>();
			for (int i = 0; i < ranArray.length(); i++) {
				HonourEntity entity=new HonourEntity();
				JSONObject hJson=ranArray.getJSONObject(i);
				entity.socre =hJson.optInt("socre");
				entity.name= hJson.optString("name");
				entity.rank= hJson.optString("rank");
			//	entity.paser(ranArray.getJSONObject(i));
				hList.add(entity);
			}
		}
		gid= json.optString("gid");
		home_name= json.optString("home_name");
		away_name= json.optString("away_name");
		match_date=json.optString("match_date");
	}
}
