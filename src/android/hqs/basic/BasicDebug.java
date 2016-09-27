package android.hqs.basic;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

/**
 * 给纯私人创建的打印日志的工具类，减少类的重复代码
 * @author hqs2063594
 *
 */
public class BasicDebug {
	
	private final String Tag;
	/** 获取实例类名 */
	public final String getClsName() {
		return Tag;
	}
	
	private final Context context;
	
	/**
	 * 初始化构造方法
	 * @param context 不能为空
	 * @param clazz 类名，用于生成打印日志的标签，不能为空
	 */
	public BasicDebug(Context context){
		if (context == null) {
			throw new NullPointerException("context can not be null!");
		}
		this.Tag = getClass().getSimpleName();
		this.context = context;
	}
	
	public Context getContext() {
		return context;
	}
	
	public ContentResolver getContentResolver() {
		if (context == null) {
			throw new NullPointerException("You Have not set context, plese check!");
		}
		return context.getContentResolver();
	}
	
	public Resources getRes(){
		if (context == null) {
			throw new NullPointerException("You Have not set context, plese check!");
		}
		return context.getResources();
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