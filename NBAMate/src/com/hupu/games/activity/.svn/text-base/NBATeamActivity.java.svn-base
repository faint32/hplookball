/**
 * 
 */
package com.hupu.games.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hupu.games.HuPuApp;
import com.hupu.games.R;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.FollowResp;
import com.hupu.games.data.game.basketball.NbaPlayersDataReq;
import com.hupu.games.data.game.basketball.NbaTeamPlayerReq;
import com.hupu.games.data.game.basketball.NbaTeamReq;
import com.hupu.games.data.game.basketball.NbaTeamScheduleReq;
import com.hupu.games.db.HuPuDBAdapter;
import com.hupu.games.fragment.NbaPlayersDataFragment;
import com.hupu.games.fragment.NbaTeamInfoFragment;
import com.hupu.games.fragment.NbaTeamPlayerFragment;
import com.hupu.games.fragment.NbaTeamScheduleFragment;
import com.hupu.http.HupuHttpHandler;

/**
 * @author panyongjun
 * 
 *  NBA球队球员页面
 */
public class NBATeamActivity extends HupuBaseActivity {

	NbaTeamInfoFragment mTeamFragment;
	/***/
	NbaTeamPlayerFragment mTeamPlayerFragment;
	/**NBA球队完整赛程*/
	NbaTeamScheduleFragment mScheduleFragment;
	/***/
	NbaPlayersDataFragment mPlayerDataFragment;
	private int i_tid;

	boolean bGotProgram;

	boolean bGotFullProgram;

	boolean bGotPlayerData;

	TextView mTxtTitle;

	String team;

	int curFrameIndex;
	final static int FRAME_PROGRAM = 1;

	final static int FRAME_FULL_PROGRAM = 2;

	final static int FRAME_PLAYER = 3;

	final static int FRAME_DATA = 4;

	ImageButton btnTeam;

	ImageButton btnPlayer;

	ImageButton btnData;

	Bundle b;

	TextView followBtn;

	HuPuDBAdapter mDBAdapter;

	boolean isFollow;

	private final int DIALOG_NOTIFY = 1314;
	private final int DIALOG_FOLLOW_CANCEL_NOTIFY = 1315;

	View tabs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (savedInstanceState != null)
			savedInstanceState.clear();
		super.onCreate(savedInstanceState);
		i_tid = getIntent().getIntExtra("tid", 0);
		team = HuPuApp.getTeamData(i_tid).str_name;
		b = new Bundle();
		b.putInt("tid", i_tid);

		init();
		treatClickEvent(R.id.btn_team);
	}

	/**
	 * 
	 * */
	private void init() {
		setContentView(R.layout.layout_team_player);

		mTxtTitle = (TextView) findViewById(R.id.txt_title);
		followBtn = (TextView) findViewById(R.id.btn_follow);
		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.btn_follow);
		setOnClickListener(R.id.btn_team);
		setOnClickListener(R.id.btn_player);
		setOnClickListener(R.id.btn_data);
		btnTeam = (ImageButton) findViewById(R.id.btn_team);
		btnPlayer = (ImageButton) findViewById(R.id.btn_player);
		btnData = (ImageButton) findViewById(R.id.btn_data);
		tabs = findViewById(R.id.layout_bottom);
		initParameter();
		mParams.add("tid", "" + i_tid);
		mDBAdapter = new HuPuDBAdapter(this);
		isFollow = mDBAdapter.isFollowTeam(1, i_tid);
		setFollowBtn();
	}

	private void showFrame(int index) {
		Fragment fragment = null;
		HupuLog.d("showFrame", "index=" + index);
		if (curFrameIndex == index)
			return;

		switch (index) {
		case FRAME_PROGRAM:
			followBtn.setVisibility(View.VISIBLE);
			if (mTeamFragment == null) {
				mTeamFragment = new NbaTeamInfoFragment();
				mTeamFragment.setArguments(b);
			}
			fragment = mTeamFragment;
			if (!bGotProgram)
				reqTeamProgram();
			mTxtTitle.setText(team);
			break;
		case FRAME_PLAYER:

			followBtn.setVisibility(View.GONE);
			if (mTeamPlayerFragment == null)
				mTeamPlayerFragment = new NbaTeamPlayerFragment();
			fragment = mTeamPlayerFragment;
			reqPlayerProgram();
			mTxtTitle.setText(team + "球员");
			break;
		case FRAME_DATA:
			followBtn.setVisibility(View.GONE);
			//
			if (mPlayerDataFragment == null) {
				mPlayerDataFragment = new NbaPlayersDataFragment();
				mPlayerDataFragment.setArguments(b);
			}
			// mPlayerDataFragment = new NbaPlayersDataFragment();
			// mPlayerDataFragment.setArguments(b);
			fragment = mPlayerDataFragment;
			if (!bGotPlayerData)
				reqPlayersData();
			mTxtTitle.setText(team + "数据");
			break;
		case FRAME_FULL_PROGRAM:
			followBtn.setVisibility(View.GONE);
			if (mScheduleFragment == null) {
				mScheduleFragment = new NbaTeamScheduleFragment();
				mScheduleFragment.setArguments(b);
			}
			fragment = mScheduleFragment;
			if (!bGotFullProgram)
				reqFullProgram();
			mTxtTitle.setText(team + "赛程");
			break;
		}
		setBackgound(index);
		curFrameIndex = index;
		replaceContent(fragment);
	}

	private void replaceContent(Fragment fragment) {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.frame_content, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (curFrameIndex == FRAME_FULL_PROGRAM)
				showFrame(FRAME_PROGRAM);
			else
				finish();
			return true;
		}
		return false;
	}

	
	@SuppressLint("NewApi")
	@Override
	public void treatClickEvent(int id) {

		super.treatClickEvent(id);
		switch (id) {
		case R.id.btn_back:
			if (curFrameIndex == FRAME_FULL_PROGRAM)
				showFrame(FRAME_PROGRAM);
			else
				finish();
			break;
		case R.id.btn_follow:
			setFollow();
			break;
		case R.id.btn_team:
			showFrame(FRAME_PROGRAM);
			break;
		case R.id.btn_player:
			showFrame(FRAME_PLAYER);
			break;
		case R.id.btn_data:
			showFrame(FRAME_DATA);
			break;
		}
	}

	/** 请求球队最近10场比赛 */
	private void reqTeamProgram() {

		sendRequest(HuPuRes.REQ_METHOD_GET_NBA_TEAM_PROGRAM, mParams,
				new HupuHttpHandler(this));
	}

	/**
	 * 请求球队中球员列表
	 */

	private void reqPlayerProgram() {
		initParameter();
		mParams.add("tid", "" + i_tid);
		sendRequest(HuPuRes.REQ_METHOD_GET_NBA_TEAM_ROSTER, mParams,
				new HupuHttpHandler(this));

	}

	private void reqFullProgram() {
		sendRequest(HuPuRes.REQ_METHOD_GET_NBA_TEAM_FULL_PROGRAM, mParams,
				new HupuHttpHandler(this));
	}

	private void reqPlayersData() {

		sendRequest(HuPuRes.REQ_METHOD_GET_NBA_PLAYER_DATA, mParams,
				new HupuHttpHandler(this));
	}

	public void showFullProgram() {
		showFrame(FRAME_FULL_PROGRAM);
	}

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		if (o == null)
			return;
		switch (methodId) {

		case HuPuRes.REQ_METHOD_GET_NBA_TEAM_PROGRAM:
			mTeamFragment.setData((NbaTeamReq) o);
			bGotProgram = true;
			break;
		case HuPuRes.REQ_METHOD_GET_NBA_TEAM_FULL_PROGRAM:
			mScheduleFragment.setData((NbaTeamScheduleReq) o);
			break;
		case HuPuRes.REQ_METHOD_GET_NBA_TEAM_ROSTER:
			mTeamPlayerFragment.setData((NbaTeamPlayerReq) o);
			break;
		case HuPuRes.REQ_METHOD_GET_NBA_PLAYER_DATA:
			mPlayerDataFragment.setData((NbaPlayersDataReq) o);
			break;
		case HuPuRes.REQ_METHOD_FOLLOW_ONE_TEAM:
		case HuPuRes.REQ_METHOD_FOLLOW_ONE_TEAM_CANCEL:
			FollowResp resp = (FollowResp) o;
			if (resp == null || resp.i_success == 0) {
				// 提交失败
				showToast("关注" + team + "失败");
				isFollow = !isFollow;
				setFollowBtn();
			} else {
				if (methodId == HuPuRes.REQ_METHOD_FOLLOW_ONE_TEAM) {
					showToast("关注成功，您将收到" + team + "的相关通知");
					mDBAdapter.setFollowTeam(1, i_tid, 1);
				} else if (methodId == HuPuRes.REQ_METHOD_FOLLOW_ONE_TEAM_CANCEL) {
					showToast("已取消关注" + team);
					mDBAdapter.setFollowTeam(1, i_tid, 0);
				}
			}
			break;
		}

	}

	@Override
	public void clickNegativeButton(int dialogId) {
		super.clickNegativeButton(dialogId);
		sendUmeng(HuPuRes.UMENG_EVENT_NBA_TEAMS,
				HuPuRes.UMENG_KEY_TEAMS, HuPuRes.UMENG_VALUE_UNFOLLOW_CANCEL);
		if (mDialog != null)
			mDialog.cancel();
	}

	@Override
	public void clickPositiveButton(int dialogId) {
		super.clickPositiveButton(dialogId);
		switch (dialogId) {

		case DIALOG_NOTIFY:
			mApp.setNotify(true);
			followTeam(1,i_tid, (byte) 1);
			isFollow = !isFollow;
			setFollowBtn();
			break;
		case DIALOG_FOLLOW_CANCEL_NOTIFY:
			sendUmeng(HuPuRes.UMENG_EVENT_NBA_TEAMS,
					HuPuRes.UMENG_KEY_TEAMS, HuPuRes.UMENG_VALUE_UNFOLLOW_CONFIRM);
			followTeam(1,i_tid, (byte) 0);
			isFollow = !isFollow;
			setFollowBtn();
			break;
		}
		// 打开通知
		if (mDialog != null)
			mDialog.cancel();
	}
	

	final static String notify = "取消关注后，您不会再收到%s队的所有相关通知，确认取消？";

	private void setFollow() {

		if (isFollow) {
			// 取消关注
			// followTeam(i_tid, (byte) 1);
			showCustomDialog(DIALOG_FOLLOW_CANCEL_NOTIFY,
					String.format(notify, team), TOW_BUTTONS,
					R.string.follow_cancel, R.string.follow_continue);
			return;
		} else {
			if (mApp.needNotify) {
				sendUmeng(HuPuRes.UMENG_EVENT_NBA_TEAMS,
						HuPuRes.UMENG_KEY_TEAMS, HuPuRes.UMENG_VALUE_FOLLOW);
				followTeam(1,i_tid, (byte) 1);
			} else {
				//
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

	/** 设置关注比赛的UI状态 */
	private void setFollowBtn() {

		if (isFollow) {
			followBtn.setBackgroundResource(R.drawable.btn_nav_down);
			followBtn.setText("已关注");
		} else {
			followBtn.setBackgroundResource(R.drawable.btn_nav_up);
			followBtn.setText("关注");
		}

	}

	/** 点击后的按钮背景变化 */
	private void setBackgound(int index) {
		int color = getResources().getColor(R.color.transform);
		switch (curFrameIndex) {// 点击前

		case FRAME_PROGRAM:
			btnTeam.setBackgroundColor(color);
			btnTeam.setImageResource(R.drawable.btn_team_up);
			break;
		case FRAME_FULL_PROGRAM:
			tabs.setVisibility(View.VISIBLE);
			break;
		case FRAME_PLAYER:
			btnPlayer.setBackgroundColor(color);
			btnPlayer.setImageResource(R.drawable.btn_player_up);
			break;
		case FRAME_DATA:
			btnData.setBackgroundColor(color);
			btnData.setImageResource(R.drawable.btn_data_up);
			break;

		}

		switch (index) {// 点击后
		case FRAME_FULL_PROGRAM:
			tabs.setVisibility(View.GONE);
			break;
		case FRAME_PROGRAM:
			sendUmeng(HuPuRes.UMENG_EVENT_NBA_TEAMS, HuPuRes.UMENG_KEY_TEAMS,
					HuPuRes.UMENG_VALUE_TEAMS_TAB);
			
			btnTeam.setBackgroundResource(R.drawable.bg_bottom_hover);
			btnTeam.setImageResource(R.drawable.btn_team_down);
			break;
		case FRAME_PLAYER:
			sendUmeng(HuPuRes.UMENG_EVENT_NBA_TEAMS, HuPuRes.UMENG_KEY_ROSTER,
					HuPuRes.UMENG_VALUE_ROSTER_TAB);
			
			btnPlayer.setBackgroundResource(R.drawable.bg_bottom_hover);
			btnPlayer.setImageResource(R.drawable.btn_player_down);
			break;
		case FRAME_DATA:
			sendUmeng(HuPuRes.UMENG_EVENT_NBA_TEAMS, HuPuRes.UMENG_KEY_STATS,
					HuPuRes.UMENG_VALUE_STATS_TAB);
			
			btnData.setBackgroundResource(R.drawable.bg_bottom_hover);
			btnData.setImageResource(R.drawable.btn_data_down);
			break;
		}
	}

}
