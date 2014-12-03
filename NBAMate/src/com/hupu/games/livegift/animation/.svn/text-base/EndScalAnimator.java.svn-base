package com.hupu.games.livegift.animation;

import com.nineoldandroids.animation.ObjectAnimator;

import android.view.View;

public class EndScalAnimator extends BaseViewAnimator {

	@Override
	protected void prepare(View target) {
		// TODO Auto-generated method stub
		getAnimatorAgent().playTogether(
				ObjectAnimator.ofFloat(target, "scaleX", 0.5f, 1f),
				ObjectAnimator.ofFloat(target, "scaleY", 0.5f, 1f),
				ObjectAnimator.ofFloat(target, "alpha", 1f, 0f));
	}
}