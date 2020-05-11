package android.hqs.widget.dynamic;

import com.android.hqs.R;
import android.content.Context;
import android.hqs.gj.util.FormatUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 向下拖动刷新数据
 * 1、正往下拖动，显示的数据：向下箭头、向下拖拽可以刷新数据、上一次刷新数据的时间
 * 2、拖拽成功，显示的数据：正在加载进度条、正在刷新、上一次刷新数据的时间
 * 3、未拖拽成功就松开或向上拖拽，显示的数据：向上箭头、向下拖拽可以刷新数据、上一次刷新数据的时间
 * @author hqs2063594
 *
 */
public class DragDownListView extends ListView implements OnScrollListener {

	/** 底部正在加载布局 */
	private View headView;
	private ImageView iv_arrow;
	private ProgressBar pb;
	private TextView tv_tip;
	private TextView tv_time;
	
	/** 顶部布局文件的高度 */
	private int headHeight;
	
	/** 当前第一个可见的Item */
	private int firstVisibleItem;
	/** 标记，当前是否是在ListView最顶端向下拖拽 */
	private boolean isRemark;
	/** 向下拖拽时的Y坐标 */
	private int startY;
	
	private static final class RefreshState{
		/** 正常状态 */
		public static final int NONE = 0;
		/** 提示下来 */
		public static final int PULL = 1;
		/** 提示释放状态 */
		public static final int RELESE = 2;
		/** 刷新状态 */
		public static final int REFASHING = 3;
	}
	/** 当前状态 */
	private int state = RefreshState.NONE;
	private int scrollState;
	
	private RotateAnimation upToDown;
	private RotateAnimation downToUp;
	
	private IReflashListener mReflashListener;
	/**
	 * 设置加载更多数据的回调接口
	 * @param listener
	 */
	public void setReflashListener(IReflashListener listener) {
		this.mReflashListener = listener;
	}
	/**
	 * 加载更多数据的回调接口
	 * @author hqs2063594
	 *
	 */
	public interface IReflashListener{
		/**
		 * 获取最新数据
		 * 通知界面显示
		 * 通知ListView刷新数据完毕
		 */
		void onReflash();
	}
	
	public DragDownListView(Context context) {
		this(context, null);
	}
	public DragDownListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public DragDownListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		setOnScrollListener(this);
	}
	
	/**
	 * 添加底部加载提示布局到ListView
	 * @param layoutId
	 */
	public void initHeadView(int layoutId) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		headView = inflater.inflate(layoutId, this, false);
		iv_arrow = (ImageView) headView.findViewById(R.id.pdd_iv_arrow);
		pb = (ProgressBar) headView.findViewById(R.id.pdd_pb);
		tv_tip = (TextView) headView.findViewById(R.id.pdd_tv_tip);
		tv_time = (TextView) headView.findViewById(R.id.pdd_tv_time);
		
		upToDown = new RotateAnimation(0, 180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		upToDown.setDuration(500);
		upToDown.setFillAfter(true);
		
		downToUp = new RotateAnimation(180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		downToUp.setDuration(500);
		downToUp.setFillAfter(true);
		
		measureView(headView);
		headHeight = headView.getMeasuredHeight();
		topPadding(-headHeight);
		addHeaderView(headView);
	}
	
	public void reflashComplete(){
		state = RefreshState.NONE;
		isRemark = false;
		reflashViewByState();
		tv_time.setText(FormatUtil.time(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState = scrollState;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstVisibleItem = firstVisibleItem;

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (firstVisibleItem == 0) {
				isRemark = true;
				startY = (int) ev.getY();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			onMove(ev);
			break;
		case MotionEvent.ACTION_UP:
			if (state == RefreshState.RELESE) {
				state = RefreshState.REFASHING;
				reflashViewByState();
				if (mReflashListener != null) {
					mReflashListener.onReflash();
				}
			} else if (state == RefreshState.PULL) {
				state = RefreshState.NONE;
				reflashViewByState();
				isRemark = false;
			}
			break;
		default:
			break;
		}
		
		return super.onTouchEvent(ev);
	}
	private void onMove(MotionEvent ev) {
		if (!isRemark) {
			return;
		}
		
		int tempY = (int) ev.getY();
		int space = tempY - startY;
		int topPadding = space - headHeight;
		switch (state) {
		case RefreshState.NONE:
			if (space > 0) {
				state = RefreshState.PULL;
				reflashViewByState();
			}
			break;
		case RefreshState.PULL:
			topPadding(topPadding);
			if (space > headHeight + 30 && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
				state = RefreshState.RELESE;
				reflashViewByState();
			}
			break;
		case RefreshState.RELESE:
			topPadding(topPadding);
			if (space < headHeight + 30) {
				state = RefreshState.PULL;
				reflashViewByState();
			} else if (space <= 0) {
				state = RefreshState.NONE;
				reflashViewByState();
				isRemark = false;
			}
			break;
		case RefreshState.REFASHING:
			
			break;

		default:
			break;
		}
	}
	
	/**
	 * 根据当前状态刷新上边布局
	 */
	private void reflashViewByState(){
		if (headView == null) {
			return;
		}
		switch (state) {
		case RefreshState.NONE:
			iv_arrow.clearAnimation();
			topPadding(-headHeight);
			break;
		case RefreshState.PULL:
			iv_arrow.setVisibility(VISIBLE);
			pb.setVisibility(GONE);
			tv_tip.setText("下拉可刷新！");
			iv_arrow.clearAnimation();
			iv_arrow.setAnimation(downToUp);
			break;
		case RefreshState.RELESE:
			iv_arrow.setVisibility(GONE);
			pb.setVisibility(GONE);
			tv_tip.setText("松开可刷新！");
			iv_arrow.clearAnimation();
			iv_arrow.setAnimation(upToDown);
			break;
		case RefreshState.REFASHING:
			topPadding(headHeight);
			iv_arrow.setVisibility(GONE);
			pb.setVisibility(VISIBLE);
			tv_tip.setText("正在刷新...");
			break;

		default:
			break;
		}
	}
	
	/**
	 * 设置顶部布局的上边距
	 * @param padding
	 */
	private void topPadding(int padding){
		if (headView == null) {
			return;
		}
		headView.setPadding(headView.getPaddingLeft(), padding,
				headView.getPaddingRight(), headView.getPaddingBottom());
		headView.invalidate();
	}
	/**
	 * 通知父布局占用宽、高
	 * @param v
	 */
	private void measureView(View v){
		ViewGroup.LayoutParams lp = v.getLayoutParams();
		if (lp == null) {
			lp = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int widdth = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
		int height;
		int tempHeight = lp.height;
		if (tempHeight > 0) {
			height = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
		} else {
			height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		v.measure(widdth, height);
	}

}
