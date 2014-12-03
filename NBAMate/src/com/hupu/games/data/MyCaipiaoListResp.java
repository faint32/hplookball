package com.hupu.games.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.game.quiz.QuizCaipiaoEntity;
/**
 * 彩票组
 * @author papa
 * */
public class MyCaipiaoListResp extends BaseEntity {

	public ArrayList<QuizCaipiaoEntity> mQuizList;
	
	public String block;  //组 名称
	
	public String scheme;  //scheme-URL
	public String lid;
	public String gid;
	

	@Override
	public void paser(JSONObject json) throws Exception {
		//JSONObject mjson = json.optJSONObject("rs");
		
		block = json.optString("block");
		
		scheme = json.optString("url");
		lid = json.optString("lid");
		gid = json.optString("gid");
			
		JSONArray array =json.optJSONArray("list");
		if(array!=null )
		{
			mQuizList =new ArrayList<QuizCaipiaoEntity>();
			int size =array.length();
			QuizCaipiaoEntity entity;
			for(int i =0;i<size;i++)
			{
				entity =new QuizCaipiaoEntity();
				entity.paser(array.getJSONObject(i));
				mQuizList.add(entity);
				
			}
		}
	}

}
