package com.hupu.games.activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hupu.games.R;
import com.hupu.games.adapter.SingleHonourListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.SingleHonourEntity;
import com.hupu.games.view.XListView;
import com.hupu.games.view.XListView.IXListViewListener;
import com.hupu.http.HupuHttpHandler;

public class SingerRankingActivity extends HupuBaseActivity {
	XListView list;
	SingleHonourListAdapter adapter;
	String gid;
	String lid;
	private int curTab = 0;
	LinkedList<SingleHonourEntity> listData = new LinkedList<SingleHonourEntity>();
	List<Button> btnList = new ArrayList<Button>();
	View headView=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_single_ranking);
		list = (XListView) this.findViewById(R.id.list_honour);
		adapter = new SingleHonourListAdapter(this);
		list.setXListViewListener(new pullListener());
		// 初始化时不需要刷新
		list.setPullLoadEnable(false, false);
		gid = this.getIntent().getStringExtra("gid");
		lid = this.getIntent().getStringExtra("lid");
		initData();
		setOnClickListener(R.id.btn_back);
	}

	@Override
	public void treatClickEvent(int id) {
		switch (id) {
		case R.id.btn_back:
			finish();
			break;

		}
	}

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		if (o == null)
			return;
		if (methodId == HuPuRes.REQ_METHOD_GET_TOTAL_MATCH_RANKING) {
			SingleHonourEntity entity = (SingleHonourEntity) o;
			listData = entity.mList;
			if(headView==null){
				headView = LayoutInflater.from(this).inflate(
						R.layout.item_honor_list_head, null);
				list.addHeaderView(headView);
				list.setAdapter(adapter);
			}
			LinearLayout layout_top = (LinearLayout) headView
					.findViewById(R.id.layout_top);
			if(headView!=null) layout_top.removeAllViews();
			int i = 0;
			for (SingleHonourEntity singEntity : entity.mList) {
				View view = LayoutInflater.from(this).inflate(
						R.layout.item_honor_head, null);
				Button btn = (Button) view;
				if (curTab == i)
					btn.setBackgroundResource(R.drawable.btn_rank_type_bg_down);
				btn.setTag(i);
				btnList.add(btn);
				btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						curTab = Integer.parseInt(v.getTag().toString());
						for (int i = 0; i < btnList.size(); i++) {
							if (curTab != i) {
								btnList.get(i).setBackgroundResource(
										R.drawable.btn_rank_type_bg);
							}
						}
						v.setBackgroundResource(R.drawable.btn_rank_type_bg_down);
						updateListData();
					}
				});
				btn.setText(singEntity.name);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
						LinearLayout.LayoutParams.FILL_PARENT, 1.0f);
				layout_top.addView(view, lp);
				i++;
			}
			LinkedList<SingleHonourEntity> uData = new LinkedList<SingleHonourEntity>();
			uData = listData.get(curTab).hList;
			if(entity.mList.get(curTab).myEntity!=null&&entity.mList.get(curTab).myEntity.rank>10)
				uData.add(entity.mList.get(curTab).myEntity);
			adapter.setData(uData);
			headView.invalidate();
		}
	}


	private void initData() {
		initParameter();
		mParams.put("lid", lid);
		mParams.put("gid", gid);
		mParams.put("token", SharedPreferencesMgr.getString("tk", null));
		sendRequest(HuPuRes.REQ_METHOD_GET_TOTAL_MATCH_RANKING, mParams,
				new HupuHttpHandler(this), true);
	}


	/** 设置listview 上拉和下拉的监听 */
	class pullListener implements IXListViewListener {
		@Override
		public void onRefresh() {
			initData();
		}

		@Override
		public void onLoadMore() {
			initData();
		}
	}

	void updateListData() {
		LinkedList<SingleHonourEntity> uData = new LinkedList<SingleHonourEntity>();
		for(SingleHonourEntity entity:listData.get(curTab).hList){
			uData.add(entity);
		}
		if(listData.get(curTab).myEntity.rank>10){
			uData.add(listData.get(curTab).myEntity);
		}
		adapter.setData(uData);
	}
}
