package android.hqs.widget.clock;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ClockService extends Service {

	private Timer mTimer;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mTimer = new Timer();
		mTimer.schedule(updateView, 0, 1000);
	}
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		// 停止TimerTask
		mTimer = null;
	}
	
	private TimerTask updateView = new TimerTask() {
		@Override
		public void run() {
			/*String time = FormatUtil.time(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
			RemoteViews rv = new RemoteViews(getPackageName(), R.layout.widget_clock);
			rv.setTextViewText(R.id.wc_tv_clock, time);
			AppWidgetManager manager = AppWidgetManager.getInstance(ClockService.this);
			ComponentName cn = new ComponentName(ClockService.this, ClockProvider.class);
			manager.updateAppWidget(cn, rv);*/
		}
	};

}
