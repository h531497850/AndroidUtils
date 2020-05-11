package android.hqs.component4.service;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.hqs.gj.tool.LogTool;
import android.hqs.gj.tool.TextTool;
import android.util.Log;

/**
 * 1、在第一次调用该服务是进入方法{@link #onCreate()}，之后不再调用，在这里可以作初始化操作；</br>
 * 2、注意在生命周期结束，即进入方法{@link #onDestroy()}时，释放内存(清空数组、链表、堆栈等，清空各种索引)，
 * 在onDestroy是否调用{@link System#exit(int)}参数为0，或{@link System#gc()}来杀死进程视情况而定。
 * 
 * @author 胡青松
 */
public abstract class BasicServive extends Service {
	private final String TAG = LogTool.makeTag(BasicServive.class, getClass());
	private final String Tag = LogTool.makeTag(getClass());

	// =================================================================
	// ======================= TODO public method ======================
	// =================================================================
	/** 获取实例类名 */
	public final String getClsName() {
		return getClass().getSimpleName();
	}

	/** 根据目标程序的包名来获取其程序的上下文 */
	public Context getTargetContext(String pkgName) throws NameNotFoundException {
		return createPackageContext(pkgName, Context.CONTEXT_IGNORE_SECURITY);
	}

	@SuppressWarnings("deprecation")
	public SharedPreferences getTargetPrefs(String pkgName, String fileName) throws NameNotFoundException {
		return getTargetContext(pkgName).getSharedPreferences(fileName,
				Context.MODE_WORLD_READABLE | Context.MODE_MULTI_PROCESS);
	}

	public SharedPreferences getPrefs(String fileName) {
		return getSharedPreferences(fileName, Context.MODE_PRIVATE);
	}

	/**
	 * 删除数据
	 * 
	 * @param fileName
	 *            文件名
	 * @param keys
	 *            键数组
	 * @return 是否 删除成功
	 */
	protected boolean removePrefsValue(String fileName, String... keys) {
		if (TextTool.isNull(fileName) || keys == null || keys.length == 0) {
			Log.e(TAG, "The file or keys are not exsits!");
			return false;
		}
		Editor editor = getPrefs(fileName).edit();
		for (String key : keys) {
			editor.remove(key);
		}
		return editor.commit();
	}

	public boolean clearPrefs(String fileName) {
		if (TextTool.isNull(fileName)) {
			return false;
		}
		Editor editor = getPrefs(fileName).edit();
		return editor.clear().commit();
	}

	// =================================================================
	// ====================== TODO print log method ====================
	// =================================================================
	/** 蓝色，调试信息 */
	protected final void debug(Object obj) {
		LogTool.debug(Tag, obj);
	}

	protected final void debug(Object obj, Throwable tr) {
		LogTool.debug(Tag, obj, tr);
	}

	/** 绿色，正常信息 */
	protected final void info(Object obj) {
		LogTool.info(Tag, obj);
	}

	protected final void info(Object obj, Throwable tr) {
		LogTool.info(Tag, obj, tr);
	}

	protected void info(String listName, byte[] list) {
		LogTool.info(Tag, listName, list);
	}

	protected void info(String listName, int[] list) {
		LogTool.info(Tag, listName, list);
	}

	/** 黑色，冗长信息 */
	protected final void verbose(Object obj) {
		LogTool.verbose(Tag, obj);
	}

	protected final void verbose(Object obj, Throwable tr) {
		LogTool.verbose(Tag, obj, tr);
	}

	/** 红色，错误信息 */
	protected final void error(Object obj) {
		LogTool.error(Tag, obj);
	}

	protected final void error(Object obj, Throwable tr) {
		LogTool.error(Tag, obj, tr);
	}

	/** 紫色，不应发生的信息 */
	protected final void wtf(Object obj) {
		LogTool.wtf(Tag, obj);
	}

	protected final void wtf(Object obj, Throwable tr) {
		LogTool.wtf(Tag, obj, tr);
	}

}