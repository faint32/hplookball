package com.hupu.games.data.game.basketball;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.MatchEntity;
import com.hupu.games.data.PlayerEntity;
import com.hupu.games.data.personal.box.BoxScoreResp;

@SuppressWarnings("serial")
public class CBABoxScoreResp extends BoxScoreResp implements Serializable {

	/** 解析每个球员数据 */
	private void paserPlayer(JSONArray arr, LinkedList<PlayerEntity> list)
			throws Exception {
		int size = 0;
		PlayerEntity entity;
		Set<String> set = null;
		if (mMapGlossary != null)
			set = mMapGlossary.keySet();
		if (arr != null) {
			size = arr.length();
			for (int i = 0; i < size; i++) {
				entity = new PlayerEntity();
				entity.paser(arr.getJSONObject(i), set);
				list.add(entity);
			}
		}
		set = null;
	}

	@Override
	public void paser(JSONObject obj) throws Exception {
		obj = obj.getJSONObject(KEY_RESULT);
		if (obj.has(KEY_SCORE_BOARD)) {
			// 比分牌数据
			JSONObject o = obj.getJSONObject(KEY_SCORE_BOARD);
			i_scoreHome = o.optInt(KEY_HOME_SCORE, -1);
			i_scoreAway = o.optInt(KEY_AWAY_SCORE, -1);
			str_process = o.optString(KEY_PROCESS, null);
		}
		i_bId = obj.optInt("bid");
		if (obj.has(KEY_DATA)) {
			// socket 接口
			obj = obj.getJSONObject(KEY_DATA);
		}
		// 解析服务器返回的列表字段
		if (obj.has(KEY_GLOSSARY)) {
			mMapGlossary = new LinkedHashMap<String, String>();
			paserKeys(obj.getJSONArray(KEY_GLOSSARY), mMapGlossary);
		}

		if(obj.has("vertical"))
		{
			mMapPortrait= new LinkedHashMap<String, String>();
			paserKeys(obj.getJSONArray("vertical"), mMapPortrait);
		}
		
		// 主客队得分解析
		JSONObject oo = obj.optJSONObject(KEY_MATCH_STATS);
		if (oo != null) {
			if (oo.has(KEY_HOME)) {
				mEntityHome = new MatchEntity();
				mEntityHome.paser(oo.optJSONObject(KEY_HOME));
			}
			if (oo.has(KEY_AWAY)) {
				mEntityAway = new MatchEntity();
				mEntityAway.paser(oo.optJSONObject(KEY_AWAY));
			}
			oo = null;
		}
		// 球员比分解析
		JSONObject player = obj.optJSONObject("player");		
		JSONObject home = player.optJSONObject("home");
		mListPlayers = new ArrayList<PlayerEntity>();

		JSONArray arr = home.optJSONArray("start");
		if (arr != null)
			paserPlayer(arr, mListPlayers);

		arr = home.optJSONArray("reserve");
		if (arr != null)
			paserPlayer(arr, mListPlayers);
		i_homePlaySize = mListPlayers.size();
		home = null;

		// 客队
		JSONObject away = player.optJSONObject("away");
		arr = away.optJSONArray("start");
		if (arr != null)
			paserPlayer(arr, mListPlayers);
		arr = away.optJSONArray("reserve");
		if (arr != null)
			paserPlayer(arr, mListPlayers);
		arr = null;
		player = null;

		// 解析球队总计
		JSONObject total = obj.optJSONObject("total");
		homeTotals = total.optJSONObject("home");
		awayTotals = total.optJSONObject("away");
		total = null;

		JSONObject percentages = obj.optJSONObject("percentages");
		home = percentages.optJSONObject("home");
		if (home != null) {
			str_home_fg = home.optString("2p", null);
			str_home_tp = home.optString("3p", null);
			str_home_ft = home.optString("ft", null);
			home = null;
		}

		away = percentages.optJSONObject("away");
		if (away != null) {
			str_away_fg = away.optString("2p", null);
			str_away_tp = away.optString("3p", null);
			str_away_ft = away.optString("ft", null);
			away = null;
		}
	}


}
