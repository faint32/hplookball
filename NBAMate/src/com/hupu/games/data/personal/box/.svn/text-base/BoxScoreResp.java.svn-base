package com.hupu.games.data.personal.box;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;
import com.hupu.games.data.MatchEntity;
import com.hupu.games.data.PlayerEntity;

@SuppressWarnings("serial")
public class BoxScoreResp extends BaseEntity implements Serializable {

	public MatchEntity mEntityHome;
	
	public MatchEntity mEntityAway;
	// public LinkedList<PlayerEntity> entity_home_start_player;
	// public LinkedList<PlayerEntity> entity_home_reserve_player;
	// public LinkedList<PlayerEntity> entity_away_start_player;
	// public LinkedList<PlayerEntity> entity_away_reserve_player;
	public ArrayList<PlayerEntity> mListPlayers;
	/** 一个key，一个中文名 */
	public  LinkedHashMap<String, String> mMapGlossary;
	
	public  LinkedHashMap<String, String> mMapPortrait;
	
	public int i_homePlaySize;
	// int awayPlaySize;
	public int i_scoreHome = -1;
	
	public int i_scoreAway = -1;

	public String str_process;

	public int i_bId;

//	public LinkedHashMap<String, String> mMapHomeTotal;
//
//	public LinkedHashMap<String, String> mMapAwayTotal;

	/** 解析每个球员数据 */
	public void paserPlayer(JSONArray arr, ArrayList<PlayerEntity> list)
			throws Exception {
		int size = 0;
		PlayerEntity entity;
		Set<String> set = null;
		if (mMapPortrait != null)
			set = mMapPortrait.keySet();
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
		// entity_home_start_player = new LinkedList<PlayerEntity>();
		mListPlayers = new ArrayList<PlayerEntity>();
		JSONArray arr = obj.optJSONArray(KEY_HOME_START_PLAYER);
		if (arr != null)
			paserPlayer(arr, mListPlayers);

		arr = obj.optJSONArray(KEY_HOME_RESERVE_PLAYER);
		if (arr != null)
			paserPlayer(arr, mListPlayers);
		i_homePlaySize = mListPlayers.size();
		// 客队
		// entity_away_start_player = new LinkedList<PlayerEntity>();

		arr = obj.optJSONArray(KEY_AWAY_START_PLAYER);
		if (arr != null)
			paserPlayer(arr, mListPlayers);
		arr = obj.optJSONArray(KEY_AWAY_RESERVE_PLAYER);
		if (arr != null)
			paserPlayer(arr, mListPlayers);
		arr = null;

		// 解析球队总计
		homeTotals = obj.optJSONObject("homeTotals");
		awayTotals = obj.optJSONObject("awayTotals");

		JSONObject homePercentages = obj.optJSONObject("homePercentages");
		if (homePercentages != null) {
			str_home_fg = homePercentages.optString("fg", null);
			str_home_tp = homePercentages.optString("tp", null);
			str_home_ft = homePercentages.optString("ft", null);
			homePercentages = null;
		}
		JSONObject awayPercentages = obj.optJSONObject("awayPercentages");
		if (awayPercentages != null) {
			str_away_fg = awayPercentages.optString("fg", null);
			str_away_tp = awayPercentages.optString("tp", null);
			str_away_ft = awayPercentages.optString("ft", null);
			awayPercentages = null;
		}
	}

	public JSONObject homeTotals;
	public JSONObject awayTotals;
	private LinkedList<String> mKeys;
	// 主队命中率
	public String str_home_fg;
	public String str_home_tp;
	public String str_home_ft;
	// 客队命中率
	public String str_away_fg;
	public String str_away_tp;
	public String str_away_ft;

}
