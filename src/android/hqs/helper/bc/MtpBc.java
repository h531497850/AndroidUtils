package android.hqs.helper.bc;

import android.content.Context;
import android.content.IntentFilter;
import android.hqs.helper.DynamicBroadcast;

/**
 * MTP USB驱动 --> 广播接收器
 * @author 胡青松
 */
public abstract class MtpBc extends DynamicBroadcast{
	
	protected final String USB_STATE = "android.hardware.usb.action.USB_STATE";
	
	@Override
	public void register(Context context) {
		IntentFilter ift = new IntentFilter();
		ift.addAction(USB_STATE);
		context.registerReceiver(this, ift);
	}

}