package com.hupu.games.data.news;

import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class NewsLightEntity extends BaseEntity {

	public long ncid;// 新闻评论唯一ID
	
	public String user_name;//  发表用户
	public String light_count;//点评数
	public String unlight_count;//不亮数
	public String content;// 评论内容
	public String from;// 
	public String create_time;// 
	public int lighted;//是否已经点亮过了！



	@Override
	public void paser(JSONObject json) throws Exception {
		ncid = json.optLong("ncid");
		user_name = json.optString("user_name");
		light_count = json.optString("light_count");
		unlight_count = json.optString("unlight_count");
		content = json.optString("content", null);
		from = json.optString("from");
		create_time = json.optString("create_time");
		lighted = json.optInt("lighted");
		
		if ("".equals(light_count)) 
			light_count = "0";
		
		if ("".equals(unlight_count)) 
			unlight_count = "0";
	}

}
