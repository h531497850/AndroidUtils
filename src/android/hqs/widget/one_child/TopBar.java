package android.hqs.widget.one_child;

import com.android.hqs.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.hqs.helper.constant.ColorConstant;
import android.hqs.util.DimenUtil;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TopBar extends RelativeLayout implements View.OnClickListener {
	
	// dp
	private final int DEFAULT_TEXT_SIZE = 20;
	
	private TextView title;
	private String titleText;
	private int titleTextSize;
	private int titleTextColor;
	private LayoutParams titleParams;

	private Button left;
	private final int LEFT_ID = 1;
	private String leftText;
	private int leftTextSize;
	private int leftTextColor;
	private Drawable leftBackground;
	private LayoutParams leftParams;

	private Button right;
	private final int RIGHT_ID = 2;
	private String rightText;
	private int rightTextSize;
	private int rightTextColor;
	private Drawable rightBackground;
	private LayoutParams rightParams;
	
	private onTopBarItemClickListener mItemClickListener;
	public interface onTopBarItemClickListener {
		void onLeftClick();;
		void onRightClick();;
	}
	public void setOnItemClickListener(onTopBarItemClickListener listener){
		mItemClickListener = listener;
	}

	public TopBar(Context context) {
		super(context);
	}

	public TopBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TopBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
		
		titleText = ta.getString(R.styleable.TopBar_title_text);
		titleTextSize = DimenUtil.dipToPx(context, ta.getDimension(
				R.styleable.TopBar_title_text_size, DEFAULT_TEXT_SIZE));
		titleTextColor = ta.getColor(R.styleable.TopBar_title_text_color, ColorConstant.PALE_BLUE);
		
		leftText = ta.getString(R.styleable.TopBar_left_text);
		leftTextSize = DimenUtil.dipToPx(context, ta.getDimension(
				R.styleable.TopBar_left_text_size, DEFAULT_TEXT_SIZE));
		leftTextColor = ta.getColor(R.styleable.TopBar_left_text_color, ColorConstant.PALE_BLUE);
		leftBackground = ta.getDrawable(R.styleable.TopBar_left_background);
		
		rightText = ta.getString(R.styleable.TopBar_right_text);
		rightTextSize = DimenUtil.dipToPx(context, ta.getDimension(
				R.styleable.TopBar_right_text_size, DEFAULT_TEXT_SIZE));
		rightTextColor = ta.getColor(R.styleable.TopBar_right_text_color, ColorConstant.PALE_BLUE);
		rightBackground = ta.getDrawable(R.styleable.TopBar_right_background);
		
		ta.recycle();
		
		if (getBackground() == null) {
			setBackgroundColor(ColorConstant.GRAY);
		}
		
		initView(context);
		
	}

	/**
	 * 向下兼容
	 * @param context
	 */
	@SuppressWarnings("deprecation")
	private void initView(Context context) {
		title = new TextView(context);
		title.setText(titleText);
		title.setTextSize(titleTextSize);
		title.setTextColor(titleTextColor);
		title.setGravity(Gravity.CENTER);
		titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		titleParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);
		addView(title, titleParams);
		
		left = new Button(context);
		//left.setClickable(false);
		left.setId(LEFT_ID);
		left.setText(leftText);
		left.setTextSize(leftTextSize);
		left.setTextColor(leftTextColor);
		if (leftBackground == null) {
			left.setBackgroundColor(ColorConstant.ORANGE);
		} else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				left.setBackground(leftBackground);
			} else {
				left.setBackgroundDrawable(leftBackground);
			}
		}
		leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);
		leftParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
		addView(left, leftParams);
		left.setOnClickListener(this);
		
		right = new Button(context);
		left.setId(RIGHT_ID);
		right.setText(rightText);
		right.setTextSize(rightTextSize);
		right.setTextColor(rightTextColor);
		if (rightBackground == null) {
			right.setBackgroundColor(ColorConstant.ORANGE);
		} else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				right.setBackground(rightBackground);
			} else {
				right.setBackgroundDrawable(rightBackground);
			}
		}
		rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);
		rightParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
		addView(right, rightParams);
		right.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case LEFT_ID:
			if (mItemClickListener != null) {
				mItemClickListener.onLeftClick();
			}
			break;
		case RIGHT_ID:
			if (mItemClickListener != null) {
				mItemClickListener.onRightClick();
			}
			break;
		default:
			break;
		}
		
	}


}
