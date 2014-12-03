package com.hupu.games.data.game.football;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class SoccerTeamReq extends BaseEntity {

	public ArrayList<SoccerTeamDataEntity> mDataList;

	public int tid; // 球队id
	public String name; // 中文短名称
	public String full_name; // 中文全名称

	public String market_values;// 34胜17负 总战绩
	public String intro;// 球队介绍
	public String rank;// 西部第5
	public String logo;//

 	
	@Override
	public void paser(JSONObject json) throws Exception {

		JSONObject obj = json.getJSONObject(KEY_RESULT);
		if (obj != null) {
			JSONObject temp = obj.optJSONObject("info");
			if (temp != null) {
				name = temp.optString("name");
				full_name = temp.optString("full_name");
				market_values = temp.optString("market_values");
				intro= temp.optString("intro","");
				rank = temp.optString("rank");
				logo = temp.optString("logo");
			}

				
			
			
			JSONArray arr =obj.optJSONArray("schedule");
			if(arr !=null)
			{
				int size =arr.length();
				mDataList=new ArrayList<SoccerTeamDataEntity> ();
				SoccerTeamDataEntity data ;
				for(int i =0;i<size;i++)
				{
					data =new SoccerTeamDataEntity();
					data.paser(arr.getJSONObject(i));
					mDataList.add(data);
				}
			}
		}
	}

}
