package com.hupu.games.data.game.basketball;

import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class NbaTeamPlayerEntity extends BaseEntity {

	public int player_id;//	球员ID
 	public String player_name;//	球员名称
 	public String player_header;//	头像
 	public String number;//	号码
 	public String position;//位置
 	public String salary;//年薪
 	
	@Override
	public void paser(JSONObject json) throws Exception {
		
		player_id=json.optInt("player_id");
		player_name=json.optString("player_name");
		player_header=json.optString("player_header");//	
		number=json.optString("number");
		position=json.optString("position");//	
		salary=json.optString("salary");//	
	}

}
