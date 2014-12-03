package com.hupu.games.data.personal.box;

import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class BoxOpenEntity extends BaseEntity {

	/** 1 宝箱开启成功, -1 宝箱名非法, -2 用户未登陆, -3 用户没有该宝箱 */
	public int status;
	/** 类型 “coin” 金币， “prize” 奖品 */
	public int type;
	/** 表示本次获取的金币数 */
	public int coin;
	
	public int balance;
	/**
	 * 奖品信息，如果为金币，data为数字，；如果为奖品，返回奖品信息 {"name": 奖品名称, "img": 图片地址}
	 * */
	private String typeName;

	public String name;
	
	public String img_url;

	@Override
	public void paser(JSONObject json) throws Exception {
		JSONObject result = json.optJSONObject(KEY_RESULT);
		status = result.optInt(KEY_STATUS);
		if (status == 1) {
			JSONObject data = result.optJSONObject(KEY_DATA);
			typeName =result.optString("type");
			if (typeName.equals("coin")) {
				// 金币				
				coin = data.optInt("coin");
				balance = data.optInt("total_coins");
				type = 1;
			} else {
				type = 2;
				name = data.optString("name", null);
				img_url = data.optString("img", null);
			}
		}

	}

}
