package android.hqs.widget.img;

import com.android.hqs.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.hqs.gj.util.DimenUtil;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 裁剪图片
 * @author 胡青松
 */
public class RoundImageView extends ImageView {
	
	private Paint mRoundPaint;
	// 默认50dp
	private int roundWidth = 50;
	private int roundHeight = 50;
	private Paint paint2;

	public RoundImageView(Context context) {
		super(context);
	}

	public RoundImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.RoundImageView);
		roundWidth = DimenUtil.dpToPx(context, ta.getDimensionPixelSize(
				R.styleable.RoundImageView_round_width, roundWidth));
		roundHeight = DimenUtil.dpToPx(context, ta.getDimensionPixelSize(
				R.styleable.RoundImageView_round_height, roundHeight));
		ta.recycle();

		init();
	}

	private void init() {
		mRoundPaint = new Paint();
		mRoundPaint.setColor(Color.WHITE);
		mRoundPaint.setAntiAlias(true);
		mRoundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

		paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint2.setXfermode(null);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 注意这里是PX，如果设置的为dp将转换为px
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		//Log.e("RoundImageView", "onMeasure --> width = " + width + ", height = " + height);
		// 如果设定的弧度宽高大于控件宽高的一半，强制将弧度宽高改为控件宽高一半的小值。
		if (width > height) {
			if (height / 2 < roundHeight) {
				roundWidth = roundHeight = height / 2;
			}
		} else {
			if (width / 2 < roundWidth) {
				roundWidth = roundHeight = width / 2;
			}
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public void draw(Canvas canvas) {
		Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
				Config.ARGB_8888);
		Canvas bmpCan = new Canvas(bitmap);
		super.draw(bmpCan);
		
		drawLeftTop(bmpCan);
		drawRightTop(bmpCan);
		drawLeftBottom(bmpCan);
		drawRightBottom(bmpCan);
		
		canvas.drawBitmap(bitmap, 0, 0, paint2);
		bitmap = null;
	}

	private void drawLeftTop(Canvas canvas) {
		Path path = new Path();
		path.moveTo(0, roundHeight);
		path.lineTo(0, 0);
		path.lineTo(roundWidth, 0);
		path.arcTo(new RectF(0, 0, roundWidth, roundHeight), -90, -90);
		path.close();
		canvas.drawPath(path, mRoundPaint);
	}

	private void drawLeftBottom(Canvas canvas) {
		Path path = new Path();
		path.moveTo(0, getHeight() - roundHeight);
		path.lineTo(0, getHeight());
		path.lineTo(roundWidth, getHeight());
		path.arcTo(new RectF(0, getHeight() - roundHeight,
				0 + roundWidth, getHeight()), 90, 90);
		path.close();
		canvas.drawPath(path, mRoundPaint);
	}

	private void drawRightBottom(Canvas canvas) {
		Path path = new Path();
		path.moveTo(getWidth() - roundWidth, getHeight());
		path.lineTo(getWidth(), getHeight());
		path.lineTo(getWidth(), getHeight() - roundHeight);
		path.arcTo(new RectF(getWidth() - roundWidth, getHeight()
				- roundHeight, getWidth(), getHeight()), 0, 90);
		path.close();
		canvas.drawPath(path, mRoundPaint);
	}

	private void drawRightTop(Canvas canvas) {
		Path path = new Path();
		path.moveTo(getWidth(), roundHeight);
		path.lineTo(getWidth(), 0);
		path.lineTo(getWidth() - roundWidth, 0);
		path.arcTo(new RectF(getWidth() - roundWidth, 0, getWidth(),
				0 + roundHeight), -90, 90);
		path.close();
		canvas.drawPath(path, mRoundPaint);
	}
	
	/**
	 * 設置弧度
	 * @param roundWidth 单位dp
	 * @param roundHeight 单位dp
	 */
	public void SetRoundValue(int roundWidth, int roundHeight) {
		this.roundWidth = DimenUtil.dpToPx(getContext(), roundWidth);
		this.roundHeight = DimenUtil.dpToPx(getContext(), roundHeight);
	}

}