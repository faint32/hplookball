package com.hupu.games.data.news;

import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class NewsEntity extends BaseEntity {

	public long nid;// 新闻唯一ID
	
	public String title;//  新闻标题
	public String newsImg;//新闻配图
	public String summary;// 视频源地址
	public int replies;// 
	public String lights;// 
	public String topType;// 



	@Override
	public void paser(JSONObject json) throws Exception {
		nid = json.optLong("nid");
		title = json.optString(KEY_TITLE, null);
		newsImg = json.optString("img");
		summary = json.optString("summary", null);
		replies = json.optInt("replies");
		lights = json.optString("lights");
		topType = json.optString("topType");
	}
}
