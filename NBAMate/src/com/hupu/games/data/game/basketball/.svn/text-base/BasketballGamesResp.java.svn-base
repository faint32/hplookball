package com.hupu.games.data.game.basketball;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.common.HuPuRes;
import com.hupu.games.data.BaseEntity;
import com.pyj.common.MyUtility;

public class BasketballGamesResp extends BaseEntity {

	public BasketballGamesResp(int t) {
		i_type = t;
	}

	/**
	 * 为0时是根据id获取单场比赛， 为1时是根据日期获取列表， 2 由服务器获取最近的比赛，并获取前后的日期 3
	 * 根据指定日期获取比赛列表，并获取前的日期 4 根据指定日期获取比赛列表，并获取后的日期 5实时接口
	 * */
	int i_type;

	public BasketballGameEntity gameEntity;

	public ArrayList<BasketBallGamesBlock> mBlockList;

	public ArrayList<String> mDays;
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
	
	public int anchorIndex;
	public int total;
	@Override
	public void paser(JSONObject obj) throws Exception {
		if (obj.isNull("is_login")) {
			isLogin = 1;
		} else {
			isLogin = obj.optInt("is_login");
		}

		if (i_type == HuPuRes.REQ_METHOD_NBA_GAMES_BY_GID) {
			gameEntity = new BasketballGameEntity();
			gameEntity.paser(obj.getJSONObject(KEY_RESULT));
		} else {
			JSONArray arr = null;
			obj = obj.getJSONObject(KEY_RESULT);
			arr = obj.getJSONArray("games");
			JSONObject days=obj.optJSONObject("days");
			if(days!=null)
			{
				min = days.optInt("min");
				max = days.optInt("max");
				current = days.optInt("current");
				anchor = days.optInt("anchor");
			}
		
			int size = arr.length();
		
			if (size > 0) {
				mBlockList = new ArrayList<BasketBallGamesBlock>();
				mDays = new ArrayList<String>();
				BasketBallGamesBlock entity;
				for (int i = 0; i < size; i++) {

					entity = new BasketBallGamesBlock();
					entity.anchor=anchor;
					entity.paser(arr.getJSONObject(i));
					mBlockList.add(entity);
					mDays.add("" + entity.mDay);
					if(i==0)
						firstDay=entity.mDay;
					if(i==size-1)
						lastDay=entity.mDay;
					if(entity.anchorIndex>-1)
						anchorIndex=total+i+entity.anchorIndex+2;//锚点的坐标等于块数+该比赛的索引
					total+=entity.mGames.size();
				}
			}
		}

	}

}
