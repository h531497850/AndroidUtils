package android.hqs.component4.service;

import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

public abstract class BackgroundService extends BasicServive {
	private volatile Looper mServiceLooper;
	private volatile BackgroundHandler mServiceHandler;

	private static final byte MSG_CREATE = 1;
	private static final byte MSG_START = MSG_CREATE + 1;

	/**
	 * Child class
	 */
	private static final byte MSG_CHILD = MSG_START + 1;

	private final class BackgroundHandler extends Handler {
		public BackgroundHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_CREATE:
				onInit();
				break;
			case MSG_START:
				onHandleIntent((Intent) msg.obj);
				break;
			default:
				onHandleMessage(msg.what - MSG_CHILD, msg.obj);
				break;
			}
		}
	}

	public void onInit() {
		// TODO Please implement
	}

	/**
	 * Tasks that have not been performed have been cancelled, and then
	 * reschedule the task
	 */
	public void onHandleIntent(final Intent intent) {
		// TODO Please implement
	}

	private int computeWhat(int event) {
		return MSG_CHILD + event;
	}

	/**
	 * Call the method if the subclass has a task that is processed during the
	 * non life cycle. Could not to be rewritten.
	 * <P>
	 * Processing business in {@link #onHandleMessage(int, Object)}, you should
	 * implement it.
	 * 
	 * @param event
	 *            >= 0
	 */
	public final void sendEmptyMessage(int event) {
		int what = computeWhat(event);
		mServiceHandler.removeMessages(what);
		mServiceHandler.sendEmptyMessage(what);
	}

	/**
	 * Processing business in {@link #onHandleMessage(int, Object)}, you should
	 * implement it.
	 */
	public final void sendEmptyMessageDelayed(int event, long delayMillis) {
		int what = computeWhat(event);
		mServiceHandler.removeMessages(what);
		mServiceHandler.sendEmptyMessageDelayed(what, delayMillis);
	}

	/**
	 * Processing business in {@link #onHandleMessage(int, Object)}, you should
	 * implement it.
	 */
	public final void sendMessage(int event, Object obj) {
		sendMessage(event, obj, false);
	}

	/**
	 * Processing business in {@link #onHandleMessage(int, Object)}, you should
	 * implement it.
	 */
	public final void sendMessage(int event, Object obj, boolean remove) {
		int what = computeWhat(event);
		if (remove) {
			mServiceHandler.removeMessages(what);
		}
		Message msg = mServiceHandler.obtainMessage(what, obj);
		mServiceHandler.sendMessage(msg);
	}

	/**
	 * Processing business in {@link #onHandleMessage(int, Object)}, you should
	 * implement it.
	 */
	public final void sendMessageDelayed(int event, long delayMillis, Object obj) {
		int what = computeWhat(event);
		mServiceHandler.removeMessages(what);
		Message msg = mServiceHandler.obtainMessage(what, obj);
		mServiceHandler.sendMessageDelayed(msg, delayMillis);
	}

	/**
	 * Please process your business here.
	 */
	public void onHandleMessage(int event, Object obj) {
		// TODO Please implement
	}

	@Override
	public final void onCreate() {
		info("onCreate");
		super.onCreate();
		HandlerThread thread = new HandlerThread("BackgroundService[" + getClsName() + "]");
		thread.start();
		mServiceLooper = thread.getLooper();
		mServiceHandler = new BackgroundHandler(mServiceLooper);

		mServiceHandler.sendEmptyMessage(MSG_CREATE);
	}
	
	@Override
	public final IBinder onBind(Intent intent) {
		info("onBind");
		mServiceHandler.removeMessages(MSG_START);
		Message msg = mServiceHandler.obtainMessage(MSG_START);
		msg.obj = intent;
		mServiceHandler.sendMessage(msg);
		return null;
	}

	@Override
	public final void onStart(Intent intent, int startId) {
		info("onStartCommand() , startId = " + startId);
		if (intent == null) {
			return;
		}
		// Cancel the tasks that have not yet been performed
		mServiceHandler.removeMessages(MSG_START);
		// Then reschedule the task
		Message msg = mServiceHandler.obtainMessage(MSG_START);
		msg.arg1 = startId;
		msg.obj = intent;
		mServiceHandler.sendMessage(msg);
	}

	/**
	 * You should not override this method for your BackgroundService. Instead,
	 * override {@link #onHandleIntent}, which the system calls when the
	 * BackgroundService receives a start request.
	 * 
	 * @see android.app.Service#onStartCommand
	 */
	@Override
	public final int onStartCommand(Intent intent, int flags, int startId) {
		onStart(intent, startId);
		return START_REDELIVER_INTENT;
	}

	@Override
	public void onDestroy() {
		mServiceLooper.quit();
		info("onDestroy");
	}

}
