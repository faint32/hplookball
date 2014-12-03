/**
 * 
 */
package com.hupu.games.data;

import org.json.JSONObject;

/**
 * @author panyongjun
 * 查询余额
 */
public class RechargeReq extends BaseEntity {

	public String orderNo;
	public String url;
	@Override
	public void paser(JSONObject json) throws Exception {
		JSONObject result=json.getJSONObject("result");
		orderNo= result.optString("orderNo");		
		url= result.optString("url");	
	}
	

}
