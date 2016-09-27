package android.hqs.helper.bc;

import android.content.Context;
import android.content.IntentFilter;
import android.hqs.helper.DynamicBroadcast;

/**
 * 闹钟 --> 广播接收器，各公司有自己的定义这里仅仅是一个示范。
 * @author 胡青松
 */
public abstract class AlarmBc extends DynamicBroadcast{
	
	protected final String ALARM_START_ACTION = "com.cn.google.AlertClock.ALARM_ALERT_begin";
	protected final String ALARM_END_ACTION = "com.cn.google.AlertClock.ALARM_ALERT_end";
	
	@Override
	public void register(Context context) {
		IntentFilter ift = new IntentFilter();
		ift.addAction(ALARM_START_ACTION);
		ift.addAction(ALARM_END_ACTION);
		context.registerReceiver(this, ift);
	}

}