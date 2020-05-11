package android.hqs.basic;

import com.vivo.android.util.LogUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;

public abstract class BasicAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
	private final String Tag = LogUtil.makeTag(getClass());

	protected final Context context;

	/**
	 * 构造
	 * 
	 * @param context
	 *            不能为null
	 * @throws NullPointerException
	 *             如果上下文为null
	 */
	public BasicAsyncTask(Context context) {
		if (context == null) {
			throw new NullPointerException("context can not be null!");
		}
		this.context = context;
	}

	// =================================================================
	// ====================== TODO public methods ======================
	// =================================================================
	/** 获取实例类名 */
	public final String getClsName() {
		return getClass().getSimpleName();
	}

	public PackageManager getPackageManager() {
		return context.getPackageManager();
	}

	public PackageInfo getPackageInfo(String packageName, int flags) throws NameNotFoundException {
		return context.getPackageManager().getPackageInfo(packageName, flags);
	}

	/**
	 * 默认使用{@link Context#MODE_PRIVATE}模式
	 * 
	 * @param name
	 *            xml文件名
	 */
	public SharedPreferences getSharedPreferences(String name) {
		return context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}

	public SharedPreferences getSharedPreferences(String name, int mode) {
		return context.getSharedPreferences(name, mode);
	}

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