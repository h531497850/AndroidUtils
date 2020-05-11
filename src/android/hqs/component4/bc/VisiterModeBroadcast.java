package android.hqs.component4.bc;

import android.content.Context;
import android.content.IntentFilter;

/**
 * 访客模式??? --> 广播接收器，各公司有自己的定义这里仅仅是一个示范。
 * @author 胡青松
 */
public abstract class VisiterModeBroadcast extends BaseBroadcast{
	public static final String ACTION_VISITMODE_SWITCH = "android.settings.VisitMode.action.TURN_ON";
	
	@Override
	public void register(Context context) {
		IntentFilter ift = new IntentFilter();
		ift.addAction(ACTION_VISITMODE_SWITCH);
		context.registerReceiver(this, ift);
		setRegister(true);
	}

}
