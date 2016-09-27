package android.hqs.helper.bc;

import android.content.Context;
import android.content.IntentFilter;
import android.hqs.helper.DynamicBroadcast;

/**
 * 超级省电 --> 广播接收器，各公司有自己的定义这里仅仅是一个示范。
 * @author 胡青松
 */
public abstract class SuperPowerBc extends DynamicBroadcast{

	protected final String SUPER_SAVE_SEND_ACTION = "intent.action.super_power_save_send";
	
	@Override
	public void register(Context context) {
		IntentFilter ift = new IntentFilter();
		ift.addAction(SUPER_SAVE_SEND_ACTION);
		context.registerReceiver(this, ift);
	}

}
