package com.hupu.games.adapter;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.data.FormationEntity;

/**
 * 猜对次数排行榜 list适配器
 * @author papa
 *
 */
public class LineupMapListAdapter extends BaseAdapter {

	LayoutInflater mInflater;
	LinkedList<FormationEntity> mListData;
	int teamType = 1;
	//OnClickListener mClick;
	
	public LineupMapListAdapter(Context context) {
		//mClick =click;
		mInflater = LayoutInflater.from(context);
		
	}
	
    public void setData(LinkedList<FormationEntity> data,int type) {
		// TODO Auto-generated method stub
    	teamType = type;
		if(data!=null)
		{
			mListData = data;
		}
	}

	@Override
    public int getCount()
    {
        // TODO Auto-generated method stub
//    	if(mListData != null)
//    		return mListData.size();
    	return 30;
    }


    @Override
    public FormationEntity getItem(int position)
    {
        // TODO Auto-generated method stub
		if (mListData == null)
			return null;
		return mListData.get(position);
    }


    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
        
    	Lineup lineup = null;
        if (convertView == null) {
        	convertView = mInflater.inflate(R.layout.item_lineup_map,
					null);
        	lineup = new Lineup();
        	lineup.muchPlaceh = (View) convertView.findViewById(R.id.lineup_much_placeh);
        	lineup.smallPlaceh = (View) convertView.findViewById(R.id.lineup_small_placeh);
        	lineup.playerNumber = (TextView) convertView.findViewById(R.id.lineup_player_number);
        	lineup.playerName = (TextView) convertView.findViewById(R.id.lineup_player_name);
        	
        	lineup.playerNumber.setBackgroundResource(teamType == 1?R.drawable.lineup_home_player_selector:R.drawable.lineup_away_player_selector);
        	lineup.playerNumber.setTextColor(teamType == 1?Color.BLACK:Color.WHITE);
        	convertView.setTag(lineup);
        }
        else {
        	lineup = (Lineup)convertView.getTag();
		}
        
        for (FormationEntity pleryInfo:mListData) {
        	int pos = pleryInfo.coordinateY * 5 + pleryInfo.coordinateX;
        	
			if (pos == position) {
				//HupuLog.e("papa", "pos----"+pos+"-----"+pleryInfo.coordinateY+"-------"+pleryInfo.coordinateX+"=--------position="+position);
				convertView.setVisibility(View.VISIBLE);
				lineup.playerNumber.setText(pleryInfo.number+"");
				lineup.playerName.setText(pleryInfo.player_name);
			}
		}
        
//        if (mListData.size() > position) {
////			if ((mListData.get(position).coordinateY * 5 + mListData.get(position).coordinateX)% ) {
////				
////			}
//        	FormationEntity entity = mListData.get(position);
//        	
//		}
        if (position == 2 || position == 6 || position == 8 || position == 11||position == 13|| position == 16|| position == 18|| position == 21|| position == 23) {
        	lineup.smallPlaceh.setVisibility(View.VISIBLE);
		}
        if (position > 2&&position % 5 == 2) {
        	lineup.muchPlaceh.setVisibility(View.VISIBLE);
		}
        
//        if (position == 25 ||position == 26 ||position == 28 ||position == 29  ) {
//        	convertView.setVisibility(View.GONE);
//		}
        
		return convertView;
	}
    
    
    class Lineup{
    	View muchPlaceh;
    	View smallPlaceh;
        TextView playerNumber;
        TextView playerName;
    }


	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}
