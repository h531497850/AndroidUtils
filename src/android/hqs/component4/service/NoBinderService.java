package android.hqs.component4.service;

import android.content.Intent;
import android.os.IBinder;

/**
 * 子类将不再实现{@link #onBind(Intent)}接口，如果要实现请复写{@link android.hqs.component4.service.BasicServive}
 * @author 胡青松
 *
 */
public class NoBinderService extends BasicServive {

	@Override
	public final IBinder onBind(Intent intent) {
		return null;
	}

}
