package com.hupu.games.data.game.football;

import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;
/**@author panyongjun 足球事件直播数据bean*/
public class FootballEventData extends BaseEntity {
	
	public int live_time; //  比赛开始分钟
	
	public String player_name;//   球员名字
	
	public int eid; //   事件编号，客户端需要根据编号处理相应事件
	
	public int id;
	//2//黄牌,3//红牌,10//比赛结束,11//进球,17，30 点球射入 21 比赛开赛 22 换人 ,28 乌龙
	
	public static final int EVENT_YELLOW_CARD=2;
	public static final int EVENT_RED_CARD=3;
	public static final int EVENT_GAME_END=10;
	public static final int EVENT_GOAL=11;
	public static final int EVENT_PENALTY_GOAL=17;//点球大战进球
	public static final int EVENT_SHOOT_OUT_GOAL=30;
	public static final int EVENT_GAME_START=21;
	public static final int EVENT_SUBSTITUTION=22;
	public static final int EVENT_OWN_GOAL=28;
	public static final int EVENT_SHOOT_OUT_MISS=18;
	
	public int half_id;   //（1：上半场，2：下半场）
	
	public String rel_alias;//    换上球员名
	
	public String player_id;//    
	public String rel_id;//    换上球员id
	
	
	

    public String score;
	
	public int tid;
	//事件类型 0 公共消息 1主队 2客队
	public byte eventType;
	
	public String desc;
	@Override
	public void paser(JSONObject json) throws Exception {
		live_time=json.optInt("live_time");
	
		eid=json.optInt("eid");  
		half_id =json.optInt("half_id");
		
		 
		
		JSONObject event =json.optJSONObject("event");
		if(event !=null)
		{
			id =event.optInt("id");
			score =event.optString("live_goals",null);
			tid  =event.optInt("tid");
			player_name =event.optString("player_name",null); 
			player_id =event.optString("player_id"); 
			rel_alias =event.optString("rel_player_name");
			rel_id =event.optString("rel_player_id");
			desc =event.optString("desc"); 
		}

	}

}
