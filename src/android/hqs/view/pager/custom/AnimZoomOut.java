package android.hqs.view.pager.custom;

import com.nineoldandroids.view.ViewHelper;

import android.hqs.view.pager.custom.GroupPager.PageTransformer;
import android.view.View;

/**
 * 为{@link GroupPager}各个页面直接滚动时设置的ZoomOut属性动画。<br>
 * 使用nineoldandroid向下兼容3.0一下，如果你不想使用nineoldandroid，可以将
 * {@link ViewHelper#setAlpha(View, float)} 替换为{@link View#setAlpha(float)}
 * ；scaleX, scaleY and translation同理。
 * 
 * @author hqs2063594
 *
 */
public class AnimZoomOut implements PageTransformer {

	private static final float MIN_SCALE = 0.85f;
	private static final float MIN_ALPHA = 0.5f;

	@Override
	public void transformPage(View page, float position) {
		int pageWidth = page.getWidth();
		int pageHeight = page.getHeight();
		if (position < -1) { // [-Infinity,-1)
			// 该页面从左边离开屏幕。
			// page.setAlpha(0);
			ViewHelper.setAlpha(page, 0);

		} else if (position <= 1) { // [-1,1]
			// a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
			// 修改默认的幻灯片转换，以缩小页
			float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
			float vertMargin = pageHeight * (1 - scaleFactor) / 2;
			float horzMargin = pageWidth * (1 - scaleFactor) / 2;
			if (position < 0) {
				// page.setTranslationX(horzMargin - vertMargin / 2);
				ViewHelper.setTranslationX(page, horzMargin - vertMargin / 2);
			} else {
				// page.setTranslationX(-horzMargin + vertMargin / 2);
				ViewHelper.setTranslationX(page, -horzMargin + vertMargin / 2);
			}

			// 测量下一页 (MIN_SCAL，1)
			// page.setScaleX(scaleFactor);
			// page.setScaleY(scaleFactor);
			ViewHelper.setScaleX(page, scaleFactor);
			ViewHelper.setScaleY(page, scaleFactor);

			// 根据页面大小设置透明度
			// page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)/ (1 -
			// MIN_SCALE) * (1 - MIN_ALPHA));
			ViewHelper.setAlpha(page, MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
		} else { // (1,+Infinity]
			// 该页面从右边离开屏幕。
			// page.setAlpha(0);
			ViewHelper.setAlpha(page, 0);
		}
	}

}