package com.pyj;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.os.Handler;
import android.util.Log;

import com.hupu.games.common.HupuLog;
import com.pyj.common.DeviceInfo;

public class BaseApplication extends Application implements
		UncaughtExceptionHandler {

	private ArrayList<Activity> actList;

	static String pakage;

	private Thread.UncaughtExceptionHandler mDefaultHandler;

	public BaseApplication() {
		DeviceInfo.init(this);
		actList = new ArrayList<Activity>();
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);

	}

	public int getVerCode() {
		int verCode = -1;
		try {
			verCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
			Log.i("GBA", "vercode == " + verCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return verCode;
	}

	public String getVerName() {
		String verName = "";
		try {
			verName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return verName;
	}

	/**
	 * 退出
	 * */
	@SuppressLint("NewApi")
	public void quit() {
		// 清空网络请求队列
		// client.cancelAll();
		// 清空手机应用信息

		int version = android.os.Build.VERSION.SDK_INT;

		clearAllAct();
		if (version <= 7) {
			System.out.println("   version  < 7");
			ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			manager.restartPackage(getPackageName());
		} else {
			ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			manager.killBackgroundProcesses(getPackageName());
		}
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				 int pid = android.os.Process.myPid();
				 android.os.Process.killProcess(pid);
			}
		},200);
		
		// System.exit(0);
	}

	public void addActivitToStack(Activity act) {
		actList.add(act);
	}

	public void removeFromStack(Activity act) {
		actList.remove(act);
	}

	public void clearAllAct()
	{
		
		for (Activity act : actList) {
			if (!act.isFinishing())
				act.finish();
		}
		actList.clear();
	}
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		ex.printStackTrace();
		Log.e("MyApp===", "" + ex.toString());
		if (!treatErr(ex) && mDefaultHandler != null)
			mDefaultHandler.uncaughtException(thread, ex);
		else
			quit();

	}

	public boolean treatErr(Throwable ex) {
		return false;
	}

}
