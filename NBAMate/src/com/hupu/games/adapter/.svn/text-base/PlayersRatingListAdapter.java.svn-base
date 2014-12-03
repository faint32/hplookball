package com.hupu.games.adapter;

import java.util.LinkedList;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.data.PlayerRatingEntity;
import com.hupu.games.data.VideoEntity;
import com.hupu.games.data.news.NewsEntity;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.pyj.adapter.BaseListAdapter;

/**
 * 所有球员，裁判，教练，球队的打分列表
 * */
public class PlayersRatingListAdapter extends
		BaseListAdapter<PlayerRatingEntity> {


	int clrScore;
	int clrNum;
	OnClickListener mClick;

	public PlayersRatingListAdapter(Context context, OnClickListener click) {
		super(context);
		mClick = click;
		clrScore = context.getResources().getColor(R.color.txt_player_rate);
		clrNum = context.getResources().getColor(R.color.txt_player_name);
	
	}


	class Holder {
		/** 球员描述 */
		TextView txtDesc;
		/** 球员名字 */
		TextView txtName;
		/** 打分人数 */
		TextView txtNum;
		/** 分数 */
		TextView txtScore;

		TextView txtMemo;

		TextView btnMyRating;
		
		LinearLayout btnRateLayout;

		ImageView imgHeader;
	}

	float fontSize;
	float bigFontSize;

	@Override
	public View getView(int pos, View contentView, ViewGroup arg2) {
		Holder item = null;
		PlayerRatingEntity entity = mListData.get(pos);
		if (contentView == null) {
			contentView = mInflater.inflate(R.layout.item_player_rating_txt,
					null);
			item = new Holder();
			item.txtDesc = (TextView) contentView
					.findViewById(R.id.txt_player_discription);
			item.txtName = (TextView) contentView
					.findViewById(R.id.txt_player_name);
			item.txtNum = (TextView) contentView
					.findViewById(R.id.txt_rating_num);
			item.txtScore = (TextView) contentView
					.findViewById(R.id.txt_player_score);
			item.imgHeader = (ImageView) contentView
					.findViewById(R.id.img_header);
			item.btnMyRating = (TextView) contentView
					.findViewById(R.id.btn_rate);
			item.btnRateLayout = (LinearLayout) contentView
					.findViewById(R.id.btn_rate_layout);

			fontSize = item.btnMyRating.getTextSize();
			bigFontSize = (float) (fontSize *1.1);

			item.txtMemo = (TextView) contentView
					.findViewById(R.id.txt_player_memo);
			contentView.setTag(item);
		} else {
			item = (Holder) contentView.getTag();
		}
		item.txtDesc.setText(entity.content);

		item.txtName.setText(entity.name);

		if (entity.memo.equals(""))
			item.txtMemo.setText("");
		else
			item.txtMemo.setText("(" + entity.memo + ")");
		if (entity.user_num > 0) {
			// item.txtScore.setTextColor(clrScore);

			item.txtScore.setText(entity.ratings + "分");
			item.txtNum.setText("/" + entity.user_num + "人评价");
		} else {
			// item.txtScore.setTextColor(clrNum);
			item.txtScore.setText("");
			item.txtNum.setText(R.string.no_rating);

		}

		item.btnMyRating.setTag(pos);
		item.btnRateLayout.setTag(pos);
		if (entity.my_rating == 0) {
			item.btnMyRating.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
			item.btnMyRating.setText(R.string.STR_RATE);
			item.btnMyRating.setOnClickListener(mClick);
			item.btnRateLayout.setOnClickListener(mClick);
			item.btnMyRating.setTextColor(0xffffffff);
			item.btnMyRating
					.setBackgroundResource(R.drawable.btn_red1_selector);
			//item.btnMyRating.setGravity(Gravity.CENTER);
		} else {
		
			item.btnMyRating.setTextSize(TypedValue.COMPLEX_UNIT_PX,bigFontSize);
			item.btnMyRating.setText("我的评分：" + entity.my_rating);
			item.btnMyRating.setOnClickListener(null);
			item.btnMyRating.setTextColor(0xff8b8b8b);
			item.btnMyRating.setBackgroundDrawable(null);
			item.btnMyRating.setGravity(Gravity.BOTTOM);
			//item.btnMyRating.setPadding(0, 0, 20, 0);
			// item.btnMyRating.setBackgroundColor(0x00000001);
		}

		// 头像的处理
		if (entity.header_img.equals("-1") || entity.header_img.equals("-3")) {
			// 无头像球员或教练
			item.imgHeader.setImageResource(R.drawable.no_photo);
		} else if (entity.header_img.equals("-2")) {
			// 无头像裁判
			item.imgHeader.setImageResource(R.drawable.no_photo);
		} else
			UrlImageViewHelper.setUrlDrawable(item.imgHeader, entity.header_img,R.drawable.no_photo);

		return contentView;
	}

}
