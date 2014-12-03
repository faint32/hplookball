package com.hupu.games.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hupu.games.R;
import com.hupu.games.HuPuApp;
import com.hupu.games.activity.NBAGameActivity.BoxscoreDatas;
import com.hupu.games.data.PlayerEntity;

/**
 * 比赛统计数据
 * */
public class GameDataListLandAdapter extends BaseAdapter {

	private ArrayList<PlayerEntity> mListPLay;

	private LayoutInflater mInflater;

	/** 主队球员数量 */
	private int i_homeSize;

	BoxscoreDatas mData;
	
	int fgIndex=-1;
	int tpIndex=-1;
	int ftIndex=-1;
	int clrMain;
	int clrSub;
	int clrTxt;
	private boolean bCBA;
	
	public GameDataListLandAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		clrMain = context.getResources().getColor(R.color.dark_gray);
		clrSub = context.getResources().getColor(R.color.res_cor6);
		clrTxt= context.getResources().getColor(R.color.txt_status);
	}

	public GameDataListLandAdapter(Context context,boolean CBA)
	{
		mInflater = LayoutInflater.from(context);
		clrMain = context.getResources().getColor(R.color.dark_gray);
		clrSub = context.getResources().getColor(R.color.res_cor6);
		clrTxt= context.getResources().getColor(R.color.txt_status);
		bCBA =CBA;
	}
	
	public void setData(BoxscoreDatas boxscoreData) {
		if(i_homeSize ==0)
		{
			i_homeSize = boxscoreData.i_homeSize;
			if(bCBA)
			{
				fgIndex =boxscoreData.mListKeys.indexOf("2p");
				tpIndex=boxscoreData.mListKeys.indexOf("3p");
				ftIndex=boxscoreData.mListKeys.indexOf("ft");
			}
			else
			{
				fgIndex =boxscoreData.mListKeys.indexOf("fg");
				tpIndex=boxscoreData.mListKeys.indexOf("tp");
				ftIndex=boxscoreData.mListKeys.indexOf("ft");
			}
			
		}
		mListPLay = boxscoreData.mListPLay;
		mData =boxscoreData;
		notifyDataSetChanged();
	}



	class Holder {
		TextView txtPlayerName;
		TextView[] datas;
	}

	private int mMode;
	private static int AWAY_MODE = 1;

	public void changeMode(int mode) {
		if (mode != mMode) {
			mMode = mode;
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		if (mListPLay != null) {
			if (mMode == AWAY_MODE)		
				return mListPLay.size() - i_homeSize+2;
			return i_homeSize +2;
		}
		return 0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public PlayerEntity getItem(int position) {

		if (mListPLay == null)
			return null;
		
		return mListPLay.get(position);
	}

	public PlayerEntity getItemAt(int position) 
	{
		if (mListPLay == null)
			return null;
		int index = mMode == AWAY_MODE ? i_homeSize : 0;
		int staticIndex = mMode == AWAY_MODE ? mListPLay.size()- i_homeSize : i_homeSize;
		if(position<staticIndex)
			return getItem(index + position) ;
		return null;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		int index = mMode == AWAY_MODE ? i_homeSize : 0;
		int staticIndex = mMode == AWAY_MODE ? mListPLay.size()- i_homeSize : i_homeSize;
	

		Holder holder = null;
		if (convertView == null) {
			//
			LinearLayout v = (LinearLayout) mInflater.inflate(
					R.layout.item_data_child_land, null);
			convertView = v;
			holder = new Holder();
			holder.txtPlayerName = (TextView) convertView
					.findViewById(R.id.txt_play_name);
			holder.datas = new TextView[mData.mListKeys.size()];
			LinearLayout.LayoutParams llp = null;
			String key =null;
			for (int i = 0; i < holder.datas.length; i++) {
				holder.datas[i] = (TextView)  mInflater
						.inflate(R.layout.static_child_land, null);
				key =mData.mListKeys.get(i);
				if (key.equals("2p") || key.equals("3p") || key.equals("ft") ||key.equals("fg") || key.equals("ft") || key.equals("tp"))
				{	
					
					llp = new LinearLayout.LayoutParams(0,
							-2, 10);
				}
				else if (key.equals("mins") ||key.equals("pts") )
					llp = new LinearLayout.LayoutParams(0,
							-2, 7);
				else
					llp = new LinearLayout.LayoutParams(0,
							-2, 5);
			
				v.addView(holder.datas[i] , llp);
			}
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		PlayerEntity entity =null;
		if(position>4 && position<staticIndex)
		{
//			convertView.setBackgroundColor(clrSub);
			holder.txtPlayerName .setTextColor(clrTxt);
			if(bCBA)
				convertView
				.setBackgroundResource(R.drawable.shape_statistic2);
			else
				convertView.setBackgroundResource(R.drawable.bg_statistic_selector2);
		}
		else
		{
//			convertView.setBackgroundColor(clrMain);
			holder.txtPlayerName .setTextColor(Color.WHITE);
			if(bCBA)
				convertView
				.setBackgroundResource(R.drawable.shape_statistic1);
			else
				convertView.setBackgroundResource(R.drawable.bg_statistic_selector1);
		}
		if(position<staticIndex)
		{
			entity = getItem(index + position);
			if(entity.on_court==1)
				holder.txtPlayerName.setTextColor(0xfff9ff50);
			holder.txtPlayerName.setText(entity.str_name);
			for (int i = 0; i < holder.datas.length; i++) {
				if(entity.on_court==1)
				{
					holder.datas[i].setTextColor(0xfff9ff50);
				}
				else
				{
					holder.datas[i].setTextColor(Color.WHITE);
				}
				holder.datas[i].setText(entity.mapDatas.get(mData.mListKeys.get(i)));
			}
		}
		else if(position==staticIndex)
		{
			//统计
			holder.txtPlayerName.setText("总计");
			for (int i = 0; i < holder.datas.length; i++) {
				holder.datas[i].setTextColor(Color.WHITE);
				if(mMode == AWAY_MODE)
					holder.datas[i].setText(mData.mMapAwayTotal.get( mData.mListKeys.get(i) ));
				else
					holder.datas[i].setText(mData.mMapHomeTotal.get( mData.mListKeys.get(i) ));
			}
		}
		else
		{
			holder.txtPlayerName.setText("命中率");
			for (int i = 0; i < holder.datas.length; i++) {
				holder.datas[i].setTextColor(Color.WHITE);
				holder.datas[i].setText("");
			}
			//命中率
			if(mMode == AWAY_MODE)
			{
				holder.datas[fgIndex].setText(mData.str_away_fg);
				holder.datas[tpIndex].setText(mData.str_away_tp);
				holder.datas[ftIndex].setText(mData.str_away_ft);
			}
			else
			{
				holder.datas[fgIndex].setText(mData.str_home_fg);
				holder.datas[tpIndex].setText(mData.str_home_tp);
				holder.datas[ftIndex].setText(mData.str_home_ft);
			}
		}

			
		
		return convertView;
	}

}
