package com.hupu.games.data;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.common.HupuLog;

import android.util.Log;

/**用户宝箱*/
public class UserBoxEntity extends BaseEntity {
	
	public int gold;
	public int copper;
	public int sliper;
	
	@Override
	public void paser(JSONObject json) throws Exception {
		HupuLog.e("ZYN", "UserBoxEntity"+json.toString());
		JSONArray jsonArr=json.getJSONArray("result");
		gold=jsonArr.getInt(2);
		copper=jsonArr.getInt(0);
		sliper=jsonArr.getInt(1);
	}

}
