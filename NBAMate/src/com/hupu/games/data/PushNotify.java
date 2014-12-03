/**
 * 
 */
package com.hupu.games.data;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.hupu.games.common.HupuLog;

import android.net.Uri;

/**
 * @author panyongjun
 * 
 */
// {"name":"wall","args":[{"room":"USER_NOTIFY","content":"coins=14&box=0&qid=235"}]}
public class PushNotify extends BaseEntity {

	// coin=12&box=铜宝箱&qid=56

	public String content;
	public int coin;
	public int box;
	public int qid;
    public int gid;
    public int lid;
    public int roomid;
	@Override
	public void paser(JSONObject json) throws Exception {
		content = json.optString("content");
//		content = "coins=14&box=0&qid=235";
		if (content != null) {
			String[] parameters = content.split("&");
			String[] ss;
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			for (String s : parameters) {
				ss = s.split("=");
				map.put(ss[0], Integer.parseInt(ss[1]));
			}

			coin = map.get("coins");
			box = map.get("box");
			qid = map.get("qid");
			gid = map.get("gid");
			lid = map.get("lid");
			roomid = map.get("roomid");
			HupuLog.e("Notify", "coin=" + coin + " ;box=" + box+" ;qid="+qid);
		}

	}

}
