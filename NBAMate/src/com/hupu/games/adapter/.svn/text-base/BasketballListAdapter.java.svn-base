package com.hupu.games.adapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.data.game.basketball.BasketballGameData;
import com.hupu.games.data.game.basketball.BasketballResp;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.pyj.common.MyUtility;

/**
 * 篮球赛程列表
 * */
public class BasketballListAdapter extends SectionedBaseAdapter  {


	private LinkedHashMap<String, ArrayList<BasketballGameData>> mListGames;
	private ArrayList<String> mKeys;
	private LayoutInflater mInflater;
	private OnClickListener mClick;

	public BasketballListAdapter(Context context,OnClickListener c) {
		mInflater = LayoutInflater.from(context);
		mClick =c;

	}

	


	public void setData(BasketballResp en) {
		if(en ==null)
		{
			mListGames =null;
			mKeys=null;
		}else
		{
			mListGames =en.mMap;
			mKeys=en.mKeys;
		}
		
		notifyDataSetChanged();
	}


	private void setSore(TextView tv, int homeScore, int awayScore) {
		tv.setText(homeScore +"-"+awayScore);

	}

	static class Holder {
	
		TextView txtHomeName;
		TextView txtAwayName;
		
		ImageView imgHomeLogo;
		ImageView imgAwayLogo;

		TextView txtProccess;
		TextView txtScore;

		ImageView imgFollow;
		ImageView imgLive;
		//View followLayout;
	}

	static class Header
	{
		TextView txtDate;
		TextView txtRound;
	}
	
	@Override
	public BasketballGameData getItem(int section, int position) {
		if(section ==-1 ||position==-1 )
			return null;
		if(mListGames!= null )
		{
			return mListGames.get(mKeys.get(section)).get(position);
		}
		return null;
	}

	public BasketballGameData getItemAt(int pos) {
		if(mListGames!= null )
		{
			int section =getSectionForPosition(pos);
			int child =getPositionInSectionForPosition(pos);
			return getItem(section,  child);
			
		}
		return null;
	}
	
	@Override
	public long getItemId(int section, int position) {
		
		return 0;
	}

	@Override
	public int getSectionCount() {
		if(mKeys!=null)
			return mKeys.size();
		else
			return 0;
	}

	@Override
	public int getCountForSection(int section) {
		
		if(mListGames!= null )
		{
			String key =mKeys.get(section);
			ArrayList<BasketballGameData> datas =mListGames.get(key);
//			Log.d("head section","section"+section+";  size ="+size );
			return datas.size();
		}
		else
			return 0;
	}

	@Override
	public View getItemView(int section, int position, View convertView,
			ViewGroup parent) {

		BasketballGameData entity = getItem(section,position);
		
		Holder holder = null;
		if (convertView == null) {
			//
			convertView = mInflater.inflate(R.layout.item_basketball_child, null);
			holder = new Holder();
			holder.txtHomeName = (TextView) convertView
					.findViewById(R.id.txt_team_left);
			holder.txtAwayName = (TextView) convertView
					.findViewById(R.id.txt_team_right);
			
			holder.imgHomeLogo = (ImageView) convertView.findViewById(R.id.img_team_left);
			holder.imgAwayLogo = (ImageView) convertView.findViewById(R.id.img_team_right);
			holder.txtProccess = (TextView) convertView
					.findViewById(R.id.txt_proccess);
			holder.txtScore = (TextView) convertView
					.findViewById(R.id.txt_score);
			

			//holder.followLayout = convertView.findViewById(R.id.layout_follow);
			holder.imgFollow = (ImageView) convertView.findViewById(R.id.img_follow);
			holder.imgLive = (ImageView) convertView.findViewById(R.id.img_live);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.txtHomeName.setText(entity.str_home_name);
		holder.txtAwayName.setText(entity.str_away_name);
		
		//写在这里解决 切换时  默认图的问题   可是有些许影响效率
        
        UrlImageViewHelper.setUrlDrawable(holder.imgHomeLogo,entity.home_logo,R.drawable.bg_home_nologo);
        UrlImageViewHelper.setUrlDrawable(holder.imgAwayLogo,entity.away_logo,R.drawable.bg_home_nologo);
		if (entity.byt_status == BasketballGameData.STATUS_WAITING
				||entity.byt_status == BasketballGameData.STATUS_DELAY
				||entity.byt_status == BasketballGameData.STATUS_CANCEL) {
			// 等待开始
			holder.txtScore.setVisibility(View.GONE);
			holder.imgFollow.setVisibility(View.VISIBLE);
			//holder.followLayout.setVisibility(View.VISIBLE);
			if (entity.i_live<1)
				holder.imgLive.setVisibility(View.INVISIBLE);
			else if(entity.i_live==1){
				holder.imgLive.setVisibility(View.VISIBLE);				
				if (entity.casino == 1) {
					holder.imgLive.setImageResource(R.drawable.icon_guess_up);				
				} else {
					holder.imgLive.setImageResource(R.drawable.icon_live_up);
				}
			}
			
			holder.txtProccess.setText(MyUtility
					.getStartTime(entity.l_begin_time * 1000));	
			holder.imgFollow.setTag(entity);
			holder.imgFollow.setOnClickListener(mClick);
			if (entity.bFollow == 1) {
				holder.imgFollow.setImageResource(R.drawable.btn_dated);
			} else {
				holder.imgFollow.setImageResource(R.drawable.btn_date);
			}
			
		} else if (entity.byt_status == BasketballGameData.STATUS_START) {
			// 已经开始
			holder.txtScore.setVisibility(View.VISIBLE);
			//holder.followLayout.setVisibility(View.GONE);
			holder.imgFollow.setVisibility(View.GONE);
			if (entity.i_live <1)
				holder.imgLive.setVisibility(View.INVISIBLE);
			else if(entity.i_live==1){
				holder.imgLive.setVisibility(View.VISIBLE);
				if (entity.casino == 1) {
					holder.imgLive.setImageResource(R.drawable.icon_guess_down);					
				} else  {
					holder.imgLive.setImageResource(R.drawable.icon_live_down);
				}
			}
			setSore(holder.txtScore, entity.i_home_score, entity.i_away_score);	
			if (entity.str_process != null)
				holder.txtProccess.setText(entity.str_process);
		} else {
			// 结束
			holder.imgFollow.setVisibility(View.GONE);
			//holder.followLayout.setVisibility(View.GONE);
			holder.txtScore.setVisibility(View.VISIBLE);
			holder.imgLive.setVisibility(View.INVISIBLE);
			setSore(holder.txtScore, entity.i_home_score, entity.i_away_score);
			if (entity.str_process != null)
				holder.txtProccess.setText(entity.str_process);
		}		
		return convertView;
	}

	@Override
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		Header header =null;
		if(convertView==null)
		{
			convertView = mInflater.inflate(R.layout.item_cba_header, null);
			header =new Header();
			header.txtDate =(TextView)convertView.findViewById(R.id.txt_date);
			convertView.setTag(header);
		}
		else 
			header=(Header)convertView.getTag();
		if(mKeys!=null)
		{
			header.txtDate.setText(mKeys.get(section));
		}
		else
			header.txtDate.setText("");
		return convertView;
	}
}
