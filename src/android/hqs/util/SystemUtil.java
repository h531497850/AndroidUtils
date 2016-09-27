package android.hqs.util;

import java.lang.reflect.Method;

import android.hqs.tool.CmdExecute;
import android.hqs.tool.TextTool;
import android.util.Log;

/**
 * 获取 系统信息<br>
 * 通過調用 {@link System#getProperties()}來獲取系統的一些相關的屬性。
 * @author hqs2063594
 *
 */
public class SystemUtil {
	
	private static final String TAG = "SystemUtil";

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
		StringBuffer buffer = new StringBuffer();
		initProperty(buffer, "java.vendor.url", "java.vendor.url");
		initProperty(buffer, "java.class.path", "java.class.path");
		// 等等
		return buffer.toString();
	}
	private static void initProperty(StringBuffer buffer, String description, String property) {
		buffer.append(description).append(":");
		buffer.append(System.getProperty(property)).append("\n");
	}
	
	/**
	 * 获取系统属性。</br>
	 * 通过反射机制加载类及其方法。
	 * @param key 方式
	 * @param def 默认值
	 * @return
	 */
	public static String getProperty(String key, String def) {
		String value = "";
		try {
			// 1、根据类名加载类
			// 2、根据方法名“get”反射调用类"android.os.SystemProperties"的get方法，get方法的参数是String类型
			Method method = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
			value = (String) method.invoke(null, key);
		} catch (Exception e) {
			Log.e(TAG, "getProperties", e);
		}
		if (TextTool.isEmpty(value)) {
			return def;
		}
		return value;
	}
	
	/**
	 * 获取系统属性，并设置回调</br>
	 * 通过反射机制加载类及其方法。
	 */
	public static String getPropertyPlusReceiver(String key) {
		String value = "";
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method m = c.getDeclaredMethod("get", String.class);
			value = (String) m.invoke(c, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public static void setProperty(String key, String value) {
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method m = c.getDeclaredMethod("set", String.class, String.class);
			value = (String) m.invoke(c, key, value);
		} catch (Exception e) {
			Log.e(TAG, String.format("failed to setSystemProperty(%s=%s)!", key, value), e);
		}
	}
	
}
