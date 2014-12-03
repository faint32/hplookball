package com.hupu.games.data.game.basketball;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class BasketballResp extends BaseEntity {
	/**key 比赛日期
	 * value 该日期下的所有比赛
	 * */
	public LinkedHashMap<String, ArrayList<BasketballGameData>> mMap;
	/**key 比赛ID
	 * value 这场比赛在列表中的位置
	 * */
	public HashMap<Integer, DataIndex> mGidMap;
	/**该轮赛程的不同日期列表
	 * */
	public ArrayList<String> mKeys;
	public String prevVal;   
	public String prevTitle;  //上一轮
	
	public String nextVal;   
	public String nextTitle;  //下一轮
	
	public String curVal;
	public String curTitle;
	
	public boolean isMatchDay;
	/**实时数据，因为实时数据的解析方式和http返回的数据结构不同，处理也不同*/
	public boolean isRealTime;
	
	/**
	 * CBA 是按轮数计算的，所以每一轮有不同的天数，因此需要通过DataIndex来确定不同的天数在列表中的位置
	 * */
	public static class DataIndex
	{
		DataIndex(String b,int p)
		{
			block =b;
			pos=p;
		}
		/**日期*/
		public String block;
		/**位置*/
		public int pos;
	}
	
	@Override
	public void paser(JSONObject json) throws Exception {
		
		
		 
		JSONArray array =null;
		if(isRealTime )
		{
			//实时数据
			array =json.optJSONArray(KEY_RESULT);
		}
		else
		{

			JSONObject result =json.getJSONObject(KEY_RESULT);
			JSONObject vals =result.optJSONObject("vals");

			JSONObject rounds =result.optJSONObject("tabs");  
		
			//isMatchDay =result.optInt("is_current", 0)>0?true:false;
			JSONObject round =rounds.optJSONObject("prev");
			
			if(round!=null)
			{
				prevVal =round.optString("val",null);
				prevTitle =round.optString("title");
			}
			round =rounds.optJSONObject("current");
			if(round!=null)
			{
				curVal =round.optString("val",null);
				curTitle =round.optString("title");
				if(vals!=null && curVal!=null)
				{
					isMatchDay =vals.optInt("current", 0)== Integer.parseInt(curVal);
				}
			}
			round =rounds.optJSONObject("next");
			if(round!=null)
			{
				nextVal =round.optString("val",null);
				nextTitle =round.optString("title");
			}	
			rounds =null;
			round=null;
			 array =result.optJSONArray("games_data");
		}

		if(array!=null)
		{
		
			int size =array.length();
			BasketballGameData data =null;
			mMap =new LinkedHashMap<String, ArrayList<BasketballGameData>>();
			if(isMatchDay)
				mGidMap=new HashMap<Integer, DataIndex>();
			mKeys =new ArrayList<String>();
			ArrayList<BasketballGameData> list =null;

			int pos=0;
			for(int i=0;i<size;i++)
			{
				data =new BasketballGameData();
				data.paser(array.getJSONObject(i));
				if(!mKeys.contains(data.block))
				{
					list =new ArrayList<BasketballGameData>();
					mKeys.add(data.block);
					mMap.put(data.block, list);	
					pos =0;
				}
				mMap.get(data.block).add(data);
				if(mGidMap!=null)
					mGidMap.put(data.i_gId, new DataIndex(data.block,pos++));
			}
		}
	
	}

}
