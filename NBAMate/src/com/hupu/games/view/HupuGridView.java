package com.hupu.games.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class HupuGridView extends GridView {
	public HupuGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public HupuGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public HupuGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 
	    // TODO Auto-generated method stub 
	    int expandSpec = MeasureSpec.makeMeasureSpec(  
	               Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);  
	     
	    super.onMeasure(widthMeasureSpec, expandSpec); 
	} 
}
