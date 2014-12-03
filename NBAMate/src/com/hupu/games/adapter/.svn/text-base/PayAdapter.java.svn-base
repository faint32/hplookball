package com.hupu.games.adapter;

import java.util.LinkedList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.data.OrderPacEntity;

public class PayAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private LinkedList<String> mList;
	private PayCallLisener listener;
	
	public PayAdapter(Context mcontext) {
		inflater = LayoutInflater.from(mcontext);
	}

	public void setData(LinkedList<String> mList) {
		this.mList = mList;
		notifyDataSetChanged();
	}
	
	public void setPayCallListener(PayCallLisener call){
		this.listener=call;
	}
	
	public interface PayCallLisener {
	    public void onCallListener(String tag);
	}
	
	@Override
	public int getCount() {
		if (null != mList) {
			return mList.size();
		} else {
			return 0;
		}
	}

	@Override
	public String getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.item_pay_blance, null);
		TextView txt_title = (TextView) convertView
				.findViewById(R.id.txt_title);
		TextView txt_gold = (TextView) convertView.findViewById(R.id.txt_gold);
	//	txt_gold.setText("("+getItem(position).total + "金币)");
		txt_title.setText(getItem(position) + "元");
		
		convertView.setTag(position);
//		if(getItem(position).recharge>10){
//			convertView.setEnabled(false);
//			convertView.setBackgroundResource(R.drawable.btn_unpay_down);
//		}
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.onCallListener(v.getTag().toString());
			}
		});
		if(position==0){
			convertView.setBackgroundResource(R.drawable.btn_pay_down);
		}
		return convertView;
	}

}
