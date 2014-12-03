package com.hupu.games.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.data.MyPrizeEntity;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.pyj.adapter.BaseListAdapter;

/**
 * 猜对次数排行榜 list适配器
 * @author papa
 *
 */
public class MyPrizeListAdapter extends BaseListAdapter<MyPrizeEntity> {

	LayoutInflater inflater;
	ArrayList<MyPrizeEntity> list;
	
	OnClickListener mClick;

	public MyPrizeListAdapter(Context context,OnClickListener click) {
		super(context);
		mClick =click;
		
	}
	
    @Override
	public void setData(ArrayList<MyPrizeEntity> data) {
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
    public MyPrizeEntity getItem(int position)
    {
        // TODO Auto-generated method stub
        return list.get(position);
    }


    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
        
    	MyPrize myPrize = null;
    	MyPrizeEntity entity = mListData.get(position);
        if (convertView == null) {
        	convertView = mInflater.inflate(R.layout.item_my_prize,
					null);
        	myPrize = new MyPrize();
        	myPrize.prizeImg = (ImageView) convertView.findViewById(R.id.prize_img);
        	myPrize.prizeName = (TextView) convertView.findViewById(R.id.prize_name);
        	myPrize.btnExchange = (Button) convertView.findViewById(R.id.commit_exchange);
        	
        	convertView.setTag(myPrize);
        }else {
        	myPrize = (MyPrize)convertView.getTag();
		}
        
        myPrize.prizeName.setText(entity.name);
        
        UrlImageViewHelper.setUrlDrawable(myPrize.prizeImg,entity.imgUrl,R.drawable.no_news_pic);
        
        myPrize.btnExchange.setOnClickListener(mClick);
        myPrize.btnExchange.setTag(entity.id);
        
        if (entity.status.equals("0")) {
			myPrize.btnExchange.setBackgroundResource(R.drawable.quiz_btn_selector);
			myPrize.btnExchange.setText("领取");
			myPrize.btnExchange.setEnabled(true);
			myPrize.btnExchange.setOnClickListener(mClick);
        }else{
			myPrize.btnExchange.setBackgroundColor(Color.GRAY);
			myPrize.btnExchange.setEnabled(false);
			myPrize.btnExchange.setText("已领取");
		}
        
        
			
        //exchange.prizeName.setText(entity.name);
//        if (position % 2 == 0)
//        {
//            convertView.setBackgroundResource(R.drawable.football_rank_bg2);
//        }else {
//            convertView.setBackgroundResource(R.drawable.football_rank_bg);
//        }
		
		return convertView;
	}
    
    
    class MyPrize{
    	ImageView prizeImg;
        TextView prizeName;
        //TextView prizeState;
        Button btnExchange;
    }
}
