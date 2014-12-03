package com.hupu.games.activity;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.hupu.games.HuPuApp;
import com.hupu.games.R;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.HupuScheme;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.InitResp;
import com.hupu.games.data.LeaguesEntity;
import com.hupu.games.data.NotificationEntity;
import com.hupu.games.pay.HupuUserBindActivity;
import com.hupu.http.HupuHttpHandler;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.mato.sdk.proxy.Proxy;
import com.pyj.common.DeviceInfo;
import com.pyj.common.DialogRes;
import com.pyj.http.AsyncHttpClient;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.ReportPolicy;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.update.UmengUpdateAgent;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageHelper;

/**
 * 打开应用时的加载页 第一 先去获取用户的昵称 第二 如果是初次使用或者是用户删除了本地数据，先到服务端同步下关注的球队列表
 * 
 * @author panyongjun
 * */
public class LaunchActivity extends HupuBaseActivity {

	private boolean toHome, isToAdview;

	private InitResp leagueListEntity;

	private HupuScheme mScheme;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mApp = (HuPuApp) getApplication();
		mApp.clearAllAct();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_launch);
	
		if (SharedPreferencesMgr.getInt("is_maa", 0) == 1) {
//			Proxy.start(this);
//			((AsyncHttpClient)mApp.mHttpClient).setWangsu();
			//HupuLog.d("proxy start"+ Proxy.start(this));
		}
		
		setUMeng();
		getSchemeData();
		leagueListEntity = new InitResp();
		leagueListEntity.mList = new LinkedList<LeaguesEntity>();

		mToken = SharedPreferencesMgr.getString("tk", null);
		imgADS = (ImageView) findViewById(R.id.img_ads);
		// showADSView();
		isToAdview = false;
		setTimiOutToUmeng();
	}
	
	//开子线程 发送超时事件到umeng；
	boolean isStop = false; 
	private void setTimiOutToUmeng(){
		new Handler().postDelayed(new Runnable() {
			public void run() {
				if (!isStop) {
					MobclickAgent.onEvent(LaunchActivity.this, "Http_TimeOut_5");
				}
			}
		}, 10000);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		isStop = true;
		super.onDestroy();
	}
	
	private boolean getMiPushData()
	{
		Intent in = getIntent();
		MiPushMessage message = (MiPushMessage) in.getSerializableExtra(PushMessageHelper.KEY_MESSAGE);
		if(message !=null)
		{
			NotificationEntity entity = new NotificationEntity();
			HupuLog.d("getMiPushData="+message.getDescription());
			HupuLog.d("getMiPushData="+message.getContent());
			JSONObject json;
			try {
				json = new JSONObject(message.getContent());
				entity.paser(json);
				mScheme =entity.mScheme;
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	/**
	 * 解析从通知点击进入或直接通过filter进入的scheme
	 * */
	private void getSchemeData() {
		Intent in = getIntent();
//		if (in.getBooleanExtra("click", false)) {
		if(getMiPushData()){
			// 通过通知点击进入
			sendUmeng(HuPuRes.UMENG_EVENT_NOTIFICATION, HuPuRes.UMENG_KEY_CLICK);
//			mScheme = (HupuScheme) in.getSerializableExtra("scheme");
//			HupuLog.d("getSchemeData===="+mScheme.mode);
		} else {
			// 通过filter进入的scheme
			Uri data = getIntent().getData();
			// Uri data=Uri.parse("kanqiu://cba/cba/chat/3560");
			// Uri data=Uri.parse("kanqiu://nba/nba/news/1651032");

			if (data != null && "kanqiu".equalsIgnoreCase(data.getScheme())) {
				// 如果是看球scheme
				mScheme = new HupuScheme();
				mScheme.paser(data);
			}
		}
	}

	/**
	 * 初始化友盟的一些设置
	 * */
	private void setUMeng() {
		// 统计错误日志
//		MobclickAgent.onError(this);
		// 任何情况下都要升级
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		// 实时发送umeng
//		MobclickAgent
//				.setDefaultReportPolicy(this, ReportPolicy.BATCH_AT_LAUNCH);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		// 去服务端检测
		detection();
		toHome = true;

		if (SharedPreferencesMgr.getBoolean("shortcut", false)) {
			Log.d("oncreate", "you le ");
		} else {
			SharedPreferencesMgr.setBoolean("shortcut", true);
			if (!hasShortCut("虎扑看球"))
				creatShortCut("虎扑看球", R.drawable.icon_launcher);
		}

	}

	private void detection() {
		initParameter();
		if (mToken != null) {
			mParams.put("token", mToken);
		}
		if (SharedPreferencesMgr.getString("app_version", "").equals(
				mApp.getVerName())) {
			mParams.put("dv", SharedPreferencesMgr.getString("sdv", ""));
		} else {
			mParams.put("dv", "");
		}
		try {
			ApplicationInfo appInfo = this.getPackageManager()
					.getApplicationInfo(getPackageName(),
							PackageManager.GET_META_DATA);
			String channel = appInfo.metaData.getString("UMENG_CHANNEL");
			if (channel == null || channel.equals("")) {
				channel = appInfo.metaData.getInt("UMENG_CHANNEL") + "";
			}
			mParams.put("channel", channel);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sendRequest(HuPuRes.REQ_METHOD_USER_INIT, mParams, new HupuHttpHandler(
				this), false);

	}

	private void startToNextScreen() {
		if (isFinishing())
			return;
		if (DeviceInfo.isNetWorkEnable(this)) {
			if (!isToAdview) {

				if (toHome) {
					Intent intent = new Intent(this, HupuHomeActivity.class);
					if (mScheme != null) {
						intent.putExtra("scheme", mScheme);
					}
					startActivity(intent);
				} else {
					Intent intent = new Intent(this, HupuLaunchActivity.class);
					startActivity(intent);
				}
				finish();
			}

		} else {
			showDialog(DialogRes.DIALOG_ID_NETWORK_NOT_AVALIABLE);
		}

	}

	/**
	 * 广告需求；跳转到广告页面
	 * 
	 */
	private void startToAdview() {
		isToAdview = true;
		sendUmeng(HuPuRes.UMENG_EVENT_PROMOTION, HuPuRes.UMENG_KEY_SPLASH,
				HuPuRes.UMENG_VALUE_CLICKBTN);

		Intent adIntent = new Intent(this, AdWebviewActivity.class);
		if (mScheme != null) {
			adIntent.putExtra("scheme", mScheme);
		}
		adIntent.putExtra("ad_url", leagueListEntity.adLink);
		adIntent.putExtra("ad_title", leagueListEntity.adTitle);

		startActivity(adIntent);
		this.finish();
	}

	@Override
	public void clickPositiveButton(int dialogId) {
		super.clickPositiveButton(dialogId);
		if (DialogRes.DIALOG_ID_NETWORK_NOT_AVALIABLE == dialogId) {
			// 重连
			detection();
		}
	}

	@Override
	public void clickNegativeButton(int dialogId) {

		if (DialogRes.DIALOG_ID_NETWORK_NOT_AVALIABLE == dialogId) {
			quit();
		}
		super.clickNegativeButton(dialogId);
	}

	@Override
	public void onErrResponse(Throwable error, String content,
			boolean isBackGroundThread) {
		showDialog(DialogRes.DIALOG_ID_NETWORK_NOT_AVALIABLE);

	}

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		if (methodId == HuPuRes.REQ_METHOD_USER_INIT) {
			leagueListEntity = (InitResp) o;
			// add by pan

			// 如果服务端返回 联赛数据的情况下 客户端存储一份
			if (leagueListEntity.mList != null) {
				// 缓存当前版本号
				SharedPreferencesMgr
						.setString("app_version", mApp.getVerName());
				mApp.insertLeagues(leagueListEntity.mList);
				mApp.insertTeams(leagueListEntity.mList);
			}

			if (SharedPreferencesMgr.getInt("is_maa", 0) == 0) {
				Proxy.stop();
				//((AsyncHttpClient)mApp.mHttpClient).setWangsu();
				//HupuLog.d("proxy start"+ Proxy.start(this));
			}else 
				Proxy.start(this);
//			leagueListEntity.showBind=1;//debug
			// 服务端给定 是否进入关注页
			if (leagueListEntity.showFollow == 1) {
				mApp.insertLeagues(leagueListEntity.mList);// 把联赛数据备份到本地数据库
				new Handler().postDelayed(new Runnable() {
					public void run() {
						toHome = false; // 表示跳转到关注联赛页
						startToNextScreen();
					}
				}, 200);
			} else if(leagueListEntity.showBind == 1){
				Intent intent = new Intent(this, HupuUserBindActivity.class);
				Bundle bd=new Bundle();
				if (mScheme != null) {
					bd.putSerializable("scheme", mScheme);
				}
				bd.putBoolean("isInit", true);//true首次登陆绑定,false使用某个功能时触发登陆后进入绑定
				intent.putExtras(bd);
				startActivity(intent);
			}else {// 不进入关注联赛页
				showADSView();
				if (leagueListEntity.adShowTime > 0) { // 广告展示时间 >0 认定为有广告要展示
					// showADSView(); // 各种分辨率处理函数
					new Handler().postDelayed(new Runnable() { // 暂停 广告展示时间后跳入
																// 主页
								public void run() {
									toHome = true;
									startToNextScreen();
								}
							}, leagueListEntity.adShowTime * 1000);
				} else { // 没有广告展示 暂停0.5秒启动页后 去主页
					new Handler().postDelayed(new Runnable() {
						public void run() {
							toHome = true;
							startToNextScreen();
						}
					}, 500);
//					toHome = true;
//					startToNextScreen();
				}
			}
			/**
			 * 接口 问题，先注释
			 */

			if (leagueListEntity.redirectors != null)
				mApp.setServer(leagueListEntity.redirectors);

			if (leagueListEntity.nickName != null)
				SharedPreferencesMgr.setString(HuPuRes.KEY_NICK_NAME,
						leagueListEntity.nickName);

		}
	}

	@Override
	protected Dialog onCreateDialog(final int id) {
		int titleId = 0;
		int msgId = 0;
		int flag = 0;
		int left = 0;
		int right = 0;
		if (DialogRes.DIALOG_ID_NETWORK_NOT_AVALIABLE == id) {
			titleId = R.string.STR_ERR_MSG;
			msgId = R.string.MSG_NO_AVAILABLE_NET;
			left = R.string.STR_RETRY;
			right = R.string.STR_QUIT;
			flag = TOW_BUTTONS;
		} else
			return super.onCreateDialog(id);
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setCancelable(true).setTitle(titleId).setMessage(msgId);
		if ((flag & 1) > 0) {
			builder.setPositiveButton(left,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							clickPositiveButton(id);
						}
					});
		}
		if ((flag & 2) > 0) {
			builder.setNegativeButton(right,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							clickNegativeButton(id);
						}
					});
		}
		mDialog = builder.create();
		return mDialog;
	}

	public void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}

	ImageView imgADS;

	/**
	 * 显示广告页
	 * */
	private void showADSView() {

		if (leagueListEntity.adShow == 1) {
			sendUmeng(HuPuRes.UMENG_EVENT_PROMOTION, HuPuRes.UMENG_KEY_SPLASH,
					HuPuRes.UMENG_VALUE_SPLASH);
			imgADS.setVisibility(View.VISIBLE);

		} else {
			imgADS.setVisibility(View.INVISIBLE);
		}
		if (leagueListEntity.adImg != null) {
			HupuLog.e("papa", "--------------" + leagueListEntity.adImg);
			// UrlImageViewHelper.setUrlDrawable(imgADS,
			// leagueListEntity.adImg, R.drawable.bg_1x1);

			UrlImageViewHelper.loadUrlDrawable(LaunchActivity.this,
					leagueListEntity.adImg, new LoadAdsOk());
			setOnClickListener(R.id.layout_launch);
		}
	}

	/**
	 * 显示广告第二页
	 * */
	private void nextPage() {
		if (isTabletDevice()) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setContentView(R.layout.layout_adversing);
		} else {
			setContentView(R.layout.layout_adversing);
		}

		setOnClickListener(R.id.img_ads_goto);
		setOnClickListener(R.id.img_ads_bg);
		// findViewById(R.id.img_ads_goto).setBackgroundResource(
		// R.drawable.fast_btn_2);
		// findViewById(R.id.img_ads_bg).setBackgroundResource(
		// R.drawable.launch_bg_2);
	}

	private GestureDetector gesture;
	private View.OnTouchListener gestureListener;

	/** 初始化手势，主要是让统计数据能够左右移动 */
	private void initGesture() {
		gesture = new GestureDetector(this, new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				try {
					gesture.onTouchEvent(event);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
			}
		};
	}

	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);
		if (R.id.img_ads_goto == id) {
			//
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://goto.hupu.com/?a=goClick&id=4381"));
			startActivity(intent);
		} else if (R.id.img_ads_bg == id) {
			toHome = true;
			startToNextScreen();
		} else if (R.id.layout_launch == id) {
			// nextPage();
			if (leagueListEntity.adShow == 1) {

				if (leagueListEntity.adLink != null) {
					Uri data = Uri.parse(leagueListEntity.adLink);
					if (data != null
							&& "kanqiu".equalsIgnoreCase(data.getScheme())) {
						// 如果是看球scheme
						mScheme = new HupuScheme();
						mScheme.paser(data);
						toHome = true;
						startToNextScreen();
						return;
					}
					if (!leagueListEntity.adLink.equals("")) {
						startToAdview();
					}
				}
			}
		}
	}

	private boolean isTabletDevice() {

		if (android.os.Build.VERSION.SDK_INT >= 11) { // honeycomb

			// test screen size, use reflection because isLayoutSizeAtLeast is
			// only available since 11
			Configuration con = getResources().getConfiguration();
			try {
				Method mIsLayoutSizeAtLeast = con.getClass().getMethod(
						"isLayoutSizeAtLeast", int.class);
				Boolean r = (Boolean) mIsLayoutSizeAtLeast.invoke(con,
						0x00000004); // Configuration.SCREENLAYOUT_SIZE_XLARGE
				return r;
			} catch (Exception x) {
				x.printStackTrace();
				return false;
			}
		}
		return false;
	}

	private boolean isPad() {
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		// 屏幕宽度
		float screenWidth = display.getWidth();
		// 屏幕高度
		float screenHeight = display.getHeight();
		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
		double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
		// 屏幕尺寸
		double screenInches = Math.sqrt(x + y);
		// 大于6尺寸则为Pad
		if (screenInches >= 6.0) {
			return true;
		}
		return false;
	}

	/** 手势监听类 */
	class MyGestureDetector extends SimpleOnGestureListener {

		private static final int SWIPE_MIN_DISTANCE = 50;
		private static final int SWIPE_MAX_OFF_PATH = 250;
		private static final int SWIPE_THRESHOLD_VELOCITY = 60;

		// Touch了滑动一点距离后，up时触发
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			Log.d("MyGesture", "onFling");
			if (e2 == null) {
				// Log.i("MyGesture", "e2==null");
				return false;
			}
			if (e1 == null) {

				return false;
			}

			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

				return true;

			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				nextPage();
				return true;
			}
			return false;

		}

		/*
		 * 两个函数都是在Touch Down后又没有滑动(onScroll)，又没有长按(onLongPress)，然后Touch Up时触发
		 * 点击一下非常快的(不滑动)Touch Up: onDown->onSingleTapUp->onSingleTapConfirmed
		 * 点击一下稍微慢点的(不滑动)Touch Up:
		 * onDown->onShowPress->onSingleTapUp->onSingleTapConfirmed
		 */
		public boolean onSingleTapConfirmed(MotionEvent e) {
			// TODO Auto-generated method stub
			return super.onSingleTapConfirmed(e);
		}

		// 双击的第二下Touch down时触发
		public boolean onDoubleTap(MotionEvent e) {
			// Log.i("MyGesture", "onDoubleTap");
			return super.onDoubleTap(e);
		}

		// 双击的第二下Touch down和up都会触发，可用e.getAction()区分
		public boolean onDoubleTapEvent(MotionEvent e) {
			// Log.i("MyGesture", "onDoubleTapEvent");
			return super.onDoubleTapEvent(e);
		}

		// Touch down时触发
		public boolean onDown(MotionEvent e) {
			// Log.i("MyGesture", "onDown");
			return super.onDown(e);
		}

		// Touch了不移动一直Touch down时触发
		public void onLongPress(MotionEvent e) {
			// Log.i("MyGesture", "onLongPress");
			super.onLongPress(e);
		}

		// Touch了滑动时触发
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// Log.i("MyGesture", "onScroll");
			return super.onScroll(e1, e2, distanceX, distanceY);
		}

		/*
		 * Touch了还没有滑动时触发 (1)onDown只要Touch Down一定立刻触发 (2)Touch
		 * Down后过一会没有滑动先触发onShowPress再触发onLongPress So: Touch Down后一直不滑动，onDown
		 * -> onShowPress -> onLongPress这个顺序触发。
		 */
		public void onShowPress(MotionEvent e) {
			// Log.i("MyGesture", "onShowPress");
			super.onShowPress(e);
		}

		public boolean onSingleTapUp(MotionEvent e) {
			// Log.i("MyGesture", "onSingleTapUp");
			return super.onSingleTapUp(e);
		}
	}

	/**
	 * 添加快捷方式
	 * */
	public void creatShortCut(String shortcutName, int resourceId) {
		Intent intent = new Intent();
		intent.setClass(this, LaunchActivity.class);
		// intent.setClass(this, LaunchActivity.class);
		/* 以下两句是为了在卸载应用的时候同时删除桌面快捷方式 */
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");

		Intent shortcutintent = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");

		// 不允许重复创建
		shortcutintent.putExtra("duplicate", false);
		// 需要显示的名称
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
		// 快捷图片
		Parcelable icon = Intent.ShortcutIconResource.fromContext(
				getApplicationContext(), resourceId);
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

		// ComponentName comp = new ComponentName(this.getPackageName(),
		// this.getPackageName() + "." + this.getLocalClassName());
		// shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
		// Intent.ACTION_MAIN).setComponent(comp));
		// 点击快捷图片，运行的程序主入口
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		// 发送广播。OK
		sendBroadcast(shortcutintent);
	}

	/**
	 * 删除程序的快捷方式
	 */
	private void delShortcut(String shortcutName) {
		Intent shortcut = new Intent(
				"com.android.launcher.action.UNINSTALL_SHORTCUT");
		// 快捷方式的名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getString(R.string.app_name));
		// 指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
		// 注意: ComponentName的第二个参数必须是完整的类名（包名+类名），否则无法删除快捷方式
		String appClass = this.getPackageName() + "."
				+ this.getLocalClassName();
		ComponentName comp = new ComponentName(this.getPackageName(), appClass);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
				Intent.ACTION_MAIN).setComponent(comp));
		sendBroadcast(shortcut);
	}

	private static final String READ_SETTINGS = "com.android.launcher.permission.READ_SETTINGS";

	/**
	 * 通过传递launcher所需要的permisson来获取本机的authority,其中传递READ_SETTINGS到permisson中。
	 * com.android.launcher.permission.READ_SETTINGS
	 * 是android中的自带的，一般改系统不会涉及到此吧。。
	 **/

	static String getAuthorityFromPermission(Context context, String permission) {
		if (permission == null)
			return null;
		List<PackageInfo> packs = context.getPackageManager()
				.getInstalledPackages(PackageManager.GET_PROVIDERS);
		if (packs != null) {
			for (PackageInfo pack : packs) {
				ProviderInfo[] providers = pack.providers;
				if (providers != null) {
					for (ProviderInfo provider : providers) {
						if (permission.equals(provider.readPermission))
							return provider.authority;
						if (permission.equals(provider.writePermission))
							return provider.authority;
					}
				}
			}
		}
		return null;
	}

	public boolean hasShortCut(String title) {
		String url = "";
		url = "content://" + getAuthorityFromPermission(this, READ_SETTINGS)
				+ "/favorites?notify=true";

		// 获取当前应用名称
		// Log.i("url:", url);

		// try {
		// final PackageManager pm = getPackageManager();
		// title = pm.getApplicationLabel(
		// pm.getApplicationInfo(getPackageName(),
		// PackageManager.GET_META_DATA)).toString();
		// } catch (Exception e) {
		// }
		ContentResolver resolver = getContentResolver();
		Cursor cursor = resolver.query(Uri.parse(url), null, "title=?",
				new String[] { title }, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.close();
			return true;
		}

		return false;
	}

	private void paserUrl1(String s) {
		Uri uri = Uri.parse(s);
		String scheme = uri.getScheme();
		if ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme)) {
		} else if ("app".equalsIgnoreCase(scheme)) {
			// 从通知中进入

		}
	}

	boolean isSet = false;

	class LoadAdsOk implements UrlImageViewCallback {

		@Override
		public void onLoaded(ImageView imageView, final Bitmap loadedBitmap,
				String url, boolean loadedFromCache) {
			// TODO Auto-generated method stub
			try {
				float scaleWidth = ((float) imgADS.getWidth())
						/ loadedBitmap.getWidth();

				Matrix matrix = new Matrix();
				matrix.postScale(scaleWidth, scaleWidth);
				HupuLog.e("papa",
						"比例：" + scaleWidth + "--ADS_width：" + imgADS.getWidth()
						+ "--bitmapWidth：" + loadedBitmap.getWidth());
				Bitmap image = Bitmap.createBitmap(loadedBitmap, 0, 0,
						loadedBitmap.getWidth(), loadedBitmap.getHeight(),
						matrix, true);

				imgADS.setImageBitmap(image);

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}
}
