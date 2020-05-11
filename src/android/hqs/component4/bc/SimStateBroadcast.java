package android.hqs.component4.bc;

import android.content.Context;
import android.content.IntentFilter;

public abstract class SimStateBroadcast extends BaseBroadcast {

	private final static String ACTION_SIM_STATE_CHANGED = "android.intent.action.SIM_STATE_CHANGED";
	
	@Override
	public void register(Context context) {
		IntentFilter ift = new IntentFilter();
		ift.addAction(ACTION_SIM_STATE_CHANGED);
		context.registerReceiver(this, ift);
		setRegister(true);
	}
	
	// 在onReceive()里使用TelephonyManager#getSimState()来获取状态信息。

}
