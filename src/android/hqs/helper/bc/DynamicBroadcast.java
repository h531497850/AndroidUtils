package android.hqs.helper.bc;

import android.content.Context;
import android.hqs.basic.BasicBroadcast;

/**
 * 动态注册的广播，调用子类实现的抽象方法{@link #register(Context)}来注册广播接收器。
 * @author 胡青松
 */
public abstract class DynamicBroadcast extends BasicBroadcast {

	/** 注册广播 */
	public abstract void register(Context context);
	
}
