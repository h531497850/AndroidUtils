package android.hqs.helper.bc;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 应用包状态信息 --> 广播接收器
 * @author 胡青松
 */
public abstract class AppStateBc extends DynamicBroadcast{
    
    @Override
    public void register(Context context) {
    	IntentFilter ift = new IntentFilter();
        ift.addAction(Intent.ACTION_PACKAGE_ADDED);				// 添加
        ift.addAction(Intent.ACTION_PACKAGE_REMOVED);			// 卸载
        ift.addAction(Intent.ACTION_PACKAGE_FULLY_REMOVED);		// 完全卸载
        ift.addAction(Intent.ACTION_PACKAGE_REPLACED);			// 更新
        ift.addAction(Intent.ACTION_PACKAGE_RESTARTED);			// 应用重启
        ift.addAction(Intent.ACTION_PACKAGE_DATA_CLEARED);		// 清除应用数据
        ift.addAction(Intent.ACTION_PACKAGE_FIRST_LAUNCH);		// 第一次启动
        ift.addDataScheme("package");
        context.registerReceiver(this, ift);
    }

}
