/**
 * 
 */
package com.hupu.games.activity;


import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.data.game.football.FootballCoachInfoReq;
import com.hupu.games.view.HupuSingleLineTextView;
import com.hupu.http.HupuHttpHandler;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

/**
 * @author papa 教练详情页
 */
public class FootballCoachInfoActivity extends HupuBaseActivity {


	ImageView coachHeader;
	TextView txtEnName,txtNumAndPositionAndTeam,dataInfo,coachIntro;
	HupuSingleLineTextView txtZhName;
	int pid;
	String tag;
	View progress;
	ScrollView headScroll;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pid = getIntent().getIntExtra("pid", 0);
		tag = getIntent().getStringExtra("tag");
		setContentView(R.layout.layout_footba_coach_info);
		progress = (ProgressBar) findViewById(R.id.probar);
		initView();   
		reqPlayerInfo();
		
		setOnClickListener(R.id.btn_back);
	}

	private void initView() {
		headScroll = (ScrollView) findViewById(R.id.head_scroll);
		coachHeader = (ImageView) findViewById(R.id.coach_header);
		txtZhName = (HupuSingleLineTextView) findViewById(R.id.coach_name_zh);
		txtEnName = (TextView) findViewById(R.id.coach_name_en);
		txtNumAndPositionAndTeam = (TextView) findViewById(R.id.coach_num_team);
		dataInfo = (TextView) findViewById(R.id.coach_detail);
		coachIntro = (TextView) findViewById(R.id.coach_intro);
		headScroll.setVisibility(View.GONE);
	}

	private void setProfileData(FootballCoachInfoReq entity) {
		headScroll.setVisibility(View.VISIBLE);
		UrlImageViewHelper.setUrlDrawable(coachHeader,
				entity.c_header, R.drawable.bg_no_player_pic);
		txtZhName.setText(entity.c_name);
		txtEnName.setText(entity.c_en_name);
		txtNumAndPositionAndTeam.setText(entity.c_role+"   "+entity.t_name);
		dataInfo.setText(Html.fromHtml(entity.detail));
		if (!"".equals(entity.intro)) {
			findViewById(R.id.layout_intro).setVisibility(View.VISIBLE);
		}
		coachIntro.setText(entity.intro);
		progress.setVisibility(View.GONE);
		((TextView)findViewById(R.id.txt_title)).setText(entity.c_s_name);
		
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}


	FootballCoachInfoReq infoReq;

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		if (o != null) {
			infoReq = (FootballCoachInfoReq) o;
			setProfileData(infoReq);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
				back();
				return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onErrResponse(Throwable error,int type) {
		//super.onErrResponse(error, type);
		showToast(getString(R.string.no_coach_info));
		progress.setVisibility(View.GONE);
		back();
	}


	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);
		switch (id) {
		case R.id.btn_back:
			back();
			break;
		}
	}


	private void back() {
		finish();
	}

	/**
	 * 获取用户
	 * */
	private void reqPlayerInfo() {
		initParameter();
		mParams.add("coach_id", "" + pid);
		sendRequest(HuPuRes.REQ_METHOD_GET_FOOTBALL_COACHINFO, tag,mParams,
				new HupuHttpHandler(this),false);
	}

}
