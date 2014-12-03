package com.hupu.games.data;

import org.json.JSONObject;

/**用户对球员的评论数据*/
public class PlayerRatingByUserEntity extends BaseEntity {

	/** 详细评分ID */
	public int coid ;   
	/** 客户端ID */
	public int cid  ;
	/** 昵称*/
	public String nickname ; 
	/** 评分 */
	public String rating ;  
	/** 说明*/
	public String desc  ; 
	/** 被赞数 */
	public int like   ;   
	/** 是否已赞过*/
	public boolean liked;       
	
	public int vip; 
	@Override
	public void paser(JSONObject json) throws Exception {

		like =json.optInt("like", 0);
		coid =json.optInt("coid", 0);
		nickname=json.optString("nickname", "");
		rating=json.optString("rating", "");
		desc=json.optString("desc", "");
		liked=json.optInt("liked", 0)==0?false:true;
		vip =json.optInt("vip", 0);
	}

}
