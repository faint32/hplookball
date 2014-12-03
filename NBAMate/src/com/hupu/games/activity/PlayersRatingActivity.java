/**
 * 
 */
package com.hupu.games.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.hupu.games.R;
import com.hupu.games.adapter.PlayersRatingListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.data.PlayerRatingEntity;
import com.hupu.games.data.PlayersRatingListResp;
import com.hupu.games.data.UserRatingEntity;
import com.hupu.games.view.XListView;
import com.hupu.games.view.XListView.IXListViewListener;
import com.hupu.http.HupuHttpHandler;
import com.pyj.http.RequestParams;

/**
 * @author panyongjun 所有球员的所有打分列表页
 */
public class PlayersRatingActivity extends HupuBaseActivity {

	XListView mListView;

	PlayersRatingListAdapter mPlayerAdapter;

	private int REQ_MY_RATING = 731;
	private static final int REQ_RATE_PLAYER = 732;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// Log.d("PlayersRatingFragment", "requestCode="+requestCode);
		if (resultCode == Activity.RESULT_OK && requestCode == REQ_MY_RATING) {
			// 需要更新的
			int index = data.getIntExtra("index", -1);
			int score = data.getIntExtra("rating", -1);
			int oid = data.getIntExtra("oid", -1);
			String desc = data.getStringExtra("desc");
			// mPlayerAdapter.getItem(index).my_rating=score;
			mPlayerAdapter.notifyDataSetChanged();
			if (index > -1)
				reqUserRating(oid, "nba", score, desc);
		} else if (REQ_RATE_PLAYER == requestCode
				&& resultCode == Activity.RESULT_OK) {
			updateData((UserRatingEntity) data.getSerializableExtra("entity"));
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_players);

		setOnClickListener(R.id.btn_back);
		mListView = (XListView) findViewById(R.id.list_player);
		mListView.setPullLoadEnable(false, false);
		mListView.setXListViewListener(new pullListener());
		mListView.setOnItemClickListener(new ListClick());
		mPlayerAdapter = new PlayersRatingListAdapter(this, new Click());
		mListView.setAdapter(mPlayerAdapter);
		mListView.setHeaderBackground();
		if (bFirstCreate) {
			bFirstCreate = false;
			reqPlayersRating(false);
		}
		
		initParameter();
		mParams.put("gid", getIntent().getStringExtra("gid"));
		reqPlayersRating(false);
	}

	private boolean bFirstCreate;
	private PlayersRatingListResp mData;
	private String type = "nba";

	public void setData(PlayersRatingListResp data) {
		mData = data;
		mPlayerAdapter.setData(data.mList);
		mListView.stopRefresh();
		if (mData.mList == null)
			showToast("没有数据");
	}

	public void updateData(UserRatingEntity en) {
		entity.my_rating = en.my_rating;
		entity.ratings = en.ratings;
		entity.user_num = en.user_num;
		mPlayerAdapter.notifyDataSetChanged();
	}

	public boolean sendRequest(int reqType, RequestParams params) {
		return sendRequest(reqType, params, new HupuHttpHandler(this));
	}

	private void reqPlayersRating(boolean isMan) {
		if (!isMan)
			mListView.setFreshState();
		mParams.put("type", type);
		sendRequest(HuPuRes.REQ_METHOD_RATING_LIST, mParams);
	}

	/**
	 * 发送用户打分数据
	 * */
	private void reqUserRating(int oid, String type, int rating, String desc) {

		mParams.put("oid", "" + oid);
		mParams.put("type", type);
		mParams.put("rating", "" + rating);
		mParams.put("desc", desc);
		sendRequest(HuPuRes.REQ_METHOD_RATING_RATE, mParams);
	}

	/** 设置listview 上拉和下拉的监听 */
	class pullListener implements IXListViewListener {

		@Override
		public void onRefresh() {
			reqPlayersRating(true);
		}

		@Override
		public void onLoadMore() {
		}

	}

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		switch (methodId) {
		case HuPuRes.REQ_METHOD_RATING_LIST:
			PlayersRatingListResp rating = (PlayersRatingListResp) o;
			setData(rating);
			break;
		case HuPuRes.REQ_METHOD_RATING_RATE:
			UserRatingEntity entity = (UserRatingEntity) o;

			showToast("评分成功");
			updateData(entity);
			break;
		}
	}

	/** 设置listview点击监听器 */
	class ListClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
			if (pos < 1)
				return;
			if (mPlayerAdapter.getItem(pos - 1) == null) 
				return;
			
			entity = mPlayerAdapter.getItem(pos - 1);
			Intent in = new Intent(PlayersRatingActivity.this,
					PlayerRatingActivity.class);
			in.putExtra("profile", entity);
			startActivityForResult(in, REQ_RATE_PLAYER);
		}

	}

	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);
		if (id == R.id.btn_back)
			finish();

	}

	PlayerRatingEntity entity;

	class Click implements OnClickListener {

		@Override
		public void onClick(View v) {
			int index = (Integer) v.getTag();
			entity = mPlayerAdapter.getItem(index);
			if (entity.my_rating == 0) {
				// 如果没有评过分数就
				Intent in = new Intent(PlayersRatingActivity.this,
						UserRateActivity.class);
				in.putExtra("name", entity.name);
				in.putExtra("oid", entity.oid);
				in.putExtra("index", index);
				startActivityForResult(in, REQ_MY_RATING);
			}

		}

	}

}
