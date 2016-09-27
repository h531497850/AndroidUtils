package android.hqs.widget.tree;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.hqs.helper.ViewHolderHelper;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

public abstract class TreeAdapter<T> extends BaseAdapter {

	private static final boolean DEBUG = false;
	
	private final Context context;
	private final String logTag;
	private final int layoutId;

	private ListView mTree;
	private List<TreeNode> mAllNodes;
	private List<TreeNode> mVisibleNodes;
	
	private static final int defaultExpandLevel = 0;
	/**
	 * 记录列表上一次的展开层级
	 */
	private int previousExpandLevel = defaultExpandLevel;
	
	private final Handler mHandler = new Handler(Looper.getMainLooper());
	/** 
	 * 用户在该方法内实现数据加载
	 * @param holder
	 * @param t
	 */
	protected abstract void convert(ViewHolderHelper holder, Object t);
	
	private OnTreeNodeClickListener mListener;
	private interface OnTreeNodeClickListener{
		void onClick(Object object, int position);
	}
	public void setOnTreeNodeClickListener(OnTreeNodeClickListener mListener) {
		this.mListener = mListener;
	}
	
	/**
	 * 初始化构造方法
	 * @param context 不能为空
	 * @param logTag 打印日志的标签，不能为空
	 * @param layoutId 用户传入ItemView的layoutID，不能为空
	 * @param tree
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public TreeAdapter(Context context, String logTag, int layoutId, ListView tree)
			throws IllegalAccessException, IllegalArgumentException {
		this(context, logTag, layoutId, null, tree, defaultExpandLevel);
	}
	/**
	 * 初始化构造方法
	 * @param context 不能为空
	 * @param logTag 打印日志的标签，不能为空
	 * @param layoutId 用户传入ItemView的layoutID，不能为空
	 * @param defaultExpandLevel
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public TreeAdapter(Context context, String logTag, int layoutId, ListView tree, int defaultExpandLevel)
			throws IllegalAccessException, IllegalArgumentException {
		this(context, logTag, layoutId, null, tree, defaultExpandLevel);
	}
	/**
	 * 初始化构造方法
	 * @param context 不能为空
	 * @param logTag 打印日志的标签，不能为空
	 * @param layoutId 用户传入ItemView的layoutID，不能为空
	 * @param datas 不能为空
	 * @param tree
	 * @param defaultExpandLevel
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public TreeAdapter(Context context, String logTag, int layoutId, List<T> datas, ListView tree, int defaultExpandLevel)
			throws IllegalAccessException, IllegalArgumentException {
		this.context = context;
		this.logTag = logTag;
		this.layoutId = layoutId;
		previousExpandLevel = defaultExpandLevel;
		if (datas == null) {
			mVisibleNodes = new LinkedList<TreeNode>();
			return;
		}
		
		mAllNodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel);
		mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
		
		mTree = tree;
		
		mTree.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				expandOrCollapse(position);
				if (mListener != null) {
					mListener.onClick(getItem(position), position);
				}
			}

		});
	}
	
	public void setData(List<T> datas) throws IllegalAccessException, IllegalArgumentException {
		setData(datas, previousExpandLevel);
	}
	
	public void setData(List<T> datas, int defaultExpandLevel) throws IllegalAccessException, IllegalArgumentException {
		if (datas == null) {
			if (this.mVisibleNodes == null) {
				this.mVisibleNodes = new LinkedList<TreeNode>();
			} else {
				this.mVisibleNodes.clear();
			}
		} else {
			mAllNodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel);
			mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
		}
		notifyDataSetChanged();
	}
	
	public Context getContext() {
		return context;
	}
	
	@Override
	public int getCount() {
		return mVisibleNodes.size();
	}

	@Override
	public TreeNode getItem(int position) {
		return mVisibleNodes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolderHelper holder = ViewHolderHelper.getHolder(context, convertView, parent, layoutId, position);
		TreeNode node = getItem(position);
		convert(holder, node);
		convertView = holder.getConvertView();
		// 设置左边距
		convertView.setPadding(node.getLevel() * 30, 3, 3, 3);
		return convertView;
	}
	
	/**
	 * 动态插入节点
	 * @param position
	 * @param data
	 */
	public void addExtraNode(int position, String data){
		TreeNode node = getItem(position);
		int indexOf = mAllNodes.indexOf(node);
		
		TreeNode extraNode = new TreeNode(-1, node.getId(), data);
		extraNode.setParent(node);
		node.getChildren().add(extraNode);
		mAllNodes.add(indexOf + 1, extraNode);
		
		mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
		notifyDataSetChanged();
	}
	
	/**
	 * 点击收缩或展开
	 * @param position
	 */
	private void expandOrCollapse(int position) {
		TreeNode n = getItem(position);
		if (n != null) {
			if (n.isLeaf()) {
				return;
			}
			n.setExpand(!n.isExpand());
			mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
			notifyDataSetChanged();
		}
	}
	
	@Override
	public void notifyDataSetChanged() {
		if(Looper.myLooper() == Looper.getMainLooper()) {
			super.notifyDataSetChanged();
		} else {
			mHandler.removeCallbacks(update);
			mHandler.postDelayed(update, 50);
		}
	}

	private final Runnable update = new Runnable() {
		@Override
		public void run() {
			notifyDataSetChanged();
		}
	};
	
	// ***********************************************************************
	// TODO***********************下面是打印日志的方法********************
	// ***********************************************************************
	/** 一般打印出来的信息时蓝色 */
	protected final void debug(Object obj) {
		if(DEBUG) Log.d(logTag, String.valueOf(obj));
	}
	/**  一般打印出来的信息时绿色 */
	protected final void info(Object obj) {
		if(DEBUG) Log.i(logTag, String.valueOf(obj));
	}
	/** 一般打印出来的信息时黑色 */
	protected final void verbose(Object obj) {
		if(DEBUG) Log.v(logTag, String.valueOf(obj));
	}
	/** 一般打印出来的信息时红色 */
	protected final void error(Object obj) {
		if(DEBUG) Log.e(logTag, String.valueOf(obj));
	}
	/** 一般打印出来的信息时红色 */
	protected final void error(Object obj, Throwable tr) {
		if(DEBUG) Log.e(logTag, String.valueOf(obj), tr);
	}
	/** 一般打印出来的信息时紫色 */
	protected final void wtf(Object obj) {
		if(DEBUG) Log.wtf(logTag, String.valueOf(obj));
	}
	/**  一般打印出来的信息时绿色 */
	protected void info(String str, byte[] list){
		if (DEBUG) {
			if (list == null || list.length == 0) {
				return;
			}
			str = new String(str + ", " + list.length + "   :");
			for(int i=0; i < list.length; ++i) {
				str += String.format("%02x ", list[i]) + ",";
			}
			Log.i(logTag, str);
		}
	}

}
