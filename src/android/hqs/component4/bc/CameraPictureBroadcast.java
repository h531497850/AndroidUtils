package android.hqs.component4.bc;

import android.content.Context;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.hardware.Camera;
import android.hqs.gj.tool.LogTool;
import android.util.Log;

/**
 * 拍照 --> 广播接收器
 * @author 胡青松
 */
public abstract class CameraPictureBroadcast extends BaseBroadcast{
	private final String TAG = LogTool.makeTag(CameraPictureBroadcast.class, getClass());
	
	@Override
	public void register(Context context) {
		IntentFilter ift = new IntentFilter();
		ift.addAction(Camera.ACTION_NEW_PICTURE);
		try {
			ift.addDataType("image/*");
			context.registerReceiver(this, ift);
			setRegister(true);
		} catch (MalformedMimeTypeException e) {
			Log.e(TAG, "read image fail.", e);
		}
	}

}
