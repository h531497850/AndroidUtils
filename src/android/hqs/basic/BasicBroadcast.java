package android.hqs.basic;

import com.vivo.android.util.LogUtil;

import android.content.BroadcastReceiver;

public abstract class BasicBroadcast extends BroadcastReceiver {
	private final String Tag = LogUtil.makeTag(getClass());

	// =================================================================
	// ======================= TODO public method ======================
	// =================================================================
	/** Fetch instance class name */
	public final String getClsName() {
		return getClass().getSimpleName();
	}

	// =================================================================
	// ====================== TODO print log method ====================
	// =================================================================
	/** Blue, debug information */
	protected final void debug(Object obj) {
		LogUtil.debug(Tag, obj);
	}

	protected final void debug(Object obj, Throwable tr) {
		LogUtil.debug(Tag, obj, tr);
	}

	/** Green, normal information */
	protected final void info(Object obj) {
		LogUtil.info(Tag, obj);
	}

	protected final void info(Object obj, Throwable tr) {
		LogUtil.info(Tag, obj, tr);
	}

	/** Black, long message */
	protected final void verbose(Object obj) {
		LogUtil.verbose(Tag, obj);
	}

	protected final void verbose(Object obj, Throwable tr) {
		LogUtil.verbose(Tag, obj, tr);
	}

	/** Red, error message */
	protected final void error(Object obj) {
		LogUtil.error(Tag, obj);
	}

	protected final void error(Object obj, Throwable tr) {
		LogUtil.error(Tag, obj, tr);
	}

}