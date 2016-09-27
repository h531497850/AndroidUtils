package android.hqs.helper.bc;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hqs.helper.DynamicBroadcast;

/**
 * 耳机(headset/headphone/earphone)插入或拔出 --> 广播接收器
 * @author 胡青松
 */
public abstract class EarphoneBc extends DynamicBroadcast{

	@Override
	public void register(Context context) {
		IntentFilter ift = new IntentFilter();
		ift.addAction(Intent.ACTION_HEADSET_PLUG);
		context.registerReceiver(this, ift);
	}
	
}
