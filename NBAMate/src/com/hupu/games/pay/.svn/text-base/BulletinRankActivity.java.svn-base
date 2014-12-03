/**
 * 
 */
package com.hupu.games.pay;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.hupu.games.R;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.adapter.RatingByWallListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.data.BulletinRankReq;
import com.hupu.http.HupuHttpHandler;

/**
 * @author panyongjun 上墙榜 需要lid和gid
 */
public class BulletinRankActivity extends HupuBaseActivity {

	/** 对该球员的打分及评论 */
	ListView mListView;

	RatingByWallListAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_wall_rank);

		initParameter();
		Intent in = getIntent();
		mParams.put("lid", "" + in.getIntExtra("lid", 1));
		mParams.put("gid", "" + in.getIntExtra("gid", 0));
		getBulletinRank();

		mListView = (ListView) findViewById(R.id.list_player);
		
		setOnClickListener(R.id.btn_back);
	}

	
	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);

		switch (id) {
		case R.id.btn_back:
			finish();
			break;
		}
	}
	
	/**
	 * 上墙榜
	 * */
	private void getBulletinRank() {
		sendRequest(HuPuRes.REQ_METHOD_GET_BULLETIN_RANK, mParams,
				new HupuHttpHandler(this), false);
	}

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		// 上墙榜
		if (o != null) {
			BulletinRankReq bulletin = (BulletinRankReq) o;

			mAdapter = new RatingByWallListAdapter(this, click);
			mAdapter.setData(bulletin.mList);
			mListView.setAdapter(mAdapter);
			mAdapter.notifyDataSetChanged();
		}
	}

}
