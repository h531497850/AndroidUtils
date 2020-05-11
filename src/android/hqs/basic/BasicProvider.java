package android.hqs.basic;

import com.vivo.android.util.LogUtil;

import android.content.ContentProvider;
import android.content.ContentResolver;

/**
 * Android的数据存储方式总共有五种，分别是：Shared Preferences、网络存储、文件存储、外储存储、SQLite。
 * 一般数据只在本身内共享，而其他应用想要共享该应用的的数据，就可用到四大组件之一的{@link #ContentProvider}。
 * 
 * @author huqingsong
 */
public abstract class BasicProvider extends ContentProvider {
	private final String Tag = LogUtil.makeTag(getClass());

	// =================================================================
	// ======================= TODO public methods =====================
	// =================================================================
	/** 获取实例类名 */
	public final String getClsName() {
		return getClass().getSimpleName();
	}

	public ContentResolver getContentResolver() {
		return getContext().getContentResolver();
	}

	// =================================================================
	// ==================== TODO print log methods =====================
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