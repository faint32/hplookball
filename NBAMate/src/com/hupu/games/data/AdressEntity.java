package com.hupu.games.data;

import org.json.JSONArray;
import org.json.JSONObject;

/** socket io 服务器地址解析*/
public class AdressEntity extends BaseEntity {

	public String[] mArrAdress;

	@Override
	public void paser(JSONObject json) throws Exception {
		// TODO Auto-generated method stub

	}

	public void paser(String json) throws Exception {

		JSONArray arr = new JSONArray(json);
		int size = arr.length();
		mArrAdress = new String[size];
		for (int i = 0; i < size; i++)
			mArrAdress[i] = arr.getString(i);
	}
}
