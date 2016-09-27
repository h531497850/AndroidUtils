package android.hqs.helper.bc;

import android.content.Context;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.hardware.Camera;
import android.hqs.helper.DynamicBroadcast;

/**
 * 拍照 --> 广播接收器
 * @author 胡青松
 */
public abstract class NewPictureBc extends DynamicBroadcast{
	
	@Override
	public void register(Context context) {
		IntentFilter pictureFilter = new IntentFilter();
		pictureFilter.addAction(Camera.ACTION_NEW_PICTURE);
		try {
			pictureFilter.addDataType("image/*");
		} catch (MalformedMimeTypeException e) {
			error("register", "read image fail.", e);
		}
		context.registerReceiver(this, pictureFilter);
	}

}
