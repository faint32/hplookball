package com.hupu.games.data;

import org.json.JSONObject;
/** 
 * *球队bean
 * */
public class TeamEntity extends BaseEntity {

	/***/
	public int i_tid;
	public String str_name_cn;
	public String str_name_en;
	public int i_color;
	

	@Override
	public void paser(JSONObject obj) throws Exception{
		i_tid=obj.optInt(KEY_TEAM_ID);
		str_name_cn =obj.optString(KEY_NAME, null);
		str_name_en =obj.optString(KEY_EN_NAME, null);
		i_color =obj.optInt(KEY_COLOR);
	}
}
