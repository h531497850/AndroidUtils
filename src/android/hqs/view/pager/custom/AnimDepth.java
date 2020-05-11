package android.hqs.view.pager.custom;

import com.nineoldandroids.view.ViewHelper;

import android.view.View;

/**
 * 为{@link GroupPager}各个页面直接滚动时设置的depth属性动画。<br>
 * 使用nineoldandroid向下兼容3.0一下，如果你不想使用nineoldandroid，可以将
 * {@link ViewHelper#setAlpha(View, float)} 替换为{@link View#setAlpha(float)}
 * ；scaleX, scaleY and translation同理。
 * 
 * @author hqs2063594
 *
 */
public class AnimDepth implements android.hqs.view.pager.custom.GroupPager.PageTransformer {

	private static final float MIN_SCALE = 0.75f;

	@Override
	public void transformPage(View page, float position) {

		int pageWidth = page.getWidth();

		if (position < -1) { // [-Infinity,-1)
			// 该页面从左边离开屏幕。
			ViewHelper.setAlpha(page, 0);

		} else if (position <= 0) { // [-1,0]
			// 使用默认幻灯片过渡到左页
			ViewHelper.setAlpha(page, 1);
			ViewHelper.setTranslationX(page, 0);
			ViewHelper.setScaleX(page, 1);
			ViewHelper.setScaleY(page, 1);

		} else if (position <= 1) { // (0,1]
			// 淡出页面。
			ViewHelper.setAlpha(page, 1 - position);

			// 抵消默认滑动转换
			ViewHelper.setTranslationX(page, pageWidth * -position);

			// 测量下一页 (MIN_SCAL，1)
			float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
			ViewHelper.setScaleX(page, scaleFactor);
			ViewHelper.setScaleY(page, scaleFactor);

		} else { // (1,+Infinity]
			// 该页面从右边离开屏幕。
			ViewHelper.setAlpha(page, 0);
		}
	}
}