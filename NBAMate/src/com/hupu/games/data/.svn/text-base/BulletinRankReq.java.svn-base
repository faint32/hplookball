package com.hupu.games.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class BulletinRankReq extends BaseEntity {

	public static class BulletinRankEntity extends BaseEntity 
	{
		public String nickname;//    昵称
		public int coin;//    消费金币
		public int like ;//   赞数
		public String content ;//   上墙内容
		
		public int isHated;
		
		public String hated_nickname  ; //  拆的人的昵称（需要转换 [vip]）
		
		public int hated_coin ;//   拆的人花费了多少金币
		@Override
		public void paser(JSONObject json) throws Exception {
			nickname = json.optString("nickname");//    昵称
			coin= json.optInt("coin");//    消费金币
			like = json.optInt("like");//   赞数
			content = json.optString("content");;//   上墙内容
			hated_coin=json.optInt("hated_coin",0);
			hated_nickname=json.optString("hated_nickname","");
			isHated =json.optInt("is_hated",0);
		}
		
	}
	public ArrayList<BulletinRankEntity> mList;
	@Override
	public void paser(JSONObject json) throws Exception {
	
		JSONArray arr =json.optJSONArray(KEY_RESULT);
		int size =arr.length();
		if( size>0)
		{
			//
			mList =new ArrayList<BulletinRankEntity>();
			BulletinRankEntity entity;
			for(int i=0;i<size;i++)
			{
				entity =new BulletinRankEntity();
				entity.paser(arr.getJSONObject(i));
				mList.add(entity);
			}
		}
	}

}
