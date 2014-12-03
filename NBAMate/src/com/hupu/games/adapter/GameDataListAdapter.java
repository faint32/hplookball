package com.hupu.games.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupu.games.HuPuApp;
import com.hupu.games.R;
import com.hupu.games.activity.NBAGameActivity.BoxscoreDatas;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.PlayerEntity;
import com.hupu.games.data.personal.box.BoxScoreResp;
import com.hupu.games.fragment.BaseBasketballFragment;
import com.hupu.games.view.HScrollView;
import com.hupu.games.view.HScrollView.OnScrollChangedListener;

/**
 * 比赛统计数据竖屏
 * */
public class GameDataListAdapter extends XSectionedBaseAdapter {

	private ArrayList<PlayerEntity> mListPLay;
	
	private ArrayList<String> mListPLayerNames;
	
	private LayoutInflater mInflater;

	// LinkedHashMap<String, String> mapTitle;
	/**横屏模式的KEY*/
	private ArrayList<String> mListLandKeys;
	/**横屏模式的Title*/
	private ArrayList<String> mListLandTitles;
	/**竖屏模式的KEY*/
	private ArrayList<String> mListPortraitKeys;
	/**竖屏模式的标题*/
	private ArrayList<String> mListPortraitTitles;

	/** 主队球员数量 */
	private int i_homeSize;
	private int i_awaySize;
	/** 标题数量*/
	private int i_titleSize;

	private LinkedHashMap<String, String> mMapHomeTotal;

	private LinkedHashMap<String, String> mMapAwayTotal;
	// 主队命中率
	private String str_home_fg;
	private String str_home_tp;
	private String str_home_ft;
	// 客队命中率
	private String str_away_fg;
	private String str_away_tp;
	private String str_away_ft;

	private int fgIndex = -1;
	private int tpIndex = -1;
	private int ftIndex = -1;

	private int clrTxt;
	/**是否是CBA列表*/
	private boolean bCBA;
	
	private String homeName;
	private String awayName;

	int txtWidth;
	int txtHeight;

	private int iColHome;
	private int iColAway;
	
	BaseBasketballFragment sob;

	HScrollView mScroll;

	OnClickListener mClick;
	public GameDataListAdapter(Context context, int hId, int aId,
			BaseBasketballFragment ob,OnClickListener click) {
		mInflater = LayoutInflater.from(context);

		clrTxt = context.getResources().getColor(R.color.txt_status);

		txtWidth = context.getResources().getDimensionPixelSize(
				R.dimen.txt_player);
		txtHeight = context.getResources().getDimensionPixelSize(
				R.dimen.txt_statistic_height);
		homeName = HuPuApp.getTeamData(hId).str_name + "球员";
		awayName = HuPuApp.getTeamData(aId).str_name + "球员";

		iColHome = HuPuApp.getTeamData(hId).i_color;
		iColAway = HuPuApp.getTeamData(aId).i_color;
		sob = ob;
		mClick =click;
	}

	public GameDataListAdapter(Context context, String home, String away,
			BaseBasketballFragment ob) {
		mInflater = LayoutInflater.from(context);
		bCBA = true;
		homeName = home + "";
		awayName = away + "";

		clrTxt = context.getResources().getColor(R.color.txt_status);
		txtWidth = context.getResources().getDimensionPixelSize(
				R.dimen.txt_player);
		txtHeight = context.getResources().getDimensionPixelSize(
				R.dimen.txt_statistic_height);
		sob = ob;
	}

	public void setData(BoxScoreResp data) {
		setTitleMap(data.mMapGlossary, data.mMapPortrait);
		if (data.mListPlayers != null) {
			i_homeSize = data.i_homePlaySize;
			mListPLay = data.mListPlayers;
			mListPLayerNames = new ArrayList<String>();
			for (PlayerEntity entity : mListPLay)
				mListPLayerNames.add(entity.str_player_id);
			i_awaySize = mListPLay.size() - i_homeSize;
			paserTotal(data, false);
			// 主队命中率
			str_home_fg = data.str_home_fg;
			str_home_tp = data.str_home_tp;
			str_home_ft = data.str_home_ft;
			// 客队命中率
			str_away_fg = data.str_away_fg;
			str_away_tp = data.str_away_tp;
			str_away_ft = data.str_away_ft;
			notifyDataSetChanged();
		}
	}

	public void paserTotal(BoxScoreResp data, boolean update) {

		if (mMapHomeTotal == null) {
			mMapHomeTotal = new LinkedHashMap<String, String>();
		}
		if (mMapAwayTotal == null) {
			mMapAwayTotal = new LinkedHashMap<String, String>();
		}
		if (data.homeTotals != null) {
			paserTotal(data.homeTotals, mMapHomeTotal, update);
		}
		if (data.awayTotals != null) {
			paserTotal(data.awayTotals, mMapAwayTotal, update);
		}
	}

	private void paserTotal(JSONObject json,
			LinkedHashMap<String, String> list, boolean update) {
		int size = mListPortraitKeys.size();
		String key = null;
		String value = null;
		for (int i = 0; i < size; i++) {
			key = mListPortraitKeys.get(i);
			value = json.optString(key, null);
			if (value != null)
				list.put(key, value);
		}
	}

	public ArrayList<String> getKeys() {
		return mListLandKeys;
	}

	public void updateData(BoxScoreResp data) {
		PlayerEntity oldPlayer;
		for (PlayerEntity newPlayer : data.mListPlayers) {
			int index = mListPLayerNames.indexOf(newPlayer.str_player_id);
			if (index > -1) {
				oldPlayer = mListPLay.get(index);
				Set<String> set = newPlayer.mapDatas.keySet();
				for (String key : set) {
					oldPlayer.mapDatas.put(key, newPlayer.mapDatas.get(key));
				}
				if (newPlayer.on_court > -1)// 如果球员上场情况变化，需要更新此数据。
					oldPlayer.on_court = newPlayer.on_court;
			}

		}
		paserTotal(data, true);

		// 主队命中率\
		if (data.str_home_fg != null)
			str_home_fg = data.str_home_fg;
		if (data.str_home_tp != null)
			str_home_tp = data.str_home_tp;
		if (data.str_home_ft != null)
			str_home_ft = data.str_home_ft;
		// 客队命中率
		if (data.str_away_fg != null)
			str_away_fg = data.str_away_fg;
		if (data.str_away_tp != null)
			str_away_tp = data.str_away_tp;
		if (data.str_away_ft != null)
			str_away_ft = data.str_away_ft;
		notifyDataSetChanged();
	}

	/** 为了获取由服务器传递过来的标题字段 */
	private void setTitleMap(LinkedHashMap<String, String> m,
			LinkedHashMap<String, String> p) {
		if (m == null)
			return;
		Iterator<Entry<String, String>> lit = m.entrySet().iterator();
		mListLandKeys = new ArrayList<String>();
		mListLandTitles = new ArrayList<String>();

		while (lit.hasNext()) {
			Map.Entry<String, String> e = lit.next();
			// 解析横屏的标题和对应的KEY值
			mListLandKeys.add(e.getKey());
			mListLandTitles.add(e.getValue());
		}
		if (p != null) {
			// 解析竖屏的标题和对应的KEY值
			mListPortraitKeys = new ArrayList<String>();
			mListPortraitTitles = new ArrayList<String>();

			lit = p.entrySet().iterator();
			while (lit.hasNext()) {
				Map.Entry<String, String> e = lit.next();
				// System.out.println("key="+e.getKey());
				mListPortraitKeys.add(e.getKey());
				mListPortraitTitles.add(e.getValue());
			}
			if (bCBA) {
				fgIndex = mListLandKeys.indexOf("2p");
				tpIndex = mListLandKeys.indexOf("3p");
				ftIndex = mListLandKeys.indexOf("ft");

			} else {
				fgIndex = mListPortraitKeys.indexOf("fg");
				tpIndex = mListPortraitKeys.indexOf("tp");
				ftIndex = mListPortraitKeys.indexOf("ft");
			}

		}
		if (mListPortraitTitles != null)
			i_titleSize = mListPortraitTitles.size();
		HupuLog.d("i_titleSize=" + mListPortraitTitles.size());
	}

	public void updatemBoxscoreDatas(BoxscoreDatas boxscoreData) {
		boxscoreData.mListPLay = mListPLay;
		boxscoreData.mListPLayerNames = mListPLayerNames;
		boxscoreData.mListKeys = mListLandKeys;
		boxscoreData.mTitles = mListLandTitles;
		boxscoreData.i_homeSize = i_homeSize;

		boxscoreData.mMapHomeTotal = mMapHomeTotal;

		boxscoreData.mMapAwayTotal = mMapAwayTotal;
		// 主队命中率
		boxscoreData.str_home_fg = str_home_fg;
		boxscoreData.str_home_tp = str_home_tp;
		boxscoreData.str_home_ft = str_home_ft;
		// 客队命中率
		boxscoreData.str_away_fg = str_away_fg;
		boxscoreData.str_away_tp = str_away_tp;
		boxscoreData.str_away_ft = str_away_ft;
	}

	class Holder {
		// View viewHeader;
		// Header header;

		Button txtPlayerName;
		TextView[] txtDatas;
		View vblank;
		View vLine;
	}

	class Header {
		TextView txtName;
		TextView[] txtTitles;
		View vLineTeam;

	}

	@Override
	public PlayerEntity getItem(int section, int position) {

		if (mListPLay == null)
			return null;
		return mListPLay.get(section * i_homeSize + position);
	}

	@Override
	public long getItemId(int section, int position) {

		return 0;
	}

	@Override
	public int getSectionCount() {

		if (mListLandKeys == null)
			return 0;
		return 2;
	}

	public PlayerEntity getItemAt(int pos) {
		if (mListPLay != null) {
			if (isSectionHeader(pos))
				return null;
			int section = getSectionForPosition(pos);
			int child = getPositionInSectionForPosition(pos);
			int staticIndex = section == 1 ? mListPLay.size() - i_homeSize
					: i_homeSize;
			if (child < staticIndex)
				return getItem(section, child);
			else
				return null;
		}
		return null;
	}

	@Override
	public int getCountForSection(int section) {
		if (section == 0)
			return i_homeSize + 2;
		else
			return i_awaySize + 2;

	}

	public class TagData
	{
		public View view;
		public PlayerEntity entity ;
	}
	@Override
	public View getItemView(int section, int position, View contentView,
			ViewGroup parent) {
		int staticIndex = section == 1 ? mListPLay.size() - i_homeSize
				: i_homeSize;

		Holder holder = null;

		if (contentView == null) {
			//
			contentView = mInflater.inflate(R.layout.item_data_child, null);
			holder = new Holder();
			holder.txtPlayerName = (Button) contentView
					.findViewById(R.id.txt_player_name);
			holder.txtPlayerName.setOnClickListener(mClick);
			holder.txtPlayerName.setTag(new TagData());
			holder.txtDatas = new TextView[i_titleSize];
			holder.vblank = contentView.findViewById(R.id.blank);
			holder.vLine = contentView.findViewById(R.id.blank_line);
			LinearLayout container = (LinearLayout) contentView
					.findViewById(R.id.layout_containter);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					txtWidth, txtHeight);

			for (int i = 0; i < holder.txtDatas.length; i++) {
				holder.txtDatas[i] = buildTextView();
				container.addView(holder.txtDatas[i], params);
			}

			HScrollView scrollView = (HScrollView) contentView
					.findViewById(R.id.hscrollview);
			if (mScroll == null) {
				mScroll = scrollView;
				sob.setScrollView(scrollView);
			} else
				mScroll.AddOnScrollChangedListener(new OnScrollChangedListenerImp(
						scrollView));

			contentView.setTag(holder);
		} else {
			holder = (Holder) contentView.getTag();
		}

		// 主客队队间需要空一行。
		if (section == 0 && position == staticIndex + 1) {
			holder.vblank.setVisibility(View.VISIBLE);
			holder.vLine.setVisibility(View.VISIBLE);
		} else {
			holder.vblank.setVisibility(View.GONE);
			holder.vLine.setVisibility(View.GONE);
		}

		holder.txtPlayerName.setVisibility(View.VISIBLE);
		if (position > 4 && position < staticIndex) {
			// 替补
			if(bCBA)
				contentView
				.setBackgroundResource(R.drawable.shape_statistic2);
			else
			contentView
					.setBackgroundResource(R.drawable.bg_statistic_selector2);
			holder.txtPlayerName.setTextColor(clrTxt);
		} else {
			// 主力球员
			if(bCBA)
				contentView
				.setBackgroundResource(R.drawable.shape_statistic1);
			else
				contentView
					.setBackgroundResource(R.drawable.bg_statistic_selector1);
			holder.txtPlayerName.setTextColor(Color.WHITE);
		}

		PlayerEntity entity = null;
		if (position < staticIndex) {
			entity = getItem(section, position);
			if (entity.on_court == 1)// 场上球员
				holder.txtPlayerName.setTextColor(0xfff9ff50);
			holder.txtPlayerName.setText(entity.str_name);
		} else if (position == staticIndex) {
			holder.txtPlayerName.setText("总计");
		} else
			holder.txtPlayerName.setText("命中率");
		if(mClick!=null)
		{
			//点击球员名字的时候绑定相关数据
			TagData data =(TagData)holder.txtPlayerName.getTag();
			data.view=contentView;
			data.entity=entity;
		}
		for (int i = 0; i < i_titleSize; i++) {
			if(i==holder.txtDatas.length)
				break;
			if (position < staticIndex && entity != null) {
				// 球员
				if (entity.on_court == 1) {
					holder.txtDatas[i].setTextColor(0xfff9ff50);
				} else {
					holder.txtDatas[i].setTextColor(Color.WHITE);
				}
				holder.txtDatas[i].setText(entity.mapDatas
						.get(mListPortraitKeys.get(i)));
			} else if (position == staticIndex) {
				// 统计
				holder.txtDatas[i].setTextColor(Color.WHITE);
				if (section == 1) {
					holder.txtDatas[i].setText(mMapAwayTotal
							.get(mListPortraitKeys.get(i)));
				} else {
					holder.txtDatas[i].setText(mMapHomeTotal
							.get(mListPortraitKeys.get(i)));
				}
			} else
				holder.txtDatas[i].setText("");

		}

		if (position - staticIndex == 1) {
			// 显示命中率
			if (section == 1) {
				holder.txtDatas[fgIndex].setText(str_away_fg);
				holder.txtDatas[tpIndex].setText(str_away_tp);
				holder.txtDatas[ftIndex].setText(str_away_ft);
			} else {
				holder.txtDatas[fgIndex].setText(str_home_fg);
				holder.txtDatas[tpIndex].setText(str_home_tp);
				holder.txtDatas[ftIndex].setText(str_home_ft);
			}
		}
		return contentView;
	}

	int count = 0;

	@Override
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		Header holder = null;
		if (convertView == null) {
			//
			convertView = mInflater.inflate(R.layout.item_data_header, null);
			holder = new Header();
			holder.txtTitles = new TextView[i_titleSize];
			holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
			holder.vLineTeam = convertView.findViewById(R.id.line);
			if (bCBA)
				holder.vLineTeam.setVisibility(View.GONE);
			LinearLayout container = (LinearLayout) convertView
					.findViewById(R.id.layout_containter);
			container.removeAllViews();
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					txtWidth, txtHeight);
			params.gravity = Gravity.CENTER_VERTICAL;

			for (int i = 0; i < i_titleSize; i++) {
				holder.txtTitles[i] = buildTextView();
				container.addView(holder.txtTitles[i], params);
			}
			count++;
			HScrollView scrollView = (HScrollView) convertView
					.findViewById(R.id.hscrollview);
			if (mScroll == null) {
				mScroll = scrollView;
				sob.setScrollView(scrollView);
			} else
				mScroll.AddOnScrollChangedListener(new OnScrollChangedListenerImp(
						scrollView));
			// scrollView.setNoHeader(sob);
			// HupuLog.d("new obj=" + count);
			convertView.setTag(holder);

		} else {
			holder = (Header) convertView.getTag();
		}

		if (section == 0) {
			holder.txtName.setText(homeName);
			if (!bCBA)
				holder.vLineTeam.setBackgroundColor(iColHome);
		} else {
			holder.txtName.setText(awayName);
			if (!bCBA)
				holder.vLineTeam.setBackgroundColor(iColAway);
		}

		for (int i = 0; i < i_titleSize; i++) {
			if(i==holder.txtTitles.length)
				break;
			holder.txtTitles[i].setText(mListPortraitTitles.get(i));

		}
		return convertView;
	}

	/**
	 * @param type
	 *            0 表示标题 1表示数据
	 * */
	private TextView buildTextView() {
		TextView tv = (TextView) mInflater.inflate(R.layout.txt_statistic_data,
				null);
		return tv;
	}

	@SuppressLint("NewApi")
	class OnScrollChangedListenerImp implements OnScrollChangedListener {
		HScrollView mScrollViewArg;

		public OnScrollChangedListenerImp(HScrollView scrollViewar) {
			mScrollViewArg = scrollViewar;
		}

		@Override
		public void onScrollChanged(int l, int t, int oldl, int oldt) {
			mScrollViewArg.smoothScrollTo(l, t);
			// HupuLog.d("onScrollChanged="+l);
		}
	};

}
