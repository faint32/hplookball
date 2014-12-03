package com.hupu.games.fragment;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;

import com.hupu.games.R;
import com.hupu.games.activity.FootballGameActivity;
import com.hupu.games.adapter.SoccerGamesListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.BaseGameEntity;
import com.hupu.games.data.game.football.FootballLeagueResp;
import com.hupu.games.data.game.football.ScoreboardEntity;
import com.hupu.games.data.game.football.SoccerGamesBlock;

@SuppressLint("ValidFragment")
public class SoccerGamesFragment extends BaseGameFragment {

	private SoccerGamesListAdapter mListAdapter;

	private ArrayList<SoccerGamesBlock> mBlockList;

	private int mLid;

	boolean bFirst=true;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTag = getArguments().getString("tag");
		mLid = getArguments().getInt("lid");
		mScheduleHandler = new ScheduleHandler();
	}

	@Override
	public void setAdapter() {
		if (mListAdapter == null) {
			Click click = new Click();
			mListAdapter = new SoccerGamesListAdapter(mAct, click);
			//mPullListener = new PullListener();
		} else {
			if (mToday != null)
				mProgressBar.setVisibility(View.GONE);
		}
		listview.setAdapter(mListAdapter);
		listview.setOnItemClickListener(new ListClick());
		if(iPos>0)
			listview.setSelectionFromTop(iPos, iTop);
		
	}

	@Override
	public void reqDefaultData() {
		req(0, 0, HuPuRes.REQ_METHOD_FOOTBALL_LEAGUE);
	}
	
	private void reqRefreshData(){
		req(0, 0, HuPuRes.REQ_METHOD_FOOTBALL_LEAGUE_REFRESH);
	}

	public void prev() {
		req(preDate, -1, HuPuRes.REQ_METHOD_FOOTBALL_LEAGUE_PREV);
	}

	public void next() {
		req(nextDate, 1, HuPuRes.REQ_METHOD_FOOTBALL_LEAGUE_NEXT);
	}

	private void calData(int methodId, FootballLeagueResp entity) {
		if (mBlockTypeList == null) {
			mDateList = new ArrayList<String>();
			mBlockTypeList = new ArrayList<String>();
			mBlockList = new ArrayList<SoccerGamesBlock>();
		}
		if (mToday == null) {
			mToday = "" + entity.current;
			mMinDate = entity.min;
			mMaxDate = entity.max;
		}

		if (methodId == HuPuRes.REQ_METHOD_FOOTBALL_LEAGUE
				|| methodId == HuPuRes.REQ_METHOD_FOOTBALL_LEAGUE_NEXT ) {
			if (methodId == HuPuRes.REQ_METHOD_FOOTBALL_LEAGUE && mBlockTypeList.size() > 0/*)|| (methodId == HuPuRes.REQ_METHOD_FOOTBALL_LEAGUE_REFRESH&& mBlockTypeList.size() > 0)*/) {
				HupuLog.e("papa", "---------覆盖==");
				// 定时刷新的数据,直接覆盖
				for (SoccerGamesBlock block : entity.mBlockList) {
					int index = mBlockTypeList.indexOf(block.mType);
					//HupuLog.e("papa", "---index="+index+"----block="+block.mDay+"----day="+mBlockList.get(index).mDay);
					if (index > -1) {
						mBlockList.set(index, block);
					}
				}
				mListReqQue.remove("" + 0);
			} else {
				if (methodId == HuPuRes.REQ_METHOD_FOOTBALL_LEAGUE) {
					if (mBlockTypeList != null)
						mBlockTypeList.clear();
					if (mBlockList != null) {
						mBlockList.clear();
					}
				}
				mDateList.addAll(entity.mDays);
				mBlockTypeList.addAll(entity.mBlockTypes);
				mBlockList.addAll(entity.mBlockList);
				if (preDate == 0 && entity.firstDay>0) {
					preDate = entity.firstDay -1;
					mListReqQue.remove("" + 0);
				} else
					mListReqQue.remove("" + nextDate);
				if (entity.lastDay+1 > nextDate)
					nextDate = entity.lastDay+1;
					//nextDate = getDate(entity.lastDay,1);
			}

		} else if (methodId == HuPuRes.REQ_METHOD_FOOTBALL_LEAGUE_PREV) {
			mDateList.addAll(0, entity.mDays);
			mBlockTypeList.addAll(0,entity.mBlockTypes);
			mBlockList.addAll(0, entity.mBlockList);
			mListReqQue.remove("" + preDate);
			if (entity.firstDay-1 < preDate || preDate == 0)
				preDate = entity.firstDay - 1;
		}else if (methodId == HuPuRes.REQ_METHOD_FOOTBALL_LEAGUE_REFRESH && mBlockList.size() > 0) {
			// 定时刷新的数据,直接覆盖
			for (SoccerGamesBlock block : entity.mBlockList) {
				int index = mBlockTypeList.indexOf(block.mType);
				//HupuLog.e("papa", "---index="+index+"----block="+mBlockTypeList.get(index)+"----day="+mBlockList.get(index).mType +"-----typesize="+mBlockTypeList.size()+"-----listsize="+mBlockList.size());
				if (index > -1) {
					mBlockList.set(index, block);
				}
			}
			mListReqQue.remove("" + 0);
		}
		listview.setPullRefreshEnable(preDate>mMinDate);
		listview.setPullLoadEnable(nextDate<mMaxDate);
	}


	/***/
	public void setData(int methodId, FootballLeagueResp resp) {
		//滑到顶部加载前面日期其实是无数据的，。所以不要去更新列表    的容错
		listview.stopRefresh();
		if (resp.mDays == null) 
			return;
		calData(methodId, resp);
		mAct.checkToken(resp.isLogin);
		mListAdapter.setData(mBlockList);
		if (methodId == HuPuRes.REQ_METHOD_FOOTBALL_LEAGUE || methodId == HuPuRes.REQ_METHOD_FOOTBALL_LEAGUE_REFRESH) {
			HupuLog.d("fb","setData update");
			int index = mDateList.indexOf(mToday);
			if (index > -1) {
				// mAct.joinRoom();// 如果是比赛日，开始socket连接
			}
			checkFresh(resp.refresh_time);
			//初次需要设置锚点
			if (methodId == HuPuRes.REQ_METHOD_FOOTBALL_LEAGUE) {
				listview.setSelectionFromTop(resp.anchorIndex, headerHeight);
			}
			if (methodId == HuPuRes.REQ_METHOD_FOOTBALL_LEAGUE_REFRESH && bFirst) {
				listview.setSelectionFromTop(resp.anchorIndex, headerHeight);
				bFirst=false;
			}
			
		}
		else if (methodId == HuPuRes.REQ_METHOD_FOOTBALL_LEAGUE_PREV)
		{
			int block=resp.mBlockList.size();
			int items=resp.total+block +1;
				listview.setSelectionFromTop(items, headerHeight);
		}

	}

	/** 推送过来的数据 */
	public void updateData(SoccerGamesBlock resp) {
		int index = mDateList.indexOf(mToday);
		SoccerGamesBlock older = mBlockList.get(index);
		if (older != null) {
			int oldIndex = 0;
			ScoreboardEntity olderEntity = null;
			int position = 0;
			for (ScoreboardEntity entity : resp.mGames) {
				// 实时推送数据会引起比赛的排序顺序变化，所以需要定位上次保存的比赛的位置；
				oldIndex = older.mIds.indexOf(entity.i_gId + "");
				// 实时推送数据会引起比赛的不包含关注信息，所以需要从上次保存的比赛的数据复制过去；
				if (oldIndex > -1) {
					olderEntity = older.mGames.get(oldIndex);
					olderEntity.i_away_score = entity.i_away_score;
					olderEntity.i_home_score = entity.i_home_score;
					olderEntity.period = entity.period;
					olderEntity.code = entity.code;
					if (position != oldIndex) {
						// 移动比赛位置
						older.mGames.remove(olderEntity);
						older.mGames.add(position, olderEntity);
					}
				}
				position++;
			}
		}
		mListAdapter.setData(mBlockList);

	}

	/** 更新比赛关注的状态 */
	public void updateFollow(BaseGameEntity entity) {
		entity.bFollow = entity.bFollow > 0 ? (byte) 0 : (byte) 1;
		mListAdapter.notifyDataSetChanged();
	}

	public void clearAll() {
		if (mListAdapter != null) {
			mListAdapter.clear();
		}

		if (mProgressBar != null) {
			mProgressBar.setVisibility(View.VISIBLE);
		}

		mListReqQue.clear();
		mToday = null;
		if (mBlockList != null)
			mBlockList.clear();
		if (mDateList != null)
			mDateList.clear();
		if (mBlockTypeList != null)
			mBlockTypeList.clear();
		mBlockList = null;
		mDateList = null;
		mBlockTypeList = null;
	}

	public void click(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.img_follow:
			ScoreboardEntity entity = (ScoreboardEntity) v.getTag();
			mAct.setFollowGame(entity, SoccerGamesFragment.this);
			break;
		}
	}

	public void itemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
		if (pos > -1 && pos < mListAdapter.getCount()) {
			ScoreboardEntity data = mListAdapter.getItemAt(pos);
			if (data != null) {
				Intent in = new Intent(getActivity(),
						FootballGameActivity.class);
				in.putExtra("data", data);
				in.putExtra("tag", mTag);
				in.putExtra("lid", mLid);
				startActivity(in);
			}
		}

	}

	@Override
	public void onSuccess(Object o, int reqType) {
		FootballLeagueResp entity = (FootballLeagueResp) o;
		setData(reqType, entity);
		if (mProgressBar != null && mProgressBar.isShown())
			mProgressBar.setVisibility(View.GONE);
	}

	@Override
	public void onResume() {
		super.onResume();
		HupuLog.e("papa-soccergamesfragment", "resume--tag=="+mTag);
		runTimer();
	}

	@Override
	public void runTimer() {
		// TODO Auto-generated method stub
		HupuLog.e("papa", "------"+delayTime+"-------"+startTime+"-----"+mAct.isActiveFragment(this));
		if(delayTime>0 && !startTime)
		{
			if (mAct.isActiveFragment(this)) {
				delayTime=1000;
				startFreshTimer();
				startSchedule();// 刷新赛事秒数
			}
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		HupuLog.e("papa-soccergamesfragment", "stop--tag=="+mTag);
		endFreshTimer();
		stopSchedule();
	}

	private int delayTime = 1;

	private boolean startTime;

	/** 定时刷新赛程 */
	FootballTimeTask mFreshTask;

	/** 每次有数据来都检查一次是否需要刷新 */
	private void checkFresh(int time) {
		HupuLog.d("fb", " checkFresh time＝"+time);
		mListAdapter.initTime();
		// --------这是测试
		// if(mAct.isActiveFragment(this))
		// {
		// delayTime=5000;
		// startFreshTimer();
		// startSchedule();// 刷新赛事秒数
		// }
		// ------测试结束
		if (time != 0 && mAct.isActiveFragment(this)) {
			// 服务器设定delayTime = time;
			delayTime = time * 1000;
//			delayTime =5* 1000;
			startFreshTimer();
			startSchedule();// 刷新赛事秒数
		} else {
			endFreshTimer();
		}
	}


	class FootballTimeTask implements Runnable {
		@Override
		public void run() {
			endFreshTimer();
//			if(	mListReqQue!=null&&	mListReqQue.size()>0)
//			{
//				checkFresh(delayTime); 
//			}
//			else
//			{
//				reqDefaultData();
//			}
			reqRefreshData();
			//reqDefaultData();
			HupuLog.d("fb", " FootballTimeTask＝ update");
		}

	}

	private void startFreshTimer() {
		if (startTime)
			return;
		startTime = true;
//		if (mFreshTask == null)
		mFreshTask = new FootballTimeTask();
		mScheduleHandler.postDelayed(mFreshTask, delayTime);
	}

	private void endFreshTimer() {
		startTime = false;
		if (mScheduleHandler != null) {
			mScheduleHandler.removeCallbacks(mFreshTask);
		}
	}

	public void updateGameTime() {
		if (mListAdapter != null)
			mListAdapter.updateTime();
	}

	// 刷新秒钟线程池
	public ScheduledExecutorService scheduExec;
	ScheduleHandler mScheduleHandler;
	boolean bStartSchedule;

	class ScheduleHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			if (mListAdapter != null)
				mListAdapter.updateTime();// 刷新秒钟
		}

	}

	/** 停止刷新秒钟 */
	public void stopSchedule() {
		if (scheduExec != null) {
			scheduExec.shutdownNow();
			scheduExec = null;
		}
		bStartSchedule = false;
	}

	/** 开始刷新秒钟 */
	public void startSchedule() {

		if (!bStartSchedule) {
			bStartSchedule = true;
			scheduExec = Executors.newScheduledThreadPool(1);
			scheduExec.scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					mScheduleHandler.sendEmptyMessage(0);
				}
			}, 0, 1, TimeUnit.SECONDS);
		}
	}

}
