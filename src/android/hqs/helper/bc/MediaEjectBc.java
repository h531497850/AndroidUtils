package android.hqs.helper.bc;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * SD卡插拔
 * @author 胡青松
 */
public abstract class MediaEjectBc extends DynamicBroadcast {

	@Override
	public void register(Context context) {
		IntentFilter ift = new IntentFilter();  
		ift.addAction(Intent.ACTION_MEDIA_MOUNTED);
		ift.addAction(Intent.ACTION_MEDIA_EJECT);
		ift.setPriority(1000);
		ift.addDataScheme("file");  
		context.registerReceiver(this , ift);
	}

}
