package android.hqs.helper;

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
	
	public LocalBroadcast(Context context) {
		setContext(context);
	}
	
	/** 外部类调用该方法注册广播，注意调用{@link #registerReceiver()}*/
	public abstract void register();
	/** 外部类调用该方法注销广播，注意调用{@link #unregisterReceiver()*/
	public abstract void unregister();
	
	/** 在子类{@link #register()}内调用该方法，注册广播 */
	protected void registerReceiver(IntentFilter ift){
		getLocalBcManager().registerReceiver(this, ift);
	}
	
	/** 在子类{@link #unregister()}内调用该方法，注销广播 */
	protected void unregisterReceiver(){
		getLocalBcManager().unregisterReceiver(this);
	}
	
	public void sendBroadcast(Intent intent){
		getLocalBcManager().sendBroadcast(intent);
	}
	
	public LocalBroadcastManager getLocalBcManager(){
		return LocalBroadcastManager.getInstance(getContext());
	}
	
}
