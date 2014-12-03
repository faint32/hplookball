/**
 * 
 */
package com.hupu.games.data.account;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.HuPuApp;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.BaseEntity;
import com.hupu.games.data.BindEntity;

/**
 * @author panyongjun
 * 请求手机绑定
 */
public class PhoneBindReq extends BaseEntity {

	//验证成功
	/**代表用户身份的令牌，在需要使用身份认证的接口中用来确认用户身份，比如获取账号余额、购买道具等*/
	public String token  ;  
	public int uid  ;  
	/** 用户的帐号余额*/
	String balance ; 
	public String nickName;
	public int show_bind;
	public int isVip;
	
	public JSONArray lids;
	public JSONObject tids;
	
	public LinkedList<BindEntity> bindInfo;
	
	@Override
	public void paser(JSONObject json) throws Exception {
		
		json =json.optJSONObject(KEY_RESULT);
		if(json!=null)
		{
			token = json.optString("token", null);
			uid = json.optInt("uid");
			show_bind = json.optInt("show_bind");
			HupuLog.e("papa", "token=="+uid);
			nickName = json.optString("nickname", null);
			balance= json.optString("balance", null);
			isVip=json.optInt("vip");
			
			if (json.optJSONObject("follow")!= null) {
				lids = json.optJSONObject("follow").optJSONArray("lids");
				tids = json.optJSONObject("follow").optJSONObject("tids");
			}
			
			JSONArray binds = json.optJSONArray("bind");
			
			 if (binds != null){
				 BindEntity bind;
				 bindInfo = new LinkedList<BindEntity>();
				 for (int i = 0; i < binds.length(); i++) {
					 bind = new BindEntity();
					 bind.paser(binds.getJSONObject(i));
					 bindInfo.add(bind);
					 SharedPreferencesMgr.setInt("channel"+bind.channel, bind.is_bind);
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

}
