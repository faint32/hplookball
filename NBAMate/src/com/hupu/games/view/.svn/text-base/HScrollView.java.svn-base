package com.hupu.games.view;

import java.util.ArrayList;
import java.util.List;

import com.hupu.games.common.HupuLog;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

/*
 * 自定义的 滚动控件
 * 重载了 onScrollChanged（滚动条变化）,监听每次的变化通知给 观察(此变化的)观察者
 * 可使用 AddOnScrollChangedListener 来订阅本控件的 滚动条变化
 * */
public class HScrollView extends HorizontalScrollView {
	ScrollViewObserver mScrollViewObserver;

	public HScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public HScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public HScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
//		Log.i("HScrollView","HScrollView onTouchEvent");
		try {
			return super.onTouchEvent(ev);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void onScrollChanged(int l, int t, int oldl, int oldt) {
		if(noHeader)
		{
			if(ob1!=null)
			{
				try {
					ob1.NotifyOnScrollChanged(this,l, t, oldl, oldt);
				} catch (Exception e) {
					// TODO: handle exception
					HupuLog.e("papa", "error");
				}
			}
		}
		else
		{
			/*
			 * 当滚动条移动后，引发 滚动事件。通知给观察者，观察者会传达给其他的。
			 */
			if (mScrollViewObserver != null /*&& (l != oldl || t != oldt)*/) {
				mScrollViewObserver.NotifyOnScrollChanged(l, t, oldl, oldt);
			}
		}
		
//		Log.i("onScrollChanged","l="+l+" oldl="+oldl);
		super.onScrollChanged(l, t, oldl, oldt);
	}

	/*
	 * 订阅 本控件 的 滚动条变化事件
	 * */
	public void AddOnScrollChangedListener(OnScrollChangedListener listener) {
		if(mScrollViewObserver ==null)
			mScrollViewObserver =new ScrollViewObserver();
		mScrollViewObserver.AddOnScrollChangedListener(listener);
	}
	
	private ScrollViewObserver1 ob1;
	public void setNoHeader(ScrollViewObserver1 ob )
	{
		noHeader=true;
		ob1=ob;
		ob.AddOnScrollChangedListener(this);
	}
	
	public ScrollViewObserver getObserver()
	{
		return mScrollViewObserver;
	}
	/*
	 * 取消 订阅 本控件 的 滚动条变化事件
	 * */
	public void RemoveOnScrollChangedListener(OnScrollChangedListener listener) {
		mScrollViewObserver.RemoveOnScrollChangedListener(listener);
	}

	/*
	 * 当发生了滚动事件时
	 */
	public static interface OnScrollChangedListener {
		public void onScrollChanged(int l, int t, int oldl, int oldt);
	}
	public boolean noHeader;
	/*
	 * 观察者
	 */
	public static class ScrollViewObserver {
		List<OnScrollChangedListener> mList;

		public ScrollViewObserver() {
			super();
			mList = new ArrayList<OnScrollChangedListener>();
		}

		public void AddOnScrollChangedListener(OnScrollChangedListener listener) {
			mList.add(listener);
		}

	
		
		public void RemoveOnScrollChangedListener(
				OnScrollChangedListener listener) {
			mList.remove(listener);
		}

		
		public void NotifyOnScrollChanged(int l, int t, int oldl, int oldt) {
			
				if (mList == null || mList.size() == 0) {
					return;
				}
				for (int i = 0; i < mList.size(); i++) {
					if (mList.get(i) != null) {
						mList.get(i).onScrollChanged(l, t, oldl, oldt);
					}
				}
			
		}
	}
	
	public static class ScrollViewObserver1 {

		List<HorizontalScrollView> mVList;
		
		public void AddOnScrollChangedListener(HorizontalScrollView v) {
			if(mVList==null)
				mVList =new ArrayList<HorizontalScrollView>();
			mVList.add(v);
		}
		
		public void NotifyOnScrollChanged(View me,int l, int t, int oldl, int oldt)
		{
//			Log.i("NotifyOnScrollChanged by view","size="+mVList.size());
			if(mVList!=null)
			{
				if (mVList.size() == 0) {
					return;
				}
				for (int i = 0; i < mVList.size(); i++) {
					HorizontalScrollView v =mVList.get(i) ;
					if (v != null && v!=me) {
						try {
							v.smoothScrollTo(l, t);
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
			}
		}
		
		public void NotifyOnScrollChanged(int dx)
		{
			//Log.i("NotifyOnScrollChanged by view","size="+mVList.size());
			if(mVList!=null)
			{
				if (mVList.size() == 0) {
					return;
				}
				HorizontalScrollView v =mVList.get(0) ;
				v.smoothScrollBy(dx, 0);
			}
		}
		
	
		public void clear()
		{
			if(mVList!=null)
				mVList.clear();
		}
		
		public int getListSize(){
			if(mVList!=null)
				return mVList.size();
			else
				return 0;
		}
	}
}
