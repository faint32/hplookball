package com.hupu.games.data.game.basketball;

import org.json.JSONObject;

import com.hupu.games.data.BaseLiveResp;

public class NBALiveResp extends BaseLiveResp {

	
	public int allCount;
	public int pageCount;
	public int page;
	public String people_num;
	

	@Override
	public void paser(JSONObject obj) throws Exception {
		// Log.d("LiveResp", obj.toString());
		if (obj.has(KEY_PID)) {
			i_pId = obj.optInt(KEY_PID, -1);
			people_num = obj.optString("online", null);
			obj = obj.getJSONObject(KEY_RESULT);

		} else {
			obj = obj.getJSONObject(KEY_RESULT);
			i_pId = obj.optInt(KEY_PID, -1);
			tvLink = obj.optString(KEY_TV_LINK, "");
			follow =obj.optInt("follow",-1);
		}

		super.paser(obj);

	}

	
}
