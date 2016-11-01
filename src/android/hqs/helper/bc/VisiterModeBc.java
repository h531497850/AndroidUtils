package android.hqs.helper.bc;

import android.content.Context;
import android.content.IntentFilter;

/**
 * 观察者模式??? --> 广播接收器，各公司有自己的定义这里仅仅是一个示范。
 * @author 胡青松
 */
public abstract class VisiterModeBc extends DynamicBroadcast{
	public static final String ACTION_VISITMODE_SWITCH = "android.settings.VisitMode.action.TURN_ON";
	
	@Override
	public void register(Context context) {
		IntentFilter mVisiterModeFilter = new IntentFilter();
		mVisiterModeFilter.addAction(ACTION_VISITMODE_SWITCH);
		context.registerReceiver(this, mVisiterModeFilter);	
	}

}
