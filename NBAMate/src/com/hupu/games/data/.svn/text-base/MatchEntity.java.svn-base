package com.hupu.games.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class MatchEntity extends BaseEntity implements Serializable{

	public int i_gId;
	public int i_tId;
//	public String str_score;
//	public String str_section1;
//	public String str_section2;
//	public String str_section3;
//	public String str_section4;
//	public String[] arr_ot_scores;
//    
//	public LinkedList<String> names;
	public HashMap<String ,String> mapScore;
	/**加时赛次数*/
	public int otTimes;

	@Override
	public void paser(JSONObject json) throws Exception {
//		i_gId = json.getInt(KEY_GAME_ID);
//		i_tId = json.getInt(KEY_GAME_ID);
//		i_section1 = json.optString(KEY_SECTION1, null);
//		i_section2 = json.optString(KEY_SECTION2, null);
//		i_section3 = json.optString(KEY_SECTION3, null);
//		i_section4 = json.optString(KEY_SECTION4, null);
//		i_score
//		if (json.optInt(KEY_OVER_TIME + "1", -1) > -1) {
//			arr_ot_scores = new int[10];
//			int ii = 0;
//			for (int i = 0; i < 10; i++) {
//				ii = json.optInt(KEY_OVER_TIME + "" + i, -1);
//				if (ii > -1)
//					arr_ot_scores[i] = ii;
//			}
//		}
		mapScore =new HashMap<String ,String>();
		JSONArray arr =json.names();
		int size =arr.length();
		String key =null;
		for(int i=0;i<size;i++)
		{
			key=arr.getString(i);
			if(key.indexOf("overtime")>-1)
				otTimes++;
			mapScore.put(key, json.optString(key,""));
		}
		arr=null;
	}

	
	
}
