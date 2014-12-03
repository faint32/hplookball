package com.hupu.games.data;

import org.json.JSONObject;

public class FollowResp extends BaseEntity {

	public int i_success=-1;

	@Override
	public void paser(JSONObject json) throws Exception {
		i_success=json.optInt(KEY_RESULT, 0);
		json=null;		
	}

}
