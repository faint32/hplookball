package com.hupu.games.data;

import java.io.Serializable;

import org.json.JSONObject;
/**
 * 球队间比赛基类bean,
*/
@SuppressWarnings("serial")
public abstract class BaseGameEntity extends BaseEntity implements Serializable{

	/**
	 * 比赛id
	 * */
	public int i_gId;

	/**
	 * 开始时间
	 * */
	public long l_begin_time;
//	public long l_date_time;
	/**
	 * 主队球队ID
	 * */
	public int i_home_tid;
	public String str_home_name;
	public int i_home_score;
	/**
	 * 客队球队ID
	 * */
	public int i_away_tid;
	public String str_away_name;
	public int i_away_score;
		
	public byte bFollow;
	
	/**live*/
	public int i_live;
	
	/**比赛类型 ，目前有nba，cba，欧联，欧冠*/
    public int mode;
    
	public String home_logo;
	public String away_logo;
	
	public int casino;
	
    public String default_tab;
}
