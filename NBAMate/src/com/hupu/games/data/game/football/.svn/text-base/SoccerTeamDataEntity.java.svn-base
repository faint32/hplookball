package com.hupu.games.data.game.football;

import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class SoccerTeamDataEntity extends BaseEntity {

	public int gid;//	比赛id
 	public String begin_time;//	12月31日 周四 （比赛时间）
 	public String score;//	vs (没有比分时显示vs)
 	public int vs_tid;//	对阵球队id
 	public int is_extra;//	是否加时 1 是 0 否
 	public String vs_team_name;//	奇才 (对阵球队名称)
 	public String vs_team_logo;
 	public String is_win;//	负/胜/9:30 如果比赛未结束则未开始时间
 	public int status;//	比赛状态（1：已结束，2:进行中，3：未开始，4：已取消）
 	public String penalty_score;//	1-5点球比分（为空则不显示）
 	public String stage;//小组赛（A组）、1/8强
    public String side;
    public String tag;
    public int lid;
 	
	@Override
	public void paser(JSONObject json) throws Exception {
		
		gid=json.optInt("gid");
	 	begin_time=json.optString("begin_time");
	
	 	penalty_score=json.optString("penalty_score",null);//	
	 	score=json.optString("score");//	
	 	vs_tid=json.optInt("vs_tid");
	 	is_extra=json.optInt("is_extra");
	 	vs_team_name=json.optString("vs_team_name");//	
	 	vs_team_logo=json.optString("vs_team_logo");//	
	 	is_win=json.optString("is_win");//	
	 	status=json.optInt("status");
	 	stage=json.optString("stage");//
	 	side=json.optString("side",null);//	
	 	tag=json.optString("en");//联赛tag
	 	lid=json.optInt("lid");//联赛id
	}

}
