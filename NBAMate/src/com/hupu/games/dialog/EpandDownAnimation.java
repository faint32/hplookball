package com.hupu.games.dialog;

import android.R.integer;
import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.hupu.games.R;

/**
 * @author wangjianjun
 * Nov 14, 2014 3:33:57 PM
 *
 * TODO
 */
public class EpandDownAnimation {
	
	private Activity mContext;
	
	private EpandAnimationCallBack callBack;
	
	public static final int ACTION_SHOW = 1;
	
	public static final int ACTION_FINISH = 2;
	
	public boolean isShowingStart;
	
	public boolean isShowingEnd;
	
	public EpandDownAnimation(Activity mContext,EpandAnimationCallBack callBack)
	{
		this.mContext = mContext;
		this.callBack = callBack;
	}
	
	/**
	 * TODO 开始动画
	 *
	 *
	 * @param view 要展示动画的View
	 * @return void
	 */
	public void startAction_TopToBottom(final View view,View lay_view)
	{
		// TODO Auto-generated method stub
		if(mContext==null||view==null||isShowingStart)
		{
			return;
		}

		view.setVisibility(View.VISIBLE);
		lay_view.setVisibility(View.VISIBLE);

//		Animation showAnim=AnimationUtils.loadAnimation(mContext, R.anim.fade_in_top);
		TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, -690, 0);
		translateAnimation.setDuration(400);

		view.startAnimation(translateAnimation);

		translateAnimation.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation animation)
			{
				view.clearAnimation();
				if(callBack!=null)
				{
					callBack.callBack(ACTION_SHOW);
				}

			}
			@Override
			public void onAnimationRepeat(Animation animation)
			{
				// TODO Auto-generated method stub
			}
			@Override
			public void onAnimationStart(Animation animation)
			{
				// TODO Auto-generated method stub
				isShowingStart = true;
			}});

	}
	
	/**
	 * TODO  结束动画
	 *
	 *
	 * @param view
	 * @return void
	 */
	public void finishAction(final View view,final View lay_view)
	{
		if(mContext==null||view==null||isShowingEnd)
		{
			return;
		}
		
		
		Animation hiddenAnim=AnimationUtils.loadAnimation(mContext, R.anim.fade_out_top);
		
		view.startAnimation(hiddenAnim);
		
		hiddenAnim.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation animation)
			{
				isShowingEnd = false;
				isShowingStart = false;
				view.clearAnimation();
				view.setVisibility(View.GONE);
				lay_view.setVisibility(View.GONE);
				if(callBack!=null)
				{
					callBack.callBack(ACTION_FINISH);
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationStart(Animation animation)
			{
				// TODO Auto-generated method stub
				isShowingEnd = true;
			}});


	}
	
	public interface EpandAnimationCallBack{
		
		/**
		 * TODO
		 *
		 *
		 * @return void 动画结束的回调
		 */
		public void callBack(int type);
	}

}
