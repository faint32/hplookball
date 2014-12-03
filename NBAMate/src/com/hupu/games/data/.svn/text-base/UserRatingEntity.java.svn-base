/**
 * 
 */
package com.hupu.games.data;

import java.io.Serializable;

import org.json.JSONObject;

/**
 * @author panyongjun
 * 用户打分返回信息
 */
@SuppressWarnings("serial")
public class UserRatingEntity extends BaseEntity implements Serializable{

	
	public int oid;       //            评分对象的ID
	public String ratings; //             平均（潜规则）评分
	public int user_num;   //      总评论人数
	public int my_rating;   //      我的评分
	@Override
	public void paser(JSONObject json) throws Exception {
		
		json=json.optJSONObject(KEY_RESULT);
		
		oid = json.optInt("oid",0);
		ratings= json.optString("ratings","");
		user_num= json.optInt("user_num",0);
		my_rating= json.optInt("my_rating",0);
	}

}
