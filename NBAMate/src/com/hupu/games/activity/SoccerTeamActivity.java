/**
 * 
 */
package com.hupu.games.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.hupu.games.data.BaseEntity;
import com.hupu.games.data.FollowResp;
import com.hupu.games.data.game.football.SoccerPlayerReq;
import com.hupu.games.data.game.football.SoccerTeamReq;
import com.hupu.games.data.game.football.SoccerTeamScheduleReq;
import com.hupu.games.db.HuPuDBAdapter;
import com.hupu.games.fragment.NbaTeamScheduleFragment;
import com.hupu.games.fragment.SoccerPlayerFragment;
import com.hupu.games.fragment.SoccerTeamInfoFragment;
import com.hupu.games.fragment.SoccerTeamScheduleFragment;
import com.hupu.http.HupuHttpHandler;

/**
 * @author panyongjun
 * 
 * 足球球队球员页面
 */
public class SoccerTeamActivity extends HupuBaseActivity {

	/**球队赛程*/
	SoccerTeamInfoFragment mTeamFragment;
	/**球员信息*/
	SoccerPlayerFragment mPlayerFragment;

	SoccerTeamScheduleFragment mScheduleFragment;
	/**球队id*/
	private int i_tid;
	/**是否得到赛程数据*/
	boolean bGotProgram;

	/**是否得到球员数据*/
	boolean bGotPlayerData;

	TextView mTxtTitle;

	String mTeamName;

	int curFrameIndex;
	
	final static int FRAME_PROGRAM = 1;

	final static int FRAME_PLAYER = 3;

	final static int FRAME_FULL_PROGRAM = 2;

	ImageButton btnTeam;

	ImageButton btnPlayer;
	/**联赛标签*/
    String mTag;
    
	Bundle b;

	TextView followBtn;

	HuPuDBAdapter mDBAdapter;

	boolean isFollow;

	private final int DIALOG_NOTIFY = 1314;
	
	private final int DIALOG_FOLLOW_CANCEL_NOTIFY = 1315;

	private int i_lid;
	
	private boolean bGotFullProgram;
	
	private View tabs;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (savedInstanceState != null)
			savedInstanceState.clear();
		super.onCreate(savedInstanceState);
		i_tid = getIntent().getIntExtra("tid", 0);
		mTag= getIntent().getStringExtra("tag");
		i_lid =findLid( mTag) ;
		b = new Bundle();
		b.putInt("tid", i_tid);
		b.putString("tag", mTag);
		init();
		treatClickEvent(R.id.btn_team);
	}

	/**
	 * 
	 * */
	private void init() {
		setContentView(R.layout.layout_soccer_team_player);

		mTxtTitle = (TextView) findViewById(R.id.txt_title);
		followBtn = (TextView) findViewById(R.id.btn_follow);
		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.btn_follow);
		setOnClickListener(R.id.btn_team);
		setOnClickListener(R.id.btn_player);

		btnTeam = (ImageButton) findViewById(R.id.btn_team);
		btnPlayer = (ImageButton) findViewById(R.id.btn_player);
		tabs = findViewById(R.id.layout_bottom);
		initParameter();
		mParams.add("tid", "" + i_tid);
		mParams.add("lid", "" + i_lid);
		mDBAdapter = new HuPuDBAdapter(this);
		isFollow = mDBAdapter.isFollowTeam(i_lid, i_tid);
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
				mTeamFragment = new SoccerTeamInfoFragment();
				mTeamFragment.setArguments(b);
			}
			fragment = mTeamFragment;
			if (!bGotProgram)
				reqTeamProgram();
			if(mTeamName!=null)
				mTxtTitle.setText(mTeamName);
			break;
		case FRAME_PLAYER:

			followBtn.setVisibility(View.GONE);
			if (mPlayerFragment == null)
				mPlayerFragment = new SoccerPlayerFragment();
			fragment = mPlayerFragment;
			reqPlayerProgram();
			if(mTeamName!=null)
				mTxtTitle.setText(mTeamName + "队球员");
			break;
		case FRAME_FULL_PROGRAM:
			followBtn.setVisibility(View.GONE);
			if (mScheduleFragment == null) {
				mScheduleFragment = new SoccerTeamScheduleFragment();
				mScheduleFragment.setArguments(b);
			}
			fragment = mScheduleFragment;
			if (!bGotFullProgram)
				reqFullProgram();
			mTxtTitle.setText(mTeamName + "赛程");
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
		}
	}

	/** 请求球队最近10场比赛 */
	private void reqTeamProgram() {

		sendRequest(HuPuRes.REQ_METHOD_GET_SOCCER_TEAM_PROGRAM,mTag, mParams,
				new HupuHttpHandler(this),false);
	}

	/**
	 * 请求球队中球员列表
	 */

	private void reqPlayerProgram() {
		sendRequest(HuPuRes.REQ_METHOD_GET_SOCCER_TEAM_ROSTER,mTag, mParams,
				new HupuHttpHandler(this),false);

	}
	/**
	 * 请求球队完整赛程
	 */
	private void reqFullProgram() {
		sendRequest(HuPuRes.REQ_METHOD_GET_SOCCER_TEAM_FULL_PROGRAM, mTag,mParams,
				new HupuHttpHandler(this),false);
	}
	
	public void showFullProgram() {
		showFrame(FRAME_FULL_PROGRAM);
	}
	
	
	@Override
	public void onErrMsg(String msg, int type) {
		// TODO Auto-generated method stub
		super.onErrMsg(msg, type);
		HupuLog.e("papa", "type=="+type);
		noData();
	}
	
	private void  noData(){
		switch (curFrameIndex) {
		case FRAME_PROGRAM:
			if (mTeamFragment == null) {
				mTeamFragment = new SoccerTeamInfoFragment();
			}
			mTeamFragment.setNoData();
			break;
		case FRAME_PLAYER:
			if (mPlayerFragment == null) {
				mPlayerFragment = new SoccerPlayerFragment();
			}
			mPlayerFragment.setNoData();
		default:
			break;
		}
	}

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		if (o == null)
			return;
		
		switch (methodId) {

		case HuPuRes.REQ_METHOD_GET_SOCCER_TEAM_PROGRAM:
			mTeamFragment.setData((SoccerTeamReq) o);
			mTeamName=((SoccerTeamReq)o).name;
			if(mTeamName!=null)
				mTxtTitle.setText(mTeamName);
			bGotProgram = true;
			break;

		case HuPuRes.REQ_METHOD_GET_SOCCER_TEAM_ROSTER:
			mPlayerFragment.setData((SoccerPlayerReq) o,mTag);
			if(mTeamName==null)
				mTeamName=((SoccerPlayerReq)o).name;
			if(mTeamName!=null)
				mTxtTitle.setText(mTeamName + "队球员");
			break;

		case HuPuRes.REQ_METHOD_FOLLOW_ONE_TEAM:
		case HuPuRes.REQ_METHOD_FOLLOW_ONE_TEAM_CANCEL:
			FollowResp resp = (FollowResp) o;
			if (resp == null || resp.i_success == 0) {
				// 提交失败
				showToast("关注" + mTeamName + "失败");
				isFollow = !isFollow;
				setFollowBtn();
			} else {
				if (methodId == HuPuRes.REQ_METHOD_FOLLOW_ONE_TEAM) {
					if(mTeamName!=null)
						showToast("关注成功，您将收到" + mTeamName + "的相关通知");
					mDBAdapter.setFollowTeam(i_lid, i_tid, 1);
				} else if (methodId == HuPuRes.REQ_METHOD_FOLLOW_ONE_TEAM_CANCEL) {
					showToast("已取消关注" + mTeamName);
					mDBAdapter.setFollowTeam(i_lid, i_tid, 0);
				}
			}
			break;
		case HuPuRes.REQ_METHOD_GET_SOCCER_TEAM_FULL_PROGRAM:
			mScheduleFragment.setData((SoccerTeamScheduleReq)o);
			if(mTeamName==null)
				mTeamName=((SoccerTeamScheduleReq)o).name;
			if(mTeamName!=null)
				mTxtTitle.setText(mTeamName + "赛程");
			break;
		}

	}

	@Override
	public void clickNegativeButton(int dialogId) {
		super.clickNegativeButton(dialogId);
//		sendUmeng(HuPuRes.UMENG_EVENT_NBA_TEAMS,
//				HuPuRes.UMENG_KEY_TEAMS, HuPuRes.UMENG_VALUE_UNFOLLOW_CANCEL);
		if (mDialog != null)
			mDialog.cancel();
	}

	@Override
	public void clickPositiveButton(int dialogId) {
		super.clickPositiveButton(dialogId);
		switch (dialogId) {

		case DIALOG_NOTIFY:
			mApp.setNotify(true);
			followTeam(i_lid,i_tid, (byte) 1);
			isFollow = !isFollow;
			setFollowBtn();
			break;
		case DIALOG_FOLLOW_CANCEL_NOTIFY:
//			sendUmeng(HuPuRes.UMENG_EVENT_NBA_TEAMS,
//					HuPuRes.UMENG_KEY_TEAMS, HuPuRes.UMENG_VALUE_UNFOLLOW_CONFIRM);
			followTeam(i_lid,i_tid, (byte) 0);
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
					String.format(notify, mTeamName), TOW_BUTTONS,
					R.string.follow_cancel, R.string.follow_continue);
			return;
		} else {
			if (mApp.needNotify) {
//				sendUmeng(HuPuRes.UMENG_EVENT_NBA_TEAMS,
//						HuPuRes.UMENG_KEY_TEAMS, HuPuRes.UMENG_VALUE_FOLLOW);
				followTeam(i_lid,i_tid, (byte) 1);
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
		}
	}

	public void switchToGameActivity(int gid,String tag,int lid)
	{
		Intent in =new Intent(this, FootballGameActivity.class);
		in.putExtra("gid", gid);
		in.putExtra("tag", tag);
		in.putExtra("lid", lid);
		startActivity(in);
	}
}
