package android.hqs.basic;

import com.vivo.android.util.LogUtil;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

/**
 * 对话框默认居中显示，基于activity界面显示。
 * <p>
 * 要对话框内各组件的点击事件就得在界面初始化完成之后注册OnClickListener，而长按不是必须的；
 * 又由于各种事件响应后得将一些相关的数据传到需要的地方，比如activity等，所有还得设置回调接口
 * 
 * @author hqs2063594
 *
 */
public abstract class BasicDialog extends DialogFragment
		implements OnTouchListener, OnClickListener, OnLongClickListener, OnKeyListener {
	private static final String TAG = LogUtil.makeTag("BasicDialog");

	private final String Tag = LogUtil.makeTag(getClass());

	/**
	 * 用户传入布局文件的id。<br>
	 * 注意：该数据不能随意修改，更不能为0。
	 */
	private int layoutId;

	/**
	 * 软引用对话框消失后里面存储的View很可能被释放，这样可以降低内存消耗
	 *//*
		 * protected SoftReference<View> mCacheView;
		 */

	/**
	 * 这里没有使用软引用，软应用对话框消失后里面存储的View很可能被释放，这样可以降低内存消耗， 所有注意释放内存
	 */
	private View mConvertView;
	/**
	 * 存放小组件的数组
	 */
	private SparseArray<View> mViews;

	private Point mPoint;
	private int mGravity = Gravity.CENTER;

	private boolean isTouch;

	/** 在这里设置你的布局id */
	protected abstract int setLayoutId();

	/**
	 * 初始化构造方法，DialogFragment必须有一个无参的构造方法，如果不实现该构造会报错；并初始化数据。
	 */
	public BasicDialog() {
		super();
		layoutId = setLayoutId();
		mViews = new SparseArray<View>();
		Log.i(TAG, "initialize----constructor done");
	}

	/** View已创建完毕，可在此控制View或注册事件等 */
	protected abstract void initComponents();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate()");
		/*
		 * 控制显示的对话框是否可撤销。用该方法而不是直接调用Dialog.setCancelable(boolean)，
		 * 因为DialogFragment的改变基于此的行为。
		 */
		// setCancelable(false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView()");
		if (mConvertView == null) {
			/*
			 * 这里传入3个参数 原因： 1、2个参数ViewGroup root=null 时XmlPullParser
			 * parser的布局里面设置的各种width、height等将毫无意义；
			 * 2、3个参数的attachToRoot如果为false表示返回的view时根据layoutId加载的ItemView，
			 * true将返回parent并且将ItemView直接加载到parent里面。
			 */
			mConvertView = inflater.inflate(layoutId, container, false);
		}
		mConvertView.setOnTouchListener(this);
		initComponents();

		if (mPoint != null) {
			setAttributes(mGravity, mPoint.x, mPoint.y);
		}
		setBackground(Color.TRANSPARENT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setOnKeyListener(this);
		return mConvertView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mConvertView = null;
		Log.i(TAG, "onDestroy()");
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isTouch = true;
			break;
		case MotionEvent.ACTION_UP:
			isTouch = false;
			break;
		}
		return false;
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 该方法已经判断了对话框是否处于显示状态所以不必自己判断
			dismiss();
			// true表示监听者触发了该事件，false表示没有触发
			return true;
		}
		// true表示监听者触发了该事件，false表示没有触发
		return false;
	}

	protected void finishActivity() {
		if (getActivity() != null) {
			getActivity().finish();
		}
	}

	protected ContentResolver getContentResolver() {
		if (getActivity() != null) {
			return getActivity().getContentResolver();
		}
		return null;
	}

	private Window getWindow() {
		return getDialog().getWindow();
	}

	// =================================================================
	// ======== TODO These methods are visible to the subclass =========
	// =================================================================
	/**
	 * 首先将各种参数保存下来以便下次在
	 * {@link #onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)}
	 * 方法中复用
	 * 
	 * @param gravity
	 * @param x
	 * @param y
	 */
	protected void setAttributes(int gravity, int x, int y) {
		mGravity = gravity;
		if (mPoint == null) {
			mPoint = new Point(x, y);
		} else {
			mPoint.set(x, y);
		}
		LayoutParams params = getWindow().getAttributes();
		params.gravity = gravity;
		params.x = x;
		params.y = y;
		getWindow().setAttributes(params);
	}

	protected void setOnKeyListener(OnKeyListener listener) {
		getDialog().setOnKeyListener(listener);
	}

	protected void requestWindowFeature(int featureId) {
		getDialog().requestWindowFeature(featureId);
	}

	protected void setBackground(int color) {
		getWindow().setBackgroundDrawable(new ColorDrawable(color));
	}

	protected void setBackgroundResource(int resid) {
		getWindow().setBackgroundDrawableResource(resid);
	}

	/**
	 * 查询组件并设置点击和长按监听
	 * 
	 * @param id
	 * @return
	 */
	protected final View findViewById(int id) {
		View v = mConvertView.findViewById(id);
		v.setOnClickListener(this);
		v.setOnLongClickListener(this);
		v.setOnTouchListener(this);
		return v;
	}

	// =================================================================
	// ======================= TODO public methods =====================
	// =================================================================
	public View getConvertView() {
		return mConvertView;
	}

	public boolean isTouch() {
		return isTouch;
	}

	/**
	 * @param manager
	 *            activity和Fragment之间的的交互接口，由activity传入。注意：该数据不能随意修改，更不能为空。
	 */
	public void show(FragmentManager manager) {
		if (!isAdded()) {
			show(manager, getClass().getName());
		}
	}

	/**
	 * @param manager
	 *            activity和Fragment之间的的交互接口，由activity传入。注意：该数据不能随意修改，更不能为空。
	 * @param x
	 *            相对于gravity的X轴坐标
	 * @param y
	 *            相对于gravity的Y轴坐标
	 */
	public void show(FragmentManager manager, int x, int y) {
		show(manager, Gravity.CENTER, x, y);
	}

	/**
	 * @param manager
	 *            activity和Fragment之间的的交互接口，由activity传入。注意：该数据不能随意修改，更不能为空。
	 * @param gravity
	 *            相对位置
	 * @param x
	 *            相对于gravity的X轴坐标
	 * @param y
	 *            相对于gravity的Y轴坐标
	 */
	public void show(FragmentManager manager, int gravity, int x, int y) {
		setAttributes(gravity, x, y);
		show(manager);
	}

	public void close() {
		if (isVisible()) {
			dismiss();
		}
	}

	/**
	 * 通过该方法获取各itemView内的各种小组件如：TextView，ImageView，ImageButton等
	 * 
	 * @param comId
	 * @return 你想要的View组件
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getComponent(int comId) {
		View Component = mViews.get(comId);
		if (Component == null) {
			Component = mConvertView.findViewById(comId);
			// 将各种小组件存放到map中，下次直接复用
			mViews.put(comId, Component);
		}
		return (T) Component;
	}

	public final void setVisibility(int visibility) {
		mConvertView.setVisibility(visibility);
	}

	/** 获取实例类名 */
	public final String getClsName() {
		return getClass().getSimpleName();
	}

	// =================================================================
	// ===================== TODO print log methods ====================
	// =================================================================
	/** Blue, debug information */
	protected final void debug(Object obj) {
		LogUtil.debug(Tag, obj);
	}

	protected final void debug(Object obj, Throwable tr) {
		LogUtil.debug(Tag, obj, tr);
	}

	/** Green, normal information */
	protected final void info(Object obj) {
		LogUtil.info(Tag, obj);
	}

	protected final void info(Object obj, Throwable tr) {
		LogUtil.info(Tag, obj, tr);
	}

	/** Black, long message */
	protected final void verbose(Object obj) {
		LogUtil.verbose(Tag, obj);
	}

	protected final void verbose(Object obj, Throwable tr) {
		LogUtil.verbose(Tag, obj, tr);
	}

	/** Red, error message */
	protected final void error(Object obj) {
		LogUtil.error(Tag, obj);
	}

	protected final void error(Object obj, Throwable tr) {
		LogUtil.error(Tag, obj, tr);
	}

}