package com.hupu.games.data;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

public class SingleHonourEntity extends BaseEntity {
	public LinkedList<SingleHonourEntity> mList;
	public int rank;
	public int score;
	public String nick_name;
	public String name;
	public int is_my;
	public LinkedList<SingleHonourEntity> hList;
	public SingleHonourEntity myEntity;
	@Override
	public void paser(JSONObject json) throws Exception {
		//json =json.optJSONObject("result");
		JSONArray array = json.optJSONArray("result");
		if (array != null) {
			int size = array.length();
			mList = new LinkedList<SingleHonourEntity>();
			SingleHonourEntity temp;
			for (int i = 0; i < size; i++) {
				temp = new SingleHonourEntity();
				temp.paser(array.getJSONObject(i));
				mList.add(temp);
			}
		}
		JSONObject myJson =json.optJSONObject("my");
		if(myJson!=null){
			myEntity=new SingleHonourEntity();
			myEntity.paser(myJson);
		}
		JSONArray hArray = json.optJSONArray("list");
		if (hArray != null) {
			int size = hArray.length();
			hList = new LinkedList<SingleHonourEntity>();
			SingleHonourEntity temp;
			for (int i = 0; i < size; i++) {
				temp = new SingleHonourEntity();
				temp.paser(hArray.getJSONObject(i));
				hList.add(temp);
			}
		}
		is_my=json.optInt("is_my");
		score =json.optInt("score");
		nick_name= json.optString("nick_name");
		rank= json.optInt("rank");
		name=json.optString("name");
	}
}
