package android.hqs.basic;

import com.vivo.android.tool.TextTool;
import com.vivo.android.util.LogUtil;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.display.DisplayManager;
import android.hardware.input.InputManager;
import android.hardware.usb.UsbManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.nfc.NfcManager;
import android.os.DropBoxManager;
import android.os.Looper;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class BasicContext {
	private final String Tag = LogUtil.makeTag(getClass());

	protected final Context context;

	/**
	 * 初始化构造方法
	 * 
	 * @param context
	 *            不能为空，保证后续要用的上下文的地方不异常
	 * @author huqingsong
	 * @Description 在构造里还会通过实例的类对象来创建来创建日志标签，我的标签都以"hqs."开头
	 * @throws NullPointerException
	 *             如果上下文为null
	 */
	public BasicContext(Context context) {
		if (context == null) {
			throw new NullPointerException("context can not be null!");
		}
		this.context = context;
	}

	// =================================================================
	// ======== TODO These methods are visible to the subclass =========
	// =================================================================

	/** 获取当前应用的上下文 */
	protected Context getApplicationContext() {
		return context.getApplicationContext();
	}

	/** 根据目标程序的包名来获取其程序的上下文 */
	protected Context getTargetContext(String pkgName) throws NameNotFoundException {
		return context.createPackageContext(pkgName, Context.CONTEXT_IGNORE_SECURITY);
	}

	protected SharedPreferences getTargetPrefs(String pkgName, String fileName) throws NameNotFoundException {
		return getTargetContext(pkgName).getSharedPreferences(fileName, 1 | 4);
	}

	/**
	 * @param name
	 *            文件名
	 * @param mode
	 *            获取资源的模式
	 * @return 共享资源接口
	 */
	protected SharedPreferences getSharedPreferences(String name) {
		return context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}

	/**
	 * @param name
	 *            文件名
	 * @param mode
	 *            获取资源的模式
	 * @return 共享资源接口
	 */
	protected SharedPreferences getSharedPreferences(String name, int mode) {
		return context.getSharedPreferences(name, mode);
	}

	protected boolean remove(String name, String... keys) throws NullPointerException {
		if (TextTool.isNull(name) || keys == null || keys.length == 0) {
			throw new NullPointerException("The file or keys are not exsits!");
		}
		Editor editor = getSharedPreferences(name).edit();
		for (String key : keys) {
			editor.remove(key);
		}
		return editor.commit();
	}

	protected boolean clear(String name) {
		if (TextTool.isNull(name)) {
			return false;
		}
		Editor editor = getSharedPreferences(name).edit();
		return editor.clear().commit();
	}

	protected Looper getMainLooper() {
		return context.getMainLooper();
	}

	protected ContentResolver getContentResolver() {
		return context.getContentResolver();
	}

	protected Resources getResources() {
		return context.getResources();
	}

	/**
	 * 根据资源id获取资源文件
	 * 
	 * @param resId
	 *            资源id
	 * @return 字符串
	 */
	protected final String getString(int resId) {
		return context.getString(resId);
	}

	/**
	 * 根据资源id获取资源文件
	 * 
	 * @param resId
	 *            资源id
	 * @return 字符序列
	 */
	protected final CharSequence getText(int resId) {
		return context.getText(resId);
	}

	protected final Configuration getConfiguration() {
		return context.getResources().getConfiguration();
	}

	protected final PackageManager getPackageManager() {
		return context.getPackageManager();
	}

	protected final WindowManager getWindowManager() {
		return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	}

	protected final LayoutInflater getLayoutInflater() {
		return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	protected final ActivityManager getActivityManager() {
		return (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	}

	protected final AlarmManager getAlarmManager() {
		return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	}

	/**
	 * @return Android本身的通知管理器
	 */
	protected final NotificationManager getNotificationManager() {
		return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	protected final ConnectivityManager getConnectivityManager() {
		return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	protected final WifiManager getWifiManager() {
		return (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}

	protected final AudioManager getAudioManager() {
		return (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	}

	protected final TelephonyManager getTelephonyManager() {
		return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}

	protected final InputMethodManager getInputMethodManager() {
		return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	protected final DropBoxManager getDropBoxManager() {
		return (DropBoxManager) context.getSystemService(Context.DROPBOX_SERVICE);
	}

	protected final DownloadManager getDownloadManager() {
		return (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
	}

	protected final NfcManager getNfcManager() {
		return (NfcManager) context.getSystemService(Context.NFC_SERVICE);
	}

	protected final UsbManager getUsbManager() {
		return (UsbManager) context.getSystemService(Context.USB_SERVICE);
	}

	protected final InputManager getInputManager() {
		return (InputManager) context.getSystemService(Context.INPUT_SERVICE);
	}

	protected final DisplayManager getDisplayManager() {
		return (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
	}

	protected final PowerManager getPowerManager() {
		return (PowerManager) context.getSystemService(Context.POWER_SERVICE);
	}

	// =================================================================
	// ========================= TODO public methods====================
	// =================================================================
	public final String getClsName() {
		return getClass().getSimpleName();
	}

	// =================================================================
	// ======================= TODO print log methods ==================
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
