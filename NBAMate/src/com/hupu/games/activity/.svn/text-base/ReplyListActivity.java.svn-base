/**
 * 
 */
package com.hupu.games.activity;

import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.adapter.ReplyListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.ReplyRespEntity;
import com.hupu.games.data.game.quiz.QuizCommitResp;
import com.hupu.games.data.news.NewsLightEntity;
import com.hupu.games.view.PinnedHeaderXListView;
import com.hupu.games.view.PinnedHeaderXListView.IXListViewListener;
import com.hupu.http.HupuHttpHandler;

/**
 * @author panyongjun 上墙榜 需要lid和gid
 */
public class ReplyListActivity extends HupuBaseActivity {

	PinnedHeaderXListView mListView;

	ReplyListAdapter mAdapter;
	Intent in;
	int reqMethodId = HuPuRes.REQ_METHOD_GET_REPLY;
	boolean isMore = true;
	LayoutInflater inflater;

	private TextView newsTitle, newsTime, newsOrigin;
	private EditText commitContent;
	InputMethodManager inputMethodManager;

	View popView;

	RelativeLayout commitLayout;
	ImageView commitImg;
	ImageButton sendImg;
	long replyId;
	boolean isTouch = false;

	String tag;

	String hid;
	boolean haveHid = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater = getLayoutInflater();
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		setContentView(R.layout.layout_reply_list);
		in = getIntent();
		if (in.getStringExtra("hid") != null) {
			hid = in.getStringExtra("hid");
			haveHid = true;
			reqMethodId = HuPuRes.REQ_METHOD_GET_LINK_REPLY;
		} else
			haveHid = false;

		getReplyList(true);
		mListView = (PinnedHeaderXListView) findViewById(R.id.list_player);
		mListView.setPullLoadEnable(true, false);

		// 默认关闭底部 没有评论了的提示
		mListView.mFooterView.findViewById(R.id.xlistview_footer_text)
				.setVisibility(View.GONE);

		mListView.setXListViewListener(new pullListener());

		commitLayout = (RelativeLayout) findViewById(R.id.commit_layout);
		commitImg = (ImageView) findViewById(R.id.to_reply_img);

		View mView = inflater.inflate(R.layout.item_add_replay_top, null);
		newsTitle = (TextView) mView.findViewById(R.id.news_title);
		newsTime = (TextView) mView.findViewById(R.id.news_time);
		newsOrigin = (TextView) mView.findViewById(R.id.news_origin);

		if (!haveHid) {
			tag = in.getStringExtra("tag");
			newsTime.setText(in.getStringExtra("time"));
			newsOrigin.setText(in.getStringExtra("origin"));
		} else {
			mView.findViewById(R.id.news_info).setVisibility(View.GONE);
		}
		newsTitle.setText(in.getStringExtra("title"));
		mListView.addHeaderView(mView);

		sendImg = (ImageButton) findViewById(R.id.commit_reply);

		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.commit_reply);
		setOnClickListener(R.id.to_reply_img);
		setOnClickListener(R.id.close_reply);
		setOnClickListener(R.id.commit_layout_bg);

		commitContent = (EditText) findViewById(R.id.reply_text_content);
		/*
		 * 监控评论的hint显示
		 */
		commitContent.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					commitContent.setHint(R.string.reply_hint_text);
				}
			}
		});
		commitContent.addTextChangedListener(mTextWatcher);

	}

	/**
	 * 输入监控
	 */
	private TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			sendImg.setBackgroundResource(s.length() > 0 ? R.drawable.btn_reply_commit
					: R.drawable.un_send_btn);
			sendImg.setEnabled(s.length() > 0 ? true : false);
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			sendImg.setBackgroundResource(s.length() > 0 ? R.drawable.btn_reply_commit
					: R.drawable.un_send_btn);
			sendImg.setEnabled(s.length() > 0 ? true : false);

		}

	};

	/** 设置listview 上拉和下拉的监听 */
	class pullListener implements IXListViewListener {

		@Override
		public void onRefresh() {
			// 加载最新评论数据
			reqMethodId = haveHid ? HuPuRes.REQ_METHOD_GET_LINK_REPLY
					: HuPuRes.REQ_METHOD_GET_REPLY;
			getReplyList(true);
		}

		@Override
		public void onLoadMore() {

			if (resp != null && !resp.isLast) {
				reqMethodId = haveHid ? HuPuRes.REQ_METHOD_GET_MORE_LINK_REPLY
						: HuPuRes.REQ_METHOD_GET_MORE_REPLY;
				getReplyList(false);
			} else {
				// showToast("已经到底啦");
				// mListView.mFooterView.findViewById(R.id.xlistview_footer_text).setVisibility(View.VISIBLE);
				// mListView.mFooterView.findViewById(R.id.xlistview_footer_progressbar).setVisibility(View.GONE);
				/*
				 * if (!isTouch) { mListView.setPullLoadEnable(false, false);
				 * }else { mListView.setPullLoadEnable(false, true); }
				 */

				// mListView.setPullLoadEnable(false, true);
				mListView.stopLoadMore();
			}
		}
	}

	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);
		switch (id) {
		case R.id.btn_back:
			finish();
			switchReplyLayout(false);
			break;
		case R.id.commit_reply:
			if (!commitContent.getText().toString().equals("")) {
				switchReplyLayout(false);

				initParameter();
				mParams.put("content", commitContent.getText().toString());
				if (haveHid) {
					mParams.put("hid", hid);
					sendRequest(HuPuRes.REQ_METHOD_POST_LINK_REPLY, mParams,
							new HupuHttpHandler(this), false);
				} else {
					mParams.put("nid", "" + in.getLongExtra("nid", 1));
					sendRequest(HuPuRes.REQ_METHOD_POST_REPLY,
							in.getStringExtra("tag"), mParams,
							new HupuHttpHandler(this), false);
				}

			} else {
				showToast("请输入评论内容");
			}
			break;

		case R.id.to_reply_img:
			replyId = 0;
			if (mAdapter != null) {
				mAdapter.setReplyId(0, replyId);
				mAdapter.notifyDataSetChanged();
			}

			if (SharedPreferencesMgr.getString(HuPuRes.KEY_NICK_NAME, "")
					.equals("")) {
				Intent inAccount = new Intent(this, NickNameActivity.class);
				startActivity(inAccount);
			} else {
				switchReplyLayout(true);
			}
			break;
		case R.id.close_reply:
			switchReplyLayout(false);
			break;

		default:
			break;
		}
	}

	@Override
	public void treatClickEvent(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.reply_item:
			String[] strings = String.valueOf(view.getTag()).split(",");
			if (replyId != Long.valueOf(strings[1])) {
				replyId = Long.valueOf(strings[1]);
				mAdapter.setReplyId(Integer.parseInt(strings[0]), replyId);
				mAdapter.notifyDataSetChanged();
			} else {
				replyId = 0;
				mAdapter.setReplyId(0, replyId);
				mAdapter.notifyDataSetChanged();
			}
			// view.findViewById(R.id.pop_layout).setVisibility(View.VISIBLE);
			break;

		case R.id.like_num:
		case R.id.to_light:

			String[] lightInfo = String.valueOf(view.getTag()).split(",");
			switch (Integer.parseInt(lightInfo[1])) {
			case 0:
				lightType = 1;
				initParameter();
				mParams.put("ncid", lightInfo[0]);
				mParams.put("type", "1");
				if (haveHid) {
					sendRequest(HuPuRes.REQ_METHOD_POST_LINK_LIGHT, mParams,
							new HupuHttpHandler(this), false);
				} else {
					sendRequest(HuPuRes.REQ_METHOD_POST_LIGHT,
							in.getStringExtra("tag"), mParams,
							new HupuHttpHandler(this), false);
				}
				break;
			case 1:
				showToast(getString(R.string.lighted));
				break;
			case 2:
				showToast(getString(R.string.unlighted));
				break;

			default:
				break;
			}

			break;
		case R.id.uplight_num:
		case R.id.to_unlight:

			String[] unLightInfo = String.valueOf(view.getTag()).split(",");
			switch (Integer.parseInt(unLightInfo[1])) {
			case 0:
				lightType = 2;
				initParameter();
				mParams.put("ncid", unLightInfo[0]);
				mParams.put("type", "2");
				if (haveHid) {
					sendRequest(HuPuRes.REQ_METHOD_POST_LINK_LIGHT, mParams,
							new HupuHttpHandler(this), false);
				} else {
					sendRequest(HuPuRes.REQ_METHOD_POST_LIGHT,
							in.getStringExtra("tag"), mParams,
							new HupuHttpHandler(this), false);
				}
				break;
			case 1:
				showToast(getString(R.string.lighted));
				break;
			case 2:
				showToast(getString(R.string.unlighted));
				break;

			default:
				break;
			}

			break;
		case R.id.to_quote:
			if (SharedPreferencesMgr.getString(HuPuRes.KEY_NICK_NAME, "")
					.equals("")) {
				Intent inAccount = new Intent(this, NickNameActivity.class);
				startActivity(inAccount);
			} else {

				replyId = 0;
				mAdapter.setReplyId(0, replyId);
				mAdapter.notifyDataSetChanged();

				commitContent.setText("@" + view.getTag().toString() + " ");
				switchReplyLayout(true);
				Editable etext = commitContent.getEditableText();

				Selection.setSelection(etext, etext.length());
			}
			break;
		case R.id.commit_layout_bg:
			switchReplyLayout(false);
			break;

		default:
			break;
		}
	}

	private void getReplyList(boolean isRefresh) {
		initParameter();
		if (resp != null && !isRefresh) {
			mParams.put("ncid", resp.lastNId + "");
			mParams.put("create_time", resp.lastTime);
		}
		if (haveHid) {
			mParams.put("hid", hid);
			mParams.put("num", "" + 20);
			sendRequest(reqMethodId, mParams, new HupuHttpHandler(this), false);
		} else {
			mParams.put("nid", "" + in.getLongExtra("nid", 1));
			mParams.put("num", "" + 20);
			sendRequest(reqMethodId, in.getStringExtra("tag"), mParams,
					new HupuHttpHandler(this), false);
		}

	}

	private ReplyRespEntity resp;
	private int lightType = 0;

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		if (methodId == HuPuRes.REQ_METHOD_GET_REPLY
				|| methodId == HuPuRes.REQ_METHOD_GET_LINK_REPLY) {
			if (o != null) {
				resp = (ReplyRespEntity) o;
				mAdapter = new ReplyListAdapter(this, click);
				mAdapter.setData(resp);
				mListView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				mListView.setOnScrollListener(new OnScrollListener() {
					@Override
					public void onScrollStateChanged(AbsListView view,
							int scrollState) {
						// TODO Auto-generated method stub
						isTouch = true;
						if (replyId != 0) {
							replyId = 0;
							mAdapter.setReplyId(0, replyId);
							mAdapter.notifyDataSetChanged();
						}
					}

					@Override
					public void onScroll(AbsListView view,
							int firstVisibleItem, int visibleItemCount,
							int totalItemCount) {
						// TODO Auto-generated method stub
					}
				});

				mListView.stopRefresh();
				mListView.stopLoadMore();
				// page = resp.page;
				// if (resp.page == resp.pages) {
				// isMore = false;
				// }
				if (resp.replyList != null) {
					findViewById(R.id.no_comment).setVisibility(
							resp.replyList.size() > 0 ? View.GONE
									: View.VISIBLE);
					mListView
							.setVisibility(resp.replyList.size() > 0 ? View.VISIBLE
									: View.GONE);

					// //默认关闭底部 没有评论了的提示
					// mListView.mFooterView.findViewById(R.id.xlistview_footer_text).setVisibility(View.GONE);
				} else {
					findViewById(R.id.no_comment).setVisibility(View.VISIBLE);
					mListView.setVisibility(View.GONE);
				}
			}
		} else if (methodId == HuPuRes.REQ_METHOD_GET_MORE_REPLY
				|| methodId == HuPuRes.REQ_METHOD_GET_MORE_LINK_REPLY) {
			if (o != null) {
				ReplyRespEntity moreResp = (ReplyRespEntity) o;
				for (int i = 0; i < moreResp.replyList.size(); i++) {
					resp.replyList.add(moreResp.replyList.get(i));
				}
				resp.lastNId = moreResp.lastNId;
				resp.lastTime = moreResp.lastTime;
				resp.isLast = moreResp.isLast;

				mAdapter.setData(resp);
				mAdapter.notifyDataSetChanged();
				mListView.stopRefresh();
				mListView.stopLoadMore();
				// page = moreResp.page;
				// if (moreResp.page == moreResp.pages) {
				// isMore = false;
				// }
			}
		} else if (methodId == HuPuRes.REQ_METHOD_POST_REPLY
				|| methodId == HuPuRes.REQ_METHOD_POST_LINK_REPLY) {
			QuizCommitResp result = (QuizCommitResp) o;
			if (result.result > 0) {
				NewsLightEntity entity = new NewsLightEntity();
				entity.user_name = SharedPreferencesMgr.getString(
						HuPuRes.KEY_NICK_NAME, "");
				entity.ncid = result.result;
				entity.content = commitContent.getText().toString();
				entity.light_count = "0";
				entity.unlight_count = "0";
				entity.create_time = "";
				if (resp.replyList == null) {
					resp.replyList = new LinkedList<NewsLightEntity>();
					resp.replyList.add(0, entity);
					findViewById(R.id.no_comment).setVisibility(
							resp.replyList.size() > 0 ? View.GONE
									: View.VISIBLE);
					mListView
							.setVisibility(resp.replyList.size() > 0 ? View.VISIBLE
									: View.GONE);
				} else {
					resp.replyList.add(0, entity);
				}
				mAdapter.setData(resp);
				mAdapter.notifyDataSetChanged();

				showToast("评论成功！");
				commitContent.setText("");
				commitContent.setHint(R.string.reply_hint_text);
			} else {
				showToast("评论失败");
			}

		} else if (methodId == HuPuRes.REQ_METHOD_POST_LIGHT
				|| methodId == HuPuRes.REQ_METHOD_POST_LINK_LIGHT) {
			QuizCommitResp result = (QuizCommitResp) o;
			if (result.result == 1) {
				// mAdapter.addLightNum(replyId);
				mAdapter.startAnim();
				mAdapter.addLightNum(replyId, lightType);
				mAdapter.notifyDataSetChanged();
			} else {

			}
		}

		if (resp != null && resp.isLast) {
			mListView.setPullLoadEnable(false, true);
		}
	}

	private void switchReplyLayout(boolean isShowEdit) {
		if (isShowEdit) {

			commitLayout.setVisibility(View.VISIBLE);
			findViewById(R.id.commit_layout_bg).setVisibility(View.VISIBLE);
			commitImg.setVisibility(View.GONE);
			commitContent.requestFocus();
			inputMethodManager.showSoftInput(commitContent,
					InputMethodManager.SHOW_IMPLICIT);
		} else {

			commitContent.clearFocus();
			inputMethodManager.hideSoftInputFromWindow(
					commitContent.getWindowToken(), 0);
			commitLayout.setVisibility(View.GONE);
			findViewById(R.id.commit_layout_bg).setVisibility(View.GONE);
			commitImg.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			if (commitLayout.getVisibility() == View.VISIBLE) {
				switchReplyLayout(false);
			} else {
				finish();
			}
		}
		return false;
	}

}
