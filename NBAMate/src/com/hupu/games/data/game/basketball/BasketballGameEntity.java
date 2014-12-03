package com.hupu.games.data.game.basketball;

import java.io.Serializable;

import org.json.JSONObject;

import com.hupu.games.data.BaseGameEntity;
/**
 * 球队间比赛bean,
 * {"gid":"21994","date_time":"1349625600","begin_time":"1349625600","home_tid":"189",
 * "home_name":" \u7c73\u5170","home_score":"0","away_tid":"1","away_name":"\u51ef\u5c14\u7279\u4eba",
 * "away_score":"0","match_type":"2","process":"","status":"3"
 * */
@SuppressWarnings("serial")
public class BasketballGameEntity extends BaseGameEntity implements Serializable{


	public long l_date_time;

	/**常规赛*/
	public final static String TYPR_REGULAR="REGULAR";
    /**PLAYOFF：季后赛*/
	public final static String TYPR_PLAYOFF="PLAYOFF";
	/**PRESEASON：季前赛*/
	public final static String TYPR_PRESEASON="PRESEASON";
	public String str_match_type;
	
	public final static byte TYPE_REGULAR=1;
	public final static byte TYPE_PLAYOFF=2;
	public final static byte TYPE_PRESEASON=3;

	/**比赛进程说明*/
	public String str_process;
	/**状态*/
	public byte byt_status;


	
	public final static byte STATUS_START=2;
	public final static byte STATUS_END=1;
	public final static byte STATUS_WAITING=3;
	public final static byte STATUS_CANCEL=4;
	
	/**for cba*/
	public final static byte CBA_STATUS_START=2;
	public final static byte CBA_STATUS_END=4;
	public final static byte CBA_STATUS_WAITING=1;
	public final static byte CBA_STATUS_CANCEL=6;
	public final static int  CBA_STATUS_DELAY = 5;
	
	/**季后赛比方*/
	public int home_series;
	public int away_series;
	
    public String round;
	
   
	@Override
	public void paser(JSONObject json) throws Exception {
		
		if(json.has(KEY_RESULT))
			json=json.optJSONObject(KEY_RESULT);
		i_gId =json.optInt(KEY_GAME_ID);
//		System.out.println("gameId="+i_gId );
		l_begin_time=json.optLong(KEY_BEGIN_TIME);
		l_date_time=json.optLong(KEY_DATE_TIME);
		i_home_tid=json.optInt(KEY_HOME_TID);
		str_home_name=json.optString(KEY_HOME_NAME, null);
		i_home_score=json.optInt(KEY_HOME_SCORE);
		i_away_tid=json.optInt(KEY_AWAY_TID);
		
		home_series=json.optInt("home_series",-1);
		away_series=json.optInt("away_series",-1);
		
		str_away_name=json.optString(KEY_AWAY_NAME);
		i_away_score=json.optInt(KEY_AWAY_SCORE);
		str_match_type=json.optString(KEY_MATCH_TYPE);
		str_process=json.optString(KEY_PROCESS);
		JSONObject st =json.optJSONObject(KEY_STATUS);
		if(st!=null)
		{
			//cba
			byt_status=(byte)st.optInt("id");
			round= json.optString("round",null);
		}
		else
			byt_status=(byte)json.optInt(KEY_STATUS);
		bFollow =(byte)json.optInt(KEY_FOLLOW,0);
//		i_live =json.optInt("live",-1);
		//android 巴拉克阶段主播改造（6.2.0/5.2.0）及以后替代 live 作用 
		i_live =json.optInt("live_status",-1);
		casino=json.optInt("casino",0);
		default_tab=json.optString("default_tab",null);
		home_logo = json.optString("home_logo",null);
		away_logo = json.optString("away_logo",null);

	}
	
	public void update(BasketballGameEntity source)
	{
		if(source.l_begin_time>0)
			l_begin_time=source.l_begin_time;
		if(source.i_home_score>0)
			i_home_score=source.i_home_score;
		if(source.i_away_score>0)
			i_away_score=source.i_away_score;
		if(source.byt_status>0)
			byt_status=source.byt_status;
		if(source.str_process!=null)
			str_process=source.str_process;
	}
}
