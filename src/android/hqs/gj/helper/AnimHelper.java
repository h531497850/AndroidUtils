package android.hqs.gj.helper;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * 4种基本动画工具类。
 * 旋转、位移、透明、尺寸。
 * @author hqs2063594
 *
 */
public class AnimHelper {
	
	/**
	 * 创建旋转动画，默认旋转360度。
	 * @param fromD 开始旋转的度数
	 * @param toD	结束旋转的度数
	 * @param duration 旋转时间
	 * @return
	 */
	public static Animation rotate(float fromD, float toD, long duration){
		return rotate(fromD, toD, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f, duration);
	}
	
	public static Animation rotate(float fromD, float toD, int pivotXType, 
			float pivotX, int pivotYType, float pivotY, long duration) {
		RotateAnimation anim = new RotateAnimation(fromD, toD, pivotXType, pivotX,
				pivotYType, pivotY);
		anim.setDuration(duration);
		// view保持在动画结束位置,旋转完成停在那里，不回到初始状态
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
	public static Animation translate(float fromX, float toX, float fromY, float toY,
			long duration){
		return translate(fromX, toX, fromY, toY, duration, 0);
	}
	public static Animation translate(float fromX, float toX, float fromY, float toY,
			long duration, long startOffset){
		TranslateAnimation anim = new TranslateAnimation(fromX, toX, fromY, toY);
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
	public static Animation scale(float fromX, float toX, float fromY, float toY, long duration) {
		return scale(fromX, toX, fromY, toY,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, duration, 0);
	}
	public static Animation scale(float fromX, float toX, float fromY, float toY,
			float pivotX, float pivotY,long duration) {
		return scale(fromX, toX, fromY, toY,
				Animation.RELATIVE_TO_SELF, pivotX, Animation.RELATIVE_TO_SELF, pivotY, duration, 0);
	}
	public static Animation scale(float fromX, float toX, float fromY, float toY,
			int pivotXType, float pivotX,int pivotYType, float pivotY,long duration) {
		return scale(fromX, toX, fromY, toY,
				pivotXType, pivotX, pivotYType, pivotY, duration, 0);
	}
	public static Animation scale(float fromX, float toX, float fromY, float toY,
			int pivotXType, float pivotX,int pivotYType, float pivotY,
			long duration, long startOffset) {
		ScaleAnimation anim = new ScaleAnimation(fromX, toX, fromY, toY,
				pivotXType, pivotX, pivotYType, pivotY);
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
	public static Animation alpha(long duration){
		return alpha(1f, 0.0f,duration);
	}
	public static Animation alpha(long duration, long startOffset){
		return alpha(1f, 0.0f,duration, startOffset);
	}
	public static Animation alpha(float fromAlpha, float toAlpha, long duration){
		return alpha(fromAlpha, toAlpha, duration, 0);
	}
	public static Animation alpha(float fromAlpha, float toAlpha, long duration, long startOffset){
		AlphaAnimation anim = new AlphaAnimation(fromAlpha, toAlpha);
		anim.setFillAfter(true);
		anim.setDuration(duration);
		anim.setStartOffset(startOffset);
		return anim;
	}

	/**
	 * 缩小
	 * @param duration
	 * @return
	 */
	public static Animation setSmall(int duration) {
		AnimationSet set = new AnimationSet(true);
		// 注意先缩放，后改变透明度
		set.addAnimation(scale(1.0f, 0.0f, 1.0f, 0.0f, duration));
		set.addAnimation(alpha(duration));
		//set.setDuration(duration);
		//set.setFillAfter(true);
		return set;
	}
	
	/**
	 * 放大
	 * @param duration
	 * @return
	 */
	public static Animation setBig(int duration) {
		AnimationSet set = new AnimationSet(true);
		// 注意先缩放，后改变透明度
		set.addAnimation(scale(1.0f, 4.0f, 1.0f, 4.0f, duration));
		set.addAnimation(alpha(duration));
		//set.setDuration(duration);
		//set.setFillAfter(true);
		return set;
	}

}
