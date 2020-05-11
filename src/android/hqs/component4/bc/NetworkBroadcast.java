package android.hqs.component4.bc;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

/**
 * 网络连接状态变化(2G/3G/4G/WiFi) --> 广播接收器
 * @author 胡青松
 */
public abstract class NetworkBroadcast extends BaseBroadcast{
	
	@Override
	public void register(Context context) {
		IntentFilter ift = new IntentFilter();
		ift.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(this, ift);
		setRegister(true);
	}
	
}