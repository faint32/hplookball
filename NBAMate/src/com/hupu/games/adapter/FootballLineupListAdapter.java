package com.hupu.games.adapter;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.LineupEntity;
import com.hupu.games.view.HupuSectionedBaseAdapter;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

/**
 * 比赛统计数据
 * */
public class FootballLineupListAdapter extends HupuSectionedBaseAdapter {

	private LinkedList<LinkedList<LineupEntity>> mListData;
	private LinkedList<LineupEntity> mDebut;
	private LinkedList<LineupEntity> mBench;
	private LinkedList<LineupEntity> mCoach;

	private LayoutInflater mInflater;
	private String debutType;
	public FootballLineupListAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}

	
	public void setData(LinkedList<LineupEntity> data,String type) {
		debutType = type;
		if (mListData!=null) {
			mListData.remove();
		}
		if(data!=null){
			mListData = new LinkedList<LinkedList<LineupEntity>>();
			mDebut = new LinkedList<LineupEntity>();
			mBench = new LinkedList<LineupEntity>();
			mCoach = new LinkedList<LineupEntity>();
			for (LineupEntity entity:data) {
				if (entity.type == 1) //首发
					mDebut.add(entity);
				
				if (entity.type == 2) //替补
					mBench.add(entity);
				
				if (entity.type == 3) //教练
					mCoach.add(entity);
				
			}
			if (mDebut.size() > 0) 
				mListData.add(mDebut);
			if (mBench.size() > 0) 
				mListData.add(mBench);
			if (mCoach.size() > 0) 
				mListData.add(mCoach);
			
			
		}
		else
			mListData = null;
	}


	@Override
	public LineupEntity getItem(int section, int position) {
		// TODO Auto-generated method stub
		if (section == -1 || position == -1)
			return null;
		if (mListData != null) {
			return mListData.get(section).get(position);
		}else
			return null;
	}
	
	public LineupEntity getItemAt(int pos) {
		if (mListData != null) {
			int section = getSectionForPosition(pos);
			int child = getPositionInSectionForPosition(pos);
			return getItem(section, child);

		}
		return null;
	}

	@Override
	public long getItemId(int section, int position) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getSectionCount() {
		// TODO Auto-generated method stub
		if (mListData != null) 
			return mListData.size();
		
		return 0;
	}


	@Override
	public int getCountForSection(int section) {
		// TODO Auto-generated method stub
		if (mListData != null) 
			return mListData.get(section).size();
		return 0;
	}


	@Override
	public View getItemView(int section, int position, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder item = new Holder();
		LineupEntity entity = mListData.get(section).get(position);
		if (convertView == null) {
			convertView =initLineup(item);
		} else {
			item=(Holder)convertView.getTag();
		}
		
		item.player_num.setText(entity.type !=3?entity.number+"":"");
		item.player_name.setText(entity.player_name);
		item.position.setText(entity.position);
		UrlImageViewHelper.setUrlDrawable(item.logo, entity.player_header,R.drawable.no_photo);
		
		if (entity.eventInfo!=null) {
			if (entity.eventInfo.size() > 0) {
				if (entity.eventInfo.size() > 1) {
					item.up_layout.setVisibility(View.VISIBLE);
					item.under_layout.setVisibility(View.VISIBLE);
					if (entity.eventInfo.get(0).type == 1) {
						item.up_time.setText(entity.eventInfo.get(0).time);
						item.under_time.setText(entity.eventInfo.get(1).time);
					}else {
						item.under_time.setText(entity.eventInfo.get(0).time);
						item.up_time.setText(entity.eventInfo.get(1).time);
					}
					
				}else {
					if (entity.eventInfo.get(0).type == 1) {
						
						item.up_layout.setVisibility(View.VISIBLE);
						item.under_layout.setVisibility(View.GONE);
						item.up_time.setText(entity.eventInfo.get(0).time);
					}else {
						item.under_layout.setVisibility(View.VISIBLE);
						item.up_layout.setVisibility(View.GONE);
						item.under_time.setText(entity.eventInfo.get(0).time);
					}
				}
			}else {
				item.up_layout.setVisibility(View.GONE);
				item.under_layout.setVisibility(View.GONE);
			}
			
		}else {
			item.up_layout.setVisibility(View.GONE);
			item.under_layout.setVisibility(View.GONE);
		}
		item.rating_num.setText(entity.rating);
		if (entity.mark == 0) {
			item.rating_img.setVisibility(View.INVISIBLE);
		}else {
			item.rating_img.setVisibility(View.VISIBLE);
			item.rating_img.setBackgroundResource(entity.mark == 1?R.drawable.icon_like:R.drawable.icon_dislike);
		}
		if (position % 2 == 0) {
			convertView.setBackgroundResource(R.drawable.bg_lineup_list1_selector);
		} else {
			convertView.setBackgroundResource(R.drawable.bg_lineup_list2_selector);
		}
		return convertView;
	}


	@Override
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = mInflater.inflate(R.layout.item_lineup_list_header, null);
		switch (section) {
		case 0:
			((TextView)convertView.findViewById(R.id.txt_date)).setText("首发( "+debutType + " )");
			break;
		case 1:
			((TextView)convertView.findViewById(R.id.txt_date)).setText("替补");
			break;
		case 2:
			((TextView)convertView.findViewById(R.id.txt_date)).setText("教练");
			break;

		default:
			break;
		}
			
		return convertView;
	}
	
	
	
	
	private View initLineup(Holder lineup) {
		View content = mInflater.inflate(R.layout.item_football_lineup,
				null, false);
		lineup.player_num = (TextView) content.findViewById(R.id.player_number);
		lineup.logo = (ImageView) content.findViewById(R.id.player_logo);
		lineup.player_name = (TextView) content.findViewById(R.id.player_name);
		lineup.position = (TextView) content.findViewById(R.id.position);
		
		
		lineup.up_layout = (LinearLayout) content.findViewById(R.id.layout_up);
		lineup.up_time = (TextView) content.findViewById(R.id.up_time);
		lineup.under_layout = (LinearLayout) content.findViewById(R.id.layout_under);
		lineup.under_time = (TextView) content
				.findViewById(R.id.under_time);
		lineup.rating_num = (TextView) content.findViewById(R.id.rating_num);
		lineup.rating_img = (ImageView) content.findViewById(R.id.rating_img);
		content.setTag(lineup);
		return content;
	}
	
	class Holder {
		TextView player_num;
		ImageView logo;
		TextView player_name;
		TextView position;
		
		LinearLayout up_layout;
		TextView up_time;
		LinearLayout under_layout;
		TextView under_time;
		TextView rating_num;
		ImageView rating_img;
		
		
		

	}
	
}
