package android.hqs.component4.bc;

import android.content.Context;
import android.content.IntentFilter;

/**
 * Hifi --> 广播接收器，各公司有自己的定义这里仅仅是一个示范。
 * @author 胡青松
 */
public abstract class HifiBroadcast extends BaseBroadcast{
	public static final String HIFI_CHANGED_ACTION = "com.bbk.audiofx.hifi.changed";
	public static final String HIFI_DISPLAY_ACTION = "com.bbk.audiofx.hifi.display";
	
	@Override
	public void register(Context context) {
		IntentFilter ift = new IntentFilter();
		ift.addAction(HIFI_CHANGED_ACTION);
		ift.addAction(HIFI_DISPLAY_ACTION);
		context.registerReceiver(this, ift);
		setRegister(true);
	}

}