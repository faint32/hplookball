package com.hupu.games.fragment;

import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.hupu.games.R;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.activity.HupuBaseActivity.ChatInterface;
import com.hupu.games.adapter.ChatListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.ChatEntity;
import com.hupu.games.data.ChatResp;
import com.hupu.games.view.XListView;
import com.hupu.games.view.XListView.IXListViewListener;

public class ChatFragment extends BaseFragment {

	// private View mProgressBar;

	ChatInterface mAct;

	LinkedList<ChatEntity> mDataList;

	ChatListAdapter mAdapter;

	/***/
	private int oldChatID;
	/***/
	private int newChatID;

	private XListView mListViewChat;

	ListClick listClick;
	
	Handler handler = new Handler();
	
    private String tag;
    
    private int mRoomId = -1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDataList = new LinkedList<ChatEntity>();
		
		listClick = new ListClick();
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mAct = (ChatInterface) getActivity();
	}

	public void setTag(String t)
	{
		tag =t;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_chat, container, false);
		mListViewChat = (XListView) v.findViewById(R.id.list_chat);
		hint = v.findViewById(R.id.hint_layout);
		if(mAdapter == null)
		{
			mAdapter = new ChatListAdapter(getActivity(),tag);
			reqNewDataDelay(false);
		}	
		mListViewChat.setAdapter(mAdapter);		
		mListViewChat.setOnItemClickListener(listClick);
		mListViewChat.setXListViewListener(new pullListener());
		mListViewChat.setPullLoadEnable(false,false);
		
		return v;
	}


	View hint;

	/** 重新刷新数据 */
	public void setData(ChatResp data,int roomId) {
		// 有消息返回的情况下，需要先判断是推送消息，还是刷新消息，还是加载更多的消息
		mRoomId = roomId;
		if(data.mList==null)
			return;
		if (data.pid_old == 0) {
			// 刷新
			// 设定偏移量
			mDataList = data.mList;
			newChatID = data.pid;
			if(mDataList != null )
				oldChatID = data.pid - mDataList.size();
			if (mDataList == null || mDataList.size() < 20 || oldChatID <2)
			{
				mListViewChat.setPullLoadEnable(false,false);
			}
			else
				mListViewChat.setPullLoadEnable(true,false);

		} else {
			if (data.direc.equals("next")) {
				// 推送过来的最新
				newChatID = data.pid;
				if(mDataList != null )
					mDataList.addAll(0, data.mList);
				else
					mDataList = data.mList;
			} else {
				// 加载更多
				if(mDataList != null )
				{
					oldChatID = data.pid - data.mList.size();
					mDataList.addAll(data.mList);
				}		
				else
				{
					mDataList = data.mList;
				}
				if (oldChatID < 2)
					mListViewChat.setPullLoadEnable(false,false);
				// mAct.showToast("没有更多聊天记录了");
//				Log.d("setData", "load more  oldChatID= "
//						+ oldChatID);
			}
		}
		if (mDataList == null || mDataList.size() == 0)
		{
			hint.setVisibility(View.VISIBLE);
		}
		else
		{
			hint.setVisibility(View.GONE);		
			mAdapter.setData(mDataList);
		}
		
		page = 0;
	}

	public int getLastId() {
		return newChatID;
	}

	public void setLastId(int id) {
		newChatID = id;
	}

	/** 新数据加入 */
	public void addData(ChatResp data) {
		mAdapter.setData(mDataList);
	}

	/** 新数据加入 */
	public void addData(int type ,String name, String ss) {

		ChatEntity entity = new ChatEntity();
		//entity.username = "游客-" + name;
		entity.username = name;
		if(type == 0)
		  entity.content = ss;
		else
		{
			entity.emoji=ss;
			entity.content ="表情";
		}
		entity.vip=SharedPreferencesMgr.getBoolean("vip", false)?1:0;
		if(mDataList!=null)
		{
			HupuLog.d("old add=");
			mDataList.add(0, entity);
			mAdapter.setData(mDataList);
		}
		if(hint!=null)
			hint.setVisibility(View.GONE);
		// mListViewChat.setSelectionAfterHeaderView() ;
		// mListViewChat.invalidate();
	}

	/** 清空所有数据 */
	public void clearData() {
		if (mDataList != null)
			mDataList.clear();
		if (mAdapter != null) {
			mAdapter.setData(mDataList);
		}
	}

	/** 获取最新数据 */
	public void reqNewData() {
		if (mDataList == null)
			mListViewChat.setPullLoadEnable(false,false);		
		mAct.reqChatData(0);	
	}

	
//	@Override
//	public void entry() {
//		super.entry();
//		if (mAct != null)		
//			reqNewDataDelay(false) ;
//		if (mRoomId == ) {
//			
//		}
//		clearData();
//	}

	
	public void entry(int roomId) {
		if (mAct != null)		
			reqNewDataDelay(false) ;
		if (mRoomId != roomId) {
			clearData();
		}
	}
	

	/** 获取最新数据 */
	public void reqNewDataDelay(boolean isMan) {
		if (mDataList == null)
			mListViewChat.setPullLoadEnable(false,false);
		if (!isMan) {
			mListViewChat.setFreshState();
//			mListViewChat.getChildAt(0).setVisibility(View.VISIBLE);
		}
		
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mAct.reqChatData(0);
			}
		}, 800);
	}

	int page;

	/** 获取更多数据 */
	public void reqMoreData() {
//		Log.d("reqMoreData", "oldChatID=" + oldChatID);
		mAct.reqChatData(oldChatID);
		
	}

	/** 设置listview 上拉和下拉的监听 */
	class pullListener implements IXListViewListener {

		@Override
		public void onRefresh() {
			reqNewDataDelay(true);
		}

		@Override
		public void onLoadMore() {
			reqMoreData();
		}

	}

	/** 设置listview点击监听器 */
	class ListClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
			// if(pos<1)
			// return;
			// Intent in =new Intent(getActivity(),WebViewActivity.class);
			// in.putExtra("url", mAdapter.getItem(pos-1).fromurl);
			// startActivity(in);
		}

	}

	public void stopLoad() {
		if (mListViewChat != null) {
			mListViewChat.stopRefresh();
			mListViewChat.stopLoadMore();
		}
	}
	
	public void switchToWebAct(String url)
	{
		
	}
}
