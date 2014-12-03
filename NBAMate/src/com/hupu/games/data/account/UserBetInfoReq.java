/**
 * 
 */
package com.hupu.games.data.account;

import java.util.ArrayList;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.BaseEntity;
import com.hupu.games.data.BindEntity;
import com.hupu.games.data.PrizeEntity;

/**
 * @author papa “我”的界面中所有的数据，包括金币，下注，奖励，任务信息等
 */
public class UserBetInfoReq extends BaseEntity {

	public int win;
	public int lose;
	public int box;
	public int balance;
	public String walletBalance;
	public ArrayList<PrizeEntity> prizeList;
	public String task;// 任务 暂时没有
	
	public int isLogin;
	public LinkedList<TaskEntity> taskList;

	@Override
	public void paser(JSONObject json) throws Exception {
		super.paser(json);
		JSONObject mJson = json.optJSONObject(KEY_RESULT);

		win = mJson.optInt("win", 0);
		lose = mJson.optInt("lose", 0);
		box = mJson.optInt("box", 0);
		balance = mJson.optInt("balance", 0);
		walletBalance = mJson.optString("wallet_balance");

		JSONArray array = mJson.optJSONArray("prize");
		if (array != null) {
			prizeList = new ArrayList<PrizeEntity>();
			int size = array.length();
			PrizeEntity entity;
			for (int i = 0; i < size; i++) {
				entity = new PrizeEntity();
				entity.paser(array.getJSONObject(i));
				prizeList.add(entity);
			}
		}
		
		JSONArray arrayTask = mJson.optJSONArray("task");
		if (arrayTask != null) {
			taskList = new LinkedList<TaskEntity>();
			TaskEntity task;
			for (int i = 0; i < arrayTask.length(); i++) {
				task = new TaskEntity();
				task.paser(arrayTask.getJSONObject(i));
				taskList.add(task);
			}
			
		}
		
		JSONArray bindArray = mJson.optJSONArray("bind");
        if (bindArray != null) {
        	BindEntity bind;
        	for (int i = 0; i < bindArray.length(); i++) {
        		 bind = new BindEntity();
				 bind.paser(bindArray.getJSONObject(i));
				 SharedPreferencesMgr.setInt("channel"+bind.channel, bind.is_bind);
				 if (bind.channel == 1) {
					 SharedPreferencesMgr.setString("bp", bind.bind_name);
				 }
				 if (bind.channel == 2) {
					 SharedPreferencesMgr.setString("qq_name", bind.bind_name);
				}
			}
		}
        
        if (json.isNull("is_login")) {
        	isLogin = 1;
		}else {
			isLogin = json.optInt("is_login");
		}
	}

}
