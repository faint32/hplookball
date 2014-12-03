package com.hupu.games.data.game.football;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.common.HupuLog;
import com.hupu.games.data.BaseEntity;

import android.support.v4.util.ArrayMap;
import android.support.v4.util.SimpleArrayMap;

public class SoccerPlayerReq extends BaseEntity {


	
	public LinkedHashMap<String,ArrayList<SoccerPlayerEntity>> mPlayerMap;

	public String keys[];
	public int tid; // 球队id
	public String name; // 中文短名称
	public String coach_name; // 中文全名称
	public int coach_id; // 球队id
	public String coach_header;
	
	public boolean have_worth = false;
	
 	
	@Override
	public void paser(JSONObject json) throws Exception {

		JSONObject obj = json.getJSONObject(KEY_RESULT);
		if (obj != null) {
			JSONObject temp = obj.optJSONObject("team_info");
			if (temp != null) {
				name = temp.optString("name");
				tid = temp.optInt("tid");
			}
			temp =obj.optJSONObject("coach");
			if (temp != null) {
				coach_name = temp.optString("coach_name");
				coach_header = temp.optString("coach_header");
				coach_id = temp.optInt("coach_id");
			}
			
			JSONArray arr =obj.optJSONArray("list");
			
			if(arr !=null)
			{

			
				int size =arr.length();
				if(size == 0)
					return;
				mPlayerMap =new LinkedHashMap<String,ArrayList<SoccerPlayerEntity>>();
				keys =new String[size];
				int s=0;
				JSONArray players;
				SoccerPlayerEntity data ;
				String group;
				for(int i =0;i<size;i++)
				{
					temp =arr.getJSONObject(i);
					group=temp.optString("group");
					ArrayList<SoccerPlayerEntity> mDataList=null;
					if(!mPlayerMap.containsKey(group))
					{
						mDataList=new ArrayList<SoccerPlayerEntity> ();
						mPlayerMap.put(group, mDataList);
						keys[i]=group;
					}
					players = temp.optJSONArray("data");
					if(players!=null)
					{
						s =players.length();
						for(int j=0;j<s;j++)
						{
							data =new SoccerPlayerEntity();
							data.paser(players.getJSONObject(j));
							mDataList.add(data);
							if (!"".equals(data.market_values)) {
								have_worth = true;
							}
						}
					}

				}
				int k=0;
			}
		}
	}

}
