/**
 * 
 */
package com.hupu.games.data;

import org.json.JSONObject;

/**
 * @author panyongjun
 * 查询余额
 */
public class CommitExchangeReq extends BaseEntity {

	public int msg;
	@Override
	public void paser(JSONObject json) throws Exception {
		JSONObject mJson= json.optJSONObject(KEY_RESULT);	
		msg = mJson.optInt(KEY_STATUS);
	}

}
