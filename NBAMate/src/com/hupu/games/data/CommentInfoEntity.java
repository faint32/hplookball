package com.hupu.games.data;

import org.json.JSONObject;

public class CommentInfoEntity extends BaseEntity {



	public int replies;//    新闻评论数
	public int lights;//   新闻亮评论数
	public int allow_comment;// 是否允许评论
	public String title;// 自定义title
	@Override
	public void paser(JSONObject json) throws Exception {
	    json =json.optJSONObject(KEY_RESULT);
	    replies = json.optInt("replies");
	    lights =json.optInt("lights");
	    allow_comment =json.optInt("allow_comment");
	    title =json.optString("title", "");
        if(title.equals(""))
        	title=null;
	}

}
