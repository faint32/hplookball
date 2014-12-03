package com.hupu.games.data.goldbean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class ExchangeGoldBeanEntity extends BaseEntity {

	  public String status;//: 1 虎扑币余额足够，直接兑换  2 虎扑币余额不足，钱包余额足够，购买虎扑币后兑换  3 钱包余额不足，第三方购买后兑换  4 兑换数量超过最大充值额度，跳到虎扑币购买页面    
	  
	  public String content;//: "提示信息"，//给用户的提示信息      
	  
	  public ArrayList<String> channel = new ArrayList<String>();//:["kanqiu_wallet_pay "], // ["hupu_dollar_pay"]或["weixin","alipay_wap"]
	  
	  public EventDataBean eventDataBean;//
	  
	  public String isLogin = "";
	  
	  public String coin;
	
	@Override
	public void paser(JSONObject json) throws Exception {
		// TODO Auto-generated method stub
		
		JSONObject mJson = json.optJSONObject(KEY_RESULT);
		isLogin = json.has("is_login")?json.optString("is_login"):"0";
		if(mJson!=null)
		{		
			status = mJson.has("status")?mJson.optString("status"):"";
			content = mJson.has("content")?mJson.optString("content"):"";
			
			JSONArray jsonArray = new JSONArray(mJson.getString("channel"));
			for (int i = 0; i < jsonArray.length(); i++) {
				channel.add(jsonArray.getString(i));
			}
			
			JSONObject jsonObject = mJson.optJSONObject("event");
			if(jsonObject!=null)
			{
				eventDataBean = new EventDataBean();
				
				eventDataBean.id = jsonObject.has("id")?jsonObject.optString("id"):"";
				eventDataBean.channel = jsonObject.has("channel")?jsonObject.optString("channel"):"";
				
				eventDataBean.give = jsonObject.has("giveAmount")?jsonObject.optString("giveAmount"):"";
				eventDataBean.recharge = jsonObject.has("rechargeAmount")?jsonObject.optString("rechargeAmount"):"";
				
				eventDataBean.total = jsonObject.has("totalAmount")?jsonObject.optString("totalAmount"):"";
				eventDataBean.title = jsonObject.has("title")?jsonObject.optString("title"):"";
			}

		}

	}
}
