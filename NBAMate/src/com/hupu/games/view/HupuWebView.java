package com.hupu.games.view;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hupu.games.HuPuApp;
import com.hupu.games.R;
import com.hupu.games.common.HupuLog;
import com.pyj.common.DeviceInfo;

@SuppressLint("NewApi")
/**
 *  第一种： 
 settings.setUseWideViewPort(true); 
 settings.setLoadWithOverviewMode(true); 
 用了这种方法，战报文字会变小
 第二种： 
 WebSetting settings = webView.getSettings(); 
 settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
 把所有内容放在webview等宽的一列中。（可能会出现页面中链接失效） 
 用了这种圣诞刮奖活动图片显示不出
 第三种： 
 DisplayMetrics metrics = new DisplayMetrics(); 
 getWindowManager().getDefaultDisplay().getMetrics(metrics); 
 int mDensity = metrics.densityDpi; 
 if (mDensity == 120) { 
 settings.setDefaultZoom(ZoomDensity.CLOSE); 
 }else if (mDensity == 160) { 
 settings.setDefaultZoom(ZoomDensity.MEDIUM); 
 }else if (mDensity == 240) { 
 settings.setDefaultZoom(ZoomDensity.FAR); 
 } 
 用了这种图片会超出一屏。
 * */
public class HupuWebView extends SimpleWebView {

	private HashMap<String, String> header;

	public HupuWebView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	public HupuWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	public HupuWebView(Context context) {
		super(context);

		init();
	}

	ProgressBar progressbar;

	@TargetApi(8)
	public void init() {

		progressbar = new ProgressBar(getContext(), null,
				android.R.attr.progressBarStyleHorizontal);
		progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				4, 0, 0));
		progressbar.setProgressDrawable(getContext().getResources().getDrawable(R.drawable.bg_progressbar));
		progressbar.getProgressDrawable().setColorFilter(0xff007aff, android.graphics.PorterDuff.Mode.MULTIPLY);
		addView(progressbar);
		// setWebViewClient(new WebViewClient(){});

		initDownloadListener();// 初始化下载监听
		initWebViewClient();
		WebSettings ws = getSettings();
		ws.setBuiltInZoomControls(true); // 设置显示缩放按钮
		ws.setSupportZoom(true); // 支持缩放
		ws.setJavaScriptEnabled(true);
		ws.setAllowFileAccess(true);
		//2014-9-30 修改新窗口打开的限制配置：目前是支持新窗口打开
		ws.setSupportMultipleWindows(false);
		ws.setRenderPriority(RenderPriority.NORMAL);
		ws.setJavaScriptCanOpenWindowsAutomatically(true);
		ws.setLoadsImagesAutomatically(true);
		// ws.setLightTouchEnabled(true);
		ws.setDomStorageEnabled(true);
		// ws.setUseWideViewPort(true);
		// ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		setWebChromeClient(new WebChromeClient() {

			@Override
			public void onReceivedTitle(WebView view, String title) {
				if (mIWebViewClientEvent != null)
					mIWebViewClientEvent.onReceivedTitle(view, title);
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					progressbar.setVisibility(GONE);
				} else {
					if (progressbar.getVisibility() == GONE)
						progressbar.setVisibility(VISIBLE);
					progressbar.setProgress(newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}



		});

		// DisplayMetrics metrics = new DisplayMetrics();
		// ((Activity) getContext()).getWindowManager().getDefaultDisplay()
		// .getMetrics(metrics);
		// int mDensity = metrics.densityDpi;
		// HupuLog.d("mDensity", ""+mDensity);
		// if (mDensity == 120) {
		// ws.setDefaultZoom(ZoomDensity.CLOSE);
		// } else if (mDensity == 160) {
		// ws.setDefaultZoom(ZoomDensity.MEDIUM);
		// } else if (mDensity == 240) {
		// ws.setDefaultZoom(ZoomDensity.FAR);
		// if(metrics.widthPixels >= 600)
		// ws.setDefaultZoom(ZoomDensity.FAR);
		// else if(metrics.widthPixels >= 480)
		// ws.setDefaultZoom(ZoomDensity.MEDIUM);
		// else
		// ws.setDefaultZoom(ZoomDensity.CLOSE);
		// // ws.setDefaultZoom(ZoomDensity.FAR);
		// }
		// else
		// ws.setDefaultZoom(ZoomDensity.FAR);

		ws.setUseWideViewPort(true);

		if (android.os.Build.VERSION.SDK_INT > 7)
			ws.setPluginState(PluginState.ON);
		// else
		if (android.os.Build.VERSION.SDK_INT > 6) {
			ws.setAppCacheEnabled(true);
			ws.setLoadWithOverviewMode(true);// completely zoomed out
		}
		ws.setCacheMode(WebSettings.LOAD_DEFAULT);// 设置缓存模式
		basicUA = ws.getUserAgentString()
				+ " kanqiu/"
				+ ((HuPuApp) getContext().getApplicationContext())
						.getPackageVersion();

		setWebViewLongClick();
	}

	String basicUA;

	private void setUA(int nt) {
		if (basicUA != null)
			getSettings().setUserAgentString(
					basicUA + " isp/" + nt + " network/" + nt);

	}

	private void initDownloadListener() {
		final Activity context = (Activity) getContext();
		setDownloadListener(new DownloadListener() {

			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength) {

//				HupuLog.d("load url =" + url + "  mimetype=" + mimetype);
				if (contentDisposition == null
						|| !contentDisposition.regionMatches(true, 0,
								"attachment", 0, 10)) {
					Intent intent = new Intent(Intent.ACTION_VIEW);

					intent.setDataAndType(Uri.parse(url), mimetype);
					ResolveInfo info = context.getPackageManager()
							.resolveActivity(intent,
									PackageManager.MATCH_DEFAULT_ONLY);

					if (info != null) {

						ComponentName myName = context.getComponentName();
						if (!myName.getPackageName().equals(
								info.activityInfo.packageName)
								|| !myName.getClassName().equals(
										info.activityInfo.name)) {

							try {
								context.startActivity(intent);
								return;
							} catch (ActivityNotFoundException ex) {
								Toast.makeText(context,
										"您没有安装流媒体播放器，请到应用市场安装播放器",
										Toast.LENGTH_SHORT);
							}
						}
					} else {
						// 自定义下载
						download(url, context);
					}

				}
			}

		});

		// setOnLongClickListener(new LongClick());
	}

	public void download(String url, Activity context) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			context.startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(context, "请下载应用市场", Toast.LENGTH_SHORT);
			e.printStackTrace();
		}
	}

	/** 主要为了增加gzip功能 */
	@Override
	public void loadUrl(String url) {
//		HupuLog.e("HupuWebView ", "loadUrl="+url);
		setUA(DeviceInfo.getNetWorkType(getContext()));
		if (android.os.Build.VERSION.SDK_INT < 8) {
			super.loadUrl(url);
			return;
		}
		if (header == null) {
			header = new HashMap<String, String>();
			header.put("Accept-Encoding", "gzip");
		}

		super.loadUrl(url, header);
	}

	// private IWebViewLongClickEvent mLongClickCallback;
	LongClick mLongClick;

	/**
	 * 设置webview长按事件的回调
	 * */
	public void setWebViewLongClick() {

		if (mLongClick == null)
			setOnLongClickListener(mLongClick = new LongClick());

	}

	class LongClick implements View.OnLongClickListener {

		@Override
		public boolean onLongClick(View v) {

			final WebView.HitTestResult result = getHitTestResult();
//			HupuLog.d("webview long type=" + result.getType() + " ;data="
//					+ result.getExtra());
			if (result != null) {
				if (result.getType() == WebView.HitTestResult.IMAGE_TYPE
						|| result.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {

					HupuLog.d("webview long ");

					AlertDialog.Builder builder = new AlertDialog.Builder(
							getContext());
					builder.setMessage(
							getContext().getString(R.string.load_web_img))
							.setCancelable(false)
							.setPositiveButton(
									getContext().getString(R.string.cancel),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
										}
									})
							.setNegativeButton(
									getContext().getString(
											R.string.title_confirm),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											((HuPuApp) getContext()
													.getApplicationContext())
													.setWebonLongClick(result
															.getExtra());
										}
									});
					AlertDialog alert = builder.create();
					alert.show();
				}

			}

			return false;
		}

	}

//	private boolean canScrollHor(int direction) {
//		final int offset = computeHorizontalScrollOffset();
//		final int range = computeHorizontalScrollRange()
//				- computeHorizontalScrollExtent();
//		if (range == 0)
//			return false;
//		if (direction < 0) {
//			return offset > 0;
//		} else {
//			return offset < range - 1;
//		}
//	}

}
