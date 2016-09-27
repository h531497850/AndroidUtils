package android.hqs.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharePrefHelper {
	
	// ========================================================================================================
	// ========================================= TODO 字符串 ===================================================
	// ========================================================================================================
	public static void setString(Context context, String fileName, String key, String value) {
		setString(context, fileName, Context.MODE_PRIVATE, key, value);
	}
	
	/**
	 * 保存数据字符串
	 * 
	 * @param fileName
	 *            所需的首选项文件。如果该文件的名称不存在，在你调用{@link android.content.SharedPreferences.Editor#edit()}时
	 *            会自动创建一个编辑器，然后通过{@link android.content.SharedPreferences.Editor#commit()()}提交更改。
	 * @param mode
	 *            操作模式。使用0个或{@link android.content.Context#MODE_PRIVATE}为默认操作，
	 *            {@link android.content.Context#MODE_WORLD_READABLE}和{@link android.content.Context#MODE_WORLD_WRITEABLE}为权限控制。
	 *            如果多个进程同时使用SharedPreferences文件也可以是{@link android.content.Context#MODE_MULTI_PROCESS}，
	 *            MODE_MULTI_PROCESS总是在应用程序针（Android
	 *            2.3）以下，并在以后的版本中默认关闭。
	 * @param key
	 * @param value
	 */
	public static void setString(Context context, String fileName, int mode, String key, String value) {
		SharedPreferences sp = context.getSharedPreferences(fileName, mode);
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static String getString(Context context, String fileName, String key) {
		return getString(context, fileName, Context.MODE_PRIVATE, key);
	}
	
	public static String getString(Context context, String fileName, int mode, String key) {
		return getString(context, fileName, mode, key, "");
	}
	
	public static String getString(Context context, String fileName, String key, String defValue) {
		return getString(context, fileName, Context.MODE_PRIVATE, key, defValue);
	}
	
	public static String getString(Context context, String fileName, int mode, String key, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(fileName, mode);
		return sp.getString(key, defValue);
	}
	
	// ========================================================================================================
	// ========================================= TODO 布尔 =====================================================
	// ========================================================================================================
	/**
	 * 获取布尔型变量。默认读取mode为{@link android.content.Context#MODE_PRIVATE};
	 * @param context
	 * @param filefileName 文件名
	 * @param key 键，通过该键找到你所要的值。
	 * @return 默认值为false。
	 */
	public static boolean getBoolean(Context context, String fileName, String key) {
		return getBoolean(context, fileName, Context.MODE_PRIVATE, key);
	}
	public static boolean getBoolean(Context context, String fileName, int mode, String key) {
		return getBoolean(context, fileName, mode, key, false);
	}
	public static boolean getBoolean(Context context, String fileName, String key, boolean defValue) {
		return getBoolean(context, fileName, Context.MODE_PRIVATE, key, defValue);
	}
	public static boolean getBoolean(Context context, String fileName, int mode, String key, boolean defValue) {
		SharedPreferences sp = context.getSharedPreferences(fileName, mode);
		return sp.getBoolean(key, defValue);
	}
	
	// ========================================================================================================
	// ========================================= TODO 整数 =====================================================
	// ========================================================================================================
	public static void setInt(Context context, String fileName, String key, int value) {
		setInt(context, fileName, Context.MODE_PRIVATE, key, value);
	}
	public static void setInt(Context context, String fileName, int mode, String key, int value) {
		SharedPreferences sp = context.getSharedPreferences(fileName, mode);
		Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	/**
	 * @return 默认返回值为0
	 */
	public static int getInt(Context context, String fileName, String key){
		return getInt(context, fileName, Context.MODE_PRIVATE, key, 0);
	}
	public static int getInt(Context context, String fileName, String key, int defValue){
		return getInt(context, fileName, Context.MODE_PRIVATE, key, defValue);
	}
	public static int getInt(Context context, String fileName, int mode, String key, int defValue){
		SharedPreferences sp = context.getSharedPreferences(fileName, mode);
		return sp.getInt(key, defValue);
	}
	
	public static void remove(Context context, String fileName, String... keys){
		SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		for (String key : keys) {
			editor.remove(key);
		}
		editor.commit();
	}


}
