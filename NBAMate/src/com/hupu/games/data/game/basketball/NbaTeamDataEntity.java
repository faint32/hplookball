package com.hupu.games.data.game.basketball;

import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class NbaTeamDataEntity extends BaseEntity {

	public int gid;//	比赛id
 	public String begin_time;//	12月31日 周四 （比赛时间）
 	public String side;//	主/客 （主客场）
 	public String score;//	101-89/vs/@ (主场对应vs，客场对应@)
 	public int vs_tid;//	对阵球队id
 	public String vs_team_name;//	奇才 (对阵球队名称)
 	public String is_win;//	负/胜/9:30 如果比赛未结束则未开始时间
 	public int status;//	比赛状态（1：已结束，2:进行中，3：未开始，4：已取消）
 	
	@Override
	public void paser(JSONObject json) throws Exception {
		
		gid=json.optInt("gid");
	 	begin_time=json.optString("begin_time");
	 	side=json.optString("side");//	
	 	score=json.optString("score");//	
	 	vs_tid=json.optInt("vs_tid");
	 	vs_team_name=json.optString("vs_team_name");//	
	 	is_win=json.optString("is_win");//	
	 	status=json.optInt("status");
	}

}
