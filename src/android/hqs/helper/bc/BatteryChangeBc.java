package android.hqs.helper.bc;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 电量变化 --> 广播接收器
 * @author 胡青松
 */
public abstract class BatteryChangeBc extends DynamicBroadcast{
	
	@Override
	public void register(Context context) {
		IntentFilter ift = new IntentFilter();
        ift.addAction(Intent.ACTION_POWER_DISCONNECTED);
        ift.addAction(Intent.ACTION_POWER_CONNECTED);
        context.registerReceiver(this, ift);
	}

}
