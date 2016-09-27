package android.hqs.view.pager.pager1;

import com.android.hqs.R;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * 原JazzyViewPager.java，通过继承{@link #ViewPager}实现。
 * 最好不要直接使用{@link #addView(View)}等方法在这里添加数据，
 * 因为有些时候要与适配器里的数据作比较，在这里直接添加会导致适配器内没有
 * 相对应的数据，这样会引起某些方法无法使用的情况。
 * @author hqs2063594
 *
 */
public class HqsPager extends ViewPager {

	private static final String TAG = HqsPager.class.getSimpleName();

	private boolean mEnabled = true;
	private boolean mFadeEnabled = false;
	private boolean mOutlineEnabled = false;
	public static int sOutlineColor = Color.WHITE;
	private int mEffect = TransitionEffect.STANDARD;
	
	private static final float SCALE_MAX = 0.5f;
	private static final float ZOOM_MAX = 0.5f;
	private static final float ROT_MAX = 15.0f;

	/**
	 * 动画显示模式
	 * @author hqs2063594
	 */
	public static final class TransitionEffect {
		public static final int STANDARD = 0;
		public static final int TABLET = 1;
		public static final int CUBE_IN = 2;
		public static final int CUBE_OUT = 3;
		public static final int FLIP_VERTICAL = 4;
		public static final int FLIP_HORIZONTAL = 5;
		public static final int STACK = 6;
		public static final int ZOOM_IN = 7;
		public static final int ZOOM_OUT = 8;
		public static final int ROTATE_UP = 9;
		public static final int ROTATE_DOWN = 10;
		public static final int ACCORDION = 11;
	}
	
	private static final boolean HIGHT_OF_API_11;
	static {
		HIGHT_OF_API_11 = Build.VERSION.SDK_INT >= 11;
	}

	public HqsPager(Context context) {
		this(context, null);
	}

	public HqsPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		setClipChildren(false);
		// now style everything!
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HqsPager);
		mEffect = ta.getInt(R.styleable.HqsPager_style, 0);
		switch (mEffect) {
		case TransitionEffect.STACK:
		case TransitionEffect.ZOOM_OUT:
			setFadeEnabled(true);
			break;
		}
		setFadeEnabled(ta.getBoolean(R.styleable.HqsPager_fade_enabled, false));
		setOutlineEnabled(ta.getBoolean(R.styleable.HqsPager_outline_enabled, false));
		setOutlineColor(ta.getColor(R.styleable.HqsPager_outline_color, Color.WHITE));
		
		ta.recycle();
	}

	/**
	 * 设置动画显示模式，您可以调用{@link TransitionEffect#}
	 * @param effect
	 */
	public void setTransitionEffect(int effect) {
		mEffect = effect;
//		reset();
	}

	public void setPagingEnabled(boolean enabled) {
		mEnabled = enabled;
	}

	public void setFadeEnabled(boolean enabled) {
		mFadeEnabled = enabled;
	}
	
	public boolean getFadeEnabled() {
		return mFadeEnabled;
	}

	public void setOutlineEnabled(boolean enabled) {
		mOutlineEnabled = enabled;
		wrapWithOutlines();
	}

	public void setOutlineColor(int color) {
		sOutlineColor = color;
	}

	private void wrapWithOutlines() {
		for (int i = 0; i < getChildCount(); i++) {
			View v = getChildAt(i);
			if (!(v instanceof OutlineContainer)) {
				removeView(v);
				super.addView(wrapChild(v), i);
			}
		}
	}

	private View wrapChild(View child) {
		if (!mOutlineEnabled || child instanceof OutlineContainer) return child;
		OutlineContainer out = new OutlineContainer(getContext());
		out.setLayoutParams(generateDefaultLayoutParams());
		child.setLayoutParams(new OutlineContainer.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		out.addView(child);
		return out;
	}

	public void addView(View child) {
		super.addView(wrapChild(child));
	}

	public void addView(View child, int index) {
		super.addView(wrapChild(child), index);
	}

	public void addView(View child, LayoutParams params) {
		super.addView(wrapChild(child), params);
	}

	public void addView(View child, int width, int height) {
		super.addView(wrapChild(child), width, height);
	}

	public void addView(View child, int index, LayoutParams params) {
		super.addView(wrapChild(child), index, params);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		return mEnabled ? super.onInterceptTouchEvent(arg0) : false;
	}

	private int mState;
	private int oldPage;

	private View mLeft;
	private View mRight;
	private float mRot;
	private float mTrans;
	private float mScale;

	private static final class State {
		public static final int IDLE = 0;
		public static final int GOING_LEFT = 1;
		public static final int GOING_RIGHT = 2;
	}
	
//	public void reset() {
//	resetPrivate();
//	int curr = getCurrentItem();
//	onPageScrolled(curr, 0.0f, 0);
//}
//
//private void resetPrivate() {
//	for (int i = 0; i < getChildCount(); i++) {
//		View v = getChildAt(i);
//		//			ViewHelper.setRotation(v, -ViewHelper.getRotation(v));
//		//			ViewHelper.setRotationX(v, -ViewHelper.getRotationX(v));
//		//			ViewHelper.setRotationY(v, -ViewHelper.getRotationY(v));
//		//
//		//			ViewHelper.setTranslationX(v, -ViewHelper.getTranslationX(v));
//		//			ViewHelper.setTranslationY(v, -ViewHelper.getTranslationY(v));
//
//		ViewHelper.setRotation(v, 0);
//		ViewHelper.setRotationX(v, 0);
//		ViewHelper.setRotationY(v, 0);
//
//		ViewHelper.setTranslationX(v, 0);
//		ViewHelper.setTranslationY(v, 0);
//
//		ViewHelper.setAlpha(v, 1.0f);
//
//		ViewHelper.setScaleX(v, 1.0f);
//		ViewHelper.setScaleY(v, 1.0f);
//
//		ViewHelper.setPivotX(v, 0);
//		ViewHelper.setPivotY(v, 0);
//
//		logState(v, "Child " + i);
//	}
//}

	private void logState(View v, String title) {
		Log.v(TAG, title + ": ROT (" + ViewHelper.getRotation(v) + ", " +
				ViewHelper.getRotationX(v) + ", " +
				ViewHelper.getRotationY(v) + "), TRANS (" +
				ViewHelper.getTranslationX(v) + ", " +
				ViewHelper.getTranslationY(v) + "), SCALE (" +
				ViewHelper.getScaleX(v) + ", " + 
				ViewHelper.getScaleY(v) + "), ALPHA " +
				ViewHelper.getAlpha(v));
	}

	protected void animateScroll(int position, float positionOffset) {
		if (mState != State.IDLE) {
			mRot = (float)(1-Math.cos(2*Math.PI*positionOffset))/2*30.0f;
			ViewHelper.setRotationY(this, mState == State.GOING_RIGHT ? mRot : -mRot);
			ViewHelper.setPivotX(this, getMeasuredWidth()*0.5f);
			ViewHelper.setPivotY(this, getMeasuredHeight()*0.5f);
		}
	}

	protected void animateTablet(View left, View right, float positionOffset) {		
		if (mState != State.IDLE) {
			if (left != null) {
				manageLayer(left, true);
				mRot = 30.0f * positionOffset;
				mTrans = getOffsetXForRotation(mRot, left.getMeasuredWidth(),
						left.getMeasuredHeight());
				ViewHelper.setPivotX(left, left.getMeasuredWidth()/2);
				ViewHelper.setPivotY(left, left.getMeasuredHeight()/2);
				ViewHelper.setTranslationX(left, mTrans);
				ViewHelper.setRotationY(left, mRot);
				logState(left, "Left");
			}
			if (right != null) {
				manageLayer(right, true);
				mRot = -30.0f * (1-positionOffset);
				mTrans = getOffsetXForRotation(mRot, right.getMeasuredWidth(), 
						right.getMeasuredHeight());
				ViewHelper.setPivotX(right, right.getMeasuredWidth()*0.5f);
				ViewHelper.setPivotY(right, right.getMeasuredHeight()*0.5f);
				ViewHelper.setTranslationX(right, mTrans);
				ViewHelper.setRotationY(right, mRot);
				logState(right, "Right");
			}
		}
	}

	private void animateCube(View left, View right, float positionOffset, boolean in) {
		if (mState != State.IDLE) {
			if (left != null) {
				manageLayer(left, true);
				mRot = (in ? 90.0f : -90.0f) * positionOffset;
				ViewHelper.setPivotX(left, left.getMeasuredWidth());
				ViewHelper.setPivotY(left, left.getMeasuredHeight()*0.5f);
				ViewHelper.setRotationY(left, mRot);
			}
			if (right != null) {
				manageLayer(right, true);
				mRot = -(in ? 90.0f : -90.0f) * (1-positionOffset);
				ViewHelper.setPivotX(right, 0);
				ViewHelper.setPivotY(right, right.getMeasuredHeight()*0.5f);
				ViewHelper.setRotationY(right, mRot);
			}
		}
	}

	private void animateAccordion(View left, View right, float positionOffset) {
		if (mState != State.IDLE) {
			if (left != null) {
				manageLayer(left, true);
				ViewHelper.setPivotX(left, left.getMeasuredWidth());
				ViewHelper.setPivotY(left, 0);
				ViewHelper.setScaleX(left, 1-positionOffset);
			}
			if (right != null) {
				manageLayer(right, true);
				ViewHelper.setPivotX(right, 0);
				ViewHelper.setPivotY(right, 0);
				ViewHelper.setScaleX(right, positionOffset);
			}
		}
	}

	private void animateZoom(View left, View right, float positionOffset, boolean in) {
		if (mState != State.IDLE) {
			if (left != null) {
				manageLayer(left, true);
				mScale = in ? ZOOM_MAX + (1-ZOOM_MAX)*(1-positionOffset) :
					1+ZOOM_MAX - ZOOM_MAX*(1-positionOffset);
				ViewHelper.setPivotX(left, left.getMeasuredWidth()*0.5f);
				ViewHelper.setPivotY(left, left.getMeasuredHeight()*0.5f);
				ViewHelper.setScaleX(left, mScale);
				ViewHelper.setScaleY(left, mScale);
			}
			if (right != null) {
				manageLayer(right, true);
				mScale = in ? ZOOM_MAX + (1-ZOOM_MAX)*positionOffset :
					1+ZOOM_MAX - ZOOM_MAX*positionOffset;
				ViewHelper.setPivotX(right, right.getMeasuredWidth()*0.5f);
				ViewHelper.setPivotY(right, right.getMeasuredHeight()*0.5f);
				ViewHelper.setScaleX(right, mScale);
				ViewHelper.setScaleY(right, mScale);
			}
		}
	}

	private void animateRotate(View left, View right, float positionOffset, boolean up) {
		if (mState != State.IDLE) {
			if (left != null) {
				manageLayer(left, true);
				mRot = (up ? 1 : -1) * (ROT_MAX * positionOffset);
				mTrans = (up ? -1 : 1) * (float) (getMeasuredHeight() - getMeasuredHeight()*Math.cos(mRot*Math.PI/180.0f));
				ViewHelper.setPivotX(left, left.getMeasuredWidth()*0.5f);
				ViewHelper.setPivotY(left, up ? 0 : left.getMeasuredHeight());
				ViewHelper.setTranslationY(left, mTrans);
				ViewHelper.setRotation(left, mRot);
			}
			if (right != null) {
				manageLayer(right, true);
				mRot = (up ? 1 : -1) * (-ROT_MAX + ROT_MAX*positionOffset);
				mTrans = (up ? -1 : 1) * (float) (getMeasuredHeight() - getMeasuredHeight()*Math.cos(mRot*Math.PI/180.0f));
				ViewHelper.setPivotX(right, right.getMeasuredWidth()*0.5f);
				ViewHelper.setPivotY(right, up ? 0 : right.getMeasuredHeight());
				ViewHelper.setTranslationY(right, mTrans);
				ViewHelper.setRotation(right, mRot);
			}
		}
	}

	private void animateFlipHorizontal(View left, View right, float positionOffset, int positionOffsetPixels) {
		if (mState != State.IDLE) {
			if (left != null) {
				manageLayer(left, true);
				mRot = 180.0f * positionOffset;
				if (mRot > 90.0f) {
					left.setVisibility(View.INVISIBLE);
				} else {
					if (left.getVisibility() == View.INVISIBLE)
						left.setVisibility(View.VISIBLE);
					mTrans = positionOffsetPixels;
					ViewHelper.setPivotX(left, left.getMeasuredWidth()*0.5f);
					ViewHelper.setPivotY(left, left.getMeasuredHeight()*0.5f);
					ViewHelper.setTranslationX(left, mTrans);
					ViewHelper.setRotationY(left, mRot);
				}
			}
			if (right != null) {
				manageLayer(right, true);
				mRot = -180.0f * (1-positionOffset);
				if (mRot < -90.0f) {
					right.setVisibility(View.INVISIBLE);
				} else {
					if (right.getVisibility() == View.INVISIBLE)
						right.setVisibility(View.VISIBLE);
					mTrans = -getWidth()-getPageMargin()+positionOffsetPixels;
					ViewHelper.setPivotX(right, right.getMeasuredWidth()*0.5f);
					ViewHelper.setPivotY(right, right.getMeasuredHeight()*0.5f);
					ViewHelper.setTranslationX(right, mTrans);
					ViewHelper.setRotationY(right, mRot);
				}
			}
		}
	}
	
	private void animateFlipVertical(View left, View right, float positionOffset, int positionOffsetPixels) {
		if(mState != State.IDLE) {
			if (left != null) {
				manageLayer(left, true);
				mRot = 180.0f * positionOffset;
				if (mRot > 90.0f) {
					left.setVisibility(View.INVISIBLE);
				} else {
					if (left.getVisibility() == View.INVISIBLE)
						left.setVisibility(View.VISIBLE);
					mTrans = positionOffsetPixels;
					ViewHelper.setPivotX(left, left.getMeasuredWidth()*0.5f);
					ViewHelper.setPivotY(left, left.getMeasuredHeight()*0.5f);
					ViewHelper.setTranslationX(left, mTrans);
					ViewHelper.setRotationX(left, mRot);
				}
			}
			if (right != null) {
				manageLayer(right, true);
				mRot = -180.0f * (1-positionOffset);
				if (mRot < -90.0f) {
					right.setVisibility(View.INVISIBLE);
				} else {
					if (right.getVisibility() == View.INVISIBLE)
						right.setVisibility(View.VISIBLE);
					mTrans = -getWidth()-getPageMargin()+positionOffsetPixels;
					ViewHelper.setPivotX(right, right.getMeasuredWidth()*0.5f);
					ViewHelper.setPivotY(right, right.getMeasuredHeight()*0.5f);
					ViewHelper.setTranslationX(right, mTrans);
					ViewHelper.setRotationX(right, mRot);
				}
			}
		}
	}

	protected void animateStack(View left, View right, float positionOffset, int positionOffsetPixels) {		
		if (mState != State.IDLE) {
			if (right != null) {
				manageLayer(right, true);
				mScale = (1-SCALE_MAX) * positionOffset + SCALE_MAX;
				mTrans = -getWidth()-getPageMargin()+positionOffsetPixels;
				ViewHelper.setScaleX(right, mScale);
				ViewHelper.setScaleY(right, mScale);
				ViewHelper.setTranslationX(right, mTrans);
			}
			if (left != null) {
				left.bringToFront();
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void manageLayer(View v, boolean enableHardware) {
		if (!HIGHT_OF_API_11) return;
		int layerType = enableHardware ? View.LAYER_TYPE_HARDWARE : View.LAYER_TYPE_NONE;
		if (layerType != v.getLayerType())
			v.setLayerType(layerType, null);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void disableHardwareLayer() {
		if (!HIGHT_OF_API_11) return;
		View v;
		for (int i = 0; i < getChildCount(); i++) {
			v = getChildAt(i);
			if (v.getLayerType() != View.LAYER_TYPE_NONE)
				v.setLayerType(View.LAYER_TYPE_NONE, null);
		}
	}

	private Matrix mMatrix = new Matrix();
	private Camera mCamera = new Camera();
	private float[] mTempFloat2 = new float[2];

	protected float getOffsetXForRotation(float degrees, int width, int height) {
		mMatrix.reset();
		mCamera.save();
		mCamera.rotateY(Math.abs(degrees));
		mCamera.getMatrix(mMatrix);
		mCamera.restore();

		mMatrix.preTranslate(-width * 0.5f, -height * 0.5f);
		mMatrix.postTranslate(width * 0.5f, height * 0.5f);
		mTempFloat2[0] = width;
		mTempFloat2[1] = height;
		mMatrix.mapPoints(mTempFloat2);
		return (width - mTempFloat2[0]) * (degrees > 0.0f ? 1.0f : -1.0f);
	}

	protected void animateFade(View left, View right, float positionOffset) {
		if (left != null) {
			ViewHelper.setAlpha(left, 1-positionOffset);
		}
		if (right != null) {
			ViewHelper.setAlpha(right, positionOffset);
		}
	}

	protected void animateOutline(View left, View right) {
		if (!(left instanceof OutlineContainer))
			return;
		if (mState != State.IDLE) {
			if (left != null) {
				manageLayer(left, true);
				((OutlineContainer)left).setOutlineAlpha(1.0f);
			}
			if (right != null) {
				manageLayer(right, true);
				((OutlineContainer)right).setOutlineAlpha(1.0f);
			}
		} else {
			if (left != null)
				((OutlineContainer)left).start();
			if (right != null)
				((OutlineContainer)right).start();
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		if (mState == State.IDLE && positionOffset > 0) {
			oldPage = getCurrentItem();
			mState = position == oldPage ? State.GOING_RIGHT : State.GOING_LEFT;
		}
		boolean goingRight = position == oldPage;				
		if (mState == State.GOING_RIGHT && !goingRight)
			mState = State.GOING_LEFT;
		else if (mState == State.GOING_LEFT && goingRight)
			mState = State.GOING_RIGHT;

		float effectOffset = isSmall(positionOffset) ? 0 : positionOffset;
		
//		mLeft = getChildAt(position);
//		mRight = getChildAt(position+1);
		mLeft = findViewFromObject(position);
		mRight = findViewFromObject(position+1);
		
		if (mFadeEnabled)
			animateFade(mLeft, mRight, effectOffset);
		if (mOutlineEnabled)
			animateOutline(mLeft, mRight);
		
		switch (mEffect) {
		case TransitionEffect.STANDARD:
			break;
		case TransitionEffect.TABLET:
			animateTablet(mLeft, mRight, effectOffset);
			break;
		case TransitionEffect.CUBE_IN:
			animateCube(mLeft, mRight, effectOffset, true);
			break;
		case TransitionEffect.CUBE_OUT:
			animateCube(mLeft, mRight, effectOffset, false);
			break;
		case TransitionEffect.FLIP_VERTICAL:
			animateFlipVertical(mLeft, mRight, positionOffset, positionOffsetPixels);
			break;
		case TransitionEffect.FLIP_HORIZONTAL:
			animateFlipHorizontal(mLeft, mRight, effectOffset, positionOffsetPixels);
		case TransitionEffect.STACK:
			animateStack(mLeft, mRight, effectOffset, positionOffsetPixels);
			break;
		case TransitionEffect.ZOOM_IN:
			animateZoom(mLeft, mRight, effectOffset, true);
			break;
		case TransitionEffect.ZOOM_OUT:
			animateZoom(mLeft, mRight, effectOffset, false);
			break;
		case TransitionEffect.ROTATE_UP:
			animateRotate(mLeft, mRight, effectOffset, true);
			break;
		case TransitionEffect.ROTATE_DOWN:
			animateRotate(mLeft, mRight, effectOffset, false);
			break;
		case TransitionEffect.ACCORDION:
			animateAccordion(mLeft, mRight, effectOffset);
			break;
		}

		super.onPageScrolled(position, positionOffset, positionOffsetPixels);

		if (effectOffset == 0) {
			disableHardwareLayer();
			mState = State.IDLE;
		}

	}

	private boolean isSmall(float positionOffset) {
		return Math.abs(positionOffset) < 0.0001;
	}
	
	/**
	 * 从适配器里获取View对象与当前容器内当前位置的图片作比较，如果相同则认为数据正确
	 * @param position
	 * @return
	 */
	public View findViewFromObject(int position) {
		PagerAdapter a = getAdapter();
		if (a == null) {
			return null;
		}
		Object op = a.instantiateItem(this, position);
		if (op == null) {
			return null;
		}
		View page = getChildAt(position);
		if (a.isViewFromObject(page, op)){
			return page;
		}
		return null;
	}
	
	/*public View findViewFromObject(int position) {
		Object o = mObjs.get(Integer.valueOf(position));
		if (o == null) {
			return null;
		}
		PagerAdapter a = getAdapter();
		View v;
		for (int i = 0; i < getChildCount(); i++) {
			v = getChildAt(i);
			if (a.isViewFromObject(v, o))
				return v;
		}
		return null;
	}*/
	
}