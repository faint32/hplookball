/**
 * 
 */
package com.hupu.games.data;

import org.json.JSONObject;

/**
 * @author panyongjun
 * 球员评分，用户点亮
 */
public class UserLikeEntity extends BaseEntity {

	
	public int coid;       //详细评分ID
	public int like;// 最新“赞”数
	@Override
	public void paser(JSONObject json) throws Exception {
		
		json=json.optJSONObject(KEY_RESULT);
		
		coid = json.optInt("coid",0);
		like= json.optInt("like",0);

	}

}
