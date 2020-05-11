package android.hqs.component4.bc;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * SD卡插拔
 * @author 胡青松
 */
public abstract class MediaBroadcast extends BaseBroadcast {

	@Override
	public void register(Context context) {
		IntentFilter ift = new IntentFilter();
		// 外部媒体存在，并安装在其安装点，共享媒体的路径到安装点包含Intent.mData域。
		// 如果媒体被安装为只读，那么该intent包含一个额外的名称“只读”和布尔值。
		ift.addAction(Intent.ACTION_MEDIA_MOUNTED);
		ift.addAction(Intent.ACTION_MEDIA_EJECT);		// 将要删除SD卡，在ACTION_MEDIA_UNMOUNTED之前
		ift.addAction(Intent.ACTION_MEDIA_UNMOUNTED);	// 外部媒体是存在的，但没有安装（被卸载等）。
		// 通过USB大量存储共享导致外部媒体未被安装，共享媒体的路径到安装点包含Intent.mData域。
		ift.addAction(Intent.ACTION_MEDIA_SHARED);
		ift.setPriority(1000);
		ift.addDataScheme("file");  
		context.registerReceiver(this, ift);
		setRegister(true);
	}

}
