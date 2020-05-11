package android.hqs.widget.siyamed;

import com.android.hqs.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 给Android开发的卫星菜单组件。
 * 
 * TODO: 请看
 * 
 * @author Siyamed SINIR
 * 
 */
public class SatelliteMenu extends FrameLayout {

	private static final int DEFAULT_SATELLITE_DISTANCE = 200;
	private static final float DEFAULT_TOTAL_SPACING_DEGREES = 90f;
	private static final boolean DEFAULT_CLOSE_ON_CLICK = true;
	private static final int DEFAULT_EXPAND_DURATION = 400;

	private Animation mainRotateRight;
	private Animation mainRotateLeft;

	private ImageView imgMain;
	private SateliteItemClickedListener itemClickedListener;
	private View.OnClickListener internalItemClickListener;

	private List<SatelliteMenuInfo> menuItems = new ArrayList<SatelliteMenuInfo>();
	private Map<View, SatelliteMenuInfo> viewToItemMap = new HashMap<View, SatelliteMenuInfo>();

	private AtomicBoolean plusAnimationActive = new AtomicBoolean(false);

	// 如何保存/恢复
	private IDegreeProvider gapDegreesProvider = new DefaultDegreeProvider();

	//这些变量的状态是否被保存
	private boolean rotated = false;
	private int measureDiff = 0;
	//这些变量的状态被保存 - 也可以从XML查看配置
	private float totalSpacingDegree = DEFAULT_TOTAL_SPACING_DEGREES;
	private int satelliteDistance = DEFAULT_SATELLITE_DISTANCE;	
	private int expandDuration = DEFAULT_EXPAND_DURATION;
	private boolean closeItemsOnClick = DEFAULT_CLOSE_ON_CLICK;

	public SatelliteMenu(Context context) {
		super(context);
		init(context, null, 0);
	}

	public SatelliteMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SatelliteMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	private void init(Context context, AttributeSet attrs, int defStyle) {
		//LayoutInflater.from(context).inflate(R.layout.sat_main, this, true);
		
		menuParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		menuParams.gravity = Gravity.LEFT | Gravity.BOTTOM;
		
		imgMain = createMainView(R.drawable.img_widget_sat_main, R.drawable.img_widget_sat_main);

		if(attrs != null){			
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SatelliteMenu, defStyle, 0);					
			satelliteDistance = typedArray.getDimensionPixelSize(R.styleable.SatelliteMenu_satellite_distance, DEFAULT_SATELLITE_DISTANCE);
			totalSpacingDegree = typedArray.getFloat(R.styleable.SatelliteMenu_total_spacing_degree, DEFAULT_TOTAL_SPACING_DEGREES);
			closeItemsOnClick = typedArray.getBoolean(R.styleable.SatelliteMenu_close_onclick, DEFAULT_CLOSE_ON_CLICK);
			expandDuration = typedArray.getInt(R.styleable.SatelliteMenu_expand_duration, DEFAULT_EXPAND_DURATION);
			//float satelliteDistance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 170, getResources().getDisplayMetrics());
			typedArray.recycle();
		}
		
		
		mainRotateLeft = SatelliteAnimCreator.createMainButtonAnimation();
		mainRotateRight = SatelliteAnimCreator.createMainButtonInverseAnimation();

		Animation.AnimationListener plusAnimationListener = new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				plusAnimationActive.set(false);
			}
		};

		mainRotateLeft.setAnimationListener(plusAnimationListener);
		mainRotateRight.setAnimationListener(plusAnimationListener);

		imgMain.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SatelliteMenu.this.onClick();
			}
		});

		internalItemClickListener = new InternalSatelliteOnClickListener(this);
	}

	private void onClick() {
		if (plusAnimationActive.compareAndSet(false, true)) {
			if (!rotated) {
				imgMain.startAnimation(mainRotateLeft);
				for (SatelliteMenuInfo item : menuItems) {
					item.getView().startAnimation(item.getOutAnimation());
				}
			} else {
				imgMain.startAnimation(mainRotateRight);
				for (SatelliteMenuInfo item : menuItems) {
					item.getView().startAnimation(item.getInAnimation());
				}
			}
			rotated = !rotated;
		}
	}
	
	private LayoutParams menuParams;
	private ImageView createMainView(int resId, int id){
		ImageView item = new ImageView(getContext());
		menuParams.leftMargin = 0;
		menuParams.bottomMargin = 0;
		item.setLayoutParams(menuParams);
		item.setId(id);
		item.setImageResource(resId);
		item.setVisibility(VISIBLE);
		return item;
	}
	private ImageView createItemView(int finalX, int finalY, int resId, int id){
		ImageView item = new ImageView(getContext());
		menuParams.leftMargin = Math.abs(finalX);
		menuParams.bottomMargin = Math.abs(finalY);
		item.setLayoutParams(menuParams);
		item.setId(id);
		item.setImageResource(resId);
		item.setVisibility(GONE);
		return item;
	}
	private ImageView createCloneView(int resId, int id){
		ImageView item = new ImageView(getContext());
		menuParams.leftMargin = 0;
		menuParams.bottomMargin = 0;
		item.setLayoutParams(menuParams);
		item.setId(id);
		item.setImageResource(resId);
		item.setVisibility(GONE);
		return item;
	}

	private float[] getDegrees(int count) {
		return gapDegreesProvider.getDegrees(count, totalSpacingDegree);
	}

	private void recalculateMeasureDiff() {
		int itemWidth = 0;
		if (menuItems.size() > 0) {
			itemWidth = menuItems.get(0).getView().getWidth();
		}
		measureDiff = Float.valueOf(satelliteDistance * 0.2f).intValue()
				+ itemWidth;
	}

	private void resetItems() {
		if (menuItems.size() > 0) {
			List<SatelliteMenuInfo> items = new ArrayList<SatelliteMenuInfo>(
					menuItems);
			menuItems.clear();
			this.removeAllViews();
			addItems(items);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		recalculateMeasureDiff();

		int totalHeight = imgMain.getHeight() + satelliteDistance + measureDiff;
		int totalWidth = imgMain.getWidth() + satelliteDistance + measureDiff;
		setMeasuredDimension(totalWidth, totalHeight);
	}

	private static class SatelliteItemClickAnimationListener implements Animation.AnimationListener {
		private WeakReference<SatelliteMenu> menuRef;
		private int tag;
		
		public SatelliteItemClickAnimationListener(SatelliteMenu menu, int tag) {
			this.menuRef = new WeakReference<SatelliteMenu>(menu);
			this.tag = tag;
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) {
		}
		
		@Override
		public void onAnimationStart(Animation animation) {
			SatelliteMenu menu = menuRef.get();
			if(menu != null && menu.closeItemsOnClick){
				menu.collapseItems();
				if(menu.itemClickedListener != null){
					menu.itemClickedListener.eventOccured(tag);
				}
			}
		}		
	}
	
	private static class SatelliteAnimationListener implements Animation.AnimationListener {
		private WeakReference<View> viewRef;
		private boolean isInAnimation;
		private Map<View, SatelliteMenuInfo> viewToItemMap;

		public SatelliteAnimationListener(View view, boolean isIn, Map<View, SatelliteMenuInfo> viewToItemMap) {
			this.viewRef = new WeakReference<View>(view);
			this.isInAnimation = isIn;
			this.viewToItemMap = viewToItemMap;
		}

		@Override
		public void onAnimationStart(Animation animation) {
			if (viewRef != null) {
				View view = viewRef.get();
				if (view != null) {
					SatelliteMenuInfo menuItem = viewToItemMap.get(view);
					if (isInAnimation) {
						menuItem.getView().setVisibility(View.VISIBLE);
						menuItem.getCloneView().setVisibility(View.GONE);
					} else {
						menuItem.getCloneView().setVisibility(View.GONE);
						menuItem.getView().setVisibility(View.VISIBLE);
					}
				}
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			if (viewRef != null) {
				View view = viewRef.get();
				if (view != null) {
					SatelliteMenuInfo menuItem = viewToItemMap.get(view);

					if (isInAnimation) {
						menuItem.getView().setVisibility(View.GONE);
						menuItem.getCloneView().setVisibility(View.GONE);
					} else {
						menuItem.getCloneView().setVisibility(View.VISIBLE);
						menuItem.getView().setVisibility(View.GONE);
					}
				}
			}
		}
	}

	private static class InternalSatelliteOnClickListener implements View.OnClickListener {
		private WeakReference<SatelliteMenu> menuRef;
		
		public InternalSatelliteOnClickListener(SatelliteMenu menu) {
			this.menuRef = new WeakReference<SatelliteMenu>(menu);
		}

		@Override
		public void onClick(View v) {
			SatelliteMenu menu = menuRef.get();
			if(menu != null){
				SatelliteMenuInfo menuItem = menu.getViewToItemMap().get(v);
				v.startAnimation(menuItem.getClickAnimation());	
			}
		}
	}
	
	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
		ss.rotated = rotated;
		ss.totalSpacingDegree = totalSpacingDegree;
		ss.satelliteDistance = satelliteDistance;
		ss.measureDiff = measureDiff;
		ss.expandDuration = expandDuration;
		ss.closeItemsOnClick = closeItemsOnClick;
		return ss;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		rotated = ss.rotated;
		totalSpacingDegree = ss.totalSpacingDegree;
		satelliteDistance = ss.satelliteDistance;
		measureDiff = ss.measureDiff;
		expandDuration = ss.expandDuration;
		closeItemsOnClick = ss.closeItemsOnClick;

		super.onRestoreInstanceState(ss.getSuperState());
	}

	static class SavedState extends BaseSavedState {
		boolean rotated;
		private float totalSpacingDegree;
		private int satelliteDistance;
		private int measureDiff;
		private int expandDuration;
		private boolean closeItemsOnClick;
		
		SavedState(Parcelable superState) {
			super(superState);
		}

		public SavedState(Parcel in) {
			super(in);
			rotated = Boolean.valueOf(in.readString());
			totalSpacingDegree = in.readFloat();
			satelliteDistance = in.readInt();
			measureDiff = in.readInt();
			expandDuration = in.readInt();
			closeItemsOnClick = Boolean.valueOf(in.readString());
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			out.writeString(Boolean.toString(rotated));
			out.writeFloat(totalSpacingDegree);
			out.writeInt(satelliteDistance);
			out.writeInt(measureDiff);
			out.writeInt(expandDuration);
			out.writeString(Boolean.toString(closeItemsOnClick));
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
	
	// TODO 下面的方法公开
	/**
	 * 展开子菜单
	 */
	public void expandItems() {
		if (plusAnimationActive.compareAndSet(false, true)) {
			if (!rotated) {
				imgMain.startAnimation(mainRotateLeft);
				for (SatelliteMenuInfo item : menuItems) {
					item.getView().startAnimation(item.getOutAnimation());
				}
			}
			rotated = !rotated;
		}
	}

	/**
	 * 折叠子菜单
	 */
	public void collapseItems() {
		if (plusAnimationActive.compareAndSet(false, true)) {
			if (rotated) {
				imgMain.startAnimation(mainRotateRight);
				for (SatelliteMenuInfo item : menuItems) {
					item.getView().startAnimation(item.getInAnimation());
				}
			}
			rotated = !rotated;
		}
	}
	

	public Map<View, SatelliteMenuInfo> getViewToItemMap() {
		return viewToItemMap;
	}
	/**
	 * 设置卫星子菜单的点击监听
	 * 
	 * @param itemClickedListener
	 */
	public void setOnItemClickedListener(SateliteItemClickedListener itemClickedListener) {
		this.itemClickedListener = itemClickedListener;
	}

	
	/**
	 * 定义各item的间隙的算法。<br>
	 * <strong>注：</strong>在添加菜单前，强烈推荐调用该方法
	 * 
	 * @param gapDegreeProvider
	 */
	public void setGapDegreeProvider(IDegreeProvider gapDegreeProvider) {
		this.gapDegreesProvider = gapDegreeProvider;
		resetItems();
	}

	/**
	 * 定义了起始与最终之间的弧度数。<br>
	 * <strong>注：</strong>在添加菜单前，强烈推荐调用该方法

	 * @param totalSpacingDegree The degree between initial and final items. 
	 */
	public void setTotalSpacingDegree(float totalSpacingDegree) {
		this.totalSpacingDegree = totalSpacingDegree;
		resetItems();
	}

	/**
	 * 设置各菜单到像素的中心的距离。<br>
	 * <strong>注：</strong>在添加菜单前，强烈推荐调用该方法
	 * 
	 * @param distance 菜单到像素中心的距离
	 */
	public void setSatelliteDistance(int distance) {
		this.satelliteDistance = distance;
		resetItems();
	}

	/**
	 * 设置各菜单展开和折叠的时间（单位：毫秒）。<br>
	 * <strong>注：</strong>在添加菜单前，强烈推荐调用该方法
	 * 
	 * @param expandDuration 置各菜单展开和折叠的时间（单位：毫秒）。
	 */
	public void setExpandDuration(int expandDuration) {
		this.expandDuration = expandDuration;
		resetItems();
	}
	
	/**
	 * 设置主菜单图片资源
	 * 
	 * @param resource 图片资源。
	 */
	public void setMainImage(int resource) {
		this.imgMain.setImageResource(resource);
	}

	/**
	 * 设置主菜单的drawable
	 * 
	 * @param resource 图片drawable。
	 */
	public void setMainImage(Drawable drawable) {
		this.imgMain.setImageDrawable(drawable);
	}

	/**
	 * 定义当“单击”子菜单时，子菜单是否要折叠，默认值折叠。
	 * 
	 * @param closeItemsOnClick
	 */
	public void setCloseItemsOnClick(boolean closeItemsOnClick) {
		this.closeItemsOnClick = closeItemsOnClick;
	}
	
	/**
	 * “单击”子菜单事件的监听器
	 * @author Siyamed SINIR
	 */
	public interface SateliteItemClickedListener {
		/**
		 * “单击”时，将被点击的子菜单的id发送出去
		 * 
		 * @param id 子菜单的id 
		 */
		public void eventOccured(int id);
	}

	public void addItems(List<SatelliteMenuInfo> items) {

		menuItems.addAll(items);
		this.removeView(imgMain);
		TextView tmpView = new TextView(getContext());
		tmpView.setLayoutParams(new FrameLayout.LayoutParams(0, 0));

		float[] degrees = getDegrees(menuItems.size());
		int index = 0;
		for (SatelliteMenuInfo menuItem : menuItems) {
			int finalX = SatelliteAnimCreator.getTranslateX(
					degrees[index], satelliteDistance);
			int finalY = SatelliteAnimCreator.getTranslateY(
					degrees[index], satelliteDistance);

			ImageView itemView = createItemView(finalX, finalY, menuItem.getImgResourceId(), menuItem.getId());
			ImageView cloneView = createCloneView(menuItem.getImgResourceId(), menuItem.getId());
			itemView.setTag(Integer.valueOf(menuItem.getId()));

			cloneView.setOnClickListener(internalItemClickListener);
			cloneView.setTag(Integer.valueOf(menuItem.getId()));

			if (menuItem.getImgResourceId() > 0) {
				itemView.setImageResource(menuItem.getImgResourceId());
				cloneView.setImageResource(menuItem.getImgResourceId());
			} else if (menuItem.getImgDrawable() != null) {
				itemView.setImageDrawable(menuItem.getImgDrawable());
				cloneView.setImageDrawable(menuItem.getImgDrawable());
			}

			Animation itemOut = SatelliteAnimCreator.createItemOutAnimation(index,expandDuration, finalX, finalY);
			Animation itemIn = SatelliteAnimCreator.createItemInAnimation(index, expandDuration, finalX, finalY);
			Animation itemClick = SatelliteAnimCreator.createItemClickAnimation();

			itemIn.setAnimationListener(new SatelliteAnimationListener(itemView, true, viewToItemMap));
			itemOut.setAnimationListener(new SatelliteAnimationListener(itemView, false, viewToItemMap));
			itemClick.setAnimationListener(new SatelliteItemClickAnimationListener(this, menuItem.getId()));

			menuItem.setView(itemView);
			menuItem.setCloneView(cloneView);
			menuItem.setInAnimation(itemIn);
			menuItem.setOutAnimation(itemOut);
			menuItem.setClickAnimation(itemClick);
			menuItem.setFinalX(finalX);
			menuItem.setFinalY(finalY);

			this.addView(itemView);
			this.addView(cloneView);
			viewToItemMap.put(itemView, menuItem);
			viewToItemMap.put(cloneView, menuItem);
			index++;
		}

		this.addView(imgMain);
	}

}
