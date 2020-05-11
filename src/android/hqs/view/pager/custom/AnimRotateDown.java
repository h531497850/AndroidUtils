package android.hqs.view.pager.custom;

import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * 为{@link GroupPager}各个页面直接滚动时设置的RotateDown属性动画。<br>
 * 使用nineoldandroid向下兼容3.0一下，如果你不想使用nineoldandroid，可以将
 * {@link ViewHelper#setAlpha(View, float)} 替换为{@link View#setAlpha(float)}
 * ；scaleX, scaleY and translation同理。
 * 
 * @author hqs2063594
 *
 */
public class AnimRotateDown implements android.hqs.view.pager.custom.GroupPager.PageTransformer {

	private static final float ROT_MAX = 20.0f;
	private float mRot;

	public void transformPage(View view, float position) {
		if (position < -1) { // [-Infinity,-1)
			// 该页面从左边离开屏幕。
			ViewHelper.setRotation(view, 0);

		} else if (position <= 1) { // [-1,1]
			/*
			 * 抵消默认滑动转换。 a页滑动至b页 ； a页从 0.0 ~ -1 ；b页从1 ~ 0.0。
			 * 那么A页应当在滑动过程中0度到-20度的偏移，B页应当在滑动过程中+20度到0度的偏移
			 */
			if (position < 0) {

				mRot = (ROT_MAX * position);
				ViewHelper.setPivotX(view, view.getMeasuredWidth() * 0.5f);
				ViewHelper.setPivotY(view, view.getMeasuredHeight());
				ViewHelper.setRotation(view, mRot);
			} else {

				mRot = (ROT_MAX * position);
				ViewHelper.setPivotX(view, view.getMeasuredWidth() * 0.5f);
				ViewHelper.setPivotY(view, view.getMeasuredHeight());
				ViewHelper.setRotation(view, mRot);
			}

			// 测量下一页 (MIN_SCAL，1)

			// 根据页面大小设置透明度

		} else { // (1,+Infinity]
			// 该页面从右边离开屏幕。
			ViewHelper.setRotation(view, 0);
		}
	}
}