package android.hqs.helper.bc;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.hardware.Camera;
import android.os.Build;

/**
 * 摄像 --> 接收器
 * @author 胡青松
 */
public abstract class NewVideoBc extends DynamicBroadcast{
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void register(Context context) {
		IntentFilter videoFilter = new IntentFilter();
		videoFilter.addAction(Camera.ACTION_NEW_VIDEO);
		try {
			videoFilter.addDataType("video/*");
		} catch (MalformedMimeTypeException e) {
			error("register", "", e);
		}
		context.registerReceiver(this, videoFilter);
	}

}