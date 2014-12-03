package com.hupu.games.data.game.basketball;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class NbaTeamReq extends BaseEntity {

	public ArrayList<NbaTeamDataEntity> mDataList;

	public int tid; // 球队id
	public String name; // 中文短名称
	public String full_name; // 中文全名称

	public String record;// 34胜17负 总战绩
	public String home_record;// 主场:21胜7负
	public String away_record;// 客场:13胜10负
	public String rank;// 西部第5

	@Override
	public void paser(JSONObject json) throws Exception {

		JSONObject obj = json.getJSONObject(KEY_RESULT);
		if (obj != null) {
			JSONObject temp = obj.optJSONObject("info");
			if (temp != null) {
				name = temp.optString("name");
				full_name = temp.optString("full_name");
			}
			temp = obj.optJSONObject("standing");
			if (temp != null) {
				record = temp.optString("record");
				home_record = temp.optString("home_record");
				away_record = temp.optString("away_record");
				rank = temp.optString("rank");
			}
			
			JSONArray arr =obj.optJSONArray("schedule");
			if(arr !=null)
			{
				int size =arr.length();
				mDataList=new ArrayList<NbaTeamDataEntity> ();
				NbaTeamDataEntity data ;
				for(int i =0;i<size;i++)
				{
					data =new NbaTeamDataEntity();
					data.paser(arr.getJSONObject(i));
					mDataList.add(data);
				}
			}
		}
	}

}
