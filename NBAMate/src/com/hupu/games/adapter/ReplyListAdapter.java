package com.hupu.games.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import com.hupu.games.R;
import com.hupu.games.data.ReplyRespEntity;
import com.hupu.games.data.news.NewsLightEntity;
import com.hupu.games.util.TimeUtile;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReplyListAdapter extends XSectionedBaseAdapter {

	private LinkedList<LinkedList<NewsLightEntity>> ReplyList;

	private LayoutInflater mInflater;

	OnClickListener mClick;
	private SimpleDateFormat format;
	private Animation myAnimation_Translate;
	private boolean isAnimation = false;
	private View animView;

	long replyId = 0;
	int replySection;
	long lightId = 0;
	int lType = 0;

	public ReplyListAdapter(Context context, OnClickListener click) {
		mClick = click;
		mInflater = LayoutInflater.from(context);
		format = new SimpleDateFormat("MM-dd HH:mm");
		
		startAnim();
	}

	public void setData(ReplyRespEntity ReplyResplist) {
		ReplyList = new LinkedList<LinkedList<NewsLightEntity>>();
		if (ReplyResplist.lightList != null
				&& ReplyResplist.lightList.size() > 0) {
			ReplyList.add(ReplyResplist.lightList);
		}

		if (ReplyResplist.replyList != null) {
			ReplyList.add(ReplyResplist.replyList);
		}

		if (ReplyResplist.lightList == null && ReplyResplist.replyList == null) {
			ReplyList = null;
		}
		replyId = 0;
	}

	public void setReplyId(int section,long ncid) {
		replySection = section;
		replyId = ncid;
	}
	public void startAnim(){
		myAnimation_Translate = new TranslateAnimation(0, 0, 0,
				-100);
		myAnimation_Translate.setDuration(1200);
		myAnimation_Translate
				.setAnimationListener(animationListener);
		isAnimation = true;
	}

	
	private AnimationListener animationListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			animView.setVisibility(View.INVISIBLE);
			replyId = 0;
			notifyDataSetChanged();
		}
	};

	/**
	 * //点亮时自动 亮的数量+1
	 * 
	 * @param ncid
	 */
	public void addLightNum(long ncid,int lightType) {
		lightId = ncid;
		lType = lightType;
	}

	public int getSection(int pos) {
		return this.getSectionForPosition(pos);
	}

	public int getChildPosition(int pos) {

		return getPositionInSectionForPosition(pos);

	}
	
	
	@Override
	public Object getItem(int section, int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int section, int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSectionCount() {
		// TODO Auto-generated method stub
		if (ReplyList != null)
			return ReplyList.size();

		return 0;
	}

	@Override
	public int getCountForSection(int section) {
		// TODO Auto-generated method stub
		if (ReplyList != null)
			return ReplyList.get(section).size();
		return 0;
	}

	@Override
	public View getItemView(int section, int pos, View convertView,
			ViewGroup parent) {
		int position = pos;
		// TODO Auto-generated method stub
		Reply reply = null;

		reply = new Reply();
		convertView = initReply(reply);
		reply.author.setText(ReplyList.get(section).get(position).user_name);
		
		//点亮时自动+1
		if (lightId == ReplyList.get(section).get(position).ncid && isAnimation) {
			animView = lType == 2?reply.addUnLightNum:reply.addLightNum;
			animView.setVisibility(View.VISIBLE);
			if (lType == 2) {
				reply.addUnLightNum.setAnimation(myAnimation_Translate);
				reply.addUnLightNum.startAnimation(myAnimation_Translate);
			}else {
				reply.addLightNum.setAnimation(myAnimation_Translate);
				reply.addLightNum.startAnimation(myAnimation_Translate);
			}
			
			
			isAnimation = false;
		}
		int lightNum = 0;//计算后的点亮数；
		if (lightId == ReplyList.get(section).get(position).ncid) {
			if (lType == 1) {
				
				ReplyList.get(section).get(position).light_count =Integer.parseInt(ReplyList.get(section).get(position).light_count) + 1 + "";
			}
			else if (lType == 2) {
				ReplyList.get(section).get(position).unlight_count =Integer.parseInt(ReplyList.get(section).get(position).unlight_count) + 1 + "";
			}
			lightNum =  Integer.parseInt(ReplyList.get(section).get(position).light_count) - Integer.parseInt(ReplyList.get(section).get(position).unlight_count);
			
			ReplyList.get(section).get(position).lighted = lType;
			lightId = 0;
			lType = 0;
		}else {
			lightNum =  Integer.parseInt(ReplyList.get(section).get(position).light_count) - Integer.parseInt(ReplyList.get(section).get(position).unlight_count);
		}
		reply.lightNum.setText("亮了(" + lightNum + ")");
		
		//-------------------------------------------------------------------------------逻辑结束
		
		reply.beforeTime
				.setText(ReplyList.get(section).get(position).create_time
						.equals("") ? "刚刚" : TimeUtile.getReplytime(format
						.format(new Date((Long.parseLong(ReplyList.get(section)
								.get(position).create_time) * 1000L)))));
		reply.replyContent
				.setText(ReplyList.get(section).get(position).content);
		if (ReplyList.get(section).get(position).ncid == replyId && section == replySection) {
			reply.popLayout.setVisibility(View.VISIBLE);
		} else {
			reply.popLayout.setVisibility(View.GONE);
		}
		
		reply.likeNum.setText(ReplyList.get(section).get(position).light_count);
		reply.unLightNum.setText(ReplyList.get(section).get(position).unlight_count);
		
		reply.replyItem.setTag(section+","+ReplyList.get(section).get(position).ncid);
		reply.replyItem.setOnClickListener(mClick);

		reply.quote.setTag(ReplyList.get(section).get(position).user_name);
		reply.quote.setOnClickListener(mClick);

		reply.light.setTag(ReplyList.get(section).get(position).ncid+","+ReplyList.get(section).get(position).lighted);
		reply.light.setOnClickListener(mClick);
		reply.light.setBackgroundResource(ReplyList.get(section).get(position).lighted == 1?R.drawable.btn_light_down:R.drawable.btn_light_selector);
		reply.likeNum.setTag(ReplyList.get(section).get(position).ncid+","+ReplyList.get(section).get(position).lighted);
		reply.likeNum.setOnClickListener(mClick);
		reply.likeNum.setTextColor(ReplyList.get(section).get(position).lighted == 1?Color.parseColor("#ba0000"):Color.WHITE);
		reply.unLight.setTag(ReplyList.get(section).get(position).ncid+","+ReplyList.get(section).get(position).lighted);
		reply.unLight.setOnClickListener(mClick);
		reply.unLight.setBackgroundResource(ReplyList.get(section).get(position).lighted == 2?R.drawable.btn_unlight_down:R.drawable.btn_unlight_selector);
		reply.unLightNum.setTag(ReplyList.get(section).get(position).ncid+","+ReplyList.get(section).get(position).lighted);
		reply.unLightNum.setOnClickListener(mClick);
		reply.unLightNum.setTextColor(ReplyList.get(section).get(position).lighted == 2?Color.parseColor("#ba0000"):Color.WHITE);
		return convertView;
	} 

	@Override
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		Header header = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_reply_group, null);
			header = new Header();
			header.groupImg = (ImageView) convertView
					.findViewById(R.id.reply_img);
			header.name = (TextView) convertView
					.findViewById(R.id.reply_light_text);
			convertView.setTag(header);
		} else
			header = (Header) convertView.getTag();
		if (ReplyList.size() == 2) {
			if (section == 0) {
				header.groupImg.setImageResource(R.drawable.icon_light_down);
				header.name.setText("这些评论亮了");
			} else {
				header.groupImg.setImageResource(R.drawable.icon_new_reply);
				header.name.setText("最新评论");
			}
		} else {
			header.groupImg.setImageResource(R.drawable.icon_new_reply);
			header.name.setText("最新评论");
		}
		return convertView;
	}

	private View initReply(Reply reply) {
		View v = mInflater.inflate(R.layout.item_hot_reply, null);
		reply.replyItem = (RelativeLayout) v.findViewById(R.id.reply_item);
		reply.author = (TextView) v.findViewById(R.id.comment_author);
		
		reply.author.getPaint().setFakeBoldText(true);
		reply.lightNum = (TextView) v.findViewById(R.id.light_num);
		reply.beforeTime = (TextView) v.findViewById(R.id.before_time);
		reply.replyContent = (TextView) v.findViewById(R.id.reply_content);

		reply.popLayout = (LinearLayout) v.findViewById(R.id.pop_layout);
		reply.quote = (ImageButton) v.findViewById(R.id.to_quote);
		reply.light = (ImageButton) v.findViewById(R.id.to_light);
		reply.addLightNum = (TextView) v.findViewById(R.id.add_light_num);
		reply.unLight = (ImageButton) v.findViewById(R.id.to_unlight);
		reply.addUnLightNum = (TextView) v.findViewById(R.id.add_unlight_num);

		reply.likeNum = (TextView) v.findViewById(R.id.like_num);
		reply.unLightNum = (TextView) v.findViewById(R.id.uplight_num);
		v.setTag(reply);
		return v;
	}

	class Reply {
		RelativeLayout replyItem;
		TextView author;
		TextView lightNum;
		TextView beforeTime;
		TextView replyContent;

		LinearLayout popLayout;
		ImageButton quote;
		ImageButton light;
		TextView addLightNum;
		
		ImageButton unLight;
		TextView addUnLightNum;
		
		TextView likeNum;
		TextView unLightNum;
	}

	class Header {
		ImageView groupImg;
		TextView name;
	}

}
