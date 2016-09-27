package android.hqs.helper;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * <ul>图像颜色变换处理：
 * <li>矩阵法；</li>
 * <li>像素块法；</li>
 * <li>画笔风格法。</li>
 * </ul>
 * @author hqs2063594
 *
 */
public class ImageColorHelper {
	
	/**
	 * 处理所给图像的色光3元素，由于原图默认不可修改，我们得重新创建画布，在画布上进行修改图像。
	 * 通过使用{@link #ColorMatrix}来调整图片的属性。
	 * @param bm 要处理的图像，默认不可修改
	 * @param hue 期望的色相
	 * @param saturation 期望的饱和度
	 * @param lum 期望的亮度
	 * @return 处理后的效果图像
	 */
	public static Bitmap handleEffect(Bitmap bm, float hue, float saturation, float lum){
		ColorMatrix mHue = new ColorMatrix();
		mHue.setRotate(0, hue);	// RED
		mHue.setRotate(1, hue);	// GREEN
		mHue.setRotate(2, hue);	// BLUE

		ColorMatrix mSaturation = new ColorMatrix();
		mSaturation.setSaturation(saturation);
		
		ColorMatrix mLum = new ColorMatrix();
		mLum.setScale(lum, lum, lum, 1);	// RED、GREEN、BLUE、ALPHA
		
		ColorMatrix mImg = new ColorMatrix();
		mImg.postConcat(mHue);
		mImg.postConcat(mSaturation);
		mImg.postConcat(mLum);
		
		// 创建一个与原图一样大小的图片，Config.ARGB_8888是32位的位图
		Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		// 画笔设置抗锯齿
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColorFilter(new ColorMatrixColorFilter(mImg));
		// 绘制图片
		canvas.drawBitmap(bm, 0, 0, paint);
		
		return bmp;
	}
	
	/**
	 * 处理所给图像的色光3元素，由于原图默认不可修改，我们得重新创建画布，在画布上进行修改图像。 通过使用{@link #ColorMatrix}
	 * 来调整图片的属性。
	 * 
	 * @param bm
	 *            要处理的图像，默认不可修改
	 * @param hue
	 *            期望的色相，长度<strong>必须</strong> = 3。<br>
	 *            hue[0] = RED，hue[1] = GREEN，hue[2] = BLUE。
	 * @param saturation
	 *            期望的饱和度
	 * @param lum
	 *            期望的亮度，长度<strong>必须</strong> = 4。<br>
	 *            lum[0] = RED，lum[1] = GREEN，lum[2] = BLUE，lum[3] = ALPHA。
	 * @return 处理后的效果图像
	 */
	public static Bitmap handleEffect(Bitmap bm, float[] hue, float saturation, float[] lum){
		ColorMatrix mHue = new ColorMatrix();
		mHue.setRotate(0, hue[0]);	// RED
		mHue.setRotate(1, hue[1]);	// GREEN
		mHue.setRotate(2, hue[2]);	// BLUE

		ColorMatrix mSaturation = new ColorMatrix();
		mSaturation.setSaturation(saturation);
		
		ColorMatrix mLum = new ColorMatrix();
		mLum.setScale(lum[0], lum[1], lum[2], lum[3]);	// RED、GREEN、BLUE、ALPHA
		
		ColorMatrix mImg = new ColorMatrix();
		mImg.postConcat(mHue);
		mImg.postConcat(mSaturation);
		mImg.postConcat(mLum);
		
		// 创建一个与原图一样大小的图片，Config.ARGB_8888是32位的位图
		Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		// 画笔设置抗锯齿
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColorFilter(new ColorMatrixColorFilter(mImg));
		// 绘制图片
		canvas.drawBitmap(bm, 0, 0, paint);
		
		return bmp;
	}
	
	/**
	 * 处理所给图像的色光3元素，由于原图默认不可修改，我们得重新创建画布，在画布上进行修改图像。 通过使用{@link #ColorMatrix}
	 * 来调整图片的属性。
	 * @param bm 要处理的图像，默认不可修改
	 * @param colorMatrix 5*4 的矩阵
	 * @return 处理后的效果图像
	 */
	public static Bitmap handleEffect(Bitmap bm, float[] colorMatrix){
		ColorMatrix mColorMatrix = new ColorMatrix();
		mColorMatrix.set(colorMatrix);
		
		// 创建一个与原图一样大小的图片，Config.ARGB_8888是32位的位图
		Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		// 画笔设置抗锯齿
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColorFilter(new ColorMatrixColorFilter(mColorMatrix));
		// 绘制图片
		canvas.drawBitmap(bm, 0, 0, paint);
		
		return bmp;
	}
	
	/**
	 * 处理所给图像的色光3元素，由于原图默认不可修改，我们得重新创建画布，在画布上进行修改图像。 通过使用{@link #ColorMatrix}
	 * 来调整图片的属性。<p>
	 * ABC 3个像素点<br>
	 * 求B点的底片效果算法：<br>
	 * B.r = 255 - B.r;<br>
	 * B.g = 255 - B.g;<br>
	 * B.b = 255 - B.b;
	 * @param bm 要处理的图像，默认不可修改
	 * @return 底片效果的图像
	 */
	public static Bitmap handleNegativeEffect(Bitmap bm){
		int w = bm.getWidth();
		int h = bm.getHeight();
		int color;
		int r, g, b, a;
		
		Bitmap bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		
		// 保存像素点数组到图像
		int[] oldPx = new int[w*h];
		int[] newPx = new int[oldPx.length];
		/*
		 * 第1个参数代表要获取像素点的数组，
		 * 第2个参数代表起点时所要读取像素的偏移量，
		 * 第3个参数代表多少个像素点为一行，
		 * 第4/5个参数代表像素点读取起始点，
		 * 第6/7个参数代表读取图像的宽高。
		 */
		bm.getPixels(oldPx, 0, w, 0, 0, w, h);
		
		for (int i = 0; i < oldPx.length; i++) {
			color = oldPx[i];
			r = Color.red(color);
			g = Color.green(color);
			b = Color.blue(color);
			a = Color.alpha(color);
			
			r = 255 - r;
			g = 255 - g;
			b = 255 - b;
			
			if (r > 255) {
				r = 255;
			} else if (r < 0) {
				r = 0;
			}
			if (g > 255) {
				g = 255;
			} else if (g < 0) {
				g = 0;
			}
			if (b > 255) {
				b = 255;
			} else if (b < 0) {
				b = 0;
			}
			newPx[i] = Color.argb(a, r, g, b);
		}
		bmp.setPixels(newPx, 0, w, 0, 0, w, h);
		
		return bmp;
	}
	/**
	 * 处理所给图像的色光3元素，由于原图默认不可修改，我们得重新创建画布，在画布上进行修改图像。 通过使用{@link #ColorMatrix}
	 * 来调整图片的属性。
	 * @param bm 要处理的图像，默认不可修改
	 * @return 怀旧效果的图像
	 */
	public static Bitmap handleOldPhotoEffect(Bitmap bm){
		int w = bm.getWidth();
		int h = bm.getHeight();
		int color;
		int r, g, b, a, r1, g1, b1;
		
		Bitmap bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		
		// 保存像素点数组到图像
		int[] oldPx = new int[w*h];
		int[] newPx = new int[oldPx.length];
		/*
		 * 第1个参数代表要获取像素点的数组，
		 * 第2个参数代表起点时所要读取像素的偏移量，
		 * 第3个参数代表多少个像素点为一行，
		 * 第4/5个参数代表像素点读取起始点，
		 * 第6/7个参数代表读取图像的宽高。
		 */
		bm.getPixels(oldPx, 0, w, 0, 0, w, h);
		
		for (int i = 0; i < oldPx.length; i++) {
			color = oldPx[i];
			r = Color.red(color);
			g = Color.green(color);
			b = Color.blue(color);
			a = Color.alpha(color);
			
			r1 = (int) (0.393*r + 0.769*g + 0.189*b);
			g1 = (int) (0.349*r + 0.686*g + 0.168*b);
			b1 = (int) (0.272*r + 0.534*g + 0.131*b);
			
			if (r1 > 255) {
				r1 = 255;
			}
			if (g1 > 255) {
				g1 = 255;
			}
			if (b1 > 255) {
				b1 = 255;
			}
			newPx[i] = Color.argb(a, r1, g1, b1);
		}
		bmp.setPixels(newPx, 0, w, 0, 0, w, h);
		
		return bmp;
	}
	/**
	 * 处理所给图像的色光3元素，由于原图默认不可修改，我们得重新创建画布，在画布上进行修改图像。 通过使用{@link #ColorMatrix}
	 * 来调整图片的属性。
	 * @param bm 要处理的图像，默认不可修改
	 * @return 浮雕效果的图像
	 */
	public static Bitmap handleReliefEffect(Bitmap bm){
		int w = bm.getWidth();
		int h = bm.getHeight();
		int color, colorBefore;
		int r, g, b, a, r1, g1, b1;
		
		Bitmap bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		
		// 保存像素点数组到图像
		int[] oldPx = new int[w*h];
		int[] newPx = new int[oldPx.length];
		/*
		 * 第1个参数代表要获取像素点的数组，
		 * 第2个参数代表起点时所要读取像素的偏移量，
		 * 第3个参数代表多少个像素点为一行，
		 * 第4/5个参数代表像素点读取起始点，
		 * 第6/7个参数代表读取图像的宽高。
		 */
		bm.getPixels(oldPx, 0, w, 0, 0, w, h);
		
		for (int i = 1; i < oldPx.length; i++) {
			colorBefore = oldPx[i-1];
			r = Color.red(colorBefore);
			g = Color.green(colorBefore);
			b = Color.blue(colorBefore);
			a = Color.alpha(colorBefore);
			
			color = oldPx[i];
			r1 = Color.red(color);
			g1 = Color.green(color);
			b1 = Color.blue(color);
			
			r = r-r1+127;
			g = g-g1+127;
			b = b-b1+127;
			
			if (r > 255) {
				r = 255;
			}
			if (g > 255) {
				g = 255;
			}
			if (b > 255) {
				b = 255;
			}
			newPx[i] = Color.argb(a, r, g, b);
		}
		bmp.setPixels(newPx, 0, w, 0, 0, w, h);
		
		return bmp;
	}

}
