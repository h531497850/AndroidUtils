package android.hqs.helper.bc;

import android.content.Context;
import android.content.IntentFilter;

/**
 * GPS --> 广播接收器
 * @author 胡青松
 */
public abstract class GPSBc extends DynamicBroadcast{
	// 下面个字符串是LocationManager中的隐藏常量。
	public static final String EXTRA_GPS_ENABLED = "enabled";
	public static final String GPS_ENABLED_CHANGE_ACTION = "android.location.GPS_ENABLED_CHANGE";
	public static final String GPS_FIX_CHANGE = "android.location.GPS_FIX_CHANGE";

	@Override
	public void register(Context context) {
		IntentFilter gpsFilter = new IntentFilter();
		gpsFilter.addAction(GPS_ENABLED_CHANGE_ACTION);
		gpsFilter.addAction(GPS_FIX_CHANGE);
		context.registerReceiver(this, gpsFilter);
	}
	
}