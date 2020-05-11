package android.hqs.gj.util;

import java.lang.reflect.Method;

import android.hqs.gj.tool.CmdExecute;
import android.hqs.gj.tool.LogTool;
import android.hqs.gj.tool.TextTool;
import android.util.Log;

/**
 * 获取 系统信息<br>
 * 通過調用 {@link System#getProperties()}來獲取系統的一些相關的屬性。</br>
 * <b>注意：方法名按字母大小排序</b>
 * @author hqs2063594
 */
public class SystemUtil {
	private static final String TAG = LogTool.makeTag(SystemUtil.class);

	/**
	 * 获取操作系统版本。<br>
	 * 调用系统的“"/system/bin/cat"”工具，获取“"/proc/version"”中内容。
	 * @return
	 */
	public static String fetchVersionInfo() {
	    CmdExecute cmdexe = new CmdExecute();
	    String[] args = {"/system/bin/cat", "/proc/version"};
	    return cmdexe.run(args, "system/bin/");
	}
	
	/**
	 * 获取系统信息。<br>
	 * 想要获取系统信息，可以调用其提供的方法 {@link System#getProperty(String)}，
	 * 而系统信息诸如用户根目录（user.home）等都可以通过这个方法获取。
	 * @return
	 */
	public static String getPropertyInfo() {
		StringBuffer sb = new StringBuffer();
		// "java.vendor.url", "java.vendor.url" 前面是描述后面是属性
		sb.append("java.vendor.url").append(":")
		.append(System.getProperty("java.vendor.url")).append("\n")
		.append("java.class.path").append(":")
		.append(System.getProperty("java.class.path"));
		// 等等
		return sb.toString();
	}
	
	/**
	 * 获取系统属性，并设置回调</br>
	 * 通过反射机制加载类及其方法。
	 * * @return 默认返回null。
	 */
	public static String getProperty(String key) {
		return getProperty(key, null);
	}
	
	/**
	 * 获取系统属性。</br>
	 * 通过反射机制加载类及其方法。
	 * @param key 方式
	 * @param def 默认值
	 * @return 返回默认值。
	 */
	public static String getProperty(String key, String def) {
		String value = null;
		try {
			// 1、根据类名加载类
			// 2、根据方法名“get”反射调用类"android.os.SystemProperties"的get方法，get方法的参数是String类型
			Class<?> SystemProperties = Class.forName("android.os.SystemProperties");
			//Method m = clazz.getDeclaredMethod("get", String.class, String.class);
			Method m = SystemProperties.getMethod("get", String.class);
			//value = (String) method.invoke(null, key);
			value = (String) m.invoke(SystemProperties, key, def);
		} catch (Exception e) {
			Log.v(TAG, "Using reflection method to obtain the system property failure.", e);
		}
		if (TextTool.isEmpty(value)) {
			return def;
		}
		return value;
	}
	
	public static void setProperty(String key, String value) {
		try {
			Class<?> SystemProperties = Class.forName("android.os.SystemProperties");
			Method m = SystemProperties.getDeclaredMethod("set", String.class, String.class);
			value = (String) m.invoke(SystemProperties, key, value);
		} catch (Exception e) {
			Log.v(TAG, String.format("failed to setSystemProperty(%s=%s)!", key, value), e);
		}
	}

}
