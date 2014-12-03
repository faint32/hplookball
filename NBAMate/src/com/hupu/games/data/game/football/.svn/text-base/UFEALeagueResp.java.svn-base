package com.hupu.games.data.game.football;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class UFEALeagueResp extends BaseEntity {

	public ArrayList<ScoreboardEntity> mList;
	
	public ArrayList<String> mGameIdList;
	
	
	public int prevDate;   
	
	public String prevTitle;  //上一轮
	
	public int nextDate;   
	
	public String nextTitle;  //下一轮
	
	public int curDate;
	
	public String curTitle;
	

	
	/**当前正在进行的日期*/
	public int currentDate;
	
	public int refresh_time;
	
	@Override
	public void paser(JSONObject json) throws Exception {
		
		JSONObject settings =json.getJSONObject("settings");
		refresh_time =settings.optInt("refresh_time");
		settings =null;
		
		JSONObject result =json.getJSONObject(KEY_RESULT);
		
		JSONObject rounds =result.getJSONObject("days");
		currentDate = rounds.optInt("current");
		rounds =null;
		
		JSONObject tabs =result.optJSONObject("tabs");
		JSONObject round =tabs.optJSONObject("prev");
		if(round!=null)
		{
			prevDate =round.optInt("day");
			prevTitle =round.optString("title");
		}
		round =tabs.optJSONObject("current");
		if(round!=null)
		{
			curDate =round.optInt("day");
			curTitle =round.optString("title");
		}
		round =tabs.optJSONObject("next");
		if(round!=null)
		{
			nextDate =round.optInt("day");
			nextTitle =round.optString("title");
		}
		tabs =null;
		round=null;
		
		JSONArray array =result.optJSONArray("games_data");
		if(array!=null)
		{
			int size =array.length();
			ScoreboardEntity data =null;
			mList =new  ArrayList<ScoreboardEntity>();
			mGameIdList =new  ArrayList<String>();
			for(int i=0;i<size;i++)
			{
				data =new ScoreboardEntity();
				data.paser(array.getJSONObject(i));
				data.key =curDate;
				mGameIdList.add(""+data.i_gId);
				mList.add(data);				
			}
		}
		result =null;
	}

}
