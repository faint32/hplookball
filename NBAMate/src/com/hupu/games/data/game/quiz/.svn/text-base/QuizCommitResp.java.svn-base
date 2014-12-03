package com.hupu.games.data.game.quiz;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;
import com.hupu.games.data.goldbean.EventDataBean;
import com.hupu.games.data.goldbean.ExchangeGoldBeanEntity;

/**
 * 竞猜提交
 * 
 * 
 * 领救济金
 * -1    令牌无效，需要重新登录
 * -2    今日已经领取过了
 * -3    领取失败
 * 1     领取成功
 * */
public class QuizCommitResp extends BaseEntity {

	public int result;// "返回结果",

	public ExchangeGoldBeanEntity eGoldBean;
	
	@Override
	public void paser(JSONObject json) throws Exception {
		
		result =json.optInt(KEY_RESULT);
		
		if(json.has("exchange"))
		{
			eGoldBean = new ExchangeGoldBeanEntity();
			JSONObject mJsonObject = new JSONObject(json.getString("exchange"));
			eGoldBean.status = mJsonObject.getString("status");
			eGoldBean.content = mJsonObject.getString("content");
			eGoldBean.coin = mJsonObject.has("coin")?mJsonObject.optString("coin"):"";
			
			JSONArray jsonArray = new JSONArray(mJsonObject.getString("channel"));
			for (int i = 0; i < jsonArray.length(); i++) {
				eGoldBean.channel.add(jsonArray.getString(i));
			}
			
			JSONObject jsonObject = new JSONObject(mJsonObject.getString("event"));
			if(jsonObject!=null)
			{
				eGoldBean.eventDataBean = new EventDataBean();
				
				eGoldBean.eventDataBean.id = jsonObject.has("id")?jsonObject.optString("id"):"";
				eGoldBean.eventDataBean.channel = jsonObject.has("channel")?jsonObject.optString("channel"):"";
				
				eGoldBean.eventDataBean.give = jsonObject.has("give")?jsonObject.optString("give"):"";
				eGoldBean.eventDataBean.recharge = jsonObject.has("recharge")?jsonObject.optString("recharge"):"";
				
				eGoldBean.eventDataBean.total = jsonObject.has("totalAmount")?jsonObject.optString("totalAmount"):"";
				eGoldBean.eventDataBean.title = jsonObject.has("title")?jsonObject.optString("title"):"";
			}
		}
	}

}
