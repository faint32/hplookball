package com.hupu.games.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.hupu.games.HuPuApp;
import com.hupu.games.R;
import com.hupu.games.activity.BaseGameActivity;
import com.hupu.games.activity.NBAGameActivity.BoxscoreDatas;
import com.hupu.games.adapter.GameDataListAdapter;
import com.hupu.games.adapter.GameDataListLandAdapter;
import com.hupu.games.data.BaseGameEntity;
import com.hupu.games.data.MatchEntity;
import com.hupu.games.data.PlayerEntity;
import com.hupu.games.data.game.basketball.CBABoxScoreResp;
import com.hupu.games.data.personal.box.BoxScoreResp;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

/**
 * 统计页面
 * */
@SuppressLint("ValidFragment")
public class CBAStatisticLandFragment extends BaseFragment {

	/** 横屏时统计列表的标题 **/
	private TextView[] txtHeaders;
	/** 横屏时统计列表 **/
	private ListView mListLandPlayer;
	/** 横屏时统计列表的适配器 **/
	private GameDataListLandAdapter mDataLandAdapter;
	/** 横屏时主队名字 **/
	TextView txtHome;
	/** 横屏时客队名 **/
	TextView txtAway;
	/** 横屏时主队logo **/
	ImageView imgHome;
	/** 横屏时客队logo **/
	ImageView imgAway;
	/** 横屏时主队分割线 **/
	View lineHome;
	/** 横屏时客队分割线 **/
	View lineAway;
	/** 横屏时主队按钮 **/
	View btnHome;
	/** 横屏时客队按钮 **/
	View btnAway;

	/** 横屏时列表标题栏球队名 **/
	private TextView txtTeamName;
	/** 颜色值 **/
	private int clrOn;

	private int clrOff;

	BaseGameActivity mAct;
	LinearLayout headView;
	
	
	public CBAStatisticLandFragment() {
		super();

	}

	String homeName;
	String awayName;
	int homeId;
	int awayId;

	String homeLogo;
	String awayLogo;
	public CBAStatisticLandFragment(BaseGameEntity entity) {
		super();
		homeName = entity.str_home_name;
		awayName = entity.str_away_name;
		homeId = entity.i_home_tid;
		awayId = entity.i_away_tid;
		homeLogo =entity.home_logo;
		awayLogo =entity.away_logo;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mAct = (BaseGameActivity) getActivity();
		clrOn = getResources().getColor(R.color.dark_gray);
		clrOff = getResources().getColor(R.color.res_cor6);

		View v = inflater.inflate(R.layout.layout_data_landscape, container,
				false);

		txtHome = (TextView) v.findViewById(R.id.txt_home_land);
		txtAway = (TextView) v.findViewById(R.id.txt_away_land);
		imgHome = (ImageView) v.findViewById(R.id.img_home_land);
		imgAway = (ImageView) v.findViewById(R.id.img_away_land);
		btnHome = v.findViewById(R.id.btn_home_land);
		btnAway = v.findViewById(R.id.btn_away_land);

		lineHome = v.findViewById(R.id.line_home);
		lineAway = v.findViewById(R.id.line_away);


	    UrlImageViewHelper.setUrlDrawable(imgHome,homeLogo,R.drawable.bg_home_nologo);
	    UrlImageViewHelper.setUrlDrawable(imgAway,awayLogo,R.drawable.bg_home_nologo);
	    
//		lineHome.setBackgroundColor(HuPuApp.getTeamData(homeId).i_color);
//		lineAway.setBackgroundColor(HuPuApp.getTeamData(awayId).i_color);
//		imgHome.setBackgroundResource(HuPuApp.getTeamData(homeId).i_logo);
//		imgAway.setBackgroundResource(HuPuApp.getTeamData(awayId).i_logo);

		txtTeamName = (TextView) v.findViewById(R.id.txt_name_land);
		headView = (LinearLayout) v.findViewById(R.id.layout_header_land);
		headView.setVisibility(View.VISIBLE);
		txtHeaders = null;
		mListLandPlayer = (ListView) v.findViewById(R.id.list_players_land);
		mDataLandAdapter = new GameDataListLandAdapter(getActivity(),true);
		mListLandPlayer.setAdapter(mDataLandAdapter);

		Click click = new Click();
		btnHome.setOnClickListener(click);
		btnAway.setOnClickListener(click);
		txtHome.setText(homeName);
		txtAway.setText(awayName);
		txtTeamName.setText(homeName);

		if (mBoxscoreData != null) {
			mDataLandAdapter.setData(mBoxscoreData);
		}
		initLand();
		return v;
	}

	/** 统计数据 **/
	public BoxscoreDatas mBoxscoreData;

	/** 初始化水平状态的一些view **/
	private void initLand() {

		if (txtHeaders == null && mBoxscoreData != null) {

			txtHeaders = new TextView[mBoxscoreData.mListKeys.size()];
			LinearLayout.LayoutParams llp = null;
			LayoutInflater in = LayoutInflater.from(getActivity());
			String key = null;
			for (int i = 0; i < txtHeaders.length; i++) {

				txtHeaders[i] = (TextView)in.inflate(R.layout.static_header_land, null);
				txtHeaders[i].setText(mBoxscoreData.mTitles.get(i));
				key = mBoxscoreData.mListKeys.get(i);
				// 计算间距
				if (key.equals("2p") || key.equals("3p") || key.equals("ft"))
					llp = new LinearLayout.LayoutParams(0, -1, 10);
				else if (key.equals("mins") || key.equals("pts"))
					llp = new LinearLayout.LayoutParams(0, -1, 7);
				else
					llp = new LinearLayout.LayoutParams(0, -1, 5);
				headView.addView(txtHeaders[i] , llp);
			}

		}
	}

	class Click implements OnClickListener {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.btn_home_land:
				if (mDataLandAdapter != null) {
					mDataLandAdapter.changeMode(0);
					if (mDataLandAdapter.getCount() > 0)
						mListLandPlayer.setSelection(0);
				}
				mAct.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
				btnHome.setBackgroundColor(clrOn);
				btnAway.setBackgroundColor(clrOff);
				txtTeamName.setText(homeName);
				break;
			case R.id.btn_away_land:
				if (mDataLandAdapter != null) {
					mDataLandAdapter.changeMode(1);
					if (mDataLandAdapter.getCount() > 0)
						mListLandPlayer.setSelection(0);
				}

				mAct.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
				btnHome.setBackgroundColor(clrOff);
				btnAway.setBackgroundColor(clrOn);
				txtTeamName.setText(awayName);
				break;
			}

		}
	}

	public void setData(CBABoxScoreResp d) {
		if (mBoxscoreData == null)
			mBoxscoreData = new BoxscoreDatas();
		setTitleMap(d.mMapGlossary);

		mBoxscoreData.mListPLay = d.mListPlayers;

		mBoxscoreData.mListPLayerNames = new ArrayList<String>();
		for (PlayerEntity entity : mBoxscoreData.mListPLay)
			mBoxscoreData.mListPLayerNames.add(entity.str_player_id);

		mBoxscoreData.i_homeSize = d.i_homePlaySize;
		paserTotal(d);
		initLand() ;
		// 主队命中率
		mBoxscoreData.str_home_fg = d.str_home_fg;
		mBoxscoreData.str_home_tp = d.str_home_tp;
		mBoxscoreData.str_home_ft = d.str_home_ft;
		// 客队命中率
		mBoxscoreData.str_away_fg = d.str_away_fg;
		mBoxscoreData.str_away_tp = d.str_away_tp;
		mBoxscoreData.str_away_ft = d.str_away_ft;
		
		mDataLandAdapter.setData(mBoxscoreData);
	}

	public void paserTotal(BoxScoreResp data) {

		if (mBoxscoreData.mMapHomeTotal == null) {
			mBoxscoreData.mMapHomeTotal = new LinkedHashMap<String, String>();
		}
		if (mBoxscoreData.mMapAwayTotal == null) {
			mBoxscoreData.mMapAwayTotal = new LinkedHashMap<String, String>();
		}
		if (data.homeTotals != null) {
			paserTotal(data.homeTotals, mBoxscoreData.mMapHomeTotal);
		}
		if (data.awayTotals != null) {
			paserTotal(data.awayTotals, mBoxscoreData.mMapAwayTotal);
		}
	}

	private void paserTotal(JSONObject json, LinkedHashMap<String, String> list) {
		int size = 0;
		size = mBoxscoreData.mListKeys.size();
		String key = null;
		String value = null;
		for (int i = 0; i < size; i++) {
			key = mBoxscoreData.mListKeys.get(i);

			value = json.optString(key, null);
			if (value != null)
				list.put(key, value);
		}
	}

	/** 为了获取由服务器传递过来的标题字段 */
	private void setTitleMap(LinkedHashMap<String, String> m) {
		if (m == null)
			return;
		Iterator<Entry<String, String>> lit = m.entrySet().iterator();
		mBoxscoreData.mListKeys = new ArrayList<String>();
		mBoxscoreData.mTitles = new ArrayList<String>();

		while (lit.hasNext()) {
			Map.Entry<String, String> e = lit.next();
			// System.out.println("key="+e.getKey());
			mBoxscoreData.mListKeys.add(e.getKey());
			mBoxscoreData.mTitles.add(e.getValue());
		}


	}
}
