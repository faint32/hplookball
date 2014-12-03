package com.hupu.games.data;

import org.json.JSONArray;
import org.json.JSONObject;


public class ObtainEntity extends BaseEntity {

	public JSONArray array;
	public String give;
	//List<String> array=new ArrayList<String>();
	@Override
	public void paser(JSONObject json) throws Exception {
		json=json.getJSONObject("result");
		give=json.getString("give");
		array =json.getJSONArray("list");
//		for(int i=0;i<array.length();i++){
//			array.getString(i);
//		}
//		array
	}

}
