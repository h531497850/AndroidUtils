package android.hqs.basic;

import android.content.Context;
import android.hqs.helper.DebugHelper;
import android.os.AsyncTask;

public abstract class BasicAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
	
	private final Context context;
	
	private DebugHelper mDebug;

	public BasicAsyncTask(Context context) {
		this.context = context;
		mDebug = new DebugHelper();
		mDebug.makeTag(getClass());
	}

	// ========================================================================================================
	// ==================================== TODO 下面是公开的方法 ============================================
	// ========================================================================================================
	/** 获取实例类名 */
	public final String getClsName() {
		return getClass().getSimpleName();
	}

	public Context getContext() {
		return context;
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