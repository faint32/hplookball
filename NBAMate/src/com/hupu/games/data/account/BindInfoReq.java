/**
 * 
 */
package com.hupu.games.data.account;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.HuPuApp;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.BaseEntity;
import com.hupu.games.data.BindEntity;

/**
 * @author papa 请求手机绑定
 */
public class BindInfoReq extends BaseEntity {

	public LinkedList<BindEntity> bindInfo;
	public String nickName;
	public int isLogin;

	@Override
	public void paser(JSONObject json) throws Exception {
		isLogin = json.optInt("is_login");
		json = json.optJSONObject(KEY_RESULT);
		SharedPreferencesMgr.setString(HuPuRes.KEY_NICK_NAME, json.optString("nickname"));
		JSONArray binds = json.optJSONArray("bind");

		if (binds != null) {
			BindEntity bind;
			bindInfo = new LinkedList<BindEntity>();
			for (int i = 0; i < binds.length(); i++) {
				bind = new BindEntity();
				bind.paser(binds.getJSONObject(i));
				bindInfo.add(bind);
				SharedPreferencesMgr.setInt("channel" + bind.channel,
						bind.is_bind);
				if (bind.channel == 1) {
					SharedPreferencesMgr.setString("bp", bind.bind_name);
				}
				if (bind.channel == 2) {
					SharedPreferencesMgr.setString("qq_name", bind.bind_name);
				}
				if (bind.channel == 3) {
					SharedPreferencesMgr.setString("hupu_name", bind.bind_name);
				}
			}
		}

	}

}
