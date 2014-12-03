package com.hupu.games.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.adapter.GameDataListAdapter;
import com.hupu.games.data.MatchEntity;
import com.hupu.games.view.HScrollView;
import com.hupu.games.view.PinnedHeaderXListView;

public class BaseBasketballFragment extends Fragment {
	public HScrollView scrollView;

	/** 球队统计数据 */
	public PinnedHeaderXListView mLvDatas;

	public GameDataListAdapter mDataListAdapter;

	public TableLayout mTable;

	/**
	 * 比分统计标题栏共有多少栏
	 * */
	protected int i_columnSize;
	/** 最多有多少次加时赛 */
	protected static final int MAX_OT_SIZE = 5;
	/** 初始化时总分所在的索引位置 */
	protected static final int COLUMN_TOTAL_INDEX = 5;

	protected static final int TITLE_TOTAL_INDEX = 9;

	/**
	 * 比分统计标题栏
	 * */
	protected String[] arrTitles;
	/**
	 * 比分统计key值
	 * */
	protected LinkedList<String> listKeys;

	protected Activity mAct;

	protected View mProgressBar;

	ListViewTouchLinstener touchListener;

	boolean bGetData;

	protected String homeName;

	protected String awayName;

	/** 季后赛主胜场次 */
	protected int iHomeSeries;
	/** 季后赛客胜场次 */
	protected int iAwaySeries;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAct = getActivity();
		if (arrTitles == null) {
			listKeys = new LinkedList<String>();
			arrTitles = mAct.getResources().getStringArray(R.array.title_score);
			listKeys.addAll(Arrays.asList(mAct.getResources().getStringArray(
					R.array.key_score)));
		}

	}

	public void setScrollView(HScrollView s) {
		scrollView = s;
	}

	public void addData(boolean b) {
		bGetData = b;
		if (bGetData && mProgressBar != null)
			mProgressBar.setVisibility(View.GONE);
	}

	/** 设置表格数据，需要动态添加加时数据 */
	public void setTableData(MatchEntity entityHome, MatchEntity entityAway) {
		// arrHomeScore = new TextView[9];
		// arrAwayScore = new TextView[9];
		int otTime = 0;
		if (entityHome != null)
			otTime = entityHome.otTimes;
		if (entityAway != null)
			otTime = entityAway.otTimes > otTime ? entityAway.otTimes : otTime;
		if (otTime > i_columnSize - 6 && otTime <= MAX_OT_SIZE) {
			// 有加时赛自动添加,但是最多只能添加5场
			int size = entityHome.otTimes + 6 - i_columnSize;
			for (int i = 0; i < size; i++) {
				// System.out.println("add colum=" );
				addColumn();
			}
		}
		String ss = null;
		String key = null;
		for (int i = 0; i < i_columnSize; i++) {
			if (i == 0) {
				// 主客队名字

				getTextView(1, 0).setText(homeName);
				getTextView(2, 0).setText(awayName);

			} else {
				if (i == i_columnSize - 1)
					key = listKeys.getLast();
				else {
					key = listKeys.get(i - 1);
				}
				if (entityHome != null) {
					ss = entityHome.mapScore.get(key);
					if (ss != null && !"".equals(ss)) {
						getTextView(1, i).setText(ss);
						copyTableData(true, key, ss);
					}
				}
				if (entityAway != null) {
					ss = entityAway.mapScore.get(key);
					if (ss != null && !"".equals(ss)) {
						getTextView(2, i).setText(ss);
						copyTableData(false, key, ss);
					}
				}

			}
		}
	}

	public void copyTableData(boolean home, String key, String value) {

	}

	/** 初始化表格 */
	public void initRow() {
		TableRow.LayoutParams lp = new TableRow.LayoutParams();
		lp.setMargins(0, 0, 1, 1);
		for (int rowIndex = 0; rowIndex < 3; rowIndex++) {// 三行

			TableRow row = new TableRow(mAct);
			for (int column = 0; column < 6; column++) {// 6列
				TextView tv = buildTextView(rowIndex);
				if (rowIndex == 0) {
					if (column == COLUMN_TOTAL_INDEX)// 总分
					{
						tv.setText(arrTitles[TITLE_TOTAL_INDEX]);
						lp.weight = 1;
					} else if (column > 0) {
						tv.setText(arrTitles[column - 1]);
						lp.weight = 0;
					}
				}
				row.addView(tv, lp);
			}
			mTable.addView(row, new TableLayout.LayoutParams());
		}
		i_columnSize = 6;
		// addColumn();
	}

	private void addColumn() {
		TableRow row;
		for (int rowIndex = 0; rowIndex < 3; rowIndex++) {
			row = (TableRow) mTable.getChildAt(rowIndex);
			TextView tv = buildTextView(0);
			if (rowIndex == 0) {
				tv.setText(arrTitles[TITLE_TOTAL_INDEX]);
				tv.setTextColor(colorTitle);
				((TextView) row.getChildAt(i_columnSize - 1))
						.setText(arrTitles[i_columnSize - 2]);
			} else
				tv.setTextColor(Color.WHITE);
			TableRow.LayoutParams lp = new TableRow.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			lp.weight = 1;
			lp.setMargins(0, 0, 1, 1);
			row.addView(tv, lp);
		}
		i_columnSize++;
	}

	int colorTitle;

	/**
	 * @param type
	 *            0 表示标题 1表示数据
	 * */
	private TextView buildTextView(int type) {
		TextView tv = new TextView(mAct);
		tv.setBackgroundResource(R.drawable.shape_gray);
		if (type == 0) {
			if (colorTitle == 0)
				colorTitle = getActivity().getResources().getColor(
						R.color.table_title);
			tv.setTextColor(colorTitle);
		} else
			tv.setTextColor(Color.WHITE);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);

		return tv;
	}

	private TextView getTextView(int row, int column) {
		return (TextView) ((TableRow) mTable.getChildAt(row))
				.getChildAt(column);
	}

	public class ListViewTouchLinstener implements View.OnTouchListener {

		float historicX = Float.NaN;
		static final int DELTA_ON_CLICK = 20;

		float x0;
		float y0;

		@SuppressLint("NewApi")
		@Override
		public boolean onTouch(View arg0, MotionEvent event) {
			if (scrollView != null)
				scrollView.onTouchEvent(event);
			if (mLvDatas != null)
				mLvDatas.invalidate();
			return gesture.onTouchEvent(event);

		}
	}

	public ArrayList<String> getKeys() {
		if (mDataListAdapter != null)
			return mDataListAdapter.getKeys();
		return null;
	}

	private GestureDetector gesture;

	/** 初始化手势，主要是让统计数据能够左右移动 */
	public void initGesture() {
		gesture = new GestureDetector(getActivity(), new MyGestureDetector());
	}

	/** 手势监听类 */
	class MyGestureDetector extends SimpleOnGestureListener {

		private static final int SWIPE_MIN_DISTANCE = 50;
		private static final int SWIPE_MAX_OFF_PATH = 250;
		private static final int SWIPE_THRESHOLD_VELOCITY = 60;

		// Touch了滑动一点距离后，up时触发
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (e2 == null) {
				return false;
			}
			if (e1 == null) {

				return false;
			}

			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				return true;

			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				return true;
			}
			return super.onFling(e1, e2, velocityX, velocityY);

		}

		public boolean onSingleTapConfirmed(MotionEvent e) {
			return super.onSingleTapConfirmed(e);
		}

		public boolean onDoubleTap(MotionEvent e) {
			return super.onDoubleTap(e);
		}

		public boolean onDoubleTapEvent(MotionEvent e) {
			// Log.i("MyGesture", "onDoubleTapEvent");
			return super.onDoubleTapEvent(e);
		}

		public boolean onDown(MotionEvent e) {
			// Log.i("MyGesture", "onDown");
			return super.onDown(e);
		}

		public void onLongPress(MotionEvent e) {
			// Log.i("MyGesture", "onLongPress");
			super.onLongPress(e);
		}

		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return super.onScroll(e1, e2, distanceX, distanceY);
		}

		public void onShowPress(MotionEvent e) {
			super.onShowPress(e);
		}

		public boolean onSingleTapUp(MotionEvent e) {
			return super.onSingleTapUp(e);
		}
	}
}
