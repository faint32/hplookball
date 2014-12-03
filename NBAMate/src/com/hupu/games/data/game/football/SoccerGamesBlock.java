package com.hupu.games.data.game.football;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.common.HuPuRes;
import com.hupu.games.data.BaseEntity;
import com.hupu.games.data.game.basketball.BasketballGameEntity;
import com.pyj.common.MyUtility;

public class SoccerGamesBlock extends BaseEntity {

	public String mDateBlock;
	public String type_block;
	public int mDay;// "20140919",
	public String mType;// "20140919_1",
	public ArrayList<ScoreboardEntity> mGames;
	public ArrayList<String> mIds;
	public int anchorIndex=-1;
	public int anchor;
	@Override
	public void paser(JSONObject json) throws Exception {
		JSONArray arrGames=json.optJSONArray(KEY_RESULT);
		if(arrGames==null)
		{
			mDateBlock =json.getString("date_block");
			type_block=json.getString("type_block");
			mDay =json.optInt("day");
			mType =json.getString("block");
			arrGames=json.optJSONArray("data");	
		}
		if(arrGames!=null)
			paser(arrGames);
	}
	
	public void paser(JSONArray arrGames) throws Exception {

		ScoreboardEntity entity;
		int size =arrGames.length();
		if(size>0)
		{
			mGames = new ArrayList<ScoreboardEntity>();
			mIds =new ArrayList<String>();
			for(int i=0;i<size;i++)
			{
				entity =new ScoreboardEntity();
				entity.paser(arrGames.getJSONObject(i));
				mGames.add(entity);
				mIds.add(entity.i_gId+"");
				if(anchor==entity.i_gId)
					anchorIndex=i;
			}
		}
	}
	
}
