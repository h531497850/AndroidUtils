package android.hqs.helper.bc;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hqs.basic.BasicBroadcast;
import android.support.v4.content.LocalBroadcastManager;

/**
 * 只在当前应用的广播
 * @author 胡青松
 */
public abstract class LocalBroadcast extends BasicBroadcast {
	
	/** 外部类调用该方法注册广播，注意调用{@link #registerReceiver()}*/
	public abstract void register(Context context);
	/** 外部类调用该方法注销广播，注意调用{@link #unregisterReceiver()*/
	public abstract void unregister(Context context);
	
	/** 在子类{@link #register()}内调用该方法，注册广播 */
	protected void registerReceiver(Context context, IntentFilter ift){
		getLocalBcManager(context).registerReceiver(this, ift);
	}
	
	/** 在子类{@link #unregister()}内调用该方法，注销广播 */
	protected void unregisterReceiver(Context context){
		getLocalBcManager(context).unregisterReceiver(this);
	}
	
	public void sendBroadcast(Context context, Intent intent){
		getLocalBcManager(context).sendBroadcast(intent);
	}
	
	public LocalBroadcastManager getLocalBcManager(Context context){
		return LocalBroadcastManager.getInstance(context);
	}
	
}
