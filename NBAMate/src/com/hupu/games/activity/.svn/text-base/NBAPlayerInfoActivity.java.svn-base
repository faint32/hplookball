/**
 * 
 */
package com.hupu.games.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.adapter.PlayerInfoListAdapter;
import com.hupu.games.adapter.PlayerinfoGridListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.game.basketball.NbaPlayerInfoReq;
import com.hupu.games.view.HScrollView;
import com.hupu.games.view.HupuPinnedHeaderListView;
import com.hupu.http.HupuHttpHandler;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

/**
 * @author papa 球员详情页
 */
public class NBAPlayerInfoActivity extends HupuBaseActivity {

	/** 对该球员的打分及评论 */
	HupuPinnedHeaderListView mListView;

	//PlayerInfoListAdapter mAdapter;
	PlayerInfoListAdapter mAdapter;
	ImageView playerHeader;
	TextView txtZhName,txtEnName,txtNumAndPositionAndTeam,txtAge,txtHeight,txtWeight,txtBirth,txtDraft,txtCollege,txtSalary,txtContract;
	ScrollView headScroll;
	int pid;
	View progress;
	
	GridView mGridListView;

	PlayerinfoGridListAdapter mGridAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pid = getIntent().getIntExtra("pid", 0);
		setContentView(R.layout.layout_player_info);
		progress = (ProgressBar) findViewById(R.id.probar);
		
		mListView = (HupuPinnedHeaderListView) findViewById(R.id.list_player_info);
		mListView.setOnItemClickListener(new PlayerListClick());
		header = LayoutInflater.from(this).inflate(
				R.layout.item_player_info_header, null);
		initHeadView();   
		mListView.addHeaderView(header);
		PlayerListViewTouchLinstener linstener = new PlayerListViewTouchLinstener();
		mListView.setOnTouchListener(linstener);
		mAdapter = new PlayerInfoListAdapter(this,linstener);
		mListView.setAdapter(mAdapter);
		reqPlayerInfo();
		
		setOnClickListener(R.id.btn_back);
	}

	View header;

	float fontSize;
	float bigFontSize;

	private void initHeadView() {
		headScroll = (ScrollView) header.findViewById(R.id.head_scroll);
		playerHeader = (ImageView) header.findViewById(R.id.player_header);
		mGridListView = (GridView) header.findViewById(R.id.grid_data_info);
		txtZhName = (TextView) header.findViewById(R.id.player_name_zh);
		txtEnName = (TextView) header.findViewById(R.id.player_name_en);
		txtNumAndPositionAndTeam = (TextView) header.findViewById(R.id.player_num_team);
		txtAge = (TextView) header.findViewById(R.id.age_info);
		txtHeight = (TextView) header.findViewById(R.id.height_info);
		txtWeight = (TextView) header.findViewById(R.id.weight_info);
		txtBirth = (TextView) header.findViewById(R.id.birth_date_info);
		txtDraft = (TextView) header.findViewById(R.id.draft_info);
		txtCollege = (TextView) header.findViewById(R.id.college_info);
		txtSalary = (TextView) header.findViewById(R.id.salary_info);
		txtContract = (TextView) header.findViewById(R.id.contract_info);
		headScroll.setVisibility(View.GONE);
		
	}

	private void setProfileData(NbaPlayerInfoReq entity) {
		headScroll.setVisibility(View.VISIBLE);
		UrlImageViewHelper.setUrlDrawable(playerHeader,
				entity.p_header, R.drawable.bg_no_player_pic);
		txtZhName.setText(entity.p_name);
		txtEnName.setText(entity.p_en_name);
		txtNumAndPositionAndTeam.setText(entity.p_number+"   "+entity.p_position+"   "+entity.t_name);
		txtAge.setText(entity.p_age);
		txtHeight.setText(entity.p_height);
		txtWeight.setText(entity.p_weight);
		txtBirth.setText(entity.p_birth_date);
		txtDraft.setText(entity.p_draft);
		txtCollege.setText(entity.p_college);
		txtSalary.setText(entity.p_salary);
		txtContract.setText(entity.p_contract);
		progress.setVisibility(View.GONE);
		mAdapter.setData(infoReq);
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
		((TextView)findViewById(R.id.txt_title)).setText(entity.p_s_name);
		
		mGridAdapter = new PlayerinfoGridListAdapter(this);
		mGridAdapter.setData(infoReq.infoDateList);
		mGridListView.setAdapter(mGridAdapter);
		if (infoReq.infoDateList == null || infoReq.infoDateList.size() == 0) {
			mGridListView.setVisibility(View.GONE);
		}
		
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

	public static int REQ_MY_RATING = 531;

	NbaPlayerInfoReq infoReq;

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		if (o != null) {
			infoReq = (NbaPlayerInfoReq) o;
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
		super.onErrResponse(error, type);
		showToast(getString(R.string.no_player_info));
		progress.setVisibility(View.GONE);
		headScroll.setVisibility(View.GONE);
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
		sendRequest(HuPuRes.REQ_METHOD_GET_NBA_PLAYER_INFO, mParams,
				new HupuHttpHandler(this));
	}
	
	class PlayerListClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			int tid = mAdapter.getItemAt(pos-1).t_id;
			
			if (tid > 0 && tid<31) {
				sendUmeng(HuPuRes.UMENG_EVENT_NBAPLAYERS, HuPuRes.UMENG_EVENT_PLAYER_DETAIL,HuPuRes.UMENG_VALUE_TAP_ONE_TEAM);
				Intent in =new Intent(NBAPlayerInfoActivity.this,NBATeamActivity.class);
				in.putExtra("tid", tid);
				startActivity(in);
			}

		}

	}
	
	public class PlayerListViewTouchLinstener implements View.OnTouchListener {

		float historicX = Float.NaN;
		static final int DELTA_ON_CLICK = 20;
	
		float x0;
		float y0;
		
		HScrollView scroll;
		HScrollView currentScroll;
		public void setScrollView(HScrollView scrollView){
			scroll = scrollView;
		}
		@Override
		public boolean onTouch(View arg0, MotionEvent event) {
			//当在listView控件上touch时，将这个touch的事件分发给 ScrollView
			if(scroll != null)
				scroll.onTouchEvent(event);
			
			if (mAdapter.ob1.getListSize() >=30) {
				mAdapter.ob1.clear();
				mAdapter.notifyDataSetChanged();
			}
			
			//HupuLog.e("papa", "size---------"+mAdapter.ob1.getListSize());
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				x0=event.getX();
				y0=event.getY();
				break;
			case MotionEvent.ACTION_UP:
				float cx = event.getX();
				float cy = event.getY();	
				if(Math.abs(cx - x0)>DELTA_ON_CLICK || Math.abs(cy - y0)>DELTA_ON_CLICK )
					return true;
				break;
			
			}
			return false;
		}
	}


}
