package com.hupu.games.data.game.quiz;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;
/**
 * 竞猜组
 * @author papa
 * */
public class QuizListResp extends BaseEntity {

	public ArrayList<QuizEntity> mQuizList;
	
	public String name;  //组 名称
	public int status;  //组状态
	
	
	public String scheme;  //scheme-URL
	public String lid;  
	public String gid;  
	
	public String vsName;//对阵情况
	public String date;//日期

	@Override
	public void paser(JSONObject json) throws Exception {
		//JSONObject mjson = json.optJSONObject("rs");
		
		JSONObject object = json.optJSONObject(KEY_INFO);
			name = object.optString("name");
			status = object.optInt("is_end");
			vsName = object.optString("vs");
			date = object.optString("date");
			
			scheme = object.optString("url");
			lid = object.optString("lid");
			gid = object.optString("gid");
			
		JSONArray array =json.optJSONArray("rs");
		if(array!=null )
		{
			mQuizList =new ArrayList<QuizEntity>();
			int size =array.length();
			QuizEntity entity;
			for(int i =0;i<size;i++)
			{
				entity =new QuizEntity();
				entity.paser(array.getJSONObject(i));
				mQuizList.add(entity);
				
			}
		}
	}

}
