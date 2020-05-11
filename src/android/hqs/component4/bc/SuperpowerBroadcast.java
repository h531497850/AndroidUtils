package android.hqs.component4.bc;

import android.content.Context;
import android.content.IntentFilter;

/**
 * 超级省电 --> 广播接收器，各公司有自己的定义这里仅仅是一个示范。
 * @author 胡青松
 */
public abstract class SuperpowerBroadcast extends BaseBroadcast{
	public static final String SUPER_POWER_SAVE = "intent.action.super_power_save_send";
	
	@Override
	public void register(Context context) {
		IntentFilter ift = new IntentFilter();
		ift.addAction(SUPER_POWER_SAVE);
		context.registerReceiver(this, ift);
		setRegister(true);
	}

}
