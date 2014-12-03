package com.hupu.games.data;

import java.io.Serializable;

import org.json.JSONObject;

/**球员总的评论数据*/
@SuppressWarnings("serial")
public class PlayerRatingEntity extends BaseEntity implements Serializable{
	/** 对象ID */
	public int oid;
	/** 对象名字 */
	public String name;
	/** 对象头像 */
	public String header_img;
	/** 表现描述 */
	public String content;
	/** 平均（潜规则）评分 */
	public String ratings;
	/** 总评论人数 */
	public int user_num;
	/** 我的评分 */
	public int my_rating;

	/** 备注，现为对象所属球队名*/
	public String memo;         
	
	public int obj_type;// 评分对象类别 1 球员 2 球队 0 其他
	public int obj_id;// 评分对象id
	@Override
	public void paser(JSONObject json) throws Exception {

		oid =json.optInt("oid", 0);
		name=json.optString("name", "");
		header_img=json.optString("header_img", "");
		content=json.optString("content", "");
		ratings=json.optString("ratings", "");
		user_num=json.optInt("user_num", 0);
		my_rating=json.optInt("my_rating", 0);
		memo=json.optString("memo", "");
		obj_type=json.optInt("obj_type", 0);
		obj_id=json.optInt("obj_id", 0);
	}

}
