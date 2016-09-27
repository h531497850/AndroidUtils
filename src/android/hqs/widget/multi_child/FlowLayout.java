package android.hqs.widget.multi_child;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 流式布局。 特点、应用场景。
 * 
 * <ul>
 * 思路：
 * <li>{@link #onMeasure(int, int)}测量子view的宽高，设置自己的宽高；</li>
 * <li>{@link #onLayout(boolean, int, int, int, int)}设置子view的位置；</li>
 * <li>{@link #onMeasure(int, int)}根据子view的布局文件，为子view设置设置测量模式和测量值；</li>
 * <li>测量 = 测量模式 + 测量值；</li>
 * <li>测量模式：<br>
 * 1、{@link MeasureSpec#EXACTLY}(精确值)：100dp，match_parent<br>
 * 2、{@link MeasureSpec#AT_MOST}(自适应)：wrap_content<br>
 * 3、{@link MeasureSpec#UNSPECIFIED}：子控件想要多大就多大，很少见。</li>
 * <li>ViewGroup --- LayoutParams。<br>
 * 子view.getLayoutParams为该子view的父控件的LayoutParams；</li>
 * <li>FlowLayout --- {@link #ViewGroup.MarginLayoutParams}</li>
 * </ul>
 * 
 * @author hqs2063594
 * 
 */
public class FlowLayout extends ViewGroup {

	/**
	 * 在代码中创建实例时调用。
	 * @param context
	 */
	public FlowLayout(Context context) {
		super(context);
		init(context);
	}
	/**
	 * 在没有自定义属性的布局文件中会调用该方法。
	 * @param context
	 * @param attrs
	 */
	public FlowLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	/**
	 * 在有自定义属性的布局文件中会调用该方法。
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 这里获取精确宽高
		int sw = MeasureSpec.getSize(widthMeasureSpec);
		int mw = MeasureSpec.getMode(widthMeasureSpec);
		int sh = MeasureSpec.getSize(heightMeasureSpec);
		int mh = MeasureSpec.getSize(heightMeasureSpec);
		
		// wrap_content
		int width = 0;
		int height = 0;
		
		// 记录每行的宽高
		int lineWidth = 0;
		int lineHeight = 0;
		
		// 得到内部元素的个数
		int cc = getChildCount();
		int index = 0;
		for (index=0; index < cc; index++) {
			View child = getChildAt(index);
			// 测量子view宽高
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			// 得到LayoutParams
			MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
			
			// 子view占据的宽度
			int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
			// 子view占据的高度
			int childHeight = child.getMeasuredWidth() + lp.topMargin + lp.bottomMargin;
			
			// 新添加子view时超过行宽度（换行）
			if (lineWidth + childWidth > sw - (getPaddingLeft()+getPaddingRight())) {
				// 对比得到行的最大宽度
				width = Math.max(width, lineWidth);
				// 重置lineWidth
				lineWidth = childWidth;
				// 记录行高
				height += lineHeight;
				lineHeight = childHeight;
			} else {
				// 叠加行宽
				lineWidth += childWidth;
				// 得到当前行最大高度
				lineHeight = Math.max(lineHeight, childHeight);
			}
		}
		// 最后一个控件
		if (index == cc) {
			// 对比得到行的最大宽度
			width = Math.max(width, lineWidth);
			// 记录行高
			height += lineHeight;
		}
		
		setMeasuredDimension(
				mw == MeasureSpec.EXACTLY ? sw : width + getPaddingLeft()+getPaddingRight(),
				mh == MeasureSpec.EXACTLY ? sh : height + getPaddingTop() + getPaddingBottom());
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/** 整个布局内的所有控件，外部List的对象是每行的控件列表 */
	private List<List<View>> mAllViews = new ArrayList<List<View>>();
	/** 每行的高度 */
	private List<Integer> mLineHeights = new ArrayList<Integer>();
	/** 每行所有Views */
	private List<View> mLineViews = new ArrayList<View>();
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {
			mAllViews.clear();
			mLineHeights.clear();
			mLineViews.clear();
			
			// 当前FlowLayout的高度
			int width = getWidth();
			
			int lineWidth = 0;
			int lineHeight = 0;
			
			int cc = getChildCount();
			
			for (int i = 0; i < cc; i++) {
				View child = getChildAt(i);
				// 得到LayoutParams
				MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

				// 子view占据的宽度
				int childWidth = child.getMeasuredWidth();
				// 子view占据的高度
				int childHeight = child.getMeasuredWidth();
				
				// 如果需要换行
				if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width - (getPaddingLeft()+getPaddingRight())) {
					// 记录LineHeight
					mLineHeights.add(lineHeight);
					// 记录当前行的Views
					mAllViews.add(mLineViews);
					
					// 重置我们的行宽高
					lineWidth = 0;
					lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
					// 重置我们的View集合
					mLineViews = new ArrayList<View>();
				}
				
				lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
				lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
				mLineViews.add(child);
			}
			
			// 处理最后一行
			mLineHeights.add(lineHeight);
			mAllViews.add(mLineViews);
			
			// 设置子View的位置
			
			int left = getPaddingLeft();
			int top = getPaddingTop();
			
			// 行数
			int lineNum = mAllViews.size();
			for (int i = 0; i < lineNum; i++) {
				// 当前行所有View
				mLineViews = mAllViews.get(i);
				lineHeight = mLineHeights.get(i);
				
				for (int j = 0; j < mLineViews.size(); j++) {
					View child = mLineViews.get(j);
					// 判断child的状态
					if (child.getVisibility() == GONE) {
						continue;
					}
					MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
					int lc = left + lp.leftMargin;
					int tc = top + lp.topMargin;
					int rc = lc + child.getMeasuredWidth();
					int bc = tc + child.getMeasuredHeight();
					
					// 为子View布局
					child.layout(lc, tc, rc, bc);
					
					left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
				}
				
				left = getPaddingLeft();
				top += lineHeight;
			}
		}
	}
	
	/**
	 * 与当前{@link #FlowLayout}对应的LayoutParams
	 */
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(), attrs);
	}

}
