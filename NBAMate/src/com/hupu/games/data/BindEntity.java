/**
 * 
 */
package com.hupu.games.data;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.HuPuApp;
import com.hupu.games.common.HupuLog;

/**
 * @author panyongjun
 * 请求手机绑定
 */
public class BindEntity extends BaseEntity {

	public int channel;  
	public int status;  
	public int is_bind; 
	public String bind_name;
	
	@Override
	public void paser(JSONObject json) throws Exception {
		channel = json.optInt("channel");
		status = json.optInt("status");
		is_bind = json.optInt("is_bind");
		bind_name = json.optString("bind_name", null);
		
		
	}

}
