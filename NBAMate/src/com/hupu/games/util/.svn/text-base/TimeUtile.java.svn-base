package com.hupu.games.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class TimeUtile {

	public static String gettime(String data) {
		String sR = "";

		Date date = new Date();

		SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String times = from.format(date);
		try {
			long re_mm = getCompareDate(data, times);
			if (re_mm <= 0) {
				sR = "1分钟前";
			}
			if (re_mm > 0 && re_mm < 60) {
				sR = String.valueOf(re_mm) + "分钟前";
			}

			if (re_mm < 60 * 24 && re_mm >= 60) {
				sR = String.valueOf(re_mm / 60) + "小时前";
			}

			if (re_mm >= 60 * 24 && re_mm < 60 * 24 * 7) {
				sR = String.valueOf(re_mm / (60 * 24)) + "天前";
			}

			if (re_mm >= 60 * 24 * 7 && re_mm < 60 * 24 * 30) {
				sR = String.valueOf(re_mm / (60 * 24 * 7)) + "周前";
			}

			if (re_mm >= 60 * 24 * 30 && re_mm < 60 * 24 * 365) {
				sR = String.valueOf(re_mm / (60 * 24 * 30)) + "个月前";
			}

			if (re_mm >= 60 * 24 * 365) {
				sR = String.valueOf(re_mm / (60 * 24 * 365)) + "年前";
			}

		} catch (ParseException e) {

			e.printStackTrace();
		}

		return sR;
	}
	
	public static String getReplytime(String data) {

		String sR = "";

		Date date = new Date();

		SimpleDateFormat from = new SimpleDateFormat("MM-dd HH:mm");

		String times = from.format(date);

		try {
			long re_mm = getCompareDate(data, times);
			if (re_mm <= 0) {
				sR = "1分钟前";
			}
			if (re_mm > 0 && re_mm < 60) {
				sR = String.valueOf(re_mm) + "分钟前";
			}

			if (re_mm < 60 * 24 && re_mm >= 60) {
				sR = String.valueOf(re_mm / 60) + "小时前";
			}

			if (re_mm >= 60 * 24){
				sR = data;
			}

		} catch (ParseException e) {

			e.printStackTrace();
		}

		return sR;
	}

	public static long getCompareDate(String startDate, String endDate)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
		Date date1 = formatter.parse(startDate);
		Date date2 = formatter.parse(endDate);
		long l = date2.getTime() - date1.getTime();
		long d = l / (60 * 1000);
		return d;
	}

}
