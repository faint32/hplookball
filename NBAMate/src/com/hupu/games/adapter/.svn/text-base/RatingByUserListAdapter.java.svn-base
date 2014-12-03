package com.hupu.games.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.data.PlayerRatingByUserEntity;
import com.pyj.adapter.BaseListAdapter;

/**
 * 用户给球员，裁判，教练，球队的打分的列表
 * */
public class RatingByUserListAdapter extends BaseListAdapter<PlayerRatingByUserEntity> {


	private ArrayList<String> mCOIDList;
	OnClickListener mClick;
	int clrLiked;
	public RatingByUserListAdapter(Context context,OnClickListener click) {
		super(context);
		mCOIDList =new ArrayList<String>();
		mClick =click;
		clrLiked =context.getResources().getColor(R.color.txt_player_rate);
	}

	public int lastSoid;	
	@Override
	public void setData(ArrayList<PlayerRatingByUserEntity> data) {
		super.setData(data);
	
		if(data!=null)
		{
			mCOIDList.clear();
			for(PlayerRatingByUserEntity entity:data)
			{
				mCOIDList.add(""+entity.coid);
				lastSoid =entity.coid;
			}
		}
	}

	public void appendData(ArrayList<PlayerRatingByUserEntity> data) {
		if(mListData!=null)
			mListData.addAll(data);	
		if(data!=null)
		{
			for(PlayerRatingByUserEntity entity:data)
			{
				if(mCOIDList.indexOf(""+entity.coid)==-1)
				{
					mCOIDList.add(""+entity.coid);
					lastSoid =entity.coid;
				}
				
			}
		}
	}
	
	public int getIndex(int coid)
	{
		return mCOIDList.indexOf(""+coid);
	}

	class Holder {

		/** 用户名字 */
		TextView txtName;
		/** 喜欢 */
		Button btnLike;
		/** 分数 */
		TextView txtScore;

		TextView txtDesc;

	}

	@Override
	public View getView(int pos, View contentView, ViewGroup arg2) {
		Holder item = null;
		PlayerRatingByUserEntity entity = mListData.get(pos);
		if (contentView == null) {
			contentView = mInflater.inflate(R.layout.item_player_rate,
					null);
			item = new Holder();
	
			item.txtName = (TextView) contentView
					.findViewById(R.id.txt_rating_name);
			item.btnLike = (Button) contentView
					.findViewById(R.id.btn_like);
			item.txtScore = (TextView) contentView
					.findViewById(R.id.txt_rating_score);
			item.txtDesc = (TextView) contentView.findViewById(R.id.txt_rating_desc);

			contentView.setTag(item);
		} else {
			item = (Holder) contentView.getTag();
		}
		item.txtDesc.setText(entity.desc);
		item.txtName.setText(entity.nickname);
		item.btnLike.setTag(pos);
		item.txtScore.setText(entity.rating+"分");
		item.btnLike.setOnClickListener(mClick);
		if(entity.liked)
		{
			item.btnLike.setTextColor(clrLiked);
			item.btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_like_selector, 0, 0, 0);
		}
		else
		{
			item.btnLike.setTextColor(Color.WHITE);
			item.btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_good_up, 0, 0, 0);
		}
		
		if(entity.like <=0)
		{
			item.btnLike.setText("+1");
		}
		else
		{
			item.btnLike.setText(""+entity.like);
		}
		
		return contentView;
	}
	
}
