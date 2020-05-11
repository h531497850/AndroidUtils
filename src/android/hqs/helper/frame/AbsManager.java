package android.hqs.helper.frame;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hqs.basic.BasicContext;

public abstract class AbsManager extends BasicContext{

	public AbsManager(Context context) {
		super(context);
	}
	/**
	 * 服务被{@link Context#bindService(Intent, android.content.ServiceConnection, int)}启动，
	 * 即调用{@link Service#onBind(Intent)时，如果<b>子类重写</b>了该方法，请同时调用该方法。
	 */
	public void onBind(Intent intent){}
	/**
	 * 服务被{@link Context#startService(Intent)}启动，即调用{@link Service#onStartCommand(Intent, int, int)}时，
	 * 如果<b>子类重写</b>了该方法，请同时调用该方法。
	 */
	public void onStartCommand(Intent intent, int flags, int startId){}
	/**
	 * 服务被摧毁（即调用{@link Activity#onDestroy}）时，如果<b>子类重写</b>了该方法，请同时调用该方法。</br>
	 * 释放内存(清空数组、链表、堆栈等，清空各种索引) 
	 */
	public void onDestroy(){}
	
	public void onConfigurationChanged(Configuration newConfig){
		
	}
	public void onLowMemory(){
		
	}
	public void onTrimMemory(int level){
		
	}
	public void onUnbind(Intent intent){
		
	}
	public void onRebind(Intent intent){
		
	}
	public void onTaskRemoved(Intent rootIntent){
		
	}
}
