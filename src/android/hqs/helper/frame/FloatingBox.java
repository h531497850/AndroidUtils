package android.hqs.helper.frame;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hqs.basic.BasicContext;
import android.hqs.util.HandlerUtil;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;

/**
 * 注意：悬浮框加载的是默认的布局参数，如果您想要自定义，可以在创建实例后设置。
 * @author hqs2063594
 */
public abstract class FloatingBox extends BasicContext implements
		OnClickListener,OnLongClickListener {
	
	private WindowManager mWindow;
	private LayoutInflater mInflater;
	
	/**
	 * 用户不能修改该数据
	 */
	private WindowManager.LayoutParams defaultLayoutParams;
	/**
	 * 用户自定义的布局参数，如果用户设置了该参数，显示时加载用户的，否则加载默认的。
	 */
	private WindowManager.LayoutParams uesrLayoutParams;

	private View mView;
	
	/**
	 * 用{@link Handler}处理内部数据的标识
	 */
	private static final int NATIVE = 1;
	/**
	 * 用{@link Handler}处理外部数据的标识
	 */
	private static final int EXTERNAL = 2;
	
	/**
	 * 显示悬浮框
	 */
	private static final int SHOW = 1;
	/**
	 * 移除悬浮框
	 */
	private static final int DISMISS = 2;
	
	/**
	 * 可以在该线程下做一些相关的耗时操作
	 */
	private HandlerThread mHandlerThread;
	private Looper mLooper;
	/**
	 * 用户可以通过它来更新UI，但用户不能随意重新初始化它。
	 */
	protected Handler mHandler;

	/**
	 * 在这里初始化悬浮框的各个组件
	 */
	public abstract void initComponents();
	/**
	 * 用{@link Handler}处理外部数据的抽象方法，需要在子类中实现；如果返回true，该Handler不再将该消息发送出去，
	 * 即它的其他方法将不再收到消息，否则反之。
	 * @param msg 
	 */
	public abstract boolean handleExteranl(Message msg);
	
	public interface ItemClickListener {
		void onItemClick(View parent, int childId, Object tag);
	}
	public interface ItemLongClickListener {
		void onItemLongClick(View parent, int childId, Object tag);
	}
	
	private ItemClickListener mClick;
	public void setOnItemClickListener(ItemClickListener click){
		mClick = click;
	}
	private ItemLongClickListener mLongClick;
	public void setOnItemLongClickListener(ItemLongClickListener click){
		mLongClick = click;
	}

	public FloatingBox(Context context, int layoutId) {
		super(context);
		init(context);
		initView(mInflater.inflate(layoutId, null));
		initComponents();
	}
	
	public FloatingBox(Context context, View view) {
		super(context);
		init(context);
		initView(view);
		initComponents();
	}
	
	@Override
	public void onClick(View v) {
		if (mClick != null) {
			mClick.onItemClick(mView, v.getId(), v.getTag());
		}
	}
	@Override
	public boolean onLongClick(View v) {
		if (mLongClick != null) {
			mLongClick.onItemLongClick(mView, v.getId(), v.getTag());
		}
		// true 表示触发长按后将不在触发click，false 表示click和longClick可以同时触发
		return true;
	}
	
	public View findViewById(int id) {
		return mView.findViewById(id);
	}
	/**
	 * 显示悬浮框
	 */
	public void show(){
		HandlerUtil.sendMsg(mHandler, NATIVE, SHOW);
	}
	/**
	 * 移除悬浮框
	 */
	public void dismiss() {
		HandlerUtil.sendMsg(mHandler, NATIVE, DISMISS);
	}
	
	public void setLayoutParams(WindowManager.LayoutParams params){
		if (mView == null) {
			throw new NullPointerException("parent view is null, you should set view before set the params.");
		}
		uesrLayoutParams = params;
		mView.setLayoutParams(uesrLayoutParams);
	}
	
	/**
	 * 处理悬浮框的显示和隐藏
	 * @param msg
	 * @return
	 */
	protected boolean handleMsg(Message msg) {
		switch (msg.what) {
		case NATIVE:
			switch (msg.arg1) {
			case SHOW:
				WindowManager.LayoutParams params = null;
				if (uesrLayoutParams == null) {
					params = defaultLayoutParams;
				} else {
					params = uesrLayoutParams;
				}
				if (mView.isShown()) {
					mWindow.updateViewLayout(mView, params);
				} else {
					mWindow.addView(mView, params);
				}
				// TODO 拦截该消息，不再发送出去
				return true;
			case DISMISS:
				if (mView.isShown()) {
					mWindow.removeView(mView);
				}
				return true;
			default:
				return false;
			}
		case EXTERNAL:
			return handleExteranl(msg);
		default:
			return false;
		}
	}

	private void initView(View view) {
		mView = view;
		//WindowManager.LayoutParams params = (LayoutParams) mView.getLayoutParams();
		mView.setLayoutParams(defaultLayoutParams);
	}
	
	/**
	 * TODO 下面开始是初始化。
	 * 初始化一些必要的数据
	 * @param context
	 */
	private void init(Context context) {
		initDefaultLayoutParams(context);
		initNecessary(context);
	}
	/**
	 * 初始化{@link WindowManager}，布局加载器。
	 * @param context
	 */
	private void initNecessary(Context context) {
		mWindow = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Point screenSize = new Point();
		mWindow.getDefaultDisplay().getSize(screenSize);
		mInflater = LayoutInflater.from(context);
		
		mHandlerThread = new HandlerThread("SonggeWindow");
		
		mHandlerThread.start();
		mLooper = mHandlerThread.getLooper();
		mHandler = new Handler(mLooper, new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				return handleMsg(msg);
			}
		});
	}

	/**
	 * 初始化默认的布局参数
	 * @param context
	 */
	private void initDefaultLayoutParams(Context context) {
		defaultLayoutParams =  new WindowManager.LayoutParams();
		defaultLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
		defaultLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
		//params.alpha = 0.8f;
		defaultLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		defaultLayoutParams.format = PixelFormat.RGB_565;
		defaultLayoutParams.flags = (WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				|WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS 
				|WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		defaultLayoutParams.gravity = Gravity.CENTER;
		//defaultLayoutParams.y = Gravity.BOTTOM + 90;
	}

}
