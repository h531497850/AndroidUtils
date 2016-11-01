package android.hqs.basic;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.hqs.tool.LogcatTool;

/**
 * Android的数据存储方式总共有五种，分别是：Shared Preferences、网络存储、文件存储、外储存储、SQLite。
 * 一般数据只在本身内共享，而其他应用想要共享该应用的的数据，就可用到四大组件之一的{@link #ContentProvider}。
 * 
 * @author 胡青松
 */
public abstract class BasicProvider extends ContentProvider {
	private final String Tag = LogcatTool.makeTag(getClass());
	
	protected final byte[] lock = new byte[0];
	
	// ========================================================================================================
	// ==================================== TODO 下面是公开的方法 ===============================================
	// ========================================================================================================
	/** 获取实例类名 */
	public final String getClsName() {
		return getClass().getSimpleName();
	}
	
	public ContentResolver getContentResolver() {
		return getContext().getContentResolver();
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