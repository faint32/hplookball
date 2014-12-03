package com.hupu.games.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.data.CoinTopEntity;
import com.pyj.adapter.BaseListAdapter;


/**
 * 获取金币排行榜 list适配器
 * @author papa
 *
 */
public class CoinRanksAdapter extends BaseListAdapter<CoinTopEntity> {

	LayoutInflater inflater;
	ArrayList<CoinTopEntity> ranks;
	String rankType;
	//private FinalBitmap finaBitmap;
	
	public CoinRanksAdapter(Context context) {
		super(context);
	}
	
    @Override
	public void setData(ArrayList<CoinTopEntity> data) {
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
    public CoinTopEntity getItem(int position)
    {
        // TODO Auto-generated method stub
        return ranks.get(position);
    }


    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
        
    	CoinTop coinTop = null;
    	CoinTopEntity entity = mListData.get(position);
        if (convertView == null) {
        	convertView = mInflater.inflate(R.layout.item_coin_rank,
					null);
        	coinTop = new CoinTop();
        	coinTop.rank_num = (TextView) convertView.findViewById(R.id.rank_num);
        	coinTop.name = (TextView) convertView.findViewById(R.id.user_id);
        	coinTop.coinNum = (TextView) convertView.findViewById(R.id.coin_num);
        	
        	convertView.setTag(coinTop);
        }else {
			coinTop = (CoinTop)convertView.getTag();
		}
        
        
        coinTop.rank_num.setText(entity.rank);
        coinTop.name.setText(entity.name);
        coinTop.coinNum.setText(entity.coins);
        
        if (position < 3) {
        	coinTop.rank_num.setBackgroundResource(R.drawable.bg_red_garden);
		}else {
			coinTop.rank_num.setBackgroundColor(0x00FFFFFF);
		}
        
        if (position % 2 == 0)
        {
            convertView.setBackgroundResource(R.drawable.bg_archer_list_down);
        }else {
            convertView.setBackgroundResource(R.drawable.bg_archer_list_up);
        }
		
		return convertView;
	}
    
    class CoinTop{
        TextView rank_num;
        TextView name;
        TextView coinNum;
    }
}
