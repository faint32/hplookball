/**
 * 
 */
package com.hupu.games.activity;


import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.adapter.FootballPlayerinfoGridListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.data.game.football.FootballPlayerInfoReq;
import com.hupu.games.view.HupuGridView;
import com.hupu.games.view.HupuSingleLineTextView;
import com.hupu.http.HupuHttpHandler;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

/**
 * @author papa 球员详情页
 */
public class FootballPlayerInfoActivity extends HupuBaseActivity {


	ImageView playerHeader;
	TextView txtEnName,txtNumAndPositionAndTeam,txtAge,txtHeight,txtWeight,dataInfo,playerIntro;
	HupuSingleLineTextView txtZhName;
	int pid;
	String tag;
	View progress;
	ScrollView headScroll;
	GridView mGridListView;
	LinearLayout dataInfoLayout;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pid = getIntent().getIntExtra("pid", 0);
		tag = getIntent().getStringExtra("tag");
		setContentView(R.layout.layout_footba_player_info);
		progress = (ProgressBar) findViewById(R.id.probar);
		initView();   
		reqPlayerInfo();
		
		setOnClickListener(R.id.btn_back);
	}

	private void initView() {
		headScroll = (ScrollView) findViewById(R.id.head_scroll);
		playerHeader = (ImageView) findViewById(R.id.player_header);
		mGridListView = (GridView) findViewById(R.id.grid_data_info);
		txtZhName = (HupuSingleLineTextView) findViewById(R.id.player_name_zh);
		txtEnName = (TextView) findViewById(R.id.player_name_en);
		txtNumAndPositionAndTeam = (TextView) findViewById(R.id.player_num_team);
		txtAge = (TextView) findViewById(R.id.age_info);
		txtHeight = (TextView) findViewById(R.id.height_info);
		txtWeight = (TextView) findViewById(R.id.weight_info);
		dataInfo = (TextView) findViewById(R.id.data_info);
		playerIntro = (TextView) findViewById(R.id.player_intro);
		headScroll.setVisibility(View.GONE);
		dataInfoLayout = (LinearLayout) findViewById(R.id.data_info_layout);
		
	}

	private void setProfileData(FootballPlayerInfoReq entity) {
		headScroll.setVisibility(View.VISIBLE);
		
		UrlImageViewHelper.setUrlDrawable(playerHeader,
				entity.p_header, R.drawable.bg_no_player_pic);
		txtZhName.setText(entity.p_name);
		txtEnName.setText(entity.p_en_name);
		txtNumAndPositionAndTeam.setText(entity.p_number+"   "+entity.t_name);
		txtAge.setText(entity.p_age);
		txtHeight.setText(entity.p_height);
		txtWeight.setText(entity.p_weight);
		if (!"".equals(entity.p_intro)) {
			findViewById(R.id.layout_intro).setVisibility(View.VISIBLE);
		}
		dataInfo.setText(Html.fromHtml(entity.p_dataInfo));
		playerIntro.setText(entity.p_intro);
		progress.setVisibility(View.GONE);
		((TextView)findViewById(R.id.txt_title)).setText(entity.p_s_name);
		
		
		if (entity.infoList != null) {
			dataInfoLayout.removeAllViews();
			for (int i = 0; i < entity.infoList.size(); i++) {
				
				View dataView = getLayoutInflater().inflate(
						R.layout.item_header_player_info_data, null);
				TextView blockTxt = (TextView) dataView.findViewById(R.id.txt_block);
				blockTxt.setText(entity.infoList.get(i).block);
				HupuGridView mGridView = (HupuGridView) dataView.findViewById(R.id.grid_data_info);
				FootballPlayerinfoGridListAdapter mGridAdapter = new FootballPlayerinfoGridListAdapter(this);
				mGridAdapter.setData(entity.infoList.get(i).infoDateList);
				mGridView.setAdapter(mGridAdapter);
				dataInfoLayout.addView(dataView);
			}
		}else {
			dataInfoLayout.setVisibility(View.GONE);
		}
		
//		mGridListView.setAdapter(mGridAdapter);
//		if (infoReq.infoDateList == null || infoReq.infoDateList.size() == 0) {
//			mGridListView.setVisibility(View.GONE);
//		}
		
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


	FootballPlayerInfoReq infoReq;

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		if (o != null) {
			infoReq = (FootballPlayerInfoReq) o;
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
		showToast(getString(R.string.no_player_info));
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
		mParams.add("player_id", "" + pid);
		
		sendRequest(HuPuRes.REQ_METHOD_GET_FOOTBALL_PLAYERINFO, tag, mParams,
				new HupuHttpHandler(this), false);
	}

}
