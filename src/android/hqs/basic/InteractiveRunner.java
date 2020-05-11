package android.hqs.basic;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public abstract class InteractiveRunner extends BasicRunnable {
	private MainHandler mMainHandler = new MainHandler();

	/**
	 * Runs on the UI thread before {@link #doInBackground}.
	 *
	 * @see #onPostExecute
	 * @see #doInBackground
	 */
	protected void onPreExecute() {
	}

	protected abstract void doInBackground();

	@Override
	public void run() {
		mMainHandler.sendEmptyMessage(1);
		doInBackground();
		if (isNext) {
			if (mBundle == null) {
				mMainHandler.sendEmptyMessage(3);
			} else {
				Message msg = mMainHandler.obtainMessage(3);
				msg.setData(mBundle);
				mMainHandler.sendMessage(msg);
			}
		} else {
			mMainHandler.sendEmptyMessage(2);
		}
	}

	/**
	 * <p>
	 * Runs on the UI thread after {@link #doInBackground}. The specified result
	 * is the value returned by {@link #doInBackground}.
	 * </p>
	 * 
	 * <p>
	 * This method won't be invoked if the task was cancelled.
	 * </p>
	 *
	 * @param result
	 *            The result of the operation computed by
	 *            {@link #doInBackground}.
	 *
	 * @see #onPreExecute
	 * @see #doInBackground
	 */
	protected void onPostExecute() {

	}

	/**
	 * Runs on the UI thread
	 */
	protected void onEvent() {

	}

	private boolean isNext;
	private Bundle mBundle;

	/**
	 * Runs on the UI thread
	 */
	protected void onEvent(Bundle b) {
		mBundle = b;
	}

	protected void setEvent(boolean next) {
		isNext = next;
	}

	protected void setEvent(boolean next, Bundle b) {
		isNext = next;
		mBundle = b;
	}

	private class MainHandler extends Handler {
		private MainHandler() {
			super(Looper.getMainLooper());
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				onPreExecute();
				break;
			case 2:
				onPostExecute();
				break;
			case 3:
				Bundle b = msg.getData();
				if (b == null) {
					onEvent();
				} else {
					onEvent(b);
				}
				break;
			default:
				break;
			}
		}
	}
}
