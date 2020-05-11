package android.hqs.component4.bc;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 电池状态 --> 广播接收器
 * @author 胡青松
 */
public abstract class BatteryBroadcast extends BaseBroadcast{
	
	@Override
	public void register(Context context) {
		IntentFilter ift = new IntentFilter();
		ift.addAction(Intent.ACTION_BATTERY_CHANGED);		// 充电状态					只能动态注册
		ift.addAction(Intent.ACTION_POWER_CONNECTED);		// 外部电源已连接到设备	
        ift.addAction(Intent.ACTION_POWER_DISCONNECTED);	// 外部电源已从设备中删除
        context.registerReceiver(this, ift);
        setRegister(true);
	}

}
