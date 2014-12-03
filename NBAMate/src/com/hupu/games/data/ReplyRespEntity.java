package com.hupu.games.data;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.news.NewsLightEntity;

public class ReplyRespEntity extends BaseEntity {

	public String count;// 评论数
//	public int pages;//总页数
//	public int page;//总页数
	public long lastNId;
	public String lastTime;
	public boolean isLast = true;
	
	public LinkedList<NewsLightEntity> lightList;
	public LinkedList<NewsLightEntity> replyList;

	@Override
	public void paser(JSONObject json) throws Exception {
		json=json.optJSONObject(KEY_RESULT);
		count = json.optString("count");
		isLast = json.optInt("hasNextPage") == 0?true:false;
		JSONArray RArray = json.optJSONArray(KEY_DATA);
		if (RArray != null) {
			int size = RArray.length();
			replyList = new LinkedList<NewsLightEntity>();
			NewsLightEntity rTemp;
			for (int i = 0; i < size; i++) {
				rTemp = new NewsLightEntity();
				rTemp.paser(RArray.getJSONObject(i));
				replyList.add(rTemp);
				if(i==size -1){
					lastNId =rTemp.ncid;
					lastTime =rTemp.create_time;
				}
			}
		}
		
		JSONArray lArray = json.optJSONArray("light_comments");
		if (lArray != null) {
			int size = lArray.length();
			lightList = new LinkedList<NewsLightEntity>();
			NewsLightEntity lTemp;
			for (int i = 0; i < size; i++) {
				lTemp = new NewsLightEntity();
				lTemp.paser(lArray.getJSONObject(i));
				lightList.add(lTemp);
			}
		}

	}

}
