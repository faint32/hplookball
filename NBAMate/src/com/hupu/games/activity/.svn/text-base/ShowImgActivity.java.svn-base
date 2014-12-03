/**
 * 
 */
package com.hupu.games.activity;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouch.OnImageViewTouchDoubleTapListener;
import it.sephiroth.android.library.imagezoom.ImageViewTouch.OnImageViewTouchSingleTapListener;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase.OnDrawableChangeListener;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.hupu.games.R;
import com.hupu.games.common.HupuLog;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

/**
 * @author
 */

public class ShowImgActivity extends HupuBaseActivity {

	private String url;
	private ImageViewTouch mImage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HupuLog.e("papa", "url========"+getIntent().getStringExtra("url"));
		if (getIntent().getStringExtra("url") != null) {
			url = getIntent().getStringExtra("url");
		}else {
			finish();
		}
		setContentView(R.layout.layout_show_img);
	}

	@Override
	public void onContentChanged() {
		mImage = (ImageViewTouch) findViewById(R.id.image);
		//mImage.setDisplayType(DisplayType.FIT_IF_BIGGER);
		setOnClickListener(R.id.image);
		setOnClickListener(R.id.btn_download);
		mImage.setSingleTapListener(new OnImageViewTouchSingleTapListener() {

			@Override
			public void onSingleTapConfirmed() {
				//HupuLog.d("papa", "onSingleTapConfirmed");
				finish();
			}
		});

		mImage.setDoubleTapListener(new OnImageViewTouchDoubleTapListener() {

			@Override
			public void onDoubleTap() {
				//Log.d("papa", "onDoubleTap");
			}
		});

		mImage.setOnDrawableChangedListener(new OnDrawableChangeListener() {

			@Override
			public void onDrawableChanged(Drawable drawable) {
				//Log.i("papa", "onBitmapChanged: " + drawable);
			}
		});
		
		UrlImageViewHelper.loadUrlDrawable(
				this, url,
				new downLoadOk());
	}


	Matrix imageMatrix;
	Bitmap downloadBitmap;
	class downLoadOk implements UrlImageViewCallback {

		@Override
		public void onLoaded(ImageView imageView, final Bitmap loadedBitmap,
				String url, boolean loadedFromCache) {
			// TODO Auto-generated method stub
			downloadBitmap = loadedBitmap;
			if( null == imageMatrix ) {
				imageMatrix = new Matrix();
			}
			float scaleWidth = ((float) mImage.getWidth())
					/ loadedBitmap.getWidth();
			//imageMatrix.postScale(scaleWidth, scaleWidth);
			mImage.setImageBitmap(loadedBitmap, imageMatrix, scaleWidth, ImageViewTouchBase.ZOOM_INVALID );
		}

	}

	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);
		switch (id) {
		case R.id.image:
			finish();
			break;
		case R.id.btn_download:
			if (downloadBitmap !=null) {
				mApp.saveBitmap(url, downloadBitmap);
			}
			break;
		}
	}

}
