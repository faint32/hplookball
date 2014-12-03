package com.hupu.games.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hupu.games.HuPuApp;
import com.hupu.games.R;
import com.hupu.games.data.StandingsResp;
import com.hupu.games.data.TeamRankEntity;


/**
 * 球队排行列表
 * @author panyongjun
 * */
public class BSStandingsListAdapter extends BaseAdapter {

	private StandingsResp mResp;

	private LayoutInflater mInflater;

   private int frame;
   
   private int i_color_text;
   
	public BSStandingsListAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		i_color_text =context.getResources().getColor(R.color.txt_status);
	}

	@Override
	public int getCount() {
		if (mResp == null || mResp.mListEast == null || mResp.mListWest == null)
			return 0;
		if(frame==0)
			return mResp.mListEast.size();
		else
			return mResp.mListWest.size();
	}

	@Override
	public TeamRankEntity getItem(int arg0) {
		if (mResp == null)
			return null;
		if(frame==0)
			return mResp.mListEast.get(arg0);
		else
			return mResp.mListWest.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}


	public void setData(StandingsResp resp) {
		mResp = resp;
		notifyDataSetChanged();
	}

	public void switchToWest()
	{
		frame =1;
		notifyDataSetChanged();
	}
	public void switchToEast(){
		frame =0;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		TeamRankEntity entity = getItem(position);
		Holder holder = null;
		if (convertView == null) {
			//
			convertView = mInflater.inflate(R.layout.item_standings, null);
			holder = new Holder();
			holder.txtRank =(TextView)convertView.findViewById(R.id.txt_rank);
			holder.teamLogo =(ImageView)convertView.findViewById(R.id.team_logo);
			holder.txtTeamName=(TextView)convertView.findViewById(R.id.txt_team);
			holder.txtWin=(TextView)convertView.findViewById(R.id.txt_win);
			holder. txtLost=(TextView)convertView.findViewById(R.id.txt_lost);
			holder.txtDif=(TextView)convertView.findViewById(R.id.txt_dif);
//			holder.txtPf=(TextView)convertView.findViewById(R.id.txt_pf);
//			holder.txtPa=(TextView)convertView.findViewById(R.id.txt_pa);
			holder.txtStrk=(TextView)convertView.findViewById(R.id.txt_status);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();

		}
		if(position>7)
		{
		
			holder.txtRank.setBackgroundResource(R.drawable.bg_1x1);
		}
		else
		{
			holder.txtRank.setBackgroundResource(R.drawable.bg_red_garden);
		}
			
		setIcon(holder.teamLogo, entity.i_tid);
		
		holder.txtRank.setText(position+1+"");
		holder.txtTeamName.setText(entity.str_name);
		holder.txtWin.setText(entity.i_win+"");
		holder. txtLost.setText(entity.i_lost+"");
		holder.txtDif.setText(entity.i_gb+"");
//		holder.txtPf.setText(entity.i_pf+"");
//		holder.txtPa.setText(entity.i_pa+"");
		holder.txtStrk.setText(entity.str_strk);
		
		return convertView;
	}

	private void setIcon(ImageView tv, int res) {
		// tv.setCompoundDrawablesWithIntrinsicBounds(
		// HuPuApp.getTeamData(res).i_logo_small, 0, 0, 0);
		tv.setImageResource(HuPuApp.getTeamData(res).i_logo);
	}
	static class Holder {
		// 第一列
		TextView txtRank;
		ImageView teamLogo;
		TextView txtTeamName;
		TextView txtWin;
		TextView txtLost;
		TextView txtDif;
//		TextView txtPf;
//		TextView txtPa;
		TextView txtStrk;
	}

}
