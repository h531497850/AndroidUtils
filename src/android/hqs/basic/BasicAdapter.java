package android.hqs.basic;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.hqs.gj.helper.ViewHolderHelper;
import android.hqs.gj.tool.LogTool;
import android.hqs.gj.util.HandlerUtil;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
	private final String Tag = LogTool.makeTag(getClass());
	/**内存管理都是jvm实现的，我们不需要各种置空处理*/
	private Context context;
	
	private final int layoutId;
	
	// 容器
	private AdapterView<BasicAdapter<T>> mAdapterView;
	// 是否触摸列表
	private boolean isTouchList;
	// 
	private Handler mainHandler;
	private AdapterHandler mHandler;
	
	private List<T> datas = new ArrayList<T>();

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
	 * @throws NullPointerException if the context is null.
	 * @throws IllegalAccessError if the layoutId is invalid.
	 */
	public BasicAdapter(Context context, int layoutId, ArrayList<T> datas) {
		if (context == null) {
			throw new NullPointerException("context can not be null!");
		}
		if (layoutId <= 0) {
			throw new IllegalAccessError("You are not assigned to layoutId!");
		}
		
		this.context = context;
		this.layoutId = layoutId;
		
		mHandler = new AdapterHandler(Looper.getMainLooper());
		
		setData(datas);
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
	
	public final class AdapterHandler extends Handler {
		public static final byte DATA_NOTIFY = 0x11;
		public static final byte DATA_SET = 0x12;
		public static final byte DATA_CLEAR = 0x13;
		
		public static final byte LIST_VIEW_SROLL = 0x20;
		
		public AdapterHandler(Looper looper) {
			super(looper);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DATA_NOTIFY:
				notifyData();
				break;
			case DATA_SET:
				ArrayList<T> list = (ArrayList<T>) msg.getData().get("data");
				datas = (List<T>) list.clone();
				notifyData();
				break;
			case DATA_CLEAR:
				datas.clear();
				notifyData();
				break;
			case LIST_VIEW_SROLL:
				int position = msg.arg1;
				if (mAdapterView != null && isDataItem(position)) {
					mAdapterView.setSelection(position);
				}
				break;
			default:
				break;
			}
		}
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
		mHandler.sendEmptyMessage(AdapterHandler.DATA_NOTIFY);
	}
	
	private final void notifyData() {
		super.notifyDataSetChanged();
	}

	// ========================================================================================================
	// ==================================== TODO 下面是公开的方法 ============================================
	// ========================================================================================================
	/**
	 * 给定索引项在AdapterView中是否可见
	 * @param index
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
	
	/** 获取实例类名 */
	public final String getClsName() {
		return getClass().getSimpleName();
	}
	
	/**
	 * 初始化或数据变化时调用
	 * @param datas 在没有数据的时候清空列表，有的时候刷新
	 */
	public final void setData(ArrayList<T> datas) {
		if (datas == null || datas.size() == 0) {
			mHandler.sendEmptyMessage(AdapterHandler.DATA_CLEAR);
		} else {
			HandlerUtil.sendMsg(mHandler, AdapterHandler.DATA_SET, datas);
		}
	}
	/**
	 * 获取当前列表的实际数据
	 */
	public final List<T> getDatas() {
		return datas;
	}

	public final AdapterView<BasicAdapter<T>> getAdapterView() {
		return mAdapterView;
	}
	public final void setAdapterView(AdapterView<BasicAdapter<T>> view) {
		this.mAdapterView = view;
	}
	
	public final void setTouch(boolean touch) {
		this.isTouchList = touch;
	}
	public final boolean isTouchList() {
		return isTouchList;
	}
	
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
	
	// ========================================================================================================
	// ==================================== TODO 下面是打印日志的方法 ============================================
	// ========================================================================================================
	/**蓝色，调试信息*/
	protected final void debug(Object obj) {
		LogTool.debug(Tag, obj);
	}
	protected final void debug(Object obj, Throwable tr) {
		LogTool.debug(Tag, obj, tr);
	}
	
	/** 绿色，正常信息 */
	protected final void info(Object obj) {
		LogTool.info(Tag, obj);
	}
	protected final void info(Object obj, Throwable tr) {
		LogTool.info(Tag, obj, tr);
	}
	protected void info(String listName, byte[] list){
		LogTool.info(Tag, listName, list);
	}
	protected void info(String listName, int[] list){
		LogTool.info(Tag, listName, list);
	}
	
	/**黑色，冗长信息*/
	protected final void verbose(Object obj) {
		LogTool.verbose(Tag, obj);
	}
	protected final void verbose(Object obj, Throwable tr) {
		LogTool.verbose(Tag, obj, tr);
	}
	
	/**红色，错误信息*/
	protected final void error(Object obj) {
		LogTool.error(Tag, obj);
	}
	protected final void error(Object obj, Throwable tr) {
		LogTool.error(Tag, obj, tr);
	}
	
	/**紫色，不应发生的信息*/
	protected final void wtf(Object obj) {
		LogTool.wtf(Tag, obj);
	}
	protected final void wtf(Object obj, Throwable tr) {
		LogTool.wtf(Tag, obj, tr);
	}
	
}