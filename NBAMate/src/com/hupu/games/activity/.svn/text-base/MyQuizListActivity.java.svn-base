/**
 * 
 */
package com.hupu.games.activity;

import java.io.Serializable;
import java.util.ArrayList;

import android.R.integer;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.MyCaipiaoListActivity.PlayerListClick;
import com.hupu.games.adapter.MyPrizeListAdapter;
import com.hupu.games.adapter.MyQuizListingAdapter;
import com.hupu.games.casino.CasinoDialog;
import com.hupu.games.casino.MyBoxActivity;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.HupuScheme;
import com.hupu.games.data.BalanceReq;
import com.hupu.games.data.IncreaseEntity;
import com.hupu.games.data.LiveEntity.Answer;
import com.hupu.games.data.game.quiz.QuizCommitResp;
import com.hupu.games.data.game.quiz.QuizEntity;
import com.hupu.games.data.game.quiz.QuizListResp;
import com.hupu.games.data.game.quiz.QuizResp;
import com.hupu.games.data.BitCoinReq;
import com.hupu.games.data.MyQuizResp;
import com.hupu.games.pay.PhoneInputActivity;
import com.hupu.games.view.HupuSectionedBaseAdapter;
import com.hupu.games.view.PinnedHeaderXListView;
import com.hupu.games.view.PinnedHeaderXListView.IXListViewListener;
import com.hupu.http.HupuHttpHandler;
import com.pyj.http.RequestParams;

/**
 * @author papa
 */
public class MyQuizListActivity extends HupuBaseActivity {

	PinnedHeaderXListView mListView;

	MyQuizListingAdapter mAdapter;
	Intent in;
	LayoutInflater mInflater;
	View v;
	int qid = 0;
	int page = 0;

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				getQuizList();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_quiz_list);
		in = getIntent();
		((TextView) findViewById(R.id.txt_title))
				.setText(getString(R.string.title_my_guess_result));
		if (!in.getStringExtra("guess_mark").equals("")) {
			((TextView) findViewById(R.id.guess_mark))
					.setText(getString(R.string.title_my_guess_result) + ": "
							+ in.getStringExtra("guess_mark"));
		}
		mInflater = LayoutInflater.from(this);
		qid = in.getIntExtra("qid", 0);
		mListView = (PinnedHeaderXListView) findViewById(R.id.list_player);
		mListView.setPullLoadEnable(false, true);
		// 默认关闭底部 没有评论了的提示
		mListView.mFooterView.findViewById(R.id.xlistview_footer_text)
				.setVisibility(View.GONE);
		((TextView) mListView.mFooterView
				.findViewById(R.id.xlistview_footer_text))
				.setText(getString(R.string.no_more_guess));

		mListView.setXListViewListener(new pullListener());
		getQuizList();
		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.gold_num);
		findViewById(R.id.gold_num).setVisibility(View.GONE);

		mListView.setOnItemClickListener(new PlayerListClick());

	}

	/** 设置listview 上拉和下拉的监听 */
	class pullListener implements IXListViewListener {

		@Override
		public void onRefresh() {

			// 加载最新竞猜数据
			getQuizList();
		}

		@Override
		public void onLoadMore() {
			HupuLog.e("papa", "page=" + page);
			if (page > 1) {
				reqMoreData();
			}
		}

	}

	private MyQuizResp quizList;

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
	 * 注释：点击比赛详情跳入直播页面 功能取消
	 */
	// @Override
	// public void treatClickEvent(View view) {
	// int id = view.getId();
	// switch (id) {
	//
	// case R.id.header_view:
	// int pos = Integer.parseInt(view.getTag().toString());
	// Intent in = new Intent(this, QuizListActivity.class);
	// in.putExtra("lid", Integer.parseInt(quizList.list.get(pos).lid));
	// in.putExtra("gid", Integer.parseInt(quizList.list.get(pos).gid));
	// startActivityForResult(in, HupuBaseActivity.REQ_SHOW_QUIZLIST);
	// break;
	//
	// default:
	// break;
	// }
	// }

	@Override
	public void treatClickEvent(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.open_result:

			Intent info = new Intent(this, UserGoldActivity.class);
			startActivity(info);
			break;
		}
	}

	void switchToPhoneBindAct() {
		Intent intent = new Intent(MyQuizListActivity.this,
				PhoneInputActivity.class);
		startActivityForResult(intent, HupuBaseActivity.REQ_GO_BIND_PHONE);
	}

	private void getQuizList() {
		initParameter();
		mParams.put("token", mToken);
		mParams.put("page", "1");
		sendRequest(HuPuRes.REQ_METHOD_MY_QUIZ_LIST, mParams,
				new HupuHttpHandler(this), false);
	}

	private void reqMoreData() {

		initParameter();
		mParams.put("token", mToken);
		mParams.put("page", page + "");
		sendRequest(HuPuRes.REQ_METHOD_MY_QUIZ_MORE_LIST, mParams,
				new HupuHttpHandler(this), false);
	}

	private void setSelection(QuizResp list, int qid) {
		int size = 0;
		for (QuizListResp quizList : list.list) {
			if (quizList.status == 2) {
				int i = 0;
				for (QuizEntity entity : quizList.mQuizList) {
					if (entity.qid == qid) {
						size += i;
						break;
					}
					i++;
				}
				break;
			}
			size += quizList.mQuizList.size();
		}
		if (size > 0 && size < mAdapter.getCount()) {
			mListView.setSelection(size);
		}
		qid = 0;
	}

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		//
		if (o != null) {
			HupuLog.e("papa", "methodId" + methodId);
			if (methodId == HuPuRes.REQ_METHOD_MY_QUIZ_LIST) {
				quizList = (MyQuizResp) o;
				HupuLog.e("papa", "quizList==" + quizList.currPage);
				if (quizList.currPage < quizList.totalPage) {
					page = quizList.currPage + 1;
					// 开启加载更多
					mListView.setPullLoadEnable(true, true);
				} else {
					mListView.setPullLoadEnable(false, true);
				}
				if (quizList.list != null && quizList.list.size() > 0) {
					findViewById(R.id.img_no_date).setVisibility(View.GONE);
				} else {
					findViewById(R.id.img_no_date).setVisibility(View.VISIBLE);
					mListView.setPullLoadEnable(false, false);
				}

				mAdapter = new MyQuizListingAdapter(this, click);

				mAdapter.setData(quizList.list);
				mListView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();

				mListView.stopRefresh();
				mListView.stopLoadMore();

			} else if (methodId == HuPuRes.REQ_METHOD_MY_QUIZ_MORE_LIST) {

				MyQuizResp morequizList = (MyQuizResp) o;
				if (morequizList.currPage < morequizList.totalPage) {
					page++;
					mListView.setPullLoadEnable(true, true);
				} else {
					mListView.setPullLoadEnable(false, true);
				}
				HupuLog.e("papa", "moresize===" + morequizList.list.size());
				for (int i = 0; i < morequizList.list.size(); i++) {
					quizList.list.add(morequizList.list.get(i));
				}
				mAdapter.setData(quizList.list);
				mAdapter.notifyDataSetChanged();

				mListView.stopRefresh();
				mListView.stopLoadMore();

			}
		}
	}

	class PlayerListClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			int section = mAdapter.getSectionForPosition(pos - 1);
			int child = mAdapter.getPositionInSectionForPosition(pos - 1);
			// mAdapter.getChildPosition(pos)

			HupuLog.e("papa", "section===" + section + "------child=" + child
					+ "--------quizList.list.get(section).gid==="
					+ quizList.list.get(section).scheme);

			if (child < 0 && !quizList.list.get(section).scheme.equals("") && !quizList.list.get(section).lid.equals("")) {

/*				Intent in = new Intent(MyQuizListActivity.this,
						NBAGameActivity.class);
				in.putExtra("gid",
						Integer.parseInt(quizList.list.get(section).gid));
				in.putExtra("lid",
						Integer.parseInt(quizList.list.get(section).lid));
				in.putExtra("tab", "casino");
				startActivity(in);*/
				
				treatScheme(quizList.list.get(section).scheme, Integer.parseInt(quizList.list.get(section).lid));
			}

		}

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
		}
		return false;
	}

}
