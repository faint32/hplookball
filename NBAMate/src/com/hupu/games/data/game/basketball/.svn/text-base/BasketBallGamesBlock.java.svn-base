package com.hupu.games.data.game.basketball;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.common.HuPuRes;
import com.hupu.games.data.BaseEntity;
import com.pyj.common.MyUtility;

public class BasketBallGamesBlock extends BaseEntity {

	public String mDateBlock;
	public String type_block;
	public int mDay;// "20140919",
	public ArrayList<BasketballGameEntity> mGames;
	public ArrayList<String> mIds;
	public int anchorIndex=-1;
	public int anchor;
	@Override
	public void paser(JSONObject json) throws Exception {
		JSONArray arrGames=json.optJSONArray(KEY_RESULT);
		if(arrGames==null)
		{
			mDateBlock =json.optString("date_block");
			type_block=json.optString("type_block");
			mDay =json.optInt("day");
			arrGames=json.optJSONArray("data");	
		}
		if(arrGames!=null)
			paser(arrGames);
	}
	
	public void paser(JSONArray arrGames) throws Exception {

		BasketballGameEntity entity;
		int size =arrGames.length();
		if(size>0)
		{
			mGames = new ArrayList<BasketballGameEntity>();
			mIds =new ArrayList<String>();
			for(int i=0;i<size;i++)
			{
				entity =new BasketballGameEntity();
				entity.paser(arrGames.getJSONObject(i));
				mGames.add(entity);
				mIds.add(entity.i_gId+"");
				if(anchor==entity.i_gId)
					anchorIndex=i;
			}
		}
	}
	
}
