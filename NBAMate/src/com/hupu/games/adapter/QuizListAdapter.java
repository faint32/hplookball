package com.hupu.games.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.QuizListActivity;
import com.hupu.games.data.game.quiz.QuizEntity;
import com.pyj.adapter.BaseListAdapter;

/**
 * 竞猜列表适配器
 * */
public class QuizListAdapter extends BaseListAdapter<QuizEntity> {


	OnClickListener mClick;
	int clrLiked;
	Context mContext;
	QuizListActivity quizAct;
	
	public QuizListAdapter(Context context,OnClickListener click) {
		super(context);
		mContext = context;
		mClick =click;
		clrLiked =context.getResources().getColor(R.color.txt_player_rate);
		
		drawablespan = context.getResources().getDrawable(R.drawable.icon_vip1);
		drawablespan.setBounds(0, 0, drawablespan.getIntrinsicWidth(), drawablespan.getIntrinsicHeight());
		span = new ImageSpan(drawablespan,ImageSpan.ALIGN_BASELINE); 
		quizAct = new QuizListActivity();
	}

	public int lastSoid;	
	
	@Override
	public void setData(ArrayList<QuizEntity> data) {
		super.setData(data);
	
		if(data!=null)
		{
			mListData = data;
		}
	}


	class Holder {
		
		Button answer1;
		Button answer2;
		
		TextView textContent;
		TextView quizResult;
		
		TextView coinNum;

		ImageView imgGold;
		TextView quizState;
		
		//宝箱 待开发

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
		QuizEntity entity = mListData.get(pos);
		if (contentView == null) {
			contentView = mInflater.inflate(R.layout.item_quiz,
					null);
			item = new Holder();
			
			item.answer1 = (Button) contentView.findViewById(R.id.btn_answer1);
			item.answer2 = (Button) contentView.findViewById(R.id.btn_answer2);
	
			item.textContent = (TextView) contentView.findViewById(R.id.txt_content);
			
			item.quizResult = (TextView) contentView
					.findViewById(R.id.quiz_result);
			
			item.coinNum = (TextView) contentView
					.findViewById(R.id.coin_num);
			item.imgGold = (ImageView) contentView
					.findViewById(R.id.img_gold);
			item.quizState = (TextView) contentView.findViewById(R.id.quiz_state);

			contentView.setTag(item);
		} else {
			item = (Holder) contentView.getTag();
		}
		
		item.textContent.setText(entity.content);
		((LinearLayout)contentView.findViewById(R.id.open_result)).setBackgroundResource(R.drawable.quiz_win_label);
//		switch (entity.is_end) {
//		case 0:
//
//			//用户参与了为开奖
//			item.coinNum.setVisibility(View.GONE);
//			item.imgGold.setVisibility(View.GONE);
//			if (entity.is_user_join == 1) {
//				item.quizResult.setText(mContext.getString(R.string.my_quiz_result)+entity.user_answer);
//				item.quizState.setVisibility(View.VISIBLE);
//				((RelativeLayout)contentView.findViewById(R.id.user_join)).setVisibility(View.VISIBLE);
//				((RelativeLayout)contentView.findViewById(R.id.no_join)).setVisibility(View.GONE);
//				item.quizState.setText(mContext.getString(R.string.wait_open));
//				
//			}else {
//				item.quizResult.setVisibility(View.GONE);
//				item.imgGold.setVisibility(View.GONE);
//				((RelativeLayout)contentView.findViewById(R.id.user_join)).setVisibility(View.GONE);
//				((RelativeLayout)contentView.findViewById(R.id.no_join)).setVisibility(View.VISIBLE);
//				
//				Log.e("papa", "答案1=="+pos);
//				item.answer1.setText(entity.answer1);
//				item.answer2.setText(entity.answer2);
//				Answer answer1 = new Answer();
//				answer1.answer_id = 1;
//				answer1.casino_id = entity.qid;
//				answer1.content = entity.content;
//				answer1.title = entity.answer1;
//				
//				item.answer1.setTag(answer1);
//				item.answer1.setOnClickListener(mClick);
//				
//				Answer answer2 = new Answer();
//				answer2.answer_id = 2;
//				answer2.casino_id = entity.qid;
//				answer2.content = entity.content;
//				answer2.title = entity.answer2;
//				
//				item.answer2.setTag(answer2);
//				item.answer2.setOnClickListener(mClick);
//			}
//			break;
//		case 1:
//			item.coinNum.setVisibility(View.GONE);
//			item.imgGold.setVisibility(View.GONE);
//			((RelativeLayout)contentView.findViewById(R.id.no_join)).setVisibility(View.GONE);
//			if (entity.is_user_join == 1) {
//				item.quizResult.setText(mContext.getString(R.string.my_quiz_result)+entity.user_answer);
//				item.quizState.setVisibility(View.VISIBLE);
//				((RelativeLayout)contentView.findViewById(R.id.user_join)).setVisibility(View.VISIBLE);
//				((RelativeLayout)contentView.findViewById(R.id.no_join)).setVisibility(View.GONE);
//				item.quizState.setText(mContext.getString(R.string.wait_open));
//				
//			}else {
//				item.quizResult.setText("");
//				item.quizState.setVisibility(View.VISIBLE);
//				((RelativeLayout)contentView.findViewById(R.id.user_join)).setVisibility(View.VISIBLE);
//				((RelativeLayout)contentView.findViewById(R.id.no_join)).setVisibility(View.GONE);
//				item.quizState.setText(mContext.getString(R.string.wait_open));
//			}
//			break;
//		case 2:
//			//开奖结束用户参加
//			((RelativeLayout)contentView.findViewById(R.id.no_join)).setVisibility(View.GONE);
//			((RelativeLayout)contentView.findViewById(R.id.user_join)).setVisibility(View.VISIBLE);
//			item.quizState.setVisibility(View.GONE);
//			item.quizResult.setVisibility(View.VISIBLE);
//			item.quizResult.setText(mContext.getString(R.string.quiz_result)+entity.right_answer);
//			if (entity.is_user_join == 1) {
//				item.coinNum.setVisibility(View.VISIBLE);
//				item.imgGold.setVisibility(View.VISIBLE);
//				if (entity.win_coin >= 0) {
//					item.coinNum.setText("+"+ entity.win_coin);
//					((LinearLayout)contentView.findViewById(R.id.open_result)).setBackgroundResource(R.drawable.quiz_win_label);
//				}else {
//					item.coinNum.setText(""+ entity.win_coin);
//					((LinearLayout)contentView.findViewById(R.id.open_result)).setBackgroundResource(R.drawable.quiz_loss_label);
//				}
//			}
//		
//			break;
//
//		default:
//			break;
//		}
		
		
		return contentView;
	}

}
