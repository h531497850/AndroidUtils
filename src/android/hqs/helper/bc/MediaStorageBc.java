package android.hqs.helper.bc;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hqs.helper.DynamicBroadcast;

/**
 * SD卡存储 --> 广播接收器
 * @author 胡青松
 */
public abstract class MediaStorageBc extends DynamicBroadcast{
	
	@Override
	public void register(Context context) {
		IntentFilter storageFilter = new IntentFilter();
		// 通过USB大量存储导致外部媒体未被安装，共享媒体的路径到安装点包含Intent.mData域。
		storageFilter.addAction(Intent.ACTION_MEDIA_SHARED);
		// 外部媒体存在，并安装在其安装点，共享媒体的路径到安装点包含Intent.mData域。
		// 如果媒体被安装为只读，那么该intent包含一个额外的名称“只读”和布尔值。
		storageFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		storageFilter.addDataScheme("file");
		context.registerReceiver(this, storageFilter);
	}

}