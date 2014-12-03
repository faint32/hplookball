package com.hupu.games.activity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.hupu.games.HuPuApp;
import com.hupu.games.R;
import com.hupu.games.casino.AuthDialog;
import com.hupu.games.casino.ShareDialog;
import com.hupu.games.common.DoubleClickUtil;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.HupuScheme;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.BaseEntity;
import com.hupu.games.data.LeaguesEntity;
import com.hupu.games.data.SSLKey;
import com.hupu.games.data.account.PhoneBindReq;
import com.hupu.games.data.account.QqLoginEntity;
import com.hupu.games.data.game.football.ScoreboardEntity;
import com.hupu.games.pay.HupuUserBindActivity;
import com.hupu.http.BaseHttpResponseHandler;
import com.hupu.http.HupuHttpException;
import com.hupu.http.HupuHttpHandler;
import com.pyj.activity.BaseActivity;
import com.pyj.common.DeviceInfo;
import com.pyj.common.DialogRes;
import com.pyj.http.RequestParams;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.BaseShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/** 所有HuPuGameMate应用Activity的基类 */
public class HupuBaseActivity extends BaseActivity {
	/** 应用类的句柄 */
	public HuPuApp mApp;
	/** http请求的参数 */
	protected RequestParams mParams;

	// public static String
	// mToken="MTM3NjQyMTMyMDA=|b12f9b61e212632555de4b3a7e0a9385";
	public static String mToken;
	public static int uid;
	/** 设备号 */

	protected static String mDeviceId;

	public final int DIALOG_SHOW_BIND_PHONE = 7733;
	/** 领取救济金 */
	public final int DIALOG_SHOW_GET_DOLE = 7735;

	public final int DIALOG_SHOW_CHARGE_NOTIFY = 7755;
	public final int DIALOG_REMOVE_BIND = 7766;
	public final int DIALOG_EXCHANGE_PRIZE = 7777;
	public final int DIALOG_EXCHANGE_SUCCESS = 7778;
	public final int DIALOG_EXCHANGE_ERROR = 7779;
	public final int DIALOG_UNBOUND = 7788;
	public final int DIALOG_ERROR = 7799;
	public final int DIALOG_SHOW_BUY_CAIPIAO = 8888;

	public final int DIALOG_RENOUNCE_BIND = 2048;

	public static final int REQ_SEND_MSG = 1000;

	public static final int REQ_SEND_WALLING = 1110;

	public static final int REQ_ZAN_CHARGE = 8865;

	public static final int REQ_GO_CHARGE = 8888;

	public static final int REQ_GO_CONFIRM_OEDER = 9988;

	public static final int REQ_GO_BIND_PHONE = 3333;

	public static final int REQ_GO_HUPU_LOGIN = 6688;

	public static final int REQ_GO_POST_ORDER = 6666;

	public static final int REQ_SETUP_NICKNAME = 5555;

	public static final int REQ_SHOW_BOX = 10000;

	public static final int REQ_SHOW_QUIZLIST = 5577;


	String userAgent;
	
	public int roomid=-1;//房间id
	public String roomPreview;//房间id

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApp = (HuPuApp) getApplication();
		if (mParams == null)
			mParams = new RequestParams();
		mDeviceId = mApp.getDeviceID();
		if (mToken == null)
			mToken = SharedPreferencesMgr.getString("tk", null);

		uid = SharedPreferencesMgr.getInt("uid", -1);
		if (userAgent == null) {
			WebView v = new WebView(this);
			userAgent = v.getSettings().getUserAgentString() + " kanqiu/"
					+ mApp.getPackageVersion();
			v = null;
			// HupuLog.d("ua="+userAgent);
			if (mApp!=null && mApp.mHttpClient!=null) {
				mApp.mHttpClient.setUserAgent(userAgent);
			}
		}

	}

	/**
	 * 发出一个Http的请求
	 * 
	 * @param methodId
	 *            方法的id，中有定义
	 * @param params
	 *            请求所需的参数
	 * @param responseHandler
	 *            请求结果处理的handler
	 * @return 是否进入请求队列
	 * */
	public boolean sendRequest(int methodId, RequestParams params,
			BaseHttpResponseHandler responseHandler) {
		return sendRequest(methodId, params, responseHandler, false);
	}

	public boolean sendRequest(int methodId, RequestParams params,
			BaseHttpResponseHandler responseHandler, boolean showDialog) {
		return sendRequest(methodId, "", params, responseHandler, showDialog);
	}

	public boolean sendRequest(int methodId, String namespace,
			RequestParams params, BaseHttpResponseHandler responseHandler,
			boolean showDialog) {
		if (DeviceInfo.isNetWorkEnable(this)) {
			try {
				mApp.mHttpClient.updateUserAgent(DeviceInfo.getNetWorkType(this));

			} catch (Exception e) {
				// TODO: handle exception
			}
			i_curState = STATE_CONNECTING;
			responseHandler.reqCode = methodId;
			try {
				if (methodId > HuPuRes.REQ_METHOD_POST) {
					if (mDeviceId != null && params != null) {
						mApp.mHttpClient.post(this, HuPuRes.getUrl(methodId, namespace)
								+ "?client=" + mDeviceId, params, responseHandler);
					}else 
						return false;
					
				} else {
					mApp.mHttpClient.get(this, HuPuRes.getUrl(methodId, namespace),
							params, responseHandler);
				}
			} catch (Exception e) {
				Toast.makeText(this, this.getString(R.string.http_error_str), Toast.LENGTH_SHORT).show();
				MobclickAgent.onEvent(this, "Http_Error_5");
				return false;
				// TODO: handle exception
			}

			//请求是带上webview 内的cookie
			//			HupuLog.e("papa", "设置http cookie");
			//			mApp.syncCookiesFromAppCookieManager(HuPuRes.getUrl(methodId, namespace)
			//					+ "?client=" + mDeviceId, ((AsyncHttpClient)mApp.mHttpClient).getDefaultHttpClient());

			if (showDialog)
				showDialog(DialogRes.DIALOG_ID_NET_CONNECT);
			return true;
		} else {
			// showDialog(DialogRes.DIALOG_ID_NETWORK_NOT_AVALIABLE);
			if (showDialog)
				showDialog(DialogRes.DIALOG_ID_NETWORK_NOT_AVALIABLE);
			else
				Toast.makeText(this, this.getString(R.string.http_error_str), Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	/**
	 * 取消该页面所有请求的链接
	 * */
	public void cancelConnection() {
		i_curReqTimes = 0;
		mApp.mHttpClient.cancelRequests(this, true);
	}

	protected boolean bBackGround;

	@Override
	protected void onStop() {

		if (i_curState == STATE_SHOW_DIALOG && mDialog.isShowing())
			mDialog.cancel();
		if (i_curReqTimes > 0)
			cancelConnection();
		i_curState = STATE_STOP;
		if (!mApp.isAppOnForeground()) {
			bBackGround = true;
			onBackground();
		}
		super.onStop();
		HupuLog.d("" + getClass().getSimpleName(), "onStop");
		//HuPuMountInterface.onStop(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		HupuLog.d("" + getClass().getSimpleName(), "onResume");
		if (mApp.isAppOnForeground()) {
			bBackGround = false;
			onForeground();
		}
		MobclickAgent.onResume(this);
		//HuPuMountInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		//HuPuMountInterface.onPause(this);
	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//HuPuMountInterface.onDestroy(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (i_curState == STATE_CONNECTING) {
				// removeDialog(DialogRes.DIALOG_ID_NET_CONNECT);
				cancelConnection();
			}
			finish();
		}
		return false;
	}

	/** 退出应用 */
	public void quit() {
		// 退出socket
		disconnect();
		mApp.quit();
	}

	@Override
	public void onReqResponse(Object o, int methodId) {

		if (o != null && ((BaseEntity) o).err != null) {
			onErrResponse(new HupuHttpException(((BaseEntity) o).err), methodId);
		}
		if (mDialog != null && mDialog.isShowing())
			mDialog.dismiss();
		removeDialog(DialogRes.DIALOG_ID_NET_CONNECT);
		if (methodId == HuPuRes.REQ_METHOD_USER_BIND) {
			if (o != null) {
				PhoneBindReq entity = (PhoneBindReq) o;
				// HupuLog.e("papa", entity.token);
				updateBindInfo(entity);
			}
		}

		//将所有cookie 存储到cookieManager 中
		//mApp.syncCookiesToAppCookieManager(HuPuRes.getUrl(methodId,""), ((AsyncHttpClient)mApp.mHttpClient).getDefaultHttpClient());

	}



	@Override
	public void onReqResponse(Object o, int methodId, int msgid1, int msgid2) {
		// TODO Auto-generated method stub

		//将所有cookie 存储到cookieManager 中
		//mApp.syncCookiesToAppCookieManager(HuPuRes.getUrl(methodId,""), ((AsyncHttpClient)mApp.mHttpClient).getDefaultHttpClient());
	}

	public void setToday(long t) {
		mApp.mHttpClient.today = t;
	}

	public HashMap<String, String> UMENG_MAP = new HashMap<String, String>();

	@Override
	public void onErrResponse(Throwable error, int type) {
		if(error instanceof HupuHttpException){
			HupuLog.e("papa", "----------");
			super.onErrResponse(error, type);
			showToast(error.toString());
		}else {
			showToast(getString(R.string.http_error_str));
		}
		if (mDialog != null && mDialog.isShowing())
			mDialog.dismiss();
		MobclickAgent.onEvent(this, "Http_Error_5");
	}
	public void onErrMsg(String msg, int type)
	{
		showToast(msg);
		if (mDialog != null && mDialog.isShowing())
			mDialog.dismiss();
	}

	/**
	 * @param categray
	 *            eventID
	 * @param key
	 *            key
	 * @param value
	 *            value
	 * */
	public void sendUmeng(String categray, String key, String value) {
		UMENG_MAP = new HashMap<String, String>();
		UMENG_MAP.put(key, value);
		MobclickAgent.onEvent(this, categray + "_5", UMENG_MAP);
		//dace 事件统计
		//HuPuMountInterface.onEvent(this, categray + "_5", UMENG_MAP);

		// showToast("事件：" + categray +"_5"+ "--------key： "+key +
		// "-----value："+ value);
	}

	public void sendUmeng(String categray, String key) {
		MobclickAgent.onEvent(this, categray + "_5", key);
		//dace 事件统计
		//HuPuMountInterface.onEvent(this, categray + "_5", key);
		// showToast("事件：" + categray +"_5"+ "--------key： "+key);
	}

	public void sendUmeng(String event) {
		MobclickAgent.onEvent(this, event + "_5");
		//dace 事件统计
		//HuPuMountInterface.onEvent(this, event + "_5");
		// showToast("事件："+event+"_5");
	}

	public void sendUmengByMap(String event) {
		MobclickAgent.onEvent(this, event + "_4", UMENG_MAP);
		//dace 事件统计
		//HuPuMountInterface.onEvent(this, event + "_4", UMENG_MAP);
		// showToast("事件：" + event + "key数量："+UMENG_MAP.size());
	}

	public void followTeam(int lid, int tid, byte isFollow) {
		initParameter();
		mParams.put("lid", "" + lid);
		mParams.put(BaseEntity.KEY_TEAM_ID, "" + tid);
		mParams.put("is_follow", "" + isFollow);
		if (isFollow > 0) {
			sendRequest(HuPuRes.REQ_METHOD_FOLLOW_ONE_TEAM, mParams,
					new HupuHttpHandler(this));
		} else
			sendRequest(HuPuRes.REQ_METHOD_FOLLOW_ONE_TEAM_CANCEL, mParams,
					new HupuHttpHandler(this));
	}

	/**
	 * 关注或取消比赛，当unFollow>0的情况下，取消关注，利用该实例来处理回调数据。
	 * */
	public void followGame(int gId, byte unFollow) {
		followGame(gId, unFollow, new HupuHttpHandler(this));
	}

	/**
	 * 关注或取消比赛，当unFollow>0的情况下，取消关注，利用该实例来处理回调数据。
	 * */
	public void followGame(int lid, int gId, byte unFollow) {
		followGame(lid, gId, unFollow, new HupuHttpHandler(this));
	}

	/**
	 * 关注或取消比赛，当unFollow>0的情况下，取消关注
	 * */
	private void followGame(int gId, byte unFollow,
			BaseHttpResponseHandler handler) {
		followGame(0, gId, unFollow, handler);

	}

	/**
	 * 关注或取消比赛，当unFollow>0的情况下，取消关注
	 * */
	public void followGame(int lid, int gId, byte unFollow,
			BaseHttpResponseHandler handler) {
		mParams.remove(BaseEntity.KEY_UNFOLLOW);
		mParams.put(BaseEntity.KEY_GAME_ID, "" + gId);
		mParams.remove("lid");
		if (lid == 0) {
			if (unFollow > 0) {
				mParams.put(BaseEntity.KEY_UNFOLLOW, "" + unFollow);
				sendRequest(HuPuRes.REQ_METHOD_FOLLOW_NBA_GAME_CANCEL, mParams,
						handler);
			} else {
				sendRequest(HuPuRes.REQ_METHOD_FOLLOW_NBA_GAME, mParams,
						handler);
			}
		} else {
			mParams.put("lid", "" + lid);
			// Log.d("unFollow",
			// "unFollow ="+unFollow+" ;gid="+gId+"  ;lid="+lid);
			if (unFollow > 0) {
				mParams.put(BaseEntity.KEY_UNFOLLOW, "" + unFollow);
				sendRequest(HuPuRes.REQ_METHOD_FOLLOW_GAME_CANCEL, mParams,
						handler);
			} else {
				sendRequest(HuPuRes.REQ_METHOD_FOLLOW_GAME, mParams, handler);
			}
		}

	}

	/**
	 * 初始化http发送参数 client为必选项
	 * **/
	public RequestParams initParameter() {
		mParams.clear();
		mParams.put("client", mDeviceId);
		//统一加上roomid
		if (roomid > -1) {
			mParams.put("roomid", roomid+"");
		}
		if (mToken != null)
			mParams.put("token", mToken);
		return mParams;
	}

	// ------------------------以下是实时接口---------------------//

	public void reconnect(boolean now) {
		mApp.reconnect(now);
	}

	private String strRoomName;

	/**
	 * 获取房间名
	 * 
	 * @return 房间名
	 */
	public String getRoom() {
		return strRoomName;
	}

	public JSONObject jsonRoom;

	/** 设置房间 */
	public JSONObject setRoomObj(String room) {
		jsonRoom = getRoomObj();
		strRoomName = room;
		try {
			jsonRoom.put("room", strRoomName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonRoom;
	}

	/** 获取room参数对象 **/
	public JSONObject getRoomObj() {
		if (jsonRoom == null) {
			jsonRoom = new JSONObject();

		}
		return jsonRoom;
	}

	/** 使用默认的参数对象来加入房间 **/
	public void joinRoom() {
		mApp.registActivity(strRoomName, this);
		mApp.joinRoom(jsonRoom);
	}

	/**
	 * 加入房间
	 * 
	 * @param room
	 *            房间名
	 **/
	public void joinRoom(String room) {
		strRoomName = room;
		mApp.registActivity(strRoomName, this);
		mApp.joinRoom(setRoomObj(room));
	}

	/**
	 * 加入房间
	 * 
	 * @param room
	 *            房间名
	 **/
	public void emit(String emit, JSONObject obj) {
		mApp.registActivity(strRoomName, this);
		mApp.emit(emit, obj);
	}

	/** 离开room */
	public void leaveRoom() {
		if (mApp.isDebugMode)
			mApp.leaveRoom(getClass().getSimpleName(),jsonRoom);
		else
			mApp.leaveRoom(jsonRoom);
	}

	public void reqLeaveRoom() {
		mApp.reqLeaveRoom(this);
	}

	public void disconnect() {
		mApp.disconnect();
	}

	public void onSocketConnect() {

	}

	public void onSocketDisconnect() {

	}

	public void onSocketError(Exception socketIOException) {

	}

	public void onSocketResp(JSONObject obj) {

	}

	public void setNetTitle() {

	}

	/** 更新socket网络状态。 */
	public void updateNetState() {
		HupuLog.d("BaseActivity updateNetState  >>>>>>:::::");
	}
	
	public void showLoadingDialog() {
		mDialog = new AlertDialog.Builder(this).create();  
		mDialog.show();  
		mDialog.setTitle("");  
		mDialog.getWindow().setContentView(R.layout.dialog_loading);
	}
	public void hideLoadingDialog() {
		if(mDialog != null){
			mDialog.dismiss();
		}
	}
	/** 显示自定义的对话框 */
	public void showCustomDialog(final int dialogId, String msg, int flag,
			int button1, int button2) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this)
		.setCancelable(true).setMessage(msg);

		if ((flag & 1) > 0) {
			// 确认
			builder.setNegativeButton(button1,
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					clickPositiveButton(dialogId);
				}
			});
		}
		if ((flag & 2) > 0) {
			// 取消健
			builder.setPositiveButton(button2,
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					clickNegativeButton(dialogId);
				}
			});

		}
		mDialog = builder.create();
		mDialog.show();
	}

	/** 显示自定义的对话框 */
	public void showCustomDialog(final int dialogId, int titleId, int msgId,
			int flag, int button1, int button2) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this)
		.setCancelable(true).setMessage(msgId);
		if (titleId > 0)
			builder.setTitle(titleId);
		if ((flag & 1) > 0) {
			// 确认
			builder.setNegativeButton(button1,
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					clickPositiveButton(dialogId);
				}
			});
		}
		if ((flag & 2) > 0) {
			// 取消健
			builder.setPositiveButton(button2,
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					clickNegativeButton(dialogId);
				}
			});

		}
		mDialog = builder.create();
		mDialog.show();
	}

	/** 显示自定义的对话框 */
	public void showCustomDialog(final int dialogId, int titleId, String msgId,
			int flag, String button1, String button2) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this)
		.setCancelable(true).setMessage(msgId);
		if (titleId > 0)
			builder.setTitle(titleId);
		if ((flag & 1) > 0) {

			builder.setNegativeButton(button1,
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					clickPositiveButton(dialogId);
				}

			});
		}
		if ((flag & 2) > 0) {
			builder.setPositiveButton(button2,
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					clickNegativeButton(dialogId);
				}
			});

		}
		mDialog = builder.create();
		mDialog.show();
	}

	public void backToHome() {
		// System.out.println("back home");
		Intent in = new Intent(this, LaunchActivity.class);
		in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(in);
	}

	public void onBackground() {
		mApp.onBackground();
	}

	public void onForeground() {
		// Log.d("baseAct", "onForeground   >>>>>>:::::");
		// SharedPreferencesMgr.init(mApp, "hupugamemate");
		//
		// Intent intent = new Intent(this, HuPuGamemateService.class);
		// intent.putExtra("stop", true);
		// // startService(intent);
		// stopService(intent);
		// Log.d("baseAct", "isServiceRunning   ==true>>>>>>:::::");
		// NotificationManager notificationManager = (NotificationManager)
		// getSystemService(NOTIFICATION_SERVICE);
		// notificationManager.cancelAll();
		mApp.onForeground();
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
	}

	/**
	 * 用来判断服务是否运行.
	 * 
	 * @param context
	 * @param className
	 *            判断的服务名字：包名+类名
	 * @return true 在运行, false 不在运行
	 */

	public boolean isServiceRunning(String className) {

		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(Integer.MAX_VALUE);
		if (!(serviceList.size() > 0)) {
			return false;
		}

		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;

	}

	/** 是否需要屏幕高亮,目前的逻辑是，非比赛进行时不需要常亮 */
	public void setScreenLight(boolean on) {

		// 设置高亮
		if (on)
			getWindow()
			.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		else
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	public static interface ChatInterface {
		void reqChatData(int id);
	}

	private final static int MIN_45 = 45 * 60;
	private final static int MIN_90 = 90 * 60;
	private final static int MIN_105 =105 * 60;
	private final static int MIN_120 =120 * 60;
	public static void showTime(ScoreboardEntity data, TextView tv, int dif) {
		if (data.code == ScoreboardEntity.STATUS_START) {
			if (data.period == 1) {
				// 上半场
				if (data.process + dif < MIN_45)
					tv.setText(getTime(data.process + dif));
				else
					tv.setText("45:00+" + getTime(data.process + dif - MIN_45));
			} else if (data.period == 2) {
				// 下半场
				if (data.process + dif < MIN_90)
					tv.setText(getTime(data.process + dif));
				else
					tv.setText("90:00+" + getTime(data.process + dif - MIN_90));
			} else if (data.period == 5 ) {
				// 加时赛上
				if (data.process + dif < MIN_105)
					tv.setText(getTime(data.process + dif ));
				else
					tv.setText("105:00+" + getTime(data.process + dif - MIN_105));
				//				tv.setText(data.str_desc);
			} else if (data.period == 6)
			{
				// 加时赛下
				if (data.process + dif < MIN_120)
					tv.setText(getTime(data.process + dif));
				else
					tv.setText("120:00+" + getTime(data.process + dif - MIN_120));
			}else if(data.period ==9)
			{
				tv.setText("加时中场休息");
			}
			else
				tv.setText(data.str_desc);
		} else
			tv.setText(data.str_desc);
	}
	/**
	 * 显示点球比分
	 * @param t1 主队点球
	 * @param s1 主队点球
	 * */
	public static void showShootOut(TextView t1,TextView t2,int s1,int s2)
	{
		t1.setVisibility(View.VISIBLE);
		t2.setVisibility(View.VISIBLE);
		if(s1>-1)
		{
			t1.setText("(" + s1+")");
			t2.setText("(" + s2+")");
		}
	}
	public static void hideShootOut(TextView t1,TextView t2)
	{
		t1.setVisibility(View.GONE);
		t2.setVisibility(View.GONE);
	}
	public void showBindDialog(String dialogTitle) {
		AuthDialog dialog = new AuthDialog(this, HupuBaseActivity.this,
				dialogTitle);
		dialog.show();
		// dialog(this, SharedPreferencesMgr.getString("dialogQuize",
		// getString(R.string.casino_notify)));
	}

	// QQ 授权登陆
	public void onClickLogin(Tencent mTencent) {

		HupuLog.e("papa", "----binding_tencent");
		IUiListener listener = new BaseUiListener() {
			@Override
			protected void doComplete(Object response) {
				// Util.showResultDialog(HupuBaseActivity.this,
				// response.toString(), "登录成功");
				HupuLog.e("papa", "result=-----------" + response.toString());
				if (response instanceof JSONObject) {
					QqLoginEntity entity = new QqLoginEntity();
					entity.paser((JSONObject) response);
					// 向服务端发送 授权信息
					sendQqEntity(entity, 2);
				}

			}


		};
		mTencent.login(this, "all", listener);
	}

	private class BaseUiListener implements IUiListener {

		protected void doComplete(Object values) {

		}

		@Override
		public void onError(UiError e) {
			showToast("授权错误：" + e.errorDetail);
		}

		@Override
		public void onCancel() {
			showToast("取消授权！");
		}

		@Override
		public void onComplete(Object response) {
			doComplete(response);
		}
	}

	/**
	 * 取消绑定
	 * 
	 * @param channel
	 *            ：渠道
	 */
	public void unBind(int channel) {
		initParameter();
		mParams.put("token", mToken);
		mParams.put("bind_channel", "" + channel);
		String sign = SSLKey.getSSLSign(mParams,
				SharedPreferencesMgr.getString("sugar", ""));// salt
		// 值由init中的sugar给出。必须要有的。
		mParams.put("sign", sign);
		sendRequest(HuPuRes.REQ_METHOD_USER_UNBIND, mParams,
				new HupuHttpHandler(this), false);
	}

	/**
	 * 提交绑定的信息
	 * 
	 * @param entity
	 * @param channel
	 */
	public void sendQqEntity(QqLoginEntity entity, int channel) {
		JSONObject json = new JSONObject();
		try {
			json.put("openid", entity.openId);
			json.put("access_token", entity.access_token);
			json.put("expires_in", entity.expires_in);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initParameter();
		mParams.put("token", mToken);
		mParams.put("bind_channel", "" + channel);
		mParams.put("bind_info", json.toString());
		String sign = SSLKey.getSSLSign(mParams,
				SharedPreferencesMgr.getString("sugar", ""));// salt
		// 值由init中的sugar给出。必须要有的。
		mParams.put("sign", sign);
		sendRequest(HuPuRes.REQ_METHOD_USER_BIND, mParams, new HupuHttpHandler(
				this), false);

	}

	public void onLoginSuccess() {
		Intent intent = new Intent();  
		intent.setAction("login");  
		sendBroadcast(intent);
	}

	/*
	 * 更新关注信息
	 */
	public void updateFollow(PhoneBindReq bind) {
		mApp.loginSuccess = 1;
		LinkedList<LeaguesEntity> leaguesEntities = new LinkedList<LeaguesEntity>();
		LinkedList<LeaguesEntity> tempList = mApp.loadLeagues();

		if (bind.lids != null) {
			for (int i = 0; i < bind.lids.length(); i++) {
				for (LeaguesEntity entity:tempList) {
					if (entity.lid == bind.lids.optInt(i)) {
						leaguesEntities.add(entity);
						break;
					}
				}
			}
		}

		//设置关注球队！
		for (int i = 0; i < tempList.size(); i++) {
			//不能保证服务端给的关注顺序 和init 给的联赛是否匹配 所以在这里将init给的数据补齐到关注的顺序 联赛中
			boolean isHave = false;
			for (LeaguesEntity entity:leaguesEntities) {
				if (entity.lid == tempList.get(i).lid) {
					isHave = true;
					break;
				}
			}
			if (!isHave) 
				leaguesEntities.add(tempList.get(i));
			//---------------------------------------------------------------------------------------------


			if (tempList.get(i).mList != null) {

				for (int j = 0; j < tempList.get(i).mList.size(); j++) {
					tempList.get(i).mList.get(j).is_follow = 0;
					if (bind.tids != null) {
						JSONArray ts = bind.tids.optJSONArray(tempList
								.get(i).lid + "");
						if (ts != null) {
							for (int k = 0; k < ts.length(); k++) {
								if (tempList.get(i).mList.get(j).tid == ts
										.optInt(k)) {
									tempList.get(i).mList.get(j).is_follow = 1;
									break;
								} else {
									tempList.get(i).mList.get(j).is_follow = 0;
								}
							}
						}
					}
				}
			}
		}

		// 插入数据
		mApp.insertLeagues(leaguesEntities);

		mApp.updateTeams(leaguesEntities);
		// 拉取 带顺序的数据
		//leaguesEntities = mApp.loadLeagues();

		// mApp.followLeague(leaguesEntities);
		// mApp.followOnlyTeams(leaguesEntities);
	}

	public void updateBindInfo(PhoneBindReq bind) {
		updateFollow(bind);
		showToast("登录成功");
		if (bind.token != null && !bind.token.equals("")) {
			SharedPreferencesMgr
			.setString(HuPuRes.KEY_NICK_NAME, bind.nickName);
			SharedPreferencesMgr.setString("tk", bind.token);
			SharedPreferencesMgr.setInt("uid", bind.uid);
			mToken = bind.token;
			uid = bind.uid;
		}
//		bind.show_bind = 1;//debug 绑定
		if(bind.show_bind == 1){
			Intent intent = new Intent(this, HupuUserBindActivity.class);
			Bundle bd=new Bundle();
			bd.putBoolean("isInit", false);//true首次登陆绑定,false使用某个功能时触发登陆后进入绑定
			intent.putExtras(bd);
			startActivity(intent);
		}

		onLoginSuccess(); // 登录成功后 告诉子类 更新界面！
	}

	public void checkToken(int isLogin) {
		if (isLogin == 0 && mToken != null) {
			showBindDialog(SharedPreferencesMgr.getString("tokenExpires",
					getString(R.string.token_expire_txt)));
			SharedPreferencesMgr.setString("bp", "");
			SharedPreferencesMgr.setString("bp", "");
			SharedPreferencesMgr.setString("tk", null);
			SharedPreferencesMgr.setString("hupu_name", "");
			SharedPreferencesMgr.setString(HuPuRes.KEY_NICK_NAME, null);
			this.mToken = null;
			this.uid = -1;
		}
	}

	private static String getTime(int t) {
		if(t<0)
			return "";
		int temp = t % 60;
		if (temp < 10)
			return t / 60 + ":" + "0" + temp;
		return t / 60 + ":" + temp;
	}

	// -----------------****社交相关***------------------//
	// 首先在您的Activity中添加如下成员变量
	protected UMSocialService mController;

	//public static final String wx_app_id = "wx4bf9d3d3775978d0";
	public static final String wx_app_id = "wxdfc13e7629c3c8a7";

	protected View btnShare;

	public void setShareEnable(boolean enable) {
		HupuLog.d("setShareEnable=" + enable);
		if (btnShare != null)
			btnShare.setEnabled(enable);
	}

	protected void setShareVisibility(int visibility) {
		if (btnShare != null)
			btnShare.setVisibility(visibility);
	}

	private void initUMSocialService(boolean needCopy) {
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA);
		mController.getConfig().setDefaultShareLocation(false);
	}


	private final String appID = "wxdfc13e7629c3c8a7";
	private final String qqzoneID = "100560807";
	private String shareURL;
	private String shareImgURL;
	private String shareWX;
	private String shareSina;
	private String shareQZone;
	private String shareCicle;

	protected void initSharePlatform(String title, String url, String content) {
		initSharePlatform(title, url, content, content, content +" "+ url
				+ " (分享自 @虎扑看球)", content, null);
	}

	/**
	 * 分平台设置分享内容
	 * */
	protected void initSharePlatform(String title, String url, String wx,
			String qzone, String weibo, String cicle, String imgUrl) {
		HupuLog.e("initSharePlatform", "url=" + url + " content wx=" + wx
				+ " ;weibo=" + weibo + " ;qzone=" + qzone);
		shareURL = url;
		shareImgURL = imgUrl;
		shareWX = wx;
		shareSina = weibo;
		shareQZone = qzone;
		shareCicle = cicle;
	}

	private static String TITLE = "虎扑看球";

	/**
	 * 
	 * **/
	private void postShare(SHARE_MEDIA media) {
		if (mController == null)
			initUMSocialService(false);
		BaseShareContent content = null;
		UMImage mUMImgBitmap = null;
		String c = null;
		if (shareImgURL != null)
			mUMImgBitmap = new UMImage(this, shareImgURL);
		else {
			mUMImgBitmap = new UMImage(this, R.drawable.icon_launcher);
		}

		if (media == SHARE_MEDIA.WEIXIN) {
			// 添加微信平台，参数1为当前Activity, 参数2为用户申请的AppID, 参数3为点击分享内容跳转到的目标url
			//			UMWXHandler wxHandler = mController.getConfig().supportWXPlatform(
			//					this, appID, shareURL);
			//			wxHandler.setWXTitle(shareWX);
			// mController.getConfig().setSsoHandler(wxHandler);

			UMWXHandler wxHandler = new UMWXHandler(this,appID);
			wxHandler.addToSocialSDK();
			wxHandler.setTitle(shareWX);
			wxHandler.setTargetUrl(shareWX);
			c = shareWX;
			content = new WeiXinShareContent(mUMImgBitmap);
			content.setTitle(shareWX);

		} else if (media == SHARE_MEDIA.WEIXIN_CIRCLE) {
			// 支持微信朋友圈
			//			UMWXHandler circleHandler = mController.getConfig()
			//					.supportWXCirclePlatform(this, appID, shareURL);
			//			circleHandler.setCircleTitle(shareCicle);
			HupuLog.d("share="+shareCicle);
			UMWXHandler wxCircleHandler = new UMWXHandler(this,appID);
			wxCircleHandler.setToCircle(true);
			wxCircleHandler.addToSocialSDK();
			wxCircleHandler.setTargetUrl(shareURL);
			c = shareCicle;
			content = new CircleShareContent(mUMImgBitmap);
			content.setTitle(shareCicle);

		} else if (media == SHARE_MEDIA.QZONE) {
			QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, qqzoneID,
					"d99232f571af401cee4b3a0a4479f287");
			qZoneSsoHandler.addToSocialSDK();

			//			mController.getConfig().setSsoHandler(
			//					new QZoneSsoHandler(this, qqzoneID));

			c = shareQZone;
			content = new QZoneShareContent(mUMImgBitmap);
			content.setTitle(shareQZone);

		} else if (media == SHARE_MEDIA.SINA) {
			mUMImgBitmap = new UMImage(this, shareImgURL);
			// 设置新浪SSO handler
			mController.getConfig().setSsoHandler(new SinaSsoHandler());
			c = shareSina;
			content = new SinaShareContent(mUMImgBitmap);

		}
		HupuLog.d("share="+c);
		content.setShareContent(c);
		content.setTargetUrl(shareURL);
		mController.setShareContent(c);
		mController.setShareMedia(content);// 现在只绑定一个平台的消息

		if (shareDialog != null) {
			shareDialog.dismiss();
			mController.getConfig().cleanListeners();
		}
		mController.postShare(this, media, new SnsPostListener() {
			@Override
			public void onStart() {
				HupuLog.d("开始分享.");
			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity) {
				HupuLog.e("papa", "code=="+eCode);
				if (eCode == 200) {
					HupuLog.d("分享成功.");
					showToast("分享成功");
					//分享成功回调服务器
					initParameter();
					mParams.put("token", mToken);
					mParams.put("url", shareURL);
					sendRequest(HuPuRes.REQ_METHOD_POST_TASK_SHARE, mParams, new HupuHttpHandler(HupuBaseActivity.this));

				} else {
					String eMsg = "分享失败";
					if (eCode == -101) {
						eMsg = "没有授权";
					}
					HupuLog.d("分享失败[" + eCode + "] " + eMsg);
					showToast(eMsg);
				}
			}
		});
	}

	// 分享dialog
	private ShareDialog shareDialog;

	public void openShareDialog(boolean isShowBrowser) {
		if (shareDialog == null) {
			shareDialog = new ShareDialog(this, HupuBaseActivity.this,
					isShowBrowser);
		}
		shareDialog.show();
	}

	@Override
	public void treatClickEvent(View v) {
		if(DoubleClickUtil.isFastDoubleClick()){
			return;
		}
		int id = v.getId();
		switch (id) {
		case R.id.share_item_wxchat:
			postShare(SHARE_MEDIA.WEIXIN);
			break;
		case R.id.share_item_qzone:
			postShare(SHARE_MEDIA.QZONE);
			break;
		case R.id.share_item_wxcircle:
			postShare(SHARE_MEDIA.WEIXIN_CIRCLE);

			break;
		case R.id.share_item_weibo:
			postShare(SHARE_MEDIA.SINA);
			break;
		case R.id.share_item_browser:
			openWebBrowser();
			break;
		case R.id.btn_cancel_share:
			if (shareDialog != null) {
				shareDialog.dismiss();
			}
			break;

		default:

			break;
		}
	}

	/**
	 * 使用系统自带的浏览器
	 * */
	protected void openWebBrowser() {
		closeDialog();

		if(shareURL!=null)
		{
			Intent viewIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(shareURL));
			startActivity(viewIntent);
		}

	}

	public void closeDialog(){
		if (shareDialog != null) {
			shareDialog.dismiss();
		}
	}

	protected void showShareView(String title, String url, String content,
			boolean browser) {
		initSharePlatform(title, url, content);
		openShareDialog(browser);
	}

	protected void showShareView(String title, String url, String wx,
			String qzone, String weibo, String cicle, String imgurl,
			boolean browser) {
		initSharePlatform(title, url, wx, qzone, weibo, cicle, imgurl);
		openShareDialog(browser);
	}

	public boolean checkNetIs2Gor3G() {
		ConnectivityManager connManager = (ConnectivityManager) this
				.getSystemService(this.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		} else
			return false;
	}


	/**
	 * scheme 跳转逻辑
	 * @param uri
	 * @param lid
	 */
	public void treatScheme(String uri,int lid)
	{
		HupuScheme scheme =new HupuScheme();
		scheme.paser(Uri.parse(uri));

		// 第二级的TAB
		Intent in = null;
		if (HuPuRes.TEMPLATE_NBA.equalsIgnoreCase(scheme.template)) {
			// NBA

			in = new Intent(this, NBAGameActivity.class);

		} else if (HuPuRes.TEMPLATE_CBA
				.equalsIgnoreCase(scheme.template)) {
			// CBA
			in = new Intent(this, BasketballActivity.class);
		} else if (HuPuRes.TEMPLATE_SOCCER_LEAGUE
				.equalsIgnoreCase(scheme.template)
				|| HuPuRes.TEMPLATE_SOCCER_CUP_LEAGUE
				.equalsIgnoreCase(scheme.template)) {
			// 足球
			in = new Intent(this, FootballGameActivity.class);
		}
		if (in != null) {
			// 必须有gid，tag,lid
			HupuLog.d("scheme jump", "tag=" + scheme.game + " lid="
					+ lid);
			in.putExtra("gid", scheme.id);
			in.putExtra("tag", scheme.game);
			in.putExtra("lid", lid);
			startActivity(in);
		}
	}

	public  int findLid(String tag) {

		if(tag==null)
			return 0;
		LinkedList<LeaguesEntity> leagueList = mApp.loadLeagues();
		for (LeaguesEntity en : leagueList) {
			if (en.en.equals(tag)) {
				return en.lid;		
			}
		}
		return 0;
	}
}
