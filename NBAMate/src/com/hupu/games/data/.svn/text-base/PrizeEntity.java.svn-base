/**
 * 
 */
package com.hupu.games.data;

import java.util.LinkedList;

import org.json.JSONObject;

/**
 * @author panyongjun 查询余额
 */
public class PrizeEntity extends BaseEntity {

	public int id;
	public String name;
	public String shortname;
	public int coin;
	public int remain;
	public String img_small;// 小图
	public String img;// 小图
	public int type;
	public int status;
	public int exchange_count;
	public String memo;

	@Override
	public void paser(JSONObject json) throws Exception {

		id = json.optInt("id", 0);
		name = json.optString("name");
		shortname = json.optString("shortname");
		coin = json.optInt("coin", 0);
		remain = json.optInt("remain", 0);
		img_small = json.optString("smallimg");
		img = json.optString("img");
		type = json.optInt("type");
		memo = json.optString("memo");
		status = json.optInt("status", 0);
		exchange_count = json.optInt("exchange_count", 0);
	}
}