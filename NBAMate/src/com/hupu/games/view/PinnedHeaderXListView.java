package com.hupu.games.view;

import com.hupu.games.R;
import com.hupu.games.adapter.SectionedBaseAdapter;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;


public class PinnedHeaderXListView extends ListView implements OnScrollListener {

	private OnScrollListener mOnScrollListener;
	private GestureDetector mGestureDetector;

	public static interface PinnedSectionedHeaderAdapter {
		public boolean isSectionHeader(int position);

		public int getSectionForPosition(int position);

		public View getSectionHeaderView(int section, View convertView,
				ViewGroup parent);

		public int getSectionHeaderViewType(int section);

		public int getCount();

	}

	private PinnedSectionedHeaderAdapter mAdapter;
	private View mCurrentHeader;
	private int mCurrentHeaderViewType = 0;
	private float mHeaderOffset;
	private boolean mShouldPin = true;
	private int mCurrentSection = 0;
	private int mWidthMode;
	private int mHeightMode;
	private float mLastX;
	
	private boolean refreshMode = true;
	public PinnedHeaderXListView(Context context) {
		super(context);
		super.setOnScrollListener(this);

		initWithContext(context);
	}

	public PinnedHeaderXListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		super.setOnScrollListener(this);

		initWithContext(context);
	}

	public PinnedHeaderXListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		super.setOnScrollListener(this);
		initWithContext(context);
	}


	public void setPinHeaders(boolean shouldPin) {
		mShouldPin = shouldPin;
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		if (mIsFooterReady == false) {
			mIsFooterReady = true;
			addFooterView(mFooterView);
		}
		mCurrentHeader = null;
		mAdapter = (PinnedSectionedHeaderAdapter) adapter;
		super.setAdapter(adapter);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (mOnScrollListener != null) {
			mOnScrollListener.onScroll(view, firstVisibleItem,
					visibleItemCount, totalItemCount);
		}

		if (mAdapter == null || mAdapter.getCount() == 0 || !mShouldPin
				|| (firstVisibleItem < getHeaderViewsCount())) {
			mCurrentHeader = null;
			mHeaderOffset = 0.0f;
			for (int i = firstVisibleItem; i < firstVisibleItem
					+ visibleItemCount; i++) {
				View header = getChildAt(i);
				if (header != null) {
					header.setVisibility(VISIBLE);
				}
			}
			// pull-refresh
			mTotalItemCount = totalItemCount;
			if (mScrollListener != null) {
				mScrollListener.onScroll(view, firstVisibleItem,
						visibleItemCount, totalItemCount);
			}
			return;
		}

		firstVisibleItem -= getHeaderViewsCount();

		int section = mAdapter.getSectionForPosition(firstVisibleItem);
		int viewType = mAdapter.getSectionHeaderViewType(section);
		mCurrentHeader = getSectionHeaderView(section,
				mCurrentHeaderViewType != viewType ? null : mCurrentHeader);
		ensurePinnedHeaderLayout(mCurrentHeader);
		mCurrentHeaderViewType = viewType;

		mHeaderOffset = 0.0f;

		for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
			if (mAdapter.isSectionHeader(i)) {
				View header = getChildAt(i - firstVisibleItem);
				float headerTop = header.getTop();
				float pinnedHeaderHeight = mCurrentHeader.getMeasuredHeight();
				header.setVisibility(VISIBLE);
				if (pinnedHeaderHeight >= headerTop && headerTop > 0) {
					mHeaderOffset = headerTop - header.getHeight();
				} else if (headerTop <= 0) {
					header.setVisibility(INVISIBLE);
				}
			}
		}

		invalidate();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mOnScrollListener != null) {
			mOnScrollListener.onScrollStateChanged(view, scrollState);
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	private View getSectionHeaderView(int section, View oldView) {
		boolean shouldLayout = section != mCurrentSection || oldView == null;

		View view = mAdapter.getSectionHeaderView(section, oldView, this);
		if (shouldLayout) {
			// a new section, thus a new header. We should lay it out again
			ensurePinnedHeaderLayout(view);
			mCurrentSection = section;
		}
		return view;
	}

	
	private void ensurePinnedHeaderLayout(View header) {
		if (header.isLayoutRequested()) {
			int widthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(),
					mWidthMode);

			int heightSpec;
			ViewGroup.LayoutParams layoutParams = header.getLayoutParams();
			if (layoutParams != null && layoutParams.height > 0) {
				heightSpec = MeasureSpec.makeMeasureSpec(layoutParams.height,
						MeasureSpec.EXACTLY);
			} else {
				heightSpec = MeasureSpec.makeMeasureSpec(0,
						MeasureSpec.UNSPECIFIED);
			}
			
			//这是一个补丁
			try {
				header.measure(widthSpec, heightSpec);
			} catch (Exception e) {
				// TODO: handle exception
			}
			header.layout(0, 0, header.getMeasuredWidth(),
					header.getMeasuredHeight());
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mAdapter == null || !mShouldPin || mCurrentHeader == null)
			return;
		int saveCount = canvas.save();
		canvas.translate(0, mHeaderOffset);
		canvas.clipRect(0, 0, getWidth(), mCurrentHeader.getMeasuredHeight()); // needed
		// for
		// <
		// HONEYCOMB
		mCurrentHeader.draw(canvas);
		canvas.restoreToCount(saveCount);
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mOnScrollListener = l;
		mScrollListener = l;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		mWidthMode = MeasureSpec.getMode(widthMeasureSpec);
		mHeightMode = MeasureSpec.getMode(heightMeasureSpec);
	}

	public void setOnItemClickListener(
			PinnedHeaderListView.OnItemClickListener listener) {
		super.setOnItemClickListener(listener);
	}

	public static abstract class OnItemClickListener implements
			AdapterView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view,
				int rawPosition, long id) {
			SectionedBaseAdapter adapter;
			if (adapterView.getAdapter().getClass()
					.equals(HeaderViewListAdapter.class)) {
				HeaderViewListAdapter wrapperAdapter = (HeaderViewListAdapter) adapterView
						.getAdapter();
				adapter = (SectionedBaseAdapter) wrapperAdapter
						.getWrappedAdapter();
			} else {
				adapter = (SectionedBaseAdapter) adapterView.getAdapter();
			}
			int section = adapter.getSectionForPosition(rawPosition);
			int position = adapter.getPositionInSectionForPosition(rawPosition);

			if (position == -1) {
				onSectionClick(adapterView, view, section, id);
			} else {
				onItemClick(adapterView, view, section, position, id);
			}
		}

		public abstract void onItemClick(AdapterView<?> adapterView, View view,
				int section, int position, long id);

		public abstract void onSectionClick(AdapterView<?> adapterView,
				View view, int section, long id);

	}

	// pull-refresh
	private float mLastY = -1; // save event y
	private Scroller mScroller; // used for scroll back
	private OnScrollListener mScrollListener; // user's scroll listener

	// the interface to trigger refresh and load more.
	private IXListViewListener mListViewListener;

	// -- header view
	private XListViewHeader mHeaderView;
	// header view content, use it to calculate the Header's height. And hide it
	// when disable pull refresh.
	private RelativeLayout mHeaderViewContent;
	private TextView mHeaderTimeView;
	private int mHeaderViewHeight; // header view's height
	private boolean mEnablePullRefresh = true;
	private boolean mPullRefreshing = false; // is refreashing.

	// -- footer view
	public XListViewFooter mFooterView;
	private boolean mEnablePullLoad;
	private boolean mPullLoading;
	private boolean mIsFooterReady = false;

	// total list items, used to detect is at the bottom of listview.
	private int mTotalItemCount;

	// for mScroller, scroll back from header or footer.
	private int mScrollBack;
	private final static int SCROLLBACK_HEADER = 0;
	private final static int SCROLLBACK_FOOTER = 1;

	private final static int SCROLL_DURATION = 400; // scroll back duration
	private final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >= 50px
														// at bottom, trigger
														// load more.
	private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
													// feature.

	private void initWithContext(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		// XListView need the scroll event, and it will dispatch the event to
		// user's listener (as a proxy).
		super.setOnScrollListener(this);

		// init header view
		mHeaderView = new XListViewHeader(context);
		mHeaderViewContent = (RelativeLayout) mHeaderView
				.findViewById(R.id.xlistview_header_content);
//		mHeaderTimeView = (TextView) mHeaderView
//				.findViewById(R.id.xlistview_header_time);
		addHeaderView(mHeaderView);

		// init footer view
		mFooterView = new XListViewFooter(context);

		// init header height
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mHeaderViewHeight = mHeaderViewContent.getHeight();
						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (getAdapter() ==null || getAdapter().getCount() <= 0) {
			return false;
		}
		
		int count = getAdapter().getCount();
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();
			if (getFirstVisiblePosition() == 0
					&& (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
				// the first item is showing, header has shown or pull down.
				updateHeaderHeight(deltaY / OFFSET_RADIO);
				invokeOnScrolling();
			} else if (getLastVisiblePosition() == count - 1
					&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
				// last item, already pulled up or want to pull up.
				updateFooterHeight(-deltaY / OFFSET_RADIO);
			}
			break;
		default:
			mLastY = -1; // reset
			if (getFirstVisiblePosition() == 0) {
				// invoke refresh
				if (mEnablePullRefresh
						&& mHeaderView.getVisiableHeight() > (refreshMode?mHeaderViewHeight:0)) {
//					mPullRefreshing = true;
//					mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
//					if (mListViewListener != null) {
//						mListViewListener.onRefresh();
//					}
					
					setRefreshing();
				}
				resetHeaderHeight();
			} else if (getLastVisiblePosition() == count - 1) {
				// invoke load more.
				if (mEnablePullLoad
				// && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA
						&& !mPullLoading) {
					startLoadMore();
				}
				resetFooterHeight();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	public void setRefreshing() {
		//mHeaderView.setVisiableHeight(mHeaderViewHeight);
		mPullRefreshing = true;
		mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
		if (mListViewListener != null) {
			mListViewListener.onRefresh();
		}
	}

	/**
	 * enable or disable pull down refresh feature.
	 * 
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) { // disable, hide the content
			mHeaderViewContent.setVisibility(View.INVISIBLE);
		} else {
			mHeaderViewContent.setVisibility(View.VISIBLE);
		}
	}

	public void setLoadTextEnable(boolean enable){
		mFooterView.hide(enable);
	}
	public void setRefreshViewEnable(boolean enable){
		refreshMode = enable;
	}
	/**
	 * enable or disable pull up load more feature.
	 * 
	 * @param enable
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mFooterView.hide();
			mFooterView.setOnClickListener(null);
		} else {
			mPullLoading = false;
			mFooterView.show();
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
			// both "pull up" and "click" will invoke load more.
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}

	public void setPullLoadEnable(boolean enable,boolean showText) {
		mEnablePullLoad = enable;
//		Log.e("setPullLoadEnable", "mEnablePullLoad="+mEnablePullLoad+" showText="+showText);
		if (!mEnablePullLoad) {
			mFooterView.hide(showText);
			mFooterView.setOnClickListener(null);
		} else {
			mPullLoading = false;
			mFooterView.show();
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
		}
	}
	
	/**
	 * stop refresh, reset header view.
	 */
	public void stopRefresh() {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}

	/**
	 * stop load more, reset footer view.
	 */
	public void stopLoadMore() {
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
		}
	}

	/**
	 * set last refresh time
	 * 
	 * @param time
	 */
	public void setRefreshTime(String time) {
		//mHeaderTimeView.setText(time);
	}

	private void invokeOnScrolling() {
		if (mScrollListener instanceof OnXScrollListener) {
			OnXScrollListener l = (OnXScrollListener) mScrollListener;
			l.onXScrolling(this);
		}
	}

	private void updateHeaderHeight(float delta) {
		mHeaderView.setVisiableHeight((int) delta
				+ mHeaderView.getVisiableHeight());
		mHeaderView.setArrowVisibility(refreshMode);
		if (mEnablePullRefresh && !mPullRefreshing) { // 鏈浜庡埛鏂扮姸鎬侊紝鏇存柊绠ご
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				mHeaderView.setState(XListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(XListViewHeader.STATE_NORMAL);
			}
		}
		setSelection(0); // scroll to top each time
	}

	/**
	 * reset header view's height.
	 */
	private void resetHeaderHeight() {
		int height = mHeaderView.getVisiableHeight();
		if (height == 0) // not visible.
			return;
		// refreshing and header isn't shown fully. do nothing.
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height,
				SCROLL_DURATION);
		// trigger computeScroll
		invalidate();
	}

	private void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
													// more.
				mFooterView.setState(XListViewFooter.STATE_READY);
			} else {
				mFooterView.setState(XListViewFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);

		// setSelection(mTotalItemCount - 1); // scroll to bottom
	}

	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			invalidate();
		}
	}

	private void startLoadMore() {
		mPullLoading = true;
		mFooterView.setState(XListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {
			mListViewListener.onLoadMore();
		}
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	public void setXListViewListener(IXListViewListener l) {
		mListViewListener = l;
	}

	/**
	 * you can listen ListView.OnScrollListener or this one. it will invoke
	 * onXScrolling when header/footer scroll back.
	 */
	public interface OnXScrollListener extends OnScrollListener {
		public void onXScrolling(View view);
	}

	/**
	 * implements this interface to get refresh/load more event.
	 */
	public interface IXListViewListener {
		public void onRefresh();

		public void onLoadMore();
	}

}
