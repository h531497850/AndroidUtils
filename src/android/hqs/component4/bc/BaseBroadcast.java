package android.hqs.component4.bc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hqs.gj.tool.LogTool;
import android.support.v4.content.LocalBroadcastManager;

public abstract class BaseBroadcast extends BroadcastReceiver {
	private final String Tag = LogTool.makeTag(getClass());
	
	private boolean isRegister = false;
	
	/** 只在当前应用的广播，如果子类实现了该接口，那么子类实现的同名方法将是接口里的方法，而不是父类的方法，超引用是父类的方法。 */
	public interface ILocal {
		/** 在子类{@link BaseBroadcast#getLocalManager(Context)}获取{@link LocalBroadcastManager}，注册广播 */
		public void register(Context context, IntentFilter ift);
		/** 在子类{@link BaseBroadcast#getLocalManager(Context)}获取{@link LocalBroadcastManager}，注销广播 */
		public void unregister(Context context);
		public void sendBroadcast(Context context, Intent intent);
	}
	
	/** 如果子类有数据缓存，最好实现该接口清除数据 */
	public interface IData {
		public void clearUp();
	}
	
	// ========================================================================================================
	// ==================================== TODO 下面是公开的方法 ===============================================
	// ========================================================================================================
	/**
	 * <strong>动态注册 </strong>（静态广播在manifest里注册，就不用实现该方法）的广播，子类实现该方法来注册广播接收器。
	 * <p>
	 * 如果广播成功注册，请调用{@link #setRegister(boolean)}，来完成注册成功事件。
	 * @param context
	 */
	public void register(Context context){}
	/** 如果在注销后又会重新注册，那么请实现该方法，并在方法结尾调用{@link #setRegister(boolean)}取消当前注册状态 */
	public void unregister(Context context){}
	
	/** 获取实例类名 */
	public final String getClsName() {
		return getClass().getSimpleName();
	}
	
	/**用来判断当前广播是否已注册，避免在没有注册的情况下注销注册导致异常。*/
	public boolean isRegister() {
		return isRegister;
	}
	/**
	 * 是否注册成功
	 * @param isRegister true，成功；false，没成功或取消注册了
	 */
	public void setRegister(boolean isRegister) {
		this.isRegister = isRegister;
	}
	
	public LocalBroadcastManager getLocalManager(Context context) {
		return LocalBroadcastManager.getInstance(context);
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