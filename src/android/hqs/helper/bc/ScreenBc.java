package android.hqs.helper.bc;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hqs.helper.DynamicBroadcast;

/**
 * 亮熄屏 --> 广播接收器
 * @author 胡青松
 */
public abstract class ScreenBc extends DynamicBroadcast{

	@Override
	public void register(Context context) {
		IntentFilter screenFilter = new IntentFilter();
		screenFilter.addAction(Intent.ACTION_SCREEN_ON);
		screenFilter.addAction(Intent.ACTION_SCREEN_OFF);
		context.registerReceiver(this, screenFilter);
	}
	
}