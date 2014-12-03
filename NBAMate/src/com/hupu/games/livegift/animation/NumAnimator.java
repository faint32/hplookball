package com.hupu.games.livegift.animation;

import com.nineoldandroids.animation.ObjectAnimator;

import android.view.View;

public class NumAnimator extends BaseViewAnimator {

	@Override
	protected void prepare(View target) {
		// TODO Auto-generated method stub
		getAnimatorAgent().playTogether(
				ObjectAnimator.ofFloat(target, "alpha", 0,1),
				ObjectAnimator.ofFloat(target, "translationY", 200,0));
	}
}