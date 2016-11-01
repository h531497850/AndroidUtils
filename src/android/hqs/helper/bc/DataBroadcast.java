package android.hqs.helper.bc;

import android.content.Context;
import android.hqs.basic.BasicBroadcast;

/**
 * 只在当前应用的广播
 * @author 胡青松
 */
public abstract class DataBroadcast extends BasicBroadcast {
	
	/** 外部类调用该方法注册广播}*/
	public abstract void register(Context context);
	/** 外部类调用该方法注销广播和清除数据*/
	public abstract void clearUp(Context context);
	
}
