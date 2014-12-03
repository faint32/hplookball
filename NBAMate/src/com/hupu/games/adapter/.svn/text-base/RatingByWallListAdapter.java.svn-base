package com.hupu.games.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.data.BulletinRankReq.BulletinRankEntity;
import com.pyj.adapter.BaseListAdapter;

/**
 * 用户给球员，裁判，教练，球队的打分的列表
 * */
public class RatingByWallListAdapter extends BaseListAdapter<BulletinRankEntity> {


	OnClickListener mClick;
	int clrLiked;
	
	public RatingByWallListAdapter(Context context,OnClickListener click) {
		super(context);
		mClick =click;
		clrLiked =context.getResources().getColor(R.color.txt_player_rate);
		
		drawablespan = context.getResources().getDrawable(R.drawable.icon_vip1);
		drawablespan.setBounds(0, 0, drawablespan.getIntrinsicWidth(), drawablespan.getIntrinsicHeight());
		span = new ImageSpan(drawablespan,ImageSpan.ALIGN_BASELINE); 
	}

	public int lastSoid;	
	@Override
	public void setData(ArrayList<BulletinRankEntity> data) {
		super.setData(data);
	
		if(data!=null)
		{
			mListData = data;
		}
	}


	class Holder {

		
		TextView rankNum;
		/** 用户名字 */
		TextView txtName;
		
		TextView coinNum;
		/** 喜欢 */
		TextView likeNum;
		/** 分数 */
		TextView content;

		TextView hatedContent;

	}

	// 图文混排
		ImageSpan span;
		SpannableString spannable;
		Drawable drawablespan;
		CharSequence charSequence;
		
	@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListData.size();
		}

	@Override
	public View getView(int pos, View contentView, ViewGroup arg2) {
		Holder item = null;
		BulletinRankEntity entity = mListData.get(pos);
		if (contentView == null) {
			contentView = mInflater.inflate(R.layout.item_wall_rate,
					null);
			item = new Holder();
	
			item.rankNum = (TextView) contentView.findViewById(R.id.txt_rating_num);
			
			item.txtName = (TextView) contentView
					.findViewById(R.id.txt_rating_name);
			
			item.coinNum = (TextView) contentView
					.findViewById(R.id.txt_rating_coin);
			item.likeNum = (TextView) contentView
					.findViewById(R.id.txt_like_num);
			item.content = (TextView) contentView.findViewById(R.id.rank_content);
			
			item.hatedContent = (TextView) contentView.findViewById(R.id.hated_text);

			contentView.setTag(item);
		} else {
			item = (Holder) contentView.getTag();
		}
		
		item.rankNum.setText(pos + 1 + ".");
		String nickname = entity.nickname;
		String hatedName = "";
		boolean isVip = false;
		
		if (entity.nickname.contains("[vip]")) {
			nickname = nickname.substring(0, nickname.indexOf("[vip]")) +  ".";
			isVip = true;
		}
		nickname = "@"+nickname;
		charSequence = nickname;
		spannable = new SpannableString(charSequence);
		if (isVip) {
			spannable.setSpan(span, nickname.length() - 1,nickname.length() , Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			//spannable.setSpan(span, 0,1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		}
		
		item.txtName.setText(spannable);
		item.coinNum.setText("("+entity.coin+"金豆)");
		item.likeNum.setText("赞 "+entity.like);

		item.content.setText(entity.content);
		if (entity.isHated == 1) {
			item.hatedContent.setVisibility(View.VISIBLE);
			if (entity.hated_nickname.contains("[vip]")) {
				hatedName = "@"+ entity.hated_nickname.substring(0, entity.hated_nickname.indexOf("[vip]"))+" 拆除了这条标语";
				charSequence = hatedName;
				spannable = new SpannableString(charSequence);
				spannable.setSpan(span, entity.hated_nickname.substring(0, entity.hated_nickname.indexOf("[vip]")).length() + 1 ,entity.hated_nickname.substring(0, entity.hated_nickname.indexOf("[vip]")).length() + 2 , Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				item.hatedContent.setText(spannable);
			}else {
				item.hatedContent.setText("@"+ entity.hated_nickname+" 拆除了这条标语");
				
			}
			
			
		}else {
			item.hatedContent.setVisibility(View.GONE);
		}
		
		
		return contentView;
	}

}
