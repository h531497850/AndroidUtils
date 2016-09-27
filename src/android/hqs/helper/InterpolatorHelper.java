package android.hqs.helper;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * 4种基本动画工具类。
 * 旋转、位移、透明、尺寸。
 * @author hqs2063594
 *
 */
public class InterpolatorHelper {
	
	/**
	 * 创建旋转动画，默认旋转360度。
	 * @param fromD 开始旋转的度数
	 * @param toD	结束旋转的度数
	 * @param duration 旋转时间
	 * @return
	 */
	public static Animation rotate(Interpolator i, float fromD, float toD, long duration){
		return rotate(i, fromD, toD, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f, duration);
	}
	
	public static Animation rotate(Interpolator i, float fromD, float toD, int pivotXType, 
			float pivotX, int pivotYType, float pivotY, long duration) {
		RotateAnimation anim = new RotateAnimation(fromD, toD, pivotXType, pivotX,
				pivotYType, pivotY);
		anim.setInterpolator(i);
		anim.setDuration(duration);
		// 旋转完成停在那里，不回到初始状态
		anim.setFillAfter(true);
		anim.setFillEnabled(true);
		return anim;
	}
	
	/**
	 * 创建位移动画
	 * @param fromX
	 * @param toX
	 * @param fromY
	 * @param toY
	 * @param duration
	 * @param startOffset
	 * @return
	 */
	public static Animation translate(Interpolator i, float fromX, float toX, float fromY, float toY,
			long duration){
		return translate(i, fromX, toX, fromY, toY, duration, 0);
	}
	public static Animation translate(Interpolator i, float fromX, float toX, float fromY, float toY,
			long duration, long startOffset){
		TranslateAnimation anim = new TranslateAnimation(fromX, toX, fromY, toY);
		anim.setInterpolator(i);
		anim.setFillAfter(true);
		anim.setDuration(duration);
		// 动画起始延时
		anim.setStartOffset(startOffset);
		return anim;
	}
	
	/**
	 * 创建缩放动画
	 * @param fromX
	 * @param toX
	 * @param fromY
	 * @param toY
	 * @param duration
	 * @return
	 */
	public static Animation scale(Interpolator i, float fromX, float toX, float fromY, float toY, long duration) {
		return scale(i, fromX, toX, fromY, toY,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, duration, 0);
	}
	public static Animation scale(Interpolator i, float fromX, float toX, float fromY, float toY,
			float pivotX, float pivotY,long duration) {
		return scale(i, fromX, toX, fromY, toY,
				Animation.RELATIVE_TO_SELF, pivotX, Animation.RELATIVE_TO_SELF, pivotY, duration, 0);
	}
	public static Animation scale(Interpolator i, float fromX, float toX, float fromY, float toY,
			int pivotXType, float pivotX,int pivotYType, float pivotY,long duration) {
		return scale(i, fromX, toX, fromY, toY,
				pivotXType, pivotX, pivotYType, pivotY, duration, 0);
	}
	public static Animation scale(Interpolator i, float fromX, float toX, float fromY, float toY,
			int pivotXType, float pivotX,int pivotYType, float pivotY,
			long duration, long startOffset) {
		ScaleAnimation anim = new ScaleAnimation(fromX, toX, fromY, toY,
				pivotXType, pivotX, pivotYType, pivotY);
		anim.setInterpolator(i);
		anim.setFillAfter(true);
		anim.setDuration(duration);
		anim.setStartOffset(startOffset);
		return anim;
	}
	
	/**
	 * 创建透明动画
	 * @param duration
	 * @return
	 */
	public static Animation alpha(Interpolator i, long duration){
		return alpha(i, 1f, 0.0f,duration);
	}
	public static Animation alpha(Interpolator i, long duration, long startOffset){
		return alpha(i, 1f, 0.0f,duration, startOffset);
	}
	public static Animation alpha(Interpolator i, float fromAlpha, float toAlpha, long duration){
		return alpha(i, fromAlpha, toAlpha, duration, 0);
	}
	public static Animation alpha(Interpolator i, float fromAlpha, float toAlpha, long duration, long startOffset){
		AlphaAnimation anim = new AlphaAnimation(fromAlpha, toAlpha);
		anim.setInterpolator(i);
		anim.setFillAfter(true);
		anim.setDuration(duration);
		anim.setStartOffset(startOffset);
		return anim;
	}

}
