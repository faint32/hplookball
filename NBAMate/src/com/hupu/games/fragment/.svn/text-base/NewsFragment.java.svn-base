package com.hupu.games.fragment;

import java.util.LinkedList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.NewsDetailActivity;
import com.hupu.games.adapter.NewsListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.news.NewsEntity;
import com.hupu.games.data.news.NewsResp;
import com.hupu.games.view.XListView;
import com.hupu.games.view.XListView.IXListViewListener;

public class NewsFragment extends BaseFragment {

	private XListView mListNews;

	private NewsListAdapter mNewsAdapter;

	private LinkedList<NewsEntity> mNewsData;

	private long lastNewsId;

	/** 最后刷新的时间 */
	private long mLastNewsTime;

	/***/
	private static final long TEN_MINS = 120000;
	/***/
	boolean bNeedFresh;

	int mode = -1;

	private int methodNews;

	private int methodNewsNext;

	private String mTag;

	String cnTag;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		methodNews = HuPuRes.REQ_METHOD_GET_NBA_NEWS;
//		methodNewsNext = HuPuRes.REQ_METHOD_GET_NBA_NEWS_NEXT;
		if(mTag==null)
		{
			mTag = getArguments().getString("tag");
			cnTag = getArguments().getString("cnTag");
			setMode(getArguments().getInt("mode"));
		}
	}
	public void onResume(){
		super.onResume();
		mNewsAdapter.notifyDataSetChanged();
		
	}

	public void setMode(int m) {

		mLastNewsTime = 0;
		// if(m==mode)
		// return;

		mode = m;
		if (mode == 0) {
			methodNews = HuPuRes.REQ_METHOD_GET_NBA_NEWS;
			methodNewsNext = HuPuRes.REQ_METHOD_GET_NBA_NEWS_NEXT;
		} else if (mode == 1) {
			methodNews = HuPuRes.REQ_METHOD_GET_CBA_NEWS;
			methodNewsNext = HuPuRes.REQ_METHOD_GET_CBA_NEWS_NEXT;
		} else {
			// 足球和杯赛
			methodNews = HuPuRes.REQ_METHOD_GET_FOOTBALL_NEWS;
			methodNewsNext = HuPuRes.REQ_METHOD_GET_FOOTBALL_NEWS_NEXT;
		}

		if (mNewsAdapter != null) {
			mNewsAdapter.clear();
			// mListNews.setFreshState();
		}
		if (mListNews != null)
			mListNews.setPullLoadEnable(true, false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_news, container, false);

		mListNews = (XListView) v.findViewById(R.id.list_news);
		((TextView) mListNews.mFooterView
				.findViewById(R.id.xlistview_footer_text))
				.setText(getString(R.string.no_more_news));

		Log.d("News", "onCreateView");
		if (mNewsAdapter == null) {
			mNewsAdapter = new NewsListAdapter(getActivity());
			reqNewData(true);
			mNewsAdapter.setAct(mAct);
		}

		ListClick listClick = new ListClick();
		mListNews.setOnItemClickListener(listClick);
		mListNews.setXListViewListener(new pullListener());
		mListNews.setAdapter(mNewsAdapter);
		// 初始化时不需要刷新
		if (mNewsAdapter.getCount() > 0) {
			mListNews.setPullLoadEnable(true, false);
		} else
			mListNews.setPullLoadEnable(false, false);
		
		return v;
	}


	/** 获取最新数据 */
	public void reqNewData(boolean isMan) {
		if (!isMan)
			mListNews.setFreshState();
		reqNewsData(methodNews, 0);
	}

	int page;

	/** 获取更多数据 */
	public void reqMoreData() {
		reqNewsData(methodNewsNext, lastNewsId);
	}

	/** 请求新闻数据 */
	private void reqNewsData(int reqId, long nId) {
		mParams = mAct.getHttpParams(true);
		if (!mAct.isActiveFragment(this)) {
			mParams.put("preload", "1");
		}
		if (nId > 0)
			mParams.put("nid", "" + nId);
		switch (reqId) {
		case HuPuRes.REQ_METHOD_GET_NBA_NEWS_NEXT:
		case HuPuRes.REQ_METHOD_GET_CBA_NEWS_NEXT:
		case HuPuRes.REQ_METHOD_GET_FOOTBALL_NEWS_NEXT:
			mParams.put("direc", "next");
			break;
		}
//		// mAct.sendRequest(reqId, mParams);
//		if (mode < 2)
//			// mAct.sendRequest(reqId, mParams);
//			mAct.sendAppRequest(reqId, null, mParams, new FragmentHttpResponseHandler());
//		else
//			// mAct.sendTagRequest(reqId, mParams);
		HupuLog.e("papa", "-----mtag="+mTag+"----cntag="+cnTag+"---reqid="+reqId);
			mAct.sendAppRequest(reqId, mTag, mParams, new FragmentHttpResponseHandler());
	}
	

	/**
	 * 由其他的页面切换进入
	 * */
	public void entry() {
		// Log.d("News", "entry" +mLastNewsTime);
		if (mNewsAdapter != null) {
			long curTime = System.currentTimeMillis();
			if (mLastNewsTime == 0 || curTime - mLastNewsTime > TEN_MINS) {
				bNeedFresh = true;
			}
			if (bNeedFresh && !isToRead) {
				reqNewData(false);
			}
			isToRead = false;
		}
	}

	/**
	 * 赋值并刷新
	 * */
	public void setData(int method, Object o) {

		NewsResp resp = (NewsResp) o;
		// Log.d("data size=", ""+resp.mList.size());

		if (resp.nextDataExists > 0)
			mListNews.setPullLoadEnable(true, false);
		else {
			mListNews.setPullLoadEnable(false, true);
			if (methodNewsNext == method)
				mAct.showToast("没有更多新闻了");
		}

		if (method == methodNewsNext) {
			if (mNewsData == null)
				method = methodNews;
		} else {
			HupuLog.e("papa", "加载了新闻");
			mListNews.setPullLoadEnable(true, true);
			mLastNewsTime = System.currentTimeMillis();
			bNeedFresh = false;
		}

		if (method == methodNewsNext) {
			// mNewsData.addAll(0, resp.mList);
			if (resp.mList != null)
				mNewsData.addAll(resp.mList);
		} else {
			mNewsData = resp.mList;
			page = 0;
		}

		if (resp != null) {
			lastNewsId = resp.lastNId;
		}
		mNewsAdapter.setData(mNewsData);
	}

	/** 停止加载动画 */
	public void stopLoad(boolean bDelay) {
		// Log.d("video", "stopLoad");
		if (mListNews != null) {
			mListNews.stopRefresh();
			mListNews.stopLoadMore();
		}
	}

	/** 设置listview 上拉和下拉的监听 */
	class pullListener implements IXListViewListener {

		@Override
		public void onRefresh() {
			reqNewData(true);
		}

		@Override
		public void onLoadMore() {
			reqMoreData();
		}

	}

	private boolean isToRead = false;

	/** 设置listview点击监听器 */
	class ListClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
			if (pos < 1)
				return;
			if (mAct.mApp.getIsRead((int) mNewsAdapter.getItem(pos - 1).nid) != 1) {
				mAct.mApp.insertIsRead((int) mNewsAdapter.getItem(pos - 1).nid);
			}

			Intent in = new Intent(getActivity(), NewsDetailActivity.class);
			in.putExtra("nid", mNewsAdapter.getItem(pos - 1).nid);
			in.putExtra("reply", mNewsAdapter.getItem(pos - 1).replies);
			in.putExtra("tag", mTag);
			// in.putExtra("mode", mode);
			in.putExtra("cntag", cnTag);
			startActivity(in);
			isToRead = true;
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
