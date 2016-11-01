package android.hqs.helper.bc;

import android.content.Context;
import android.content.IntentFilter;

/**
 * 音量变化 --> 广播接收器，各公司有自己的定义这里仅仅是一个示范。
 * @author 胡青松
 */
public abstract class AudioVolumeChangeBc extends DynamicBroadcast{
	
	// 下面的3个是AudioManager类中的隐藏常量
	public static final String VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION";
	public static final String EXTRA_VOLUME_STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE";
	public static final String EXTRA_VOLUME_STREAM_VALUE = "android.media.EXTRA_VOLUME_STREAM_VALUE";
	
	@Override
	public void register(Context context) {
		IntentFilter audioFilter = new IntentFilter();
		audioFilter.addAction(VOLUME_CHANGED_ACTION);
		context.registerReceiver(this, audioFilter);
	}

}
