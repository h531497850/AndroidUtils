package android.hqs.basic;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.hqs.helper.ViewHolderHelper;
import android.hqs.util.HandlerUtil;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

/**
 * 基本通用型数据适配器，由于数据集可能为空，将其抛出，好让上层处理; 将数据保存到适配器，
 * 避免出现adapter的内容变化了，但是你的AdapterView并不知情的状况发生。
 * 
 * @author hqs2063594
 * 
 * @param <T>
 *            数据集类型，可以在初始化时指定，也可以子类直接指定
 */
public abstract class BasicAdapter<T> extends android.widget.BaseAdapter{
	
	private final Context context;
	private final String Tag;
	/** 获取实例类名 */
	public final String getClsName() {
		return Tag;
	}
	
	private final int layoutId;

	protected List<T> datas = new ArrayList<T>();

	/** 
	 * 用户在该方法内实现数据加载
	 * @param holder
	 * @param bean
	 * @param position 
	 */
	protected abstract void convert(ViewHolderHelper holder, T bean, int position);
	
	/**
	 * 初始化构造方法
	 * @param context 不能为空
	 * @param clazz 类名，用于生成打印日志的标签，不能为空
	 * @param layoutId 用户传入ItemView的layoutID，不能为空
	 */
	public BasicAdapter(Context context, int layoutId) {
		this(context, layoutId, null);
	}
	/**
	 * 初始化构造方法
	 * @param context 不能为空
	 * @param clazz 类名，用于生成打印日志的标签，不能为空
	 * @param layoutId 用户传入ItemView的layoutID，不能为空
	 * @param datas 不能为空
	 */
	public BasicAdapter(Context context, int layoutId, ArrayList<T> datas) {
		if (context == null) {
			throw new NullPointerException("context can not be null!");
		}
		if (layoutId <= 0) {
			throw new NullPointerException("You are not assigned to layoutId!");
		}
		
		this.context = context;
		this.Tag = getClass().getSimpleName();
		this.layoutId = layoutId;
		
		setData(datas);
	}
	
	public void setData(ArrayList<T> datas) {
		if (datas == null || datas.size() == 0) {
			HandlerUtil.sendMsg(mHandler, DATA_CLEAR);
		} else {
			HandlerUtil.sendMsg(mHandler, DATA_SET, datas);
		}
	}
	public final List<T> getDatas() {
		return datas;
	}
	
	@Override
	public int getCount() {
		return getDataCount();
	}
	
	public final int getDataCount(){
		return datas.size();
	}
	
	/** TODO 根据该方法查看当前item显示的是否是数据项 */
	public final boolean isDataItem(int position){
		return (position >= 0) && (position < getDataCount());
	}

	@Override
	public T getItem(int position) {
		if (isDataItem(position)) {
			return datas.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolderHelper holder = ViewHolderHelper.getHolder(context, convertView, parent, layoutId, position);
		convert(holder, getItem(position), position);
		return holder.getConvertView();
	}
	
	@Override
	public void notifyDataSetChanged() {
		mHandler.sendEmptyMessage(DATA_NOTIFY);
	}
	
	public void destroy(){
		HandlerUtil.sendMsg(mHandler, DATA_CLEAR);
	}
	
	private final void notifyData() {
		super.notifyDataSetChanged();
	}

	private final byte DATA_NOTIFY = 0x11;
	private final byte DATA_SET = 0x12;
	private final byte DATA_CLEAR = 0x13;
	
	protected final byte LIST_VIEW_SROLL = 0x20;
	
	protected final Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
		@SuppressWarnings("unchecked")
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case DATA_NOTIFY:
				notifyData();
				return true;
			case DATA_SET:
				ArrayList<T> list = (ArrayList<T>) msg.getData().get("data");
				datas = (List<T>) list.clone();
				notifyData();
				return true;
			case DATA_CLEAR:
				datas.clear();
				notifyData();
				return true;
			case LIST_VIEW_SROLL:
				int position = msg.arg1;
				if (mAdapterView != null && isDataItem(position)) {
					mAdapterView.setSelection(position);
				}
				return true;
			default:
				return false;
			}
		}
	});
	
	/**
	 * 给定索引项在AdapterView中是否可见
	 * @param index
	 * @return
	 */
	public final boolean isItemVisible(int index){
		if (mAdapterView == null) {
			throw new NullPointerException("plese set ListView before you use this method!");
		}
		if (index >= mAdapterView.getFirstVisiblePosition() && index <= mAdapterView.getLastVisiblePosition()) {
			return true;
		}
		return false;
	}
	
/*	private LooperThread mLooperThread;
	private class LooperThread extends Thread {
		public Handler mHandler;

		@Override
		public void run() {
			Looper.prepare();// 给线程创建一个消息循环
			mHandler = new Handler(new Handler.Callback() {
				@Override
				public boolean handleMessage(Message msg) {
					if (msg.what == NOTIFY) {
						debug("刷新列表数据！");
						notifyData();
						return true;
					}
					return false;
				}
			});
			Looper.loop();// 使消息循环起作用，从消息队列里取消息，处理消息
		}
	}
	*/
	
	private AdapterView<BasicAdapter<T>> mAdapterView;
	public final AdapterView<BasicAdapter<T>> getAdapterView() {
		return mAdapterView;
	}
	public final void setAdapterView(AdapterView<BasicAdapter<T>> view) {
		this.mAdapterView = view;
	}
	
	private Handler mainHandler;
	/**
	 * 将主Handler传入，方便将适配器内的数据传出
	 * @param handler
	 */
	public final void setMainHandler(Handler handler) {
		mainHandler = handler;
	}
	public final Handler getMainHandler() {
		return mainHandler;
	}

	private boolean isTouchList;
	public final void setTouch(boolean touch) {
		this.isTouchList = touch;
	}
	public final boolean isTouchList() {
		return isTouchList;
	}

	// ========================================================================================================
	// ==================================== TODO 下面是打印日志的方法 ============================================
	// ========================================================================================================
	private boolean DEBUG = false;
	protected final void setDebug(boolean debug) {
		DEBUG = debug;
	}
	
	protected final void debug(Object obj) {
		if(DEBUG) Log.d(Tag, String.valueOf(obj));
	}
	/**
	 * 蓝色，调试信息
	 * @param methodName 方法名
	 * @param obj 要打印的消息
	 */
	protected final void debug(String methodName, Object obj) {
		if(DEBUG) Log.d(Tag, methodName + " --> " + String.valueOf(obj));
	}
	protected final void debug(String methodName, Throwable tr) {
		if(DEBUG) Log.d(Tag, methodName, tr);
	}
	
	protected final void info(Object obj) {
		if(DEBUG) Log.i(Tag, String.valueOf(obj));
	}
	/**
	 * 绿色，正常信息
	 * @param methodName 方法名
	 * @param obj 要打印的消息
	 */
	protected final void info(String methodName, Object obj) {
		if(DEBUG) Log.i(Tag, methodName + " --> " + String.valueOf(obj));
	}
	protected final void info(String methodName, Throwable tr) {
		if(DEBUG) Log.i(Tag, methodName, tr);
	}
	
	protected final void verbose(Object obj) {
		if(DEBUG) Log.v(Tag, String.valueOf(obj));
	}
	/**
	 *  黑色，冗长信息
	 * @param methodName 方法名
	 * @param obj 要打印的消息
	 */
	protected final void verbose(String methodName, Object obj) {
		if(DEBUG) Log.v(Tag, methodName + " --> " + String.valueOf(obj));
	}
	protected final void verbose(String methodName, Throwable tr) {
		if(DEBUG) Log.v(Tag, methodName, tr);
	}
	
	protected final void error(Object obj) {
		if(DEBUG) Log.e(Tag, String.valueOf(obj));
	}
	/**
	 *  红色，错误信息
	 * @param methodName 方法名
	 * @param obj 要打印的消息
	 */
	protected final void error(String methodName, Object obj) {
		if(DEBUG) Log.e(Tag, methodName + " --> " + String.valueOf(obj));
	}
	protected final void error(String methodName, Throwable tr) {
		if(DEBUG) Log.e(Tag, methodName, tr);
	}
	protected final void error(String methodName, Object obj, Throwable tr) {
		if(DEBUG) Log.e(Tag, methodName + " --> " + String.valueOf(obj), tr);
	}
	
	protected final void wtf(Object obj) {
		if(DEBUG) Log.wtf(Tag, String.valueOf(obj));
	}
	/**
	 * 紫色，不应发生的信息
	 * @param methodName 方法名
	 * @param obj 要打印的消息
	 */
	protected final void wtf(String methodName, Object obj) {
		if(DEBUG) Log.wtf(Tag, methodName + " --> " + String.valueOf(obj));
	}
	protected final void wtf(String methodName, Throwable tr) {
		if(DEBUG) Log.wtf(Tag, methodName, tr);
	}
	
	protected void info(String listName, byte[] list){
		if (DEBUG) {
			if (list == null || list.length == 0) {
				return;
			}
			listName = new String(listName + ", " + list.length + "   :");
			for(int i=0; i < list.length; ++i) {
				listName += String.format("%02x ", list[i]) + ",";
			}
			Log.i(Tag, listName);
		}
	}
	protected final void info(String methodName, String listName, byte[] list) {
		if (DEBUG) {
			if (list == null || list.length == 0) {
				return;
			}
			listName = new String(listName + ", " + list.length + "   :");
			for(int i=0; i < list.length; ++i) {
				listName += String.format("%02x ", list[i]) + ",";
			}
			Log.i(Tag, methodName + " --> " + listName);
		}
	}
	protected void info(String listName, int[] list){
		if (DEBUG) {
			if (list == null || list.length == 0) {
				return;
			}
			listName = new String(listName + ", " + list.length + "   :");
			for(int i=0; i < list.length; ++i) {
				listName += String.valueOf(list[i]) + ",";
			}
			Log.i(Tag, listName);
		}
	}
	protected final void info(String methodName, String listName, int[] list) {
		if (DEBUG) {
			if (list == null || list.length == 0) {
				return;
			}
			listName = new String(listName + ", " + list.length + "   :");
			for(int i=0; i < list.length; ++i) {
				listName += String.valueOf(list[i]) + ",";
			}
			Log.i(Tag, methodName + " --> " + listName);
		}
	}
	
}