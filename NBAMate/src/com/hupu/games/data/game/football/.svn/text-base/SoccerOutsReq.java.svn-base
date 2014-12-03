package com.hupu.games.data.game.football;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

/**
 * @author panyongjun 足球赛况数据
 * */
public class SoccerOutsReq extends BaseEntity {
	
	public ScoreboardEntity scoreBoard;
	/**定时刷新的时间*/
	public int refresh_time;
	/**统计的标题*/
	public ArrayList<String> mTitles;
	/**统计的主队数据*/
	public ArrayList<String> mHomeValue;
	/**统计的客队数据*/
	public ArrayList<String> mAwayValue;
	
	public ArrayList<FootballEventData> mLiveDatas;
	
	public  String tvLink;
	
	public int iFollow;
	
	public String adUrl;
	public String adImg;
	@Override
	public void paser(JSONObject json) throws Exception {
		JSONObject settings =json.getJSONObject("settings");
		refresh_time =settings.optInt("refresh_time");
		settings =null;
		
	    json =json.optJSONObject(KEY_RESULT);
		JSONObject score =json.optJSONObject("scoreboard");
		if(score!=null)
		{
			scoreBoard =new ScoreboardEntity();
			scoreBoard.paser(score);
		}
		
	    JSONArray tabs =json.optJSONArray("stats");
		
	    if(tabs !=null)
	    {
	    	JSONArray arr1=tabs.getJSONArray(0);
	    	JSONArray arr2=tabs.getJSONArray(1);
	     	JSONArray arr3=tabs.getJSONArray(2);
	     	mTitles = new ArrayList<String>();
	     	mHomeValue= new ArrayList<String>();
	     	mAwayValue= new ArrayList<String>();
	     	int size =arr1.length();
	     	for(int i=0;i<size ;i++)
	     	{
	     		mTitles.add(arr1.getString(i));
	     		mHomeValue.add(arr2.getString(i));
	     		mAwayValue.add(arr3.getString(i));
	     	}
	    }
	    
		tvLink =json.optString(KEY_TV_LINK, "");
		iFollow=json.optInt(KEY_FOLLOW);
		
		JSONArray array = json.optJSONArray("events");
		//解析事件直播
		if (array != null && array.length() > 0) {
			mLiveDatas = new ArrayList<FootballEventData>();
			int size = array.length();
			FootballEventData temp;
			for (int i = 0; i < size; i++) {
				temp = new FootballEventData();
				temp.paser(array.getJSONObject(i));
				if (temp.tid == scoreBoard.i_home_tid)
					temp.eventType = 1;
				else if (temp.tid == scoreBoard.i_away_tid)
					temp.eventType = 2;
				mLiveDatas.add(temp);
			}
		}
	}

}
