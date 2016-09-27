package android.hqs.helper.bc;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hqs.helper.DynamicBroadcast;

/**
 * 注册SD卡是挂载、卸载的广播
 * @author 胡青松
 */
public abstract class MediaMountedBc extends DynamicBroadcast{

	@Override
	public void register(Context context) {
		IntentFilter otgFilter = new IntentFilter();
		otgFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		otgFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		otgFilter.addDataScheme("file");
		context.registerReceiver(this, otgFilter); 
	}
	
}
