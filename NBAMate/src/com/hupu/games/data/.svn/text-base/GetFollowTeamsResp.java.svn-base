package com.hupu.games.data;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetFollowTeamsResp extends BaseEntity {

	public LinkedList<Integer> mListTeams;

	@Override
	public void paser(JSONObject json) throws Exception {
		mListTeams =new  LinkedList<Integer>();
        JSONArray arr =json.getJSONArray(KEY_RESULT);
        int size =arr.length();
        for(int i=0;i<size;i++)
        {
        	mListTeams.add(arr.getInt(i));
        }
		
	}

}
