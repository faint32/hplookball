/**
 * 
 */
package com.hupu.games.activity;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.adapter.LiveDataListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.HupuSchemeProccess;
import com.hupu.games.data.LiveEntity;
import com.hupu.games.data.game.base.SimpleLiveResp;
import com.hupu.games.data.game.base.SimpleScoreboard;
import com.hupu.games.data.game.football.ScoreboardEntity;
import com.hupu.games.view.XListView;
import com.hupu.http.HupuHttpHandler;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

/**
 * @author panyongjun 足球的文字直播被单独剥离出来了
 */
public class SoccerLiveActivity extends HupuBaseActivity {

	String tag;
	int lid;
	int gid;
	/** 比赛未开始时显示的开赛时间 */
	protected TextView txtStartTime;
	/** 比赛开始后显示的时间 */
	protected TextView txtProcess;
	/** 主队队名 */
	protected TextView txtTeamLeft;
	/** 客队队队名 */
	protected TextView txtTeamRight;

	protected TextView txtScore;

	protected ImageView imgTeamLeft;

	protected ImageView imgTeamRight;

	private ScoreboardEntity mGameEntity;

	private XListView mLvLive;

	private LiveDataListAdapter mListAdapter;

	private View mProgressBar;

	TextView txtShootout1;
	TextView txtShootout2;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Intent in = getIntent();
		tag = in.getStringExtra("tag");
		if (tag == null)
			finish();
		setContentView(R.layout.layout_football_live);

		gid = in.getIntExtra("gid", 0);
		setOnClickListener(R.id.btn_back);
		getGameEntityById();

	}

	private void getGameEntityById()
	{
		
		initParameter();
		// 获取比赛信息
		lid = findLid(tag);
		mParams.put("lid", "" + lid);
		mParams.put("gid", "" + gid);
		sendRequest(HuPuRes.REQ_METHOD_FOOTBALL_GAME_BY_GID, tag, mParams,
				new HupuHttpHandler(this), false);
	}
	private void init() {

		setOnClickListener(R.id.img_team_left);
		setOnClickListener(R.id.img_team_right);

		txtTeamLeft = (TextView) findViewById(R.id.txt_team_left);
		txtTeamRight = (TextView) findViewById(R.id.txt_team_right);
		txtProcess = (TextView) findViewById(R.id.txt_proccess);

		txtScore = (TextView) findViewById(R.id.txt_score);
		txtShootout1 = (TextView) findViewById(R.id.txt_shootout1);
		txtShootout2 = (TextView) findViewById(R.id.txt_shootout2);

		setSore(0, 0);
		txtStartTime = (TextView) findViewById(R.id.txt_start_time);

		imgTeamLeft = (ImageView) findViewById(R.id.img_team_left);

		imgTeamRight = (ImageView) findViewById(R.id.img_team_right);

		mProgressBar = findViewById(R.id.probar);

		mListAdapter = new LiveDataListAdapter(this,0,0, click);
		mLvLive = (XListView) findViewById(R.id.list_live);
		mLvLive.setPullLoadEnable(false, false);
		mLvLive.setAdapter(mListAdapter);
		setTeamName();

		updateScoreView();

		mParams.put("sort", "asc");
		sendRequest(HuPuRes.REQ_METHOD_GET_LIVE_ASC, tag, mParams, new HupuHttpHandler(this), false);

	}

	private void updateScoreView() {

		setSore(mGameEntity.i_home_score, mGameEntity.i_away_score);
		txtProcess.setText(mGameEntity.str_desc);

		if (mGameEntity.home_out_goals > 0 || mGameEntity.away_out_goals > 0) {
			// 结束后没有period字段，只能通过比分来判断。
			showShootOut(txtShootout1, txtShootout2,
					mGameEntity.home_out_goals, mGameEntity.away_out_goals);
			txtProcess.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.ic_penalty, 0);
		} else {
			if (mGameEntity.is_extra > 0) {
				txtProcess.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						R.drawable.ic_overtime, 0);
			}
			hideShootOut(txtShootout1, txtShootout2);
		}

	}


	/** 设置球队名称 */
	private void setTeamName() {
		// 球队名称
		txtTeamLeft.setText(mGameEntity.str_home_name);
		txtTeamRight.setText(mGameEntity.str_away_name);
		UrlImageViewHelper.setUrlDrawable(imgTeamLeft, mGameEntity.home_logo,
				R.drawable.bg_home_nologo);
		UrlImageViewHelper.setUrlDrawable(imgTeamRight, mGameEntity.away_logo,
				R.drawable.bg_home_nologo);

	}

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		if (o == null)
			return;
		if (HuPuRes.REQ_METHOD_GET_LIVE_ASC == methodId) {
			SimpleLiveResp live = (SimpleLiveResp) o;
			updateProccess(live.scoreBoard);
			setData(live.dataList);
		} else if (HuPuRes.REQ_METHOD_FOOTBALL_GAME_BY_GID == methodId) {
			mGameEntity = (ScoreboardEntity) o;
			init();
		}
	}

	public void setData(ArrayList<LiveEntity> gameList) {
		if (mProgressBar != null)
			mProgressBar.setVisibility(View.GONE);
		if (mListAdapter != null) {
			mListAdapter.setData(gameList);
		}
	}
	
	/** 设置球队比分 需要翻牌，这个逻辑就需要先判断比分的变换了。 */
	private void setSore(int homeScore, int awayScore) {
		if (homeScore > -1 && awayScore > -1)
			txtScore.setText(homeScore + " - " + awayScore);
	}
	/** 更新比赛过程的标题信息 */
	private void updateProccess(SimpleScoreboard scoreBoard) {

		setSore(scoreBoard.i_scoreHome,
				scoreBoard.i_scoreAway);

		txtProcess.setText(scoreBoard.desc);
	}


	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);

		switch (id) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.img_team_right:
			if (mGameEntity.i_away_tid < 990000)
				toSoccerTeamAct(mGameEntity.i_away_tid);
			break;
		case R.id.img_team_left:
			if (mGameEntity.i_home_tid < 990000)
				toSoccerTeamAct(mGameEntity.i_home_tid);
			break;
		}
	}

	@Override
	public void treatClickEvent(View v) {
		super.treatClickEvent(v);
		if (v instanceof TextView) {
			
			String url = (String) v.getTag();
			HupuLog.d("live url="+url);
			if (url != null) {
				Uri uri = Uri.parse(url);
				if (uri.getScheme().equalsIgnoreCase("kanqiu")) {
					HupuSchemeProccess.proccessScheme(this, uri);
				}else
				{
					Intent in = new Intent(this,
							WebViewActivity.class);
					in.putExtra("url", url);
					startActivity(in);
				}
				
			}

		} else if (v instanceof ImageView) {
			String url = (String) v.getTag();
			showImgDialog(url);
		}
	}

	private void toSoccerTeamAct(int tid) {
		Intent in = new Intent(this, SoccerTeamActivity.class);
		in.putExtra("tid", tid);
		in.putExtra("tag", tag);
		startActivity(in);
	}
	Dialog mImgDialog;
	WebView webview;
	Handler mImgHandler;
	View progress;

	/** 放大图片 */
	public void showImgDialog(final String url) {
		if (url == null)
			return;
		if (mImgHandler == null)
			mImgHandler = new Handler();

		if (mImgDialog != null && mImgDialog.isShowing()) {
			mImgDialog.dismiss();
		}
		View v = LayoutInflater.from(this).inflate(
				R.layout.dialog_img, null);
		progress = v.findViewById(R.id.probar);

		v.findViewById(R.id.mask).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mImgDialog.cancel();
			}
		});
		// gifView = (GifView) v.findViewById(R.id.btn_close);
		// gifView.setGifImageType(GifImageType.WAIT_FINISH);

		webview = (WebView) v.findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webview.setBackgroundColor(Color.argb(0, 0, 0, 0));
		webview.setVisibility(View.INVISIBLE);
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if (newProgress >= 100) {
					progress.setVisibility(View.GONE);
					webview.setVisibility(View.VISIBLE);
				}
			}

		});
		mImgHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				webview.loadDataWithBaseURL(null, String.format(IMG_URL, url),
						"text/html", "utf-8", null);
			}
		}, 300);

		mImgDialog = new Dialog(this, R.style.MyWebDialog);
		mImgDialog.setContentView(v);
		// mImgDialog.setCanceledOnTouchOutside(true);
		// mImgDialog.setCancelable(true);
		mImgDialog.getWindow().setGravity(Gravity.CENTER);
		mImgDialog.show();
		mImgDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

	}
	

	public final static String IMG_URL = "<div style=\"display:table\" id=\"JPicWrap\">"
			+ "<div style=\"display:table-cell;text-align:center;vertical-align:middle;horizontal-align:middle\">"
			+ "<img src=\"%s\" alt=\"\">"
			+ "</div>"
			+ "</div>"
			+ "<script type=\"text/javascript\">"
			+ "window.onload = function(){"
			+ "clientH = document.documentElement.clientHeight||document.body.clientHeight;"
			+ "document.getElementById('JPicWrap').style.height = clientH+'px';"
			+ "clientW = document.documentElement.clientWidth||document.body.clientWidth;"
			+ "document.getElementById('JPicWrap').style.width = clientW+'px';"
			+ "}" + "</script>";
}
