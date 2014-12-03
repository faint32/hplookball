package com.hupu.games.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hupu.games.HuPuApp;
import com.hupu.games.R;
import com.hupu.games.data.LiveEntity;
import com.hupu.games.data.TeamValueEntity;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class LiveDataListAdapter extends BaseAdapter {

	private ArrayList<LiveEntity> mLiveList;

	private LayoutInflater mInflater;

	// private String strHomeName;
	// private String strAwayName;
	private int i_homeColor;
	private int i_awayColor;

	private int i_bg_gray;
	private int i_txt_gray;

	OnClickListener click;

	

	public LiveDataListAdapter(Context context, int homeId, int awayId,
			OnClickListener c) {
		mInflater = LayoutInflater.from(context);
		TeamValueEntity entity = HuPuApp.getTeamData(homeId);
		// strHomeName = entity.str_name;
		i_homeColor = entity.i_color;
		entity = HuPuApp.getTeamData(awayId);
		// strAwayName =entity.str_name;
		i_awayColor = entity.i_color;
		// System.out.println("strHomeName="+strHomeName
		// +"strAwayName="+strAwayName);
		Resources res = context.getResources();
		i_bg_gray = res.getColor(R.color.res_cor6);
		i_txt_gray = res.getColor(R.color.txt_status);
		
		click = c;

	}


	@Override
	public int getCount() {
		if (mLiveList == null)
			return 0;
		return mLiveList.size();
	}

	@Override
	public LiveEntity getItem(int arg0) {
		if (mLiveList == null)
			return null;
		return mLiveList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	public void setData(ArrayList<LiveEntity> gameList) {
		mLiveList = gameList;
		notifyDataSetChanged();
	}




	public ArrayList<LiveEntity> appendData(ArrayList<LiveEntity> gameList) {
		if (mLiveList == null)
			mLiveList = new ArrayList<LiveEntity>();
		mLiveList.addAll(gameList);	
		notifyDataSetChanged();		
		return mLiveList;
	}

	public ArrayList<LiveEntity> addDataToHead(ArrayList<LiveEntity> gameList) {
		if (mLiveList == null)
			mLiveList = new ArrayList<LiveEntity>();
		mLiveList.addAll(0, gameList);
		notifyDataSetChanged();		
		return mLiveList;
	}

//	boolean bShowRate;
	/**设置是否显示赔率*/
	public void setShowRate(boolean b)
	{
//		bShowRate =b;
	} 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// System.out.print("GamesListAdapter getView pos =" + position);
		LiveEntity entity = mLiveList.get(position);
		Holder holder = null;
		if (convertView == null) {
			//
			convertView = mInflater.inflate(R.layout.item_live_msg, null);
			holder = new Holder();
			holder.txtTeamName = (TextView) convertView
					.findViewById(R.id.txt_team);
			holder.txtTime = (TextView) convertView.findViewById(R.id.txt_time);
			holder.txtEvent = (TextView) convertView
					.findViewById(R.id.txt_event);
			holder.split = convertView.findViewById(R.id.view_split);
			holder.img = (ImageView) convertView.findViewById(R.id.img_live);

			holder.casino = convertView.findViewById(R.id.layout_casino);
			holder.btn1 = (Button) convertView.findViewById(R.id.btn_1);
			holder.btn2 = (Button) convertView.findViewById(R.id.btn_2);

			holder.odds = convertView.findViewById(R.id.layout_odds);
			holder.txtOdds1 = (TextView) convertView
					.findViewById(R.id.txt_odds1);
			holder.txtOdds2 = (TextView) convertView
					.findViewById(R.id.txt_odds2);

			holder.Info = convertView.findViewById(R.id.layout_casino_info);
//			holder.txtPeople = (TextView) convertView
//					.findViewById(R.id.txt_people);
			holder.txtStatus = (TextView) convertView
					.findViewById(R.id.txt_status);
			holder.txtAnswer= (TextView) convertView
					.findViewById(R.id.txt_answer);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.txtEvent.setTextColor(Color.WHITE);
		holder.txtAnswer.setVisibility(View.GONE);
		if (entity.byt_team == 0) {
			holder.txtTeamName.setVisibility(View.GONE);
			holder.split.setBackgroundColor(0xff666666);
//			holder.txtTime.setBackgroundColor(i_bg_gray);

			// holder.txtEvent.setTextColor(i_txt_gray);

			if (entity.type == 1) {
				// 问答题
				holder.casino.setVisibility(View.VISIBLE);
				holder.Info.setVisibility(View.VISIBLE);
				if(entity.is_show_odds && entity.answers[0].odds!=null)
				{
					//需要显示赔率
					holder.odds.setVisibility(View.VISIBLE);
					holder.txtOdds1.setText("1赔" + entity.answers[0].odds);
					holder.txtOdds2.setText("1赔" + entity.answers[1].odds);
				}
				else
					holder.odds.setVisibility(View.GONE);//隐藏赔率行
				
				holder.btn1.setText(entity.answers[0].title);
				holder.btn1.setOnClickListener(click);
				holder.btn1.setTag(entity.answers[0]);
				holder.btn2.setText(entity.answers[1].title);
				holder.btn2.setOnClickListener(click);
				holder.btn2.setTag(entity.answers[1]);				
				if (entity.quizStatus == 1) {
					// 竞猜进行中
					holder.btn1.setBackgroundResource(R.drawable.quiz_btn_selector);
					holder.btn2.setBackgroundResource(R.drawable.quiz_btn_selector);
					holder.btn1.setVisibility(View.VISIBLE);
					holder.btn2.setVisibility(View.VISIBLE);
					if(entity.isCasino>0)
					{
						//已经参加过竞猜，只需显示右侧按钮						
						holder.btn1.setVisibility(View.INVISIBLE);
						holder.txtOdds1.setVisibility(View.INVISIBLE);
						holder.btn2.setText("追加投入");
						if(entity.isCasino ==1)
							holder.btn2.setTag(entity.answers[0]);
					}
					else
						holder.txtOdds1.setVisibility(View.VISIBLE);
//					holder.txtPeople.setVisibility(View.INVISIBLE);
					holder.txtStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_live_man
							, 0, 0, 0);
					holder.txtStatus.setText("" + entity.userCount+"人已参与");
				} else {
					// 关闭或结束时不显示竞猜人数和竞猜按钮
//					holder.txtPeople.setVisibility(View.INVISIBLE);
					holder.odds.setVisibility(View.GONE);
					holder.casino.setVisibility(View.GONE);
					if(entity.quizStatus ==3 && entity.rightId>0)
					{
						//已开奖需要显示竞猜答案
						holder.txtAnswer.setVisibility(View.VISIBLE);
						holder.txtAnswer.setText("开奖："+entity.answers[entity.rightId-1].title);
					}
					holder.txtStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_live_time
							, 0, 0, 0);
					holder.txtStatus.setText(entity.quizStr);
				}
				
				if(entity.isCasino>0 && ( entity.quizStatus !=3 && entity.quizStatus !=6 ))
				{   //未开奖时只要用户参与过，显示用户自己选择的答案
					holder.txtAnswer.setVisibility(View.VISIBLE);	
					if(entity.isCasino ==1)
						holder.txtAnswer.setText("我猜："+entity.answers[0].title);
					else if(entity.isCasino ==2)
						holder.txtAnswer.setText("我猜："+entity.answers[1].title);
				}
				
			} else {
				holder.casino.setVisibility(View.GONE);
				holder.Info.setVisibility(View.GONE);
				holder.odds.setVisibility(View.GONE);
			}
		} else {
			holder.casino.setVisibility(View.GONE);
			holder.Info.setVisibility(View.GONE);
			holder.odds.setVisibility(View.GONE);
			holder.txtTeamName.setVisibility(View.VISIBLE);
			if (entity.byt_team == 1) {
				// holder.txtTeamName.setText(strHomeName);
				holder.txtTeamName.setText("主");
				holder.split.setBackgroundColor(i_homeColor);
			} else {
				// holder.txtTeamName.setText(strAwayName);
				holder.txtTeamName.setText("客");
				holder.split.setBackgroundColor(i_awayColor);
			}
			//holder.txtTime.setBackgroundColor(Color.BLACK);
		}

		holder.txtTime.setText(entity.i_endTime);
		// 变化文字颜色
		if (entity.i_color != -1)
			holder.txtEvent.setTextColor(entity.i_color);
		// Log.d("str_img_thumb", "img url="+entity.str_img_thumb);
		// 显示图片
		if (entity.str_img_thumb != null) {
			holder.img.setVisibility(View.VISIBLE);
			UrlImageViewHelper.setUrlDrawable(holder.img, entity.str_img_thumb,R.drawable.live_default);
			holder.img.setClickable(true);
			holder.img.setTag(entity.str_img);
			holder.img.setOnClickListener(click);
		} else
			holder.img.setVisibility(View.GONE);
		// 文字跳转
		if (entity.type == 1) {
			//竞猜
			holder.txtEvent.setTextColor(0xfff9ff50);
			if(entity.max_bet>0)

				holder.txtEvent.setText(entity.content+"(最多投入"+entity.max_bet+"金豆)");

			else
				holder.txtEvent.setText(entity.content);
			holder.txtEvent.setClickable(false);
		} else {
			holder.txtEvent.setClickable(true);
			holder.txtEvent.setOnClickListener(click);
			holder.txtEvent.setTag(entity.str_link);
			holder.txtEvent.setText(entity.str_event);
		}

		return convertView;
	}

	static class Holder {
		// 第一列
		TextView txtTeamName;
		TextView txtTime;
		TextView txtEvent;
		TextView txtAnswer;
	
		View split;
		ImageView img;

		View casino;
		Button btn1;//下注按钮
		Button btn2;

		View odds;
		TextView txtOdds1;//赔率
		TextView txtOdds2;

		View Info;//竞猜相关信息
//		TextView txtPeople;
		TextView txtStatus;
	}

}
