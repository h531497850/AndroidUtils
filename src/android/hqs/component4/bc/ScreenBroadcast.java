package android.hqs.component4.bc;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 亮熄屏 --> 广播接收器
 * @author 胡青松
 */
public abstract class ScreenBroadcast extends BaseBroadcast{

	@Override
	public void register(Context context) {
		IntentFilter ift = new IntentFilter();
		ift.addAction(Intent.ACTION_SCREEN_ON);
		ift.addAction(Intent.ACTION_SCREEN_OFF);
		context.registerReceiver(this, ift);
		setRegister(true);
	}
	
}