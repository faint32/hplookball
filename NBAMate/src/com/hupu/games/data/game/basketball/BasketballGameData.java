package com.hupu.games.data.game.basketball;

import java.io.Serializable;

import org.json.JSONObject;

import com.hupu.games.data.BaseGameEntity;

@SuppressWarnings("serial")
public class BasketballGameData extends BaseGameEntity implements Serializable {

	public int round;

	public int byt_status;
	// public String date;
	public String str_process;

	public final static byte STATUS_START = 2;
	public final static byte STATUS_END = 4;
	public final static byte STATUS_WAITING = 1;
	public final static byte STATUS_CANCEL = 6;
	public final static int STATUS_DELAY = 5;

	/** 属于哪一块 */
	public String block;

	@Override
	public void paser(JSONObject json) throws Exception {
		if (json.has(KEY_RESULT))
			json = json.optJSONObject(KEY_RESULT);
		i_gId = json.optInt(KEY_GAME_ID);
		// System.out.println("gameId="+i_gId );
		l_begin_time = json.optLong(KEY_BEGIN_TIME, -1);
		// date=json.optString(KEY_DATE);
		i_home_tid = json.optInt(KEY_HOME_TID, -1);
		str_home_name = json.optString(KEY_HOME_NAME, null);
		home_logo = json.optString("home_logo", null);
		i_home_score = json.optInt(KEY_HOME_SCORE, 0);
		i_away_tid = json.optInt(KEY_AWAY_TID, -1);
		str_away_name = json.optString(KEY_AWAY_NAME, null);
		i_away_score = json.optInt(KEY_AWAY_SCORE, 0);
		away_logo = json.optString("away_logo", null);
		str_process = json.optString(KEY_PROCESS, null);
		// 推送数据没有block
		block = json.optString("block", "socket");
		// byt_status=(byte)json.optInt(KEY_STATUS);
		if (json.has(KEY_STATUS))
			byt_status = (byte) json.optJSONObject(KEY_STATUS).optInt("id");
		bFollow = (byte) json.optInt(KEY_FOLLOW, 0);
		i_live = json.optInt("live", 0);
		casino = json.optInt("casino", 0);
	}

	public void update(BasketballGameData source) {
		if (source.l_begin_time > 0)
			l_begin_time = source.l_begin_time;
		if (i_home_score > 0)
			i_home_score = source.i_home_score;
		if (source.i_away_score > 0)
			i_away_score = source.i_away_score;
		if (source.byt_status > 0)
			byt_status = source.byt_status;
		if (source.str_process != null)
			str_process = source.str_process;
	}
}
