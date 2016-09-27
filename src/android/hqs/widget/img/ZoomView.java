package android.hqs.widget.img;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;

/**
 * <ul>打造个性的图片预览与多点触控，功能如下：
 * <li>旋转；</li>
 * <li>多点触控缩放；</li>
 * <li>双击缩放；</li>
 * <li>自由移动。</li>
 * </ul>
 * 
 * <ul>需要用到的知识点：
 * <li>{@link #Matrix}矩阵来处理图片的放大缩小旋转等；</li>
 * <li>{@link #ScaleGestureDetector}，检测用户多点触控缩放的手势；</li>
 * <li>{@link #GestureDetector}，检测用户双击的手势；</li>
 * <li>使用接口{@link #View.OnTouchListener}，将用户触摸事件传递给{@link #ScaleGestureDetector}，让它来处理各种手势；</li>
 * <li>事件分发机制，如左右平移时如何避免与{@link #ViewPager}页面切换的冲突问题。</li>
 * </ul>
 * 
 * 使用{@link #OnGlobalLayoutListener}接口监听全局布局完成（即我们的图片加载完成）事件，我们在
 * {@link #onAttachedToWindow()}处注册监听，在{@link #onDetachedFromWindow()}处移除监听。
 * 
 * @author hqs2063594
 *
 */
public class ZoomView extends ImageView implements OnGlobalLayoutListener, OnScaleGestureListener,
 	OnTouchListener{
	
	/**
	 * 图片缩放只进行一次
	 */
	private boolean isOnce = true;
	/**
	 * 初始化时缩放值
	 */
	private float mInitRatio;
	/**
	 * 双击时缩放到达的值
	 */
	private float mMidRatio;
	/**
	 *  放大的最大值
	 */
	private float mMaxRatio;
	
	/**
	 * 3*3矩阵<br>
	 * xScale（缩放） 	xSkew（错切）  	xTrans（平移）<br>
	 * ySkew（错切） 		yScale（缩放） 	yTrans（平移）<br>
	 * mPersp0（透视）	mPersp1（透视）	mPersp2（透视）
	 */
	private Matrix mScaleMatrix;
	private RectF mMatrixRectF;
	/**
	 * 捕获用户多点触控时缩放的比例
	 */
	private ScaleGestureDetector mScaleGestureDetector;
	
	// 自由移动
	/**
	 * 记录上一次多点触控数手指数
	 */
	private int mLastPointerCount;
	private float mLastX;
	private float mLastY;
	private int mTouchSlop;
	/**
	 * 记录是否可以拖动图片
	 */
	private boolean isCanDrag;
	
	private boolean isCheckLeftAndRight;
	private boolean isCheckTopAndBottom;
	
	// 双击缩放
	private GestureDetector mGestureDetector;
	private static final int DEFAULT_DOUBLE_TAPING_EACH_SCALE_TIME = 16;
	/**
	 * 双击图片时每次自动缩放的时间间隔
	 */
	private int mDoubleTapingEachScaleTime = DEFAULT_DOUBLE_TAPING_EACH_SCALE_TIME;
	/**
	 * 如果正在自动缩放，那么用户又双击时不再缩放。
	 */
	private boolean isAutoScale;
	/**
	 * 是否缓慢缩放，默认缓慢缩放
	 */
	private boolean isSlowScale = true;

	public ZoomView(Context context) {
		super(context);
		init(context);
	}

	public ZoomView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ZoomView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context) {
		mScaleMatrix = new Matrix();
		setScaleType(ScaleType.MATRIX);
		
		mScaleGestureDetector = new ScaleGestureDetector(context, this);
		setOnTouchListener(this);
		
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		
		mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
			@Override
			public boolean onDoubleTap(MotionEvent e) {
				
				if(isAutoScale){
					return true;
				}
				
				float x = e.getX();
				float y = e.getY();
				
				if (getScale() < mMidRatio) {
					if (isSlowScale) {	// 缓慢缩放
						postDelayed(new AutoScaleRun(mMidRatio, x, y), mDoubleTapingEachScaleTime);
					} else {			// 直接缩放
						mScaleMatrix.postScale(mMidRatio / getScale(), mMidRatio / getScale(), x, y);
						setImageMatrix(mScaleMatrix);
					}
					isAutoScale = true;
				} else {
					if (isSlowScale) {	// 缓慢缩放
						postDelayed(new AutoScaleRun(mInitRatio, x, y), mDoubleTapingEachScaleTime);
					} else {			// 直接缩放
						mScaleMatrix.postScale(mInitRatio / getScale(), mInitRatio / getScale(), x, y);
						setImageMatrix(mScaleMatrix);
					}
					isAutoScale = true;
				}
				return super.onDoubleTap(e);
			}
		});
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		getViewTreeObserver().addOnGlobalLayoutListener(this);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		getViewTreeObserver().removeGlobalOnLayoutListener(this);
	}

	/**
	 * TODO 获取{@link #ImageView}加载完成的图片。
	 * 在这里得到图片大小，然后与屏幕大小作比较。大了，将图片缩小到屏幕大小并居中；小了，反之。
	 */
	@Override
	public void onGlobalLayout() {
		if (isOnce) {
			// 得到控件宽高
			int w = getWidth();
			int h = getHeight();
			// 得到我们的图片以及宽高
			Drawable drawable = getDrawable();
			if (drawable == null) {
				return;
			}
			int dw = drawable.getIntrinsicWidth();
			int dh = drawable.getIntrinsicHeight();
			
			float ratio = 1.0f;
			
			if (dw > w && dh < h) {
				// 如果图片的宽度大于控件的宽度，但高度小于控件的高度，缩小图片
				ratio = w * 1.0f / dw;
			}else if (dw < w && dh > h) {
				// 如果图片的宽度小于控件的宽度，但高度大于控件的高度，缩小图片
				ratio = h * 1.0f / dh;
			}else if ((dw > w && dh > h)||(dw < w && dh < h)) {
				// 如果宽高都大于控件，缩小图片；
				// 如果宽高都小于控件，放大图片；
				ratio = Math.min(w * 1.0f / dw, h * 1.0f / dh);
			}
			
			// 初始化时的缩放比例
			mInitRatio = ratio;
			mMaxRatio = mInitRatio * 4;
			mMidRatio = mInitRatio * 2;
			
			// 将图片移动到控件中心
			int dx = w / 2 - dw / 2;
			int dy = h / 2 - dh / 2;
			
			mScaleMatrix.postTranslate(dx, dy);
			mScaleMatrix.postScale(mInitRatio, mInitRatio, w/2, h/2);
			setImageMatrix(mScaleMatrix);
			
			isOnce = false;
		}
	}
	
	/**
	 * 获取当前图片的缩放值
	 * @return
	 */
	private float getScale(){
		float[] values = new float[9];
		mScaleMatrix.getValues(values);
		return values[Matrix.MSCALE_X];
	}

	// 缩放区间[mMaxRatio,mMaxRatio]
	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		float scale = getScale();
		float scaleFactor = detector.getScaleFactor();
		
		if (getDrawable() == null) {
			return true;
		}
		
		// 缩放范围控制
		if ((scale < mMaxRatio && scaleFactor > 1.0f) || (scale > mInitRatio && scaleFactor < 1.0f)) {
			if (scale * scaleFactor < mInitRatio) {
				scaleFactor = mInitRatio / scale;
			}
			if (scale * scaleFactor > mMaxRatio) {
				scale = mMaxRatio / scale;
			}
			// 缩放
			mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
			checkBorderAndCenterWhenScale();
			setImageMatrix(mScaleMatrix);
		}
		return true;
	}
	
	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// 双击时不在多点缩放和移动
		if (mGestureDetector.onTouchEvent(event)) {
			return true;
		}
		
		mScaleGestureDetector.onTouchEvent(event);
		
		float x = 0;
		float y = 0;
		
		// 获取多点触控的手指数量
		int pointerCount = event.getPointerCount();
		for (int i = 0; i < pointerCount; i++) {
			x += event.getX(i);
			y += event.getY(i);
		}
		
		x /= pointerCount;
		y /= pointerCount;
		
		// 用户触摸过程中手指数发生改变
		if (mLastPointerCount != pointerCount) {
			isCanDrag = false;
			mLastX = x;
			mLastY = y;
		}
		mLastPointerCount = pointerCount;
		
		RectF rectF = getMatrixRectF();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 如果图片宽或高大于组件的宽或高，不让父控件拦截该事件，可以继续上下左右移动
			if ((rectF.width() > (getWidth() + 0.01f)) || (rectF.height() > (getHeight() + 0.01f))) {
				if (getParent() instanceof ViewGroup) {
					getParent().requestDisallowInterceptTouchEvent(true);
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			// 如果图片宽或高大于组件的宽或高，不让父控件拦截该事件，可以继续上下左右移动
			if ((rectF.width() > (getWidth() + 0.01f)) || (rectF.height() > (getHeight() + 0.01f))) {
				if (getParent() instanceof ViewGroup) {
					getParent().requestDisallowInterceptTouchEvent(true);
				}
			}
			float dx = x - mLastX;
			float dy = y - mLastY;
			
			if (!isCanDrag) {
				isCanDrag = isMoveAction(dx, dy);
			}
			if (isCanDrag) {
				if (getDrawable() != null) {
					
					isCheckLeftAndRight = isCheckTopAndBottom = true;
					
					// 如果图片宽度小于控件宽度，不可横向移动
					if (rectF.width() < getWidth()) {
						isCheckLeftAndRight = false;
						dx = 0;
					}
					// 如果图片高度小于控件高度，不可纵向移动
					if (rectF.height() < getHeight()) {
						isCheckTopAndBottom = false;
						dy = 0;
					}
					
					mScaleMatrix.postTranslate(dx, dy);
					checkBorderWhenTranslate();
					setImageMatrix(mScaleMatrix);
				}
			}
			mLastX = x;
			mLastY = y;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mLastPointerCount = 0;
			break;
		default:
			break;
		}
		
		return true;
	}
	
	/**
	 * 获得图片放大缩小后的宽高和left/top/right/bottom
	 * @return
	 */
	private RectF getMatrixRectF(){
		Matrix matrix = mScaleMatrix;
		if (mMatrixRectF == null) {
			mMatrixRectF = new RectF();
		}
		Drawable d = getDrawable();
		if (d!= null) {
			// left、top都设置为0
			mMatrixRectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
			matrix.mapRect(mMatrixRectF);
		}
		return mMatrixRectF;
	}

	/**
	 * 在缩放时进行边界控制和图片位置控制，获取当前图片的宽高和坐标等。
	 */
	private void checkBorderAndCenterWhenScale() {
		RectF rectF = getMatrixRectF();
		float dx = 0;
		float dy = 0;
		
		// 获取控件宽高
		int w = getWidth();
		int h = getHeight();
		
		// 水平控制
		if (rectF.width() >= w) { 	// 如果图片宽大于控件宽
			// 当图片在屏幕左边有空隙，左移
			if (rectF.left > 0) {
				dx = -rectF.left;
			}
			// 当图片在屏幕右边有空隙，右移
			if (rectF.right < w) {
				dx = w - rectF.right;
			}
		} else { 					// 如果图片宽小于控件宽，让其水平居中
			// 控件宽中心 - 图片宽中心
			dx = w / 2f - (rectF.right - rectF.width() / 2f);
			//deltaX = w / 2 - (rectF.right + rectF.left) / 2;
		}
		
		// 垂直控制
		if (rectF.height() >= h) {	// 如果图片高小于控件高
			// 当图片在屏幕上边有空隙，上移
			if (rectF.top > 0) {
				dy = - rectF.top;
			}
			// 当图片在屏幕下边有空隙，下移
			if (rectF.bottom < h) {
				dy = h - rectF.bottom;
			}
		} else { 					// 如果图片高小于控件高，让其垂直居中
			// 控件高中心 - 图片高中心
			dy = h / 2f - (rectF.bottom - rectF.height() / 2f);
		}
		mScaleMatrix.postTranslate(dx, dy);
	}


	/**
	 * 移动时，进行边界检查
	 */
	private void checkBorderWhenTranslate() {
		RectF rectF = getMatrixRectF();
		float dx = 0;
		float dy = 0;
		
		// 获取控件宽高
		int w = getWidth();
		int h = getHeight();
		
		// 水平控制
		if (isCheckLeftAndRight) {
			if (rectF.left > 0) {
				// 当图片在屏幕左边有空隙，左移
				dx = -rectF.left;
			}
			if (rectF.right < w) {
				// 当图片在屏幕右边有空隙，右移
				dx = w - rectF.right;
			}
		}
		
		// 垂直控制
		if (isCheckTopAndBottom) {
			if (rectF.top > 0) {
				// 当图片在屏幕上边有空隙，上移
				dy = - rectF.top;
			}
			if (rectF.bottom < h) {
				// 当图片在屏幕下边有空隙，下移
				dy = h - rectF.bottom;
			}
		}
		mScaleMatrix.postTranslate(dx, dy);
	}

	/**
	 * 是否可以移动图片
	 * @param dx
	 * @param dy
	 * @return
	 */
	private boolean isMoveAction(float dx, float dy) {
		return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
	}
	
	/**
	 * 根据用户给定的最终缩放大小，慢慢缩放图片。
	 * @author hqs2063594
	 *
	 */
	private class AutoScaleRun implements Runnable {
		/**
		 * 最终缩放大小
		 */
		private float mTargetScale;
		// 缩放中心
		private float px;
		private float py;
		
		private final float bigger = 1.07f;
		private final float small = 0.93f;
		
		private float tmpScale;
		
		/**
		 * 根据用户给定的最终缩放大小，慢慢缩放
		 * @param mTargetScale
		 * @param px
		 * @param py
		 */
		public AutoScaleRun(float mTargetScale, float px, float py){
			this.mTargetScale = mTargetScale;
			this.px = px;
			this.py = py;
			
			if (getScale() < mTargetScale) {
				tmpScale = bigger;
			} else {
				tmpScale = small;
			}
			
		}
		
		@Override
		public void run() {
			// 开始自动缩放
			mScaleMatrix.postScale(tmpScale, tmpScale, px, py);
			checkBorderAndCenterWhenScale();
			setImageMatrix(mScaleMatrix);
			
			float curScale = getScale();
			if ((tmpScale > 1.0f && curScale < mTargetScale) || 
					(tmpScale < 1.0f && curScale > mTargetScale)) {
				postDelayed(AutoScaleRun.this, mDoubleTapingEachScaleTime);
			} else {	// 设置为我们的目标值
				float scale = mTargetScale / curScale;
				mScaleMatrix.postScale(scale, scale, px, py);
				checkBorderAndCenterWhenScale();
				setImageMatrix(mScaleMatrix);
				
				// 自动缩放结束，可以再次双击缩放
				isAutoScale = false;
			}
		}
	}
	
	/**
	 * 设置双击图片时每次自动缩放的时间间隔，默认时长{@link #DEFAULT_DOUBLE_TAPING_EACH_SCALE_TIME}16毫秒。
	 * @param time
	 */
	public void setDoubleTapingEachScaleTime(int time) {
		this.mDoubleTapingEachScaleTime = time;
	}
	/**
	 * 设置是否缓慢缩放，默认缓慢缩放
	 * @param isSlowScale
	 */
	public void setSlowScale(boolean isSlowScale) {
		this.isSlowScale = isSlowScale;
	}

}
