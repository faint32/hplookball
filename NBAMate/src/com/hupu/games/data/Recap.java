package com.hupu.games.data;

import java.io.Serializable;

import org.json.JSONObject;

@SuppressWarnings("serial")
public class Recap extends BaseEntity implements Serializable{

	public int i_gId;
	public String str_title;

	public String str_content;
	public String str_img;
	public String str_video;

	@Override
	public void paser(JSONObject json) throws Exception {
		
		JSONObject obj = json.optJSONObject(KEY_RESULT);
		if(obj==null)
		{		
			str_content =json.optString(KEY_RESULT,null);
		}
		else
		{
			i_gId = obj.optInt(KEY_GAME_ID);
			str_title = obj.optString(KEY_TITLE,null);
			str_content =obj.optString(KEY_CONTENT,null);
			str_img=obj.optString(KEY_IMG,null);
			str_video =obj.optString(KEY_VIDEO_LINK,null);
		}
		
	}

}
