package com.hupu.games.adapter;

import java.util.LinkedList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.data.HonourEntity;
import com.hupu.games.data.MyHonourEntity;
import com.hupu.games.data.SingleHonourEntity;

/**
 * 比赛统计数据
 * */
public class SingleHonourListAdapter extends BaseAdapter {

	private LinkedList<SingleHonourEntity> mListData;

	private LayoutInflater mInflater;

	private Context mcontext;

	public SingleHonourListAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		mcontext = context;
	}

	public void setData(LinkedList<SingleHonourEntity> data) {
//		if (mListData != null)
//			mListData.clear();
		mListData = data;
		// Log.d("VideoListAdapter", "total size ="+mListData.size());
		notifyDataSetChanged();
	}

	class Holder {
		TextView name;
		TextView nick;
		TextView number;
	}

	@Override
	public SingleHonourEntity getItem(int position) {
		if (mListData == null)
			return null;
		return mListData.get(position);
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	public int getCount() {
		if (mListData == null)
			return 0;
		return mListData.size();
	}

	@Override
	public View getView(int pos, View contentView, ViewGroup arg2) {
		Holder item = null;
		SingleHonourEntity entity = mListData.get(pos);
		if (contentView == null) {
			contentView = mInflater.inflate(R.layout.item_list_honor, null);
			item = new Holder();
			item.name = (TextView) contentView
					.findViewById(R.id.txt_sinhonour_name);
			item.nick = (TextView) contentView
					.findViewById(R.id.txt_sinhonour_nick);
			item.number = (TextView) contentView
					.findViewById(R.id.txt_sinhonour_num);
			contentView.setTag(item);
		} else {
			item = (Holder) contentView.getTag();
		}
		if ((entity.rank < 10 && entity.is_my == 1) || (entity.rank > 10)) {
			item.name.setTextColor(mcontext.getResources()
					.getColor(R.color.res_cor3));
			item.nick.setTextColor(mcontext.getResources()
					.getColor(R.color.res_cor3));
			item.number.setTextColor(mcontext.getResources().getColor(
					R.color.res_cor3));
		} else {
			item.name.setTextColor(mcontext.getResources().getColor(
					R.color.res_cor1));
			item.nick.setTextColor(mcontext.getResources().getColor(
					R.color.res_cor1));
			item.number.setTextColor(mcontext.getResources().getColor(
					R.color.res_cor1));
		}
		item.name.setText(Integer.toString(mListData.get(pos).rank));
		item.nick.setText(mListData.get(pos).nick_name);
		item.number.setText(Integer.toString(mListData.get(pos).score));
		return contentView;
	}
}
