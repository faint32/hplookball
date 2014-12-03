/**
 * 
 */
package com.hupu.games.data;

import org.json.JSONObject;

/**
 * @author panyongjun 发现里的数据 
 * lid: "101", 
 * en: "bbs", 
 * name: "论坛", 
 * logo:"http://b1.hoopchina.com.cn/games/leagues/bbs.png", 
 * show_template:"browser", 
 * show_default_tab:"http://m.hupu.com/bbs?from=kanqiu&utm_source=kanqiu&utm_medium=kanqiu&utm_content=bbs&utm_campaign=kanqiu"
 */
public class DiscoveryEntity extends BaseEntity {

	public int mLid;
	public String mEn;
	public String mName;
	public String mLogo;
	public String mTemplate;
	public String mDefaultTab;

	@Override
	public void paser(JSONObject json) throws Exception {
		mLid =json.optInt("lid");
		mEn =json.optString("en");
		mName=json.optString("name");
		mLogo=json.optString("logo");
		mTemplate =json.getString("show_template");
		mDefaultTab =json.getString("show_default_tab");
	}
}
