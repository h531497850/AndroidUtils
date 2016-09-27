package android.hqs.util;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * 图像转换工具
 * @author hqs2063594
 *
 */
public class ImageConvertUtil {
	
	public static Bitmap drawableToBitmap(Context context, int resId){
		return BitmapFactory.decodeResource(context.getResources(), resId);
	}
	
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bmp = Bitmap.createBitmap(
		drawable.getIntrinsicWidth(),
		drawable.getIntrinsicHeight(),
		drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
		: Bitmap.Config.RGB_565);

		Canvas canvas = new Canvas(bmp);
		// canvas.setBitmap(bitmap);

		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bmp;
	}
	
	public static Drawable bitmapToDrawable(Context context, Bitmap bmp){
		return new BitmapDrawable(context.getResources(), bmp);
	}
	public static Drawable bitmapToDrawable(Context context, InputStream is){
		return new BitmapDrawable(context.getResources(), is);
	}
	public static Drawable bitmapToDrawable(Context context, String filePath){
		return new BitmapDrawable(context.getResources(), filePath);
	}
	
	public static Bitmap bytesToBimap(byte[] b) {
		if (b == null || b.length == 0) {
			return null;
		}
		return BitmapFactory.decodeByteArray(b, 0, b.length);
	}

}
