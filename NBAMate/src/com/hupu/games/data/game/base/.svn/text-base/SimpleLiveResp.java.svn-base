package com.hupu.games.data.game.base;

import org.json.JSONObject;

import com.hupu.games.data.BaseLiveResp;

public class SimpleLiveResp extends BaseLiveResp {
	public String adUrl;
	public String adImg;
	public String online;
	public String people_num;
	@Override
	public void paser(JSONObject obj) throws Exception {
		online =obj.optString("online",null);
		if (obj.has(KEY_PID)) {
			i_pId = obj.optInt(KEY_PID, -1);
			people_num = obj.optString("online", null);
			obj = obj.getJSONObject(KEY_RESULT);

		} else {
			obj = obj.getJSONObject(KEY_RESULT);
			i_pId = obj.optInt(KEY_PID, -1);
			tvLink = obj.optString(KEY_TV_LINK, "");
			
			if (!obj.isNull("ad")) {
				JSONObject adJsonObj = obj.optJSONObject("ad"); 
				adImg = adJsonObj.optString("img");
				adUrl = adJsonObj.optString("url");
			}
		}
		
		super.paser(obj);
	}
}
