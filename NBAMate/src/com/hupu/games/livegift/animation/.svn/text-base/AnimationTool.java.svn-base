package com.hupu.games.livegift.animation;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.hupu.games.R;
import com.nineoldandroids.animation.Animator;

@SuppressLint("NewApi")
public class AnimationTool {

	private LruCache<Integer, Bitmap> lruCache = new LruCache<Integer, Bitmap>(
			8192 / 4);

	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "AnimationTool#" + mCount.getAndIncrement());
		}
	};

	private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(
			10);
	private static ThreadPoolExecutor fixThreadPool;
	// = new ThreadPoolExecutor(THREAD_COUNT, THREAD_COUNT,
	// 20, TimeUnit.MILLISECONDS, sPoolWorkQueue, sThreadFactory,
	// new ThreadPoolExecutor.DiscardPolicy());

	// (ThreadPoolExecutor) Executors
	// .newFixedThreadPool(THREAD_COUNT, sThreadFactory);

	public final static int[] res = { R.drawable.bubble1, R.drawable.bubble2,
		R.drawable.bubble3, R.drawable.bubble4, R.drawable.bubble5,
		R.drawable.bubble6, R.drawable.bubble7, R.drawable.boom1,
		R.drawable.boom2, R.drawable.boom3, R.drawable.boom3_1,
		R.drawable.boom4, R.drawable.boom4_1, R.drawable.boom5,
		R.drawable.boom5_1, R.drawable.boom6, R.drawable.boom6_1,
		R.drawable.boom7, R.drawable.boom8, R.drawable.boom9,
		R.drawable.boom10, R.drawable.boom11, R.drawable.boom12, };

	private final static int MSG_CHANGE_FRAME = 0;
	private final static int MSG_FRAME_COMPLETED = 3;
	private final static int MSG_START_HALF_TEXT_ANIMATION = 1;
	private final static int MSG_START_END_TEXT_ANIMATION = 2;

	private static int CPU_COUNT = Runtime.getRuntime().availableProcessors();

	private int THREAD_COUNT;
	/** 图片每帧时间 */
	private static final int IMG_FRAME_DURATION = 41;

	private static final int TEXT_SHOW_FRAME_INDEX = 5;
	/** 文字等待多少ms开始动画 */
	private static final int TEXT_SHOW_WAIT_TIME = 400;
	/** 文字动画中间停顿多少ms */
	private static final int TEXT_SHOW_END_TIME = 200;
	/** 文字动画前半段总共显示的时间 */
	private static final int TEXT_SHOW_HALF_DURATION = 700;
	/** 文字动画后半段总共显示的时间 */
	private static final int TEXT_SHOW_END_DURATION = 500;
	/** 数字动画总共显示的时间 */
	private static final int NUMBER_SHOW_DURATION = 700;
	/** 默认显示的文字 */
	private static final String DEFAULT_TEXT = "哈";

	protected static final String TAG = AnimationTool.class.getSimpleName();
	/** 要显示的文字 */
	private String animationText = DEFAULT_TEXT;
	/** 同时显示的动画最大数 */
	private int maxAnimationNum = CPU_COUNT*3+3;

	private FrameLayout frameLayout;
	/** 屏幕宽度 */
	private int w;
	/** 屏幕高度 */
	private int h;
	/** 所显示动画activity */
	private Activity activity;

	private boolean isDestroyed = false;

	private int textLayoutId;
	private ViewGroup numTextViewGroup;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_CHANGE_FRAME:
				ImageView iv = (ImageView) msg.obj;
				if (iv != null) {
					Bitmap bitmap = msg.getData().getParcelable("bitmap");
					if (bitmap != null)
						iv.setImageBitmap(bitmap);
				}
				break;
			case MSG_FRAME_COMPLETED:
				iv = (ImageView) msg.obj;
				iv.setImageResource(0);
				if(frameLayout!=null){
					synchronized (frameLayout) {
						frameLayout.removeView(iv);
					}
				}
				break;
			case MSG_START_HALF_TEXT_ANIMATION:
				startTextViewHalfAnimation((ViewGroup) msg.obj);
				break;
			case MSG_START_END_TEXT_ANIMATION:
				startTextViewEndAnimation((ViewGroup) msg.obj);
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 
	 * @param activity
	 * @param textLayoutId
	 *            承载数字textview的framelayout布局
	 */
	public AnimationTool(Activity activity, int textLayoutId) {
		initAnimationTool(activity, textLayoutId);
	}

	public void initAnimationTool(Activity activity, int textLayoutId) {
		this.activity = activity;
		Log.i(TAG, "cpu count=" + CPU_COUNT);
		THREAD_COUNT = maxAnimationNum;
		isDestroyed = false;
		Display mDisplay = activity.getWindowManager().getDefaultDisplay();
		DisplayMetrics realDisplay = new DisplayMetrics();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			mDisplay.getRealMetrics(realDisplay);
		} else {
			mDisplay.getMetrics(realDisplay);
		}
		w = realDisplay.widthPixels;
		h = realDisplay.heightPixels;
		frameLayout = new FrameLayout(activity);
		activity.addContentView(frameLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		if (fixThreadPool == null || fixThreadPool.isShutdown()) {
			fixThreadPool = new ThreadPoolExecutor(THREAD_COUNT,
					THREAD_COUNT * 2, 1, TimeUnit.MILLISECONDS, sPoolWorkQueue,
					sThreadFactory, new ThreadPoolExecutor.DiscardPolicy());
			// (ThreadPoolExecutor)
			// Executors.newCachedThreadPool(sThreadFactory);
			//
			// Executors.newFixedThreadPool(
			// THREAD_COUNT, sThreadFactory);
		}

		// numTextViewGroup = (ViewGroup) activity.findViewById(textLayoutId);
	}

	/**
	 * 显示动画
	 * 
	 * @param num显示数字的值
	 * 
	 */
	public void showAnimation(String animationText, int color) {
		// HupuLog.e("showAnimation", animationText);
		Log.i(TAG, "showAnimation animationText=" + animationText);
		if (frameLayout != null) {
			synchronized (frameLayout) {
				if (frameLayout.getChildCount() < maxAnimationNum) {
					ViewGroup viewGroup = addViews(animationText, color);
					if (viewGroup != null) {
						// ImageView iv = (ImageView) viewGroup.getChildAt(0);
						// TextView tv = (TextView) viewGroup.getChildAt(1);
						startFrameAnimation(viewGroup);
						// startClockTask(viewGroup);
					}
				}
				// startNumberTextAnimation(num);
			}
		}
	}

	// public void showAnimation(String animText) {
	// setAnimationText(animText);
	// showAnimation(animText);
	//
	// }

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	@SuppressLint("NewApi")
	private Bitmap loadBitmapFromRes(final Context context, final int resId) {
		Bitmap bitmap = null;
		bitmap = lruCache.get(resId);
		if (bitmap != null) {
			return bitmap;
		}
		InputStream stream = null;

		try {
			BitmapFactory.Options o = null;
			o = new BitmapFactory.Options();
			o.inPurgeable = true;
			o.inInputShareable = true;
			o.inPreferredConfig = Bitmap.Config.ARGB_8888;
			o.inSampleSize = 2;
			stream = activity.getResources().openRawResource(resId);
			bitmap = BitmapFactory.decodeStream(stream, null, o);
			// Log.i(TAG,String.format("Loaded bitmap (%dx%d).",
			// bitmap.getWidth(),
			// bitmap.getHeight()));
			if (bitmap != null)
				lruCache.put(resId, bitmap);
			return bitmap;
		} catch (final Exception e) {
			return null;
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					Log.w(TAG, "Failed to close FileInputStream", e);
				}
			}
		}
	}

	/**
	 * 启动帧动画
	 * 
	 * @param iv
	 * @param position
	 */
	public void startFrameAnimation(final ViewGroup viewGroup) {
		if (handler != null) {
			final ImageView iv = (ImageView) viewGroup.getChildAt(0);
			if (iv != null) {
				fixThreadPool.execute(new Runnable() {
					long start;
					long end;

					@Override
					public void run() {
						// HupuLog.e("AnimationTool",
						// "startFrameAnimation run");
						// TODO Auto-generated method stub
						int position = 0;
						while (!isDestroyed && position <= res.length - 1) {
							iv.setDrawingCacheEnabled(false);

							start = System.currentTimeMillis();
							Bitmap bitmap = loadBitmapFromRes(activity,
									res[position]);
							//							Log.i(TAG, "图片获取时间:"
							//									+ (System.currentTimeMillis() - start));
							Bundle bundle = new Bundle();
							bundle.putParcelable("bitmap", bitmap);
							if (handler != null) {
								Message msg = handler
										.obtainMessage(MSG_CHANGE_FRAME);
								msg.obj = iv;
								msg.setData(bundle);
								msg.sendToTarget();
								if (position == TEXT_SHOW_FRAME_INDEX) {
									msg = handler
											.obtainMessage(MSG_START_HALF_TEXT_ANIMATION);
									msg.obj = viewGroup;
									msg.sendToTarget();
								}
							} else {
								if (bitmap != null && !bitmap.isRecycled())
									bitmap.recycle();
								bundle.clear();
							}
							try {
								Thread.currentThread()
								.sleep(IMG_FRAME_DURATION);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								break;
							} finally {
								// System.gc();
								position++;
							}
						}

						if (handler != null) {
							iv.setDrawingCacheEnabled(false);
							Message msg = handler
									.obtainMessage(MSG_FRAME_COMPLETED);
							msg.obj = iv;
							msg.sendToTarget();
						}
					}
				});

			}
		}
		// handler.postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// if (position <= res.length - 1) {
		// iv.setImageResource(res[position]);
		// startFrameAnimation(iv, position + 1);
		// }
		// }
		// }, IMG_FRAME_DURATION);
		// }
	}

	/**
	 * 添加动画布局
	 * 
	 * @param activity
	 * @return
	 */
	public ViewGroup addViews(String animationText, int textColor) {
		if (frameLayout != null) {
			synchronized (frameLayout) {
				// HupuLog.e("AnimationTool", "addViews");

				FrameLayout flAnimation = new FrameLayout(activity);
				// 图片
				ImageView iv = new ImageView(activity);
				iv.setScaleType(ScaleType.FIT_XY);
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
				// lp.gravity = Gravity.CENTER_HORIZONTAL
				// | Gravity.CENTER_VERTICAL;
				// iv.setLayoutParams(lp);

				TextView tv = new TextView(activity);
				LayoutParams tvLp = new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT);

				int size = 0;
				if (isChinese(animationText)) {
					if (animationText.length() == 3)
						size = 3;
					else {
						size = 2;
					}

					int textSize = w / 2;
					tvLp.topMargin = (341 - textSize / 2) * h / 1037;

					if (animationText.length() == 3) {
						textSize = w / 3;
						tvLp.topMargin = (int) ((341 - textSize / 2.0) * h / 1037);
					} else if (animationText.length() == 4) {
						textSize = w / 4;
						tvLp.topMargin = (int) ((341 - textSize) * h / 1037);
					}
					Log.i(TAG, "defaultMagin=" + tvLp.topMargin);
					tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

				} else {
					size = 4;

					tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, w / 4);
					if (animationText.length() <= 4)
						tvLp.topMargin = 180;
					else
						tvLp.topMargin = 130;
				}
				if (animationText.length() >= size) {
					int type = animationText.length() / size;
					int type2 = animationText.length() % size;
					StringBuffer temp = new StringBuffer();
					for (int i = 0; i <= type - 1; i++) {
						temp.append(animationText.substring(i * size, i * size
								+ size));
						if (i != type - 1 || type2 > 0)
							temp.append("\n");
					}
					if (type2 > 0) {
						temp.append(animationText.substring(
								animationText.length() - type2,
								animationText.length()));
					}
					animationText = temp.toString();
				}

				tv.setText(animationText);
				tv.setTextColor(activity.getResources().getColor(textColor));
				tv.setVisibility(View.INVISIBLE);
				tv.setGravity(Gravity.CENTER);

				// 添加View
				flAnimation.addView(iv, lp);
				flAnimation.addView(tv, tvLp);

				frameLayout.addView(flAnimation, LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
				return flAnimation;
			}
		}
		return null;
	}

	/**
	 * 等待。。时间后显示文字动画
	 * 
	 * @param viewGroup
	 */
	public void startClockTask(final ViewGroup viewGroup) {
		// handler.postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// startTextViewHalfAnimation(viewGroup);
		// }
		// }, TEXT_SHOW_WAIT_TIME);

		fixThreadPool.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean isBreak = false;
				try {
					Thread.currentThread().sleep(TEXT_SHOW_WAIT_TIME);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					isBreak = true;
				}
				if (!isBreak) {
					Message msg = handler
							.obtainMessage(MSG_START_HALF_TEXT_ANIMATION);
					msg.obj = viewGroup;
					msg.sendToTarget();
				}
			}
		});

	}

	/**
	 * 显示文字前半段动画
	 * 
	 * @param viewGroup
	 */
	public void startTextViewHalfAnimation(final ViewGroup viewGroup) {
		final TextView tv = (TextView) viewGroup.getChildAt(1);
		HalfScalAnimator halfScalAnimator = new HalfScalAnimator();
		halfScalAnimator.setDuration(TEXT_SHOW_HALF_DURATION);
		halfScalAnimator.addAnimatorListener(new SimpleAnimationListener() {
			@Override
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub
				super.onAnimationStart(arg0);
				tv.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				// TODO Auto-generated method stub
				super.onAnimationEnd(arg0);
				if (handler != null) {
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							startTextViewEndAnimation(viewGroup);
						}
					}, TEXT_SHOW_END_TIME);
				}
			}
		});
		halfScalAnimator.animate(tv);
	}

	/**
	 * 显示结尾动画并移除动画布局
	 * 
	 * @param viewGroup
	 */
	public void startTextViewEndAnimation(final ViewGroup viewGroup) {
		final TextView tv = (TextView) viewGroup.getChildAt(1);
		final ImageView iv = (ImageView) viewGroup.getChildAt(0);
		EndScalAnimator endScalAnimator = new EndScalAnimator();
		endScalAnimator.setDuration(TEXT_SHOW_END_DURATION);
		endScalAnimator.addAnimatorListener(new SimpleAnimationListener() {

			@Override
			public void onAnimationEnd(Animator arg0) {
				// TODO Auto-generated method stub
				super.onAnimationEnd(arg0);
				if (frameLayout != null) {
					synchronized (frameLayout) {
						// iv.getDrawable().setCallback(null);
						viewGroup.removeAllViews();
						frameLayout.removeView(viewGroup);
					}
				}
			}
		});
		endScalAnimator.animate(tv);
	}

	/**
	 * 显示数字动画
	 * 
	 * @param num
	 *            显示的数字的值
	 */
	public void startNumberTextAnimation(int num) {
		final TextView tv = new TextView(activity);
		tv.setText(num + "");
		tv.setTextSize(60);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.RIGHT;
		tv.setLayoutParams(layoutParams);
		tv.setGravity(Gravity.RIGHT);
		if (numTextViewGroup != null) {
			synchronized (numTextViewGroup) {
				numTextViewGroup.addView(tv);
				NumAnimator numAnimator = new NumAnimator();
				numAnimator.setDuration(NUMBER_SHOW_DURATION);
				numAnimator.addAnimatorListener(new SimpleAnimationListener() {
					@Override
					public void onAnimationEnd(Animator arg0) {
						// TODO Auto-generated method stub
						super.onAnimationEnd(arg0);
						synchronized (numTextViewGroup) {
							numTextViewGroup.removeView(tv);
						}
					}
				});
				numAnimator.animate(tv);
			}
		}
	}

	/**
	 * 设置显示的文字
	 * 
	 * @param text
	 */
	public void setAnimationText(String text) {
		this.animationText = text;
	}

	/**
	 * 设置同时显示动画的最大数量
	 * 
	 * @param maxNum
	 */
	public void setMaxAnimationNum(int maxNum) {
		this.maxAnimationNum = maxNum;
	}

	public void destroy() {
		isDestroyed = true;
		if (fixThreadPool != null) {
			fixThreadPool.shutdown();
		}
		fixThreadPool = null;
		frameLayout = null;
		numTextViewGroup = null;
		activity = null;
		handler = null;
	}

	/**
	 * 描述：是否是中文.
	 * 
	 * @param str
	 *            指定的字符串
	 * @return 是否是中文:是为true，否则false
	 */
	public static Boolean isChinese(String str) {
		Boolean isChinese = true;
		String chinese = "[\u0391-\uFFE5]";
		if (str != null && !str.equals("")) {
			// 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
			for (int i = 0; i < str.length(); i++) {
				// 获取一个字符
				String temp = str.substring(i, i + 1);
				// 判断是否为中文字符
				if (temp.matches(chinese)) {
				} else {
					isChinese = false;
				}
			}
		}
		return isChinese;
	}

}
