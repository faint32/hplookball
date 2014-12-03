package com.hupu.games.data.task;

import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class TaskRewardEntity extends BaseEntity {

	public String id = ""; //  领取奖励返回状态
	
	public String text = ""; //领取返回的提示信息
	
	@Override
	public void paser(JSONObject json) throws Exception {
		// TODO Auto-generated method stub
		
		JSONObject mJson = json.optJSONObject(KEY_RESULT);
		
		if(mJson!=null)
		{		
			id = mJson.has("id")?mJson.optString("id"):"";
			text = mJson.has("text")?mJson.optString("text"):"";
		}

	}

}
