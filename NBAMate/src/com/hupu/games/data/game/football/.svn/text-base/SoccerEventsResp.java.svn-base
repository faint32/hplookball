package com.hupu.games.data.game.football;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class SoccerEventsResp extends BaseEntity {

	public ScoreboardEntity scoreBoard;

	public ArrayList<FootballEventData> mDatas;

	public int refresh_time;
	
	public  String tvLink;
	
	public int iFollow;
	
	public String adUrl;
	public String adImg;
	
	@Override
	public void paser(JSONObject json) throws Exception {
		
		JSONObject settings =json.getJSONObject("settings");
		refresh_time =settings.optInt("refresh_time");
		settings =null;
		
		json = json.optJSONObject(KEY_RESULT);
		scoreBoard = new ScoreboardEntity();
		scoreBoard.paser(json.optJSONObject("scoreboard"));
		JSONArray array = json.optJSONArray("data");
		if (array != null && array.length() > 0) {
			mDatas = new ArrayList<FootballEventData>();
			int size = array.length();
			FootballEventData temp;
			for (int i = 0; i < size; i++) {
				temp = new FootballEventData();
				temp.paser(array.getJSONObject(i));
				if (temp.tid == scoreBoard.i_home_tid)
					temp.eventType = 1;
				else if (temp.tid == scoreBoard.i_away_tid)
					temp.eventType = 2;
				mDatas.add(temp);

			}
		}
		tvLink =json.optString(KEY_TV_LINK, "");
		iFollow=json.optInt(KEY_FOLLOW);
		if (!json.isNull("ad")) {
			JSONObject adJsonObj = json.optJSONObject("ad"); 
			adImg = adJsonObj.optString("img");
			adUrl = adJsonObj.optString("url");
		}
	}

}
