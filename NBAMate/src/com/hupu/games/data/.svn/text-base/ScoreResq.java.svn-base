package com.hupu.games.data;

import org.json.JSONObject;

public class ScoreResq extends BaseEntity {

	public String title;    //排行标题
	public String alias;    //排行英文别名
	

	
	@Override
	public void paser(JSONObject json) throws Exception {
		json =json.optJSONObject(KEY_RESULT);
		title =json.optString(KEY_TITLE);
		alias =json.optString("alias");
		//排行数据
		json = json.optJSONObject("ranks");
		  
	}

}
