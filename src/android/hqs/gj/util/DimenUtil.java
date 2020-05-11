package android.hqs.gj.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**尺寸转换*/
public class DimenUtil {

	/**
	 * dp转换为px
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dpToPx(Context context, float dpValue) {
		return dpToPx(context.getResources(), dpValue);
	}
	public static int dpToPx(Resources res, float dpValue) {
		return dpToPx(res.getDisplayMetrics(), dpValue);
	}
	public static int dpToPx(DisplayMetrics metrics, float dpValue) {
		return (int) (dpValue * metrics.density + 0.5f);
	}
	
	/**
	 * px转换为dp
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int pxToDp(Context context, float pxValue) {
		return pxToDp(context.getResources(), pxValue);
	}
	public static int pxToDp(Resources res, float pxValue) {
		return pxToDp(res.getDisplayMetrics(), pxValue);
	}
	public static int pxToDp(DisplayMetrics metrics, float pxValue) {
		return (int) (pxValue / metrics.density + 0.5f);
	}
	
	/**
	 * dip转换为px
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dipToPx(Context context, float dpValue) {
		return dipToPx(context.getResources(), dpValue);
	}
	public static int dipToPx(Resources res, float dpValue) {
		return dipToPx(res.getDisplayMetrics(), dpValue);
	}
	public static int dipToPx(DisplayMetrics metrics, float dpValue) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue, metrics);
	}
	
	/**
	 * sp转换为px
	 * @param context
	 * @param spValue
	 * @return
	 */
	public static int spToPx(Context context, float spValue) {
		return spToPx(context.getResources(), spValue);
	}
	public static int spToPx(Resources res, float spValue) {
		return spToPx(res.getDisplayMetrics(), spValue);
	}
	public static int spToPx(DisplayMetrics metrics, float spValue) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, metrics);
	}
	
	/**
	 * px转换为sp
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int pxToSp(Context context, float pxValue) {
		return pxToSp(context.getResources(), pxValue);
	}
	public static int pxToSp(Resources res, float pxValue) {
		return pxToSp(res.getDisplayMetrics(), pxValue);
	}
	public static int pxToSp(DisplayMetrics metrics, float pxValue) {
		return (int) (pxValue / metrics.scaledDensity);
	}
	
	/**
	 * pt转换为px
	 * @param context
	 * @param ptValue
	 * @return
	 */
	public static int ptToPx(Context context, float ptValue) {
		return ptToPx(context.getResources(), ptValue);
	}
	public static int ptToPx(Resources res, float ptValue) {
		return ptToPx(res.getDisplayMetrics(), ptValue);
	}
	public static int ptToPx(DisplayMetrics metrics, float ptValue) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, ptValue, metrics);
	}
	
	/**
	 * px转换为pt
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int pxToPt(Context context, float pxValue) {
		return pxToPt(context.getResources(), pxValue);
	}
	public static int pxToPt(Resources res, float pxValue) {
		return pxToPt(res.getDisplayMetrics(), pxValue);
	}
	public static int pxToPt(DisplayMetrics metrics, float pxValue) {
		return (int) (pxValue / metrics.xdpi / (1.0f/72));
	}
	
	/**
	 * 英寸转换为px
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int inToPx(Context context, float dpValue) {
		return inToPx(context.getResources(), dpValue);
	}
	public static int inToPx(Resources res, float dpValue) {
		return inToPx(res.getDisplayMetrics(), dpValue);
	}
	public static int inToPx(DisplayMetrics metrics, float dpValue) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_IN, dpValue, metrics);
	}
	
	/**
	 * px转换为英寸
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int pxToIn(Context context, float pxValue) {
		return pxToIn(context.getResources(), pxValue);
	}
	public static int pxToIn(Resources res, float pxValue) {
		return pxToIn(res.getDisplayMetrics(), pxValue);
	}
	public static int pxToIn(DisplayMetrics metrics, float pxValue) {
		return (int) (pxValue / metrics.xdpi);
	}

	/**
	 * 毫米转换为px
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int mmToPx(Context context, float dpValue) {
		return mmToPx(context.getResources(), dpValue);
	}
	public static int mmToPx(Resources res, float dpValue) {
		return mmToPx(res.getDisplayMetrics(), dpValue);
	}
	public static int mmToPx(DisplayMetrics metrics, float dpValue) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, dpValue, metrics);
	}
	
	/**
	 * px转换为毫米
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int pxToMm(Context context, float pxValue) {
		return pxToMm(context.getResources(), pxValue);
	}
	public static int pxToMm(Resources res, float pxValue) {
		return pxToMm(res.getDisplayMetrics(), pxValue);
	}
	public static int pxToMm(DisplayMetrics metrics, float pxValue) {
		return (int) (pxValue / metrics.xdpi / (1.0f/25.4f));
	}
	
}
