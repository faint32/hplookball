package com.hupu.games.data.news;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class NewsDetailEntity extends BaseEntity {

	public long nid;// 新闻唯一ID

	public String title;// 新闻标题
	public String summary;// 新闻描述
	public String replies;// 播新闻评论数

	public String lights;// 新闻亮评论数

	public String origin;// 新闻来源
	public String img;// 新闻配图
	public String mImg;// 新闻配图
	public String content;// 新闻正文内容
	public String league;// 新闻分类
	public String addtime;// 发布时间，时间戳
	public String replyurl;// wap版该新闻的评论网址
	public LinkedList<NewsLightEntity> lightList;

	public String wechatShare;    //微信分享内容
	public String wechatMomentsShare;//   微信朋友圈分享内容
	public String qzoneShare;    //QQ空间分享内容
	public String weiboShare;    //新浪微博分享内容
	public String shareUrl;    //分享链接地址
	
	public int unShare;
	public String shareImg;
	
	
	@Override
	public void paser(JSONObject json) throws Exception {
		json=json.optJSONObject(KEY_RESULT);
		nid = json.optLong("vid");

		title = json.optString(KEY_TITLE, null);
		summary = json.optString("summary");
		replies = json.optString("replies");
		lights = json.optString("lights");
		origin = json.optString("origin");
		img = json.optString(KEY_IMG);
		mImg = json.optString("img_m");
		content = json.optString(KEY_CONTENT);
		league = json.optString("league");
		addtime = json.optString("addtime");
		replyurl = json.optString("replyurl");
		
		unShare = json.optInt("un_share");
		
		JSONArray array = json.optJSONArray("light_comments");
		if (array != null) {
			int size = array.length();
			lightList = new LinkedList<NewsLightEntity>();
			NewsLightEntity temp;
			for (int i = 0; i < size; i++) {
				temp = new NewsLightEntity();
				temp.paser(array.getJSONObject(i));
				lightList.add(temp);
			}
		}
		json= json.optJSONObject("share");
		if(json !=null)
		{
			//分享内容
			wechatShare=json.optString("wechat");
			wechatMomentsShare=json.optString("wechat_moments");
			qzoneShare=json.optString("qzone");
			weiboShare=json.optString("weibo");
			shareUrl=json.optString("url");
			shareImg = json.optString("img");
		}
	}

}
