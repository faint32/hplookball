package com.hupu.games.data.game.football;

import org.json.JSONObject;

import com.hupu.games.data.BaseLiveResp;

public class SoccerLiveResp extends BaseLiveResp {

	public String online;
	public int period;
	@Override
	public void paser(JSONObject obj) throws Exception {
		online =obj.optString("online",null);
		if (obj.has(KEY_PID)) {
			i_pId = obj.optInt(KEY_PID, -1);
			obj = obj.getJSONObject(KEY_RESULT);
			period=obj.optInt("period", -1);
		} else {
			obj = obj.getJSONObject(KEY_RESULT);
			i_pId = obj.optInt(KEY_PID, -1);
			tvLink = obj.optString(KEY_TV_LINK, "");
			period=obj.optInt("period", -1);
		}
		
		super.paser(obj);
	}
}
