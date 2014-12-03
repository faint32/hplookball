package com.hupu.games.fragment;

import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.hupu.games.R;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.activity.WebViewActivity;
import com.hupu.games.adapter.VideoListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.VideoEntity;
import com.hupu.games.data.VideoResp;
import com.hupu.games.view.XListView;
import com.hupu.games.view.XListView.IXListViewListener;
import com.pyj.http.RequestParams;

@SuppressLint("ValidFragment")
public class VideoFragment extends BaseFragment {

	private XListView mListVideo;
	private Button mVideoBtn1;

	private Button mVideoBtn2;

	private VideoListAdapter mVideoAdapter;

	private LinkedList<VideoEntity> mData1;

	private LinkedList<VideoEntity> mData2;

	private int frame = 0;

	private long last1Vid;

	private long last2Vid;

	/** 最后刷新比赛视频的时间 */
	private long mLastTime1;
	/** 最后刷新推荐视频的时间 */
	private long mLastTime2;
	/***/
	private static final long TEN_MINS = 600000;
	/***/
	boolean bNeedFresh1;
	/***/
	boolean bNeedFresh2;

	private Click click;

	private RequestParams mParams;

	private int method1;
	private int method2;
	private int method1Next;
	private int method2Next;
	private String reqTag1;
	private String reqTag2;

	boolean hasBtnTxt;

	int mode;

	private String mTag;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(mTag==null)
		{
			mTag = getArguments().getString("tag");
			mode=getArguments().getInt("mode");
			setMode(mode,mTag);
		}
	}
	public void setMode(int m,String tag) {
		mode = m;
		mTag =tag;
		if (mode == 0) {
			// nba
		} else if (mode == 1) {
			// cba
			method1 = HuPuRes.REQ_METHOD_GET_CBA_VIDEO_GAME;
			method2 = 10000;
			method1Next = HuPuRes.REQ_METHOD_GET_CBA_VIDEO_GAME_NEXT;
			method2Next = 10000;
		} else {
			// 足球和杯赛
			method1 = HuPuRes.REQ_METHOD_GET_FOOTBALL_VIDEO;
			method2 = 10000;
			method1Next = HuPuRes.REQ_METHOD_GET_FOOTBALL_VIDEO_NEXT;
			method2Next = 10000;
		}
		clearVideo();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_video, container, false);
		if(click==null)
		{
			click = new Click();
			mVideoAdapter = new VideoListAdapter(getActivity());
		}
		if (mode == 0) {
			mVideoBtn1 = (Button) v.findViewById(R.id.btn_video_1);
			mVideoBtn2 = (Button) v.findViewById(R.id.btn_video_2);
			
			if (!hasBtnTxt) {
				mVideoBtn1.setClickable(false);
				mVideoBtn2.setClickable(false);// 由于需要从服务端拉取tabs，所以一上来不能点击。
			}else
			{
				mVideoBtn1.setText(tab1);
				mVideoBtn2.setText(tab2);
			}
			
			mVideoBtn1.setOnClickListener(click);
			mVideoBtn2.setOnClickListener(click);
		} else {
			v.findViewById(R.id.layout_top).setVisibility(View.GONE);
		}
		mListVideo = (XListView) v.findViewById(R.id.list_video);

		if (mVideoAdapter.getCount()==0) {
			if (mData2 != null) {
				swtichToFrame(1);
				refreshListViewData(1);
				mListVideo.setSelection(0);
			}else{
				reqNewData(false, null);
				mListVideo.setPullLoadEnable(false, false);
			}
		} else {
			swtichToFrame(frame);
			mListVideo.setPullLoadEnable(true, false);
		}
		mListVideo.setOnItemClickListener(new ListClick());
		mListVideo.setXListViewListener(new pullListener());
		mListVideo.setAdapter(mVideoAdapter);
		// 初始化时不需要刷新
		//mListVideo.setPullLoadEnable(false, false);

		return v;
	}

	public void clearVideo() {
		// Log.d("video fragment","clearVideo");
		if (mVideoAdapter != null)
			mVideoAdapter.setData(null);
		if (mData1 != null)
			mData1.clear();
		if (mData2 != null)
			mData2.clear();
		bNeedFresh1 = true;
		bNeedFresh2 = true;
		reqTag1 = null;
		reqTag2 = null;
	}

	/** 获取最新数据 */
	public void reqNewData(boolean isMan, String type) {
		HupuLog.d("reqNewData", "type =" + type + " isMan=" + isMan+"--frame="+frame +"--method="+method1);
		if (frame == 0) {
			if (mData1 == null)
				mListVideo.setPullLoadEnable(false, false);
			if (!isMan)
				mListVideo.setFreshState();
			if (method1 > 0)
				reqVideoData(method1, 0, type);
			else
				reqVideoData(HuPuRes.REQ_METHOD_GET_NBA_VIDEO, 0, null);
		} else {
			if (mData2 == null)
				mListVideo.setPullLoadEnable(false, false);
			if (!isMan)
				mListVideo.setFreshState();
			reqVideoData(method2, 0, type);
		}
	}

	/** 排行视频数据 */
	private void reqVideoData(int reqid, long vid, String type) {
		mParams = mAct.getHttpParams(true);
		if (!mAct.isActiveFragment(this)) {
			mParams.put("preload", "1");
		}
		
		if (type != null)
			mParams.put("type", type);
		if (vid > 0)
			mParams.put("vid", "" + vid);
		switch (reqid) {
		case HuPuRes.REQ_METHOD_GET_NBA_VIDEO_GAME_NEXT:
		case HuPuRes.REQ_METHOD_GET_NBA_VIDEO_HOT_NEXT:
		case HuPuRes.REQ_METHOD_GET_CBA_VIDEO_GAME_NEXT:
		case HuPuRes.REQ_METHOD_GET_FOOTBALL_VIDEO_NEXT:
			mParams.put("direc", "next");
			break;
		}

//		mAct.sendTagRequest(reqid, mParams);
		mAct.sendAppRequest(reqid,mTag, mParams,new FragmentHttpResponseHandler());
	}

	int pageGame;
	int pageHot;

	/** 获取更多数据 */
	private void reqMoreData() {
		HupuLog.d("reqMoreData", "type ====");
		if (frame == 0) {
			reqVideoData(method1Next, last1Vid, reqTag1);
		} else {
			reqVideoData(method2Next, last2Vid, reqTag2);
		}
	}

	/**
	 * 由其他的页面切换进入
	 * */
	public void entry() {
		if (mAct != null) {

			long curTime = System.currentTimeMillis();
			if (curTime - mLastTime1 > TEN_MINS) {
				bNeedFresh1 = true;
			}
			if (curTime - mLastTime2 > TEN_MINS) {
				bNeedFresh2 = true;
			}

			if (frame == 0 && bNeedFresh1 || frame == 1 && bNeedFresh2) {
				if (frame == 0)
					reqNewData(false, reqTag1);
				else
					reqNewData(false, reqTag2);
				HupuLog.d("entry", "type =====");
			}
		}

	}

	boolean bOpen;

	String tab1;
	String tab2;
	/**
	 * 赋值并刷新
	 * */
	public void setData(int method, Object o) {
		HupuLog.d("setData", "method =====" + method);
		VideoResp resp = (VideoResp) o;
		if (!hasBtnTxt && resp.tabs1 != null) {
			tab1 =resp.tabs1;
			tab2 =resp.tabs2;
			mVideoBtn1.setText(resp.tabs1);
			mVideoBtn2.setText(resp.tabs2);
			mVideoBtn1.setClickable(true);
			mVideoBtn2.setClickable(true);
			// 设置所有的请求参数
			reqTag1 = resp.tabsTap1;
			reqTag2 = resp.tabsTap2;
			if (reqTag1.equals("game")) {
				method1 = HuPuRes.REQ_METHOD_GET_NBA_VIDEO_GAME;
				method2 = HuPuRes.REQ_METHOD_GET_NBA_VIDEO_HOT;
				method1Next = HuPuRes.REQ_METHOD_GET_NBA_VIDEO_GAME_NEXT;
				method2Next = HuPuRes.REQ_METHOD_GET_NBA_VIDEO_HOT_NEXT;
			} else {
				method2 = HuPuRes.REQ_METHOD_GET_NBA_VIDEO_GAME;
				method1 = HuPuRes.REQ_METHOD_GET_NBA_VIDEO_HOT;
				method2Next = HuPuRes.REQ_METHOD_GET_NBA_VIDEO_GAME_NEXT;
				method1Next = HuPuRes.REQ_METHOD_GET_NBA_VIDEO_HOT_NEXT;
			}

			hasBtnTxt = true;
			if (resp.on == 1) {
				// 默认显示第二个标签
				method = method2;
				swtichToFrame(1);
			} else {
				method = method1;
			}
		}
		bOpen = resp.open == 1 ? true : false;
		if (resp.nextDataExists > 0)
			mListVideo.setPullLoadEnable(true, false);
		else {
			mListVideo.setPullLoadEnable(false, false);
			if (method1Next == method)
				mAct.showToast("没有更多视频了");
		}

		if (method == method1) {
			mLastTime1 = System.currentTimeMillis();
			bNeedFresh1 = false;
		} else if (method == method1Next) {
			if (mData1 == null)
				method = method1;
		} else if (method == method2) {
			mLastTime2 = System.currentTimeMillis();
			bNeedFresh2 = false;
		} else if (method == method2Next) {
			if (mData2 == null)
				method = method2;
		}

		// ---
		if (method == method1) {
			mData1 = resp.mList;
			pageGame = 0;
			// Log.d("setData", "set all"+resp.mList.size());
		} else if (method == method1Next) {
			// Log.d("setData", "add all"+resp.mList.size());
			mData1.addAll(resp.mList);
		} else if (method == method2) {
			mData2 = resp.mList;
			pageHot = 0;
		} else if (method == method2Next) {
			mData2.addAll(resp.mList);
		}

		if (method == method1 || method == method1Next)
			last1Vid = resp.lastVId;
		else
			last2Vid = resp.lastVId;

		refreshListViewData(method == method1 || method == method1Next ? 0 : 1);

	}

	/** 停止加载动画 */
	public void stopLoad(boolean bDelay) {
		// Log.d("video", "stopLoad");
		mListVideo.stopRefresh();
		mListVideo.stopLoadMore();
	}

	/** 刷新列表数据 */
	private void refreshListViewData(int type) {
		HupuLog.e("papa", "-----"+type+"----"+frame);
		if (type == 0 && frame == 0)
			mVideoAdapter.setData(mData1);
		if (type == 1 && frame == 1)
			mVideoAdapter.setData(mData2);
	}

	private class Click implements OnClickListener {

		@Override
		public void onClick(View v) {
			int id = v.getId();

			switch (id) {
			case R.id.btn_video_1:
				swtichToFrame(0);
				refreshListViewData(0);
				if (mData1 != null)
					mListVideo.setSelection(0);
				if (mData1 == null || bNeedFresh1)
					reqNewData(false, reqTag1);

				break;
			case R.id.btn_video_2:
				swtichToFrame(1);
				refreshListViewData(1);
				if (mData2 != null)
					mListVideo.setSelection(0);
				if (mData2 == null || bNeedFresh2) {
					reqNewData(false, reqTag2);
				}
				break;
			}

		}
	}

	/** 设置listview 上拉和下拉的监听 */
	class pullListener implements IXListViewListener {

		@Override
		public void onRefresh() {
			if (frame == 0)
				reqNewData(true, reqTag1);
			else
				reqNewData(true, reqTag2);
		}

		@Override
		public void onLoadMore() {
			if ((frame == 0 && mData1 != null)
					|| (frame == 1 && mData2 != null)) {
				reqMoreData();
			}
			// reqMoreData();
		}

	}

	/** 设置listview点击监听器 */
	class ListClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, final int pos, long arg3) {
			if (pos < 1)
				return;

			if (((HupuBaseActivity)getActivity()).checkNetIs2Gor3G()) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage(getActivity().getString(R.string.play_video_2G3G))
				       .setCancelable(false)
				       .setPositiveButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				           }
				       })
				       .setNegativeButton(getString(R.string.play_video_next), new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   toVideoAct(pos);
				           }
				       });
				AlertDialog alert = builder.create();
				alert.show();
			}else {
				toVideoAct(pos);
			}
		}
	}

	private void toVideoAct(int pos){
		if (bOpen) {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(mVideoAdapter.getItem(pos - 1).fromurl));
			startActivity(intent);
		} else {
			Intent in = new Intent(getActivity(), WebViewActivity.class);
			
			in.putExtra("content", mVideoAdapter.getItem(pos - 1).title);//为了分享功能加入
			in.putExtra("url", mVideoAdapter.getItem(pos - 1).fromurl);
			in.putExtra("source",  mVideoAdapter.getItem(pos - 1).source);
			startActivity(in);
		}
	}
	private void swtichToFrame(int f) {
		frame = f;
		if(mode==0)
		{
			if (frame == 0) {
				mVideoBtn1.setTextColor(Color.WHITE);
				mVideoBtn1.setBackgroundResource(R.drawable.btn_rank_type_bg_down);
				mVideoBtn2.setBackgroundResource(R.drawable.btn_rank_type_selector);
				mVideoBtn2.setTextColor(Color.GRAY);
			} else {
				mVideoBtn2.setTextColor(Color.WHITE);
				mVideoBtn2.setBackgroundResource(R.drawable.btn_rank_type_bg_down);
				mVideoBtn1.setBackgroundResource(R.drawable.btn_rank_type_selector);
				mVideoBtn1.setTextColor(Color.GRAY);
			}
		}
	}
	
	@Override
	public void onSuccess(Object o, int reqType) {
		stopLoad(false);
		if (o != null)
			setData(reqType, o);
	}
	

	public void onFailure(Throwable error, int reqType) {
		stopLoad(false);
	}
	

}
