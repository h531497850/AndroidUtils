package android.hqs.widget.menu;

import com.android.hqs.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hqs.gj.helper.AnimHelper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

/**
 * 卫星按钮，首先将子菜单隐藏，点击主菜单时用动画将子菜单从圆心移动到将要显示的位置并用{@link View#setVisibility(int)}显示；
 * 再次点击反之。可以在布局文件内自己添加也可以在代码内设置资源文件
 * @author hqs2063594
 *
 */
public class ArcMenu extends ViewGroup implements OnClickListener{
	
	/** 菜单状态，默认不展开 */
	private int mStatus = Status.CLOSE;
	/** 菜单位置 */
	private int mPosition = Position.RIGHT_BOTTOM;
	/** 子菜单半径 */
	private int mRadius;
	/** 主菜单图片资源id */
	private int mMasterResId;
	/** 主菜单背景资源id */
	private int mMasterBgId;
	private LayoutParams mMasterParams;
	
 	/** 子菜单图片资源Id */
	private int[] mChildMenuIds;
	private LayoutParams mChildParams;
	
	/**
	 * 菜单状态类
	 * @author hqs2063594
	 */
	public static final class Status{
		public static final int CLOSE = 0;
		public static final int OPEN = 1;
	}
	
	/**
	 * 菜单的位置
	 * @author hqs2063594
	 */
	public static final class Position{
		public static final int LEFT_TOP = 0;
		public static final int LEFT_BOTTOM = 1;
		public static final int RIGHT_TOP = 2;
		public static final int RIGHT_BOTTOM = 3;
	}
	
	/** 点击子菜单的回调接口 */
	private OnMenuItemClickListener mItemClickListener;
	/**
	 * 设置点击子菜单的回调接口
	 * @param Listener
	 */
	public void setOnItemClickListener(OnMenuItemClickListener Listener) {
		this.mItemClickListener = Listener;
	}
	/**
	 * 点击子菜单的回调接口
	 * @author hqs2063594
	 */
	public interface OnMenuItemClickListener{
		/**
		 * 如果实现了该接口，那么点击子菜单时会触发该事件
		 * @param child 子菜单
		 * @param position 子菜单在子菜单项内的索引（排除主菜单）
		 */
		void onClick(View child, int position);
	}
	
	public ArcMenu(Context context) {
		this(context, null);
	}

	public ArcMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ArcMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mMasterParams = mChildParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.ArcMenu, defStyle, 0);
		
		mStatus = a.getInt(R.styleable.ArcMenu_status, Status.CLOSE);
		
		mPosition = a.getInt(R.styleable.ArcMenu_position, Position.RIGHT_BOTTOM);
		
		mRadius = (int) a.getDimension(R.styleable.ArcMenu_radius,
				TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
						getResources().getDisplayMetrics()));
		
		a.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 测量child
		// 子布局个数
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			// 需要传入父view的spec
			measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 *  对子菜单布局，默认在右下角，在布局内添加子菜单。<br>
	 *  如果在左上角({@link Position#LEFT_TOP})，从bottom到right逆时针旋转；<br>
	 *  如果在左下角({@link Position#LEFT_BOTTOM})，从top到right顺时针旋转；<br>
	 *  如果在右上角({@link Position#RIGHT_TOP})，从bottom到left顺时针旋转；<br>
	 *  如果在右上角({@link Position#RIGHT_BOTTOM})，从top到left逆时针旋转。<br>
	 *  x = r.sin(a)<br>
	 *  y = r.cos(a)<br>
	 *  a = Math.PI / 2 / (count - 1) * i
	 * @param count 子菜单个数，> 1
	 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		if (changed) {
			layoutMasterMenu(getChildAt(0));
			/*
			 * 分析： menuButton距离每个item为radius。
			 * 到item作直线，其夹角，应为90度均分。90/(item-1)=每个夹角的度数。
			 *  有角度，就能求出正弦值sina。
			 * 根据正弦公式：sina=a/c，且已知c=radius，求出a边长，即x坐标。
			 *  有角度，就能求出正弦值cosa。
			 * 余弦公式：cosa=b/c,且已知radius(斜边)，求出b边长，即y坐标
			 * 
			 * 排除主菜单开始布局子菜单
			 * 对90度均分
			 */
			int count = getChildCount() - 1;
			if (count == 1) {	// 仅有一个子菜单，在布局内添加子菜单。
				layoutOnlyOneChild(getChildAt(1), 0);
			} else if (count > 1) {
				for (int i = 0; i < count; i++) {
					layoutMultipleChild(Math.PI / 2 / (count - 1) * i, getChildAt(i + 1), i);
				}
			}
		}
	}
	
	private void addMenu() {
		addMasterMenu();
		addChildMenu();
	}
	
	private void addMasterMenu() {
		ImageView master = new ImageView(getContext());
		if (mMasterBgId <= 0) {
			master.setBackgroundColor(Color.TRANSPARENT);
		} else {
			master.setBackgroundResource(mMasterBgId);
		}
		
		Bitmap bmpBg = BitmapFactory.decodeResource(getResources(), mMasterBgId);
		int bgW = bmpBg.getWidth();
		int bgH = bmpBg.getHeight();
		if (!bmpBg.isRecycled()) {
			bmpBg.recycle();
		}
		Bitmap bmpRes = BitmapFactory.decodeResource(getResources(), mMasterResId);
		int resW = bmpRes.getWidth();
		int resH = bmpRes.getHeight();
		if (!bmpRes.isRecycled()) {
			bmpRes.recycle();
		}
		int paddingTop = (bgH - resH) / 2;
		int paddingLeft = (bgW - resW) / 2;
		// SDK 17
		master.setPaddingRelative(paddingLeft, paddingTop, paddingLeft, paddingTop);
		
		master.setId(mMasterResId);
		master.setImageResource(mMasterResId);
		addViewInLayout(master, 0, mMasterParams, false);
	}

	private void addChildMenu() {
		if (mChildMenuIds == null || mChildMenuIds.length == 0) {
			return;
		}
		for (int i = 0; i < mChildMenuIds.length; i++) {
			ImageView child = new ImageView(getContext());
			child.setId(mChildMenuIds[i]);
			child.setBackgroundColor(Color.TRANSPARENT);
			child.setImageResource(mChildMenuIds[i]);
			//child.setLayoutParams(mChildParams);
			addViewInLayout(child, i+1, mChildParams, false);
		}
	}

	/**
	 * 定位主菜单按钮
	 * @param masterMenu 
	 */
	private void layoutMasterMenu(View masterMenu) {
		masterMenu.setOnClickListener(this);
		int l = 0;
		int t = 0;
		int width = masterMenu.getMeasuredWidth();
		int height = masterMenu.getMeasuredHeight();
		switch (mPosition) {
		case Position.LEFT_TOP:
			break;
		case Position.LEFT_BOTTOM:
			t = getMeasuredHeight() - height;
			break;
		case Position.RIGHT_TOP:
			l = getMeasuredWidth() - width;
			break;
		case Position.RIGHT_BOTTOM:
			l = getMeasuredWidth() - width;
			t = getMeasuredHeight() - height;
			break;
		}
		masterMenu.layout(l, t, l + width, t + width);
	}

	private void layoutOnlyOneChild(View child, int index) {
		switch (mStatus) {
		case Status.CLOSE:
			child.setVisibility(View.GONE);
			break;
		case Status.OPEN:
			child.setVisibility(View.VISIBLE);
			break;		
		}
		setChildMenuClickListener(child, index);
		int cl = (int) (mRadius * Math.sin(Math.PI / 2 / 2));
		int ct = (int) (mRadius * Math.cos(Math.PI / 2 / 2));
		int cWidth = child.getMeasuredWidth();
		int cHeight = child.getMeasuredHeight();
		// 如果菜单位置在右上，右下
		if (mPosition == Position.RIGHT_TOP || mPosition == Position.RIGHT_BOTTOM) {
			cl = getMeasuredWidth() - cWidth - cl;
		}
		// 如果菜单位置在底部 左下，右下
		if (mPosition == Position.LEFT_BOTTOM || mPosition == Position.RIGHT_BOTTOM) {
			ct = getMeasuredHeight() - cHeight - ct;
		}
		child.layout(cl, ct, cl + cWidth, ct + cHeight);
	}
	
	private void layoutMultipleChild(double a, View child, int index){
		switch (mStatus) {
		case Status.CLOSE:
			child.setVisibility(View.GONE);
			break;
		case Status.OPEN:
			child.setVisibility(View.VISIBLE);
			break;		
		}
		setChildMenuClickListener(child, index);
		int l = (int) (mRadius * Math.sin(a));	//对横边长
		int t = (int) (mRadius * Math.cos(a));	//邻纵边长 
		int w = child.getMeasuredWidth();
		int h = child.getMeasuredHeight();
		
		//左上，左下 left值 就是上面的l l递增    符合默认变化规则  
        //左上，右上 top值 就是上面的t  t递减    符合默认变化规则 
		//右上、右下 left值一样: 从右向左 递减 
		//左下、右下 top值一样: 从上向下 递增 
		
		switch (mPosition) {
		case Position.LEFT_BOTTOM: // 从top到right顺时针旋转
			t = getMeasuredHeight() - t  - h;
			break;
		case Position.RIGHT_TOP:	// 从bottom到left顺时针旋转
			l = getMeasuredWidth() - l - w;
			break;
		case Position.RIGHT_BOTTOM:	// 从top到left逆时针旋转
			l = getMeasuredWidth() - l - w;
			t = getMeasuredHeight() - t - h;
			break;
		}
		child.layout(l, t, l + w, t + h);
	}

	@Override
	public void onClick(View v) {
		v.startAnimation(AnimHelper.rotate(0f, 360f, 300));
		toggleMenu(300);
	}
	
	/**
	 * 切换菜单，当前是隐藏的显示出来，否则反之。
	 */
	public void toggleMenu(int duration) {
		// 为menuItem添加平移动画和旋转动画
		// 去除主菜单
		int count = getChildCount() - 1;
		for (int i = 0; i < count; i++) {
			// 一个圆的弧度是2π,角度是360°   π/2即90度的弧度 
			toggleMenu(getChildAt(i + 1), Math.PI / 2 / (count - 1) * i, duration, (i * 100) / count);
		}
		// 切换菜单状态
		changeStatus();
	}
	
	/**
	 * 点击子菜单展开或折叠。<p>
	 * 平移动画 以layout中计算的长度 再乘以1或-1 <br>
	 * close： <br>
	 * 		左上 r->l b->t <br>
	 * 		右上 l->r b->t <br>
	 * 		左下 r->l t->b <br>
	 * 		右下 l->r t->b <br>
	 * open： <br>
	 * 		左上 <br>
	 * 		右上 <br>
	 * 		左下 <br>
	 * 		右下 <br>
	 * 
	 * @param child
	 *            子菜单
	 * @param index
	 *            子菜单在子菜单项内的索引（排除主菜单）
	 * @param a
	 *            子菜单的角度
	 * @param duration
	 *            子菜单动画时间
	 * @param startOffset
	 *            子菜单动画延时
	 */
	private void toggleMenu(final View child, double a, long duration, long startOffset) {
		child.setVisibility(View.VISIBLE);
		// end 0 , 0
		// start
		int cl = (int) (mRadius * Math.sin(a));
		int ct = (int) (mRadius * Math.cos(a));
		// 动画在xy上是否增大
		int xflag = 1;
		int yflag = 1;

		if (mPosition == Position.LEFT_TOP || mPosition == Position.LEFT_BOTTOM) {
			xflag = -1;
		}
		if (mPosition == Position.LEFT_TOP || mPosition == Position.RIGHT_TOP) {
			yflag = -1;
		}

		Animation translate = null;
		if (mStatus == Status.CLOSE) {	// to open
			translate = AnimHelper.translate(xflag * cl, 0, yflag * ct, 0, duration, startOffset);
			setItemClickable(child, true);
		} else {						// to close
			 //4个值是起始点和结束点,相对于自身x、y的距离
			translate = AnimHelper.translate(0, xflag * cl, 0, yflag * ct, duration, startOffset);
			setItemClickable(child, false);
		}

		translate.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {}
			@Override
			public void onAnimationRepeat(Animation animation) {}
			@Override
			public void onAnimationEnd(Animation animation) {
				if (mStatus == Status.CLOSE) {
					child.setVisibility(View.GONE);
				}
			}
		});
		// 先旋转后平移
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(AnimHelper.rotate(0f, 720f, duration));
		set.addAnimation(translate);
		child.startAnimation(set);
	}
	
	private void setItemClickable(View child, boolean b) {
		child.setClickable(b);
		child.setFocusable(b);
	}
	
	/**
	 * 为子菜单设置点击监听
	 * @param child 子菜单
	 * @param index 子菜单在子菜单项内的索引（排除主菜单）
	 */
	private void setChildMenuClickListener(final View child, final int index) {
		child.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mItemClickListener != null) {
					mItemClickListener.onClick(child, index);
				}
				childMenuAnim(index);
				changeStatus();
				collapseChildMenus();
			}
		});
	}

	/**
	 * 添加menuItem的点击动画
	 * @param pos
	 */
	private void childMenuAnim(int pos) {
		for (int i = 0; i < getChildCount() - 1; i++) {
			View child = getChildAt(i + 1);
			if (i == pos) { // 放大
				child.startAnimation(AnimHelper.setBig(300));
			} else { // 缩小
				child.startAnimation(AnimHelper.setSmall(300));
			}
			setItemClickable(child, false);
		}
	}
	
	/**
	 * 切换菜单状态
	 */
	private void changeStatus() {
		mStatus = (mStatus == Status.CLOSE ? Status.OPEN : Status.CLOSE);
	}
	
	// TODO 下面的方法公开
	/**
	 * 设置主菜单和子菜单按钮的图片资源
	 * @param resId
	 * @param childIds
	 */
	public void setMenuIds(int resId, int[] childIds){
		mMasterResId = resId;
		mChildMenuIds = childIds;
		addMenu();
	}
	/**
	 * 设置主菜单和子菜单按钮的图片资源，并设置布局参数
	 * @param resId
	 * @param masterParams
	 * @param childIds
	 * @param childParams
	 */
	public void setMenuIds(int resId, LayoutParams masterParams, int[] childIds, LayoutParams childParams){
		mMasterResId = resId;
		mMasterParams = masterParams;
		mChildMenuIds = childIds;
		mChildParams = childParams;
		addMenu();
	}
	/**
	 * 设置主菜单和子菜单按钮的图片资源
	 * @param resId
	 * @param bgId
	 * @param childIds
	 */
	public void setMenuIds(int resId, int bgId, int[] childIds){
		mMasterResId = resId;
		mMasterBgId = bgId;
		mChildMenuIds = childIds;
		addMenu();
	}
	/**
	 * 设置主菜单和子菜单按钮的图片资源，并设置布局参数
	 * @param resId
	 * @param bgId
	 * @param masterParams
	 * @param childIds
	 * @param childParams
	 */
	public void setMenuIds(int resId, int bgId, LayoutParams masterParams,
			int[] childIds, LayoutParams childParams) {
		mMasterResId = resId;
		mMasterBgId = bgId;
		mMasterParams = masterParams;
		mChildMenuIds = childIds;
		mChildParams = childParams;
		addMenu();
	}
	
	public boolean isOpen() {
		return mStatus == Status.OPEN;
	}
	
	/**
	 * 设置菜单所处的4个位置
	 * 
	 * @param position
	 *            {@link Position#LEFT_TOP}、{@link Position#LEFT_BOTTOM} 、
	 *            {@link Position#RIGHT_TOP}、{@link Position#RIGHT_BOTTOM}
	 */
	public void setPosition(int position) {
		if (mPosition == position) {
			return;
		}
		this.mPosition = position;
		View child;  
        int count = getChildCount();  
        for (int i = 0; i < count; i++) {  
            child = getChildAt(i);  
            child.clearAnimation();  
        }  
//      invalidate(); //会触发 测量、布局和绘制  
        requestLayout(); //这里只要请求布局  
	}
	
	/**
	 * 展开（显示）子菜单
	 */
	public void expandChildMenus(){
		for (int i = 0; i < getChildCount() - 1; i++) {
			getChildAt(i + 1).setVisibility(VISIBLE);
		}
	}
	/**
	 * 折叠（隐藏）子菜单
	 */
	public void collapseChildMenus(){
		for (int i = 0; i < getChildCount() - 1; i++) {
			getChildAt(i + 1).setVisibility(GONE);
		}
	}
}
