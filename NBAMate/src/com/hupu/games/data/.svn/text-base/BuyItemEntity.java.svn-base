/**
 * 
 */
package com.hupu.games.data;

import org.json.JSONObject;

/**
 * @author panyongjun
 * 
 */
public class BuyItemEntity extends BaseEntity {

	/**
	 * -1 令牌无效，需要重新登录 -2 购买失败 -3 余额不足 -4 在当前时间内无法使用道具 -5 比赛未开启此道具 1 购买成功，未扣费 2 购买成功，已扣费
	 * */
	public int state;

	public int pid;
	public int num;

	@Override
	public void paser(JSONObject json) throws Exception {
		state = json.optInt(KEY_RESULT, 0);
	}

}
