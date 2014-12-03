package com.hupu.games.activity;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.common.DoubleClickUtil;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.LeaguesEntity;
import com.hupu.games.data.game.quiz.QuizCommitResp;
import com.hupu.games.data.news.NewsDetailEntity;
import com.hupu.games.data.news.NewsLightEntity;
import com.hupu.games.handler.IWebViewClientEvent;
import com.hupu.games.util.TimeUtile;
import com.hupu.games.view.HupuWebView;
import com.hupu.http.HupuHttpHandler;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.umeng.socialize.sso.UMSsoHandler;

/**
 * 该页面需要被分享 分享的内容包括新闻标题，文字，配图
 * */
public class NewsDetailActivity extends HupuBaseActivity implements
IWebViewClientEvent {

	// private WebView mWebView;

	private long nid;
	private HupuWebView newsDetail;
	private String html = "<!DOCTYPE html>"
			+ "<html><head><meta http-equiv=\"Content-Type\" content=\"textml; charset=utf-8\"/>"
			+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no\" /><title>tilte</title></head>"
			+ "<style type=\"text/css\">body,dl,dt,dt,ul,ol,li,h1,h2,h3,p{padding:0;margin:0;text-align:justify;} ol,ul{list-style:none}.html-body-overflow{overflow-x:hidden;overflow-y:hidden;}"
			+ ".article-main{width:100%;font:17px/1.5 'Adobe \\9ed1\\4f53 Std',\\9ed1\\4f53,\"Arial\";text-align:justify;}.article-main a{color:#004499;text-decoration: none}.article-main a:line{text-decoration:none}"
			+ ".article-main .bd{padding-left:24px;padding-right:24px; solid #fff}.article-main .bd p{margin:.5em 0 .2em;text-indent:2em;} </style><body style = \"background-color:#eee\"><div class=\"article-main\"><div class=\"bd\">";
	private String detailConent = "";

	private TextView txtReplies, newsTitle, newsTime, newsOrigin,
	commentAuthor, beforeTime, replyContent, lightNum, likeNum,
	unLightNum;
	ImageButton btn_quote, btn_light, btn_unlight;
	EditText commitContent;
	private ImageView newsImg;
	private boolean isDownload = false;
	private int reply;
	private Animation myAnimation_Translate;
	View progressbar;

	/** 区分哪个联赛的新闻 */
	private String tag;

	private SimpleDateFormat format;
	private boolean inAnimation = false;

	LinearLayout hotReplyLayout;

	LayoutInflater inflater;

	InputMethodManager inputMethodManager;

	RelativeLayout commitLayout;

	ImageView commitImg, newLine;

	ImageButton sendImg;

	NewsDetailEntity entity;

	static final String mimeType = "text/html";

	static final String encoding = "utf-8";

	private String queryStr = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		format = new SimpleDateFormat("MM-dd HH:mm");
		inflater = getLayoutInflater();
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		setContentView(R.layout.layout_news_detail);
		init();
		reqNews();
	}

	@SuppressLint("NewApi")
	@TargetApi(8)
	private void init() {
		Intent intent = getIntent();
		nid = intent.getLongExtra("nid", 0);
		queryStr = intent.getStringExtra("Query");
		HupuLog.e("papa", "------query=" + queryStr);

		if (nid <= 0)// 如果是非法新闻id
			finish();
		reply = intent.getIntExtra("reply", 0);
		tag = intent.getStringExtra("tag");
		newsTitle = (TextView) findViewById(R.id.news_title);
		newsTime = (TextView) findViewById(R.id.news_time);
		newsOrigin = (TextView) findViewById(R.id.news_origin);
		newsDetail = (HupuWebView) findViewById(R.id.news_detail);
		newsDetail.setWebViewClientEventListener(this, true);
		if (android.os.Build.VERSION.SDK_INT > 11) {
			newsDetail.setLayerType(View.LAYER_TYPE_NONE, null);
		}

		newsImg = (ImageView) findViewById(R.id.news_img);
		newLine = (ImageView) findViewById(R.id.line);
		isDownload = false;
		hotReplyLayout = (LinearLayout) findViewById(R.id.news_hot_reply);
		hotReplyLayout.setVisibility(View.GONE);
		commitLayout = (RelativeLayout) findViewById(R.id.commit_layout);
		commitImg = (ImageView) findViewById(R.id.to_reply_img);
		sendImg = (ImageButton) findViewById(R.id.commit_reply);

		String cnTag = intent.getStringExtra("cntag");
		if (cnTag == null)
			cnTag = findCnTag(tag);
		((TextView) findViewById(R.id.txt_title)).setText(cnTag + "新闻");

		txtReplies = (TextView) findViewById(R.id.reply_num);
		progressbar = findViewById(R.id.probar);

		txtReplies.setText(reply + "");
		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.to_jump_img);
		setOnClickListener(R.id.reply_num);
		setOnClickListener(R.id.commit_reply);
		setOnClickListener(R.id.to_reply_img);
		setOnClickListener(R.id.close_reply);
		setOnClickListener(R.id.commit_layout_bg);
		// setOnClickListener(R.id.news_hot_reply);

		btnShare = findViewById(R.id.btn_share);
		setShareEnable(false);
		setOnClickListener(R.id.btn_share);
		// setOnClickListener(R.id.news_detail_layout);

		commitContent = (EditText) findViewById(R.id.reply_text_content);
		// commitContent.setFocusable(false);
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

		// newsDetail.getSettings().setBlockNetworkImage(true);
		newsDetail.getSettings().setRenderPriority(RenderPriority.HIGH);
		newsDetail.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		newsDetail.setVisibility(View.GONE);
		newsDetail.clearView();

	}

	/**
	 * 用来延迟显示 热门评论内容
	 */
	private Handler handler = new Handler();

	private Runnable runnable = new Runnable() {
		public void run() {
			hotReplyLayout.setVisibility(View.VISIBLE);
		}
	};

	/**
	 * 输入监控
	 */
	private TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {

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

	private void reqNews() {
		initParameter();
		mParams.put("nid", "" + nid);
		if (queryStr != null && !"".equals(queryStr)) {
			Uri uri = Uri.parse(queryStr);
			getQueryParameterNames();

		}
		sendRequest(HuPuRes.REQ_METHOD_GET_NEWS_DETAIL, tag, mParams,
				new HupuHttpHandler(this), false);
	}

	/**
	 * 任务中查看了新闻 需要在请求接口中加入相应参数
	 * 
	 * @return
	 */
	public Set<String> getQueryParameterNames() {

		String query = queryStr;
		if (query == null) {
			return Collections.emptySet();
		}

		Set<String> names = new LinkedHashSet<String>();
		int start = 0;
		do {
			int next = query.indexOf('&', start);
			int end = (next == -1) ? query.length() : next;

			int separator = query.indexOf('=', start);
			if (separator > end || separator == -1) {
				separator = end;
			}

			String name = query.substring(start, separator);
			HupuLog.e("papa", "parameter====" + query.substring(separator, end));
			mParams.put(name, query.substring(separator, end));
			names.add(name);

			// Move start to end of name.
			start = end + 1;
		} while (start < query.length());

		return Collections.unmodifiableSet(names);
	}

	// private HashMap<String, String> UMENG_MAP = new HashMap<String,
	// String>();

	private ImageView hotLine;
	private LinearLayout hotTopLayout;

	private void setLightInfo(NewsDetailEntity entity) {
		startAnim();
		hotLine = (ImageView) hotReplyLayout.findViewById(R.id.hot_line);
		hotTopLayout = (LinearLayout) hotReplyLayout
				.findViewById(R.id.hot_layout);
		hotReplyLayout.removeAllViews();
		hotReplyLayout.addView(hotLine);
		hotReplyLayout.addView(hotTopLayout);
		findViewById(R.id.from_text).setVisibility(View.VISIBLE);
		for (NewsLightEntity lightEntity : entity.lightList) {
			View mView = inflater.inflate(R.layout.item_hot_reply, null);
			commentAuthor = (TextView) mView.findViewById(R.id.comment_author);
			beforeTime = (TextView) mView.findViewById(R.id.before_time);
			replyContent = (TextView) mView.findViewById(R.id.reply_content);
			lightNum = (TextView) mView.findViewById(R.id.light_num);
			likeNum = (TextView) mView.findViewById(R.id.like_num);
			unLightNum = (TextView) mView.findViewById(R.id.uplight_num);

			btn_quote = (ImageButton) mView.findViewById(R.id.to_quote);
			btn_light = (ImageButton) mView.findViewById(R.id.to_light);
			btn_unlight = (ImageButton) mView.findViewById(R.id.to_unlight);

			btn_quote.setTag(lightEntity.user_name);
			btn_quote.setOnClickListener(click);
			btn_light.setTag(lightEntity.lighted + "");
			btn_light.setOnClickListener(click);
			btn_unlight.setTag(lightEntity.lighted + "");
			btn_unlight.setOnClickListener(click);

			likeNum.setTag(lightEntity.lighted + "");
			likeNum.setOnClickListener(click);
			unLightNum.setTag(lightEntity.lighted + "");
			unLightNum.setOnClickListener(click);

			switch (lightEntity.lighted) {
			case 0:

				break;
			case 1:
				btn_light.setBackgroundResource(R.drawable.btn_light_down);
				likeNum.setTextColor(Color.parseColor("#ba0000"));
				break;
			case 2:
				btn_unlight.setBackgroundResource(R.drawable.btn_unlight_down);
				unLightNum.setTextColor(Color.parseColor("#ba0000"));
				break;

			default:
				break;
			}

			commentAuthor.setText(lightEntity.user_name);
			// beforeTime.setText(replyEntity.created_at);
			beforeTime.setText(TimeUtile.getReplytime(format.format(new Date(
					(Long.parseLong(lightEntity.create_time) * 1000L)))));
			replyContent.setText(lightEntity.content);
			likeNum.setText(lightEntity.light_count);
			unLightNum.setText(lightEntity.unlight_count);
			lightNum.setText("亮了("
					+ (Integer.parseInt(lightEntity.light_count) - Integer
							.parseInt(lightEntity.unlight_count)) + ")");
			mView.setTag(lightEntity.ncid);
			hotReplyLayout.addView(mView);
			mView.setOnClickListener(click);
		}

	}

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		if (o != null) {
			progressbar.setVisibility(View.GONE);
			if (methodId == HuPuRes.REQ_METHOD_POST_REPLY) {
				QuizCommitResp result = (QuizCommitResp) o;
				if (result.result > 0) {
					showToast("评论成功！");
					commitContent.setText("");
					commitContent.setHint(R.string.reply_hint_text);
				} else {
					showToast("评论失败");
				}

			} else if (methodId == HuPuRes.REQ_METHOD_POST_LIGHT) {
				HupuLog.e("papa", "点亮成功");
			} else {
				entity = (NewsDetailEntity) o;
				setShareEnable(true);
				if (entity != null && entity.title != null) {
					txtReplies.setText(entity.replies + "");
					newsTitle.setText(entity.title);
					detailConent = entity.content;
					html = html + detailConent + "</div></div></body></html>";
					newsDetail.loadDataWithBaseURL(null, html, "text/html",
							"utf-8", null);
					// newsDetail.setVisibility(View.VISIBLE);
					newsOrigin.setText(entity.origin);

					UrlImageViewHelper.isLocalFile(this, entity.mImg);
					newsImg.setVisibility(View.VISIBLE);
					newLine.setVisibility(View.VISIBLE);
					if (SharedPreferencesMgr.getBoolean("is_no_pic", true)) {
						if (UrlImageViewHelper.isLocalFile(this, entity.mImg)) {
							UrlImageViewHelper.loadUrlDrawable(
									NewsDetailActivity.this, entity.mImg,
									new downLoadOk());
						} else {
							if (!checkNetIs2Gor3G()) {
								UrlImageViewHelper.loadUrlDrawable(
										NewsDetailActivity.this, entity.mImg,
										new downLoadOk());
							} else {
								newsImg.setBackgroundResource(R.drawable.bg_newsdetail_no_pic_3g);
								setOnClickListener(R.id.news_img);
							}
						}
					} else {
						UrlImageViewHelper.loadUrlDrawable(
								NewsDetailActivity.this, entity.mImg,
								new downLoadOk());
					}

					newsTime.setText(TimeUtile.getReplytime(format
							.format(new Date(
									(Long.parseLong(entity.addtime) * 1000L)))));

					findViewById(R.id.from_text).setVisibility(View.VISIBLE);
					if (entity.lightList != null && entity.lightList.size() > 0) {
						setLightInfo(entity);
					}

					if (entity.unShare == 1) {
						btnShare.setEnabled(false);
					}
				} else {
					showToast(getString(R.string.no_news_detail));
					this.finish();
				}
			}
		}
	}
	@Override
	public void onErrResponse(Throwable error, int type) {
		super.onErrResponse(error, type);
		progressbar.setVisibility(View.GONE);

	}

	/**
	 * 处理隐藏所有
	 */

	private void editLight(int type) {
		if (!inAnimation) {
			int replyID = Integer.parseInt(replyItem.getTag().toString());
			initParameter();
			mParams.put("ncid", replyItem.getTag().toString());
			mParams.put("type", type + "");
			sendRequest(HuPuRes.REQ_METHOD_POST_LIGHT, tag, mParams,
					new HupuHttpHandler(this), false);
			for (int i = 0; i < entity.lightList.size(); i++) {
				if (replyID == entity.lightList.get(i).ncid) {
					int likeNum = Integer
							.parseInt(entity.lightList.get(i).light_count);
					int unLikeNum = Integer
							.parseInt(entity.lightList.get(i).unlight_count);
					entity.lightList.get(i).light_count = (likeNum + (type == 1 ? 1
							: 0))
							+ "";
					entity.lightList.get(i).unlight_count = (unLikeNum + (type == 2 ? 1
							: 0))
							+ "";
					entity.lightList.get(i).lighted = type;
					break;
				}
			}
			animView = (type == 2 ? replyItem
					.findViewById(R.id.add_unlight_num) : replyItem
					.findViewById(R.id.add_light_num));
			animView.setVisibility(View.VISIBLE);
			animView.setAnimation(myAnimation_Translate);
			animView.startAnimation(myAnimation_Translate);
		}

	}

	int replyId = -1;
	private View replyItem;

	@Override
	public void treatClickEvent(View v) {
		super.treatClickEvent(v);
		int id = v.getId();
		switch (id) {
		case R.id.reply_item:
			if (replyItem == null) {
				replyItem = v;
				replyItem.findViewById(R.id.pop_layout).setVisibility(
						View.VISIBLE);
				replyId = Integer.parseInt(v.getTag().toString());
			} else {
				if (replyId == Integer.parseInt(v.getTag().toString())) {
					replyItem.findViewById(R.id.pop_layout).setVisibility(
							View.GONE);
					replyId = -1;
				} else {
					v.findViewById(R.id.pop_layout).setVisibility(View.VISIBLE);
					if (replyId != -1) {
						replyItem.findViewById(R.id.pop_layout).setVisibility(
								View.GONE);
					}
					replyId = Integer.parseInt(v.getTag().toString());
				}
				replyItem = v;
			}

			// ((ImageButton)replyItem.findViewById(R.id.to_quote)).setOnClickListener(click);
			// ((ImageButton)replyItem.findViewById(R.id.to_light)).setOnClickListener(click);
			// ((ImageButton)replyItem.findViewById(R.id.to_unlight)).setOnClickListener(click);

			break;
		case R.id.to_quote:
			if (SharedPreferencesMgr.getString(HuPuRes.KEY_NICK_NAME, "")
					.equals("")) {
				Intent inAccount = new Intent(this, NickNameActivity.class);
				startActivity(inAccount);
			} else {
				commitContent.setText("@" + v.getTag().toString() + " ");
				switchReplyLayout(true);
				Editable etext = commitContent.getEditableText();
				Selection.setSelection(etext, etext.length());

				setLightInfo(entity);
			}
			break;
		case R.id.like_num:
		case R.id.to_light:
			int lighted = Integer.parseInt(v.getTag().toString());
			if (lighted == 0) {
				editLight(1);
			} else {
				showToast(lighted == 1 ? getString(R.string.lighted)
						: getString(R.string.unlighted));
			}
			break;
		case R.id.uplight_num:
		case R.id.to_unlight:
			int unlighted = Integer.parseInt(v.getTag().toString());
			if (unlighted == 0) {
				editLight(2);
			} else {
				showToast(unlighted == 1 ? getString(R.string.lighted)
						: getString(R.string.unlighted));
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void treatClickEvent(int id) {

		super.treatClickEvent(id);
		HupuLog.d("click item =" + id);
		switch (id) {

		case R.id.btn_back:
			finish();
			break;
		case R.id.news_hot_reply:
			toReplyAct();
			break;
		case R.id.reply_num:
			toReplyAct();
			break;
		case R.id.to_jump_img:
			toReplyAct();
			break;
		case R.id.news_img:
			if (!isDownload) {
				UrlImageViewHelper.loadUrlDrawable(NewsDetailActivity.this,
						entity.mImg, new downLoadOk());
				progressbar.setVisibility(View.VISIBLE);
			} else {
				Intent in = new Intent(this, ShowImgActivity.class);
				in.putExtra("url", entity.mImg);
				startActivity(in);

			}
			break;
		case R.id.commit_reply:
			if (!commitContent.getText().toString().equals("")) {
				switchReplyLayout(false);
				tag = getIntent().getStringExtra("tag");
				initParameter();
				mParams.put("nid", "" + nid);
				mParams.put("content", commitContent.getText().toString());

				sendRequest(HuPuRes.REQ_METHOD_POST_REPLY, tag, mParams,
						new HupuHttpHandler(this), false);
			} else {
				showToast("请输入评论内容");
			}
			break;
		case R.id.to_reply_img:
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
		case R.id.commit_layout_bg:
			switchReplyLayout(false);
			break;
		case R.id.news_detail_layout:
			showToast("点击了webview");
			break;
		case R.id.btn_share:
			if (DoubleClickUtil.isFastDoubleClick()) {
				return;
			}
			openShare();
			break;
		}
	}

	private void openShare() {
		showShareView("虎扑看球", entity.shareUrl, entity.wechatShare,
				entity.qzoneShare, entity.weiboShare,
				entity.wechatMomentsShare, entity.shareImg, false);
	}

	private void toReplyAct() {
		if (entity != null) {
			tag = getIntent().getStringExtra("tag");

			Intent in = new Intent(this, ReplyListActivity.class);
			in.putExtra("tag", tag);
			in.putExtra("nid", nid);
			in.putExtra("title", entity.title);
			in.putExtra("origin", entity.origin);
			in.putExtra("time", TimeUtile.getReplytime(format.format(new Date(
					(Long.parseLong(entity.addtime) * 1000L)))));
			startActivity(in);

		}
	}

	private void switchReplyLayout(boolean isShowEdit) {
		if (isShowEdit) {
			commitLayout.setVisibility(View.VISIBLE);
			findViewById(R.id.commit_layout_bg).setVisibility(View.VISIBLE);
			commitImg.setVisibility(View.GONE);
			findViewById(R.id.to_jump_img).setVisibility(View.GONE);
			findViewById(R.id.reply_num).setVisibility(View.GONE);
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
			findViewById(R.id.to_jump_img).setVisibility(View.VISIBLE);
			findViewById(R.id.reply_num).setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onDestroy() {

		if (newsDetail != null) {
			newsDetail.setVisibility(View.GONE);
			newsDetail.stopLoading();
			newsDetail.destroy();
		}
		super.onDestroy();
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

	/**
	 * 中间加入监听 newsImg 是否绘制完成，如果完成就设置内容。如果没有就等待绘制完成后
	 */
	boolean isSet = false;

	class downLoadOk implements UrlImageViewCallback {

		@Override
		public void onLoaded(ImageView imageView, final Bitmap loadedBitmap,
				String url, boolean loadedFromCache) {
			// TODO Auto-generated method stub
			// 监听 view 是否绘制完成！
			ViewTreeObserver vto = newsImg.getViewTreeObserver();
			vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

				@Override
				public boolean onPreDraw() {
					// TODO Auto-generated method stub
					try {
						if (!isSet && newsImg.getWidth() != 0) {
							isSet = true;
							float scaleWidth = ((float) newsImg.getWidth())
									/ loadedBitmap.getWidth();
							Matrix matrix = new Matrix();
							matrix.postScale(scaleWidth, scaleWidth);

							Bitmap image = Bitmap.createBitmap(loadedBitmap, 0,
									0, loadedBitmap.getWidth(),
									loadedBitmap.getHeight(), matrix, true);
							newsImg.setBackgroundResource(R.drawable.bg_1x1);
							newsImg.setImageBitmap(image);
							setOnClickListener(R.id.news_img);
							// loadedBitmap.recycle();
							isDownload = true;
							progressbar.setVisibility(View.GONE);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					return true;
				}
			});
		}

	}

	@Override
	public void onPageFinished(WebView view, String url) {
		newsDetail.setVisibility(View.VISIBLE);
		if (entity != null && entity.lightList != null
				&& entity.lightList.size() > 0) {
			handler.postDelayed(runnable, 500);
		}
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url,
			boolean isScheme) {
		if (!isScheme) {
			Intent webIntent = new Intent(NewsDetailActivity.this,
					WebViewActivity.class);
			webIntent.putExtra("url", url);
			startActivity(webIntent);
		}
		return true;
	}

	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
	}

	private String findCnTag(String tag) {

		LinkedList<LeaguesEntity> leagueList = mApp.loadLeagues();
		for (LeaguesEntity en : leagueList) {
			if (en.en.equals(tag)) {
				return en.name;
			}
		}
		return "";
	}

	public void startAnim() {
		myAnimation_Translate = new TranslateAnimation(0, 0, 0, -100);
		myAnimation_Translate.setDuration(1200);
		myAnimation_Translate.setAnimationListener(animationListener);
		// isAnimation = true;
	}

	// 执行动画的view
	private View animView;
	private AnimationListener animationListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
			inAnimation = true;
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			animView.setVisibility(View.INVISIBLE);
			setLightInfo(entity);

			inAnimation = false;
		}
	};

	@Override
	public void onReceivedTitle(WebView view, String title) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}
}
