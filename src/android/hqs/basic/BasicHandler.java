package android.hqs.basic;

import android.hqs.tool.LogcatTool;
import android.os.Handler;
import android.os.Looper;

public abstract class BasicHandler extends Handler {
	private final String Tag = LogcatTool.makeTag(getClass());
	
	public BasicHandler(Callback callback) {
		super(callback);
	}

	public BasicHandler(Looper looper) {
		super(looper);
	}

	public BasicHandler(Looper looper, Callback callback) {
		super(looper, callback);
	}
	
	// ========================================================================================================
	// ==================================== TODO 下面是打印日志的方法 ============================================
	// ========================================================================================================
	/**蓝色，调试信息*/
	protected final void debug(Object obj) {
		LogcatTool.debug(Tag, obj);
	}
	protected final void debug(String methodName, Object obj) {
		LogcatTool.debug(Tag, methodName, obj);
	}
	protected final void debug(String methodName, Throwable tr) {
		LogcatTool.debug(Tag, methodName, tr);
	}
	
	/** 绿色，正常信息 */
	protected final void info(Object obj) {
		LogcatTool.info(Tag, obj);
	}
	protected final void info(String methodName, Object obj) {
		LogcatTool.info(Tag, methodName, obj);
	}
	protected final void info(String methodName, Throwable tr) {
		LogcatTool.info(Tag, methodName, tr);
	}
	protected void info(String listName, byte[] list){
		LogcatTool.info(Tag, listName, list);
	}
	protected final void info(String methodName, String listName, byte[] list) {
		LogcatTool.info(Tag, methodName, listName, list);
	}
	protected void info(String listName, int[] list){
		LogcatTool.info(Tag, listName, list);
	}
	protected final void info(String methodName, String listName, int[] list) {
		LogcatTool.info(Tag, methodName, listName, list);
	}
	
	/**黑色，冗长信息*/
	protected final void verbose(Object obj) {
		LogcatTool.verbose(Tag, obj);
	}
	protected final void verbose(String methodName, Object obj) {
		LogcatTool.verbose(Tag, methodName, obj);
	}
	protected final void verbose(String methodName, Throwable tr) {
		LogcatTool.verbose(Tag, methodName, tr);
	}
	
	/**红色，错误信息*/
	protected final void error(Object obj) {
		LogcatTool.error(Tag, obj);
	}
	protected final void error(String methodName, Object obj) {
		LogcatTool.error(Tag, methodName, obj);
	}
	protected final void error(String methodName, Throwable tr) {
		LogcatTool.error(Tag, methodName, tr);
	}
	protected final void error(String methodName, Object obj, Throwable tr) {
		LogcatTool.error(Tag, methodName, obj, tr);
	}
	
	/**紫色，不应发生的信息*/
	protected final void wtf(Object obj) {
		LogcatTool.wtf(Tag, obj);
	}
	protected final void wtf(String methodName, Object obj) {
		LogcatTool.wtf(Tag, methodName, obj);
	}
	protected final void wtf(String methodName, Throwable tr) {
		LogcatTool.wtf(Tag, methodName, tr);
	}

}
