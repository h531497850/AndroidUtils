package android.hqs.basic;

import android.annotation.Nullable;
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

	private int computeWhat(int event) {
		return MSG_CHILD + event;
	}

	private int computeEvent(int what) {
		return what - MSG_CHILD;
	}

	/**
	 * Cancel the tasks that have not yet been performed task
	 */
	private boolean isCancelNotPerformedIntent;

	public boolean isCancelNotPerformed() {
		return isCancelNotPerformedIntent;
	}

	/**
	 * If set to true, the tasks that are not executed in
	 * {@link #onStart(Intent, int)}and{@link #onStartCommand(Intent, int, int)}
	 * will be removed.
	 * 
	 * @param isCancelNotPerformedIntent
	 *            default false
	 */
	public void setCancelNotPerformed(boolean isCancelNotPerformedIntent) {
		this.isCancelNotPerformedIntent = isCancelNotPerformedIntent;
	}

	private final class BackgroundHandler extends Handler {
		private BackgroundHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_CREATE:
				onInit();
				break;
			case MSG_START:
				if (msg.obj != null && msg.obj instanceof Intent) {
					onHandleIntent((Intent) msg.obj);
				}
				break;
			default:
				onHandleMessage(computeEvent(msg.what), msg.obj);
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
	 * 
	 * @param intent
	 *            Any status will not be empty
	 */
	public void onHandleIntent(@Nullable Intent intent) {
		// TODO Please implement
	}

	/**
	 * Please process your business here.
	 */
	public void onHandleMessage(int event, Object obj) {
		// TODO Please implement
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
		sendEmptyMessage(event, true);
	}

	public final void sendEmptyMessage(int event, boolean isCancelNotPerformed) {
		int what = computeWhat(event);
		if (isCancelNotPerformed) {
			mServiceHandler.removeMessages(what);
		}
		mServiceHandler.sendEmptyMessage(what);
	}

	/**
	 * Processing business in {@link #onHandleMessage(int, Object)}, you should
	 * implement it.
	 */
	public final void sendEmptyMessageDelayed(int event, long delayMillis) {
		sendEmptyMessageDelayed(event, delayMillis, true);
	}

	public final void sendEmptyMessageDelayed(int event, long delayMillis, boolean isCancelNotPerformed) {
		int what = computeWhat(event);
		if (isCancelNotPerformed) {
			mServiceHandler.removeMessages(what);
		}
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
	public final void sendMessage(int event, Object obj, boolean isCancelNotPerformed) {
		int what = computeWhat(event);
		if (isCancelNotPerformed) {
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
		sendMessageDelayed(event, delayMillis, obj, false);
	}

	public final void sendMessageDelayed(int event, long delayMillis, Object obj, boolean isCancelNotPerformed) {
		int what = computeWhat(event);
		mServiceHandler.removeMessages(what);
		Message msg = mServiceHandler.obtainMessage(what, obj);
		mServiceHandler.sendMessageDelayed(msg, delayMillis);
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
		if (intent == null) {
			error("onBind Do nothing.");
			return null;
		}
		mServiceHandler.removeMessages(MSG_START);
		Message msg = mServiceHandler.obtainMessage(MSG_START);
		msg.obj = intent;
		mServiceHandler.sendMessage(msg);
		return null;
	}

	@Override
	public final void onStart(Intent intent, int startId) {
		info("onStart() , startId = " + startId);
		if (intent == null) {
			error("onStart Do nothing.");
			return;
		}
		if (isCancelNotPerformedIntent) {
			mServiceHandler.removeMessages(MSG_START);
		}
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
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		mServiceLooper.quit();
		super.onDestroy();
	}

}
