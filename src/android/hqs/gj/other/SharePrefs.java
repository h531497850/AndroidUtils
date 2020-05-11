package android.hqs.gj.other;

import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharePrefs {
	
	public static SharedPreferences get(Context context, String fileName) {
		return context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
	}
	
	public static Editor getEditor(Context context, String fileName) {
		return context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit();
	}
	
	// ========================================================================================================
	// ========================================= TODO 字符串 ===================================================
	// ========================================================================================================
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
	 *            MODE_MULTI_PROCESS总是在应用程序针（Android 2.3）以下，并在以后的版本中默认关闭。
	 * @param key
	 * @param value
	 */
	public static boolean setString(Context context, String fileName, String key, String value) {
		return context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit().putString(key, value).commit();
	}
	
	public static boolean setString(Context context, String fileName, Map<String, String> datas) {
		if (datas == null || datas.size() <= 0) {
			return false;
		}
		Editor editor = context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit();
		//Iterator<?> iter = datas.entrySet().iterator();
		/*// 只遍历key
		for (String key : map.keySet()) {
			
		}
		// 遍历key+value
		for (Entry<String, String> entry : map.entrySet()) {
			key = entry.getKey();
			value = entry.getValue();
		}
		// 只遍历value
		for (String value : map.values()) {
			
		}*/
		
		Set<Map.Entry<String, String>> params = datas.entrySet();
		for (Map.Entry<String, String> entry : params) {
			editor.putString(entry.getKey(), entry.getValue());
		}
		return editor.commit();
	}
	
	public static String getString(Context context, String fileName, String key) {
		return context.getSharedPreferences(fileName, Context.MODE_PRIVATE).getString(key, null);
	}
	
	public static String getString(Context context, String fileName, String key, String defValue) {
		return context.getSharedPreferences(fileName, Context.MODE_PRIVATE).getString(key, defValue);
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
		return context.getSharedPreferences(fileName, Context.MODE_PRIVATE).getBoolean(key, false);
	}
	public static boolean getBoolean(Context context, String fileName, String key, boolean defValue) {
		return context.getSharedPreferences(fileName, Context.MODE_PRIVATE).getBoolean(key, defValue);
	}
	
	// ========================================================================================================
	// ========================================= TODO 整数 =====================================================
	// ========================================================================================================
	public static boolean setInt(Context context, String fileName, String key, int value) {
		return context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit().putInt(key, value).commit();
	}
	public static boolean setInt(Context context, String fileName, Map<String, Integer> datas) {
		if (datas == null || datas.size() <= 0) {
			return false;
		}
		Editor editor = context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit();
		Set<Map.Entry<String, Integer>> params = datas.entrySet();
		for (Map.Entry<String, Integer> entry : params) {
			editor.putInt(entry.getKey(), entry.getValue());
		}
		return editor.commit();
	}
	/**
	 * @return 默认返回值为0
	 */
	public static int getInt(Context context, String fileName, String key){
		return context.getSharedPreferences(fileName, Context.MODE_PRIVATE).getInt(key, 0);
	}
	public static int getInt(Context context, String fileName, String key, int defValue){
		return context.getSharedPreferences(fileName, Context.MODE_PRIVATE).getInt(key, defValue);
	}
	
	public static boolean setLong(Context context, String fileName, String key, long value) {
		return context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit().putLong(key, value).commit();
	}
	
	public static boolean remove(Context context, String fileName, String... keys){
		if (keys == null || keys.length <= 0) {
			return false;
		}
		Editor editor = context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit();
		for (String key : keys) {
			editor.remove(key);
		}
		return editor.commit();
	}
	
	public static boolean clear(Context context, String fileName){
		return context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit().clear().commit();
	}

}
