package android.hqs.basic;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import com.vivo.android.util.LogUtil;

import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.app.IProcessObserver;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 1、在第一次调用该服务时进入方法{@link #onCreate()}，之后不再调用，在这里可以作初始化操作；</br>
 * 2、注意在生命周期结束，即进入方法{@link #onDestroy()}时，释放内存(清空数组、链表、堆栈等，清空各种索引)，
 * 在onDestroy是否调用{@link System#exit(int)}参数为0，或{@link System#gc()}来杀死进程视情况而定。
 * 
 * @author huqingsong
 */
public abstract class BasicServive extends Service {
	private final String Tag = LogUtil.makeTag(getClass());

	// =================================================================
	// ======================= TODO public method ======================
	// =================================================================
	/** Fetch instance class name */
	public final String getClsName() {
		return getClass().getSimpleName();
	}

	public SharedPreferences getPrefs(String name) {
		return getSharedPreferences(name, Context.MODE_PRIVATE);
	}

	public SharedPreferences getPrefs(String name, int mode) {
		return getSharedPreferences(name, mode);
	}

	@Override
	public void unregisterReceiver(BroadcastReceiver receiver) {
		if (receiver == null) {
			return;
		}
		try {
			// Avoid errors without registration
			super.unregisterReceiver(receiver);
		} catch (Exception e) {
		}
	}

	/**
	 * main thread
	 * 
	 * @param name
	 *            xml file name
	 */
	public boolean putInt(String name, String key, int value) {
		return putInt(name, Context.MODE_PRIVATE, key, value);
	}

	/**
	 * main thread
	 * 
	 * @param name
	 *            xml file name
	 */
	public boolean putInt(String name, int mode, String key, int value) {
		return getSharedPreferences(name, mode).edit().putInt(key, value).commit();
	}

	/**
	 * main thread
	 * 
	 * @param name
	 *            xml file name
	 */
	public boolean putInt(String name, Map<String, Integer> params) {
		return putInt(name, Context.MODE_PRIVATE, params);
	}

	/**
	 * main thread
	 * 
	 * @param name
	 *            xml file name
	 */
	public boolean putInt(String name, int mode, Map<String, Integer> params) {
		if (params == null || params.size() <= 0) {
			return false;
		}
		SharedPreferences.Editor spe = getSharedPreferences(name, mode).edit();
		for (Map.Entry<String, Integer> entry : params.entrySet()) {
			if (entry != null) {
				spe.putInt(entry.getKey(), entry.getValue());
			}
		}
		return spe.commit();
	}

	public boolean putInt(SharedPreferences sp, String key, int value) {
		return sp.edit().putInt(key, value).commit();
	}

	public boolean putInt(SharedPreferences.Editor spe, String key, int value) {
		return spe.putInt(key, value).commit();
	}

	/**
	 * child thread
	 * 
	 * @param name
	 *            xml file name
	 */
	public void applyInt(String name, String key, int value) {
		applyInt(name, Context.MODE_PRIVATE, key, value);
	}

	/**
	 * child thread
	 * 
	 * @param name
	 *            xml file name
	 */
	public void applyInt(String name, int mode, String key, int value) {
		getSharedPreferences(name, mode).edit().putInt(key, value).apply();
	}

	/**
	 * @param name
	 *            xml file name
	 * @return defult 0
	 */
	public int getInt(String name, String key) {
		return getInt(name, key, 0);
	}

	/**
	 * @param name
	 *            xml file name
	 */
	public int getInt(String name, String key, int defValue) {
		return getInt(name, Context.MODE_PRIVATE, key, defValue);
	}

	/**
	 * @param name
	 *            xml file name
	 */
	public int getInt(String name, int mode, String key, int defValue) {
		return getSharedPreferences(name, mode).getInt(key, defValue);
	}

	/**
	 * main thread
	 * 
	 * @param name
	 *            xml file name
	 */
	public boolean putLong(String name, String key, long value) {
		return putLong(name, Context.MODE_PRIVATE, key, value);
	}

	/**
	 * main thread
	 * 
	 * @param name
	 *            xml file name
	 */
	public boolean putLong(String name, int mode, String key, long value) {
		return getSharedPreferences(name, mode).edit().putLong(key, value).commit();
	}

	/**
	 * child thread
	 * 
	 * @param name
	 *            xml file name
	 */
	public void applyLong(String name, String key, long value) {
		applyLong(name, Context.MODE_PRIVATE, key, value);
	}

	/**
	 * child thread
	 * 
	 * @param name
	 *            xml file name
	 */
	public void applyLong(String name, int mode, String key, long value) {
		getSharedPreferences(name, mode).edit().putLong(key, value).apply();
	}

	/**
	 * @param name
	 *            xml file name
	 * @return defult 0
	 */
	public long getLong(String name, String key) {
		return getLong(name, key, 0L);
	}

	/**
	 * @param name
	 *            xml file name
	 */
	public long getLong(String name, String key, long defValue) {
		return getLong(name, Context.MODE_PRIVATE, key, defValue);
	}

	/**
	 * @param name
	 *            xml file name
	 */
	public long getLong(String name, int mode, String key, long defValue) {
		return getSharedPreferences(name, mode).getLong(key, defValue);
	}

	/**
	 * main thread
	 * 
	 * @param name
	 *            xml file name
	 */
	public boolean putString(String name, String key, String value) {
		return putString(name, Context.MODE_PRIVATE, key, value);
	}

	/**
	 * main thread
	 * 
	 * @param name
	 *            xml file name
	 */
	public boolean putString(String name, int mode, String key, String value) {
		return getSharedPreferences(name, mode).edit().putString(key, value).commit();
	}

	/**
	 * child thread
	 * 
	 * @param name
	 *            xml file name
	 */
	public void applyString(String name, String key, String value) {
		applyString(name, Context.MODE_PRIVATE, key, value);
	}

	/**
	 * child thread
	 * 
	 * @param name
	 *            xml file name
	 */
	public void applyString(String name, int mode, String key, String value) {
		getSharedPreferences(name, mode).edit().putString(key, value).apply();
	}

	/**
	 * @param name
	 *            xml file name
	 * @return defult null
	 */
	public String getString(String name, String key) {
		return getString(name, key, null);
	}

	/**
	 * @param name
	 *            xml file name
	 */
	public String getString(String name, String key, String defValue) {
		return getString(name, Context.MODE_PRIVATE, key, defValue);
	}

	/**
	 * @param name
	 *            xml file name
	 */
	public String getString(String name, int mode, String key, String defValue) {
		return getSharedPreferences(name, mode).getString(key, defValue);
	}

	/**
	 * main thread
	 * 
	 * @param name
	 *            xml file name
	 */
	public boolean remove(String name, String key) {
		return getSharedPreferences(name, Context.MODE_PRIVATE).edit().remove(key).commit();
	}

	/**
	 * main thread
	 * 
	 * @param name
	 *            xml file name
	 */
	public boolean remove(String name, Set<String> keys) {
		if (keys == null || keys.size() <= 0) {
			return false;
		}
		SharedPreferences.Editor spe = getSharedPreferences(name, Context.MODE_PRIVATE).edit();
		for (String key : keys) {
			spe.remove(key);
		}
		return spe.commit();
	}

	/**
	 * child thread
	 * 
	 * @param name
	 *            xml file name
	 */
	public void applyRemove(String name, String key) {
		getSharedPreferences(name, Context.MODE_PRIVATE).edit().remove(key).apply();
	}

	/**
	 * main thread
	 * 
	 * @param name
	 *            xml file name
	 */
	public boolean clear(String name) {
		return getSharedPreferences(name, Context.MODE_PRIVATE).edit().clear().commit();
	}

	/**
	 * child thread
	 * 
	 * @param name
	 *            xml file name
	 */
	public void applyClear(String name) {
		getSharedPreferences(name, Context.MODE_PRIVATE).edit().clear().apply();
	}

	protected void registerProcessObserver(IProcessObserver ipo) {
		if (ipo == null) {
			return;
		}
		try {
			ActivityManagerNative.getDefault().registerProcessObserver(ipo);
		} catch (Exception e) {
			debug("", e);
		}
	}

	protected void unregisterProcessObserver(IProcessObserver ipo) {
		if (ipo == null) {
			return;
		}
		try {
			ActivityManagerNative.getDefault().unregisterProcessObserver(ipo);
		} catch (Exception e) {
			debug("", e);
		}
	}

	public int getInstanceId(int pid) {
		IActivityManager amn = ActivityManagerNative.getDefault();
		try {
			// Class<?> amn = Class.forName("android.app.ActivityManagerNative");
			// instanceId = amn.getInstanceId(pid);
			Method method = amn.getClass().getMethod("getInstanceId", int.class);
			return (int) method.invoke(amn, pid);
		} catch (Exception e) {
			return 0;
		}
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