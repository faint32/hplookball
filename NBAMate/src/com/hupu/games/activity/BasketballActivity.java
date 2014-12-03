package com.hupu.games.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hupu.games.HuPuApp;
import com.hupu.games.R;
import com.hupu.games.adapter.RoomListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.BaseEntity;
import com.hupu.games.data.BaseLiveResp;
import com.hupu.games.data.ChatResp;
import com.hupu.games.data.FollowResp;
import com.hupu.games.data.PushNotify;
import com.hupu.games.data.Recap;
import com.hupu.games.data.SendMsgResp;
import com.hupu.games.data.game.base.SimpleLiveResp;
import com.hupu.games.data.game.basketball.BasketballGameEntity;
import com.hupu.games.data.game.basketball.CBABoxScoreResp;
import com.hupu.games.data.game.football.ScoreboardEntity;
import com.hupu.games.data.game.quiz.QuizCommitResp;
import com.hupu.games.data.room.GiftEntity;
import com.hupu.games.data.room.GiftReqDataEntity;
import com.hupu.games.data.room.GiftRespResultEntity;
import com.hupu.games.data.room.RoomEntity;
import com.hupu.games.data.room.RoomListEntity;
import com.hupu.games.dialog.EpandDownAnimation;
import com.hupu.games.dialog.EpandDownAnimation.EpandAnimationCallBack;
import com.hupu.games.dialog.GiftTipsDialog;
import com.hupu.games.dialog.TipsDialog;
import com.hupu.games.fragment.CBAReportFragment;
import com.hupu.games.fragment.CBAStatisticFragment;
import com.hupu.games.fragment.CBAStatisticLandFragment;
import com.hupu.games.fragment.ChatFragment;
import com.hupu.games.fragment.LiveFragment;
import com.hupu.games.fragment.QuizListFragment;
import com.hupu.games.fragment.ReportFragment;
import com.hupu.games.hupudollor.activity.HupuDollorOrderActivity;
import com.hupu.games.hupudollor.data.HupuDollorBalanceReq;
import com.hupu.games.livegift.animation.AnimationTool;
import com.hupu.games.pay.BasePayActivity;
import com.hupu.games.pay.HupuOrderActivity;
import com.hupu.http.HupuHttpHandler;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.pyj.common.DeviceInfo;
import com.pyj.common.MyUtility;

/**
 * 赛前：直播、（竞猜）、热线； 赛中：直播、统计、（竞猜）、热线； 赛后：战报、统计、直播、（竞猜）、热线；
 * */
public class BasketballActivity extends BaseGameLiftActivity {

	private BasketballGameEntity mGameEntity;

	private final static int GAME_STATE_CANCEL = 6;
	private final static int GAME_STATE_DELAY = 5;
	private final static int GAME_STATE_ONGOING = 2;
	private final static int GAME_STATE_END = 4;
	private final static int GAME_STATE_NOT_START = 1;

	/** 前瞻页 */
	CBAReportFragment qzFragment;
	/** 统计 */
	CBAStatisticFragment mStatisticFragment;

	/** 统计 */
	CBAStatisticLandFragment mStatisticLandFragment;

	ImageView btnReport;

	View mLayoutScoreBar;

	/***/
	private int lastLiveID;

	private int lastChatID;

	/** 统计id */
	private int bid;

	private boolean getLiveEndData;

	private boolean getBoxEndData;


	private int indexLive;

	private int indexStatistic;

	String mDefaultTab;


	/** 房间礼物相关 */
	private ListView mRoomListView;
	private RoomListAdapter mRoomListAdapter;
	//	private LinearLayout mGiftView;
	private LinearLayout mRoomView;

	private int sortId;//请求正序还是逆序
	String curRoomtitle;
	String curRoomOnline; 
	private boolean isPreview;


	private RelativeLayout title_layout;
	private ImageView title_room_switch;

	private TextView room_people_numTextView;
	private String live_online="%s人在线";
	private boolean livefirstIn;

	private boolean isInitCbaRoom;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Intent in = getIntent();

		tag = in.getStringExtra("tag");
		lid = in.getIntExtra("lid", 0);
		gid = in.getIntExtra("gid", 0);
		if (lid <= 0)
			lid = findLid(tag);
		// gid=3560;
		setContentView(R.layout.layout_basketball_portait);
		initParameter();
		if (gid > 0) {
			mDefaultTab = in.getStringExtra("tab");

			// 外部跳转
			mParams.put("gid", "" + gid);
			sendRequest(HuPuRes.REQ_METHOD_CBA_GAME_BY_GID, mParams);
		} else {
			// 从赛程页进入
			mGameEntity = (BasketballGameEntity) in.getSerializableExtra("data");
			gid = mGameEntity.i_gId;
			mDefaultTab = mGameEntity.default_tab;
			if (mGameEntity == null) {
				finish();
			}
			init();
		}

	}

	private void init() {

		// 进入页面 底部先隐藏

		mParams.put("gid", "" + gid);

		setJosnObj(BaseEntity.KEY_GAME_ID, gid);

		mLayoutScoreBar = findViewById(R.id.layout_score_bar);

		txtTitle = (TextView) findViewById(R.id.txt_title);
		txtTitle.setText(R.string.title_live);

		txtTeamLeft = (TextView) findViewById(R.id.txt_team_left);
		txtTeamRight = (TextView) findViewById(R.id.txt_team_right);
		txtProcess = (TextView) findViewById(R.id.txt_proccess);

		txtScore = (TextView) findViewById(R.id.txt_score);

		txtStartTime = (TextView) findViewById(R.id.txt_start_time);

		imgTeamLeft = (ImageView) findViewById(R.id.img_team_left);

		imgTeamRight = (ImageView) findViewById(R.id.img_team_right);
		setTeamName();

		btnSecond = (ImageButton) findViewById(R.id.btn_second);

		btnThird = (ImageButton) findViewById(R.id.btn_third);

		btnReport = (ImageButton) findViewById(R.id.btn_report);

		btnChat = (ImageButton) findViewById(R.id.btn_chat);

		btnFollow = (Button) findViewById(R.id.btn_follow);
		btnPlay = (Button) findViewById(R.id.btn_play);

		curGameState = (byte) mGameEntity.byt_status;
		btnQuiz = (ImageButton) findViewById(R.id.btn_quiz);
		imgRedPiont = (ImageView) findViewById(R.id.icon_red_point);
		imgRedPiont.setVisibility(View.GONE);
		rewardInfo = (RelativeLayout) findViewById(R.id.reward_info);

		title_room_switch = (ImageView) findViewById(R.id.title_room_switch);
		room_people_numTextView = (TextView) findViewById(R.id.title_room_peple_online);

		HupuLog.d("init", "curGameState=" + curGameState);

		isFollow = mGameEntity.bFollow == 1;

		setFollowBtn();

		setViewByState(true);

		updateScoreView();


		if (mDefaultTab != null) {
			switchTab();
		} else {
			if (curGameState == GAME_STATE_END) {
				treatClickEvent(R.id.btn_report);
			} else if (curGameState == GAME_STATE_NOT_START)
				treatClickEvent(R.id.btn_quiz);
			else
				treatClickEvent(indexLive);
		}

		// init 房间部分
		initRoomAndGift();
		setTeamName();
		// test
		// reqHttp(HuPuRes.REQ_METHOD_GET_PLAY_LIVE);
		//
		setOnClickListener(R.id.layout_live_title);
		//		setOnClickListener(R.id.txt_title);
		//		setOnClickListener(R.id.title_room_switch);
		setOnClickListener(R.id.choose_room_dialog);
		setOnClickListener(R.id.btn_back);

		setOnClickListener(R.id.btn_follow);
		setOnClickListener(R.id.btn_play);
		setOnClickListener(R.id.btn_sent);
		setOnClickListener(R.id.btn_land);
		setOnClickListener(R.id.btn_report);
		setOnClickListener(R.id.btn_second);
		setOnClickListener(R.id.btn_third);
		setOnClickListener(R.id.btn_chat);
		setOnClickListener(R.id.btn_quiz);
		setOnClickListener(R.id.gold_num);

	}

	/** 根据默认标签跳转到不同的页面 */
	private void switchTab() {
		if (mDefaultTab.equals(TAB_GUESS))
		{
			treatClickEvent(R.id.btn_quiz);
		}
		else if (mDefaultTab.equals(TAB_BOXSCORE))
			treatClickEvent(indexStatistic);
		else if (mDefaultTab.equals(TAB_CHAT))
		{
			treatClickEvent(R.id.btn_chat);
		}
		else if (mDefaultTab.equals(TAB_LIVE))
			treatClickEvent(indexLive);// 直播
		else if (mDefaultTab.equals(TAB_REPORT))
			treatClickEvent(R.id.btn_report);
		else
			treatClickEvent(indexLive);// 聊天
	}

	@Override
	public void treatClickEvent(int id) {

		super.treatClickEvent(id);
		// 更换底部菜单按键背景

		if (id == indexLive) {
			setBackgound(INDEX_LIVE);
			replaceContent(INDEX_LIVE);
			// 一直常亮
			setScreenLight(true);
		} else if (id == indexStatistic) {
			setBackgound(INDEX_STATISTIC);
			replaceContent(INDEX_STATISTIC);
		}
		//		
		//		UMENG_MAP.clear();
		//		//		showRoomAction();

		//		if (id == indexLive) {
		//			if (curFragmentIndex != INDEX_LIVE) {//从其他tab点击到直播
		//
		//				setBackgound(INDEX_LIVE);
		//				replaceContent(INDEX_LIVE);
		//				mFragmentLive.entry();
		//				if (!bHasLiveData) {
		//					// 初次进入该页面需要获取全部的实时播报信息
		//					if (curGameState == GAME_STATE_ONGOING)// 进行中
		//					{
		//						sortId = HuPuRes.REQ_METHOD_GET_LIVE_ASC;
		//						req_room_http(roomid);
		//					} else {
		//						sortId = HuPuRes.REQ_METHOD_GET_LIVE_DESC;
		//						req_room_http(roomid);
		//					}
		//				} else {
		//					bJoinRoom = true;
		//					// 加入前设置最后一条消息id
		//					if (lastLiveID > 0)
		//						setJsonObj(BaseEntity.KEY_PID, lastLiveID);
		//					// 如果是有竞猜的比赛，需要加入所有为关闭的竞猜id
		//					setJsonObj("qids", mFragmentLive.getQids());
		//					joinRoom(HuPuRes.ROOM_CBA_PLAYBYPLAY_CASINO);
		//
		//				}
		//				setScreenLight(true);
		//			}
		//			curFragmentIndex = INDEX_LIVE;
		//
		//		} else if (id == indexStatistic) {
		//			if (curFragmentIndex != INDEX_STATISTIC) {
		//				txtTitle.setText(R.string.title_statistic);
		//				setBackgound(INDEX_STATISTIC);
		//				replaceContent(INDEX_STATISTIC);
		//				if (!bHasStatisticData || (bEnd && !getBoxEndData)) {
		//					mParams.put(BaseEntity.KEY_VERTICAL, "" + true);
		//					reqHttp(HuPuRes.REQ_METHOD_CBA_BOX_SCORE);
		//				} else if (curGameState != GAME_STATE_END) {
		//					joinRoom(HuPuRes.ROOM_CBA_BOXSCORE);
		//					bJoinRoom = true;
		//				}
		//
		//				findViewById(R.id.btn_land).setVisibility(View.VISIBLE);
		//				if (curGameState == GAME_STATE_ONGOING
		//						|| curGameState == GAME_STATE_NOT_START)
		//					findViewById(R.id.layout_title_btn)
		//					.setVisibility(View.GONE);
		//			}
		//			curFragmentIndex = INDEX_STATISTIC;
		//		}

		switch (id) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_report:
			setBackgound(INDEX_REPORT);
			replaceContent(INDEX_REPORT);
			if (!bHasReportData)
				req();
			// mLayoutScoreBar.setVisibility(View.GONE);
			break;
		case R.id.btn_quiz:
			txtTitle.setText(R.string.quiz_list_tiele);
			setBackgound(INDEX_QUIZ);
			replaceContent(INDEX_QUIZ);
			curFragmentIndex = INDEX_QUIZ;
			break;
		case R.id.btn_chat:
			setBackgound(INDEX_CHAT);
			replaceContent(INDEX_CHAT);
			break;
		case R.id.btn_follow:
			setFollow();
			break;
		case R.id.btn_play:
			if (movieUrl == null || movieUrl.equals(""))
				showToast("暂无视频直播");
			else {
				Intent in = new Intent(this, WebViewActivity.class);
				in.putExtra("url", movieUrl);
				startActivity(in);

			}
			break;
		case R.id.btn_sent:
			Intent in = new Intent(this, ChatInputActivity.class);
			in.putExtra("tag", TAG);
			in.putExtra("roomid", roomid);
			startActivityForResult(in, REQ_SEND_MSG);
			b2Chat = true;
			break;
		case R.id.btn_land:
			showTipsDialog();
			break;
		case R.id.btn_close:
			closeDialog();
			break;

		case R.id.gold_num:
			if (mToken == null) {
				showBindDialog(SharedPreferencesMgr.getString("dialogBtnText",
						getString(R.string.casino_notify)));
				// dialog(this, SharedPreferencesMgr.getString("dialogRecharge",
				// getString(R.string.casino_notify)));
			} else {

				Intent info = new Intent(this, HupuOrderActivity.class);
				startActivity(info);
			}

			break;
		case R.id.layout_live_title:
			if(curFragmentIndex == INDEX_LIVE){
				if(mRoomView.getVisibility()==View.GONE)
				{
					reqRoomlist();
				}
				else
				{
					hideRoomAction();
					selectDefaultRoom();
					title_room_switch.setImageResource(R.drawable.icon_arrow_up_live);
				}
			}
			break;
		case R.id.choose_room_dialog:
			if(curFragmentIndex == INDEX_LIVE){
				hideRoomAction();
				selectDefaultRoom();
				title_room_switch.setImageResource(R.drawable.icon_arrow_up_live);
			}
			break;
		}
	}

	public void selectDefaultRoom()
	{
		if(livefirstIn)
		{
			req_room_http(roomid);
			livefirstIn = false;
		}

	}

	boolean b2Chat;
	private boolean bottombarVisible = false;
	private void showBottomBar() {
		if(findViewById(R.id.layout_bottom)!=null)
		{
			findViewById(R.id.layout_bottom).setVisibility(View.VISIBLE);
			bottombarVisible = true;
		}
	}

	private void replaceContent(int index) {
		if (curFragmentIndex == index)
			return;
		Fragment fragment = null;

		curFragmentIndex = index;

		switch (index) {
		case INDEX_REPORT:
			if (mFragmentReport == null)
				mFragmentReport = new ReportFragment();

			fragment = mFragmentReport;
			txtTitle.setText("战报");

			setScreenLight(false);
			break;
		case INDEX_LIVE:
			//			txtTitle.setText("直播");

			// 如果是人工直播，就显示直播页面
			if (mFragmentLive == null) {
				mFragmentLive = new LiveFragment(
						0,
						0,
						(curGameState != GAME_STATE_NOT_START
						&& curGameState != GAME_STATE_CANCEL && curGameState != GAME_STATE_DELAY));
			}
			if (curGameState == GAME_STATE_END
					|| curGameState == GAME_STATE_ONGOING)
				mFragmentLive.isStart(true);
			fragment = mFragmentLive;

			mFragmentLive.entry();

			if (!bHasLiveData || (bEnd && !getLiveEndData)) {
				// 初次进入该页面需要获取全部的实时播报信息
				if (curGameState == GAME_STATE_ONGOING)// 进行中
				{
					mParams.put("sort", "desc");
					sortId = HuPuRes.REQ_METHOD_GET_LIVE_DESC;
					reqHttp(HuPuRes.REQ_METHOD_GET_LIVE_DESC);
				} else {
					mParams.put("sort", "asc");
					sortId = HuPuRes.REQ_METHOD_GET_LIVE_ASC;
					reqHttp(HuPuRes.REQ_METHOD_GET_LIVE_ASC);
				}
			} else if (curGameState != GAME_STATE_END) {
				mFragmentLive.setPreview(roomPreview);
				if (roomPreview != null && !"".equals(roomPreview)) {
					// 设置前瞻
					mFragmentLive.setURL(roomPreview);
				}
				bJoinRoom = true;
				// 加入前设置最后一条消息id
				setJosnObj(BaseEntity.KEY_PID, lastLiveID);
				jsonRoom.remove("type");
				jsonRoom.remove("direc");
				jsonRoom.remove("num");
				JSONArray arr = mFragmentLive.getQids();
				if (arr != null)
					setJsonObj("qids", mFragmentLive.getQids());
				joinRoom(HuPuRes.ROOM_CBA_PLAYBYPLAY_CASINO);
			}

			setScreenLight(false);

			break;
		case INDEX_STATISTIC:
			if (mStatisticFragment == null) {
				mStatisticFragment = new CBAStatisticFragment(
						mGameEntity.str_home_name, mGameEntity.str_away_name);
			}
			if (!bHasStatisticData || (bEnd && !getBoxEndData)) {
				// mParams.put(BaseEntity.KEY_VERTICAL, "" + true);
				// reqHttp(HuPuRes.REQ_METHOD_CBA_BOX_SCORE);
			} else if (curGameState != GAME_STATE_END) {
				// joinRoom(HuPuRes.ROOM_CBA_BOXSCORE);
				// bJoinRoom = true;
			}
			mParams.put(BaseEntity.KEY_VERTICAL, "" + true);
			reqHttp(HuPuRes.REQ_METHOD_CBA_BOX_SCORE);

			fragment = mStatisticFragment;

			txtTitle.setText("统计");
			setScreenLight(false);
			break;

		case INDEX_STATISTIC_LAND:
			if (mStatisticLandFragment == null) {
				mStatisticLandFragment = new CBAStatisticLandFragment(
						mGameEntity);
			}
			mParams.put(BaseEntity.KEY_VERTICAL, "" + false);
			reqHttp(HuPuRes.REQ_METHOD_CBA_BOX_SCORE);

			fragment = mStatisticLandFragment;
			break;
		case INDEX_CHAT:
			if (mFragmentChat == null) {
				mFragmentChat = new ChatFragment();
				mFragmentChat.setTag(TAG);
			}
			bJoinRoom = true;
			mFragmentChat.entry(roomid);
			fragment = mFragmentChat;
			txtTitle.setText("热线");

			// 一直常亮
			setScreenLight(true);
			break;
		case INDEX_QUIZ:
			if (mFragmentQuizList == null) {
				mFragmentQuizList = new QuizListFragment();
				// mFragmentQuizList.setBets(bets);
			}

			fragment = mFragmentQuizList;
			// setJsonObj(BaseEntity.KEY_GAME_ID, mEntityGame.i_gId);
			joinRoom(tag.toUpperCase() + HuPuRes.ROOM_CASINO);
			// if (mQid >0)
			// mFragmentQuizList.setSelection(mQid);

			break;
		}

		if (index != INDEX_CHAT && index != INDEX_LIVE && index != INDEX_QUIZ) {
			// ---
			if (bJoinRoom) {
				leaveRoom();
			}
		}
		if (fragment != null)
			replaceContent(fragment);

	}

	private void replaceContent(Fragment fragment) {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.fragment_content, fragment);
		transaction.addToBackStack(null);
		transaction.commitAllowingStateLoss();
	}

	/** 点击后的按钮背景变化 */
	private void setBackgound(int index) {
		int color = getResources().getColor(R.color.transform);
		initGift(null,index,false);
		switchRoomStatus(index,livefirstIn);
		// 点击前
		switch (curFragmentIndex) {
		case INDEX_REPORT:
			// mLayoutScoreBar.setVisibility(View.VISIBLE);
			btnReport.setBackgroundColor(color);
			btnReport.setImageResource(R.drawable.btn_report);
			mLayoutScoreBar.setVisibility(View.VISIBLE);
			break;
		case INDEX_LIVE:
			//			findViewById(R.id.layout_gift).setVisibility(View.VISIBLE);
			findViewById(R.id.layout_title_btn).setVisibility(View.GONE);
			if (indexLive == R.id.btn_second)// 进行中
			{
				btnSecond.setBackgroundColor(color);
				btnSecond.setImageResource(R.drawable.btn_live_up);
			} else {
				btnThird.setBackgroundColor(color);
				btnThird.setImageResource(R.drawable.btn_live_up);
			}
			break;
		case INDEX_STATISTIC:
			if (indexStatistic == R.id.btn_second) {
				btnSecond.setBackgroundColor(color);
				btnSecond.setImageResource(R.drawable.btn_statistics);
			} else {
				btnThird.setBackgroundColor(color);
				btnThird.setImageResource(R.drawable.btn_statistics);
			}
			findViewById(R.id.btn_land).setVisibility(View.GONE);
			unregistSetting();
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			break;
		case INDEX_CHAT:
			btnChat.setBackgroundColor(color);
			btnChat.setImageResource(R.drawable.btn_chat_up);
			findViewById(R.id.btn_sent).setVisibility(View.GONE);
			break;
		case INDEX_QUIZ:
			btnQuiz.setBackgroundColor(color);
			btnQuiz.setImageResource(R.drawable.btn_tag_guess_up);
			findViewById(R.id.gold_num).setVisibility(View.GONE);
			break;
		}

		switch (index) {// 点击后
		case INDEX_REPORT:
			mLayoutScoreBar.setVisibility(View.VISIBLE);
			btnReport.setBackgroundResource(R.drawable.bg_bottom_hover);
			btnReport.setImageResource(R.drawable.btn_report_hover);
			break;
		case INDEX_LIVE:
			findViewById(R.id.layout_gift).setVisibility(View.VISIBLE);
			mLayoutScoreBar.setVisibility(View.VISIBLE);
			if (indexLive == R.id.btn_second) {
				btnSecond.setBackgroundResource(R.drawable.bg_bottom_hover);
				btnSecond.setImageResource(R.drawable.btn_live_down);
			} else {
				btnThird.setBackgroundResource(R.drawable.bg_bottom_hover);
				btnThird.setImageResource(R.drawable.btn_live_down);
			}
			if (curGameState == GAME_STATE_ONGOING
					|| curGameState == GAME_STATE_NOT_START
					|| curGameState == GAME_STATE_CANCEL
					|| curGameState == GAME_STATE_DELAY) {
				findViewById(R.id.layout_title_btn).setVisibility(View.VISIBLE);
			}

			findViewById(R.id.btn_sent).setVisibility(View.GONE);
			if (curGameState != ScoreboardEntity.STATUS_END
					&& curGameState != ScoreboardEntity.STATUS_CANCEL)
				findViewById(R.id.layout_title_btn).setVisibility(View.VISIBLE);
			break;
		case INDEX_STATISTIC:
			mLayoutScoreBar.setVisibility(View.VISIBLE);
			if (indexStatistic == R.id.btn_second) {
				btnSecond.setBackgroundResource(R.drawable.bg_bottom_hover);
				btnSecond.setImageResource(R.drawable.btn_statistics_hover);
			} else {
				btnThird.setBackgroundResource(R.drawable.bg_bottom_hover);
				btnThird.setImageResource(R.drawable.btn_statistics_hover);
			}
			registSetting();
			if (Settings.System.getInt(getContentResolver(),
					Settings.System.ACCELEROMETER_ROTATION, 0) != 0)
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
			findViewById(R.id.btn_land).setVisibility(View.VISIBLE);
			findViewById(R.id.layout_title_btn).setVisibility(View.GONE);

			break;
		case INDEX_CHAT:
			mLayoutScoreBar.setVisibility(View.VISIBLE);
			btnChat.setBackgroundResource(R.drawable.bg_bottom_hover);
			btnChat.setImageResource(R.drawable.btn_chat_down);
			findViewById(R.id.layout_title_btn).setVisibility(View.GONE);
			findViewById(R.id.btn_sent).setVisibility(View.VISIBLE);
			break;

		case INDEX_QUIZ:
			mLayoutScoreBar.setVisibility(View.GONE);
			btnQuiz.setBackgroundResource(R.drawable.bg_bottom_hover);
			btnQuiz.setImageResource(R.drawable.btn_tag_guess_down);

			findViewById(R.id.gold_num).setVisibility(View.VISIBLE);
			findViewById(R.id.layout_title_btn).setVisibility(View.GONE);
			imgRedPiont.setVisibility(View.GONE);

			break;

		}
	}

	int[] bets;

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		if (o == null)
			return;

		// 数据过来后 底部显示出来
		if (!bottombarVisible) {
			showBottomBar();
		}
		switch (methodId) {
		case HuPuRes.REQ_METHOD_CBA_GAME_BY_GID:// 根据gid获取单场比赛信息
			mGameEntity = (BasketballGameEntity) o;
			init();
			break;
		case HuPuRes.REQ_METHOD_GET_LIVE_DESC:
		case HuPuRes.REQ_METHOD_GET_LIVE_ASC:
			SimpleLiveResp data = (SimpleLiveResp) o;
			if (data != null) {
				initGift(data,INDEX_LIVE,true);
				movieUrl = data.tvLink;
				if (!"".equals(movieUrl))
					btnPlay.setEnabled(true);
				mFragmentLive.addData(true);
				isPreview = data.preview != null&& !"".equals(data.preview)?true:false;
				//				boolean isShowRoomlist = data.is_enter==0?true:false;

				if (data.roomList != null) {

					boolean isShow = data.roomList!=null&&data.roomList.size()>1?true:false;

					if (data.is_enter == 0&&isShow) {
						livefirstIn = true;
					}else{
						livefirstIn = false;
					}
					//重置roomid
					roomid = data.default_room_id;
					switchRoomStatus(INDEX_LIVE,livefirstIn);
					setRoomList(data.roomList,data.default_room_id);
					for (RoomEntity entity:data.roomList) {
						if (entity.id == data.default_room_id) {
							if(data.is_enter == 0){
								if(data.roomList!=null&&data.roomList.size()>1)
								{
									setRoomTitle(getString(R.string.title_select_room));
									mRoomListAdapter.setDefaultId(-1);
									mRoomListAdapter.notifyDataSetChanged();
									showRoomAction();
								}
								curRoomtitle = entity.name;
								curRoomOnline = String.format(live_online, entity.count);


							}else{
								setRoomTitle(entity.name);
								setRoomOnlineNum(String.format(live_online, entity.count),true);

							}
						}
					}

				}

				roomPreview = data.preview;
				mFragmentLive.setPreview(roomPreview);
				if (data.preview != null&& !"".equals(data.preview)) {
					// 设置前瞻
					mFragmentLive.setURLForCBA(data.preview);
					return;
				}
				//			if (data.scoreBoard != null && data.scoreBoard.live == 0) {
				//				if (data.preview != null&& !"".equals(data.preview)) {
				//					// 设置前瞻
				//					mFragmentLive.setURLForCBA(data.preview);
				//					return;
				//				}
				////				return;
				//			} else {
				//				if (curGameState != GAME_STATE_NOT_START
				//						&& curGameState != GAME_STATE_CANCEL
				//						&& curGameState != GAME_STATE_DELAY)
				//					mFragmentLive.isStart(true);
				//				else if (data.preview != null) {
				//					// 设置前瞻
				//					mFragmentLive.setURLForCBA(data.preview);
				//				}
				//			}

				if (data.casinoInit != null) {
					bets = data.casinoInit.bets;
					mFragmentLive.setBets(data.casinoInit.bets);
				}
		
				setJsonObj("roomid", roomid);
				if (data.dataList == null) {
					// 没有数据
					if (curGameState == GAME_STATE_NOT_START) {
						// showToast("比赛未开始");
						bJoinRoom = true;
						jsonRoom.remove("type");
						jsonRoom.remove("direc");
						jsonRoom.remove("num");

						joinRoom(HuPuRes.ROOM_CBA_PLAYBYPLAY);
					} else {
						showToast("暂无直播数据");
					}
					mFragmentLive.addData(true);
					//				return;
				}
				mFragmentLive.setData(data.dataList);

				bHasLiveData = true;
				lastLiveID = data.i_pId;
				// Log.d("http back pid", ""+data.i_pId);

				//			if (data.dataList == null) {
				//				// 没有数据
				//				if (curGameState == GAME_STATE_NOT_START) {
				//					// showToast("比赛未开始");
				//					bJoinRoom = true;
				//					joinRoom(HuPuRes.ROOM_CBA_PLAYBYPLAY_CASINO);
				//				} else {
				//					showToast("暂无直播数据");
				//				}
				//				mFragmentLive.addData(true);
				//				//return;
				//			}
				//
				//			mFragmentLive.setData(data.dataList);
				//
				//			bHasLiveData = true;
				//			lastLiveID = data.i_pId;
				// Log.d("http back pid", ""+data.i_pId);

				// 如果是比赛
				if (methodId == HuPuRes.REQ_METHOD_GET_PLAY_LIVE_DESC){
					setJsonObj(BaseEntity.KEY_PID, data.i_pId);
				}

				// 如果是比赛
				if (methodId == HuPuRes.REQ_METHOD_GET_LIVE_DESC)
					setJosnObj(BaseEntity.KEY_PID, data.i_pId);
				if (curGameState != GAME_STATE_CANCEL) {// 如果比赛未结束需要加入房间
					bJoinRoom = true;
					setJsonObj("roomid", data.default_room_id);
					jsonRoom.remove("type");
					jsonRoom.remove("direc");
					jsonRoom.remove("num");
					setJsonObj("roomid", data.default_room_id);
					joinRoom(HuPuRes.ROOM_CBA_PLAYBYPLAY);
					// Log.d("join", "ROOM_CBA_PLAYBYPLAY");
				} else {
					getLiveEndData = true;
				}
				if (data.scoreBoard != null) {
					setSore(data.scoreBoard.i_scoreHome,
							data.scoreBoard.i_scoreAway);
					updateProccess(data.scoreBoard.str_process);
				}

			}

			break;
		case HuPuRes.REQ_METHOD_CBA_BOX_SCORE:
			CBABoxScoreResp d = (CBABoxScoreResp) o;
			if (d.mEntityHome == null) {
				// 没有数据
				mStatisticFragment.addData(true);
				showToast("暂无统计数据");
				return;
			}

			mStatisticFragment.setData(d);
			bHasStatisticData = true;

			setJsonObj("bid", d.i_bId);
			if (curGameState != GAME_STATE_END) {
				setJsonObj("roomid", roomid);
				joinRoom(HuPuRes.ROOM_CBA_BOXSCORE);
				bJoinRoom = true;
			} else {
				getBoxEndData = true;
			}
			if (isLandMode) {
				mStatisticLandFragment.setData(d);
			}
			break;

		case HuPuRes.REQ_METHOD_GET_GAME_REPORT:
			Recap re = (Recap) o;
			mFragmentReport.setData(re);
			break;
		case HuPuRes.REQ_METHOD_EN_SENT_CHAT:
			SendMsgResp smrsp = (SendMsgResp) o;
			if (smrsp.err != null) {
				// Log.d("SendMsgResp", data.err);
				showToast(smrsp.err);
			} else {
				reqChatData(lastChatID);

				if (smrsp.pid == 0) {
					reqChatData(lastChatID);
				} else {
					mFragmentChat.setLastId(smrsp.pid);
					lastChatID = smrsp.pid;
					showToast("发送成功");
					setJosnObj(BaseEntity.KEY_PID, lastChatID);
					setJosnObj("direc", "next");
					if (mToken != null)
						setJosnObj("tk", "mToken");
					setJsonObj("roomid", roomid);
					joinRoom(HuPuRes.ROOM_CBA_PLAYBYPLAY);
				}
			}
			break;

		case HuPuRes.REQ_METHOD_FOLLOW_GAME:
		case HuPuRes.REQ_METHOD_FOLLOW_GAME_CANCEL:
			FollowResp resp = (FollowResp) o;
			if (resp == null || resp.i_success == 0) {
				// 提交失败
				showToast(String.format(SORRY_NOTIFY,
						mGameEntity.str_home_name, mGameEntity.str_away_name));
				isFollow = !isFollow;
				setFollowBtn();
			} else {
				if (isFollow)
					mGameEntity.bFollow = 1;
				else
					mGameEntity.bFollow = 0;
				if (methodId == HuPuRes.REQ_METHOD_FOLLOW_GAME)
					showToast(String.format(SUCCESS_NOTIFY,
							mGameEntity.str_home_name,
							mGameEntity.str_away_name));
				if (methodId == HuPuRes.REQ_METHOD_FOLLOW_GAME_CANCEL)
					showToast(CANCEL_NOTIFY);
			}
			break;

		case HuPuRes.REQ_METHOD_GET_DOLE:
			QuizCommitResp dole = new QuizCommitResp();
			if (dole.result == -1)
				showToast("请重新登录");
			else if (dole.result == -2) {
				// 通知领救济金
				showToast("今天已经领过");
			} else if (dole.result == -3) {
				showToast("领取失败");
			} else {
				showToast("领取成功");
			}

			break;
		case HuPuRes.REQ_METHOD_QUIZ_LIST:
		case HuPuRes.REQ_METHOD_QUIZ_LIST_COMMIT:
		case HuPuRes.REQ_METHOD_POST_INCREASE:
		case HuPuRes.REQ_METHOD_BET_COINS:
		case HuPuRes.REQ_METHOD_CAIPIAO_COMMIT:
			if (curFragmentIndex == INDEX_QUIZ)
				mFragmentQuizList.onReqResponse(o, methodId, mQid);

			break;
//		case HuPuRes.REQ_METHOD_GET_PLAY_LIVE_SEND_GIFT:
//			if (o != null) {
//				GiftRespResultEntity gren = (GiftRespResultEntity)o;
//				//				this.hupuDollor = gren.balance;
//				this.interval = gren.interval;
//			}
//
//			break;
//		case HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_BALANCE:
//			if (o != null) {
//				HupuDollorBalanceReq info = (HupuDollorBalanceReq) o;
//				this.hupuDollor=info.balance;
//			}
//			break;
		case HuPuRes.REQ_METHOD_GET_PLAY_LIVE_ROOMLIST:
			if (o != null) {
				RoomListEntity info = (RoomListEntity) o;
				if(info!=null&&info.roomList!=null)
				{
					//2014-11-24 修改
					//					if(isInitCbaRoom)
					//					{
					//						mRoomListAdapter.setData(info.roomList);
					//						mRoomListAdapter.notifyDataSetChanged();
					//					}
					//					else
					//					{
					//						switchRoomStatus(INDEX_LIVE,false);
					//						setRoomList(info.roomList,roomid);
					//					}

					setRoomList(info.roomList,roomid);
					showRoomAction();
				}

			}
			break;
		}
		// updateProccess(data.str_content) ;
		//		findViewById(R.id.layout_bottom).setVisibility(View.VISIBLE);
	}

	@Override
	public void onReqResponse(Object o, int methodId, int msg1, int msg2) {
		super.onReqResponse(o, methodId);
		if (o == null)
			return;
		switch (methodId) {
		case HuPuRes.REQ_METHOD_QUIZ_COMMIT:
		case HuPuRes.REQ_METHOD_QUIZ_COMMIT_INCREASE:
			// 提交竞猜
			QuizCommitResp entity = (QuizCommitResp) o;
			if (entity.result == -1)
				showToast("参与过了");
			else if (entity.result == 2) {
				// 通知领救济金
				showCustomDialog(DIALOG_SHOW_GET_DOLE, 0,
						R.string.get_now_info, ONE_BUTTON,
						R.string.title_confirm, 0);
				mFragmentLive.updateCommit(msg1, msg2);
			} else if (entity.result == -2 || entity.result == -3) {
				//				showToast("金豆余额不足");
				//				showNoMoney(0, 0);
				if(!isExchange)
				{
					mFragmentLive.showNoMoney(entity.eGoldBean);
				}
			} else if (entity.result == -4) {
				showToast("竞猜已关闭");
			} else {
				showToast("参与成功");
				mFragmentLive.updateCommit(msg1, msg2);
			}
			break;

		}
	}

	private void setFollow() {
		if (isFollow) {
			followGame(lid, mGameEntity.i_gId, (byte) 1);

		} else {
			if (mApp.needNotify) {

				followGame(lid, mGameEntity.i_gId, (byte) 0);

			} else {
				showCustomDialog(DIALOG_NOTIFY, R.string.push_title,
						R.string.push_open_notify, 3, R.string.open_notify,
						R.string.cancel);
				return;
			}
		}
		// 立即修改
		isFollow = !isFollow;
		setFollowBtn();
	}

	private final int DIALOG_NOTIFY = 1315;

	@Override
	public void clickNegativeButton(int dialogId) {
		super.clickNegativeButton(dialogId);
		if (dialogId == DIALOG_NOTIFY || DIALOG_SHOW_GET_DOLE == dialogId) {
			if (mDialog != null)
				mDialog.cancel();
		}
	}

	@Override
	public void clickPositiveButton(int dialogId) {
		super.clickPositiveButton(dialogId);
		if (dialogId == DIALOG_NOTIFY) {
			// 打开通知
			if (mDialog != null)
				mDialog.cancel();
			mApp.setNotify(true);

			followGame(lid, mGameEntity.i_gId, (byte) 0);
			isFollow = !isFollow;
			setFollowBtn();
		} else if (DIALOG_SHOW_GET_DOLE == dialogId) {
			if (mDialog != null)
				mDialog.cancel();
			sendRequest(HuPuRes.REQ_METHOD_GET_DOLE, mParams,
					new HupuHttpHandler(this), false);
		}
	}

	/** 设置球队名称 */
	private void setTeamName() {
		// 球队名称
		txtTeamLeft.setText(mGameEntity.str_home_name);
		txtTeamRight.setText(mGameEntity.str_away_name);
		UrlImageViewHelper.setUrlDrawable(imgTeamLeft, mGameEntity.home_logo,
				R.drawable.bg_home_nologo);
		UrlImageViewHelper.setUrlDrawable(imgTeamRight, mGameEntity.away_logo,
				R.drawable.bg_home_nologo);

	}

	private void updateScoreView() {
		if (mGameEntity.byt_status == GAME_STATE_END
				|| mGameEntity.byt_status == GAME_STATE_ONGOING) {
			// 如果比赛结束显示结果
			// scoreView = findViewById(R.id.layout_score);
			// scoreView.setVisibility(View.VISIBLE);
			txtStartTime.setVisibility(View.GONE);
			setSore(mGameEntity.i_home_score, mGameEntity.i_away_score);
			updateProccess(mGameEntity.str_process);
		}
		// else if (data.byt_status == GAME_STATE_ONGOING) {
		// // 显示进行中
		// txtStartTime.setText("进行中");
		// }
		else if (mGameEntity.byt_status == GAME_STATE_NOT_START) {
			txtScore.setVisibility(View.GONE);
			txtStartTime.setVisibility(View.VISIBLE);
			txtStartTime.setText(MyUtility.getStartTime(
					mGameEntity.l_begin_time * 1000, sdf));
		} else {
			txtStartTime.setText("已取消");
		}

	}

	/** 设置关注比赛的UI状态 */
	private void setFollowBtn() {

		if (curGameState == GAME_STATE_END || curGameState == GAME_STATE_CANCEL) {
			findViewById(R.id.layout_title_btn).setVisibility(View.GONE);
		} else {
			findViewById(R.id.layout_title_btn).setVisibility(View.VISIBLE);
			if (isFollow) {
				btnFollow.setBackgroundResource(R.drawable.btn_alarm_bright);
			} else {
				btnFollow.setBackgroundResource(R.drawable.btn_alarm_dark);
			}
		}
	}

	/**
	 * 根据不同的比赛状态调整显示的view
	 * 
	 **/
	void setViewByState(boolean init) {

		if (curGameState == GAME_STATE_END) {
			// 比赛结束后第一个按键是战报，第二个是统计，三个是直播，第四个是热线
			btnReport.setVisibility(View.VISIBLE);
			btnSecond.setImageResource(R.drawable.btn_statistics);
			btnThird.setVisibility(View.VISIBLE);
			btnThird.setImageResource(R.drawable.btn_live_up);
			indexLive = R.id.btn_third;
			indexStatistic = R.id.btn_second;
			if (!init) {
				// setBackgound(curFragmentIndex);
				int color = getResources().getColor(R.color.transform);
				btnSecond.setBackgroundColor(color);
				btnThird.setBackgroundColor(color);
				// Log.d("setViewByStatus",
				// "curFragmentIndex="+curFragmentIndex+"  ;i_liveIndex="+i_liveIndex+";i_staticIndex="+i_staticIndex);
				if (INDEX_LIVE == curFragmentIndex) {
					btnThird.setBackgroundResource(R.drawable.bg_bottom_hover);
					btnThird.setImageResource(R.drawable.btn_live_down);
				} else if (INDEX_STATISTIC == curFragmentIndex) {
					btnSecond.setBackgroundResource(R.drawable.bg_bottom_hover);
					btnSecond.setImageResource(R.drawable.btn_statistics_hover);
				}
			}

			// setScreenLight(false);// game over

		} else if (curGameState == GAME_STATE_ONGOING
				|| curGameState == GAME_STATE_NOT_START
				|| curGameState == GAME_STATE_CANCEL
				|| curGameState == GAME_STATE_DELAY) {
			indexLive = R.id.btn_second;
			indexStatistic = R.id.btn_third;
			btnSecond.setImageResource(R.drawable.btn_live_up);
			btnThird.setImageResource(R.drawable.btn_statistics);
			if (curGameState == GAME_STATE_ONGOING && !init) {
				mLayoutScoreBar.setVisibility(View.VISIBLE);
				txtScore.setVisibility(View.VISIBLE);
				setSore(mGameEntity.i_home_score, mGameEntity.i_away_score);
				txtStartTime.setVisibility(View.GONE);
				txtProcess.setText(mGameEntity.str_process);
				// mPager.setCurrentItem(INDEX_LIVE, false);
				replaceContent(INDEX_LIVE);
				setBackgound(INDEX_LIVE);
				setScreenLight(true);
			}
			if (curGameState == GAME_STATE_NOT_START
					|| curGameState == GAME_STATE_CANCEL
					|| curGameState == GAME_STATE_DELAY) {
				//
				btnThird.setVisibility(View.GONE);
			} else {
				btnThird.setVisibility(View.VISIBLE);
			}
		}
	}

	/** 设置球队比分 需要翻牌，这个逻辑就需要先判断比分的变换了。 */
	private void setSore(int homeScore, int awayScore) {
		if (homeScore > 0 || awayScore > 0)
			txtScore.setText(homeScore + " - " + awayScore);
	}

	/** 更新比赛过程的标题信息 */
	private void updateProccess(String s) {
		if (s != null)
			txtProcess.setText(s);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (bJoinRoom) {
			// 如果需要加入房间
			if (DeviceInfo.isNetWorkEnable(this)) {
				// 如果是非聊天
				if (curFragmentIndex == INDEX_LIVE) {
					setJosnObj(BaseEntity.KEY_PID, lastLiveID);
					JSONArray arr = mFragmentLive.getQids();
					if (arr != null)
						setJsonObj("qids", arr);
					joinRoom();
				} else if (curFragmentIndex == INDEX_CHAT) {
					// reqChatData(lastChatID);
					if (lastChatID > 0)
						setJosnObj(BaseEntity.KEY_PID, lastChatID);
					else
						setJosnObj(BaseEntity.KEY_PID, "");
					if (!b2Chat) {
						setJosnObj("direc", "next");
						joinRoom();
					}
					b2Chat = false;
				} else if (curFragmentIndex == INDEX_STATISTIC) {
					if (bid > 0 && !getBoxEndData) {
						joinRoom();
					}
				}

			} else {
				mApp.setNetState(HuPuApp.STATE_NO_NET);
				reconnect(false);
			}
		}
	}

	@Override
	public void onLoginSuccess() {
		// TODO Auto-generated method stub
		super.onLoginSuccess();
		if (curFragmentIndex == INDEX_QUIZ) {
			getQuizList();
			joinRoom();
		}
	}

	private void setJosnObj(String key, int value) {
		JSONObject obj = getRoomObj();
		try {
			obj.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void setJosnObj(String key, String value) {
		JSONObject obj = getRoomObj();
		try {
			obj.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/** 请求热线数据，每页20条 */
	public void reqChatData(int id) {

		setJosnObj("type", tag);
		setJosnObj("num", 20);
		if (id > 0) {
			setJosnObj("pid", id);
			setJosnObj("direc", "prev");

		} else {
			// 重新获取最新的聊天记录，不传偏移量
			setJosnObj("pid", "");
			setJosnObj("direc", "next");
			lastChatID = 0;
		}
		setJsonObj("roomid", roomid);
		joinRoom(HuPuRes.ROOM_NBA_CHAT);
	}

	/** 请求Http数据 */
	private void reqHttp(int repType) {
		mParams.put(BaseEntity.KEY_GAME_ID, "" + gid);
		sendRequest(repType, tag, mParams, new HupuHttpHandler(this), false);
	}

	/** 直播过程中请求刷，就是重新加入room */
	public void reqFresh() {
		if (bJoinRoom && getRoom() != null)
			joinRoom();
		//
		mFragmentLive.stopLoad();

	}

	public void req() {
		if (curFragmentIndex == INDEX_STATISTIC)
			sendRequest(HuPuRes.REQ_METHOD_CBA_BOX_SCORE, mParams,
					new HupuHttpHandler(this));
		else if (curFragmentIndex == INDEX_REPORT)
			sendRequest(HuPuRes.REQ_METHOD_GET_GAME_REPORT, tag, mParams,
					new HupuHttpHandler(this), false);
	}

	@Override
	public void updateMoney(int betCoin, int balance) {
		HupuLog.e("papa", "coin=="+betCoin+"------balance="+balance);
		if (mFragmentLive != null) {
			mFragmentLive.updateMoney(betCoin, money);
		}
	}

	@Override
	public void onSocketConnect() {
		// Log.d("FootballGameAct", "onSocketConnect  >>>>>>:::::");
		super.onSocketConnect();
		// 连接成功了，加入room。
		joinRoom();
		mApp.setNetState(HuPuApp.STATE_CONNECTED);
	}

	@Override
	public void onSocketDisconnect() {
		// Log.d("HupuDataActivity", "onSocketDisconnect  >>>>>>:::::"
		// + android.os.Process.myPid());
		super.onSocketDisconnect();
		mApp.setNetState(HuPuApp.STATE_DISCONNECT);
		setNetTitle();
		// if (bJoinRoom)
		// reconnect(false);
		updateNetState();
	}

	@Override
	public void onErrResponse(Throwable error, String content,
			boolean isBackGroundThread) {

		if (content != null) {
			showToast(content);
		}
	}

	@Override
	public void onSocketResp(JSONObject obj) {
		 Log.d("HupuDataActivity", "onSocketResp  >>>>>>:::::"+obj.toString());
		if (!bottombarVisible) {
			showBottomBar();
		}
		if (obj != null) {
			try {
				String room = obj.optString("room");
				if (HuPuRes.ROOM_USER_NOTIFY.equals(room)) {
					final PushNotify notify = new PushNotify();
					notify.paser(obj);
					if (notify.gid == gid && notify.lid == lid&&(notify.roomid == roomid || notify.roomid == -1)) {
						// mFragmentLive.showPop(notify.qid);
						mQid = notify.qid;

						if (curFragmentIndex != INDEX_QUIZ) {
							if (mToken != null)
								imgRedPiont.setVisibility(View.VISIBLE);

						} else {
							getQuizList();
						}
						if (mToken != null)
							startAnim(notify.coin);
					}
					return;
				}
				int status = obj.optInt(BaseEntity.KEY_STATUS, -1);
				// Log.d("HupuDataActivity", "onSocketResp  >>>>>>::::: status"
				// + status);
				int gd = obj.optInt(BaseEntity.KEY_GAME_ID, -1);
				if (gd != gid)
					return;
				if (status > -1 && status != curGameState) {
					curGameState = status;
					setViewByState(false);
				}
				if (HuPuRes.ROOM_NBA_CHAT.equals(room)) {
					// Log.d("ROOM_CBA_PLAYBYPLAY", "1");
					ChatResp data = new ChatResp();
					data.paser(obj);
					mFragmentChat.stopLoad();
					if (data.mList != null) {
						if (data.direc.equals("next") && data.pid > 0
								&& data.pid <= lastChatID)
							return;
					}
					mFragmentChat.setData(data,roomid);
					lastChatID = mFragmentChat.getLastId();
					if (curFragmentIndex == INDEX_CHAT && data.online != null)
						txtTitle.setText("热线(" + data.online + "人)");
					if (data.score != null) {
						setSore(data.score.i_scoreHome, data.score.i_scoreAway);
						updateProccess(data.score.str_process);
					}
				} else if (HuPuRes.ROOM_CBA_PLAYBYPLAY.equals(room)) {
					if (curGameState == GAME_STATE_END) {
						bEnd = true;
						getLiveEndData = true;
					}
					// 直播
					SimpleLiveResp data = new SimpleLiveResp();
					data.paser(obj);

					mFragmentLive.stopLoad();
					if (data.bHasData) {
						// Log.d("bHasData", "bHasData");
						if (data.i_pId > lastLiveID && data.i_pId > -1)
							mFragmentLive.updateData(data);
					} else if (data.casinoList != null) {
						// Log.d("bHasData", "no data");
						mFragmentLive.updateData(data);
					}
					if (data.casinoInit != null)
						mFragmentLive.setBets(data.casinoInit.bets);
					if (data.i_pId > -1 && data.i_pId > lastLiveID) {
						lastLiveID = data.i_pId;
						// 更新消息id
						setJosnObj(BaseEntity.KEY_PID, data.i_pId);
					}
					if (curFragmentIndex == INDEX_LIVE && data.people_num != null)
					{

						//							txtTitle.setText("直播(" + data.online + "人)");
						boolean isShowRoomlist = false;

						boolean isShow = false;
						if (data.roomList != null) {

							isShow = data.roomList!=null&&data.roomList.size()>1?true:false;

							if (data.is_enter == 0&&isShow) {
								isShowRoomlist = true;
							}else{
								isShowRoomlist = false;
							}
						}
						setRoomOnlineNum(String.format(this.live_online, data.people_num),isShowRoomlist);

					}
					if (data.scoreBoard != null) {
						setSore(data.scoreBoard.i_scoreHome,
								data.scoreBoard.i_scoreAway);
						updateProccess(data.scoreBoard.str_process);
					}
					if(data.giftList!=null && data.giftList.size()>0){
						pushUpdataGift(data.giftList);
					}

				}
				else if (HuPuRes.ROOM_CBA_PLAYBYPLAY.equals(room)) {
					if (curGameState == GAME_STATE_END) {
						bEnd = true;
						getLiveEndData = true;
					}
					// 直播
					SimpleLiveResp data = new SimpleLiveResp();
					data.paser(obj);

					if (curFragmentIndex == INDEX_LIVE
							&& data.people_num != null)
						//						txtTitle.setText("直播(" + data.people_num + "人)");
					{
						boolean isShowRoomlist = data.is_enter == 0?true:false;
						setRoomOnlineNum(String.format(this.live_online, data.people_num),isShowRoomlist);
					}

					mFragmentLive.stopLoad();
					if (data.bHasData) {
						if (data.i_pId > lastLiveID && data.i_pId > -1)
							mFragmentLive.updateData(data);
					} else if (data.casinoList != null) {
						Log.d("bHasData", "no data");
						mFragmentLive.updateData(data);
					}
					if (data.i_pId > -1 && data.i_pId > lastLiveID) {
						lastLiveID = data.i_pId;
						// 更新消息id
						setJsonObj(BaseEntity.KEY_PID, data.i_pId);
					}
					if (data.scoreBoard != null) {
						setSore(data.scoreBoard.i_scoreHome,
								data.scoreBoard.i_scoreAway);
						updateProccess(data.scoreBoard.str_process);
					}
					if(data.giftList!=null && data.giftList.size()>0){
						pushUpdataGift(data.giftList);
					}
				} 
				else if (HuPuRes.ROOM_CBA_BOXSCORE.equals(room)) {
					if (curGameState == GAME_STATE_END) {
						bEnd = true;
						getBoxEndData = true;
					}
					CBABoxScoreResp d = new CBABoxScoreResp();
					d.paser(obj);
					bid = obj.optInt("bid", -1);
					// 更新消息id
					if (bid > 0)
						setJsonObj("bid", bid);
					setSore(d.i_scoreHome, d.i_scoreAway);
					updateProccess(d.str_process);
					mStatisticFragment.updateData(d);
				} else if (HuPuRes.ROOM_USER_NOTIFY.equals(room)) {
					final PushNotify notify = new PushNotify();
					notify.paser(obj);
					// 中奖了
					// mFragmentLive.showPop(notify.qid);
					// mFragmentLive.QuizeRewardAnimationManage(notify.coin,
					// true);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		mApp.setNetState(HuPuApp.STATE_ON_LINE);

	}

	@Override
	protected void onStop() {
		if (bJoinRoom)
			reqLeaveRoom();
		super.onStop();
	}

	/** 切换到竖直模式 **/
	public void switchToPortraitMode() {

		isLandMode = false;
		quitFullScreen();
		replaceContent(INDEX_STATISTIC);
		findViewById(R.id.layout_title_bar).setVisibility(View.VISIBLE);
		findViewById(R.id.layout_score_bar).setVisibility(View.VISIBLE);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				findViewById(R.id.layout_bottom).setVisibility(View.VISIBLE);
			}

		}, 600);

	}

	/** 切换到水平模式 **/
	public void switchToLandMode() {
		closeDialog();
		isLandMode = true;
		setFullScreen();
		replaceContent(INDEX_STATISTIC_LAND);

		// 隐藏title，和底部栏
		findViewById(R.id.layout_title_bar).setVisibility(View.GONE);
		findViewById(R.id.layout_bottom).setVisibility(View.GONE);
		findViewById(R.id.layout_score_bar).setVisibility(View.GONE);
	}

	/***********************房间相关*****************************/
	/**
	 * 请求房间列表
	 */
	private void req_room_http(int id) {
		if (sortId == HuPuRes.REQ_METHOD_GET_LIVE_DESC) 
			mParams.put("sort", "desc");
		else
			mParams.put("sort", "asc");

		roomid = id;
		mParams.put("roomid", roomid+"");

		reqHttp(sortId);
	}
	/**
	 * 初始化房间列表和礼物 view
	 */
	private void initRoomAndGift() {
		eAnimation = new EpandDownAnimation(this,new EpandCallBack());
		title_layout = (RelativeLayout)findViewById(R.id.layout_live_title);
		mRoomView = (LinearLayout) findViewById(R.id.choose_room_dialog);
		mRoomListView = (ListView) findViewById(R.id.list_room);
		mRoomListAdapter = new RoomListAdapter(this, click);
	}

	private void setRoomList(final ArrayList<RoomEntity> roomList,int default_id){
		HupuLog.d("papa", "roomlistSize=="+roomList.size());
		isInitCbaRoom = true;
		mRoomListAdapter.setData(roomList);
		mRoomListAdapter.setDefaultId(default_id);
		mRoomListView.setAdapter(mRoomListAdapter);
		mRoomListAdapter.notifyDataSetChanged();

		mRoomListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				RoomEntity roomEntity = roomList == null || roomList.get(position)==null?null:roomList.get(position);
				if(roomEntity!=null)
				{
					req_room_http(roomList.get(position).id);
					setRoomTitle(roomEntity.name);
					setRoomOnlineNum(String.format(live_online, roomEntity.count),true);
				}
				hideRoomAction();
			}
		});
	}

	private void setRoomTitle(String title){
		this.curRoomtitle = title;
		txtTitle.setText(curRoomtitle);
	}

	private void setRoomOnlineNum(String num,boolean isShow){
		if(isShow){
			room_people_numTextView.setVisibility(View.VISIBLE);
		}
		room_people_numTextView.setText(num);
		curRoomOnline = num;
	}

	private EpandDownAnimation eAnimation;

	public void showRoomAction()
	{
		if(eAnimation==null)
		{
			return;
		}
		if(mRoomView.getVisibility()==View.VISIBLE)
		{
			//				mRoomView.setVisibility(View.VISIBLE);
			hideRoomAction();
		}
		else
		{
			eAnimation.startAction_TopToBottom(findViewById(R.id.list_room),mRoomView);
		}
	}

	public void hideRoomAction()
	{
		if(eAnimation==null)
		{
			return;
		}
		eAnimation.finishAction(findViewById(R.id.list_room),mRoomView);
	}

	@SuppressLint("ResourceAsColor")
	private void switchRoomStatus(int curtab,boolean isShowAction)
	{
		if(curtab == INDEX_LIVE)
		{
			if(title_layout!=null)
			{
				title_layout.setClickable(true);
			}
			title_room_switch.setVisibility(View.VISIBLE);
			if(isShowAction){
				room_people_numTextView.setVisibility(View.GONE);
				setRoomTitle(getString(R.string.title_select_room));

			}
			else{
				setRoomTitle(curRoomtitle);
				setRoomOnlineNum(curRoomOnline,true);
			}
			if(mRoomView==null)
			{
				return;
			}
			if(mRoomView.getVisibility()==View.VISIBLE )
			{
//				txtTitle.setTextColor(this.getResources().getColor(R.color.txt_title_live_color));
//				room_people_numTextView.setTextColor(this.getResources().getColor(R.color.txt_title_live_color));
				title_room_switch.setImageResource(R.drawable.icon_arrow_up_live);
			}
			else
			{
				txtTitle.setTextColor(this.getResources().getColor(android.R.color.white));
				room_people_numTextView.setTextColor(this.getResources().getColor(android.R.color.white));
				title_room_switch.setImageResource(R.drawable.icon_arrow_down_live);
			}
		}
		else//非直播tab
		{
			if(title_layout!=null)
			{
				title_layout.setClickable(false);
			}
			if(mRoomView!=null && mRoomView.getVisibility()==View.VISIBLE ){
				mRoomView.setVisibility(View.GONE);
			}

			title_room_switch.setVisibility(View.GONE);
			room_people_numTextView.setVisibility(View.GONE);
			txtTitle.setTextColor(this.getResources().getColor(android.R.color.white));

		}
		//		livefirstIn=false;
	}
	/**
	 * TODO  点击选择房间时候 重新获取房间列表
	 *
	 *
	 * @return void
	 */
	public void reqRoomlist()
	{
		initParameter();
		mParams.put("gid", gid + "");
		mParams.put("lid", lid + "");
		mParams.put("roomid", roomid+"");
		sendRequest(HuPuRes.REQ_METHOD_GET_PLAY_LIVE_ROOMLIST, mParams,
				new HupuHttpHandler(BasketballActivity.this), false);
	}
	protected void onDestroy() {
		//		// TODO Auto-generated method stub
		super.onDestroy();
//		quitLive();
		roomid = -1;
	}
	class EpandCallBack implements EpandAnimationCallBack
	{
		@Override
		public void callBack(int type) {
			switchRoomStatus(INDEX_LIVE,livefirstIn);
		}
	}
}

