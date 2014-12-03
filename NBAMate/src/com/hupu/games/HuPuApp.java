package com.hupu.games;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.RFC2109Spec;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.activity.HupuHomeActivity;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.common.SocketIoHandler;
import com.hupu.games.data.AdressEntity;
import com.hupu.games.data.BaseEntity;
import com.hupu.games.data.FollowResp;
import com.hupu.games.data.JsonPaserFactory;
import com.hupu.games.data.LeaguesEntity;
import com.hupu.games.data.TeamValueEntity;
import com.hupu.games.data.TeamsEntity;
import com.hupu.games.db.HuPuDBAdapter;
import com.hupu.games.handler.ISocketCallBack;
import com.hupu.http.BaseHttpClient;
import com.koushikdutta.async.http.socketio.ConnectCallback;
import com.koushikdutta.async.http.socketio.SocketIOClient;
import com.koushikdutta.async.http.socketio.SocketIORequest;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.pyj.BaseApplication;
import com.pyj.common.DeviceInfo;
import com.pyj.common.MyUtility;
import com.pyj.http.AsyncHttpClient;
import com.pyj.http.AsyncHttpResponseHandler;
import com.pyj.http.RequestParams;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Constants;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

public class HuPuApp extends BaseApplication implements ISocketCallBack {
	public BaseHttpClient mHttpClient;
	private static LinkedList<Integer> mListFollowedTeam;
	// private static LinkedList<Integer> mListFollowedGames;
	private static HuPuApp instance;
	public String cachePath;

	public static boolean needNotify = true;

	Intent filter;

	public static boolean isDebugMode = true;

	public static boolean isShouFa = false;

	public boolean doPost;

	public final static String QQ_APP_ID = "100560807";

	public static final String XIAOMI_APP_ID = "2882303761517135243";
	public static final String XIAOMI_APP_KEY = "5821713550243";
	public static final String XIAOMI_TAG = "com.hupu.games";

	public static int wxMsgCode = 213;
	/**
	 * 退出
	 * */
	public static int loginSuccess = 0;
	public static boolean bStart;

	private RandomAccessFile in;

	private java.nio.channels.FileLock lock;

	public HuPuApp() {
		mHttpClient = new AsyncHttpClient();
		// mHttpClient = new HupuOkHttpClient();

		mListFollowedTeam = new LinkedList<Integer>();
		// mListFollowedGames =new LinkedList<Integer> ();
		instance = this;
		filter = new Intent("QUIT_NOTIFY");
		doPost = false;
		HupuLog.isDebug = isDebugMode;
		com.umeng.socialize.utils.Log.LOG = false;
		// getRedirector();
		HupuLog.d("app=" + android.os.Process.myPid());
	}

	@Override
	public void onCreate() {
		super.onCreate();
		bStart = true;
		SharedPreferencesMgr.init(this, "hupugamemate");
		//HuPuMountInterface.initMountService(this);
		initTeamDatasByDB();
		needNotify = SharedPreferencesMgr.getBoolean("is_push", true);
		// ----
		IntentFilter ift = new IntentFilter();
		ift.addAction("QUIT_NOTIFY");
		registerReceiver(new AppReceiver(), ift);
		
		//cookie set to http 监听
		initCookieHandler();

		LoggerInterface newLogger = new LoggerInterface() {
			@Override
			public void setTag(String tag) {
				// ignore
			}

			@Override
			public void log(String content, Throwable t) {
				Log.d("HUPU", content, t);
			}

			@Override
			public void log(String content) {
				Log.d("HUPU", content);
			}
		};
		Logger.setLogger(this, newLogger);

	}

	public long getServerToday() {
		if (mHttpClient != null && mHttpClient.today > 0) {
			return MyUtility.getZeroTime(mHttpClient.today / 1000);
		}
		return -1;
	}

	private static String mDeviceId;

	public String getDeviceID() {
		if (mDeviceId == null) {
			mDeviceId = DeviceInfo.getDeviceInfo(this);
			if (mDeviceId == null || mDeviceId.length() < 2) {
				mDeviceId = SharedPreferencesMgr.getString("clientid", null);
				if (mDeviceId == null) {
					mDeviceId = DeviceInfo.getUUID();
					SharedPreferencesMgr.setString("clientid", mDeviceId);
				}
			}
		}
		return mDeviceId;
	}

	/**
	 * 获取当前程序版本名
	 * 
	 * @return
	 */
	public String getPackageVersion() {
		String version = "";
		try {
			PackageManager pm = getPackageManager();
			PackageInfo pi = null;
			pi = pm.getPackageInfo(getPackageName(), 0);
			version = pi.versionName + "/" + pi.versionCode;
		} catch (Exception e) {
			version = ""; // failed, ignored
		}
		return version;
	}

	public void setNotify(boolean b) {
		needNotify = b;
		SharedPreferencesMgr.setBoolean("is_push", needNotify);
	}

	public void quit() {
		// 清空网络请求队列
		if (mHttpClient != null)
			mHttpClient.cancelRequests(this, true);
		mHttpClient = null;
		if (mIoClient != null && mIoClient.isConnected())
			mIoClient.disconnect();
		// mIoClient =null;
		// for test
		// Intent intent =new Intent(this,HuPuGamemateService.class);
		// stopService(intent);
		// test end
		bStart = false;
		onBackground();
		// int pid = android.os.Process.myPid();
		// Log.d("Hupu app", "pid="+pid);
		super.quit();
	}

	public static LinkedList<Integer> getFollowTeams() {
		return mListFollowedTeam;
	}

	public LinkedList<Integer> loadFollowTeam() {
		HuPuDBAdapter mDbAdapter = new HuPuDBAdapter(this);
		mListFollowedTeam = mDbAdapter.getFollowTeams();
		return mListFollowedTeam;
	}

	public LinkedList<LeaguesEntity> loadLeagues() {

		HuPuDBAdapter mDbAdapter = new HuPuDBAdapter(this);
		LinkedList<LeaguesEntity> leaguesEntities = mDbAdapter.getAllLeagues();

		for (int i = 0; i < leaguesEntities.size(); i++) {
			leaguesEntities.get(i).mList = mDbAdapter
					.getTeamsByLid(leaguesEntities.get(i).lid);
		}

		return leaguesEntities;
	}

	public void insertLeaguesFrist(LinkedList<LeaguesEntity> list) {
		// 组合顺序
		String order = ""; // 记录顺序规则
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).is_follow == 1) {
				order += list.get(i).lid + ",";
			}
		}
		
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).is_follow == 0) {
				order += list.get(i).lid + ",";
			}
		}
		SharedPreferencesMgr.setString("league_order", order);
		
		HuPuDBAdapter mDBAdapter = new HuPuDBAdapter(this);
		
		mDBAdapter.delLeagues();
		
		mDBAdapter.insertLeagueFrist(list);
		
		mDBAdapter.close();
		
		/*
		 * mDBAdapter.delTeam();
		 * 
		 * for (int i = 0; i < list.size(); i++) {
		 * mDBAdapter.insertTeam(list.get(i).mList, list.get(i).lid); }
		 */
	}
	
	public void insertLeagues(LinkedList<LeaguesEntity> list) {
		// 组合顺序
//		 String order = ""; // 记录顺序规则
//		 for (int i = 0; i < list.size(); i++) {
//		 if (list.get(i).is_follow == 1) {
//		 order += list.get(i).lid + ",";
//		 }
//		 }
//		
//		 for (int i = 0; i < list.size(); i++) {
//		 if (list.get(i).is_follow == 0) {
//		 order += list.get(i).lid + ",";
//		 }
//		 }
//		 SharedPreferencesMgr.setString("league_order", order);

		HuPuDBAdapter mDBAdapter = new HuPuDBAdapter(this);

		mDBAdapter.delLeagues();

		mDBAdapter.insertLeague(list);

		mDBAdapter.close();

		/*
		 * mDBAdapter.delTeam();
		 * 
		 * for (int i = 0; i < list.size(); i++) {
		 * mDBAdapter.insertTeam(list.get(i).mList, list.get(i).lid); }
		 */
	}

	public boolean sendHttpRequest(int ReqId, String tag,
			RequestParams mParams, AsyncHttpResponseHandler handler) {
		if (DeviceInfo.isNetWorkEnable(this)) {
			handler.reqCode = ReqId;
			try {
				mHttpClient.get(this, HuPuRes.getUrl(ReqId, tag), mParams, handler);
				return true;
				
			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}
		} else
			return false;
	}

	/**
	 * 新闻已读信息插入
	 * 
	 * @param nid
	 */
	public void insertIsRead(int nid) {
		HuPuDBAdapter mDBAdapter = new HuPuDBAdapter(this);
		mDBAdapter.insertIsRead(nid);
	}

	/**
	 * 获取新闻是否已读
	 * 
	 * @param nid
	 * @return
	 */
	public int getIsRead(int nid) {
		HuPuDBAdapter mDbAdapter = new HuPuDBAdapter(this);
		return mDbAdapter.getIsRead(nid);
	}

	public void insertTeams(LinkedList<LeaguesEntity> list) {

		HuPuDBAdapter mDBAdapter = new HuPuDBAdapter(this);
		mDBAdapter.delTeam();
		mDBAdapter.delDiscovery();

		LeaguesEntity entity = null;
		for (int i = 0; i < list.size(); i++) {
			entity = list.get(i);
			if ("discovery".equals(entity.en))
				mDBAdapter.insertDiscoveryData(entity.mDiscoverList);
			else
				mDBAdapter.insertTeam(entity.mList, entity.lid);
		}
		mDBAdapter.close();
		initTeamDatasByDB();
	}
	
	public void updateTeams(LinkedList<LeaguesEntity> list) {

		HuPuDBAdapter mDBAdapter = new HuPuDBAdapter(this);
		mDBAdapter.delTeam();

		LeaguesEntity entity = null;
		for (int i = 0; i < list.size(); i++) {
			entity = list.get(i);
			if (!"discovery".equals(entity.en)){
				mDBAdapter.insertTeam(entity.mList, entity.lid);
			}
		}
		mDBAdapter.close();
		initTeamDatasByDB();
	}

	public static String getFollowTeamsNames(LinkedList<Integer> list) {
		String ss = "";
		if (list == null)
			list = mListFollowedTeam;

		TeamValueEntity data = null;
		int size = list.size();
		for (int i = 0; i < size; i++) {

			data = getTeamData(list.get(i));
			ss += data.str_name;
			if (i == 1 && size == 2)
				break;
			if (i == 1 && size > 2) {
				ss += "等";
				break;
			}
			if (i != size - 1)
				ss += "、";
		}
		return ss;
	}

	/**
	 * 批量关注球队。 由于关注页面已经被finish，所以不能把该接口写在BaseActivity中。
	 * **/
	public void followTeams(LinkedList<Integer> list, Activity act) {
		RequestParams mParams = new RequestParams();
		StringBuffer sb = new StringBuffer();
		int size = list.size();
		for (int i = 0; i < size; i++) {
			sb.append(list.get(i));
			if (i != size - 1)
				sb.append(',');
		}
		mParams.put(BaseEntity.KEY_TEAM_IDS, sb.toString());
		mParams.put("client", getDeviceID());
		if (DeviceInfo.isNetWorkEnable(this)) {
			mHttpClient
					.get(this, HuPuRes
							.getUrl(HuPuRes.REQ_METHOD_SET_FOLLOW_TEAMS),
							mParams, new TeamsHandler(list,
									HuPuRes.REQ_METHOD_SET_FOLLOW_TEAMS));
		} else {
			// Toast.makeText(act, "没有网络，请稍候再试。", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 批量提交关注联赛和球队信息
	 * 
	 * @param listTeams
	 */
	public void followLeague(LinkedList<LeaguesEntity> leaguesEntities) {

		RequestParams mParams = new RequestParams();
		JSONObject jsonObject;
		JSONArray jsonArray = new JSONArray();

		for (int i = 0; i < leaguesEntities.size(); i++) {
			jsonObject = new JSONObject();
			try {
				//jsonObject.put(leaguesEntities.get(i).lid + "", 1);
				 //注释说明：之前提交是否关注的信息！
				 jsonObject.put(leaguesEntities.get(i).lid + "",
				 leaguesEntities.get(i).is_follow);
				jsonArray.put(jsonObject);
				// Log.i("papa", jsonObject.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// jsonArray.toString();
		mParams.put("token", SharedPreferencesMgr.getString("tk", ""));
		mParams.put("lids", jsonArray.toString());
		Log.i("papa", jsonArray.toString());
		if (DeviceInfo.isNetWorkEnable(this)) {
			mHttpClient.post(this,
					HuPuRes.getUrl(HuPuRes.REQ_METHOD_FOLLOW_LEAGUE)
							+ "?client=" + mDeviceId, mParams,
					new AsyncHttpResponseHandler());
		} else {

		}
	}

	/**
	 * 提交关注球队信息(设置界面使用 单独提交球队关注信息)
	 * 
	 * @param listTeams
	 */

	public void followOnlyTeams(LinkedList<LeaguesEntity> leaguesEntities) {
		RequestParams mParams = new RequestParams();
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray;
		for (int i = 0; i < leaguesEntities.size(); i++) {
			jsonArray = new JSONArray();
			for (int j = 0; j < leaguesEntities.get(i).mList.size(); j++) {
				if (leaguesEntities.get(i).mList.get(j).is_follow == 1) {
					jsonArray.put(leaguesEntities.get(i).mList.get(j).tid);
				}
			}
			try {
				jsonObject.putOpt(leaguesEntities.get(i).lid + "", jsonArray);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		mParams.put("token", SharedPreferencesMgr.getString("tk", ""));
		mParams.put("lids", jsonObject.toString());
		// HupuLog.d("lids="+jsonObject.toString());
		/*
		 * sendRequest(HuPuRes.REQ_METHOD_FOLLOW_ALL_TEAM, mParams, new
		 * HupuHttpHandler(this), true);
		 */
		if (DeviceInfo.isNetWorkEnable(this)) {
			mHttpClient.post(this,
					HuPuRes.getUrl(HuPuRes.REQ_METHOD_FOLLOW_ALL_TEAM)
							+ "?client=" + mDeviceId, mParams,
					new OnlyTeamsHandler(leaguesEntities,
							HuPuRes.REQ_METHOD_FOLLOW_ALL_TEAM));
		}
	}

	/** 关注球队回调handler */
	class OnlyTeamsHandler extends AsyncHttpResponseHandler {
		private LinkedList<LeaguesEntity> listTeams;

		public OnlyTeamsHandler(LinkedList<LeaguesEntity> list, int code) {
			listTeams = list;
			reqCode = code;
		}

		@Override
		public void onSuccess(String content, int reqType) {

		}

		@Override
		public void onFailure(Throwable error, int reqType) {
			// TODO Auto-generated method stub

		}
	}

	public void insertTeamsToDB(LinkedList<Integer> listTeams) {
		mListFollowedTeam = listTeams;
		HuPuDBAdapter mDbAdapter = new HuPuDBAdapter(this);
		mDbAdapter.delTeams();
		mDbAdapter.insertTeams(listTeams);
	}

	/**
	 * 批量关注球队数据响应handle类
	 * 
	 * **/
	class TeamsHandler extends AsyncHttpResponseHandler {

		private LinkedList<Integer> listTeams;

		public TeamsHandler(LinkedList<Integer> list, int code) {
			listTeams = list;
			reqCode = code;
		}

		@Override
		public void onSuccess(String content, int reqType) {
			FollowResp resp = (FollowResp) JsonPaserFactory.paserObj(content,
					reqType);
			if (resp.i_success == 1) {
				/** 将列表插入数据库中 */
				insertTeamsToDB(listTeams);
				// SharedPreferencesMgr.setBoolean("isFirst", false);
			}
		}

		@Override
		public void onFailure(Throwable error, int reqType) {

		}

	}

	class RedirectorHandler extends AsyncHttpResponseHandler {

		public RedirectorHandler(int code) {
			reqCode = code;
		}

		@Override
		public void onSuccess(String content, int reqType) {
			AdressEntity entity = (AdressEntity) JsonPaserFactory.paserObj(
					content, reqType);
			setServer(entity.mArrAdress);
			recntTime = 0;
			initConnect();
			//
		}

		@Override
		public void onFailure(Throwable error, int reqType) {
			reconnect(false);
		}
	}

	// -------------------初始化球队数据------------------------------------------//
	private static HashMap<Integer, TeamValueEntity> mapTeams;

	/**
	 * 获取球队的基本数据
	 * 
	 * @param tid
	 *            球队的id
	 * @return 球队的基本数据
	 * */
	public static TeamValueEntity getTeamData(int tid) {
		if (mapTeams.get(tid) != null)
			return mapTeams.get(tid);
		else {
			TeamValueEntity defaultTeams = TeamValueEntity.getDefault(tid);
			addToTeams(tid, defaultTeams);
			return defaultTeams;
		}
	}

	public static boolean hasTeam(int tid, String name) {
		TeamValueEntity tt = mapTeams.get(tid);
		if (tt == null) {
			TeamValueEntity defaultTeams = TeamValueEntity.getDefault(tid);
			defaultTeams.str_name = name;
			addToTeams(tid, defaultTeams);
			return false;
		} else {
			if (tt.str_name == null) {
				tt.str_name = name;
				return false;
			}
		}
		return true;
	}

	/**
	 * 添加非nba球队到球队列表中
	 * */
	public static void addToTeams(int id, TeamValueEntity defaultTeams) {
		mapTeams.put(id, defaultTeams);
	}

	boolean initTeamDatasByDB() {
		HuPuDBAdapter mDbAdapter = new HuPuDBAdapter(this);
		LinkedList<TeamsEntity> list = mDbAdapter.getTeamsByLid(1);
		if (list.size() == 0)
			return false;

		mapTeams = new HashMap<Integer, TeamValueEntity>();
		TeamValueEntity entity;
		int isLite = SharedPreferencesMgr.getInt("is_lite", 0);
		for (TeamsEntity en : list) {
			entity = new TeamValueEntity();
			entity.i_tid = en.tid;
			int c = Integer.parseInt(en.color, 16);
			c |= 0xff000000;
			entity.i_color = c;
			entity.str_name = en.name;
			entity.str_name_en = en.enName;
			if (isLite == 0) {
				entity.i_logo = TeamValueEntity.ICON_RES[entity.i_tid - 1];
				entity.i_logo_small = TeamValueEntity.ICON_RES_SMALL[entity.i_tid - 1];
			} else {
				entity.i_logo = TeamValueEntity.ICON_RES_LITE[entity.i_tid - 1];
				entity.i_logo_small = TeamValueEntity.ICON_RES_LITE[entity.i_tid - 1];
			}
			mapTeams.put(entity.i_tid, entity);
		}
		return true;
	}

	/**
	 * 程序是否在前台运行
	 * 
	 * @return
	 */
	public boolean isAppOnForeground() {
		// Returns a list of application processes that are running on the
		// device

		ActivityManager activityManager = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();

		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean treatErr(Throwable ex) {
		Intent intent = new Intent(getApplicationContext(),
				HupuHomeActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		startActivity(intent);
		return false;
	}

	private boolean bBackground = true;

	private long backgroundTime;

	public void onBackground() {
		// Log.d("HuPuApp", "onBackground startService ");
		bBackground = true;
		if (needNotify) {
			try {
				if (lock != null) {
					lock.release();
				}
				if (in != null)
					in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 打开推送
			// HupuPushInterface.startPushService(this);
			startXiaoMiPush();
		}

		leaveRoom(jsonRoom);
		backgroundTime = System.currentTimeMillis();
		// 半小时后自动退出。
		quitLater(QUIT_INTERVAL);

	}

	class AppReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (bBackground) {
				quit();
			}
			// Log.d("AppReceiver", "intent"+intent.getLongExtra("time", 0));
		}
	}

	private void cancelAlarm() {
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.cancel(getPendingIntent());
	}

	private static final long QUIT_INTERVAL = 60000 * 10;

	/** 定时关闭 */
	private void quitLater(long later) {
		cancelAlarm();
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		long firstWake = System.currentTimeMillis() + later;
		am.set(AlarmManager.RTC_WAKEUP, firstWake, getPendingIntent());
	}

	private PendingIntent getPendingIntent() {

		filter.putExtra("time", backgroundTime);
		return PendingIntent.getBroadcast(this, 0, filter,
				PendingIntent.FLAG_UPDATE_CURRENT);

	}

	public void onForeground() {
		// Log.d("HuPuApp",
		// "onForeground startService  >>>>>>:::::"+bBackground);
		if (bBackground) {
			cancelAlarm();
			File file = new File(this.getFilesDir() + "/pushapp.lock");
			try {
				if (!file.exists()) {
					file.createNewFile();
				}
				in = new RandomAccessFile(this.getFilesDir() + "/pushapp.lock",
						"rw");
				lock = in.getChannel().lock();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// HupuPushInterface.startPushService(this);
			stopXiaoMiPush();
		}
		bBackground = false;
	}

	/** 获取重定向地址 */
	private void getRedirector() {
		mHttpClient.get(this, HuPuRes.getUrl(HuPuRes.REQ_METHOD_REDIRECTOR)
				+ "?client=" + mDeviceId, null, new RedirectorHandler(
				HuPuRes.REQ_METHOD_REDIRECTOR));
	}

	public void setServer(String server[]) {
		SharedPreferencesMgr.setString("default_server", server[0]);
		SharedPreferencesMgr.setString("backup_server", server[1]);
		HuPuRes.setServer(server[0], server[1]);
	}

	// ------------------------以下是实时接口---------------------//

	/** ------------socket---------------------- **/

	// private SocketIO socketClient;
	private JSONObject jsonRoom;
	private String joinRoomName;
	private String leaveRoomName;
	//private boolean isLeaveRoom = false;

	private SocketIoHandler mSocketHandler;

	protected boolean bSocketConnect;

	public int i_net_state;
	public final static int STATE_NO_NET = 1;
	/** 连接中 */
	public final static int STATE_CONNECTING = 3;

	/** 连接成功 */
	public final static int STATE_CONNECTED = 4;

	/** 连接断开 */
	public final static int STATE_DISCONNECT = 5;

	/** 在线 */
	public final static int STATE_ON_LINE = 7;

	public final static int STATE_NET_ERR = 8;

	private HupuBaseActivity mAct;

	private ConcurrentHashMap<String, HupuBaseActivity> roomsMap;

	public boolean isSocketConn() {
		return bSocketConnect;
	}

	public void setNetState(int ns) {
		i_net_state = ns;
	}

	public void registActivity(String name, HupuBaseActivity a) {
		if (roomsMap == null)
			roomsMap = new ConcurrentHashMap<String, HupuBaseActivity>();
		// if(roomsMap.get(name)!=null)
		if (name != null)
			roomsMap.put(name, a);
		mAct = a;
	}

	/** 初始化socketio的连接 */
	public void joinRoom(JSONObject jRoom) {

		jsonRoom = jRoom;

		if (mIoClient != null && mIoClient.isConnected()) {
			// leaveRoom();
			mIoClient.emitJsonObj("join", jsonRoom);
			joinRoomName = jsonRoom.optString("room");
			HupuLog.d("APP join >>>>>>:::::" + jsonRoom.toString());
		} else
			initConnect();
	}

	/** emit */
	public void emit(String emit, JSONObject Request) {

		if (mIoClient != null && mIoClient.isConnected()) {
			mIoClient.emitJsonObj(emit, Request);
			HupuLog.d(emit + "APP join >>>>>>:::::" + Request.toString());
		} else
			initConnect();
	}

	/**
	 * 请求离开room 为了解决前一个页面在onStop中调用leaveRoom
	 * 可能发生在当前页面joinRoom之后，造成刚join进入房间就被leave了 在子Activity中的onStop中调用
	 * */
	public void reqLeaveRoom(HupuBaseActivity act) {
		// 如果当前活动页不是该页
		if (mAct == act) {
			leaveRoom(jsonRoom);
		}
	}

	/** 离开room */
	public void leaveRoom(JSONObject jRoom) {
		if (mIoClient != null && mIoClient.isConnected()) {
			HupuLog.d(" APP leaveRoom >>>>>>:::::"+jRoom.toString());
			mIoClient.emitJsonObj("leave", jRoom);
		}
	}

	public void leaveRoom(String activityName,JSONObject jRoom) {
		if (mIoClient != null && mIoClient.isConnected()) {
			HupuLog.d("Appp=" + activityName + " leaveRoom >>>>>>:::::"+jRoom.toString());
			mIoClient.emitJsonObj("leave", jRoom);
		}
	}

	public void disconnect() {
		HupuLog.d("Appp" + "==disconnect=====");
		if (mIoClient != null && mIoClient.isConnected()) {
			mIoClient.disconnect();
		}
	}

	private final int RECON_INTEVAL = 60000;
	Handler handler = new Handler();
	/* 重连次数 */
	int recntTime;
	private static final int MAX_TIME = 2;

	Handler recHandler = new Handler();

	Runnable run;

	/** 断线或网络错误后重新链接 */
	public void reconnect(boolean now) {
		if (bBackground)
			return;
		if (mIoClient != null && mIoClient.isConnected()) {
			onSocketConnect();
			return;
		}
		HupuLog.d("reconnect==recntTime=====" + recntTime);
		int interval;
		if (DeviceInfo.isNetWorkEnable(this)) {
			if (now)
				interval = 5000;
			else
				interval = 20000;// 60秒后重连
		} else {
			interval = RECON_INTEVAL;
			i_net_state = STATE_NO_NET;// 没有网络，180秒后重连
		}
		if (run != null) {
			recHandler.removeCallbacks(run);
			run = null;
		}
		run = new Runnable() {
			@Override
			public void run() {
				recntTime++;
				if (recntTime < MAX_TIME) {
					initConnect();
				} else {
					if (mIoClient != null) {
						mIoClient.disconnectByMan();
						mIoClient = null;
						request = null;
						// 重新获取redireconnect地址
					}
					getRedirector();
				}
			}

		};
		recHandler.postDelayed(run, interval);
	}

	public void onReconnect() {
		HupuLog.d("onReconnect========");
		// if(mIoClient ==null || !mIoClient.isConnected())
		reconnect(true);
	}

	public void onSocketConnect() {
		HupuLog.d("onSocketConnect========");
		// System.out.println(" onSocketConnect");
		recntTime = 0;
		i_net_state = STATE_CONNECTED;
		bSocketConnect = true;
		if (mAct != null && !mAct.isFinishing())
			mAct.onSocketConnect();
	}

	public void onSocketDisconnect() {
		HupuLog.d("onSocketDisconnect========");
		// System.out.println("on onDisconnect bSocketConnect= " +
		// bSocketConnect);
		bSocketConnect = false;
		i_net_state = STATE_DISCONNECT;
		if (bBackground)
			return;
		if (mAct != null && !mAct.isFinishing())
			mAct.onSocketDisconnect();
	}

	private HashMap<String, String> UMENG_MAP = new HashMap<String, String>();

	public void onSocketResp(JSONArray obj) {
		// System.out.println("on===" + obj);
		i_net_state = STATE_ON_LINE;
		if (mAct != null && !mAct.isFinishing()) {
			if (obj != null)
				try {
					mAct.onSocketResp(obj.getJSONObject(0));
				} catch (JSONException e) {
					e.printStackTrace();
				}
		}
	}

	/** 更新socket网络状态。 */
	public void updateNetState() {

		String ss;

		// Log.d("HupuApp", "updateNetState  >>>>>>:::::");

		switch (i_net_state) {
		case STATE_NO_NET:
			ss = "无网络";
			break;
		/** 连接中 */
		case STATE_CONNECTING:
			ss = "连接中";
			break;

		/** 连接成功 */
		case STATE_CONNECTED:
			ss = "已连接";
			break;
		/** 连接断开 */
		case STATE_DISCONNECT:
			ss = "断线";
			break;
		/** 在线 */
		case STATE_ON_LINE:
			ss = "在线";
			break;
		case STATE_NET_ERR:
			ss = "连接错误";
			break;
		}
	}

	SocketIORequest request;
	SocketIOClient mIoClient;
	SocketIoHandler mIoHandler;

	/** 初始化socketio */
	private void initConnect() {

		// 清除自动重连线程。
		if (run != null) {
			recHandler.removeCallbacks(run);
			run = null;
		}

		if (mIoClient != null) {
			mIoClient.disconnect();
			// mIoClient.reconnect();
			HupuLog.d("initConnect========AAA");
		}

		HupuLog.d("initConnect========BBB");
		if (request == null)
			request = new SocketIORequest(getDefaultServer(), "/nba_v1",
					getParameter());
		if (mIoHandler == null)
			mIoHandler = new SocketIoHandler(this);
		SocketIOClient.connect(com.koushikdutta.async.http.AsyncHttpClient
				.getDefaultInstance(), request, new ConnectCallback() {

			@Override
			public void onConnectCompleted(Exception ex, SocketIOClient client) {
				mIoClient = client;
				if (ex != null) {
					ex.printStackTrace();
					return;
				}

				onSocketConnect();
				client.setStringCallback(mIoHandler);
				client.setReconnectCallback(mIoHandler);
				client.setDisconnectCallback(mIoHandler);
				client.setJSONCallback(mIoHandler);
				client.addListener("wall", mIoHandler);
			}
		});

	}

	private String socket_server;

	public String getDefaultServer() {
		if (socket_server != null)
			return socket_server;
		if (socket_server == null
				&& SharedPreferencesMgr.getString("default_server", null) != null) {
			socket_server = "http://"
					+ SharedPreferencesMgr.getString("default_server", null);
			return socket_server;
		}
		return null;
	}

	public static String getParameter() {
		HupuLog.d("socketID client=" + mDeviceId + "&t="
				+ System.currentTimeMillis() / 1000 + "&type=1"
				+ "&background=false");
		return "client=" + mDeviceId + "&t=" + System.currentTimeMillis()
				/ 1000 + "&type=1" + "&background=false";
	}

	@Override
	public void onSocketError(Exception socketIOException) {
		// System.out.println("on err" + socketIOException.toString());
		bSocketConnect = false;
		i_net_state = STATE_NET_ERR;
		if (mAct != null && !mAct.isFinishing())
			mAct.onSocketError(socketIOException);

		UMENG_MAP.put("socket", "failed");
		// MobclickAgent.onEvent(this, HuPuRes.UMENG_KEY_NETWORK, UMENG_MAP);

	}

	// 响应webview 长按

	public void setWebonLongClick(final String url) {
		UrlImageViewHelper.loadUrlDrawable(this, url, new downLoadOk());
	}

	class downLoadOk implements UrlImageViewCallback {

		@Override
		public void onLoaded(ImageView imageView, final Bitmap loadedBitmap,
				String url, boolean loadedFromCache) {
			// TODO Auto-generated method stub
			// 监听 view 是否绘制完成！
			saveBitmap(url, loadedBitmap);
		}
	}

	public void saveBitmap(String url, Bitmap loadedBitmap) {

		// TODO Auto-generated method stub
		// 监听 view 是否绘制完成！
		HupuLog.e("papa", "load-url===" + url);
		String path = Environment.getExternalStorageDirectory().toString()
				+ "/hupu/games/image";
		File fDir = new File(path);
		if (!fDir.exists()) {
			fDir.mkdirs();
		}
		String fileName = url.substring(url.lastIndexOf("/"),
				url.lastIndexOf(".") - 1).hashCode()
				+ url.substring(url.lastIndexOf(".") - 1, url.length());
		fDir = new File(path, fileName);
		if (fDir.exists()) {
			fDir.delete();
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(fDir);
			loadedBitmap.compress(Bitmap.CompressFormat.JPEG, 95, out);
			out.flush();
			out.close();
			fileScan(fDir);
			Toast.makeText(
					HuPuApp.this,
					HuPuApp.this.getString(R.string.in_sd) + "hupu/games/image",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// if (out!=null) {
			// try {
			//
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
		}

	}

	public void fileScan(File filePath) {
		Uri data = Uri.fromFile(filePath);
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));
	}

	private void startXiaoMiPush() {

		// 将push服务切换到正式服务器
		Constants.useOfficial();
		// Constants.useSandbox();
		// 初始化push推送服务
		if (shouldInit()) {
			MiPushClient.registerPush(this, XIAOMI_APP_ID, XIAOMI_APP_KEY);
		}

	}

	private void stopXiaoMiPush() {
		// MiPushClient.pausePush(this, null);
		// MiPushClient.unregisterPush(this);
	}

	private boolean shouldInit() {
		ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
		List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
		String mainProcessName = getPackageName();

		int myPid = android.os.Process.myPid();
		for (RunningAppProcessInfo info : processInfos) {
			if (info.pid == myPid && mainProcessName.equals(info.processName)) {
				return true;
			}
		}
		return false;
	}

	public static void syncCookiesFromAppCookieManager(String url,
			DefaultHttpClient httpClient) {

		BasicCookieStore cookieStore = new BasicCookieStore();
		httpClient.setCookieStore(cookieStore);

		CookieManager cookieManager = CookieManager.getInstance();

		if (cookieManager == null)
			return;

		RFC2109Spec cookieSpec = new RFC2109Spec();
		String rawCookieHeader = null;
		try {

			URL parsedURL = new URL(url);

			// rawCookieHeader = cookieManager.getCookie(parsedURL.getHost());
			rawCookieHeader = cookieManager.getCookie(parsedURL.getHost());
			if (rawCookieHeader == null)
				return;

			int port = parsedURL.getPort() == -1 ? parsedURL.getDefaultPort()
					: parsedURL.getPort();

			CookieOrigin cookieOrigin = new CookieOrigin(parsedURL.getHost(),
					port, "/", false);
			List<Cookie> appCookies = cookieSpec.parse(new BasicHeader(
					"set-cookie", rawCookieHeader), cookieOrigin);


			cookieStore.addCookies(appCookies.toArray(new Cookie[appCookies
					.size()]));
		} catch (MalformedURLException e) {
			// Handle Error
		} catch (MalformedCookieException e) {
			// Handle Error
		}
	}

	public static void syncCookiesToAppCookieManager(String url,
			DefaultHttpClient httpClient) {

		CookieStore clientCookieStore = httpClient.getCookieStore();
		List<Cookie> cookies = clientCookieStore.getCookies();
		if (cookies.size() < 1)
			return;

		CookieSyncManager syncManager = CookieSyncManager.getInstance();
		CookieManager appCookieManager = CookieManager.getInstance();
		if (appCookieManager == null)
			return;

		// Extract any stored cookies for HttpClient CookieStore
		// Store this cookie header in Android app CookieManager
		for (Cookie cookie : cookies) {
			// HACK: Work around weird version-only cookies from cookie
			// formatter.
			if (cookie.getName() == "$Version")
				break;
			String setCookieHeader = cookie.getName() + "=" + cookie.getValue()
					+ "; Domain=" + cookie.getDomain() + ";expires="
					+ cookie.getExpiryDate().toGMTString()+";HTTPOnly";

			//HupuLog.e("papa", "---"+setCookieHeader);
			appCookieManager.setCookie(cookie.getDomain(), setCookieHeader);
			
		}

		// Sync CookieManager to disk if we added any cookies
		syncManager.sync();
		clientCookieStore.clear();
	}
	public static void syncCookiesToAppCookieManager(Header header,int reqId) {
		CookieSyncManager syncManager = CookieSyncManager.getInstance();
		CookieManager appCookieManager = CookieManager.getInstance();
		if (appCookieManager == null)
			return;
		appCookieManager.setCookie(HuPuRes.getUrl(reqId, ""), header.getValue());
	}
	
	 void initCookieHandler() {
	        final DefaultHttpClient httpClient = ((AsyncHttpClient) mHttpClient).getDefaultHttpClient();
	        final CookieManager cookieManager = CookieManager.getInstance();

	        httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
	            @Override
	            public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
	                Header host = httpRequest.getFirstHeader("host");
	                if (host == null) return;

	                String url = "http://" + host.getValue() + httpRequest.getRequestLine().getUri();
	                // Cleanup
	                httpRequest.removeHeaders("cookie");

	                String cookie = cookieManager.getCookie(url);
	                if (cookie != null) {
	                    httpRequest.addHeader("cookie", cookie);
	                }
	            }
	        });

	        httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
	            @Override
	            public void process(HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
	                if (httpClient.getCookieStore() != null) {
	                    httpClient.getCookieStore().clear();
	                    HupuLog.d("cookie", "clear obsoleted store");
	                }
	            }
	        });
	    }

}
