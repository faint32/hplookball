package com.hupu.games.data.game.basketball;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class NbaPlayersDataReq extends BaseEntity {

	public ArrayList<PlayerDataEntity> mDataList;

	public int tid; // 球队id
	public String name; // 中文短名称
	public String full_name; // 中文全名称

	public String [] headerKeys;
	public String [] headerValues;
	
	@Override
	public void paser(JSONObject json) throws Exception {

		JSONObject obj = json.getJSONObject(KEY_RESULT);
		if (obj != null) {
			JSONObject temp = obj.optJSONObject("info");
			if (temp != null) {
				name = temp.optString("name");
				full_name = temp.optString("full_name");
			}

			JSONArray arr =obj.optJSONArray("players_stats_glossary");
			if(arr !=null)
			{
				//解析字典
				JSONArray arr1 =arr.getJSONArray(0);
				JSONArray arr2 =arr.getJSONArray(1);
				int size =arr1.length();
				headerKeys =new String[size];
				headerValues=new String[size];
				for(int i=0;i<size;i++)
				{
					headerKeys[i]= arr1.getString(i);
					headerValues[i]= arr2.getString(i);
				}
			}
			
			arr =obj.optJSONArray("players_stats");
			if(arr !=null)
			{
				//解析数据
				int size =arr.length();
				mDataList=new ArrayList<PlayerDataEntity> ();
				PlayerDataEntity data ;
				for(int i =0;i<size;i++)
				{
					data =new PlayerDataEntity();
					data.paser(arr.getJSONObject(i));
					mDataList.add(data);
				}
			}
		
		}
	}

	public  class PlayerDataEntity extends BaseEntity
	{
		public int player_id;//	球员id
	 	public String values[];
		@Override
		public void paser(JSONObject json) throws Exception {
			player_id=json.optInt("player_id");
			values=new String [headerKeys.length];
			for(int i =0;i<values.length;i++)
			{
				values[i]=json.optString(headerKeys[i]);
			}
		}
	}
}
