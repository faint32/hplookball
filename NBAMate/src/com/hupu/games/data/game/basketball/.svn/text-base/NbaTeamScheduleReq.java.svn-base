package com.hupu.games.data.game.basketball;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.common.HupuLog;
import com.hupu.games.data.BaseEntity;

public class NbaTeamScheduleReq extends BaseEntity {

	public int tid; // 球队id
	public String name; // 中文短名称
	public String full_name; // 中文全名称
	public int anchor;

	public String[] blocks;

	public HashMap<String, ArrayList<NbaTeamDataEntity>> mDataMap;
	public int pos;
	@Override
	public void paser(JSONObject json) throws Exception {

		JSONObject obj = json.getJSONObject(KEY_RESULT);
		if (obj != null) {
			JSONObject temp = obj.optJSONObject("info");
			if (temp != null) {
				name = temp.optString("name");
				full_name = temp.optString("full_name");
			}
			anchor = obj.optInt("anchor");
			JSONArray arr = obj.optJSONArray("schedule");
			if (arr != null) {
				int size = arr.length();
				blocks = new String[size];
				NbaTeamDataEntity data;
				ArrayList<NbaTeamDataEntity> mDataList;
				mDataMap = new HashMap<String, ArrayList<NbaTeamDataEntity>>();
				JSONArray arr1;
				int index=0;
				for (int i = 0; i < size; i++) {
					temp = arr.getJSONObject(i);
					blocks[i] = temp.optString("block");
					mDataList = new ArrayList<NbaTeamDataEntity>();
					mDataMap.put(blocks[i], mDataList);
					arr1 = temp.optJSONArray("data");
					int size1 = arr1.length();
					for (int j = 0; j < size1; j++) {
						data = new NbaTeamDataEntity();
						data.paser(arr1.getJSONObject(j));
						mDataList.add(data);
//						HupuLog.d("gid="+data.gid+"  index="+index);
						if(data.gid == anchor)
							pos=index; 
//						data.side+=index;
						index++;
					}
				}
				HupuLog.d("pos====="+pos);
			}
			
		}
	}

}
