/**
 * 
 */
package com.hupu.games.data.game.football;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

/**
 * @author panyongjun
 *
 */
public class FootballStatisticResp extends BaseEntity {

	public ScoreboardEntity scoreBoard;
	
	
	public ArrayList<String> mTitles;
	
	public ArrayList<String> homeValue;
	
	public ArrayList<String> awayValue;

	public int refresh_time;
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
		JSONArray tabs =json.optJSONArray("data");
		
	    if(tabs !=null)
	    {
	    	JSONArray arr1=tabs.getJSONArray(0);
	    	JSONArray arr2=tabs.getJSONArray(1);
	     	JSONArray arr3=tabs.getJSONArray(2);
	     	mTitles = new ArrayList<String>();
	     	homeValue= new ArrayList<String>();
	     	awayValue= new ArrayList<String>();
	     	int size =arr1.length();
	     	for(int i=0;i<size ;i++)
	     	{
	     		mTitles.add(arr1.getString(i));
	     		homeValue.add(arr2.getString(i));
	     		awayValue.add(arr3.getString(i));
	     	}
	    }

	}

}
