/**
 * 
 */
package com.hupu.games.data.game.football;

import java.io.Serializable;

import org.json.JSONObject;

import com.hupu.games.data.BaseGameEntity;

/**
 * @author panyongjun
 *
 */
public class ScoreboardEntity extends BaseGameEntity implements Serializable{

	public int key;
	
	
	//点球
	public int home_out_goals;
	public int away_out_goals;
	
    public String home_logo;
    public String away_logo;
    
	public String date;
	public String str_desc;
	
	public int process;// 比赛已开始时间（秒）
	

	public byte show_desc;
	
	//1：上半场，2，下半场，3中场休息，4下半场结束，5加时赛上，6加时赛下，7加时赛结束，8点球大战
    public byte period;
	
	// 状态数字（1：未开始，2：比赛中，4：比赛结束，5：延期，6：取消，8：主场禁赛，9：取消）
	public byte code;
	public int is_extra;
	public final static byte STATUS_NOT_START = 1;
	public final static byte STATUS_START = 2;
	public final static byte STATUS_END = 4;
	public final static byte STATUS_EXTEND = 5;
	public final static byte STATUS_CANCEL = 6;
	
	@Override
	public void paser(JSONObject json) throws Exception {
		if(json == null )
			return;
		if(json.has(KEY_RESULT))
			json=json.optJSONObject(KEY_RESULT);
		i_gId = json.optInt(KEY_GAME_ID);
		// System.out.println("gameId="+i_gId );
		l_begin_time = json.optLong(KEY_BEGIN_TIME);
		date = json.optString(KEY_DATE);
		i_home_tid = json.optInt(KEY_HOME_TID);
		str_home_name = json.optString(KEY_HOME_NAME, null);
		i_home_score = json.optInt(KEY_HOME_SCORE);
		
		home_out_goals = json.optInt("home_out_goals");
		away_out_goals= json.optInt("away_out_goals");
		i_away_tid = json.optInt(KEY_AWAY_TID);
		str_away_name = json.optString(KEY_AWAY_NAME);
		i_away_score = json.optInt(KEY_AWAY_SCORE);
		
	    home_logo= json.optString("home_logo");
	    away_logo= json.optString("away_logo");


		process = json.optInt(KEY_PROCESS);
		bFollow = (byte) json.optInt(KEY_FOLLOW);
		
		show_desc =(byte)json.optInt("show_title");
		period= (byte) json.optInt("period");
		//android 巴拉克阶段主播改造（6.2.0/5.2.0）及以后替代 live 作用 
		i_live =json.optInt("live_status",0);
		is_extra= json.optInt("is_extra", 0);
		casino =json.optInt("casino",0);
		default_tab=json.optString("default_tab",null);
		json = json.optJSONObject(KEY_STATUS);
		if (json != null) {
			str_desc = json.optString(KEY_DESC);
			code = (byte) json.optInt("id");
		}

	}

}
