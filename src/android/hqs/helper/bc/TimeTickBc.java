package android.hqs.helper.bc;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hqs.helper.DynamicBroadcast;

/**
 * 注册监听系统每1分钟发送的广播
 * @author 胡青松
 */
public abstract class TimeTickBc extends DynamicBroadcast {

	@Override
	public void register(Context context) {
		IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK); 
		context.registerReceiver(this, filter);
	}

}
