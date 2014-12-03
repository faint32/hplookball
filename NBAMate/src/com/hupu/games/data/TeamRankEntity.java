package com.hupu.games.data;

import java.io.Serializable;

import org.json.JSONObject;

@SuppressWarnings("serial")
public class TeamRankEntity extends BaseEntity implements Serializable{

	public int i_tid;    //球队ID
	public String str_name;//    球队名
	public int i_win;   // 胜场数
	public int i_lost;   //败场数
	public String str_win_rate; //  胜率
	public String str_strk;    //近况
	public String i_gb;   // 胜场差
	
	public String i_pf;   // 得分
	public String i_pa;   // 失分
	@Override
	public void paser(JSONObject json) throws Exception {
		i_tid=json.optInt(KEY_TEAM_ID);    
		str_name=json.optString(KEY_NAME);    
		i_win=json.optInt("win");   
		i_lost=json.optInt("lost");   
		str_win_rate =json.optString("win_rate");    
		str_strk =json.optString("strk");    
		i_gb =json.optString("gb");   
		
		i_pf =json.optString("pf");   
		i_pa =json.optString("pa");   

	}

}
