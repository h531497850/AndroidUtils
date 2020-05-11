package android.hqs.component4.bc;

import android.content.Context;
import android.content.IntentFilter;

/**
 * 蓝牙分为传文件和听声音两种不同用途，分别对应不同的广播。
 * <b>这里是（听声音）</b>，各公司有自己的定义这里仅仅是一个示范。
 */
public abstract class BtSoundBroadcast extends BaseBroadcast{
	public static final String BLUETOOTH_PLAYING_CHANG = "android.bluetooth.a2dp.profile.action.PLAYING_STATE_CHANGED";
	
	@Override
	public void register(Context context) {
		IntentFilter ift = new IntentFilter();
		ift.addAction(BLUETOOTH_PLAYING_CHANG);
		context.registerReceiver(this, ift);
		setRegister(true);
	}

}