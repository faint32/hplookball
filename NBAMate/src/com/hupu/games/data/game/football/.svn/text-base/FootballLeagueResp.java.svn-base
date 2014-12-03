package com.hupu.games.data.game.football;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.common.HuPuRes;
import com.hupu.games.data.BaseEntity;
import com.hupu.games.data.game.basketball.BasketballGameEntity;
import com.hupu.games.data.game.basketball.BasketBallGamesBlock;

public class FootballLeagueResp extends BaseEntity {

	public BasketballGameEntity gameEntity;

	public ArrayList<SoccerGamesBlock> mBlockList;

	public ArrayList<String> mDays;
	public ArrayList<String> mBlockTypes;
	public int isLogin;

	/** 下拉刷新需要 */
	public int min;// 20140919,
	/** 最大 */
	public int max;// 20150416,
	/** 上拉刷新需要 */
	public int current;// 20140922,

	public int anchor;// "9990101"

	public int firstDay;

	public int lastDay;

	public int refresh_time;

	public int anchorIndex;

	public int total;
	@Override
	public void paser(JSONObject obj) throws Exception {
		if (obj.isNull("is_login")) {
			isLogin = 1;
		} else {
			isLogin = obj.optInt("is_login");
		}

		if (obj.has("settings")) {
			refresh_time = obj.getJSONObject("settings").optInt("refresh_time");
		}
		JSONArray arr = null;
		obj = obj.getJSONObject(KEY_RESULT);
		arr = obj.getJSONArray("games");
		JSONObject days = obj.optJSONObject("days");
		if (days != null) {
			min = days.optInt("min");
			max = days.optInt("max");
			current = days.optInt("current");
			anchor = days.optInt("anchor");
		}

		int size = arr.length();

		if (size > 0) {
			mBlockList = new ArrayList<SoccerGamesBlock>();
			mDays = new ArrayList<String>();
			mBlockTypes = new ArrayList<String>();
			SoccerGamesBlock entity;
			for (int i = 0; i < size; i++) {

				entity = new SoccerGamesBlock();
				entity.anchor = anchor;
				entity.paser(arr.getJSONObject(i));
				mBlockList.add(entity);
				mDays.add("" + entity.mDay);
				mBlockTypes.add(entity.mType);
				
				if (i == 0)
					firstDay = entity.mDay;
				if (i == size - 1)
					lastDay = entity.mDay;
				if (entity.anchorIndex > -1)
					anchorIndex = total + i + entity.anchorIndex + 2;// 锚点的坐标等于块数+该比赛的索引 + 下拉刷新的item
				total += entity.mGames.size();
			}
		}

	}

}
