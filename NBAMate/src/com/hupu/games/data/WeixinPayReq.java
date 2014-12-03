/**
 * 
 */
package com.hupu.games.data;

import org.json.JSONObject;

import com.hupu.games.common.HupuLog;

/**
 * @author panyongjun
 * 查询余额
 */
public class WeixinPayReq extends BaseEntity {

	public String sign;
	public String retcode;
	public String appId;
	public String timeStamp;
	public String packageValue;
	public String partnerId;
	public String retmsg;
	public String nonceStr;
	public String prepayId;
	@Override
	public void paser(JSONObject json) throws Exception {
		sign= json.optString("sign");		
		retcode= json.optString("retcode");	
		appId= json.optString("appId");	
		timeStamp= json.optString("timeStamp");	
		packageValue= json.optString("packageValue");	
		partnerId= json.optString("partnerId");	
		retmsg= json.optString("retmsg");	
		nonceStr= json.optString("nonceStr");	
		prepayId= json.optString("prepayId");	
	}
	

}
