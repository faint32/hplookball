package com.hupu.games.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class TeamsResp extends BaseEntity {

	public ArrayList<TeamEntity> aList_Teams1;
//	@Override
//	public void paser(String json) throws Exception {
//		JSONArray arr=new JSONArray(json);
//		int size =arr.length();
//		TeamEntity temp;
//		for(int i =0;i<size;i++)
//		{
//			temp =new TeamEntity();
//			temp.paser(arr.getJSONObject(i));
//		}
//	}
	@Override
	public void paser(JSONObject json) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
