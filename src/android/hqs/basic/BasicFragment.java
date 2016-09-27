package android.hqs.basic;

import android.app.Fragment;
import android.content.res.XmlResourceParser;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 注意该Fragment是V4的不是app的。
 * @author hqs2063594
 *
 */
public abstract class BasicFragment extends Fragment {

	/** 打印日志的标签	不能为空 */
	private final String Tag;
	/** 获取实例类名 */
	public final String getClsName() {
		return Tag;
	}
	
	/**
	 * 用户传入布局文件的id。<br>
	 * 注意：该数据不能随意修改，更不能为0。
	 */
	private int layoutId;
	
	/**
	 * 存放小组件的数组
	 */
	private SparseArray<View> mViews;
	
	/**
	 * 在这里设置你的布局id
	 */
	protected abstract int setLayoutId();
	/**
	 * 初始化各种主键，并注册（register）相关监听器
	 */
	protected abstract void initComponents();
	/**
     * 初始化数据，主要是初始化Presenter，而在Presenter内主要有以下几种数据获取方式：
     * <ul>
     * <li>intent</li>
     * <li>网络数据</li>
     * <li>服务传输过来的信息。</li>
     * </ul>
     * 数据初始化完成之后注意刷新界面。
     */
	protected abstract void initData();

	/**
	 * @param logTag 打印日志的标签	不能为空
	 */
	public BasicFragment() {
		super();
		Tag = getClass().getSimpleName();
		layoutId = setLayoutId();
		mViews = new SparseArray<View>();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = getView();
		if(view == null) {
			/*
			 * 这里传入3个参数
			 * 原因：
			 * 1、2个参数ViewGroup root=null 时XmlPullParser parser的布局里面设置的各种width、height等将毫无意义；
			 * 2、3个参数的attachToRoot如果为false表示返回的view时根据layoutId加载的ItemView，
			 * 	  true将返回parent并且将ItemView直接加载到parent里面。
			 */
			view = inflater.inflate(layoutId, container, false);
		}
		
		initComponents();
		
		return view;
	}
	
	// ========================================================================================================
	// ========================================== TODO 下面是公布给子类的方法 ====================================
	// ========================================================================================================
	protected final XmlResourceParser getAnimation(int id) {
		return getResources().getAnimation(id);
	}
	
	protected final XmlResourceParser getLayout(int id) {
		return getResources().getLayout(id);
	}
	
	protected final XmlResourceParser getXml(int id) {
		return getResources().getXml(id);
	}
	
	/**
	 * 根据特定资源Id返回一个布尔值，可以使用任何完整res值，如果res值不为0，返回该真实值。
	 * @param id 通过aapt工具生成的资源标识符。这个整数包含编码包、类型和资源入口，值0是一个无效的标识符。
	 * @return 
	 */
	protected final boolean getBoolean(int id) {
		return getResources().getBoolean(id);
	}
	
	/**
	 * TODO 注意sdk6.0已经有方法{@link #getColor(int)}
	 */
	protected final int getColor(int id) {
		return getResources().getColor(id);
	}
	
	/**
	 * 返回一个与特定资源标识相关联的整数。
	 * @param id 通过aapt工具生成的资源标识符。这个整数包含编码包、类型和资源入口，值0是一个无效的标识符。
	 * @return
	 */
	protected final int getInteger(int id) {
		return getResources().getInteger(id);
	}
	
	/**
	 * 根据特定的资源ID获取尺寸，单位转换基于与资源相关的当前的DisplayMetrics。
	 * @param id 通过aapt工具生成的资源标识符。这个整数包含编码包、类型和资源入口，值0是一个无效的标识符。
	 * @return
	 */
	protected final float getDimension(int id) {
		return getResources().getDimension(id);
	}
	
	protected final DisplayMetrics getDisplayMetrics() {
		return getResources().getDisplayMetrics();
	}
	
	/**
	 * TODO 注意sdk6.0已经有{@link #getDrawable(int)}<br>
	 * 获取资源ID相关联的Drawable对象，对象的各种类型取决于基本资源--例如，纯色，PNG图像，图像分级等，实现细节被绘图API隐藏。
	 * @param id 通过aapt工具生成的资源标识符。这个整数包含编码包、类型和资源入口，值0是一个无效的标识符。
	 * @return
	 */
	protected final Drawable getDrawable(int id) {
		return getResources().getDrawable(id);
	}
	
	/**
	 * 返回一个与特定资源标识相关联的Movie对象。
	 * @param id
	 * @return
	 */
	protected final Movie getMovie(int id) {
		return getResources().getMovie(id);
	}
	
	/**
	 * 返回给定资源标识符的输入名称。
	 * @param id
	 * @return
	 */
	protected final String getResourceEntryName(int id) {
		return getResources().getResourceEntryName(id);
	}
	
	/**
	 * 返回给定资源标识符的全名。这个名字是一个字符串的形式“package:type/entry”。<br>
	 * {@link #getResourcePackageName()}，{@link #getResourceTypeName()}，{@link #getResourceEntryName()}
	 * @param id
	 * @return
	 */
	protected final String getResourceName(int id) {
		return getResources().getResourceName(id);
	}
	
	protected final String getResourcePackageName(int id) {
		return getResources().getResourcePackageName(id);
	}
	
	protected final String getResourceTypeName(int id) {
		return getResources().getResourceTypeName(id);
	}
	protected final View findViewById(int id) {
		return getView().findViewById(id);
	}
	
	// ========================================================================================================
	// ============================================= TODO 下面是公开的方法 ======================================
	// ========================================================================================================
	public final void setVisibility(int visibility) {
		getView().setVisibility(visibility);
	}

	/**
	 * 通过该方法获取各itemView内的各种小组件如：TextView，ImageView，ImageButton等
	 * @param comId 
	 * @return 你想要的View组件
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getComponent(int comId) {
		View Component = mViews.get(comId);
		if (Component == null) {
			Component = getView().findViewById(comId);
			// 将各种小组件存放到map中，下次直接复用
			mViews.put(comId, Component);
		}
		return (T) Component;
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