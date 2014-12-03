package com.hupu.games.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.data.game.basketball.NbaPlayerInfoReq.NbaPlayerInfoDataEntity;
import com.pyj.adapter.BaseListAdapter;

/**
 * 球员赛季平均数据
 * @author papa
 *
 */
public class PlayerinfoGridListAdapter extends BaseListAdapter<NbaPlayerInfoDataEntity> {

	ArrayList<NbaPlayerInfoDataEntity> infoList;
	
	
	public PlayerinfoGridListAdapter(Context context) {
		super(context);
		
	}
	
    @Override
	public void setData(ArrayList<NbaPlayerInfoDataEntity> data) {
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
    public NbaPlayerInfoDataEntity getItem(int position)
    {
        // TODO Auto-generated method stub
        return mListData.get(position);
    }


    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
        
    	InfoData infoData = null;
    	NbaPlayerInfoDataEntity entity = mListData.get(position);
        if (convertView == null) {
        	convertView = mInflater.inflate(R.layout.item_player_info_data,
					null);
        	infoData = new InfoData();
        	infoData.dataInfo = (TextView) convertView.findViewById(R.id.data_info);
        	infoData.dataTitle = (TextView) convertView.findViewById(R.id.data_title);
        	infoData.dataRank = (TextView) convertView.findViewById(R.id.data_rank);
        	
        	convertView.setTag(infoData);
        }else {
        	infoData = (InfoData)convertView.getTag();
		}
        
        infoData.dataInfo.setText(entity.values[1]);
        infoData.dataTitle.setText(entity.values[0]);
        infoData.dataRank.setText(entity.values[2]);
        
     
		return convertView;
	}
    
    
    class InfoData{
        TextView dataInfo;
        TextView dataTitle;
        TextView dataRank;
    }
}
