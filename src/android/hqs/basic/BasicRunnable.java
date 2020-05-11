package android.hqs.basic;

import com.vivo.android.util.LogUtil;

public abstract class BasicRunnable implements Runnable {
	private final String Tag = LogUtil.makeTag(getClass());

	// =================================================================
	// =================== TODO print log methods ======================
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
