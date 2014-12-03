package com.hupu.games.data.game.basketball;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class NbaTeamPlayerReq extends BaseEntity {

	public ArrayList<NbaTeamPlayerEntity> mDataList;

	public int tid; // 球队id
	public String name; // 中文短名称
	public String full_name; // 中文全名称
	
	@Override
	public void paser(JSONObject json) throws Exception {

		JSONObject obj = json.getJSONObject(KEY_RESULT);
		if (obj != null) {
			JSONObject temp = obj.optJSONObject("info");
			if (temp != null) {
				name = temp.optString("name");
				full_name = temp.optString("full_name");
			}
			
			JSONArray arr =obj.optJSONArray("list");
			if(arr !=null)
			{
				int size =arr.length();
				mDataList=new ArrayList<NbaTeamPlayerEntity> ();
				NbaTeamPlayerEntity data ;
				for(int i =0;i<size;i++)
				{
					data =new NbaTeamPlayerEntity();
					data.paser(arr.getJSONObject(i));
					mDataList.add(data);
				}
			}
		}
	}

}
