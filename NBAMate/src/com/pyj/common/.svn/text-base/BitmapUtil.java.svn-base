package com.pyj.common;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * @author panyongjun
 * 
 *         Bitmap工具类
 */
public class BitmapUtil {

	/**
	 * 以最省内存的方式读取本地资源的图片 或者SDCard中的图片
	 * 
	 * @param imagePath
	 *            图片在SDCard中的路径
	 * @return
	 */
	public static Bitmap getSDCardImg(String imagePath) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		return BitmapFactory.decodeFile(imagePath, opt);
	}

	/**
	 * 读取SD卡图片(以流的形式,减少内存的消耗)
	 * 
	 * @param context
	 *            上下文
	 * @param resId
	 *            图片的ID
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 按比例对图片进行缩放(防止内存溢出)
	 * 
	 * @param path
	 *            图片的存储路径
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getBitmapFromFile(String path, int width, int height) {
		BitmapFactory.Options opts = null;
		if (width > 0 && height > 0) {
			opts = new BitmapFactory.Options();
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			// 不分配内存
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, opts);
			// 计算图片缩放比例
			opts.inSampleSize = computeSampleSize(opts, -1, 128 * 128);
			opts.inJustDecodeBounds = false;
			opts.inInputShareable = true;
			opts.inPurgeable = true;
		}
		try {
			return BitmapFactory.decodeFile(path, opts);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Android提供计算inSampleSize的方法
	 */
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 生成圆角图片
	 * 
	 * @param bitmap
	 *            资源位图
	 * @param pixels
	 *            圆角弧度
	 * @return Bitmap
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			Paint paint = new Paint();
			Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight()));
			float roundPx = pixels;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.BLACK);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			canvas.drawBitmap(bitmap, src, rect, paint);
			return output;
		} catch (Exception e) {
			return bitmap;
		}
	}

	/**
	 * 图片反转
	 * 
	 * @param path
	 *            图片的路径
	 * @param angle
	 *            旋转的角度
	 */
	public static Bitmap reverseBitmap(String path, float angle) {
		// 图片太大 要进行防止OOM操作
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		Matrix m = new Matrix();
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		m.setRotate(angle);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);
		return bitmap;
	}

	/**
	 * 图片旋转并进行缩放(缩放用来适应显示区域)
	 * 
	 * @param bitmap
	 *            原图
	 * @param count
	 *            旋转的count
	 * @return Bitmap
	 */
	@SuppressWarnings("unused")
	private Bitmap reverseAndZoomBitmap(Bitmap bitmap, int count) {
		Matrix matrix = new Matrix();
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		if (count == 0 || count == 4) {
			matrix.setRotate(90 * count);
		} else {
			matrix.setRotate(90 * count);
			float scale = (float) height / width;
			matrix.postScale(scale, scale);
		}
		return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	}

	/**
	 * 回收Bitmap对象
	 * 
	 * @param bitmap
	 *            需要回收的Bitmap对象
	 */
	public static void recyleBitmap(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			System.gc();
		}
	}
}
