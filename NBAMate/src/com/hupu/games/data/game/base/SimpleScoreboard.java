package com.hupu.games.data.game.base;

import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class SimpleScoreboard extends BaseEntity {

	public int i_scoreHome = -1;
	public int i_scoreAway = -1;

	public String str_process;
	
	public int period;
	
	public int live;
	
	public int away_out_goals;
	
	public int home_out_goals;
	
	public int is_extra;
	
	public int id;
	
	public String desc;
	@Override
	public void paser(JSONObject o) throws Exception {
		
		i_scoreHome = o.optInt(KEY_HOME_SCORE, -1);
		i_scoreAway = o.optInt(KEY_AWAY_SCORE, -1);
		str_process = o.optString(KEY_PROCESS, null);
		home_out_goals = o.optInt("home_out_goals",-1);
		away_out_goals= o.optInt("away_out_goals",-1);
		period  = o.optInt("period", -1);
		live= o.optInt("live", 0);
		is_extra= o.optInt("is_extra", 0);
	    JSONObject status =o.optJSONObject(KEY_STATUS);
	    if(status !=null)
	    {
	    	id =status.optInt("id", 0);
	    	desc =status.optString("desc");
	    }
	
	}

}
