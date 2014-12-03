package com.hupu.games.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.data.PrizeEntity;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.pyj.adapter.BaseListAdapter;

/**
 * 猜对次数排行榜 list适配器
 * @author papa
 *
 */
public class ExchangeListAdapter extends BaseListAdapter<PrizeEntity> {

	LayoutInflater inflater;
	ArrayList<PrizeEntity> list;
	
	OnClickListener mClick;
	
	public ExchangeListAdapter(Context context,OnClickListener click) {
		super(context);
		mClick =click;
		
	}
	
    @Override
	public void setData(ArrayList<PrizeEntity> data) {
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
    public PrizeEntity getItem(int position)
    {
        // TODO Auto-generated method stub
        return mListData.get(position);
    }


    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
        
    	Exchange exchange = null;
        PrizeEntity entity = mListData.get(position);
        if (convertView == null) {
        	convertView = mInflater.inflate(R.layout.item_coin_prize,
					null);
        	exchange = new Exchange();
        	exchange.prizeImg = (ImageView) convertView.findViewById(R.id.prize_img);
        	exchange.prizeName = (TextView) convertView.findViewById(R.id.prize_name);
        	exchange.prizeCoin = (TextView) convertView.findViewById(R.id.coin_num);
        	exchange.joinNum = (TextView) convertView.findViewById(R.id.join_num);
        	exchange.prizeType = (ImageView) convertView.findViewById(R.id.prize_status);
        	exchange.joinNum.setVisibility(View.VISIBLE);
        	
        	convertView.setTag(exchange);
        }else {
        	exchange = (Exchange)convertView.getTag();
		}
        
        exchange.prizeName.setText(entity.shortname);

        exchange.prizeCoin.setText(entity.coin+"金豆");

        exchange.joinNum.setVisibility(entity.exchange_count > 0 ? View.VISIBLE:View.INVISIBLE);
        exchange.joinNum.setText(entity.exchange_count+"人已兑换");
        //exchange.joinNum.setText(entity.exchange_count+"人兑换,剩余 "+entity.remain);
        if (entity.type == 0) {
        	exchange.prizeType.setVisibility(View.GONE);
			
		}else {
			exchange.prizeType.setVisibility(View.VISIBLE);
			switch (entity.type) {
			case 1:
				exchange.prizeType.setBackgroundResource(R.drawable.icon_myaccount_new);
				break;
			case 2:
				exchange.prizeType.setBackgroundResource(R.drawable.icon_myaccount_hot);
				break;
			case 3:
				exchange.prizeType.setBackgroundResource(R.drawable.icon_myaccount_limit);
				break;
				
			default:
				break;
			}
		}
        
        UrlImageViewHelper.setUrlDrawable(exchange.prizeImg,entity.img_small,R.drawable.no_news_pic);
		return convertView;
	}
    
    
    class Exchange{
    	ImageView prizeImg;
        TextView prizeName;
        TextView prizeCoin;
        TextView joinNum;
        ImageView prizeType;
    }
}
