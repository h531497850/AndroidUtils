package android.hqs.basic;

import android.content.ContentProvider;
import android.hqs.helper.DebugHelper;

/**
 * Android的数据存储方式总共有五种，分别是：Shared Preferences、网络存储、文件存储、外储存储、SQLite。
 * 一般数据只在本身内共享，而其他应用想要共享该应用的的数据，就可用到四大组件之一的{@link #ContentProvider}。
 * 
 * @author 胡青松
 */
public abstract class BasicProvider extends ContentProvider {

	private DebugHelper mDebug;
	
	public BasicProvider() {
		mDebug = new DebugHelper();
		mDebug.makeTag(getClass());
	}

	// ========================================================================================================
	// ==================================== TODO 下面是公开的方法 ===============================================
	// ========================================================================================================
	/** 获取实例类名 */
	public final String getClsName() {
		return getClass().getSimpleName();
	}
	
	// ========================================================================================================
	// ==================================== TODO 下面是打印日志的方法 ============================================
	// ========================================================================================================
	protected final void setDebug(boolean debug) {
		mDebug.setDebug(debug);
	}
	
	// 调试
	protected final void debug(Object obj) {
		mDebug.debug(obj);
	}
	protected final void debug(String methodName, Object obj) {
		mDebug.debug(methodName, obj);
	}
	protected final void debug(String methodName, Throwable tr) {
		mDebug.debug(methodName, tr);
	}
	
	// 普通
	protected final void info(Object obj) {
		mDebug.info(obj);
	}
	protected final void info(String methodName, Object obj) {
		mDebug.info(methodName, obj);
	}
	protected final void info(String methodName, Throwable tr) {
		mDebug.info(methodName, tr);
	}
	protected void info(String listName, byte[] list){
		mDebug.info(listName, list);
	}
	protected final void info(String methodName, String listName, byte[] list) {
		mDebug.info(methodName, listName, list);
	}
	protected void info(String listName, int[] list){
		mDebug.info(listName, list);
	}
	protected final void info(String methodName, String listName, int[] list) {
		mDebug.info(methodName, listName, list);
	}
	
	// 正常
	protected final void verbose(Object obj) {
		mDebug.verbose(obj);
	}
	protected final void verbose(String methodName, Object obj) {
		mDebug.verbose(methodName, obj);
	}
	protected final void verbose(String methodName, Throwable tr) {
		mDebug.verbose(methodName, tr);
	}
	
	// 错误
	protected final void error(Object obj) {
		mDebug.error(obj);
	}
	protected final void error(String methodName, Object obj) {
		mDebug.error(methodName, obj);
	}
	protected final void error(String methodName, Throwable tr) {
		mDebug.error(methodName, tr);
	}
	protected final void error(String methodName, Object obj, Throwable tr) {
		mDebug.error(methodName, obj, tr);
	}
	
	// 不应发生的
	protected final void wtf(Object obj) {
		mDebug.wtf(obj);
	}
	protected final void wtf(String methodName, Object obj) {
		mDebug.wtf(methodName, obj);
	}
	protected final void wtf(String methodName, Throwable tr) {
		mDebug.wtf(methodName, tr);
	}

}