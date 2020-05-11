package android.hqs.game.pintu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.android.hqs.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hqs.gj.tool.LogTool;
import android.hqs.gj.util.DimenUtil;
import android.hqs.gj.util.ImageConvertUtil;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * <strong>注意：</strong>我们通过设置背景图片来设置游戏图片
 * @author hqs2063594
 *
 */
public class GamePintuLayout extends RelativeLayout implements OnClickListener {
	private static final String TAG = LogTool.makeTag(GamePintuLayout.class);

	private int mColumn = 3;
	/** 容器的内边距 */
	private int mPadding;
	/** 每张小图之间的距离（横、纵） dp  */
	private int mMargin = 3;

	private ImageView[] mGamePintuItems;

	private int mItemWidth;

	/** 游戏的图片 */
	private Bitmap mGameBmp;
	/** 游戏资源图片 */
	private Drawable mGameDrawable;

	private List<ImageInfo> mItemBmps;

	private boolean once;

	/** 游戏面板的宽度 */
	private int mWidth;

	private boolean isGameSuccess;
	private boolean isGameOver;

	public interface GamePintuListener {
		void nextLevel(int nextLevel);
		void timeChanged(int currentTime);
		void gameOver();
	}

	private GamePintuListener mListener;
	/**
	 * 设置接口回调
	 * 
	 * @param mListener
	 */
	public void setOnGamePintuListener(GamePintuListener mListener) {
		this.mListener = mListener;
	}
	
	public class ImageInfo {
		private int index;
		private Bitmap bitmap;

		public ImageInfo() {}

		public ImageInfo(int index, Bitmap bitmap) {
			this.index = index;
			this.bitmap = bitmap;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public Bitmap getBitmap() {
			return bitmap;
		}

		public void setBitmap(Bitmap bitmap) {
			this.bitmap = bitmap;
		}

		@Override
		public String toString() {
			return "ImageBean [index=" + index + ", bitmap=" + bitmap + "]";
		}
	}

	private int mLevel = 1;
	private static final int TIME_CHANGED = 0x110;
	private static final int NEXT_LEVEL = 0x111;

	private Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case TIME_CHANGED:
				if (isGameSuccess || isGameOver || isPause)
					return true;
				if (mListener != null) {
					mListener.timeChanged(mTime);
				}
				if (mTime == 0) {
					isGameOver = true;
					mListener.gameOver();
					return true;
				}
				mTime--;
				mHandler.sendEmptyMessageDelayed(TIME_CHANGED, 1000);
				return true;
			case NEXT_LEVEL:
				mLevel = mLevel + 1;
				if (mListener != null) {
					mListener.nextLevel(mLevel);
				} else {
					nextLevel();
				}
				return true;
			}
			return false;
		}
	});

	private boolean isTimeEnabled = false;
	private int mTime;

	/**
	 * 设置是否开启时间
	 * 
	 * @param isTimeEnabled
	 */
	public void setTimeEnabled(boolean isTimeEnabled) {
		this.isTimeEnabled = isTimeEnabled;
	}

	public GamePintuLayout(Context context) {
		this(context, null);
	}

	public GamePintuLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GamePintuLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GamePintuLayout);
		mGameDrawable = ta.getDrawable(R.styleable.GamePintuLayout_game_src);
		ta.recycle();
		
		init(context);
	}

	private void init(Context context) {
		mMargin = DimenUtil.dipToPx(context, 3);
		mPadding = min(getPaddingLeft(), getPaddingRight(), getPaddingTop(),
				getPaddingBottom());
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 取宽和高中的小值
		mWidth = Math.min(getMeasuredHeight(), getMeasuredWidth());
		
		if (!once) {
			// 进行切图，以及排序
			initBitmap();
			// 设置ImageView(Item)的宽高等属性
			initItem();
			// 判断是否开启时间
			checkTimeEnable();

			once = true;
		}

		setMeasuredDimension(mWidth, mWidth);

	}

	private void checkTimeEnable() {
		if (isTimeEnabled) {
			// 根据当前等级设置时间
			countTimeBaseLevel();
			mHandler.sendEmptyMessage(TIME_CHANGED);
		}
	}

	private void countTimeBaseLevel() {
		mTime = (int) Math.pow(2, mLevel) * 60;
	}

	/**
	 * 进行切图，以及排序
	 */
	private void initBitmap() {
		/*if (mGameBmp == null) {
			mGameBmp = ImageConvertUtil.drawableToBitmap(getBackground());
			//getBackground().setVisible(false, false);
			getBackground().setAlpha(0);
			mBitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.pic_game_pintu);
		}*/
		
		if (mGameBmp == null) {
			if (mGameDrawable == null) {
				//throw new NullPointerException("Game image can not be null!");
				Log.e(TAG, "Game image can not be null!");
				return;
			} else {
				setGamePicture(ImageConvertUtil.drawableToBitmap(mGameDrawable));
			}
		}
		if (mGameBmp == null) {
			return;
		}
		mItemBmps = splitImage(mGameBmp, mColumn);

		// 使用sort完成我们的乱序
		Collections.sort(mItemBmps, new Comparator<ImageInfo>() {
			@Override
			public int compare(ImageInfo a, ImageInfo b) {
				return Math.random() > 0.5 ? 1 : -1;
			}
		});
	}
	
	/**
	 * @param bitmap
	 * @param piece
	 *            切成piece*piece块
	 * @return List<ImageBean>
	 */
	private List<ImageInfo> splitImage(Bitmap bitmap, int piece) {
		List<ImageInfo> beans = new ArrayList<ImageInfo>();
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int pieceWidth = Math.min(w, h) / piece;
		for (int i = 0; i < piece; i++) {
			for (int j = 0; j < piece; j++) {
				ImageInfo bean = new ImageInfo();
				bean.setIndex(j + i * piece);
				int x = j * pieceWidth;
				int y = i * pieceWidth;
				bean.setBitmap(Bitmap.createBitmap(bitmap, x, y,
						pieceWidth, pieceWidth));
				beans.add(bean);
			}
		}
		return beans;
	}

	/**
	 * 设置ImageView(Item)的宽高等属性
	 */
	private void initItem() {
		if (mGameBmp == null) {
			return;
		}
		mItemWidth = (mWidth - mPadding * 2 - mMargin * (mColumn - 1))
				/ mColumn;
		mGamePintuItems = new ImageView[mColumn * mColumn];
		// 生成我们的Item，设置Rule
		for (int i = 0; i < mGamePintuItems.length; i++) {
			ImageView item = new ImageView(getContext());
			item.setOnClickListener(this);
			item.setImageBitmap(mItemBmps.get(i).getBitmap());

			mGamePintuItems[i] = item;
			item.setId(i + 1);

			// 在Item的tag中存储了index
			item.setTag(i + "_" + mItemBmps.get(i).getIndex());

			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					mItemWidth, mItemWidth);

			// 设置Item间横向间隙，通过rightMargin
			// 不是最后一列
			if ((i + 1) % mColumn != 0) {
				lp.rightMargin = mMargin;
			}
			// 不是第一列
			if (i % mColumn != 0) {
				lp.addRule(RelativeLayout.RIGHT_OF,
						mGamePintuItems[i - 1].getId());
			}
			// 如果不是第一行 , 设置topMargin和rule
			if ((i + 1) > mColumn) {
				lp.topMargin = mMargin;
				lp.addRule(RelativeLayout.BELOW,
						mGamePintuItems[i - mColumn].getId());
			}
			addView(item, lp);
		}

	}

	public void restart() {
		isGameOver = false;
		mColumn--;
		nextLevel();
	}

	private boolean isPause;

	public void pause() {
		isPause = true;
		mHandler.removeMessages(TIME_CHANGED);
	}

	public void resume() {
		if (isPause) {
			isPause = false;
			mHandler.sendEmptyMessage(TIME_CHANGED);
		}
	}

	public void nextLevel() {
		this.removeAllViews();
		mAnimLayout = null;
		mColumn++;
		isGameSuccess = false;
		checkTimeEnable();
		initBitmap();
		initItem();
	}

	/**
	 * 获取多个参数的最小值
	 */
	private int min(int... params) {
		int min = params[0];

		for (int param : params) {
			if (param < min)
				min = param;
		}
		return min;
	}

	private ImageView mFirst;
	private ImageView mSecond;

	@Override
	public void onClick(View v) {
		if (isAniming)
			return;

		// 两次点击同一个Item
		if (mFirst == v) {
			mFirst.setColorFilter(null);
			mFirst = null;
			return;
		}
		if (mFirst == null) {
			mFirst = (ImageView) v;
			mFirst.setColorFilter(Color.parseColor("#55FF0000"));
		} else {
			mSecond = (ImageView) v;
			// 交换我们的Item
			exchangeView();
		}

	}

	/**
	 * 动画层
	 */
	private RelativeLayout mAnimLayout;
	private boolean isAniming;
	
	private ImageView copyPieceView(ImageView src, Bitmap bmp){
		ImageView iv = new ImageView(getContext());
		LayoutParams lp = new LayoutParams(mItemWidth, mItemWidth);
		lp.leftMargin = src.getLeft() - mPadding;
		lp.topMargin = src.getTop() - mPadding;
		iv.setLayoutParams(lp);
		iv.setImageBitmap(bmp);
		return iv;
	}

	/**
	 * 交换我们的Item
	 */
	private void exchangeView() {
		mFirst.setColorFilter(null);
		// 构造我们的动画层
		setUpAnimLayout();

		final Bitmap firstBitmap = mItemBmps.get(
				getImageIdByTag((String) mFirst.getTag())).getBitmap();
		ImageView first = copyPieceView(mFirst, firstBitmap);
		mAnimLayout.addView(first);

		final Bitmap secondBitmap = mItemBmps.get(
				getImageIdByTag((String) mSecond.getTag())).getBitmap();
		ImageView second = copyPieceView(mSecond, secondBitmap);
		mAnimLayout.addView(second);

		// 设置动画
		TranslateAnimation anim = new TranslateAnimation(0, mSecond.getLeft()
				- mFirst.getLeft(), 0, mSecond.getTop() - mFirst.getTop());
		anim.setDuration(300);
		anim.setFillAfter(true);
		first.startAnimation(anim);

		TranslateAnimation animSecond = new TranslateAnimation(0,
				-mSecond.getLeft() + mFirst.getLeft(), 0, -mSecond.getTop()
						+ mFirst.getTop());
		animSecond.setDuration(300);
		animSecond.setFillAfter(true);
		second.startAnimation(animSecond);

		// 监听动画
		anim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				mFirst.setVisibility(View.INVISIBLE);
				mSecond.setVisibility(View.INVISIBLE);

				isAniming = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {

				String firstTag = (String) mFirst.getTag();
				String secondTag = (String) mSecond.getTag();

				mFirst.setImageBitmap(secondBitmap);
				mSecond.setImageBitmap(firstBitmap);

				mFirst.setTag(secondTag);
				mSecond.setTag(firstTag);

				mFirst.setVisibility(View.VISIBLE);
				mSecond.setVisibility(View.VISIBLE);

				// 断开引用，但内存中还存在
				mFirst = mSecond = null;
				mAnimLayout.removeAllViews();
				// 判断用户游戏是否成功
				checkSuccess();
				isAniming = false;
			}
		});

	}

	/**
	 * 判断用户游戏是否成功
	 */
	private void checkSuccess() {
		boolean isSuccess = true;

		for (int i = 0; i < mGamePintuItems.length; i++) {
			ImageView imageView = mGamePintuItems[i];
			if (getImageIndexByTag((String) imageView.getTag()) != i) {
				isSuccess = false;
			}
		}

		if (isSuccess) {
			isGameSuccess = true;
			mHandler.removeMessages(TIME_CHANGED);

			Toast.makeText(getContext(), "Success ， level up !!!",
					Toast.LENGTH_LONG).show();
			mHandler.sendEmptyMessage(NEXT_LEVEL);
		}

	}

	/**
	 * 根据tag获取Id
	 * 
	 * @param tag
	 * @return
	 */
	private int getImageIdByTag(String tag) {
		String[] split = tag.split("_");
		return Integer.parseInt(split[0]);
	}

	private int getImageIndexByTag(String tag) {
		String[] split = tag.split("_");
		return Integer.parseInt(split[1]);
	}

	/**
	 * 构造我们的动画层
	 */
	private void setUpAnimLayout() {
		if (mAnimLayout == null) {
			mAnimLayout = new RelativeLayout(getContext());
			addView(mAnimLayout);
		}
	}
	
	public void setGamePicture(int resId){
		mGameDrawable = getContext().getResources().getDrawable(resId);
	}
	
	public void setGamePicture(Bitmap bmp){
		mGameBmp = bmp;
	}

}
