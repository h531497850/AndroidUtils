package android.hqs.basic;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.hardware.display.DisplayManager;
import android.hardware.input.InputManager;
import android.hardware.usb.UsbManager;
import android.hqs.helper.DebugHelper;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.nfc.NfcManager;
import android.os.DropBoxManager;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class BasicContext extends DebugHelper {
	
	private final Context context;
	
	/**
	 * 初始化构造方法
	 * @param context 不能为空，保证后续要用的上下文的地方不异常
	 * @author 胡青松
	 * @Description 在构造里还会通过实例的类对象来创建来创建日志标签，我的标签都以"hqs."开头
	 */
	public BasicContext(Context context){
		if (context == null) {
			throw new NullPointerException("context can not be null!");
		}
		this.context = context;
		makeTag(getClass());
	}
	
	// ========================================================================================================
	// ==================================== TODO 下面是公布给子类的方法 ==========================================
	// ========================================================================================================
	protected final Context getContext() {
		return context;
	}
	
	public final String getClsName(){
		return getClass().getSimpleName();
	}
	
	protected final ContentResolver getContentResolver() {
		return context.getContentResolver();
	}
	
	protected final Resources getRes(){
		return context.getResources();
	}
	
	/**
	 * 根据资源id获取资源文件
	 * @param resId 资源id
	 * @return 字符串
	 */
	protected final String getString(int resId){
		return context.getString(resId);
	}
	
	/**
	 * 根据资源id获取资源文件
	 * @param resId 资源id
	 * @return 字符序列
	 */
	protected final CharSequence getText(int resId){
		return context.getText(resId);
	}
	
	/**
	 * @param name 文件名
	 * @param mode 获取资源的模式
	 * @return 共享资源接口
	 */
	protected final SharedPreferences getSP(String name){
		return getSP(name, Context.MODE_PRIVATE);
	}
	
	/**
	 * @param name 文件名
	 * @param mode 获取资源的模式
	 * @return 共享资源接口
	 */
	protected final SharedPreferences getSP(String name, int mode){
		return context.getSharedPreferences(name, mode);
	}
	
	protected final WindowManager getWindowManager(){
		return (WindowManager) getSystemService(Context.WINDOW_SERVICE);
	}
	
	protected final LayoutInflater getLayoutInflater(){
		return (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	protected final ActivityManager getActivityManager(){
		return (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	}
	
	protected final AlarmManager getAlarmManager(){
		return (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	}
	
	/** @return Android本身的通知管理器
	 */
	protected final NotificationManager getNotificationManager(){
		return (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	protected final ConnectivityManager getConnectivityManager(){
		return (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	}
	
	protected final WifiManager getWifiManager(){
		return (WifiManager) getSystemService(Context.WIFI_SERVICE);
	}
	
	protected final AudioManager getAudioManager(){
		return (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	}

	protected final TelephonyManager getTelephonyManager(){
		return (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
	}

	protected final InputMethodManager getInputMethodManager(){
		return (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	}
	
	protected final DropBoxManager getDropBoxManager(){
		return (DropBoxManager) getSystemService(Context.DROPBOX_SERVICE);
	}

	protected final DownloadManager getDownloadManager(){
		return (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
	}

	protected final NfcManager getNfcManager(){
		return (NfcManager) getSystemService(Context.NFC_SERVICE);
	}

	protected final UsbManager getUsbManager(){
		return (UsbManager) getSystemService(Context.USB_SERVICE);
	}
	
	protected final InputManager getInputManager(){
		return (InputManager) getSystemService(Context.INPUT_SERVICE);
	}

	protected final DisplayManager getDisplayManager(){
		return (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
	}

	protected final Object getSystemService(String name){
		return context.getSystemService(name);
	}
	
	/**
	 * @return 应用的上下文
	 */
	protected Context getAppContext(){
		return context.getApplicationContext();
	}

	protected Looper getMainLooper(){
		return context.getMainLooper();
	}

}
