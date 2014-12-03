package com.hupu.games.activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
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
import com.hupu.games.activity.HupuBaseActivity.ChatInterface;
import com.hupu.games.adapter.RoomListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.HupuScheme;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.BaseEntity;
import com.hupu.games.data.ChatResp;
import com.hupu.games.data.FollowResp;
import com.hupu.games.data.PushNotify;
import com.hupu.games.data.Recap;
import com.hupu.games.data.SendMsgResp;
import com.hupu.games.data.game.base.SimpleLiveResp;
import com.hupu.games.data.game.base.SimpleScoreboard;
import com.hupu.games.data.game.football.ScoreboardEntity;
import com.hupu.games.data.game.football.SoccerEventsResp;
import com.hupu.games.data.game.football.SoccerLiveResp;
import com.hupu.games.data.game.football.SoccerOutsReq;
import com.hupu.games.data.game.quiz.QuizCommitResp;
import com.hupu.games.data.room.GiftRespResultEntity;
import com.hupu.games.data.room.RoomEntity;
import com.hupu.games.data.room.RoomListEntity;
import com.hupu.games.dialog.EpandDownAnimation;
import com.hupu.games.dialog.EpandDownAnimation.EpandAnimationCallBack;
import com.hupu.games.fragment.ChatFragment;
import com.hupu.games.fragment.FootballLineupFragment;
import com.hupu.games.fragment.FootballLiveFragment;
import com.hupu.games.fragment.LiveFragment;
import com.hupu.games.fragment.QuizListFragment;
import com.hupu.games.fragment.ReportFragment;
import com.hupu.games.fragment.SoccerPlayByPlayFragment;
import com.hupu.games.hupudollor.data.HupuDollorBalanceReq;
import com.hupu.games.pay.HupuOrderActivity;
import com.hupu.http.HupuHttpHandler;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.pyj.common.DeviceInfo;

/** 足球比赛 */
public class FootballGameActivity extends BaseGameLiftActivity implements
ChatInterface {

	private ScoreboardEntity mGameEntity;

	/** 事件直播页 */
	private FootballLiveFragment mFragmentEvents;

	/** 战况页 */
	private SoccerPlayByPlayFragment mFragmentPlayByPlay;

	private QuizListFragment mFragmentQuizList;

	private FootballLineupFragment mFootballLineupFragment;

	TextView txtShootout1;

	TextView txtShootout2;
	// 比分牌
	View scoreLayout;

	TextView txtTime;

	boolean bGameEnd;

	private int curGameState;

	private int period;
	View mLayoutScoreBar;

	private String adUrl;
	/**
	 * 是否为关注比赛，该变量是临时设置，因为关注或取消操作是延时操作，只有数据成功返回后，才是真实情况。 返回失败，则会重置
	 */
	private boolean isFollow;

	private static String SORRY_NOTIFY = "抱歉，%s vs %s闹钟设置失败";
	private static String SUCCESS_NOTIFY = "闹钟设置成功，您将会收到%s vs %s的推送通知";
	private static String CANCEL_NOTIFY = "闹钟取消成功";

	public final static byte STATUS_NOT_START = 1;
	public final static byte STATUS_START = 2;
	public final static byte STATUS_END = 4;
	public final static byte STATUS_EXTEND = 5;
	public final static byte STATUS_CANCEL = 6;

	private String playByPlayRoom = "%s_PLAYBYPLAY";

	/** 阵容 */
	private ImageView btnLineup;

	boolean showLineUp;

	LinearLayout layoutBottom;

	/** 最近一次的时间，由于当前的时间采用服务端推送和客户端倒计时同时进行，两者间存在误差，根据产品需求，当前采用保留最大的时间值，忽略较小的值。 */
	private int lastTime;

	ImageButton btnLive;
	/** 赛况 */
	ImageButton btnOuts;


	/** 房间礼物相关 */
	private ListView mRoomListView;
	private RoomListAdapter mRoomListAdapter;
	//	private LinearLayout mGiftView;
	private LinearLayout mRoomView;

	private int sortId;//请求正序还是逆序
	String curRoomtitle;
	String curRoomOnline; 


	private RelativeLayout title_layout;
	private ImageView title_room_switch;

	private TextView room_people_numTextView;
	private String live_online="%s人在线";
	boolean livefirstIn;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		timerHandler = new TimerHandler();
		Intent in = getIntent();
		tag = in.getStringExtra("tag");
		lid = in.getIntExtra("lid", 0);
		if (lid <= 0)
			lid = findLid(tag);
		gid = in.getIntExtra("gid", 0);
		HupuLog.d("gid=" + gid);
		setContentView(R.layout.layout_football_game);
		TAG = tag.toUpperCase();
		playByPlayRoom = String.format(playByPlayRoom, TAG);// 哪个直播间
		initParameter();

		// 世界杯需要显示阵容按键
		btnLineup = (ImageButton) findViewById(R.id.btn_lineup);
		btnLineup.setVisibility(View.VISIBLE);
		setOnClickListener(R.id.img_team_left);
		setOnClickListener(R.id.img_team_right);
		showLineUp = true;

		if (gid > 0) {
			mDefaultTab = in.getStringExtra("tab");
			// 外部跳转
			mParams.put("gid", "" + gid);
			sendRequest(HuPuRes.REQ_METHOD_FOOTBALL_GAME_BY_GID, mParams);
		} else {
			// 从赛程页进入
			mGameEntity = (ScoreboardEntity) in.getSerializableExtra("data");
			mDefaultTab = mGameEntity.default_tab;
			gid = mGameEntity.i_gId;
			byMan = mGameEntity.i_live > 0;
			if (mGameEntity == null) {
				finish();
			}
			init();
		}

	}

	private void init() {

		isFollow = mGameEntity.bFollow == 1;

		mParams.put("gid", "" + gid);

		setJsonObj(BaseEntity.KEY_GAME_ID, gid);

		mLayoutScoreBar = findViewById(R.id.layout_score_bar);
		scoreLayout = findViewById(R.id.layout_score);
		txtTitle = (TextView) findViewById(R.id.txt_title);
		txtTitle.setText(R.string.title_live);
		TopAdImg = (ImageView) findViewById(R.id.top_ad_img);

		txtTeamLeft = (TextView) findViewById(R.id.txt_team_left);
		txtTeamRight = (TextView) findViewById(R.id.txt_team_right);
		txtProcess = (TextView) findViewById(R.id.txt_proccess);
		txtTime = (TextView) findViewById(R.id.txt_start_time);
		txtScore = (TextView) findViewById(R.id.txt_score);
		txtShootout1 = (TextView) findViewById(R.id.txt_shootout1);
		txtShootout2 = (TextView) findViewById(R.id.txt_shootout2);
		imgTeamLeft = (ImageView) findViewById(R.id.img_team_left);
		imgTeamRight = (ImageView) findViewById(R.id.img_team_right);

		setTeamName();
		layoutBottom = (LinearLayout) findViewById(R.id.layout_bottom);
		btnOuts = (ImageButton) findViewById(R.id.btn_outs);
		btnLive = (ImageButton) findViewById(R.id.btn_live);
		btnReport = (ImageButton) findViewById(R.id.btn_first);
		btnChat = (ImageButton) findViewById(R.id.btn_chat);
		btnQuiz = (ImageButton) findViewById(R.id.btn_quiz);

		imgRedPiont = (ImageView) findViewById(R.id.icon_red_point);
		imgRedPiont.setVisibility(View.GONE);
		rewardInfo = (RelativeLayout) findViewById(R.id.reward_info);

		btnFollow = (Button) findViewById(R.id.btn_follow);
		btnPlay = (Button) findViewById(R.id.btn_play);



		// init 房间部分
		initRoomAndGift();
		title_room_switch = (ImageView) findViewById(R.id.title_room_switch);
		room_people_numTextView = (TextView) findViewById(R.id.title_room_peple_online);
		setOnClickListener(R.id.layout_live_title);
		setOnClickListener(R.id.choose_room_dialog);




		curGameState = mGameEntity.code;
		setFollowBtn();
		setViewByStatus(true);
		// updateScoreView();
		if (mDefaultTab != null)
			switchTab();
		else {
			if (curGameState == ScoreboardEntity.STATUS_END) {
				treatClickEvent(R.id.btn_first);
			} else if (curGameState == ScoreboardEntity.STATUS_NOT_START) {
				treatClickEvent(R.id.btn_quiz);
			} else {
				if (byMan)
					treatClickEvent(R.id.btn_live);
				else
					treatClickEvent(R.id.btn_outs);

			}

		}



		setOnClickListener(R.id.btn_outs);
		setOnClickListener(R.id.btn_live);
		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.btn_first);
		setOnClickListener(R.id.btn_quiz);
		setOnClickListener(R.id.btn_sent);

		setOnClickListener(R.id.btn_chat);
		setOnClickListener(R.id.btn_follow);
		setOnClickListener(R.id.btn_play);
		setOnClickListener(R.id.btn_quiz);
		setOnClickListener(R.id.btn_sent);
		setOnClickListener(R.id.btn_lineup);
		setOnClickListener(R.id.gold_num);

		updateProccess(mGameEntity);
	}

	/** 根据默认标签跳转到不同的页面 */
	private void switchTab() {

		if (mDefaultTab.equals(TAB_GUESS)) {
			treatClickEvent(R.id.btn_quiz);
		} else if (mDefaultTab.equals(TAB_BOXSCORE))
			treatClickEvent(R.id.btn_outs);
		else if (mDefaultTab.equals(TAB_CHAT)) {
			treatClickEvent(R.id.btn_chat);
		} else if (mDefaultTab.equals(TAB_REPORT))
			treatClickEvent(R.id.btn_first);
		else if (mDefaultTab.endsWith(TAB_LIVE))
			treatClickEvent(R.id.btn_live);
		else {
			if (byMan)
				treatClickEvent(R.id.btn_live);
			else
				treatClickEvent(R.id.btn_outs);// 直播
		}

	}

	private String movieUrl;

	private boolean bottombarVisible = false;

	private void showBottomBar() {
		if (layoutBottom != null) {
			layoutBottom.setVisibility(View.VISIBLE);
			bottombarVisible = true;
		}
	}

	@Override
	public void treatClickEvent(int id) {

		super.treatClickEvent(id);

		switch (id) {
		case R.id.btn_outs:// 赛况
			if (curFragmentIndex != INDEX_STATISTIC) {
				setBackgound(INDEX_STATISTIC);
				replaceContent(INDEX_STATISTIC);
			}
			break;
		case R.id.btn_live:
			if (curFragmentIndex == INDEX_LIVE_BY_MAN
			|| curFragmentIndex == INDEX_LIVE)
				return;
			if (curFragmentIndex != INDEX_LIVE && curFragmentIndex != INDEX_LIVE_BY_MAN) {
				findViewById(R.id.layout_gift).setVisibility(View.VISIBLE);
			}
			if (byMan && curFragmentIndex != INDEX_LIVE_BY_MAN) {
				setBackgound(INDEX_LIVE_BY_MAN);
				replaceContent(INDEX_LIVE_BY_MAN);
				return;
			}
			if (curFragmentIndex != INDEX_LIVE && curFragmentIndex != INDEX_LIVE_BY_MAN) {
				if (curGameState == STATUS_NOT_START) {
					setBackgound(INDEX_LIVE_BY_MAN);
					replaceContent(INDEX_LIVE_BY_MAN);
				} else {

					//不需要事件直播   没有人工直播 就是 webview
					setBackgound(INDEX_LIVE_BY_MAN);
					replaceContent(INDEX_LIVE_BY_MAN);
					//					setBackgound(INDEX_LIVE);
					//					replaceContent(INDEX_LIVE);
				}

				return;
			}
			break;
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_first:
			if (curFragmentIndex != INDEX_REPORT) {
				setBackgound(INDEX_REPORT);
				replaceContent(INDEX_REPORT);
			}
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
			HupuLog.e("footballGame-401", "send_chat_roomid="+roomid);
			in.putExtra("roomid", roomid);
			startActivityForResult(in, REQ_SEND_MSG);
			toChat = true;
			break;
		case R.id.btn_quiz:

			setBackgound(INDEX_QUIZ);
			replaceContent(INDEX_QUIZ);
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
		case R.id.top_ad_img:
			Intent webIntent = new Intent(this, AdWebviewActivity.class);
			webIntent.putExtra("ad_url", adUrl);
			// webIntent.putExtra("source", adUrl);
			startActivity(webIntent);
			break;
		case R.id.btn_lineup:
			setBackgound(INDEX_LINEUP);
			replaceContent(INDEX_LINEUP);
			showLineup();
			break;
		case R.id.img_team_right:
			if (mGameEntity.i_away_tid < 990000)
				toSoccerTeamAct(mGameEntity.i_away_tid);
			break;
		case R.id.img_team_left:
			if (mGameEntity.i_home_tid < 990000)
				toSoccerTeamAct(mGameEntity.i_home_tid);
			break;
		case R.id.layout_live_title:
			if(curFragmentIndex == INDEX_LIVE ||curFragmentIndex == INDEX_LIVE_BY_MAN){
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
			if(curFragmentIndex == INDEX_LIVE || curFragmentIndex == INDEX_LIVE_BY_MAN){
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

	private void toSoccerTeamAct(int tid) {
		Intent in = new Intent(this, SoccerTeamActivity.class);
		in.putExtra("tid", tid);
		in.putExtra("tag", tag);
		startActivity(in);
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
		if (dialogId == DIALOG_NOTIFY) {
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
		}
	}

	/** 设置关注比赛的UI状态 */
	private void setFollowBtn() {
		if (curGameState == ScoreboardEntity.STATUS_END
				|| curGameState == ScoreboardEntity.STATUS_CANCEL) {
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

	/** 当前所在页索引 */
	private int curFragmentIndex = -1;

	private boolean bHasReportData;
	/** 是否开启人工直播 */
	boolean byMan;
	boolean getLiveEndData;
	private int lastLiveID;

	private void replaceContent(int index) {
		if (curFragmentIndex == index)
			return;
		Fragment fragment = null;

		curFragmentIndex = index;

		switch (index) {
		case INDEX_REPORT:
			if (mFragmentReport == null)
				mFragmentReport = new ReportFragment();
			if (!bHasReportData)
				req();
			fragment = mFragmentReport;
			txtTitle.setText("战报");

			setScreenLight(false);
			break;
		case INDEX_LIVE_BY_MAN:
			// 如果是人工直播，就显示直播页面
			if (mFragmentLive == null) {
				mFragmentLive = new LiveFragment(
						0,
						0,
						(curGameState != STATUS_NOT_START
						&& curGameState != STATUS_CANCEL && curGameState != STATUS_EXTEND));
			}
			if (curGameState == STATUS_END || curGameState == STATUS_START)
				mFragmentLive.isStart(true);
			fragment = mFragmentLive;
			mFragmentLive.entry();

			if (!bHasLiveData || (bEnd && !getLiveEndData)) {
				// 初次进入该页面需要获取全部的实时播报信息
				if (curGameState == STATUS_START)// 进行中
				{
					mParams.put("sort", "desc");
					sortId = HuPuRes.REQ_METHOD_GET_LIVE_DESC;
					reqHttp(HuPuRes.REQ_METHOD_GET_LIVE_DESC);
				} else {
					mParams.put("sort", "asc");
					sortId = HuPuRes.REQ_METHOD_GET_LIVE_ASC;
					reqHttp(HuPuRes.REQ_METHOD_GET_LIVE_ASC);
				}
			} else if (curGameState != STATUS_END) {
				mFragmentLive.setPreview(roomPreview);
				if (roomPreview != null && !"".equals(roomPreview)) {
					// 设置前瞻
					mFragmentLive.setURL(roomPreview);
				}
				bJoinRoom = true;
				// 加入前设置最后一条消息id
				setJsonObj(BaseEntity.KEY_PID, lastLiveID);
				setJsonObj("roomid", roomid);
				jsonRoom.remove("type");
				jsonRoom.remove("direc");
				jsonRoom.remove("num");
				JSONArray arr = mFragmentLive.getQids();
				if (arr != null)
					setJsonObj("qids", mFragmentLive.getQids());
				joinRoom(playByPlayRoom);
			}
			//			txtTitle.setText("直播");
			setScreenLight(true);
			break;
		case INDEX_LIVE:
			// 事件直播
			if (mFragmentEvents == null)
				mFragmentEvents = new FootballLiveFragment();
			fragment = mFragmentEvents;
			mFragmentEvents.entry();

			//			txtTitle.setText("直播");
			setScreenLight(false);
			break;

		case INDEX_STATISTIC:
			if (mFragmentPlayByPlay == null)
				mFragmentPlayByPlay = new SoccerPlayByPlayFragment();
			fragment = mFragmentPlayByPlay;
			mFragmentPlayByPlay.entry();
			txtTitle.setText("赛况");
			setScreenLight(false);
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
			}

			fragment = mFragmentQuizList;
			joinRoom(tag.toUpperCase() + HuPuRes.ROOM_CASINO);
			txtTitle.setText(R.string.quiz_list_tiele);
			break;

		case INDEX_LINEUP:
			if (mFootballLineupFragment == null) {
				mFootballLineupFragment = new FootballLineupFragment();
			}

			fragment = mFootballLineupFragment;
			txtTitle.setText("阵容");
			break;

		}

		if (index != INDEX_CHAT && index != INDEX_LIVE_BY_MAN
				&& index != INDEX_QUIZ) {
			// ---
			if (bJoinRoom) {
				bJoinRoom = false;
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

		initGift(null,index,false);
		switchRoomStatus(index,livefirstIn);

		int color = getResources().getColor(R.color.transform);
		// 点击前
		switch (curFragmentIndex) {
		case INDEX_REPORT:
			// mLayoutScoreBar.setVisibility(View.VISIBLE);
			btnReport.setBackgroundColor(color);
			btnReport.setImageResource(R.drawable.btn_report);
			break;
		case INDEX_LIVE:
		case INDEX_LIVE_BY_MAN:

			btnLive.setBackgroundColor(color);
			btnLive.setImageResource(R.drawable.btn_live_up);

			findViewById(R.id.layout_title_btn).setVisibility(View.GONE);
			break;
		case INDEX_STATISTIC:

			btnOuts.setBackgroundColor(color);
			btnOuts.setImageResource(R.drawable.btn_outs_up);

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
		case INDEX_LINEUP:
			btnLineup.setBackgroundColor(color);
			btnLineup.setImageResource(R.drawable.btn_lineup_up);
			break;
		}

		switch (index) {// 点击后
		case INDEX_REPORT:
			mLayoutScoreBar.setVisibility(View.VISIBLE);
			btnReport.setBackgroundResource(R.drawable.bg_bottom_hover);
			btnReport.setImageResource(R.drawable.btn_report_hover);
			break;
		case INDEX_LIVE:
		case INDEX_LIVE_BY_MAN:

			btnLive.setBackgroundResource(R.drawable.bg_bottom_hover);
			btnLive.setImageResource(R.drawable.btn_live_down);

			findViewById(R.id.btn_sent).setVisibility(View.GONE);
			mLayoutScoreBar.setVisibility(View.VISIBLE);
			if (curGameState != ScoreboardEntity.STATUS_END
					&& curGameState != ScoreboardEntity.STATUS_CANCEL)
				findViewById(R.id.layout_title_btn).setVisibility(View.VISIBLE);
			break;
		case INDEX_STATISTIC:

			btnOuts.setBackgroundResource(R.drawable.bg_bottom_hover);
			btnOuts.setImageResource(R.drawable.btn_outs_down);

			mLayoutScoreBar.setVisibility(View.VISIBLE);
			if (curGameState != ScoreboardEntity.STATUS_END
					&& curGameState != ScoreboardEntity.STATUS_CANCEL)
				findViewById(R.id.layout_title_btn).setVisibility(View.VISIBLE);
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

		case INDEX_LINEUP:
			mLayoutScoreBar.setVisibility(View.GONE);
			btnLineup.setBackgroundResource(R.drawable.bg_bottom_hover);
			btnLineup.setImageResource(R.drawable.btn_lineup_down);
			break;

		}
	}

	/** 请求Http数据 */
	private void reqHttp(int repType) {
		mParams.put(BaseEntity.KEY_GAME_ID, "" + gid);
		sendRequest(repType, tag, mParams, new HupuHttpHandler(this), false);
	}

	int[] bets;

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		if (o == null)
			return;
		showBottomBar();
		switch (methodId) {
		case HuPuRes.REQ_METHOD_FOOTBALL_GAME_BY_GID:
			mGameEntity = (ScoreboardEntity) o;
			byMan = mGameEntity.i_live > 0;
			init();
			break;
		case HuPuRes.REQ_METHOD_GET_FOOTBALL_OUTS:
			SoccerOutsReq d = (SoccerOutsReq) o;
			dif = 0;
			updateProccess(d.scoreBoard);
			mFragmentPlayByPlay.setData(d,tag);
			break;
		case HuPuRes.REQ_METHOD_GET_FOOTBALL_EVENTS:
			SoccerEventsResp live = (SoccerEventsResp) o;
			movieUrl = live.tvLink;
			if (!"".equals(movieUrl))
				btnPlay.setEnabled(true);

			if (live.adImg != null && !"".equals(live.adImg)) {
				TopAdImg.setVisibility(View.VISIBLE);
				UrlImageViewHelper.setUrlDrawable(TopAdImg, live.adImg);
				setOnClickListener(R.id.top_ad_img);
				adUrl = live.adUrl;
			} else {
				TopAdImg.setVisibility(View.GONE);
			}
			dif = 0;
			updateProccess(live.scoreBoard);
			mFragmentEvents.setData(live,tag);
			break;
		case HuPuRes.REQ_METHOD_GET_LIVE_DESC:
		case HuPuRes.REQ_METHOD_GET_LIVE_ASC:
			SimpleLiveResp data = (SimpleLiveResp) o;
			movieUrl = data.tvLink;
			//直播间和礼物
			initGift(data,INDEX_LIVE_BY_MAN,true);
			boolean isShow  = false;
			if (data.roomList != null) {
				isShow = data.roomList!=null&&data.roomList.size()>1?true:false;

				if (data.is_enter == 0&&isShow) {
					livefirstIn = true;
				}else{
					livefirstIn = false;
				}
				//重置roomid
				roomid = data.default_room_id;
				switchRoomStatus(INDEX_LIVE_BY_MAN,livefirstIn);
				setRoomList(data.roomList,data.default_room_id);
				for (RoomEntity entity:data.roomList) {
					if (entity.id == data.default_room_id) {
						if(data.is_enter == 0){

							if(data.roomList!=null&&data.roomList.size()>1)
							{
								curRoomtitle = getString(R.string.title_select_room);
								setRoomTitle(curRoomtitle);
								mRoomListAdapter.setDefaultId(-1);
								mRoomListAdapter.notifyDataSetChanged();
								showRoomAction();
							}
							else
							{
								curRoomtitle = entity.name;
								curRoomOnline = String.format(live_online, entity.count);
							}
							setRoomOnlineNum("0",false);
						}else{
							setRoomTitle(entity.name);
							setRoomOnlineNum(String.format(live_online, entity.count),true);

						}

					}
				}

			}
			//直播和礼物end



			// topbar 广告图片展示
			if (data.adImg != null && !"".equals(data.adImg)) {
				HupuLog.e("papa", "adImg===" + data.adImg);
				TopAdImg.setVisibility(View.VISIBLE);
				UrlImageViewHelper.setUrlDrawable(TopAdImg, data.adImg);
				setOnClickListener(R.id.top_ad_img);
				adUrl = data.adUrl;
			} else {
				TopAdImg.setVisibility(View.GONE);
			}

			if (!"".equals(movieUrl))
				btnPlay.setEnabled(true);
			mFragmentLive.addData(true);

			setJsonObj("roomid", roomid);
			roomPreview = data.preview;
			mFragmentLive.setPreview(roomPreview);
			if (data.scoreBoard != null && data.scoreBoard.live == 0) {
				if (data.preview != null) {
					// 设置前瞻
					mFragmentLive.setURLForCBA(data.preview);
				}
				return;
			} else {
				if (curGameState != STATUS_NOT_START
						&& curGameState != STATUS_CANCEL
						&& curGameState != STATUS_EXTEND)
					mFragmentLive.isStart(true);
				else if (data.preview != null) {
					// 设置前瞻
					mFragmentLive.setURLForCBA(data.preview);
				}
			}

			if (data.casinoInit != null) {
				bets = data.casinoInit.bets;
				mFragmentLive.setBets(data.casinoInit.bets);
			}

			if (data.dataList == null) {
				// 没有数据
				if (curGameState == STATUS_NOT_START) {
					// showToast("比赛未开始");
					bJoinRoom = true;
					jsonRoom.remove("type");
					jsonRoom.remove("direc");
					jsonRoom.remove("num");
					joinRoom(playByPlayRoom);
				} else {
					showToast("暂无直播数据");
				}
				mFragmentLive.addData(true);
				//return;
			}
			mFragmentLive.setData(data.dataList);

			bHasLiveData = true;
			lastLiveID = data.i_pId;
			// Log.d("http back pid", ""+data.i_pId);

			// 如果是比赛
			if (methodId == HuPuRes.REQ_METHOD_GET_LIVE_DESC)
				setJsonObj(BaseEntity.KEY_PID, data.i_pId);
			if (curGameState != STATUS_CANCEL) {// 如果比赛未结束需要加入房间
				bJoinRoom = true;
				jsonRoom.remove("type");
				jsonRoom.remove("direc");
				jsonRoom.remove("num");
				joinRoom(playByPlayRoom);
				// Log.d("join", "ROOM_CBA_PLAYBYPLAY");
			} else {
				getLiveEndData = true;
			}
			if (data.scoreBoard != null) {
				setSore(data.scoreBoard.i_scoreHome,
						data.scoreBoard.i_scoreAway);
				// updateProccess(data.score.str_process);
				if (data.scoreBoard.period > -1) {
					period = mGameEntity.period = (byte) data.scoreBoard.period;
				}
				if (data.scoreBoard.str_process != null) {
					updateTime(period, data.scoreBoard.str_process);
				}
			}


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
					setRoomList(info.roomList, roomid);
					showRoomAction();
				}

			}
			break;
		case HuPuRes.REQ_METHOD_GET_GAME_REPORT:
			Recap re = (Recap) o;
			mFragmentReport.setData(re);
			break;
		case HuPuRes.REQ_METHOD_EN_SENT_CHAT:
			SendMsgResp smrsp = (SendMsgResp) o;
			// HupuLog.d("SendMsgResp="+smrsp.pid);
			if (smrsp.err != null) {
				// Log.d("SendMsgResp", data.err);
				showToast(smrsp.err);
			} else {
				if (smrsp.pid == 0) {
					reqChatData(lastChatID);
				} else {
					mFragmentChat.setLastId(smrsp.pid);
					lastChatID = smrsp.pid;
					setJsonObj("pid", lastChatID);
					setJsonObj("direc", "next");
					if (mToken != null)
						setJsonObj("tk", "mToken");
					joinRoom(HuPuRes.ROOM_NBA_CHAT);
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
				Log.d("follow", "methodId=" + methodId);
				if (methodId == HuPuRes.REQ_METHOD_FOLLOW_GAME)
					showToast(String.format(SUCCESS_NOTIFY,
							mGameEntity.str_home_name,
							mGameEntity.str_away_name));
				else if (methodId == HuPuRes.REQ_METHOD_FOLLOW_GAME_CANCEL)
					showToast(CANCEL_NOTIFY);
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
		case HuPuRes.REQ_METHOD_GET_ROSTER:
			if (curFragmentIndex == INDEX_LINEUP) {
				mFootballLineupFragment.onReqResponse(o, methodId, methodId);
			}
		}

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
				// showCustomDialog(DIALOG_SHOW_GET_DOLE, 0,
				// R.string.get_now_info, ONE_BUTTON,
				// R.string.title_confirm, 0);
				showToast(getResources().getString(R.string.get_now_info));
				mFragmentLive.updateCommit(msg1, msg2);
			} else if (entity.result == -2 || entity.result == -3) {
				// showToast("金币余额不足");
				// showNoMoney(0, 0);
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

	// 更新直播流中竞猜对话框余额
	@Override
	public void updateMoney(int betCoin, int balance) {
		if (mFragmentLive != null)
			mFragmentLive.updateMoney(betCoin, money);
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

	/** 设置球队比分 需要翻牌，这个逻辑就需要先判断比分的变换了。 */
	private void setSore(int homeScore, int awayScore) {
		if (homeScore > -1 && awayScore > -1)
			txtScore.setText(homeScore + " - " + awayScore);
	}

	/** 更新比赛过程的标题信息 */
	private void updateProccess(ScoreboardEntity entity) {

		if (entity != null) {
			mGameEntity = entity;
			setSore(mGameEntity.i_home_score, mGameEntity.i_away_score);
		}

		if (curGameState != entity.code) {
			curGameState = entity.code;
			setViewByStatus(false);

		}
		if (!startTime && entity.code == ScoreboardEntity.STATUS_START) {
			startTime = true;
			startTimer();
		}
		if (startTime && entity.code == ScoreboardEntity.STATUS_END)
			stopTimer();
		showTime(entity, txtProcess);

	}

	/**
	 * 根据不同的比赛状态调整显示底部菜单
	 * 
	 **/
	void setBottomBar() {
		int color = getResources().getColor(R.color.transform);
		HupuLog.d("setBottomBar", "curGameState=" + curGameState);
		switch (curGameState) {
		case ScoreboardEntity.STATUS_NOT_START:
		case ScoreboardEntity.STATUS_EXTEND:
		case ScoreboardEntity.STATUS_CANCEL:
			// 显示三个tab 前瞻，赛况，竞猜，热线
			btnReport.setVisibility(View.GONE);
			btnOuts.setVisibility(View.GONE);
			break;
		case ScoreboardEntity.STATUS_START:
			// 五个tab 直播 赛况 阵容 竞猜 热线
			if (!byMan)
				//btnLive.setVisibility(View.GONE);// 隐藏直播
				btnReport.setVisibility(View.GONE);
			btnOuts.setVisibility(View.VISIBLE);
			break;
		case ScoreboardEntity.STATUS_END:
			// 五个tab 战报 赛况 阵容 竞猜 热线，直播放到战报里去了
			btnReport.setVisibility(View.VISIBLE);
			btnLive.setVisibility(View.GONE);// 隐藏直播
			btnOuts.setVisibility(View.VISIBLE);

			if (curFragmentIndex == INDEX_LIVE
					|| curFragmentIndex == INDEX_LIVE_BY_MAN) {
				// 直播高亮
			} else if (curFragmentIndex == INDEX_STATISTIC) {
				// 统计高亮
			}
			break;
		}

	}

	void updateProccess() {
		switch (curGameState) {
		case ScoreboardEntity.STATUS_START:
			if (mGameEntity.period == 8) {
				// 显示点球比分
				showShootOut(txtShootout1, txtShootout2,
						mGameEntity.home_out_goals, mGameEntity.away_out_goals);
			} else {
				HupuBaseActivity.hideShootOut(txtShootout1, txtShootout2);
			}
			break;
		case ScoreboardEntity.STATUS_END:
			if (mGameEntity.home_out_goals > 0
					|| mGameEntity.away_out_goals > 0) {
				// 结束后没有period字段，只能通过比分来判断。
				showShootOut(txtShootout1, txtShootout2,
						mGameEntity.home_out_goals, mGameEntity.away_out_goals);
				txtProcess.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						R.drawable.ic_penalty, 0);
			} else if (mGameEntity.is_extra > 0) {
				txtProcess.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						R.drawable.ic_overtime, 0);
			} else
				HupuBaseActivity.hideShootOut(txtShootout1, txtShootout2);
			break;
		}
	}

	/**
	 * 根据不同的比赛状态调整显示的view
	 * 
	 **/
	void setViewByStatus(boolean init) {

		setBottomBar();
		switch (curGameState) {
		case ScoreboardEntity.STATUS_START:
			txtTime.setVisibility(View.GONE);
			scoreLayout.setVisibility(View.VISIBLE);
			if (mGameEntity.period == 8) {
				// 显示点球比分
				showShootOut(txtShootout1, txtShootout2,
						mGameEntity.home_out_goals, mGameEntity.away_out_goals);
			} else {
				HupuBaseActivity.hideShootOut(txtShootout1, txtShootout2);
			}
			if (!init) {
				mLayoutScoreBar.setVisibility(View.VISIBLE);
				txtScore.setVisibility(View.VISIBLE);
				setSore(mGameEntity.i_home_score, mGameEntity.i_away_score);
				setScreenLight(true);
			}
			break;
		case ScoreboardEntity.STATUS_END:
			txtTime.setVisibility(View.GONE);
			scoreLayout.setVisibility(View.VISIBLE);

			HupuLog.d("papa", "状态：=" + mGameEntity.code);
			txtProcess.setText(mGameEntity.str_desc);

			if (mGameEntity.home_out_goals > 0
					|| mGameEntity.away_out_goals > 0) {
				// 结束后没有period字段，只能通过比分来判断。
				showShootOut(txtShootout1, txtShootout2,
						mGameEntity.home_out_goals, mGameEntity.away_out_goals);
				txtProcess.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						R.drawable.ic_penalty, 0);
			} else if (mGameEntity.is_extra > 0) {
				txtProcess.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						R.drawable.ic_overtime, 0);
			} else
				HupuBaseActivity.hideShootOut(txtShootout1, txtShootout2);
			// 比赛结束，停止事件刷新
			stopTimer();
			break;
		case ScoreboardEntity.STATUS_NOT_START:
		case ScoreboardEntity.STATUS_EXTEND:
		case ScoreboardEntity.STATUS_CANCEL:
			scoreLayout.setVisibility(View.GONE);
			txtTime.setVisibility(View.VISIBLE);
			if (curGameState == ScoreboardEntity.STATUS_NOT_START) {
				txtTime.setText("未开始");
			} else {
				txtProcess.setText(mGameEntity.str_desc);
			}
			break;
		}
	}

	/** 请求热线数据，每页20条 */
	public void reqChatData(int id) {

		UMENG_MAP.clear();
		setJsonObj("type", tag);
		setJsonObj("num", 20);
		if (id > 0) {
			setJsonObj("pid", id);
			setJsonObj("direc", "prev");

		} else {
			// 重新获取最新的聊天记录，不传偏移量
			setJsonObj("pid", "");
			setJsonObj("direc", "next");
		}
		joinRoom(HuPuRes.ROOM_NBA_CHAT);
	}

	public void req() {

		if (curFragmentIndex == INDEX_LIVE)
			sendRequest(HuPuRes.REQ_METHOD_GET_FOOTBALL_EVENTS, tag, mParams,
					new HupuHttpHandler(this), false);
		else if (curFragmentIndex == INDEX_STATISTIC) {
			initParameter();
			mParams.put("lid", "" + lid);
			mParams.put("gid", "" + gid);
			mParams.put("token", mToken);
			sendRequest(HuPuRes.REQ_METHOD_GET_FOOTBALL_OUTS, tag, mParams,
					new HupuHttpHandler(this), false);
		} else if (curFragmentIndex == INDEX_REPORT)
			sendRequest(HuPuRes.REQ_METHOD_GET_GAME_REPORT, tag, mParams,
					new HupuHttpHandler(this), false);
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
	public void onErrResponse(Throwable error, String content,
			boolean isBackGroundThread) {

		if (content != null) {
			showToast(content);
		}
	}

	@Override
	public void onSocketResp(JSONObject obj) {
		HupuLog.e("footballGamesActivity--socket", "---socket="+obj.toString());
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
						mQid = notify.qid;

						if (curFragmentIndex != INDEX_QUIZ) {
							if (mToken != null) {
								imgRedPiont.setVisibility(View.VISIBLE);
							}
						} else {
							getQuizList();
						}
						if (mToken != null) {
							startAnim(notify.coin);
						}
					}
					return;
				}

				int gd = obj.optInt(BaseEntity.KEY_GAME_ID, -1);
				if (gd != gid)
					return;// 如果不是这场比赛的ID，忽略该数据

				if (HuPuRes.ROOM_NBA_CHAT.equals(room)) {
					ChatResp data = new ChatResp();
					data.paser(obj);
					mFragmentChat.stopLoad();
					if (data.mList != null) {
						if (data.direc.equals("next") && data.pid > 0
								&& data.pid <= lastChatID)
							return;
					}
					mFragmentChat.setData(data,roomid);
					if (curFragmentIndex == INDEX_CHAT && data.online != null)
						txtTitle.setText("热线(" + data.online + "人)");

					updateData(data.score);
				} else if ((playByPlayRoom).equals(room)) {
					if (curGameState == STATUS_END) {
						bEnd = true;
						getLiveEndData = true;
					}
					// 直播
					SoccerLiveResp data = new SoccerLiveResp();
					data.paser(obj);

					if (curFragmentIndex == INDEX_LIVE_BY_MAN
							&& data.online != null)
					{
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
						setRoomOnlineNum(String.format(this.live_online, data.online),!isShowRoomlist);
					}
					mFragmentLive.stopLoad();
					if (data.bHasData) {
						if (data.i_pId > lastLiveID && data.i_pId > -1)
							mFragmentLive.updateData(data);
					} else if (data.casinoList != null) {
						mFragmentLive.updateData(data);
					}
					if (data.i_pId > -1 && data.i_pId > lastLiveID) {
						lastLiveID = data.i_pId;
						// 更新消息id
						setJsonObj(BaseEntity.KEY_PID, data.i_pId);
					}
					updateData(data.scoreBoard);

					if(data.giftList!=null && data.giftList.size()>0){
						pushUpdataGift(data.giftList);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		mApp.setNetState(HuPuApp.STATE_ON_LINE);
		setNetTitle();
	}

	/** 更新状态 */
	private void updateData(SimpleScoreboard score) {

		if (score != null) {
			if (score.id > 0 && score.id != curGameState) {
				curGameState = mGameEntity.code = (byte) score.id;
				mGameEntity.str_desc = score.desc;
				// setViewByStatus(false);
				updateProccess();
			}

			setSore(score.i_scoreHome, score.i_scoreAway);
			mGameEntity.i_away_score = score.i_scoreAway;
			mGameEntity.i_home_score = score.i_scoreAway;
			mGameEntity.away_out_goals = score.away_out_goals;
			mGameEntity.home_out_goals = score.home_out_goals;

			if (score.period > -1) {
				if (mGameEntity.period != score.period)
					dif = 0;
				period = mGameEntity.period = (byte) score.period;
			}
			if (period == 8) {
				showShootOut(txtShootout1, txtShootout2,
						mGameEntity.home_out_goals, mGameEntity.away_out_goals);
			}
			if (score.str_process != null) {
				updateTime(period, score.str_process);
			}
		}
	}

	/**
	 * @param period
	 *            中场，下半场等
	 * */
	private void updateTime(int period, String pro) {
		if (mGameEntity.code == ScoreboardEntity.STATUS_START) {
			HupuLog.e("papa", "比赛状态===" + period);
			String txt = null;
			// 已经开始
			switch (period) {
			case 3:// 中场休息：
				txt = "中场休息";
				break;
			case 4:// 下半场结束
				txt = "下半场结束";
				break;
			case 7://
				txt = "加时赛结束";
				break;
			case 9:
				txt = "加时中场休息";
				break;
			case 8:
				txt = "点球大战";
				break;
			}
			if (txt != null) {
				txtProcess.setText(txt);
				mGameEntity.str_desc = txt;
				dif = 0;
			} else {
				try {
					int p = Integer.parseInt(pro);
					if (p > lastTime + dif) {
						mGameEntity.process = p;
						lastTime = p;
						dif = 0;
						updateTime();
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}

	}

	int dif;

	Handler timeHandler;

	private void showTime(ScoreboardEntity en, TextView tv) {
		mGameEntity = en;
		showTime(en, tv, dif);

	}

	/** 时间计数 */
	private void updateTime() {
		if (mGameEntity != null) {
			dif++;
			showTime(mGameEntity, txtProcess);
		}
	}

	private void showLineup() {

	}

	/** 比赛是否处于开赛状态 */
	boolean startTime;
	boolean isResume;

	boolean bJoinRoom;

	@Override
	protected void onResume() {
		super.onResume();
		if (startTime)
			startTimer();
		if (bJoinRoom) {
			// 如果需要加入房间
			if (DeviceInfo.isNetWorkEnable(this)) {
				// 如果是非聊天
				if (curFragmentIndex == INDEX_LIVE_BY_MAN) {
					setJsonObj(BaseEntity.KEY_PID, lastLiveID);
					// 如果是有竞猜的比赛，需要加入所有为关闭的竞猜id
					setJsonObj("qids", mFragmentLive.getQids());
					joinRoom();
				} else if (curFragmentIndex == INDEX_CHAT) {
					// reqChatData(lastChatID);
					// if (!mApp.isSocketConn()) {
					// Log.d("onResume", "join statistic");
					// Log.d("onResume", "join statistic="+toChat);
					if (lastChatID > 0)
						setJsonObj(BaseEntity.KEY_PID, lastChatID);
					if (!toChat) {
						setJsonObj("direc", "next");
						joinRoom();
					}
					toChat = false;
				}

			} else {
				mApp.setNetState(HuPuApp.STATE_NO_NET);
				reconnect(false);
			}
		}
		if (curFragmentIndex == INDEX_QUIZ) {
			if (isResume) {
				getQuizList();
			} else {
				isResume = true;
			}
			joinRoom();
		}

	}

	/** 直播过程中请求刷，就是重新加入room */
	public void reqFresh() {
		if (bJoinRoom && getRoom() != null)
			joinRoom();
		//
		mFragmentLive.stopLoad();
	}

	@Override
	protected void onStop() {
		super.onStop();
		stopTimer();
		if (bJoinRoom)
			reqLeaveRoom();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopTimer();
		bJoinRoom = false;

//		quitLive();
		roomid = -1;
	}

	Timer timer;
	TimerHandler timerHandler;
	MyTimerTask task;
	boolean bStartTimer;

	private void stopTimer() {
		// Stop timer
		if (null != task) {
			task.cancel();
			task = null;
		}
		if (null != timer) {
			timer.cancel(); // Cancel timer
			timer.purge();
			timer = null;
		}
		bStartTimer = false;
	}

	private void startTimer() {
		if (bStartTimer)
			return;
		if (timer == null) {
			timer = new Timer();
			task = new MyTimerTask();
			timer.schedule(task, 0, 1000);
		}
		bStartTimer = true;

	}

	private class MyTimerTask extends TimerTask {

		public void run() {
			timerHandler.sendEmptyMessage(0);
		}
	}

	public class TimerHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			updateTime();
		}
	}

	// 竞猜部分
	@Override
	public void onLoginSuccess() {
		// TODO Auto-generated method stub
		super.onLoginSuccess();
		if (curFragmentIndex == INDEX_QUIZ) {
			getQuizList();
			joinRoom();
		}
	}

	public void getQuizList() {
		HupuLog.e("papa", "gid===" + gid + "---------lid===" + lid);
		initParameter();
		mParams.put("lid", "" + lid);
		mParams.put("gid", "" + gid);
		mParams.put("token", mToken);
		sendRequest(HuPuRes.REQ_METHOD_QUIZ_LIST, mParams, new HupuHttpHandler(
				this), false);
	}

	public void getLineupList() {
		initParameter();
		// mParams.put("lid", "" + lid);
		mParams.put("gid", "" + gid);
		sendRequest(HuPuRes.REQ_METHOD_GET_ROSTER, tag, mParams,
				new HupuHttpHandler(this), false);
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

	public void toEventsActivity(HupuScheme scheme) {
		if (scheme.id <= 0)
			return;
		Intent in = new Intent(this, SoccerEventsActivity.class);
		in.putExtra("gid", scheme.id);
		in.putExtra("tag", scheme.game);
		in.putExtra("data", mGameEntity);
		in.putExtra("lid", lid);
		startActivity(in);
	}

	public String getTag() {
		return tag;
	}

	public int getLid() {
		return lid;
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
		if(curRoomtitle == null){
			curRoomtitle = this.getString(R.string.title_select_room);
		}
		this.curRoomtitle = title;
		txtTitle.setText(curRoomtitle);
	}
	private void setRoomOnlineNum(String num,boolean isShowRoomlist){
		if(isShowRoomlist){
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
		if(curtab == INDEX_LIVE || curtab == INDEX_LIVE_BY_MAN)
		{
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
				new HupuHttpHandler(FootballGameActivity.this), false);
	}
	class EpandCallBack implements EpandAnimationCallBack
	{
		@Override
		public void callBack(int type) {
			switchRoomStatus(INDEX_LIVE,livefirstIn);
		}
	}
}
