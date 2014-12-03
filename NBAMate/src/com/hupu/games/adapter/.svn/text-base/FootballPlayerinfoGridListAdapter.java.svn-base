package com.hupu.games.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.data.game.basketball.NbaPlayerInfoReq.NbaPlayerInfoDataEntity;
import com.hupu.games.data.game.football.FootballPlayerInfoReq.FootballPlayerInfoDataEntity;
import com.pyj.adapter.BaseListAdapter;

/**
 * 猜对次数排行榜 list适配器
 * @author papa
 *
 */
public class FootballPlayerinfoGridListAdapter extends BaseListAdapter<FootballPlayerInfoDataEntity> {

	ArrayList<FootballPlayerInfoDataEntity> infoList;
	
	
	public FootballPlayerinfoGridListAdapter(Context context) {
		super(context);
		
	}
	
    @Override
	public void setData(ArrayList<FootballPlayerInfoDataEntity> data) {
		// TODO Auto-generated method stub
		super.setData(data);
		if(data!=null)
		{
			mListData = data;
		}
	}

	@Override
    public int getCount()
    {
        // TODO Auto-generated method stub
    	if(mListData != null)
        return mListData.size();
    	return 0;
    }


    @Override
    public FootballPlayerInfoDataEntity getItem(int position)
    {
        // TODO Auto-generated method stub
        return mListData.get(position);
    }


    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
        
    	InfoData infoData = null;
    	FootballPlayerInfoDataEntity entity = mListData.get(position);
        if (convertView == null) {
        	convertView = mInflater.inflate(R.layout.item_player_info_data,
					null);
        	infoData = new InfoData();
        	infoData.dataInfo = (TextView) convertView.findViewById(R.id.data_info);
        	infoData.dataTitle = (TextView) convertView.findViewById(R.id.data_title);
        	
        	convertView.setTag(infoData);
        }else {
        	infoData = (InfoData)convertView.getTag();
		}
        
        infoData.dataTitle.setText(entity.name);
        infoData.dataInfo.setText(entity.num);
     
		return convertView;
	}
    
    
    class InfoData{
        TextView dataInfo;
        TextView dataTitle;
    }
}
