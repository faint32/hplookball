package com.hupu.games.data;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.game.base.SimpleScoreboard;
/**聊天列表页*/
public class ChatResp extends BaseEntity {

	public LinkedList<ChatEntity> mList;


	public long lastVId;
	/**请求数据中最新的偏移量*/
	public int pid;    
	/** 终端请求的偏移量*/
	public int pid_old ;  
	

	
	/** 数据偏移方向*/
	public String direc; 
	/**在线人数*/
	public String online;  
	
	public int size;

	public SimpleScoreboard score;

	@Override
	public void paser(JSONObject json) throws Exception {
		
		online =json.optString("online",null);
		direc =json.optString("direc", "");
		pid = json.optInt("pid");
		pid_old = json.optInt("pid_old",-1);
		json =json.optJSONObject("result");
		
		//新增解析上墙数据

	
		if (json.has(KEY_SCORE_BOARD)) {
			// 比分牌数据
			JSONObject o = json.getJSONObject(KEY_SCORE_BOARD);
			score =new SimpleScoreboard();
			score.paser(o);
		}
		
		
		JSONArray array = json.optJSONArray("data");
		
		
		if (array != null) {
			size = array.length();
			if(size >0)
			{
				mList = new LinkedList<ChatEntity>();
				ChatEntity temp;
				for (int i = 0; i < size; i++) {
					temp = new ChatEntity();
					temp.paser(array.getJSONObject(i));
					if(!temp.username.equals(""))
						mList.add(temp);
				}
			}
			
		}
		
	}

}
