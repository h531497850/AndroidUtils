package android.hqs.component4.bc;

import android.content.Context;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.hardware.Camera;
import android.hqs.gj.tool.LogTool;
import android.util.Log;

/**
 * 摄像 --> 接收器
 * @author 胡青松
 */
public abstract class CameraVideoBroadcast extends BaseBroadcast{
	private final String TAG = LogTool.makeTag(CameraVideoBroadcast.class, getClass());
	
	@Override
	public void register(Context context) {
		IntentFilter videoFilter = new IntentFilter();
		videoFilter.addAction(Camera.ACTION_NEW_VIDEO);
		try {
			videoFilter.addDataType("video/*");
			context.registerReceiver(this, videoFilter);
			setRegister(true);
		} catch (MalformedMimeTypeException e) {
			Log.e(TAG, "read video fail.", e);
		}
	}

}