package com.hupu.games.common;
/**
 * 防止按钮快速重复点击
 * @author songzhenhua
 *
 */
public class DoubleClickUtil {
	private static long lastClickTime;
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if ( 0 < timeD && timeD < 500) {   
			return true;   
		}   
		lastClickTime = time;   
		return false;   
	}
}
