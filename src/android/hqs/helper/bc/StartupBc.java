package android.hqs.helper.bc;

import android.hqs.basic.BasicBroadcast;

/**
 * 接收开机广播，有可能接收不到，注意要在AndroidManifest.xml中注册
 * @author 胡青松
 */
public abstract class StartupBc extends BasicBroadcast {


/*	<receiver android:name="com.bbk.iqoo.logsystem.receiver.StartupReceiver" >
	    <intent-filter>
	        <action android:name="android.intent.action.BOOT_COMPLETED" />
	        <category android:name="android.intent.category.HOME" />
	    </intent-filter>
    </receiver>*/
	
}
