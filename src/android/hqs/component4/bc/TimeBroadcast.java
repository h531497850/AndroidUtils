package android.hqs.component4.bc;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 时间广播
 * @author 胡青松
 */
public abstract class TimeBroadcast extends BaseBroadcast {

	@Override
	public void register(Context context) {
		IntentFilter ift = new IntentFilter();
		ift.addAction(Intent.ACTION_TIME_CHANGED);		// 时间人为设置改变
		ift.addAction(Intent.ACTION_TIME_TICK);			// 注册监听系统每1分钟发送的广播
		ift.addAction(Intent.ACTION_TIMEZONE_CHANGED);	// 时区改变，这是一个受保护的意图，只能由系统发送。
		context.registerReceiver(this, ift);
		setRegister(true);
	}

}
