package com.hupu.games.data;

import org.json.JSONObject;

public class NickNameEntity extends BaseEntity {

	public String name;
	@Override
	public void paser(JSONObject json) throws Exception {
		name = json.optString(BaseEntity.KEY_RESULT, "");
	}

}
