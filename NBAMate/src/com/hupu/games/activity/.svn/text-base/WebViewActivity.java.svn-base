package com.hupu.games.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.CommentInfoEntity;
import com.hupu.games.handler.IWebViewClientEvent;
import com.hupu.games.view.HupuWebView;
import com.hupu.http.HupuHttpHandler;
import com.mato.sdk.proxy.Proxy;
import com.umeng.socialize.sso.UMSsoHandler;

public class WebViewActivity extends HupuBaseActivity implements
		IWebViewClientEvent {

	private HupuWebView mWebView;

	private ImageButton mBtnPre;
	private ImageButton mBtnNext;

	private String mStrUrl;

	private View tileBar;
	private View toolBar;
	private View mBtnIn;
	private View mBtnOut;
	private View mReplyView;
	private ImageButton mBtnReply;
	private TextView mTxtReply;

	Animation toolbarAnimIn;
	Animation toolbarAnimOut;
	Animation titlebarAnimIn;
	Animation titlebarAnimOut;

	private boolean bVertical;
	/** 网页的标题，分享用 */
	private String mWebTitle;
	/** 由于视频网页标题的文字都是腾讯视频或新浪视频，所以视频分享的内容由外部传人 */
	String mContent;
	/** 内置播放器视频播放地址 */
	private String mSource;

	private TextView mTxtTitle;

	private String initURL;

	boolean hasComment;
	String hid;
	String mURLTitle;

	boolean bFistTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		initURL = mStrUrl = getIntent().getStringExtra("url");
		mSource = getIntent().getStringExtra("source");
		mContent = getIntent().getStringExtra("content");
		setContentView(R.layout.layout_webview);
		bVertical = isPortrait();
		init();
		setState();
	}

	@SuppressLint("NewApi")
	@TargetApi(8)
	private void init() {
		mBtnPre = (ImageButton) findViewById(R.id.btn_pre);
		mBtnNext = (ImageButton) findViewById(R.id.btn_next);
		mWebView = (HupuWebView) findViewById(R.id.webview);
		Proxy.supportWebview(this);
		mBtnReply = (ImageButton) findViewById(R.id.btn_reply);
		mTxtReply = (TextView) findViewById(R.id.txt_reply);
		mTxtTitle = (TextView) findViewById(R.id.txt_title);
		mBtnOut = findViewById(R.id.btn_out);
		if (!bVertical)
			mBtnOut.setVisibility(View.VISIBLE);
		// 打开软件加速
		if (android.os.Build.VERSION.SDK_INT > 11) {
			mWebView.setLayerType(View.LAYER_TYPE_NONE, null);
		}
		mWebView.setWebViewClientEventListener(this, true);

		tileBar = findViewById(R.id.layout_title_bar);
		toolBar = findViewById(R.id.layout_tool_bar);
		mBtnIn = findViewById(R.id.btn_in);
		mReplyView = findViewById(R.id.layout_reply);
		MyAnimateListener ma = new MyAnimateListener();
		toolbarAnimIn = AnimationUtils.loadAnimation(this, R.anim.toolbar_in);
		toolbarAnimIn.setAnimationListener(ma);
		toolbarAnimOut = AnimationUtils.loadAnimation(this, R.anim.toolbar_out);
		toolbarAnimOut.setAnimationListener(ma);
		titlebarAnimIn = AnimationUtils.loadAnimation(this, R.anim.titlebar_in);
		titlebarAnimIn.setAnimationListener(ma);
		titlebarAnimOut = AnimationUtils.loadAnimation(this,
				R.anim.titlebar_out);
		// titlebarAnimOut.setAnimationListener(ma);
		btnShare = findViewById(R.id.btn_share);
		if (paserURL())
			mWebView.switchActivity(uri);
		else
			mWebView.loadUrl(mStrUrl);
		// mWebView.loadUrl("http://www.whatsmyuseragent.com/");

		setOnClickListener(R.id.btn_share);
		setOnClickListener(R.id.btn_pre);
		setOnClickListener(R.id.btn_next);
		setOnClickListener(R.id.btn_fresh);
		setOnClickListener(R.id.btn_back);
		// setOnClickListener(R.id.btn_out);
		setOnClickListener(R.id.btn_in);
		setOnClickListener(R.id.layout_reply);
		setOnClickListener(R.id.btn_out);

		// setTitle(true);
	}

	@Override
	public void treatClickEvent(int id) {

		super.treatClickEvent(id);
		switch (id) {
		case R.id.btn_fresh:
			mWebView.reload();
			break;
		case R.id.btn_next:
			goForward();
			break;
		case R.id.btn_pre:
			goBack();
			break;
		case R.id.btn_share:
			// 分享
			// Intent viewIntent = new Intent(Intent.ACTION_VIEW,
			// Uri.parse(mStrUrl));
			// startActivity(viewIntent);
			openShare();
			break;
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_out:
			setFullScreen();
			break;
		case R.id.btn_in:
			quitFullScreen();
			break;
		case R.id.layout_reply:
			if (hasComment)
				startToCommentActivity();
			break;
		}
	}

	private void setTitle(boolean isFirst) {
		// hasComment =false;
		if (isFirst) {
			if (mURLTitle != null)
				mTxtTitle.setText(mURLTitle);
			else if (mWebTitle != null)
				mTxtTitle.setText(mWebTitle);
		} else {
			if (mWebTitle != null)
				mTxtTitle.setText(mWebTitle);
		}

	}

	private void startToCommentActivity() {

		if (hid != null) {
			Intent in = new Intent(this, ReplyListActivity.class);
			in.putExtra("hid", hid);
			in.putExtra("title", mURLTitle);
			startActivity(in);
		}
	}

	Uri uri;

	/**
	 * @return 是否是看球scheme
	 * */
	private boolean paserURL() {
		HupuLog.d("paserURL=" + mStrUrl);
		uri = Uri.parse(mStrUrl);
		String id = null;
		try {
			id = uri.getQueryParameter("hid");
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (id != null) {
			hid = id;

			initParameter();
			mParams.put("hid", hid);
			sendRequest(HuPuRes.REQ_METHOD_GET_GOMMENT_INFO, mParams,
					new HupuHttpHandler(this));
		}
		if ("kanqiu".equalsIgnoreCase(uri.getScheme()))
			return true;
		else
			return false;

	}

	private void openShare() {
		if (mWebTitle != null) {
			if (mContent != null)// 视频
				showShareView("虎扑看球", "http://kanqiu.hupu.com/s?u=" + mStrUrl,
						mContent, true);
			else
				showShareView("虎扑看球", "http://kanqiu.hupu.com/s?u=" + mStrUrl,
						mWebTitle, true);
		}
	}

	@TargetApi(8)
	private boolean isPortrait() {
		int orientation = 0;
		if (android.os.Build.VERSION.SDK_INT > 7)
			orientation = getWindowManager().getDefaultDisplay().getRotation();
		else
			getWindowManager().getDefaultDisplay().getOrientation();
		if (orientation == Surface.ROTATION_90
				|| orientation == Surface.ROTATION_270) {

			return false;
		}
		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			bVertical = false;
			setFullScreen();
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			bVertical = true;
			quitFullScreen();
			mBtnOut.setVisibility(View.GONE);
		}
	}

	private void setFullScreen() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().getDecorView().invalidate();
		tileBar.setVisibility(View.GONE);
		toolBar.setVisibility(View.GONE);
		mBtnIn.setVisibility(View.VISIBLE);
		// tileBar.setAnimation(titlebarAnimOut);
		toolBar.startAnimation(toolbarAnimOut);
	}

	private void quitFullScreen() {
		tileBar.setAnimation(titlebarAnimIn);
		toolBar.startAnimation(toolbarAnimIn);
		mBtnIn.setVisibility(View.GONE);
		mBtnOut.setVisibility(View.VISIBLE);
		tileBar.setVisibility(View.VISIBLE);
		final WindowManager.LayoutParams attrs = getWindow().getAttributes();
		attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setAttributes(attrs);
		getWindow()
				.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}

	private void goBack() {
		if (mWebView.canGoBack())
			mWebView.goBack();
		setState();

	}

	private void goForward() {
		if (mWebView.canGoForward())
			mWebView.goForward();
		setState();
	}

	private void setState() {
		if (mWebView != null) {
			if (mWebView.canGoBack()) {
				mBtnPre.setEnabled(true);
				if (mWebTitle != null)
					mTxtTitle.setText(mWebTitle);
			} else {
				mBtnPre.setEnabled(false);
				setTitle(false);
			}
			// mBtnPre.setImageResource(R.drawable.btn_pre_disable);
			if (mWebView.canGoForward())
				// mBtnNext.setImageResource(R.drawable.btn_next);
				mBtnNext.setEnabled(true);
			else
				// mBtnNext.setImageResource(R.drawable.btn_next_disable);
				mBtnNext.setEnabled(false);
		}
	}

	public void onResume() {
		super.onResume();
		// mWebView.resumeTimers();
		if (mWebView != null) {
			try {
				mWebView.getClass().getMethod("onResume")
						.invoke(mWebView, (Object[]) null);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void onPause() {
		super.onPause();
		// mWebView.loadUrl("about:blank");
		// mWebView.pauseTimers();
		if (isFinishing()) {
			mWebView.loadUrl("about:blank");
			// setContentView(new FrameLayout(this));
		}
		if (mWebView != null) {
			// mWebView.stopLoading();
			try {
				mWebView.getClass().getMethod("onPause")
						.invoke(mWebView, (Object[]) null);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onDestroy() {

		if (mWebView != null) {
			// mWebView.loadUrl("about:blank");
			setContentView(new FrameLayout(this));
			mWebView.stopLoading();
			mWebView.clearHistory();
			mWebView.setVisibility(View.GONE);
			mWebView.destroy();
			mWebView = null;
		}
		super.onDestroy();
	}

	class MyAnimateListener implements AnimationListener {

		@Override
		public void onAnimationEnd(Animation animation) {
			if (animation == toolbarAnimIn)
				toolBar.setVisibility(View.VISIBLE);
			else if (animation == toolbarAnimOut)
				mBtnIn.setVisibility(View.VISIBLE);
			else if (animation == titlebarAnimIn)
				tileBar.setVisibility(View.VISIBLE);

		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub

		}
	}

	private String getMimeType(String url) {
		String extension = MimeTypeMap.getFileExtensionFromUrl(url);
		System.out.println("load extension =" + extension);
		if ("3gp".equals(extension))
			return "video/3gpp";
		else if ("mp4".equals(extension))
			return "video/mp4";
		else if ("flv".equals(extension))
			return "video/flv";
		else if ("asf".equals(extension))
			return "video/x-ms-asf";
		return null;
	}

	private void showMovie(String url) {

		Intent intent = null;
		String mime = getMimeType(url);
		if (mime != null) {
			// 如果是播放器
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse(url), mime);
			ResolveInfo info = getPackageManager().resolveActivity(intent,
					PackageManager.MATCH_DEFAULT_ONLY);

			if (info != null) {
				ComponentName myName = getComponentName();
				if (!myName.getPackageName().equals(
						info.activityInfo.packageName)
						|| !myName.getClassName()
								.equals(info.activityInfo.name)) {
					try {
						// 使用ANDROID内置播放器
						startActivity(intent);
						return;
					} catch (ActivityNotFoundException ex) {
						showToast("您没有安装播放器，请到应用市场安装播放器");
					}
				}
			} else
				mWebView.download(url, this);

		} else {

			mWebView.download(url, this);
		}

	}

	// private boolean isShowView = true;

	@Override
	public void onPageFinished(WebView view, String url) {
		// "getOriginalUrl="+view.getOriginalUrl());
		// 视频相关播放跳转逻辑
		// if (isShowView && mSource != null && !mSource.equals("")) {
		// Intent videoIntent = new Intent(WebViewActivity.this,
		// HupuVideoActivity.class);
		// videoIntent.putExtra("source", mSource);
		// startActivity(videoIntent);
		// isShowView = false;
		// }
		setState();
		setShareEnable(true);
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url,
			boolean isScheme) {
		// HupuLog.e("webact shouldOverrideUrlLoading", url);
		// HupuLog.e("webact shouldOverrideUrlLoading",
		// "getOriginalUrl=" + view.getOriginalUrl());
		/** 需求是第一次从外部进入时需要记录标题，由于会有302跳转，所以需要对比原始地址来确定加载的页面是不是第一次进入时的页面 */
		if (view.getOriginalUrl() == null
				|| view.getOriginalUrl().equals(initURL))
			bFistTitle = true;
		else
			bFistTitle = false;
		mWebTitle = null;
		mStrUrl = url;
		setShareEnable(false);
		if (!isScheme) {
			if (url.indexOf(".3gp") != -1 || url.indexOf(".mp4") != -1
					|| url.indexOf(".flv") != -1 || url.indexOf("rtsp://") > -1
					|| url.indexOf(".swf") != -1) {
				showMovie(url);
			} else {
				mStrUrl = url;
				HitTestResult hit = view.getHitTestResult();
				if (hit == null) {
					return false;// 不捕获302重定向
				} else {
					int hitType = hit.getType();
					if (hitType == HitTestResult.SRC_ANCHOR_TYPE) {// 点击超链接
						view.loadUrl(url);
						return true;// 返回true浏览器不再执行默认的操作
					} else if (hitType == 0) {// 重定向时hitType为0
						view.loadUrl(url);
						return false;// 不捕获302重定向
					} else {
						view.loadUrl(url);
						return false;
					}
				}

			}
		} else
			view.loadUrl(url);

		return true;
	}

	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		showToast("加载出错！");
	}

	@Override
	public void onReceivedTitle(WebView view, String t) {
		if (bFistTitle) {
			if (mURLTitle == null)
				mWebTitle = t;
			else
				mWebTitle = mURLTitle;
		} else
			mWebTitle = t;
		setTitle(false);
	}

	@Override
	protected void openWebBrowser() {
		closeDialog();

		Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mStrUrl));
		startActivity(viewIntent);
	}

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		if (methodId == HuPuRes.REQ_METHOD_GET_GOMMENT_INFO) {
			CommentInfoEntity entity = (CommentInfoEntity) o;

			if (entity.allow_comment > 0) {
				hasComment = true;
				mURLTitle = entity.title;
				mTxtReply.setText("" + entity.replies);

				mBtnReply.setImageResource(R.drawable.btn_comment_down);
				mBtnReply.setEnabled(true);
				mTxtReply.setEnabled(true);
				mReplyView.setEnabled(true);

			} else {
				if (entity.title != null)
					mURLTitle = entity.title;
				mTxtReply.setText("");
			}
			setTitle(true);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	private static final String CACHE_DATA_FILE = "webviewCache.db";
	private static final String WEB_VIEW_CACHE_PATH = "webviewCache";

	private static InputStream getInputStreamFromDatabase(Context context,
			String url) {
		SQLiteDatabase mDatabase = context.openOrCreateDatabase(
				CACHE_DATA_FILE, 0, null);
		if (mDatabase != null) {
			Cursor c = mDatabase.rawQuery("SELECT filepath FROM cache"
					+ " WHERE lastmodify LIKE '%GMT' AND url = '" + url + "'",
					null);
			if (c != null) {
				if (c.moveToFirst()) {
					String fileName = c.getString(0);
					FileInputStream fis = null;
					if (fileName != null && fileName.trim().length() > 0) {
						try {
							String filePath = context.getCacheDir()
									+ File.separator + WEB_VIEW_CACHE_PATH
									+ File.separator + fileName;
							fis = new FileInputStream(filePath);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
					return fis;
				}
				c.close();
			}
			mDatabase.close();
		}
		return null;
	}
}
