package com.hupu.games.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
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
import com.hupu.games.adapter.GameDataListLandAdapter;
import com.hupu.games.adapter.RoomListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.BaseEntity;
import com.hupu.games.data.BaseLiveResp;
import com.hupu.games.data.ChatResp;
import com.hupu.games.data.FollowResp;
import com.hupu.games.data.PlayerEntity;
import com.hupu.games.data.PushNotify;
import com.hupu.games.data.Recap;
import com.hupu.games.data.SendMsgResp;
import com.hupu.games.data.game.basketball.BasketballGameEntity;
import com.hupu.games.data.game.basketball.NBALiveResp;
import com.hupu.games.data.game.quiz.QuizCommitResp;
import com.hupu.games.data.personal.box.BoxScoreResp;
import com.hupu.games.data.room.GiftEntity;
import com.hupu.games.data.room.GiftReqDataEntity;
import com.hupu.games.data.room.GiftRespResultEntity;
import com.hupu.games.data.room.RoomEntity;
import com.hupu.games.data.room.RoomListEntity;
import com.hupu.games.dialog.EpandDownAnimation;
import com.hupu.games.dialog.EpandDownAnimation.EpandAnimationCallBack;
import com.hupu.games.dialog.GiftTipsDialog;
import com.hupu.games.dialog.TipsDialog;
import com.hupu.games.fragment.ChatFragment;
import com.hupu.games.fragment.LiveFragment;
import com.hupu.games.fragment.QuizListFragment;
import com.hupu.games.fragment.ReportFragment;
import com.hupu.games.fragment.StatisticFragment;
import com.hupu.games.hupudollor.activity.HupuDollorOrderActivity;
import com.hupu.games.hupudollor.data.HupuDollorBalanceReq;
import com.hupu.games.livegift.animation.AnimationTool;
import com.hupu.games.pay.BasePayActivity;
import com.hupu.games.pay.HupuOrderActivity;
import com.hupu.http.HupuHttpHandler;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.pyj.common.DeviceInfo;
import com.pyj.common.MyUtility;
import com.pyj.http.RequestParams;

/**
 * 显示直播，统计数据页面
 * */
public class NBAGameActivity extends BaseGameLiftActivity {

	private final static int GAME_STATE_CANCEL = 4;
	private final static int GAME_STATE_ONGOING = 2;
	private final static int GAME_STATE_END = 1;
	private final static int GAME_STATE_NOT_START = 3;
	/** 比赛的entity */
	private BasketballGameEntity mEntityGame;

	/** 包含比分，球队信息，开赛时间的layout */
	private View mLayoutScoreBar;

	// private ImageButton btnRating;

	/** 关注比赛按钮 */
	private Button btnFollow;
	/** 视频播放按钮 */
	private Button btnPlay;

	// private HupuViewPager mPager;

	/** 统计页 */
	private StatisticFragment mFragmentStatistic;

	/** 评分页 */
	// private PlayersRatingFragment mFragmentPlayer;

	/** 比赛中 和 比赛后 直播和统计页所在的位置不同，所以需要做相应的处理 */
	private int i_liveIndex;

	private int i_staticIndex;
	/** 是否是比赛日 */
	private boolean bMatchDay;

	/** 回调需要记录在从比赛列表进入时是不是关注了该比赛，返回时可以及时的响应出来 */
	// private int i_initFollow;

	private Intent mIntent;

	/** 竖直View */
	View vPortrait;
	/** 水平的View */
	View vLandscape;

	/** 比赛已经结束后，是不是获取的最后直播数据 */
	public boolean getLiveEndData;
	/** 比赛已经结束后，是不是获取的最后统计数据 */
	public boolean getBoxEndData;

	/***/
	private int lastLiveID;

	private int lastChatID;

	/**
	 * 是否为关注比赛，该变量是临时设置，因为关注或取消操作是延时操作，只有数据成功返回后，才是真实情况。 返回失败，则会重置
	 */
	private boolean isFollow;

	private String mDefaultTab;

	/** 统计偏移量 */
	private int bid;

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
		if (savedInstanceState != null)
			savedInstanceState.clear();
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.layout_data);

		HuPuRes.setClient(mDeviceId);
		// l_date =mIntent.getLongExtra("date", 0);
		mIntent = getIntent();
		tag = mIntent.getStringExtra("tag");
		gid = mIntent.getIntExtra("gid", 0);
		lid = mIntent.getIntExtra("lid", 1);
		if (gid > 0) {
			mDefaultTab = mIntent.getStringExtra("tab");

			HupuLog.e("papa", "defaultTab===" + mDefaultTab);
			// 外部跳转
			initParameter();
			mParams.put("gid", "" + gid);
			sendRequest(HuPuRes.REQ_METHOD_NBA_GAMES_BY_GID, mParams);
		} else {
			// 从赛程页进入
			mEntityGame = (BasketballGameEntity) mIntent
					.getSerializableExtra("game");
			gid = mEntityGame.i_gId;
			mDefaultTab = mEntityGame.default_tab;
			if (mEntityGame == null) {
				finish();
			}
			bMatchDay = mIntent.getBooleanExtra("match", false);
			init();
		}
	}


	private boolean isResume = false;

	public void onResume() {

		super.onResume();

		if (bJoinRoom) {
			// 如果需要加入房间
			if (DeviceInfo.isNetWorkEnable(this)) {
				// 如果是非聊天
				if (curFragmentIndex == INDEX_LIVE) {
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
				} else if (curFragmentIndex == INDEX_STATISTIC) {
					if (bid > 0 && !getBoxEndData) {
						// Log.d("onResume", "join statistic");
						joinRoom();
					}
				}
			} else {
				mApp.setNetState(HuPuApp.STATE_NO_NET);
				reconnect(false);
			}
		}
		if (curFragmentIndex == INDEX_PLAYER_RATING) {
			// 刷新
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

	@Override
	public void onLoginSuccess() {
		// TODO Auto-generated method stub
		super.onLoginSuccess();
		if (curFragmentIndex == INDEX_QUIZ) {
			getQuizList();
			joinRoom();
		}
	}

	/** 发送信息 */
	@Override
	public void sendChatMsg(int type, String name, String ss,int roomid) {
		mParams.put("type", "nba");
		mParams.put("username", name);
		mParams.put("roomid", roomid+"");
		if (type == 0) {
			mParams.put("content", ss);
			mParams.remove("emoji");
		} else {
			mParams.put("emoji", ss);
			mParams.remove("content");
		}

		mParams.put("token", SharedPreferencesMgr.getString("tk", ""));
		mParams.put("gid", "" + gid);
		// Log.d("token", SharedPreferencesMgr.getString("tk", ""));
		reqHttp(HuPuRes.REQ_METHOD_SENT_CHAT);
		if (mFragmentChat != null)
			mFragmentChat.addData(type, name, ss);
		// joinRoom();
	}

	private void replaceContent(int index) {
		if (curFragmentIndex == index)
			return;
		Fragment fragment = null;

		switch (index) {
		case INDEX_REPORT:
			if (mFragmentReport == null)
				mFragmentReport = new ReportFragment();
			fragment = mFragmentReport;

			break;
		case INDEX_LIVE:
			if (mFragmentLive == null)
				mFragmentLive = new LiveFragment(mEntityGame.i_home_tid,
						mEntityGame.i_away_tid,
						curGameState != GAME_STATE_NOT_START);
			fragment = mFragmentLive;

			break;
		case INDEX_STATISTIC:
			if (mFragmentStatistic == null)
				mFragmentStatistic = new StatisticFragment(mEntityGame);
			fragment = mFragmentStatistic;

			break;
		case INDEX_CHAT:
			if (mFragmentChat == null) {
				mFragmentChat = new ChatFragment();
				mFragmentChat.setTag("NBA");
			}
			fragment = mFragmentChat;
			break;
		case INDEX_PLAYER_RATING:

			// if (mFragmentPlayer == null)
			// mFragmentPlayer = new PlayersRatingFragment();
			// fragment = mFragmentPlayer;

			break;

		case INDEX_QUIZ:
			if (mFragmentQuizList == null) {
				mFragmentQuizList = new QuizListFragment();
				// mFragmentQuizList.setBets(bets);
			}

			fragment = mFragmentQuizList;
			setJsonObj(BaseEntity.KEY_GAME_ID, gid);
			setJsonObj("roomid", roomid);
			joinRoom(HuPuRes.ROOM_NBA_CASINO);
			// if (mQid >0)
			// mFragmentQuizList.setSelection(mQid);

			break;

		}

		curFragmentIndex = index;
		if (fragment != null)
			replaceContent(fragment);
	}

	private void replaceContent(Fragment fragment) {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.nba_content, fragment);
		transaction.addToBackStack(null);
		transaction.commitAllowingStateLoss();
	}

	public RequestParams getHttpParams() {
		return mParams;

	}

	public boolean sendRequest(int reqType, RequestParams params) {
		return sendRequest(reqType, params, new HupuHttpHandler(this));
	}

	private void initView() {
		vPortrait = findViewById(R.id.layout_portrait);
		vLandscape = findViewById(R.id.layout_land);
		// mPager = (HupuViewPager) findViewById(R.id.pager_datas);

		//		txtTitle = (TextView) findViewById(R.id.txt_title);
		mLayoutScoreBar = findViewById(R.id.layout_score_bar);
		mLayoutScoreBar.setVisibility(View.VISIBLE);

		txtTitle = (TextView) findViewById(R.id.txt_title);
		//		txtTitle.setText(R.string.title_live);

		txtTeamLeft = (TextView) findViewById(R.id.txt_team_left);
		txtTeamRight = (TextView) findViewById(R.id.txt_team_right);
		txtProcess = (TextView) findViewById(R.id.txt_proccess);
		txtScore = (TextView) findViewById(R.id.txt_score);
		setSore(0, 0);
		txtStartTime = (TextView) findViewById(R.id.txt_start_time);

		imgTeamLeft = (ImageView) findViewById(R.id.img_team_left);
		imgTeamRight = (ImageView) findViewById(R.id.img_team_right);
		setTeamName();

		btnChat = (ImageButton) findViewById(R.id.btn_chat);
		btnQuiz = (ImageButton) findViewById(R.id.btn_quiz);

		btnFollow = (Button) findViewById(R.id.btn_follow);
		btnPlay = (Button) findViewById(R.id.btn_play);

		btnThird = (ImageButton) findViewById(R.id.btn_third);
		btnSecond = (ImageButton) findViewById(R.id.btn_second);
		btnReport = (ImageButton) findViewById(R.id.btn_first);

		// btnRating = (ImageButton) findViewById(R.id.btn_players_rating);

		// initExciting();
		imgRedPiont = (ImageView) findViewById(R.id.icon_red_point);
		imgRedPiont.setVisibility(View.GONE);
		rewardInfo = (RelativeLayout) findViewById(R.id.reward_info);

		title_room_switch = (ImageView) findViewById(R.id.title_room_switch);
		room_people_numTextView = (TextView) findViewById(R.id.title_room_peple_online);
		//		eAnimation = new EpandDownAnimation(this,new EpandCallBack());
	}

	/** 初始化 */
	private void init() {
		curGameState = mEntityGame.byt_status;
		txtStartTime = (TextView) findViewById(R.id.txt_start_time);
		UMENG_MAP.clear();
		HupuLog.e("papa", "curGameState==" + curGameState);
		if (curGameState == GAME_STATE_CANCEL) {
			txtStartTime.setText(R.string.canceled);
		} else {
			initView();
			initParameter();
			setJsonObj(BaseEntity.KEY_GAME_ID, gid);
			mParams.put(BaseEntity.KEY_GAME_ID, "" + gid);
			mParams.put(BaseEntity.KEY_VERTICAL, "" + true);

			setViewByStatus(true);
			if (curGameState == GAME_STATE_NOT_START) {
				// 未开始
				txtStartTime.setVisibility(View.VISIBLE);
				txtStartTime.setText(MyUtility.getStartTime(
						mEntityGame.l_begin_time * 1000, sdf));
				txtScore.setVisibility(View.GONE);

				if (mDefaultTab == null)
					treatClickEvent(R.id.btn_quiz);// 竞猜
				else
					switchTab();
				setScreenLight(true);
			} else {

				// mFragmentLive.isStart(true);

				mLayoutScoreBar.setVisibility(View.VISIBLE);
				txtStartTime.setVisibility(View.GONE);
				setSore(mEntityGame.i_home_score, mEntityGame.i_away_score);
				txtProcess.setText(mEntityGame.str_process);

				if (mDefaultTab != null) {
					switchTab();
				} else {
					if (curGameState == GAME_STATE_END)// 已经结束
					{
						treatClickEvent(R.id.btn_first);// 战报

					} else if (curGameState == GAME_STATE_ONGOING) // 进行中
					{
						// mPager.setCurrentItem(1, false);
						treatClickEvent(R.id.btn_second);// 直播
						setScreenLight(true);
					}
				}
			}

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
		setOnClickListener(R.id.btn_third);
		setOnClickListener(R.id.btn_second);
		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.btn_first);
		setOnClickListener(R.id.btn_follow);
		setOnClickListener(R.id.btn_play);
		// setOnClickListener(R.id.btn_players_rating);
		setOnClickListener(R.id.btn_land);
		setOnClickListener(R.id.btn_chat);
		setOnClickListener(R.id.btn_quiz);
		setOnClickListener(R.id.btn_sent);

		setOnClickListener(R.id.gold_num);
		setOnClickListener(R.id.img_team_left);
		setOnClickListener(R.id.img_team_right);
		btnFollow = (Button) findViewById(R.id.btn_follow);
		btnPlay = (Button) findViewById(R.id.btn_play);

		isFollow = mEntityGame.bFollow == 1;
		setFollowBtn();
	}

	/*
	 * @Override public void onWindowFocusChanged(boolean hasFocus) {
	 * phizAnimation.start();
	 * 
	 * }
	 */


	/** 设置关注比赛的UI状态 */
	private void setFollowBtn() {
		if (curGameState == GAME_STATE_ONGOING
				|| curGameState == GAME_STATE_NOT_START) {

			// 默认tag 修改后 会有自动跳入其他tag 所以默认不显示 故注释掉 2014 1-22

			// findViewById(R.id.layout_title_btn).setVisibility(View.VISIBLE);
			if (isFollow) {
				// btnFollow.setImageResource(R.drawable.btn_txt_followed);
				// btnFollow.setImageResource(R.drawable.btn_dated_2);
				btnFollow.setBackgroundResource(R.drawable.btn_alarm_bright);
			} else {
				// btnFollow.setImageResource(R.drawable.btn_txt_follow);
				// btnFollow.setImageResource(R.drawable.btn_date_2);
				btnFollow.setBackgroundResource(R.drawable.btn_alarm_dark);
			}

		} else {
			btnFollow.setVisibility(View.GONE);
			btnPlay.setVisibility(View.GONE);
			findViewById(R.id.layout_title_btn).setVisibility(View.GONE);
		}
	}

	/** 设置球队名称 */
	private void setTeamName() {
		// 球队名称
		txtTeamLeft.setText(mEntityGame.str_home_name + " (主)");
		txtTeamRight.setText(mEntityGame.str_away_name + " (客)");
		// UrlImageViewHelper.setUrlDrawable(imgTeamLeft, mEntityGame.home_logo,
		// R.drawable.bg_home_nologo);
		// UrlImageViewHelper.setUrlDrawable(imgTeamRight,
		// mEntityGame.away_logo, R.drawable.bg_home_nologo);
		if (mEntityGame.home_logo != null)
			UrlImageViewHelper.setUrlDrawable(imgTeamLeft,
					mEntityGame.home_logo, R.drawable.bg_home_nologo);
		else
			imgTeamLeft.setImageResource(HuPuApp
					.getTeamData(mEntityGame.i_home_tid).i_logo_small);

		if (mEntityGame.away_logo != null)
			UrlImageViewHelper.setUrlDrawable(imgTeamRight,
					mEntityGame.away_logo, R.drawable.bg_home_nologo);
		else
			imgTeamRight.setImageResource(HuPuApp
					.getTeamData(mEntityGame.i_away_tid).i_logo_small);
	}

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);

		// 底部栏 需要数据返回后才显示！
		showBottomBar();
		switch (methodId) {
		case HuPuRes.REQ_METHOD_GET_PLAY_LIVE_ASC:
		case HuPuRes.REQ_METHOD_GET_PLAY_LIVE_DESC:

			NBALiveResp data = (NBALiveResp) o;
			if (data != null) {
				initGift(data,INDEX_LIVE,true);

				movieUrl = data.tvLink;
				if (!"".equals(movieUrl))
					btnPlay.setEnabled(true);
				if (curGameState != GAME_STATE_NOT_START)
					mFragmentLive.isStart(true);

				if (data.follow > -1 && data.follow != mEntityGame.bFollow) {
					mEntityGame.bFollow = (byte) data.follow;
					isFollow = mEntityGame.bFollow == 1;
					setFollowBtn();
				}
				roomPreview = data.preview;
				mFragmentLive.setPreview(roomPreview);
				roomid = data.default_room_id;
				setJsonObj("roomid", roomid);
				if (data.preview != null && !"".equals(data.preview)) {
					// 设置前瞻
					HupuLog.e("papa", "---set----preview="+roomPreview);
					mFragmentLive.setURL(data.preview);
				}

				if (data.casinoInit != null) {
					bets = data.casinoInit.bets;
					mFragmentLive.setBets(data.casinoInit.bets);
				}
				if (data.dataList == null) {
					// 没有数据
					if (curGameState == GAME_STATE_NOT_START) {
						// showToast("比赛未开始");
						bJoinRoom = true;
						joinRoom(HuPuRes.ROOM_PLAYBYPLAY);
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
				if (methodId == HuPuRes.REQ_METHOD_GET_PLAY_LIVE_DESC){
					setJsonObj(BaseEntity.KEY_PID, data.i_pId);
				}
				
				if (curGameState != GAME_STATE_CANCEL) {// 如果比赛未结束需要加入房间
					bJoinRoom = true;
					joinRoom(HuPuRes.ROOM_PLAYBYPLAY);
				} else {
					getLiveEndData = true;
				}


				//				if (data.is_enter == 0) {
				//					livefirstIn = true;
				//					setRoomTitle(getString(R.string.title_select_room));
				//					showRoomAction();
				//					title_room_switch.setImageResource(R.drawable.icon_arrow_up_live);
				//				}else{
				//					livefirstIn=false;
				//				}

				if (data.roomList != null) {
					boolean isShow = data.roomList!=null&&data.roomList.size()>1?true:false;

					if (data.is_enter == 0&&isShow) {
						livefirstIn = true;
					}else{
						livefirstIn = false;
					}
					switchRoomStatus(INDEX_LIVE,livefirstIn);
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



			}
			break;
		case HuPuRes.REQ_METHOD_BOX_SCORE:
			BoxScoreResp box = (BoxScoreResp) o;
			if (box.mEntityHome == null) {
				// 没有数据
				mFragmentStatistic.addData(true);
				showToast("暂无统计数据");
				return;
			}
			mFragmentStatistic.setData(box, false);
			if (mBoxscoreData == null)
				mBoxscoreData = new BoxscoreDatas();
			mFragmentStatistic.updateBoxScoreData(mBoxscoreData);
			bHasStatisticData = true;
			setJsonObj("bid", box.i_bId);
			if (curGameState != GAME_STATE_END) {
				setJsonObj("roomid", roomid);
				joinRoom(HuPuRes.ROOM_NBA_BOXSCORE);
				bJoinRoom = true;
			} else {
				getBoxEndData = true;
			}

			if (isLandMode) {
				if (mDataLandAdapter == null)
					initLand();
				mDataLandAdapter.setData(mBoxscoreData);
			}
			break;
		case HuPuRes.REQ_METHOD_GET_RECAP:
			Recap recap = (Recap) o;
			mFragmentReport.setData(recap);
			bHasReportData = true;
			break;
		case HuPuRes.REQ_METHOD_FOLLOW_NBA_GAME:
		case HuPuRes.REQ_METHOD_FOLLOW_NBA_GAME_CANCEL:
			FollowResp resp = (FollowResp) o;
			if (resp == null || resp.i_success == 0) {
				// 提交失败
				showToast(String.format(SORRY_NOTIFY,
						mEntityGame.str_home_name, mEntityGame.str_away_name));
				isFollow = !isFollow;
				setFollowBtn();
			} else {
				if (isFollow)
					mEntityGame.bFollow = 1;
				else
					mEntityGame.bFollow = 0;
				if (methodId == HuPuRes.REQ_METHOD_FOLLOW_NBA_GAME)
					showToast(String.format(SUCCESS_NOTIFY,
							mEntityGame.str_home_name,
							mEntityGame.str_away_name));
				if (methodId == HuPuRes.REQ_METHOD_FOLLOW_NBA_GAME_CANCEL)
					showToast(CANCEL_NOTIFY);
			}
			break;
		case HuPuRes.REQ_METHOD_SENT_CHAT:
			SendMsgResp msg = (SendMsgResp) o;
			if (msg.err != null) {
				// Log.d("SendMsgResp", data.err);
				showToast(msg.err);
			} else {

				if (msg.pid == 0) {
					reqChatData(lastChatID);
				} else {
					mFragmentChat.setLastId(msg.pid);
					lastChatID = msg.pid;
					setJsonObj("pid", lastChatID);
					setJsonObj("direc", "next");
					if (mToken != null)
						setJsonObj("tk", "mToken");
					setJsonObj("roomid", roomid);	
					joinRoom(HuPuRes.ROOM_NBA_CHAT);
				}

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
		case HuPuRes.REQ_METHOD_NBA_GAMES_BY_GID:
			mEntityGame = (BasketballGameEntity) o;
			init();
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
				if (!isExchange) {
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			cancelConnection();
			finish();
		}
		return false;
	}

	private final int DIALOG_NOTIFY = 1314;

	@Override
	public void clickNegativeButton(int dialogId) {
		super.clickNegativeButton(dialogId);

		if (mDialog != null)
			mDialog.cancel();
	}

	@Override
	public void clickPositiveButton(int dialogId) {
		super.clickPositiveButton(dialogId);
		switch (dialogId) {

		case DIALOG_NOTIFY:
			mApp.setNotify(true);
			followGame(gid, (byte) 0);
			isFollow = !isFollow;
			setFollowBtn();
			break;
		}
		// 打开通知
		if (mDialog != null)
			mDialog.cancel();
	}

	private void setFollow() {
		if (isFollow) {
			followGame(gid, (byte) 1);

		} else {
			if (mApp.needNotify) {
				followGame(gid, (byte) 0);
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

	@Override
	public void onErrResponse(Throwable error, int type) {
		String content = error.toString();
		if (content != null) {
			if (type == HuPuRes.REQ_METHOD_RATING_RATE) {
			}
			showToast(content);
		}

	}

	@Override
	public void updateMoney(int betCoin, int balance) {
		if (mFragmentLive != null)
			mFragmentLive.updateMoney(betCoin, money);
	}

	/** 请求Http数据 */
	private void reqHttp(int repType) {
		mParams.put(BaseEntity.KEY_GAME_ID, "" + gid);
		sendRequest(repType, mParams, new HupuHttpHandler(this));
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
		if (bJoinRoom)
			reqLeaveRoom();
	}

	/** 根据不同的比赛状态，切换不同的显示方式 */
	private void setViewByStatus(boolean init) {

		HupuLog.d("setViewByStatus", "curGameState=" + curGameState);

		if (curGameState == GAME_STATE_END) {
			// 比赛结束后第一个按键是战报，第二个是统计，三个是直播，第四个是热线
			btnReport.setVisibility(View.VISIBLE);
			btnSecond.setImageResource(R.drawable.btn_statistics);
			btnThird.setImageResource(R.drawable.btn_live_up);
			i_liveIndex = R.id.btn_third;
			i_staticIndex = R.id.btn_second;
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
				|| curGameState == GAME_STATE_NOT_START) {
			i_liveIndex = R.id.btn_second;
			i_staticIndex = R.id.btn_third;
			btnSecond.setImageResource(R.drawable.btn_live_up);
			btnThird.setImageResource(R.drawable.btn_statistics);
			if (curGameState == GAME_STATE_ONGOING && !init) {
				mLayoutScoreBar.setVisibility(View.VISIBLE);
				txtScore.setVisibility(View.VISIBLE);
				setSore(mEntityGame.i_home_score, mEntityGame.i_away_score);
				txtStartTime.setVisibility(View.GONE);
				txtProcess.setText(mEntityGame.str_process);
				// mPager.setCurrentItem(INDEX_LIVE, false);
				replaceContent(INDEX_LIVE);
				setBackgound(INDEX_LIVE);
				setScreenLight(true);
			}
			if (curGameState == GAME_STATE_NOT_START) {
				//
				btnThird.setVisibility(View.GONE);
			} else {
				btnThird.setVisibility(View.VISIBLE);
			}
		}
	}

	/** 点击后的按钮背景变化 */
	private void setBackgound(int index) {

		initGift(null,index,false);
		switchRoomStatus(index,livefirstIn);

		int color = getResources().getColor(R.color.transform);
		switch (curFragmentIndex) {// 点击前
		case INDEX_PLAYER_RATING:
		case INDEX_REPORT:
			btnReport.setBackgroundColor(color);
			btnReport.setImageResource(R.drawable.btn_report);
			break;
		case INDEX_LIVE:
			if (i_liveIndex == R.id.btn_second)// 进行中
			{
				btnSecond.setBackgroundColor(color);
				btnSecond.setImageResource(R.drawable.btn_live_up);
			} else {
				btnThird.setBackgroundColor(color);
				btnThird.setImageResource(R.drawable.btn_live_up);
			}
			break;
		case INDEX_STATISTIC:
			if (i_staticIndex == R.id.btn_second) {
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
			// case INDEX_PLAYER_RATING:
			// btnRating.setBackgroundColor(color);
			// btnRating.setImageResource(R.drawable.btn_rating);
			// break;

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
			if (i_liveIndex == R.id.btn_second) {
				btnSecond.setBackgroundResource(R.drawable.bg_bottom_hover);
				btnSecond.setImageResource(R.drawable.btn_live_down);
			} else {
				btnThird.setBackgroundResource(R.drawable.bg_bottom_hover);
				btnThird.setImageResource(R.drawable.btn_live_down);
			}
			if (curGameState == GAME_STATE_ONGOING
					|| curGameState == GAME_STATE_NOT_START) {
				findViewById(R.id.layout_title_btn).setVisibility(View.VISIBLE);
			}
			break;
		case INDEX_STATISTIC:
			mLayoutScoreBar.setVisibility(View.VISIBLE);
			if (i_staticIndex == R.id.btn_second) {
				btnSecond.setBackgroundResource(R.drawable.bg_bottom_hover);
				btnSecond.setImageResource(R.drawable.btn_statistics_hover);
			} else {
				btnThird.setBackgroundResource(R.drawable.bg_bottom_hover);
				btnThird.setImageResource(R.drawable.btn_statistics_hover);
			}
			findViewById(R.id.btn_land).setVisibility(View.VISIBLE);
			findViewById(R.id.layout_title_btn).setVisibility(View.GONE);
			registSetting();
			if (Settings.System.getInt(getContentResolver(),
					Settings.System.ACCELEROMETER_ROTATION, 0) != 0)
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
			break;
		case INDEX_CHAT:
			mLayoutScoreBar.setVisibility(View.VISIBLE);
			btnChat.setBackgroundResource(R.drawable.bg_bottom_hover);
			btnChat.setImageResource(R.drawable.btn_chat_down);
			findViewById(R.id.layout_title_btn).setVisibility(View.GONE);
			findViewById(R.id.btn_sent).setVisibility(View.VISIBLE);
			break;
		case INDEX_PLAYER_RATING:
			mLayoutScoreBar.setVisibility(View.GONE);
			// btnRating.setBackgroundResource(R.drawable.bg_bottom_hover);
			// btnRating.setImageResource(R.drawable.btn_rating_hover);

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

	/** 根据默认标签跳转到不同的页面 */
	private void switchTab() {

		if (mDefaultTab.equals(TAB_GUESS)) {
			treatClickEvent(R.id.btn_quiz);
		} else if (mDefaultTab.equals(TAB_BOXSCORE))
			treatClickEvent(R.id.btn_third);
		else if (mDefaultTab.equals(TAB_CHAT)) {
			treatClickEvent(R.id.btn_chat);
		} else if (mDefaultTab.equals(TAB_REPORT))
			treatClickEvent(R.id.btn_first);
		else
			treatClickEvent(R.id.btn_second);// 直播
	}
	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);
		UMENG_MAP.clear();
		//		showRoomAction();

		if (id == i_liveIndex) {
			if (curFragmentIndex != INDEX_LIVE) {//从其他tab点击到直播

				//				setRoomTitle(curRoomtitle);
				//				setRoomOnlineNum(curRoomOnline);

				setBackgound(INDEX_LIVE);
				// mPager.setCurrentItem(INDEX_LIVE, true);
				replaceContent(INDEX_LIVE);
				mFragmentLive.entry();
				if (!bHasLiveData) {
					// 初次进入该页面需要获取全部的实时播报信息
					if (curGameState == GAME_STATE_ONGOING)// 进行中
					{
						sortId = HuPuRes.REQ_METHOD_GET_PLAY_LIVE_DESC;
						req_room_http(roomid);
					} else {
						sortId = HuPuRes.REQ_METHOD_GET_PLAY_LIVE_ASC;
						req_room_http(roomid);
					}
				} else {
					mFragmentLive.setPreview(roomPreview);
					if (roomPreview != null && !"".equals(roomPreview)) {
						// 设置前瞻
						mFragmentLive.setURL(roomPreview);
					}
					bJoinRoom = true;
					// 加入前设置最后一条消息id
					if (lastLiveID > 0)
						setJsonObj(BaseEntity.KEY_PID, lastLiveID);
					// 如果是有竞猜的比赛，需要加入所有为关闭的竞猜id
					setJsonObj("qids", mFragmentLive.getQids());
					setJsonObj("roomid", roomid);
					joinRoom(HuPuRes.ROOM_PLAYBYPLAY);

				}
				setScreenLight(true);
			}
			curFragmentIndex = INDEX_LIVE;

		} else if (id == i_staticIndex) {
			if (curFragmentIndex != INDEX_STATISTIC) {
				txtTitle.setText(R.string.title_statistic);
				setBackgound(INDEX_STATISTIC);
				replaceContent(INDEX_STATISTIC);
				if (!bHasStatisticData || (bEnd && !getBoxEndData)) {
					mParams.put(BaseEntity.KEY_VERTICAL, "" + true);
					reqHttp(HuPuRes.REQ_METHOD_BOX_SCORE);
				} else if (curGameState != GAME_STATE_END) {
					setJsonObj("roomid", roomid);
					joinRoom(HuPuRes.ROOM_NBA_BOXSCORE);
					bJoinRoom = true;
				}

				findViewById(R.id.btn_land).setVisibility(View.VISIBLE);
				if (curGameState == GAME_STATE_ONGOING
						|| curGameState == GAME_STATE_NOT_START)
					findViewById(R.id.layout_title_btn)
					.setVisibility(View.GONE);
				// mFragmentStatistic.entry();
			}
			curFragmentIndex = INDEX_STATISTIC;
		}

		switch (id) {
		case R.id.btn_first:
			if (curFragmentIndex != INDEX_REPORT) {
				txtTitle.setText(R.string.title_report);
				setBackgound(INDEX_REPORT);
				replaceContent(INDEX_REPORT);
				if (bMatchDay || !bHasReportData)
					reqHttp(HuPuRes.REQ_METHOD_GET_RECAP);
			}
			curFragmentIndex = INDEX_REPORT;
			break;

		case R.id.btn_chat:
			if (curFragmentIndex != INDEX_CHAT) {
				txtTitle.setText(R.string.title_chat);
				setBackgound(INDEX_CHAT);
				replaceContent(INDEX_CHAT);

				mFragmentChat.entry(roomid);
				bJoinRoom = true;
				// 一直常亮
				setScreenLight(true);
			}
			curFragmentIndex = INDEX_CHAT;
			break;
		case R.id.btn_quiz:
			txtTitle.setText(R.string.quiz_list_tiele);
			setBackgound(INDEX_QUIZ);
			replaceContent(INDEX_QUIZ);
			curFragmentIndex = INDEX_QUIZ;
			break;
		case R.id.btn_back:
			finish();
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
		case R.id.btn_home_land:
			if (mDataLandAdapter != null) {
				mDataLandAdapter.changeMode(0);
				if (mDataLandAdapter.getCount() > 0)
					mListLandPlayer.setSelection(0);
			}
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
			btnHome.setBackgroundColor(clrOn);
			btnAway.setBackgroundColor(clrOff);
			if (txtTeamName != null && mEntityGame != null
					&& mEntityGame.str_home_name != null)
				txtTeamName.setText(mEntityGame.str_home_name);
			break;
		case R.id.btn_away_land:
			if (mDataLandAdapter != null) {
				mDataLandAdapter.changeMode(1);
				if (mDataLandAdapter.getCount() > 0)
					mListLandPlayer.setSelection(0);
			}

			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
			btnHome.setBackgroundColor(clrOff);
			btnAway.setBackgroundColor(clrOn);
			if (txtTeamName != null && mEntityGame != null
					&& mEntityGame.str_away_name != null)
				txtTeamName.setText(mEntityGame.str_away_name);
			break;
		case R.id.btn_land:
			// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
			// switchToLandMode();
			// showToast("翻转查看");
			showTipsDialog();
			break;
		case R.id.btn_close:
			closeDialog();
			break;
		case R.id.btn_sent:
			Intent in = new Intent(this, ChatInputActivity.class);
			in.putExtra("tag", "NBA");
			in.putExtra("roomid", roomid);
			startActivityForResult(in, REQ_SEND_MSG);
			toChat = true;

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
		case R.id.img_team_right:
			if (mEntityGame.i_away_tid < 31) {
				sendUmeng(HuPuRes.UMENG_EVENT_NBA_TEAMS,
						HuPuRes.UMENG_KEY_ENTRANCE,
						HuPuRes.UMENG_VALUE_NBA_SCORE_CARD);
				Intent right = new Intent(this, NBATeamActivity.class);
				right.putExtra("tid", mEntityGame.i_away_tid);
				startActivity(right);
			}
			break;
		case R.id.img_team_left:
			if (mEntityGame.i_home_tid < 31) {
				sendUmeng(HuPuRes.UMENG_EVENT_NBA_TEAMS,
						HuPuRes.UMENG_KEY_ENTRANCE,
						HuPuRes.UMENG_VALUE_NBA_SCORE_CARD);
				Intent left = new Intent(this, NBATeamActivity.class);
				left.putExtra("tid", mEntityGame.i_home_tid);
				startActivity(left);
			}

			break;
			//		case R.id.title_room_switch:
			//		case R.id.txt_title:
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

	/** 设置球队比分 需要翻牌，这个逻辑就需要先判断比分的变换了。 */
	private void setSore(int homeScore, int awayScore) {
		if (homeScore > 0 || awayScore > 0)
			txtScore.setText(homeScore + " - " + awayScore);
		// HupuLog.d("setsore", "s="+homeScore);

	}

	/** 更新比赛过程的标题信息 */
	private void updateProccess(String s) {
		if (s != null && curGameState != GAME_STATE_NOT_START)
			txtProcess.setText(s);
	}

	/** 请求热线数据，每页20条 */
	public void reqChatData(int id) {

		UMENG_MAP.clear();
		setJsonObj("type", "nba");
		setJsonObj("num", 20);
		setJsonObj("roomid", roomid);
		if (id > 0) {
			setJsonObj("pid", id);
			setJsonObj("direc", "prev");
		} else {
			// 重新获取最新的聊天记录，不传偏移量
			setJsonObj("pid", "");
			setJsonObj("direc", "next");
			lastChatID = 0;
		}
		if (mToken != null)
			setJsonObj("tk", "mToken");
		joinRoom(HuPuRes.ROOM_NBA_CHAT);
	}

	@Override
	public void onSocketConnect() {
		// Log.d("HupuDataActivity", "onSocketConnect  >>>>>>:::::");
		super.onSocketConnect();
		// 连接成功了，加入room。
		joinRoom();
		mApp.setNetState(HuPuApp.STATE_CONNECTED);
		setNetTitle();
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
	public void onSocketError(Exception socketIOException) {
		super.onSocketError(socketIOException);

		if (curFragmentIndex == INDEX_LIVE) {
			mFragmentLive.stopLoad();
		} else if (curFragmentIndex == INDEX_CHAT) {
			mFragmentChat.stopLoad();
		}
		mApp.setNetState(HuPuApp.STATE_NET_ERR);
		setNetTitle();
		reconnect(false);

	}

	private boolean bottombarVisible = false;

	private void showBottomBar() {
		if (findViewById(R.id.layout_bottom) != null) {
			findViewById(R.id.layout_bottom).setVisibility(View.VISIBLE);
			bottombarVisible = true;
		}
	}

	@Override
	public void onSocketResp(JSONObject obj) {
		Log.d("nbaactivity", "onSocketResp  >>>>>>:::::"+obj.toString());
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
				int gd = obj.optInt(BaseEntity.KEY_GAME_ID, -1);
				// Log.d("HupuDataActivity", "onSocketResp  >>>>>>::::: gid"
				// + gd);
				if (gd != gid)
					return;

				if (status > -1 && status != curGameState) {
					curGameState = status;
					setViewByStatus(false);
				}
				if (HuPuRes.ROOM_NBA_BOXSCORE.equals(room)) {
					if (curGameState == GAME_STATE_END) {
						bEnd = true;
						getBoxEndData = true;
					}
					// 统计
					BoxScoreResp data = new BoxScoreResp();
					data.paser(obj);
					bid = obj.optInt("bid", -1);
					// 更新消息id
					if (bid > 0)
						setJsonObj("bid", bid);
					setSore(data.i_scoreHome, data.i_scoreAway);
					updateProccess(data.str_process);
					mFragmentStatistic.updateData(data);

					mFragmentStatistic.updateBoxScoreData(mBoxscoreData);
					if (isLandMode)
						mDataLandAdapter.setData(mBoxscoreData);

				} else if (HuPuRes.ROOM_NBA_PLAYBYPLAY_CASINO.equals(room)) {
					if (curGameState == GAME_STATE_END) {
						bEnd = true;
						getLiveEndData = true;
					}
					// 直播
					NBALiveResp data = new NBALiveResp();
					data.paser(obj);

					if (curFragmentIndex == INDEX_LIVE
							&& data.people_num != null)
					{
//						txtTitle.setText("直播(" + data.online + "人)");
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
				else if (HuPuRes.ROOM_PLAYBYPLAY.equals(room)) {
					if (curGameState == GAME_STATE_END) {
						bEnd = true;
						getLiveEndData = true;
					}
					// 直播
					NBALiveResp data = new NBALiveResp();
					data.paser(obj);
					if (curFragmentIndex == INDEX_LIVE
							&& data.people_num != null)
						//						txtTitle.setText("直播(" + data.people_num + "人)");
						setRoomOnlineNum(String.format(this.live_online, data.people_num),true);
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
				else if (HuPuRes.ROOM_NBA_CHAT.equals(room)) {
					// 热线
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
					if (data.score != null)
						setSore(data.score.i_scoreHome, data.score.i_scoreAway);
					updateProccess(data.score.str_process);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		mApp.setNetState(HuPuApp.STATE_ON_LINE);
		setNetTitle();
	}

	/** 横屏时统计列表的标题 **/
	private TextView[] txtHeaders;
	/** 横屏时统计列表 **/
	private ListView mListLandPlayer;
	/** 横屏时统计列表的适配器 **/
	private GameDataListLandAdapter mDataLandAdapter;
	/** 横屏时主队名字 **/
	TextView txtHome;
	/** 横屏时客队名 **/
	TextView txtAway;
	/** 横屏时主队logo **/
	ImageView imgHome;
	/** 横屏时客队logo **/
	ImageView imgAway;
	/** 横屏时主队分割线 **/
	View lineHome;
	/** 横屏时客队分割线 **/
	View lineAway;
	/** 横屏时主队按钮 **/
	View btnHome;
	/** 横屏时客队按钮 **/
	View btnAway;

	/** 横屏时列表标题栏球队名 **/
	private TextView txtTeamName;
	/** 颜色值 **/
	private int clrOn;

	private int clrOff;

	/** 用代码处理屏幕翻转的逻辑 */
	@Override
	public void lockScreenRotation(int orientation) {
		// Stop the screen orientation changing during an event
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

	/** 切换到水平模式 **/
	public void switchToLandMode() {
		isLandMode = true;
		vPortrait.setVisibility(View.GONE);
		vLandscape.setVisibility(View.VISIBLE);
		closeDialog();

		if (txtHome == null) {
			txtHome = (TextView) findViewById(R.id.txt_home_land);

			txtAway = (TextView) findViewById(R.id.txt_away_land);

			imgHome = (ImageView) findViewById(R.id.img_home_land);
			imgAway = (ImageView) findViewById(R.id.img_away_land);
			btnHome = findViewById(R.id.btn_home_land);
			btnAway = findViewById(R.id.btn_away_land);
			setOnClickListener(R.id.btn_home_land);
			setOnClickListener(R.id.btn_away_land);
			txtHome.setText(mEntityGame.str_home_name);
			txtAway.setText(mEntityGame.str_away_name);
			lineHome = findViewById(R.id.line_home);
			lineAway = findViewById(R.id.line_away);
			lineHome.setBackgroundColor(HuPuApp
					.getTeamData(mEntityGame.i_home_tid).i_color);
			lineAway.setBackgroundColor(HuPuApp
					.getTeamData(mEntityGame.i_away_tid).i_color);
			if (mEntityGame.home_logo != null)
				UrlImageViewHelper.setUrlDrawable(imgHome,
						mEntityGame.home_logo, R.drawable.bg_home_nologo);
			else
				imgHome.setImageResource(HuPuApp
						.getTeamData(mEntityGame.i_home_tid).i_logo_small);

			if (mEntityGame.away_logo != null)
				UrlImageViewHelper.setUrlDrawable(imgAway,
						mEntityGame.away_logo, R.drawable.bg_home_nologo);
			else
				imgAway.setImageResource(HuPuApp
						.getTeamData(mEntityGame.i_away_tid).i_logo_small);
			clrOn = getResources().getColor(R.color.dark_gray);
			clrOff = getResources().getColor(R.color.res_cor6);

		}
		if (txtHeaders == null)
			initLand();
		if (mBoxscoreData != null) {
			mDataLandAdapter.setData(mBoxscoreData);
		}

		setFullScreen();
	}

	/** 统计数据 **/
	public BoxscoreDatas mBoxscoreData;

	/** 切换到竖直模式 **/
	public static class BoxscoreDatas {
		public ArrayList<PlayerEntity> mListPLay;
		public ArrayList<String> mListPLayerNames;
		/** 横屏模式的key */
		public ArrayList<String> mListKeys;
		// 横屏Titles
		public ArrayList<String> mTitles;
		public int i_homeSize;

		public LinkedHashMap<String, String> mMapHomeTotal;

		public LinkedHashMap<String, String> mMapAwayTotal;
		// 主队命中率
		public String str_home_fg;
		public String str_home_tp;
		public String str_home_ft;
		// 客队命中率
		public String str_away_fg;
		public String str_away_tp;
		public String str_away_ft;

	}

	/** 初始化水平状态的一些view **/
	private void initLand() {
		// if (curFragmentIndex == INDEX_STATISTIC) {
		// findViewById(R.id.btn_land).setVisibility(View.VISIBLE);
		// findViewById(R.id.layout_title_btn).setVisibility(View.GONE);
		// } else {
		// findViewById(R.id.btn_land).setVisibility(View.GONE);
		// findViewById(R.id.layout_title_btn).setVisibility(View.VISIBLE);
		// }
		if (txtHeaders == null && mBoxscoreData != null) {
			txtTeamName = (TextView) findViewById(R.id.txt_name_land);
			LinearLayout headView = (LinearLayout) findViewById(R.id.layout_header_land);
			headView.setVisibility(View.VISIBLE);
			txtHeaders = new TextView[mBoxscoreData.mListKeys.size()];
			LinearLayout.LayoutParams llp = null;
			LayoutInflater in = LayoutInflater.from(this);
			String key = null;
			for (int i = 0; i < txtHeaders.length; i++) {
				txtHeaders[i] = (TextView) in.inflate(
						R.layout.static_header_land, null);

				txtHeaders[i].setText(mBoxscoreData.mTitles.get(i));
				key = mBoxscoreData.mListKeys.get(i);
				// 计算间距
				if (key.equals("fg") || key.equals("ft") || key.equals("tp"))
					llp = new LinearLayout.LayoutParams(0, -1, 10);
				else if (key.equals("mins") || key.equals("pts"))
					llp = new LinearLayout.LayoutParams(0, -1, 7);
				else
					llp = new LinearLayout.LayoutParams(0, -1, 5);
				headView.addView(txtHeaders[i], llp);
			}
			mListLandPlayer = (ListView) findViewById(R.id.list_players_land);
			mDataLandAdapter = new GameDataListLandAdapter(this);
			mListLandPlayer.setAdapter(mDataLandAdapter);
			txtTeamName.setText(mEntityGame.str_home_name);
			setOnItemClick(mListLandPlayer);
		}
	}

	/** 切换到竖直模式 **/
	public void switchToPortraitMode() {

		isLandMode = false;
		vPortrait.setVisibility(View.VISIBLE);
		vLandscape.setVisibility(View.GONE);

		quitFullScreen();
	}

	@Override
	public void treatItemClick(AdapterView<?> list, View v, int pos, long arg3) {
		super.treatItemClick(list, v, pos, arg3);
		if (list == mListLandPlayer) {

			PlayerEntity data = (PlayerEntity) mDataLandAdapter.getItemAt(pos);
			if (data != null) {

				int id = -1;
				try {
					id = Integer.parseInt(data.str_player_id);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				if (id > 0) {
					Intent in = new Intent(this, NBAPlayerInfoActivity.class);
					in.putExtra("pid", id);
					startActivity(in);
				}

			}
		}
	}

	DecimalFormat df = new DecimalFormat("0.0");

	/**
	 * 跳转到竞猜列表页
	 * */
	private void switchToGuessActivity() {
		if (bets != null) {
			Intent in = new Intent(this, QuizListActivity.class);
			in.putExtra("lid", lid);
			in.putExtra("gid", gid);
			if (mQid > -1)
				in.putExtra("qid", mQid);
			in.putExtra("bets", bets);
			startActivityForResult(in, HupuBaseActivity.REQ_SHOW_QUIZLIST);
			mQid = -1;
		}
		// 重置掉qid 防止重复定位
	}

	private boolean isShowPop;
	int[] bets;

	public void showPop(int q) {
		mQid = q;
		isShowPop = true;
	}

	public void hidePop() {
		isShowPop = false;
	}

	public void switchToRating() {
		Intent in = new Intent(this, PlayersRatingActivity.class);
		in.putExtra("gid", "" + gid);
		startActivity(in);
	}
	/***********************房间相关*****************************/
	/**
	 * 请求房间列表
	 */
	private void req_room_http(int id) {
		if (sortId == HuPuRes.REQ_METHOD_GET_PLAY_LIVE_DESC) 
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
		mRoomListAdapter.setData(roomList);
		mRoomListAdapter.setDefaultId(default_id);
		mRoomListView.setAdapter(mRoomListAdapter);
		mRoomListAdapter.notifyDataSetChanged();

		mRoomListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				RoomEntity roomEntity = (roomList == null || roomList.get(position)==null)?null:roomList.get(position);
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


	/***********************礼物相关*****************************/
//	GiftTipsDialog	tipsDialog;
//	LinearLayout layGift;
//	public int hupuDollor;
//	AnimationTool animationTool;
//	public ArrayList<GiftEntity> giftList;
//	public ArrayList<View> giftTvList = new ArrayList<View>();
//	boolean exit;
//	public final static int Msg_Show_Gift=1;
//	ArrayList<GiftReqDataEntity> sendlist = new ArrayList<GiftReqDataEntity>();
//	int interval=1;
//	Handler mHandler;
//	final int LETTER_MAX_LINE=3;
//	final int CHAR_MAR_LINE=6;
//	private void initGift(final BaseLiveResp data,int tab,boolean fromResp){
//		if(layGift == null){
//			layGift= (LinearLayout)findViewById(R.id.layout_gift);
//		}
//		if(fromResp){
//			layGift.removeAllViews();
//		}
//
//		if(tab != INDEX_LIVE){//非直播tab
//			//            quitLive();
//			layGift.setVisibility(View.GONE);
//			return;
//		}
//		if(data == null || data.giftList == null || data.giftList.size() == 0){
//			return;
//		}
//		giftList = data.giftList;
//		roomid = data.default_room_id;
//
//		reqBalance();//获取虎扑币
//		initThread();
//		initGiftList();
//		animationTool = new AnimationTool(this,0);
//	}
//
//	/**
//	 * 收到push消息更新礼物按钮
//	 */
//	private void updateGiftShow(Object obj,int i){
//		int len = layGift.getChildCount();
//		//		for(int i=0;i<len;i++){
//		GiftEntity ge=(GiftEntity)obj;
//
//		RelativeLayout giftview = (RelativeLayout)layGift.getChildAt(i);
//		TextView txtSwitcher = (TextView)giftview.getChildAt(0);
//		TextView  view =  (TextView)giftview.getChildAt(1);
//
//		int type=0;
//		//			giftList.get(i).gift_name="dddeee";//test
//		if(ge.gift_name != null && ge.gift_name.length()>0){
//			type = isChineseEnglish(ge.gift_name.charAt(0));
//		}
//
//		String bttxt=ge.gift_name;
//		if(type == 0 && ge.gift_name.length()>LETTER_MAX_LINE){
//			bttxt=ge.gift_name.substring(0, LETTER_MAX_LINE-1)+"\n"+ge.gift_name.substring(LETTER_MAX_LINE-1);
//		}else if(type != 0  && ge.gift_name.length()>CHAR_MAR_LINE){
//			bttxt=ge.gift_name.substring(0, CHAR_MAR_LINE-2)+"\n"+ge.gift_name.substring(CHAR_MAR_LINE-2);
//		}
//
//		view.setText(bttxt);
//		txtSwitcher.setText(ge.count+"");
//		//		}
//	}
//	private void initGiftList(){
//
//		layGift.setVisibility(View.VISIBLE);
//		layGift.removeAllViews();
//
//		int len=giftList.size();
//		for(int i=0;i<len;i++){
//			GiftEntity gift = giftList.get(i);
//			View giftview = LayoutInflater.from(this).inflate(R.layout.item_live_room_gift, null);
//			TextView view=(TextView)giftview.findViewById(R.id.btn_gift_green);
//			TextView txtSwitcher = (TextView)giftview.findViewById(R.id.gift_total_1);
//
//			int type=0;
//			//			gift.gift_name="dddeeerrr";//
//			if(gift.gift_name != null && gift.gift_name.length()>0){
//				type = isChineseEnglish(gift.gift_name.charAt(0));
//			}
//			String bttxt=gift.gift_name;
//			if(type == 0 && gift.gift_name.length()>LETTER_MAX_LINE){
//				bttxt=gift.gift_name.substring(0, LETTER_MAX_LINE-1)+"\n"+gift.gift_name.substring(LETTER_MAX_LINE-1);
//			}else if(type != 0 && gift.gift_name.length()>CHAR_MAR_LINE){
//				bttxt=gift.gift_name.substring(0, CHAR_MAR_LINE-2)+"\n"+gift.gift_name.substring(CHAR_MAR_LINE-2);
//			}
//
//			view.setText(bttxt);
//			view.setOnClickListener(new GiftClick(i));
//			txtSwitcher.setText(gift.count+"");
//			layGift.addView(giftview);
//			if(i==1){
//				view.setBackgroundResource(R.drawable.btn_gift_orange_selector);
//				view.setTextColor(this.getResources().getColor(R.color.txt_live_send_gift_bt_orange));
//				txtSwitcher.setTextColor(this.getResources().getColor(R.color.txt_live_send_gift_bt_orange));
//			}
//		}
//	}
//	public int isChineseEnglish(char c) {  
//		if (c >= 0 && c <= 9) {            
//			// 是数字  
//			return 4;//"是数字字符";  
//		} else if ((c >= 'a' && c <= 'z')) { 
//			// 是小写字母  
//			return 3;//"是小写字母";  
//		}else if ((c >= 'A' && c <= 'z')) {  
//			// 是大写字母  
//			return 2;//"是大写字母";  
//		} else if (Character.isLetter(c)) {   
//			// 是汉字  
//			return 0;//"是汉字字符";  
//		} else {                             
//			// 是特殊符号  
//			return 1;//"是特殊符号";  
//		}  
//	} 
//	class GiftClick implements OnClickListener {
//		int index;
//		GiftClick(int index){
//			this.index = index;
//		}
//		@Override
//		public void onClick(View v) {
//			checkSendGift(giftList.get(index));
//		}
//	}
//	void checkSendGift(GiftEntity gift){
//		//是否登录
//		if (mToken == null) {
//			showBindDialog(SharedPreferencesMgr.getString("dialogBtnText",
//					getString(R.string.bind_phone_dialog)));
//			return;
//		} 
//		//是否第一次送礼
//		if(!SharedPreferencesMgr.getBoolean("sendGift", false)){
//			firstSendNotice(gift);
//			SharedPreferencesMgr.setBoolean("sendGift", true);
//			return;
//		}
//
//		//		hupuDollor=0;//test
//		//虎扑币是否够
//		if(gift.price > hupuDollor){
//			hupuDollorLessNotice();
//			return;
//		}
//		add2SendList(gift.gift_id,"",giftList.indexOf(gift));
//	}
//	private void add2SendList(int giftid,String uid,int giftindex){
//		int color=0;
//		if(giftindex == 0){
//			color=R.color.txt_live_send_gift_bt_green;
//		}else {
//			color=R.color.txt_live_send_gift_bt_orange;
//		}
//
//		animationTool.showAnimation(giftList.get(giftindex).gift_name,color);
//		this.hupuDollor-=giftList.get(giftindex).price;
//		giftList.get(giftindex).count+=giftList.get(giftindex).price;
//
//
//		this.sendMessage(giftindex, giftList.get(giftindex));
//
//
//		GiftReqDataEntity object = new GiftReqDataEntity();
//		object.setGiftid(giftid);
//		object.setUid(uid);
//		this.sendlist.add(object);
//
//		//		//test 
//		//		ArrayList<GiftEntity> list = new ArrayList<GiftEntity>();
//		//		for(int i=0;i<giftList.size();i++){
//		//			GiftEntity gift  = new GiftEntity();
//		//			gift.gift_id = giftList.get(i).gift_id;
//		//			gift.gift_name = giftList.get(i).gift_name;
//		//			gift.count = giftList.get(i).count+100;
//		//			list.add(gift);
//		//		}
//		//		pushUpdataGift(list);
//	}
//	private  String getSendList(){
//		String result = "";
//		JSONObject jsonObject;
//		JSONArray jsonArray = new JSONArray();
//
//		for (int i = 0; i < sendlist.size(); i++) {
//			jsonObject = new JSONObject();
//			try {
//				//jsonObject.put(leaguesEntities.get(i).lid + "", 1);
//				//注释说明：之前提交是否关注的信息！
//				jsonObject.put("giftid",sendlist.get(i).getGiftid());
//				jsonObject.put("uid",sendlist.get(i).getUid());
//				jsonArray.put(jsonObject);
//				// Log.i("papa", jsonObject.toString());
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		result = jsonArray.toString();
//		sendlist.clear();
//		return result;
//	}
//	private void pushUpdataGift(ArrayList<GiftEntity> glist){
//		for(int i=0;i<glist.size();i++){
//			GiftEntity ge = glist.get(i);
//			GiftEntity localGe = giftList.get(i);
//
//
//			dealPushGiftCount(ge,localGe,i);
//			localGe.gift_name=ge.gift_name;
//			localGe.price = ge.price;
//		}
//	}
//	int push_gift_count_add_times=10;//1秒钟加的次数
//	int push_gift_count_add_min=2;//push的count最小多少无需累加
//	Thread gift_add_Thread;
//	private void dealPushGiftCount(GiftEntity ge ,final GiftEntity localGe,final int i){
//		localGe.push_count = ge.count;
//		if((!localGe.gift_name.equals(ge.gift_name)) //名字修改了
//				|| (localGe.gift_name.equals(ge.gift_name) && ge.count -localGe.count >=0 && ge.count -localGe.count<=push_gift_count_add_min)){//名字没改但是增加的数量小于push_gift_count_add_min
//			localGe.count=ge.count;
//			this.sendMessage(i, localGe);
//		}else if(ge.count -localGe.count > push_gift_count_add_min){
//			localGe.push_count_interadd = (localGe.push_count-localGe.count)/push_gift_count_add_times;
//			//			localGe.count += localGe.push_count_interadd;
//			if(gift_add_Thread == null){
//				gift_add_Thread = new Thread(new Runnable() {
//					@Override
//					public void run() {
//						while(!exit){
//							if(localGe.count<localGe.push_count){
//								localGe.count += localGe.push_count_interadd;
//								if(localGe.count > localGe.push_count){
//									localGe.count = localGe.push_count;
//								}
//								sendMessage(i, localGe);
//							}
//							try {
//								Thread.sleep(1000/push_gift_count_add_times);
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
//					}
//				});
//				gift_add_Thread.start();
//			}
//
//		}
//	}
//	void reqSendGift(){
//		if(sendlist.size()==0)
//		{
//			return;
//		}
//		initParameter();
//		mParams.put("gid", gid + "");
//		mParams.put("roomid", roomid+"");
//		mParams.put("type", tag);
//		mParams.put("data", getSendList());
//		sendRequest(HuPuRes.REQ_METHOD_GET_PLAY_LIVE_SEND_GIFT, mParams,
//				new HupuHttpHandler(NBAGameActivity.this), false);
//	}

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
				new HupuHttpHandler(NBAGameActivity.this), false);
	}

//	private void initThread() {
//		HandlerThread thread = new HandlerThread("sendGiftService");
//		thread.start();
//		Looper looper = thread.getLooper();
//		if(mHandler == null){
//			mHandler = new Handler(looper);
//		}
//		mHandler.postDelayed(checkSer, interval);
//	}
//	private Runnable checkSer = new Runnable() {
//		@Override
//		public void run() {
//			reqSendGift();
//			if(!exit){
//				mHandler.postDelayed(checkSer, interval);
//			}
//		}
//	};
//	public void sendMessage(int what, Object object) {
//		Message msg = new Message();
//		msg.what = what;
//		msg.obj = object;
//		giftHandler.sendMessage(msg);
//	}
//
//	Handler giftHandler = new Handler(){
//		public void handleMessage(Message msg) {
//			//			switch (msg.what) {
//			//			case Msg_Show_Gift:
//			//				HupuLog.e(tag, "handler-------------Msg_Show_Gift");
//
//			updateGiftShow(msg.obj,msg.what);
//			//				break;
//
//			//			}
//		}
//	};
//	private void quitLive(){
//		exit=true;
//		if (animationTool !=null) {
//			animationTool.destroy();
//			animationTool=null;
//		}
//
//	}
	protected void onDestroy() {
		//		// TODO Auto-generated method stub
		super.onDestroy();
//		quitLive();
		roomid = -1;
	}
//	private void reqBalance() {
//		if (mToken != null) {
//			initParameter();
//			mParams.put("token", mToken);
//			sendRequest(HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_BALANCE, mParams,
//					new HupuHttpHandler(this), false);
//		}
//	}
//	private void firstSendNotice(final GiftEntity gift)
//	{	
//		String content = String.format(getString(R.string.live_first_send_gift_notice), gift.price);
//		tipsDialog = new GiftTipsDialog(this, new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				tipsDialog.dismiss();
//				// TODO Auto-generated method stubW
//				if(v.getId()==GiftTipsDialog.BTN_OK_ID)//确定
//				{
//					if(gift.price > hupuDollor){
//						hupuDollorLessNotice();
//					}else{
//						add2SendList(gift.gift_id,"",giftList.indexOf(gift));
//					}
//				}
//
//			}
//		},content, GiftTipsDialog.DEFAULT);
//		tipsDialog.initData(content, TipsDialog.DEFAULT);
//		tipsDialog.initBtn(getString(R.string.title_confirm), getString(R.string.cancel));
//		tipsDialog.show();
//	}
//	private void hupuDollorLessNotice()
//	{	
//		String content = this.getString(R.string.live_send_gift_hupudollr_insufficent);
//		tipsDialog = new GiftTipsDialog(this, new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				tipsDialog.dismiss();
//				// TODO Auto-generated method stubW
//				if(v.getId()==GiftTipsDialog.BTN_OK_ID)//确定
//				{
//					Intent it = new Intent(NBAGameActivity.this,HupuDollorOrderActivity.class);
//					it.putExtra("hupuDollor_balance", hupuDollor+"");
//					startActivityForResult(it, REQ_GO_POST_ORDER);
//				}
//
//			}
//		},content, GiftTipsDialog.DEFAULT);
//		tipsDialog.initData(content, TipsDialog.DEFAULT);
//		tipsDialog.initBtn(getString(R.string.title_confirm), getString(R.string.cancel));
//		tipsDialog.show();
//	}
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		HupuLog.e("papa", "resultId==" + resultCode);
//		if (requestCode == REQ_GO_POST_ORDER) {
//			if (resultCode == RESULT_OK) {
//				this.reqBalance();
//
//			}
//		}
//	}

	class EpandCallBack implements EpandAnimationCallBack
	{
		@Override
		public void callBack(int type) {
			switchRoomStatus(INDEX_LIVE,livefirstIn);
		}
	}
}
