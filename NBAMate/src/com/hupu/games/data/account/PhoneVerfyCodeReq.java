package com.hupu.games.data.account;

import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;
/**
 * @author panyongjun
 * 请求手机验资码
 */
public class PhoneVerfyCodeReq extends BaseEntity {

	public int status;//发送状态（0：未发送或发送失败，1：发送成功）
	public int expire ;//还剩多少秒才能再次获取短信密码
	    
	@Override
	public void paser(JSONObject json) throws Exception {
		json =json.optJSONObject(KEY_RESULT);
		if(json!=null)
		{
			status = json.optInt("status", 0);
			expire= json.optInt("expire", 0);
		}
	}

}
