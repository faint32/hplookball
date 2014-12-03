/**
 * 
 */
package com.hupu.games.hupudollor.data;

import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

/**
 * @author zhenhua
 * 虎扑币-获取单项订单信息
 */
public class HupuDollorRechargeReq extends BaseEntity {

	public String orderNo;
	public String url;
	public int is_login;
	public int status;
	@Override
	public void paser(JSONObject json) throws Exception {
		JSONObject result=json.getJSONObject("result");
		is_login = json.optInt("is_login");
		orderNo= result.optString("orderNo");		
		url= result.optString("url");	
		status = result.optInt("status");
		
	}
	

}
