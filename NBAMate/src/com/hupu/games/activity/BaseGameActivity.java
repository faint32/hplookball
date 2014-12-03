package com.hupu.games.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.HupuBaseActivity.ChatInterface;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.IncreaseEntity;
import com.hupu.games.data.LiveEntity.Answer;
import com.hupu.games.data.SSLKey;
import com.hupu.games.data.game.quiz.QuizCaipiaoEntity;
import com.hupu.games.data.game.quiz.QuizCaipiaoEntity.CaipiaoAnswer;
import com.hupu.games.fragment.ChatFragment;
import com.hupu.games.fragment.LiveFragment;
import com.hupu.games.fragment.QuizListFragment;
import com.hupu.games.fragment.ReportFragment;
import com.hupu.http.HupuHttpHandler;
import com.pyj.http.RequestParams;

public class BaseGameActivity extends HupuBaseActivity implements ChatInterface {

	public final static int INDEX_REPORT = 1;
	public final static int INDEX_LIVE = 3;
	public final static int INDEX_STATISTIC = 2;
	public final static int INDEX_CHAT = 4;
	public final static int INDEX_PLAYER_RATING = 5;
	public final static int INDEX_QUIZ = 6;

	public final static int INDEX_STATISTIC_LAND = 7;

	public final static int INDEX_LIVE_BY_MAN = 10;
	public final static int INDEX_LINEUP = 15;
	public static String SORRY_NOTIFY = "抱歉，%s vs %s闹钟设置失败";
	public static String SUCCESS_NOTIFY = "闹钟设置成功，您将会收到%s vs %s的推送通知";
	public static String CANCEL_NOTIFY = "闹钟取消成功";

	public static String TAB_REPORT = "recap";

	public static String TAB_GUESS = "casino";

	public static String TAB_LIVE = "live";

	public static String TAB_BOXSCORE = "stats";

	public static String TAB_CHAT = "chat";

	protected String movieUrl;

	protected String tag;

	protected String TAG;

	/** 比赛是否已经结束了 */
	public boolean bEnd;

	/** 联赛id */
	public int lid;

	public int gid;
	
	/** 战报页 */
	public ReportFragment mFragmentReport;

	/** 热线页 */
	public ChatFragment mFragmentChat;

	/** 直播页 */
	public LiveFragment mFragmentLive;

	public QuizListFragment mFragmentQuizList;

	public TextView txtTitle;

	protected Button btnFollow;

	protected Button btnPlay;

	protected Button btnSend;

	FragmentManager fragmentManager;

	/** 是否有直播数据 */
	public boolean bHasLiveData;
	/** 是否有统计数据 */
	public boolean bHasStatisticData;
	/** 是否比赛战报 */
	public boolean bHasReportData;

	public static SimpleDateFormat sdf = new java.text.SimpleDateFormat(
			"开赛:M月d日 H:mm", java.util.Locale.CHINESE);

	/** 比赛状态 */
	public int curGameState;

	/**
	 * 是否为关注比赛，该变量是临时设置，因为关注或取消操作是延时操作，只有数据成功返回后，才是真实情况。 返回失败，则会重置
	 */
	protected boolean isFollow;

	/** 比赛未开始时显示的开赛时间 */
	protected TextView txtStartTime;
	/** 比赛开始后显示的时间 */
	protected TextView txtProcess;
	/** 主队队名 */
	protected TextView txtTeamLeft;
	/** 客队队队名 */
	protected TextView txtTeamRight;

	protected TextView txtScore;

	protected ImageView imgTeamLeft;

	protected ImageView imgTeamRight;

	protected ImageView TopAdImg;

	/**
	 * 产品要求咱的底部菜单栏目会随着比赛的状态变化而变化，高级吧 赛前，赛中： 热线，直播，统计 赛后： 战报，统计，直播，热线
	 * */
	protected ImageView btnReport;
	protected ImageView btnLive;
	protected ImageView btn4th;
	protected ImageView btnChat;
	protected ImageView btnQuiz;

	protected ImageButton btnThird;
	protected ImageButton btnSecond;

	public int lastChatID;

	/** 比赛直播页和统计页需要有初期数据才加入socket */
	protected boolean bJoinRoom;

	/** 当前所在页索引 */
	public int curFragmentIndex;

	protected RelativeLayout rewardInfo;

	protected ImageView imgRedPiont;
	public int mQid;
	/** 默认打开的tab */
	public String mDefaultTab;
	/** 是否跳转到了热线输入界面 */
	protected boolean toChat;
	
	public boolean isExchange = false;

	public void reqFresh() {

	}

	@Override
	/** 请求热线数据，每页20条 */
	public void reqChatData(int id) {
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (REQ_SEND_MSG == requestCode && resultCode == RESULT_OK) {
			String content = data.getStringExtra("content");
			//String emoji = data.getStringExtra("emoji");
			String userName = data.getStringExtra("user");
			int mRoomid = data.getIntExtra("roomid", 0);
			if (content != null)
				sendChatMsg(0, userName, content,mRoomid);
			
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/** 发送信息 */
	public void sendChatMsg(int type, String name, String ss,int mRoomid) {

		mParams.put("type", tag);
		mParams.put("username", name);
		mParams.put("roomid", mRoomid+"");
		if (type == 0) {
			mParams.put("content", ss);
			mParams.remove("emoji");
			// mParams.remove("token");
		} else {
			mParams.put("emoji", ss);
			mParams.remove("content");
		}
		if (mToken != null)
			mParams.put("token", mToken);
		mParams.put("gid", "" + gid);
		sendRequest(HuPuRes.REQ_METHOD_SENT_CHAT, mParams,
				new HupuHttpHandler(this), false);
		mFragmentChat.addData(type, name, ss);
	}

	HupuHttpHandler httpHandler;

	/** 下注 */
	public void sendQuizCommit(Answer answer, int coin, boolean increase) {
		//
		RequestParams p = new RequestParams();
		p.clear();
		p.put("client", mDeviceId);
		if (mToken != null)
			p.put("token", mToken);

		p.put("lid", "" + lid);
		p.put("gid", "" + gid);
		p.put("qid", "" + answer.casino_id);
		p.put("coin", "" + coin);
		p.put("answer", "" + answer.answer_id);
		httpHandler = new HupuHttpHandler(this);
		httpHandler.messageID1 = answer.casino_id;
		httpHandler.messageID2 = answer.answer_id;
		HupuLog.d("sendQuizCommit token=" + mToken);
		HupuLog.d("sendQuizCommit lid=" + lid + " ;gid=" + gid + " ;qid="
				+ answer.casino_id + " ;coin=" + coin + " ;answer="
				+ answer.answer_id);
		if (increase)
			sendRequest(HuPuRes.REQ_METHOD_QUIZ_COMMIT_INCREASE, tag, p,
					httpHandler, false);
		else
			sendRequest(HuPuRes.REQ_METHOD_QUIZ_COMMIT, tag, p, httpHandler,
					false);

	}

	/** 设置为全屏 **/
	public void setFullScreen() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().getDecorView().invalidate();
	}

	/** 退出全屏模式 **/
	public void quitFullScreen() {
		final WindowManager.LayoutParams attrs = getWindow().getAttributes();
		attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setAttributes(attrs);
		getWindow()
				.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}

	private ContentObserver mObserver = new ContentObserver(new Handler()) {
		@Override
		public void onChange(boolean selfChange) {
			if (Settings.System.getInt(getContentResolver(),
					Settings.System.ACCELEROMETER_ROTATION, 0) != 0)
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
			else
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	};

	/** 监听系统翻转设置 **/
	public void registSetting() {
		getContentResolver()
				.registerContentObserver(
						Settings.System
								.getUriFor(Settings.System.ACCELEROMETER_ROTATION),
						true, mObserver);
	}

	public void unregistSetting() {
		getContentResolver().unregisterContentObserver(mObserver);
	}

	/** 是否是水平模式 */
	public boolean isLandMode;

	/** 用代码处理屏幕翻转的逻辑 */
	public void lockScreenRotation(int orientation) {
		switch (orientation) {

		case Configuration.ORIENTATION_PORTRAIT:
			// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			if (isLandMode) {
				switchToPortraitMode();
				// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
			}
			break;
		case Configuration.ORIENTATION_LANDSCAPE:
			if (curFragmentIndex == INDEX_STATISTIC) {
				if (Settings.System.getInt(getContentResolver(),
						Settings.System.ACCELEROMETER_ROTATION, 0) == 0) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					// Log.d("lockScreenRotation", "lockScreenRotation");
					return;
				}

				if (!isLandMode) {
					switchToLandMode();
				}
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
			}
			break;
		default:
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
			break;
		}
	}

	public void switchToPortraitMode() {

	}

	/** 切换到水平模式 **/
	public void switchToLandMode() {

	}

	/** 为了处理屏幕翻转事件 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// Log.d("onConfigurationChanged", "ORIENTATION=" +
		// newConfig.orientation);
		super.onConfigurationChanged(newConfig);
		switch (newConfig.orientation) {
		case Configuration.ORIENTATION_PORTRAIT:
			// taking action on event
			lockScreenRotation(Configuration.ORIENTATION_PORTRAIT);
			// Log.d("onConfigurationChanged", "ORIENTATION_PORTRAIT");
			break;
		case Configuration.ORIENTATION_LANDSCAPE:
			// taking action on event
			lockScreenRotation(Configuration.ORIENTATION_LANDSCAPE);
			// Log.d("onConfigurationChanged", "ORIENTATION_LANDSCAPE");
			break;
		case Configuration.ORIENTATION_SQUARE:
			// taking action on event
			lockScreenRotation(Configuration.ORIENTATION_SQUARE);
			// Log.d("onConfigurationChanged", "ORIENTATION_SQUARE");
			break;
		}

	}

	Dialog mTipsDialog;

	/** 显示一个dialog，告诉用户可以翻转屏幕使用 */
	public void showTipsDialog() {
		View v = LayoutInflater.from(this).inflate(R.layout.dialog_rotation,
				null);
		v.findViewById(R.id.btn_close).setOnClickListener(click);

		mTipsDialog = new Dialog(this, R.style.MyDialog);

		mTipsDialog.setContentView(v);
		int w = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
		int h = (int) (getResources().getDisplayMetrics().density * 330);
		mTipsDialog.getWindow().setLayout(w, h);
		mTipsDialog.show();
	}

	/** 关闭个dialog */
	public void closeDialog() {
		if (mTipsDialog != null && mTipsDialog.isShowing()) {
			mTipsDialog.dismiss();
			mTipsDialog = null;
		}

	}

	// ------新加竞猜tag 部分 使用的函数

	public void getQuizList() {
		HupuLog.e("papa", "gid===" + gid);
		initParameter();
		mParams.put("lid", "" + lid);
		mParams.put("gid", "" + gid);
		mParams.put("token", mToken);
		sendRequest(HuPuRes.REQ_METHOD_QUIZ_LIST, mParams, new HupuHttpHandler(
				this), false);
	}

	public void reqBitCoin(int qid) {
		if (mToken != null) {
			initParameter();
			mParams.put("token", mToken);
			mParams.put("qid", "" + qid);
			sendRequest(HuPuRes.REQ_METHOD_BET_COINS, mParams,
					new HupuHttpHandler(this), false);
		}
	}

	public void sendQuizListCommit(Answer answer, int coin, boolean isIncrease) {
		//
		RequestParams p = new RequestParams();
		p.clear();
		p.put("client", mDeviceId);
		p.put("qid", "" + answer.casino_id);
		p.put("answer", "" + answer.answer_id);
		p.put("coin", "" + coin);
		p.put("roomid", roomid+"");
		if (mToken != null && coin > 0)
			p.put("token", mToken);
		if (isIncrease) {
			isIncrease = false;
			sendRequest(HuPuRes.REQ_METHOD_POST_INCREASE, p,
					new HupuHttpHandler(this), false);
		} else {
			p.put("lid", "" + lid);
			p.put("gid", "" + gid);
			sendRequest(HuPuRes.REQ_METHOD_QUIZ_LIST_COMMIT, p,
					new HupuHttpHandler(this), false);
		}
	}

	// 发送彩票投注请求
	public void sendCaipiaoCommit(QuizCaipiaoEntity caipiao, int coin, int index) {
		//
		initParameter();
		mParams.put("bid", "" + caipiao.bid);
		mParams.put("answer", "" + caipiao.mList.get(index).answer_id);
		mParams.put("coin", "" + coin);
		if (mToken != null && coin > 0)
			mParams.put("token", mToken);
		String sign = SSLKey.getSSLSign(mParams,
				SharedPreferencesMgr.getString("sugar", ""));// salt
																// 值由init中的sugar给出。必须要有的。

		mParams.put("sign", sign);
		sendRequest(HuPuRes.REQ_METHOD_CAIPIAO_COMMIT, mParams,
				new HupuHttpHandler(this), false);
	}
	
	// 发送彩票投注请求
	public void sendScoreCaipiaoCommit(CaipiaoAnswer scoreAnswer, int coin) {
		//
		initParameter();
		mParams.put("bid", "" + scoreAnswer.bid);
		mParams.put("answer", "" + scoreAnswer.answer_id);
		mParams.put("coin", "" + coin);
		if (mToken != null && coin > 0)
			mParams.put("token", mToken);
		String sign = SSLKey.getSSLSign(mParams,
				SharedPreferencesMgr.getString("sugar", ""));// salt
		// 值由init中的sugar给出。必须要有的。
		
		mParams.put("sign", sign);
		sendRequest(HuPuRes.REQ_METHOD_CAIPIAO_COMMIT, mParams,
				new HupuHttpHandler(this), false);
	}

	/**
	 * 确认付款 dialog
	 * 
	 * @param caipiao
	 * @param coin
	 * @param index
	 */
	public void showBuyCaipiao(final QuizCaipiaoEntity caipiao, final int coin,
			final int index) {
		String confirm_buy_num = getResources().getString(
				R.string.dialog_buy_caipiao_confirm);
		String confirm_buy_answer = getResources().getString(
				R.string.dialog_buy_caipiao_confirm_answer);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				confirm_buy_num.format(confirm_buy_num, coin)
						+ confirm_buy_answer.format(confirm_buy_answer,
								caipiao.mList.get(index).answer_title))
				.setCancelable(false)
				.setPositiveButton(getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								sendUmeng(HuPuRes.UMENG_EVENT_LOTTERY,
										HuPuRes.UMENG_KEY_LOTTERY_BET,
										HuPuRes.UMENG_VALUE_DOUBLE_CHECK_CANCEL);
							}
						})
				.setNegativeButton(getString(R.string.submit_confirm),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								sendUmeng(
										HuPuRes.UMENG_EVENT_LOTTERY,
										HuPuRes.UMENG_KEY_LOTTERY_BET,
										HuPuRes.UMENG_VALUE_DOUBLE_CHECK_CONFIRM);
								sendCaipiaoCommit(caipiao, coin, index);
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	/**
	 * 确认比分玩法付款 dialog
	 * 
	 * @param caipiao
	 * @param coin
	 * @param index
	 */
	public void showBuyScoreCaipiao(final CaipiaoAnswer scoreAnswer, final int coin) {
		String confirm_buy_num = getResources().getString(
				R.string.dialog_buy_caipiao_confirm);
		String confirm_buy_answer = getResources().getString(
				R.string.dialog_buy_caipiao_confirm_answer);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				confirm_buy_num.format(confirm_buy_num, coin)
						+ confirm_buy_answer.format(confirm_buy_answer,
								scoreAnswer.answer_title))
				.setCancelable(false)
				.setPositiveButton(getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								sendUmeng(HuPuRes.UMENG_EVENT_LOTTERY,
										HuPuRes.UMENG_KEY_LOTTERY_BET,
										HuPuRes.UMENG_VALUE_DOUBLE_CHECK_CANCEL);
							}
						})
				.setNegativeButton(getString(R.string.submit_confirm),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								sendUmeng(
										HuPuRes.UMENG_EVENT_LOTTERY,
										HuPuRes.UMENG_KEY_LOTTERY_BET,
										HuPuRes.UMENG_VALUE_DOUBLE_CHECK_CONFIRM);
								sendScoreCaipiaoCommit(scoreAnswer, coin);
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	

	/**
	 * 提示余额不足的dialog
	 * 
	 * @param caipiao
	 * @param coin
	 * @param index
	 */
	public void showRechargeCaipiao(final Context context, final int coin) {
		String confirm_buy = getResources().getString(R.string.no_wallet);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(confirm_buy.format(confirm_buy, coin + ""))
				.setCancelable(false)
				.setPositiveButton(getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

							}
						})
				.setNegativeButton(getString(R.string.charge_now),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								sendUmeng(
										HuPuRes.UMENG_EVENT_LOTTERY,
										HuPuRes.UMENG_KEY_LOTTERY_BET,
										HuPuRes.UMENG_VALUE_NO_MONEY_ALERT_CONFIRM);
								Intent recharge = new Intent(context,
										UserRechargeActivity.class);
								recharge.putExtra("recharge_num", coin);
								startActivity(recharge);
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void switchToGuessRank() {

		Intent intent = new Intent(this, GuessRankActivity.class);
		intent.putExtra("lid", lid);
		intent.putExtra("gid", gid);
		intent.putExtra("roomid", roomid);
		intent.putExtra("rank_type", 0);
		startActivity(intent);
	}

	/**
	 * 从竞猜页面返回时更新直播列表的竞猜状态
	 * */
	public void updateBet(ArrayList<IncreaseEntity> list) {
		if (mFragmentLive != null)
			mFragmentLive.updateBet(list);
	}

	private int isStart = -1;

	protected void startAnim(int coinNum) {
		rewardInfo.setVisibility(View.VISIBLE);
		isStart = 0;
		myAnimation_Translate = new TranslateAnimation(0, 0, 70, 0);
		myAnimation_Translate.setDuration(500);
		myAnimation_Translate.setAnimationListener(animationListener);
		rewardInfo.setAnimation(myAnimation_Translate);
		rewardInfo.startAnimation(myAnimation_Translate);
		((TextView) findViewById(R.id.reward_coin_num)).setText("+" + coinNum);
	}

	private void readAnim() {
		isStart = 1;
		myAnimation_Translate = new TranslateAnimation(0, 0, 0, 0);
		myAnimation_Translate.setDuration(3000);
		myAnimation_Translate.setAnimationListener(animationListener);
		rewardInfo.setAnimation(myAnimation_Translate);
		rewardInfo.startAnimation(myAnimation_Translate);
	}

	private void endAnim() {
		isStart = 2;
		myAnimation_Translate = new TranslateAnimation(0, 0, 0, 70);
		myAnimation_Translate.setDuration(500);
		myAnimation_Translate.setAnimationListener(animationListener);
		rewardInfo.setAnimation(myAnimation_Translate);
		rewardInfo.startAnimation(myAnimation_Translate);
	}

	private Animation myAnimation_Translate;
	private AnimationListener animationListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			switch (isStart) {
			case 0:
				readAnim();
				break;

			case 1:
				endAnim();
				break;

			case 2:
				rewardInfo.setVisibility(View.GONE);
				break;
			}
		}
	};

	public boolean sendRequest(int reqType, RequestParams params) {
		return sendRequest(reqType, tag, params, new HupuHttpHandler(this),
				false);
	}

	protected void setJsonObj(String key, int value) {
		JSONObject obj = getRoomObj();
		try {
			obj.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void setJsonObj(String key, String value) {
		JSONObject obj = getRoomObj();
		try {
			obj.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void setJsonObj(String key, JSONArray a) {
		if (a == null)
			return;
		JSONObject obj = getRoomObj();
		try {
			obj.put(key, a);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	
}
