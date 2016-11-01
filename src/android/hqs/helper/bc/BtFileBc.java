package android.hqs.helper.bc;

import android.content.Context;
import android.content.IntentFilter;

/**
 * 蓝牙分为传文件和听声音两种不同用途，分别对应不同的广播。
 * <b>这里是（传文件）</b>，各公司有自己的定义这里仅仅是一个示范。
 * @author 胡青松
 */
public abstract class BtFileBc extends DynamicBroadcast {
	public static final String MTK_BLUETOOTH_START_ACTION = "android.bluetooth.profilemanager.action.STATE_CHANGED";
	public static final String QCOM_BLUETOOTH_START_ACTION = "android.btopp.intent.action.VIVO_FILE_TRANSFER_START";
	public static final String QCOM_BLUETOOTH_END_ACTION = "android.btopp.intent.action.VIVO_FILE_TRANSFER_STOP";
	
	@Override
	public void register(Context context) {
		IntentFilter btoothFilter = new IntentFilter();
		btoothFilter.addAction(MTK_BLUETOOTH_START_ACTION);
		btoothFilter.addAction(QCOM_BLUETOOTH_START_ACTION);
		btoothFilter.addAction(QCOM_BLUETOOTH_END_ACTION);
		context.registerReceiver(this, btoothFilter);
	}
	
}