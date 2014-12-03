package com.hupu.games.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.hupu.games.R;
import com.hupu.games.adapter.GameDataListAdapter;
import com.hupu.games.data.game.basketball.CBABoxScoreResp;
import com.hupu.games.view.PinnedHeaderXListView;

/**
 * 统计页面
 * */
@SuppressLint("ValidFragment")
public class CBAStatisticFragment extends BaseBasketballFragment {

    CBABoxScoreResp mData;   

	public CBAStatisticFragment() {
		super();

	}

	public CBAStatisticFragment(String h, String a) {
		super();
		homeName =h;
		awayName =a;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_statistics, container,
				false);

		mProgressBar = v.findViewById(R.id.probar);
		mTable = (TableLayout) v.findViewById(R.id.table_score);
		mLvDatas = (PinnedHeaderXListView) v.findViewById(R.id.list_players);
		mLvDatas.setPullLoadEnable(false);
		mLvDatas.setPullRefreshEnable(false);
		
		mDataListAdapter = new GameDataListAdapter(getActivity(), homeName,
				awayName,this);
		
		initGesture();
		touchListener = new ListViewTouchLinstener();
		mLvDatas.setOnTouchListener(touchListener);
		mLvDatas.setAdapter(mDataListAdapter);
		initRow();
		if(mData!=null)
			setData(mData);
		return v;
	}





	public void setData(CBABoxScoreResp data) {
		
		bGetData =true;
		mData =data;
		if (mProgressBar != null)
			mProgressBar.setVisibility(View.GONE);
//		mData =data;
		// 需要先设置title
		if (data != null && mLvDatas != null) {

			mLvDatas.setVisibility(View.VISIBLE);
			mTable.setVisibility(View.VISIBLE);

			mDataListAdapter.setData(data);
			
			setTableData(data.mEntityHome, data.mEntityAway);
		}
	}


	
	public void updateData(CBABoxScoreResp data) {

		setTableData(data.mEntityHome, data.mEntityAway);
		mDataListAdapter.updateData(data);
	}


	

}
