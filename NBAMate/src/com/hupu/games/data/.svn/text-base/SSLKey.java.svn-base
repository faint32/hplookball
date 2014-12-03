package com.hupu.games.data;

import com.hupu.games.common.HupuLog;
import com.hupu.games.common.MD5Util;
import com.pyj.http.RequestParams;

public class SSLKey {

	public static String getSSLSign(RequestParams params,String salt)
	{
		return MD5Util.md5(params.getSortURL(salt));
	}
}
