package android.hqs.widget.img;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hqs.tool.TextTool;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class LockPatternView extends View {
	
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Matrix mMatrix = new Matrix();
	/** 九个点 */
	private Point[][] mPoints = new Point[3][3];
	/** 按钮半径 */
	private float mRadius;
	/** 按钮图片起始位置偏移量*/
	private float mOffsetX,mOffsetY;
	/** 至少选中2个点*/
	private final int MIN_POINT = 5;
	/** 存放已选中的点 */
	private List<Point> list_Point = new ArrayList<Point>();
	
	/** 通过图片绘制 */
	private final int DRAW_BITMAP = 0;
	/** 通过颜色绘制 */
	private final int DRAW_COLOR = 1;
	private int mDrawStyle = DRAW_BITMAP;
	
	/** 按钮图片 */
	private Bitmap mPointNormal,mPointPressed,mPointError;
	/** 连接线图片*/
	private Bitmap mLinePressed,mLineError;
	
	private float mMovingX,mMovingY;
	private boolean isSelect;
	private boolean isFinish;
	private boolean movingNoPoint;
	private Point mMovingNoPoint = new Point();
	private boolean once;

	private OnPatterChangeListener mPatterChangeListener;
	public void setPatterChangeListener(OnPatterChangeListener mPatterChangeListener) {
		this.mPatterChangeListener = mPatterChangeListener;
	}
	public interface OnPatterChangeListener {
		void onPatterSuccess(String password);
		void onPatterFailed();
		void onPatterStart(boolean start);
	}

	public LockPatternView(Context context) {
		super(context);
	}

	public LockPatternView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		if (!once) {
			initPoints();
			once = true;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		switch (mDrawStyle) {
		case DRAW_BITMAP:
			pointsToCanvasByBmp(canvas);
			
			// 画线
			if (list_Point.size() > 0) {
				Point a = list_Point.get(0);
				// 绘制九宫格坐标点
				for (int i = 0; i < list_Point.size(); i++) {
					Point b = list_Point.get(i);
					lineToCanvas(canvas, a, b);
					a = b;
				}
				// 绘制鼠标坐标点
				if (movingNoPoint) {
					lineToCanvas(canvas, a, mMovingNoPoint);
				}
			}
			break;
		case DRAW_COLOR:
			break;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		movingNoPoint = false;
		isFinish = false;
		mMovingX = event.getX();
		mMovingX = event.getY();
		mMovingNoPoint.set(mMovingX, mMovingY);
		
		Point point = null;
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (mPatterChangeListener != null) {
				mPatterChangeListener.onPatterStart(true);
			}
			resetPoint();
			point = checkSelectPoint();
			if (point != null) {
				isSelect = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (isSelect) {
				point = checkSelectPoint();
				if (point == null) {
					movingNoPoint = true;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			isFinish = true;
			isSelect = false;
			break;
		default:
			break;
		}
		
		// 选中重复检查
		if (!isFinish && isSelect && point != null) {
			if (crossPoint(point)) {	// 交叉
				movingNoPoint = true;
			} else {	// 新点
				point.state = Point.STATE_PRESSED;
				list_Point.add(point);
			}
		}
		// 绘制结束
		if (isFinish) {
			if (list_Point.size() == 1) {	// 绘制不成立
				resetPoint();
			} else if (list_Point.size() < MIN_POINT && list_Point.size() > 0) {	// 绘制错误
				errorPoint();
				if (mPatterChangeListener != null) {
					mPatterChangeListener.onPatterFailed();
				}
			} else {	// 绘制成功
				if (mPatterChangeListener != null) {
					String password = null;
					for (int i = 0; i < list_Point.size(); i++) {
						password += String.valueOf(list_Point.get(i).index); 
					}
					if (!TextTool.isEmpty(password)) {
						mPatterChangeListener.onPatterSuccess(password);
					}
				}
			}
		}
		// 刷新
		postInvalidate();
		return true;
	}
	
	/**
	 * 判断点是否交叉
	 * @param p
	 * @return
	 */
	private boolean crossPoint(Point p){
		if (list_Point.contains(p)) {
			return true;
		}
		return false;
	}
	
	/** 设置绘制错误的状态 */
	private void errorPoint() {
		for (Point point : list_Point) {
			point.state = Point.STATE_ERROR;
		}
	}

	/** 设置绘制不成立 */
	private void resetPoint() {
		for (int i = 0; i < list_Point.size(); i++) {
			Point point = list_Point.get(i);
			point.state = Point.STATE_NORMAL;
		}
		list_Point.clear();
	}

	private Point checkSelectPoint(){
		for (int i = 0; i < mPoints.length; i++) {
			for (int j = 0; j < mPoints[i].length; j++) {
				Point point = mPoints[i][j];
				if (with(mRadius, point.x, point.y, mMovingX, mMovingY)) {
					return point;
				}
			}
		}
		return null;
	}
	
	/**
	 * 初始化9个点
	 */
	private void initPoints(){
		int w = getWidth();
		int h = getHeight();
		
		float offsetX = 0f,offsetY = 0f;
		
		// 判断横竖屏，获取正方形
		if (w > h) {
			offsetX = (w - h) /2;
			w = h;
		} else {
			offsetY = (h - w) /2;
			h = w;
		}
		
		mRadius = mPointNormal.getWidth();
		mOffsetX = mRadius / 2;
		mOffsetY = mPointNormal.getHeight() / 2;
		
		mPoints[0][0] = new Point(offsetX + w/4, offsetY + w/4);
		mPoints[0][1] = new Point(offsetX + w/2, offsetY + w/4);
		mPoints[0][2] = new Point(offsetX + w/4*3, offsetY + w/4);

		mPoints[1][0] = new Point(offsetX + w/4, offsetY + w/2);
		mPoints[1][1] = new Point(offsetX + w/2, offsetY + w/2);
		mPoints[1][2] = new Point(offsetX + w/4*3, offsetY + w/2);

		mPoints[2][0] = new Point(offsetX + w/2, offsetY + w/4*3);
		mPoints[2][1] = new Point(offsetX + w/2, offsetY + w/4*3);
		mPoints[2][2] = new Point(offsetX + w/4*3, offsetY + w/4*3);
		
		// 设置密码(1~9)
		int index = 1;
		for (Point[] points :this.mPoints) {
			for (Point point : points) {
				point.index = index;
				index ++;
			}
		}

	}
	
	private void lineToCanvas(Canvas canvas, Point a, Point b){
		// 线的长度
		float lineLen = (float) distance(a, b);
		/*
		 * 将旋转图片转为旋转画布。效率较高。Android官方Demo：LunarLander正是旋转画布，从而达到旋转图片，但是从效果上看，有些失真
		 * 。（旋转图片时，一般旋转中心为图片的中心。） (旋转方向为顺时针，若角度为负则为逆时针 )。 
		 * 注意画布的状态保存和恢复
		 */
		float degree = getDegree(a, b);
		canvas.save();
		//参数分别为：旋转角度，图片X中心，图片Y中心。  
		canvas.rotate(degree, a.x, a.y); 
		switch (a.state) {
		case Point.STATE_PRESSED:
			mMatrix.setScale(lineLen / mLinePressed.getWidth(), 1);
			mMatrix.postTranslate(a.x - mLinePressed.getWidth() / 2, a.y - mLinePressed.getHeight() / 2);
			//mMatrix.postRotate(degree);
			canvas.drawBitmap(mLinePressed, mMatrix, mPaint);
			break;
		default:
			mMatrix.setScale(lineLen / mLineError.getWidth(), 1);
			mMatrix.postTranslate(a.x - mLineError.getWidth() / 2, a.y - mLineError.getHeight() / 2);
			//mMatrix.postRotate(degree);
			canvas.drawBitmap(mLineError, mMatrix, mPaint);
			break;
		}
		//canvas.rotate(-degree, px, py);
		canvas.restore();
	}
	
	private void pointsToCanvasByBmp(Canvas canvas) {
		for (int i = 0; i < mPoints.length; i++) {
			for (int j = 0; j < mPoints[i].length; j++) {
				Point po = mPoints[i][j];
				switch (po.state) {
				case Point.STATE_PRESSED:
					canvas.drawBitmap(mPointPressed, po.x - mOffsetX, po.y - mOffsetY, mPaint);
					break;
				case Point.STATE_ERROR:
					canvas.drawBitmap(mPointError, po.x - mOffsetX, po.y - mOffsetY, mPaint);
					break;
				default:
					canvas.drawBitmap(mPointNormal, po.x - mOffsetX, po.y - mOffsetY, mPaint);
					break;
				}
			}
		}
	}
	
	public void setPoints(int normalId, int pressedId, int errorId){
		mPointNormal = BitmapFactory.decodeResource(getResources(), normalId);
		mPointPressed = BitmapFactory.decodeResource(getResources(), pressedId);
		mPointError = BitmapFactory.decodeResource(getResources(), errorId);
	}

	public void setLines(int pressedId, int errorId){
		mLinePressed = BitmapFactory.decodeResource(getResources(), pressedId);
		mLineError = BitmapFactory.decodeResource(getResources(), errorId);
	}
	
	/**
	 * 两点之间的度数
	 * @param a
	 * @param b
	 * @return
	 */
	private float getDegree(Point a, Point b) {
		float y = Math.abs(a.y - b.y);
		double z = Math.sqrt((a.x - b.x)*(a.x - b.x) + y*y);
		return Math.round(Math.asin(y/z) / Math.PI * 180);
	}
	
	/**
	 * 两点之间的距离
	 * @param a
	 * @param b
	 * @return
	 */
	private double distance(Point a, Point b){
    	return Math.sqrt((a.x - b.x)*(a.x - b.x) + (a.y - b.y)*(a.y - b.y));
    }
    
	/**
	 * 拖动时连接线是否与按钮重合
	 * @param r 按钮的半径
	 * @param pointX 参考点
	 * @param pointY 参考点
	 * @param movingX 移动点
	 * @param movingY 移动点
	 * @return 是否重合
	 */
	private boolean with(float r, float pointX, float pointY, float movingX, float movingY){
		return Math.sqrt((pointX - movingX)*(pointX - movingX) + (pointY - movingY)*(pointY - movingY)) < r;
    }
	private class Point {
		/** 正常状态 */
		public static final int STATE_NORMAL = 0;
		/** 按下状态 */
		public static final int STATE_PRESSED = 1;
		/** 错误状态 */
		public static final int STATE_ERROR = 2;
		/** 点的位置, 注意画笔绘制点时的起始位置要减去图片宽高的一半*/
		public float x,y;
		public int index,state;

	    public Point() {}
	    public Point(float x, float y) {
	        this.x = x;
	        this.y = y;
	    }
	    public void set(float x, float y) {
	        this.x = x;
	        this.y = y;
	    }
	    
	    @Override
	    public String toString() {
	        return "Point(" + x + ", " + y + ")";
	    }
	}

}
