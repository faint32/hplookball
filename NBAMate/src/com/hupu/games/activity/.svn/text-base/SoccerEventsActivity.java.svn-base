/**
 * 
 */
package com.hupu.games.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.adapter.FootballEventsListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.data.game.football.ScoreboardEntity;
import com.hupu.games.data.game.football.SoccerEventsResp;
import com.hupu.http.HupuHttpHandler;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

/**
 * @author panyongjun 足球的事件直播被单独剥离出来了
 */
public class SoccerEventsActivity extends HupuBaseActivity {

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

	private ListView mLvLive;

	private FootballEventsListAdapter mListAdapter;

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
		setContentView(R.layout.layout_football_events);
		setOnClickListener(R.id.btn_back);
		gid = in.getIntExtra("gid", 0);
		mGameEntity = (ScoreboardEntity) in.getSerializableExtra("data");
		// mGameEntity=null;
		initParameter();
		if (mGameEntity == null) {
			// 获取比赛信息
			lid = findLid(tag);
			mParams.put("lid", "" + lid);
			mParams.put("gid", "" + gid);
			sendRequest(HuPuRes.REQ_METHOD_FOOTBALL_GAME_BY_GID, tag, mParams,
					new HupuHttpHandler(this), false);
		} else {
			lid = in.getIntExtra("lid", -1);
			init();
		}
	}

	private void init() {
		mParams.put("lid", "" + lid);
		mParams.put("gid", "" + gid);
		if ("worldcup".equals(tag)) {
			// 世界杯需要显示阵容按键
			setOnClickListener(R.id.img_team_left);
			setOnClickListener(R.id.img_team_right);
		}
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

		mLvLive = (ListView) findViewById(R.id.list_live);

		mListAdapter = new FootballEventsListAdapter(this,new Click());

		mLvLive.setAdapter(mListAdapter);
		setTeamName();

		updateScoreView();

		sendRequest(HuPuRes.REQ_METHOD_GET_FOOTBALL_EVENTS, tag, mParams,
				new HupuHttpHandler(this), false);

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

	/** 设置球队比分 需要翻牌，这个逻辑就需要先判断比分的变换了。 */
	private void setSore(int homeScore, int awayScore) {
		txtScore.setText(homeScore + " - " + awayScore);
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
		if (HuPuRes.REQ_METHOD_GET_FOOTBALL_EVENTS == methodId) {
			SoccerEventsResp live = (SoccerEventsResp) o;
			updateProccess(live.scoreBoard);
			setData(live);
		} else if (HuPuRes.REQ_METHOD_FOOTBALL_GAME_BY_GID == methodId) {
			mGameEntity = (ScoreboardEntity) o;
			init();
		}
	}

	/** 更新比赛过程的标题信息 */
	private void updateProccess(ScoreboardEntity entity) {

		if (entity != null) {
			mGameEntity = entity;
			setSore(mGameEntity.i_home_score, mGameEntity.i_away_score);
		}
		showTime(entity, txtProcess);
	}

	private void showTime(ScoreboardEntity en, TextView tv) {
		mGameEntity = en;
		showTime(en, tv, 0);

	}

	public void setData(SoccerEventsResp data) {

		if (mProgressBar != null)
			mProgressBar.setVisibility(View.GONE);
		if (mListAdapter != null) {
			mListAdapter.setData(data.mDatas);
		}

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

	private void toSoccerTeamAct(int tid) {
		Intent in = new Intent(this, SoccerTeamActivity.class);
		in.putExtra("tid", tid);
		in.putExtra("tag", tag);
		startActivity(in);
	}
	
	
	public class Click implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int pid = 0;
			if ("".equals(v.getTag().toString()) && v.getTag() !=null) {
				pid = 0;
			}else {
				pid = Integer.parseInt(v.getTag().toString());
			}
			
			if (pid != 0) {
				Intent in = new Intent(SoccerEventsActivity.this, FootballPlayerInfoActivity.class);
				in.putExtra("pid", pid);
				in.putExtra("tag", tag);
				startActivity(in);
			}
			
		}
		
	}
}
