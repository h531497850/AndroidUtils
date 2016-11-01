package android.hqs.basic;

import android.app.Service;
import android.hqs.tool.LogcatTool;

/**
 * 1、在第一次调用该服务是进入方法{@link #onCreate()}，之后不再调用，在这里可以作初始化操作；</br>
 * 2、注意在生命周期结束，即进入方法{@link #onDestroy()}时，释放内存(清空数组、链表、堆栈等，清空各种索引)，
 * 在onDestroy是否调用{@link System#exit(int)}参数为0，或{@link System#gc()}来杀死进程视情况而定。
 * 
 * @author 胡青松
 */
public abstract class BasicServive extends Service {
	private final String Tag = LogcatTool.makeTag(getClass());

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