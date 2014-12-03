package com.hupu.games.data;

import java.io.Serializable;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;


/**球队排行*/
public class StandingsResp extends BaseEntity implements Serializable{

	public LinkedList<TeamRankEntity> mListEast;
	
	public LinkedList<TeamRankEntity> mListWest;
	
	private static final String KEY_EAST="east";
	private static final String KEY_WEST="west";
	@Override
	public void paser(JSONObject json) throws Exception {
		JSONObject jsonObject  =json.getJSONObject(KEY_RESULT);
		
		
		mListEast =new  LinkedList<TeamRankEntity> ();
		mListWest=new  LinkedList<TeamRankEntity> ();
	
		paserTeams(jsonObject,mListEast,KEY_EAST);
		paserTeams(jsonObject,mListWest,KEY_WEST);
		jsonObject =null;
		json= null;
	}

	private void paserTeams(JSONObject json,LinkedList<TeamRankEntity> teams,String key) throws Exception 
	{
		JSONArray arr =json.optJSONArray(key);
		int size =arr.length();
		if( arr !=null)
		{
			TeamRankEntity temp =null;
			for(int i=0;i<size;i++)
			{
				temp =new TeamRankEntity();
				temp.paser(arr.getJSONObject(i));
				teams.add(temp);
			}
		}
		json =null;
	}
}
