/**
 * 
 */
package com.hupu.games.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author panyongjun
 * 查询余额
 */
public class RechargeMethodReq extends BaseEntity {

	public long rechargeNum;
	public String[] channelList;
	@Override
	public void paser(JSONObject json) throws Exception {
		JSONArray result=json.optJSONArray("result");
		if (result != null) {
			channelList = new String[result.length()];
			for (int i = 0; i < result.length(); i++) {
				JSONObject channel = result.optJSONObject(i);
				if (channel != null) {
					channelList[i] = channel.optString("id");
				}
			}
		}
	}

}
