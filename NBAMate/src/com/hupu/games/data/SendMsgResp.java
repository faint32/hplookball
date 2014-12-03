package com.hupu.games.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;
/**聊天列表页*/
public class SendMsgResp extends BaseEntity {

	
	public int pid;
	

	@Override
	public void paser(JSONObject json) throws Exception {
		
		pid=json.optInt(KEY_RESULT,0);
		
	}

}
