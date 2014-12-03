package com.pyj.common;

import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 移动设备相关信息类
 * */
public class DeviceInfo {

	private Context context;
	private float scale;
	private float screenWidth;
	private float screenHeight;

	private DeviceInfo(Context c) {
		context = c;
		instance = this;
		// scale = context.getResources().getDisplayMetrics().density;
	}

	public static void init(Context c) {
		new DeviceInfo(c);
	}

	public float getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(float screenWidth) {
		this.screenWidth = screenWidth;
	}

	public float getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(float screenHeight) {
		this.screenHeight = screenHeight;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public static DeviceInfo instance;

	/** 是否有可用的网络 */
	public static boolean isNetWorkEnable(Context context) {
		// return isWifi(context)|| isMobile(context);
		return isNetAvailable(context);
	}

	/**
	 * @Description Checking if wifi is available
	 * @param context
	 *            Context
	 * @return true: wifi network is available, false: wifi network is
	 *         unavailable.
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (ni == null)
			return false;
		NetworkInfo.State st = ni.getState();
		return ni.getState() == State.CONNECTED;
		// boolean isWifiAvail = ni.isAvailable();
		// boolean isWifiConnect = ni.isConnected();
		// return isWifiAvail && isWifiConnect;
	}

	/**
	 * @Description Checking if Mobile network is available
	 * @param context
	 *            Context
	 * @return true: mobile network is available, false: mobile network is
	 *         unavailable.
	 */
	public static boolean isMobile(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (ni == null)
			return false;
		return ni.getState() == State.CONNECTED;
		// boolean isMobileAvail = ni.isAvailable();
		// boolean isMobileConnect = ni.isConnected();
		// return isMobileAvail && isMobileConnect;
	}

	/**
	 * @Description Checking if Mobile network is available
	 * @param context
	 *            Context
	 * @return true: mobile network is available, false: mobile network is
	 *         unavailable.
	 */
	public static boolean isNetAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null)
			return false;
		return ni.getState() == State.CONNECTED;

	}

	/** 是否有sd卡 */
	public static boolean isSdcardExist() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static int getNetWorkType(Context context) {
		if (isWifi(context))
			return -1;
		TelephonyManager telephoneManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephoneManager.getNetworkType();
	}

	public static final String CTWAP = "ctwap";
	public static final String CTNET = "ctnet";
	public static final String CMWAP = "cmwap";
	public static final String CMNET = "cmnet";
	public static final String WAP_3G = "3gwap";
	public static final String NET_3G = "3gnet";
	public static final String UNIWAP = "uniwap";
	public static final String UNINET = "uninet";
	
	public static final int TYPE_NET_WORK_DISABLED = 0;// 网络不可用
	public static final int TYPE_CM_CU_WAP = 4;// 移动联通wap10.0.0.172
	public static final int TYPE_CT_WAP = 5;// 电信wap 10.0.0.200
	public static final int TYPE_OTHER_NET = 6;// 电信,移动,联通,wifi 等net网络
	public static Uri PREFERRED_APN_URI = Uri
			.parse("content://telephony/carriers/preferapn");

	public static String getNetWorkName(Context context) {
		if (isWifi(context))
			return "wifi";
		TelephonyManager telephoneManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		final int netType = telephoneManager.getNetworkType();

		// 注意二：
		// 判断是否电信wap:
		// 不要通过getExtraInfo获取接入点名称来判断类型，
		// 因为通过目前电信多种机型测试发现接入点名称大都为#777或者null，
		// 电信机器wap接入点中要比移动联通wap接入点多设置一个用户名和密码,
		// 所以可以通过这个进行判断！

		final Cursor c = context.getContentResolver().query(PREFERRED_APN_URI,
				null, null, null, null);
		if (c != null) {
			c.moveToFirst();
			final String user = c.getString(c.getColumnIndex("user"));
			if (user != null) {
				if (user.startsWith(CTWAP)) 
					return CTWAP;
				else if(user.startsWith(CTNET))
					return CTNET;
			}
		}
		c.close();
		
		ConnectivityManager connectionManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
		// 注意三：
		// 判断是移动联通wap:
		// 其实还有一种方法通过getString(c.getColumnIndex("proxy")获取代理ip
		// 来判断接入点，10.0.0.172就是移动联通wap，10.0.0.200就是电信wap，但在
		// 实际开发中并不是所有机器都能获取到接入点代理信息，例如魅族M9 （2.2）等...
		// 所以采用getExtraInfo获取接入点名字进行判断

		String netMode = networkInfo.getExtraInfo();
		if (netMode != null) {
			// 通过apn名称判断是否是联通和移动wap
			netMode = netMode.toLowerCase();
			if (netMode.equals(CMWAP)) {
				return CMWAP;
			} else if (netMode.equals(WAP_3G)) {
				return WAP_3G;
			} else if (netMode.equals(UNIWAP)) {
				return UNIWAP;
			}else if (netMode.equals(CMNET)) {
				return CMNET;
			} else if (netMode.equals(NET_3G)) {
				return NET_3G;
			}else if (netMode.equals(UNINET))
				return UNINET;
		}
        return netMode+" netType:"+netType;
	}

	public static boolean is3G(Context context) {
		TelephonyManager telephoneManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		final int type = telephoneManager.getNetworkType();
		switch (type) {

		// 联通3g
		case TelephonyManager.NETWORK_TYPE_UMTS:
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			// 电信3g
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return true;
			// 移动或者联通2g
		case TelephonyManager.NETWORK_TYPE_GPRS:
		case TelephonyManager.NETWORK_TYPE_EDGE:
			// 电信2g
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return false;
		default:
			return false;
		}

	}

	/**
	 * 获取屏幕分辨率
	 * */
	public static int getResolution(Activity act) {
		int Width = 0;
		int Height = 0;
		Width = act.getWindowManager().getDefaultDisplay().getWidth();
		Height = act.getWindowManager().getDefaultDisplay().getHeight();

		return Width * Height;
	}

	public static boolean bChineseVersion() {
		return instance.context.getResources().getConfiguration().locale
				.toString().contains("zh");
	}

	private static String deviceId;

	public static String getDeviceInfo(Context context) {
		if (deviceId != null)
			return deviceId;

		TelephonyManager tel = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		try {
			deviceId = tel.getDeviceId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (deviceId == null) {
			deviceId = instance.getAndroidId();
			// Log.d("client=", deviceId);
		}
		// System.out.println("devicId==="+"model=" + android.os.Build.MODEL +
		// ",release="
		// + android.os.Build.VERSION.RELEASE + ",sim=" + sim
		// + ",deviceId=" + dId);
		// deviceId = "model=" + android.os.Build.MODEL + ",release="
		// + android.os.Build.VERSION.RELEASE + ",sim=" + sim
		// + ",deviceId=" + dId);
		return deviceId;
	}

	private String getAndroidId() {
		return android.provider.Settings.Secure.getString(
				context.getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);
	}

	public static String getUUID() {
		return UUID.randomUUID().toString();
	}
}
