package android.hqs.component4.bc;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 耳机(headset/headphone/earphone)插入或拔出 --> 广播接收器
 * @author 胡青松
 */
public abstract class EarphoneBroadcast extends BaseBroadcast{

	@Override
	public void register(Context context) {
		IntentFilter ift = new IntentFilter();
		ift.addAction(Intent.ACTION_HEADSET_PLUG);
		context.registerReceiver(this, ift);
		setRegister(true);
	}
	
}
