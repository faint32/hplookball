package com.hupu.games.livegift.animation;

import com.nineoldandroids.animation.ObjectAnimator;

import android.view.View;

public class HalfScalAnimator extends BaseViewAnimator {

	@Override
	protected void prepare(View target) {
		// TODO Auto-generated method stub
		getAnimatorAgent().playTogether(
				ObjectAnimator.ofFloat(target, "scaleY", 0,0.5f),
				ObjectAnimator.ofFloat(target, "scaleX", 0, 0.5f));
	}
}