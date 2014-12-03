package com.hupu.games.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.hupu.games.R;
import com.hupu.games.activity.NBAGameActivity;
import com.hupu.games.activity.PlayerRatingActivity;
import com.hupu.games.activity.UserRateActivity;
import com.hupu.games.adapter.PlayersRatingListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.data.PlayerRatingEntity;
import com.hupu.games.data.PlayersRatingListResp;
import com.hupu.games.data.UserRatingEntity;
import com.hupu.games.view.XListView;
import com.hupu.games.view.XListView.IXListViewListener;
/**
 *  球员评分列表页
 * */
public class PlayersRatingFragment extends BaseFragment {

	XListView mListView;
	
	NBAGameActivity mAct;
	
	PlayersRatingListAdapter mPlayerAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private int REQ_MY_RATING=731;
	private static final int REQ_RATE_PLAYER = 732;
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		Log.d("PlayersRatingFragment", "requestCode="+requestCode);
		if (resultCode == Activity.RESULT_OK && requestCode == REQ_MY_RATING) 
		{
			//需要更新的
			int index = data.getIntExtra("index", -1);
			int score = data.getIntExtra("rating", -1);
			int oid = data.getIntExtra("oid", -1);
			String desc = data.getStringExtra("desc");
//			mPlayerAdapter.getItem(index).my_rating=score;
			mPlayerAdapter.notifyDataSetChanged();
			if(index >-1)
				reqUserRating(oid,"nba",score,desc);
		}
		else if( REQ_RATE_PLAYER == requestCode &&  resultCode == Activity.RESULT_OK )
		{
			updateData((UserRatingEntity)data.getSerializableExtra("entity"));
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mAct =(NBAGameActivity)activity;
		mPlayerAdapter =new PlayersRatingListAdapter(activity,new Click());
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view =inflater.inflate(R.layout.fragment_players,null);
		mListView =(XListView)view.findViewById(R.id.list_player);
		mListView.setPullLoadEnable(false, false);
		mListView.setXListViewListener(new pullListener());
		mListView.setOnItemClickListener(new ListClick());
		mListView.setAdapter(mPlayerAdapter);
		mListView.setHeaderBackground();
		if (bFirstCreate) {
			bFirstCreate = false;
			reqPlayersRating(false);
		}

		return view;
	}

	private boolean bFirstCreate;
	private PlayersRatingListResp mData; 
	private String type ="nba";
	
	@Override
	public void entry() {
		if (mAct == null) {
			bFirstCreate = true;
		} else {
			 reqPlayersRating(false);
		}
	}
	
	public void setData(PlayersRatingListResp data )
	{
		mData =data;
		mPlayerAdapter.setData(data.mList);
		mListView.stopRefresh();
		if(mData.mList ==null)
			mAct.showToast("没有数据");
	}
	
	public void updateData(UserRatingEntity en)
	{
		entity.my_rating=en.my_rating;
		entity.ratings =en.ratings;
		entity.user_num =en.user_num;
		mPlayerAdapter.notifyDataSetChanged();
	}
	
	
	private void reqPlayersRating(boolean isMan)
	{
		if(!isMan)
			mListView.setFreshState();
		mParams = mAct.getHttpParams();
		mParams.put("type", type);
		mAct.sendRequest(HuPuRes.REQ_METHOD_RATING_LIST, mParams);
	}
	
	
	/**
	 * 发送用户打分数据
	 * */
	private void reqUserRating(int oid,String type,int rating,String desc)
	{
		
		mParams = mAct.getHttpParams();
		mParams.put("oid", ""+oid);
		mParams.put("type", type);
		mParams.put("rating", ""+rating);
		mParams.put("desc", desc);
		mAct.sendRequest(HuPuRes.REQ_METHOD_RATING_RATE, mParams);
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
	

	/** 设置listview点击监听器 */
	class ListClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
			if (pos < 1)
				return;
			entity =mPlayerAdapter.getItem(pos-1);
			Intent in = new Intent(mAct, PlayerRatingActivity.class);
		    in.putExtra("profile", entity);
			startActivityForResult(in, REQ_RATE_PLAYER);
		}

	}
	PlayerRatingEntity entity;
	class Click implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			int index = (Integer)v.getTag();
			entity =mPlayerAdapter.getItem(index);
			if(entity.my_rating ==0)
			{
				//如果没有评过分数就
				Intent in =new Intent(mAct, UserRateActivity.class);
			    in.putExtra("name", entity.name);
			    in.putExtra("oid", entity.oid);
			    in.putExtra("index", index);
			    in.putExtra("obj_type", entity.obj_type);
			    in.putExtra("obj_id", entity.obj_id);
				startActivityForResult(in, REQ_MY_RATING);
			}
		    
		}
		
	}
	
	
}
