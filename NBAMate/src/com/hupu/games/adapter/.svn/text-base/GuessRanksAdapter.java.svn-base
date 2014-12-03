package com.hupu.games.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.data.GuessTopEntity;
import com.pyj.adapter.BaseListAdapter;

/**
 * 猜对次数排行榜 list适配器
 * @author papa
 *
 */
public class GuessRanksAdapter extends BaseListAdapter<GuessTopEntity> {

	LayoutInflater inflater;
	ArrayList<GuessTopEntity> ranks;
	String rankType;
	//private FinalBitmap finaBitmap;
	
	public GuessRanksAdapter(Context context) {
		super(context);
	}
	
    @Override
	public void setData(ArrayList<GuessTopEntity> data) {
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
    public GuessTopEntity getItem(int position)
    {
        // TODO Auto-generated method stub
        return ranks.get(position);
    }


    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
        
    	GuessTop guessTop = null;
        GuessTopEntity guesseEntity = mListData.get(position);
        if (convertView == null) {
        	convertView = mInflater.inflate(R.layout.item_guess_rank,
					null);
        	guessTop = new GuessTop();
        	guessTop.rank_num = (TextView) convertView.findViewById(R.id.rank_num);
        	guessTop.name = (TextView) convertView.findViewById(R.id.user_id);
        	guessTop.guessRightNum = (TextView) convertView.findViewById(R.id.right_num);
        	guessTop.winScale = (TextView) convertView.findViewById(R.id.win_scale);
        	
        	convertView.setTag(guessTop);
        }else {
			guessTop = (GuessTop)convertView.getTag();
		}
        
        
        guessTop.rank_num.setText(guesseEntity.rank);
        guessTop.name.setText(guesseEntity.name);
        guessTop.guessRightNum.setText(""+guesseEntity.win_count);
        guessTop.winScale.setText(guesseEntity.rate);
        
        if (position < 3) {
        	guessTop.rank_num.setBackgroundResource(R.drawable.bg_red_garden);
		}else {
			guessTop.rank_num.setBackgroundColor(0x00FFFFFF);
		}
        
        if (position % 2 == 0)
        {
            convertView.setBackgroundResource(R.drawable.bg_archer_list_down);
        }else {
            convertView.setBackgroundResource(R.drawable.bg_archer_list_up);
        }
		
		return convertView;
	}
    
    
    class GuessTop{
        TextView rank_num;
        TextView name;
        TextView guessRightNum;
        TextView winScale;
    }
}
