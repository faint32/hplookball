package com.hupu.games.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.hupu.games.R;
import com.hupu.games.activity.BasketballActivity;
import com.hupu.games.adapter.BasketBallGamesListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.BaseGameEntity;
import com.hupu.games.data.game.basketball.BasketBallGamesBlock;
import com.hupu.games.data.game.basketball.BasketballGameEntity;
import com.hupu.games.data.game.basketball.BasketballGamesResp;

/** 篮球赛程页面碎片 */
public class BasketballGamesFragment extends BaseGameFragment {

	private BasketBallGamesListAdapter mListAdapter;

	private ArrayList<BasketBallGamesBlock> mBlockList;

	public final long TEN_MINS = 120000;
	/** 最后刷新的时间 */
	public long mLastNewsTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTag = getArguments().getString("tag");
	}

	@Override
	public void setAdapter() {
		if (mListAdapter == null) {
			Click click = new Click();
			if (mTag.equals("cba"))
				mListAdapter = new BasketBallGamesListAdapter(mAct, click, 1);
			else
				mListAdapter = new BasketBallGamesListAdapter(mAct, click, 0);
			// mPullListener = new PullListener();
		} else {
			if (mToday != null) {
				mProgressBar.setVisibility(View.GONE);
				int index = mDateList.indexOf(mToday);
				if (index > -1) { // 如果是比赛日，开始socket连接
					if (mTag.equals("cba"))
						mAct.joinRoom(HuPuRes.ROOM_CBA_HOME);
					else
						mAct.joinRoom(HuPuRes.ROOM_NBA_HOME);
				}
			}
		}
		listview.setOnItemClickListener(new ListClick());
		// if (iPos > 0)
		// listview.setSelectionFromTop(iPos, iTop);

	}

	@Override
	public void reqDefaultData() {
		if (!runDefaultReq)
			req(0, 0, HuPuRes.REQ_METHOD_BASKETBALL_GAMES_BY_DEFAULT);
		HupuLog.e("papa", "nba||cba-reqDefaultData=" + mTag);
	}

	private boolean runDefaultReq = false;

	private void reqDefault() {
		if (mProgressBar != null)
			mProgressBar.setVisibility(View.VISIBLE);

		runDefaultReq = true;
		if (mListReqQue != null)
			mListReqQue.remove("" + 0);
		preDate = 0;
		nextDate = 0;

		req(0, 0, HuPuRes.REQ_METHOD_BASKETBALL_GAMES_BY_DEFAULT);
	}

	public void prev() {
		// HupuLog.d("fb","prev");
		req(preDate, -1, HuPuRes.REQ_METHOD_BASKETBALL_GAMES_BY_PRE);
	}

	public void next() {
		// HupuLog.d("fb","next");
		req(nextDate, 1, HuPuRes.REQ_METHOD_BASKETBALL_GAMES_BY_NEXT);
	}

	/** 计算和合并数据 */
	private void calData(int methodId, BasketballGamesResp entity) {
		if (mDateList == null) {
			mDateList = new ArrayList<String>();
			mBlockList = new ArrayList<BasketBallGamesBlock>();
		}
		if (mToday == null) {
			mToday = "" + entity.current;
			mMinDate = entity.min;
			mMaxDate = entity.max;
		}

		if (methodId == HuPuRes.REQ_METHOD_BASKETBALL_GAMES_BY_DEFAULT
				|| methodId == HuPuRes.REQ_METHOD_BASKETBALL_GAMES_BY_NEXT) {
			if (entity.mDays != null) {
				if (methodId == HuPuRes.REQ_METHOD_BASKETBALL_GAMES_BY_DEFAULT) {
					if (mDateList != null)
						mDateList.clear();
					if (mBlockList != null) {
						mBlockList.clear();
					}
				}
				mDateList.addAll(entity.mDays);
				mBlockList.addAll(entity.mBlockList);
				if (preDate == 0 && entity.firstDay > 0) {
					preDate = entity.firstDay - 1;
					mListReqQue.remove("" + 0);
				} else
					mListReqQue.remove("" + nextDate);
				if (entity.lastDay + 1 > nextDate)
					nextDate = entity.lastDay + 1;
				// nextDate = getDate(entity.lastDay,1);
			}

		} else if (methodId == HuPuRes.REQ_METHOD_BASKETBALL_GAMES_BY_PRE) {
			if (entity.mDays != null) {
				mDateList.addAll(0, entity.mDays);
				mBlockList.addAll(0, entity.mBlockList);
				mListReqQue.remove("" + preDate);
				if (entity.firstDay - 1 < preDate || preDate == 0)
					preDate = getDate(entity.firstDay, -1);
			}
		}
		listview.setPullRefreshEnable(preDate > mMinDate);
		listview.setPullLoadEnable(nextDate < mMaxDate);

	}

	private boolean isOnresume = false;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

		HupuLog.e("papa", "nba||cba-resume=" + mTag);
		// 适用于董老师的 除了进直播才不刷新 实时列表 其他情况回到nba和cba 比赛列表都刷新
		if (mTag.equals("cba") || mTag.equals("nba")) {
			if (isOnresume) {
				reqDefault();
			} else{
				isOnresume = true;
				if (listview != null ) {
					if (listview.getAdapter() == null) {
						//HupuLog.e("papa", "mlistadapter size=="+listview.getAdapter().getCount());
						reqDefault();
					}
				}
			}
		}

		JoinRoom();
		super.onResume();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		HupuLog.e("papa-baskeball", "stop--tag==" + mTag);
		if (!isLeaveRoom) {
			mAct.leaveRoom();
			isLeaveRoom = false;
		}
		super.onStop();
	}

	public void JoinRoom() {
		if (mToday != null) {
			int index = mDateList.indexOf(mToday);
			if (index > -1) { // 如果是比赛日，开始socket连接

				if (mTag.equals("cba"))
					mAct.joinRoom(HuPuRes.ROOM_CBA_HOME);
				else
					mAct.joinRoom(HuPuRes.ROOM_NBA_HOME);
			}
		}
	}

	/** 设置数据 */
	public void setData(int methodId, BasketballGamesResp resp) {
		listview.stopRefresh();
		if (resp.mBlockList == null)
			return;

		calData(methodId, resp);
		mAct.checkToken(resp.isLogin);
		mListAdapter.setData(mBlockList);
		// if(mTag.equals("cba"))
		// mAct.joinRoom(HuPuRes.ROOM_CBA_HOME);
		if (methodId == HuPuRes.REQ_METHOD_BASKETBALL_GAMES_BY_DEFAULT) {
			listview.setAdapter(mListAdapter);
			int index = mDateList.indexOf(mToday);
			if (index > -1) {
				// 如果是比赛日，开始socket连接
				if (mTag.equals("cba"))
					mAct.joinRoom(HuPuRes.ROOM_CBA_HOME);
				else
					mAct.joinRoom(HuPuRes.ROOM_NBA_HOME);

				if (mBlockList != null) {// 如果还有比赛在进行中，常亮
					byte status = resp.mBlockList.get(index).mGames.get(0).byt_status;
					if (status == 2)
						mAct.setScreenLight(true);
					else
						mAct.setScreenLight(false);
				}
			}
			// 设置锚点
			listview.setSelectionFromTop(resp.anchorIndex, headerHeight);
		} else if (methodId == HuPuRes.REQ_METHOD_BASKETBALL_GAMES_BY_PRE) {
			int block = resp.mBlockList.size();
			int items = resp.total + block + 1;
			listview.setSelectionFromTop(items, headerHeight);
		}
	}

	/** 推送过来的数据 */
	public void updateData(BasketBallGamesBlock resp) {
		if (mBlockList == null && mBlockList.size() == 0)
			return;

		HupuLog.e("papa", "update=" + mBlockList.size());
		int index = mDateList.indexOf(mToday);
		if (index > -1) {
			BasketBallGamesBlock older = mBlockList.get(index);
			if (older != null) {
				int oldIndex = 0;
				BasketballGameEntity olderEntity = null;
				int position = 0;
				for (BasketballGameEntity entity : resp.mGames) {
					// 实时推送数据会引起比赛的排序顺序变化，所以需要定位上次保存的比赛的位置；
					oldIndex = older.mIds.indexOf(entity.i_gId + "");
					// 实时推送数据会引起比赛的不包含关注信息，所以需要从上次保存的比赛的数据复制过去；
					if (oldIndex > -1) {
						olderEntity = older.mGames.get(oldIndex);
						olderEntity.update(entity);
						if (position != oldIndex) {
							// 移动比赛位置
							// older.mGames.remove(olderEntity);
							older.mIds.remove(oldIndex);
							older.mIds.add(position, olderEntity.i_gId + "");
							older.mGames.remove(oldIndex);
							older.mGames.add(position, olderEntity);
						}
					}
					position++;
				}
			}
			mListAdapter.setData(mBlockList);

		}
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
		mBlockList = null;
		mDateList = null;
	}

	public void click(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.img_follow:
			BasketballGameEntity entity = (BasketballGameEntity) v.getTag();
			mAct.setFollowGame(entity, BasketballGamesFragment.this);
			break;
		}
	}

	private boolean isLeaveRoom = false;
	@Override
	public void itemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
		if (pos > -1 && pos < mListAdapter.getCount()) {
			BasketballGameEntity data = mListAdapter.getItemAt(pos);
			if (data != null) {
				isOnresume = false;
				mAct.leaveRoom();
				isLeaveRoom = true;
				if ("nba".equals(mTag))
					mAct.switchToLive(data, pos, true,mTag);
				else {
					Intent in = new Intent(getActivity(),
							BasketballActivity.class);
					in.putExtra("data", data);
					in.putExtra("tag", mTag);
					startActivity(in);
				}
			}
		}

	}

	@Override
	public void onSuccess(Object o, int reqType) {
		BasketballGamesResp entity = (BasketballGamesResp) o;
		setData(reqType, entity);
		runDefaultReq = false;
		if (mProgressBar != null && mProgressBar.isShown())
			mProgressBar.setVisibility(View.GONE);
	}

}
