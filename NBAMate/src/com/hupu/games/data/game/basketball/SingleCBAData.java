package com.hupu.games.data.game.basketball;

import java.io.Serializable;

import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

@SuppressWarnings("serial")
public class SingleCBAData extends BaseEntity implements Serializable{
	    public int i_gId;

		public int round;
	
		public int byt_status;

		public String str_content;
		
		public int i_home_score;
		
		public int i_away_score;
		/**是否有有效的比赛统计数据*/
		public int  bs    ;
	@Override
	public void paser(JSONObject json) throws Exception {
       json=json.getJSONObject(KEY_RESULT);

		i_gId =json.optInt(KEY_GAME_ID);
		round=json.optInt("round");
		i_home_score=json.optInt(KEY_HOME_SCORE);
		i_away_score=json.optInt(KEY_AWAY_SCORE);
		str_content  =json.optString(KEY_CONTENT);
		byt_status=(byte)json.optInt(KEY_STATUS);
		bs =json.optInt("bs");
	}

}
