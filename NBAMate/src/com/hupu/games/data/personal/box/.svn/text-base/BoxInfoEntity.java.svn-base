package com.hupu.games.data.personal.box;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class BoxInfoEntity extends BaseEntity {

	public int [] boxInfo;
	
	public String [] description;
	@Override
	public void paser(JSONObject json) throws Exception {
		JSONArray arr =json.optJSONArray(KEY_RESULT);
		if(arr!=null)
		{
			boxInfo =new int[arr.length()];
			for(int i=0;i<boxInfo.length;i++)
			{
				boxInfo[i]=arr.getInt(i);
			}
		}

	}

}
