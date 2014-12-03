package com.hupu.games.data;

import org.json.JSONObject;

/**聊天数据*/
public class ChatEntity extends BaseEntity {

	/**username    用户名或昵称
	user_type*/
	public String username;
	/**   用户类型，0为游客，1为网站用户*/
	public int user_type;
	/**聊天内容*/
	public String content;
	/** 发送时间*/
	public long send_time;

	public String emoji;

	public int vip;
	public ChatGiftEntity cgift;
	@Override
	public void paser(JSONObject json) throws Exception {
		username =json.optString("username");
		user_type =json.optInt("user_type");
		content =json.optString("content");
		send_time =json.optLong("send_time");
		emoji =json.optString("emoji",null);
		vip =json.optInt("vip");

		try{
			JSONObject o = json.getJSONObject("gift");
			if(o!=null){
				cgift = new ChatGiftEntity();
				cgift.linkColor = o.optString("link_color");
				if(cgift.linkColor.charAt(0)!='#'){
					cgift.linkColor = "#"+cgift.linkColor;
				}
			}
		}catch(Exception e){

		}


	}

}
