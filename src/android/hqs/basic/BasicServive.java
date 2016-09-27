package android.hqs.basic;

import android.app.Service;
import android.os.Bundle;
import android.util.Log;

/**
 * 1、在第一次调用该服务是进入方法{@link #onCreate()}，之后不再调用，在这里可以作初始化操作；</br>
 * 2、注意在生命周期结束，即进入方法{@link #onDestroy()}时，释放内存(清空数组、链表、堆栈等，清空各种索引)，
 * 在onDestroy是否调用{@link System#exit(int)}参数为0，或{@link System#gc()}来杀死进程视情况而定。
 * 
 * @author 胡青松
 */
public abstract class BasicServive extends Service {
	
	private final String Tag;
	/** 获取实例对象类的名称，即子类，不是父类 */
	public final String getClsName() {
		return Tag;
	}
	
	/**
	 * 初始化Service，生成实例类名，可作为打印日志的标签，<b>注意：这时上下文(context)还没实现</b>，
	 * 它是在系统调用{@link #onCreate(Bundle)}之后实现的。所以子类实现该构造时不要在构造方法里初始化一些需要上下文的实例。
	 */
	public BasicServive(){
		this.Tag = getClass().getSimpleName();
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