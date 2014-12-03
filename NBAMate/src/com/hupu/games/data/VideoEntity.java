package com.hupu.games.data;

import org.json.JSONObject;

public class VideoEntity extends BaseEntity {

	public long vid;// 视频唯一ID
	public int gid;// 相应比赛ID，热门视频此项数据为空
	public String title;// 视频标题
	public String fromurl;// 视频源地址
	public String playtime;// 播放时长
	public String cover;// 视频截图
	public String html5url;
	public String source;
	public int isoptimize ;

	@Override
	public void paser(JSONObject json) throws Exception {
		vid = json.optLong("vid");
		gid = json.optInt(KEY_GAME_ID);
		title = json.optString(KEY_TITLE, null);
		fromurl = json.optString("fromurl", null);
		html5url =json.optString("html5",null);
		playtime = json.optString("playtime");
		cover = json.optString("cover");
		source = json.optString("source");
		isoptimize  = json.optInt("isoptimize");
	}

}
