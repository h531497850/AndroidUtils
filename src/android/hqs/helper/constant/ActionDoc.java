package android.hqs.helper.constant;

import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.hardware.Camera;;

/**
 * Intent Action信息
 * @author 胡青松
 */
public class ActionDoc {
	
	/**
	 * 系统启动完成会发出一个Standard Broadcast Action，且只会发出一次。
	 * @see Intent.ACTION_BOOT_COMPLETED
	 */
	public final String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
	
	/**
	 * activity，启动一个电话,{@link Intent#ACTION_CALL}
	 */
	public final String ACTION_CALL = "android.intent.action.CALL";
	/**
	 * activity，显示用户编辑的数据,{@link Intent#ACTION_EDIT}
	 */
	public final String ACTION_EDIT = "android.intent.action.EDIT";
	/**
	 * activity，作为Task中第一个Activity启动,{@link Intent#ACTION_MAIN}
	 */
	public final String ACTION_MAIN = "android.intent.action.MAIN";
	/**
	 * activity，同步手机与数据服务器上的数据,{@link Intent#ACTION_SYNC}
	 */
	public final String ACTION_SYNC = "android.intent.action.SYNC";
	/**
	 * activity，电池电量过低警告,{@link Intent#ACTION_BATTERY_LOW}
	 */
	public final String ACTION_BATTERY_LOW = "android.intent.action.BATTERY_LOW";
	/**
	 * activity，改变时区警告,{@link Intent#ACTION_TIMEZONE_CHANGED}
	 */
	public final String ACTION_TIMEZONE_CHANGED = "android.intent.action.TIMEZONE_CHANGED";
	
	
	// 下面的3个是AudioManager类中的隐藏常量
	public final String VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION";
	public final String EXTRA_VOLUME_STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE";
	public final String EXTRA_VOLUME_STREAM_VALUE = "android.media.EXTRA_VOLUME_STREAM_VALUE";
	
	/**
	 * 用户目前设备唤醒后发送的（例如当键盘锁了）。
	 * {@link Intent#ACTION_USER_PRESENT}
	 */
	public final String ACTION_USER_PRESENT = "android.intent.action.USER_PRESENT";
	
	// GPS
	/**
	 * {@link LocationManager#GPS_ENABLED_CHANGE_ACTION}
	 */
	public final String GPS_ENABLED_CHANGE_ACTION = "android.location.GPS_ENABLED_CHANGE";
	/**
	 * {@link LocationManager#GPS_FIX_CHANGE}
	 */
	public final String GPS_FIX_CHANGE = "android.location.GPS_FIX_CHANGE";
	
	// 时间
	/**
	 * 广播动作：设置时间。 {@link Intent#ACTION_TIME_CHANGED}
	 */
	public final String ACTION_TIME_CHANGED = "android.intent.action.TIME_SET";
	
	// 电源
	/**
	 * 广播动作：外部电源已从设备中删除。{@link Intent#ACTION_POWER_DISCONNECTED}
	 */
	public final String ACTION_POWER_DISCONNECTED = "android.intent.action.ACTION_POWER_DISCONNECTED";
	/**
	 * 广播动作：外部电源已连接到设备。 {@link Intent#ACTION_POWER_CONNECTED}
	 */
	public final String ACTION_POWER_CONNECTED = "android.intent.action.ACTION_POWER_CONNECTED";

	// 耳机 broadcast receiver 插拔耳机警告
	/**
	 * 广播：有线耳机插入或拔出。{@link Intent#ACTION_HEADSET_PLUG}
	 */
	public final String ACTION_HEADSET_PLUG = "android.intent.action.HEADSET_PLUG";
	
	// 网络
	/**
	 * 发生了网络连接的变化。{@link ConnectivityManager#CONNECTIVITY_ACTION}
	 */
	public final String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	
	// 相机
	/**
	 * 广播动作：相机拍摄了一张新图片，且该图片的入口已被添加到媒体商店。
	 * {@link Camera#ACTION_NEW_PICTURE}，
	 * {@link android.content.Intent#getData()}是URI图片。
	 */
	public final String ACTION_NEW_PICTURE = "android.hardware.action.NEW_PICTURE";
	/**
	 * 广播动作：摄像机拍摄了一个新视频，且该视频的入口已被添加到媒体商店。
	 * {@link Camera#ACTION_NEW_VIDEO}，
	 * {@link android.content.Intent#getData()}是URI视频。
	 */
	public final String ACTION_NEW_VIDEO = "android.hardware.action.NEW_VIDEO";
	
	//屏幕 broadcast receiver 屏幕变亮警告
	/**
	 * 当设备处于唤醒状态且成为交互设备时发送的广播。
	 * {@link Intent#ACTION_SCREEN_ON}
	 */
	public final String ACTION_SCREEN_ON = "android.intent.action.SCREEN_ON";
	/**
	 * 当设备处于休眠状态且成为非交互设备时发送的广播。
	 * {@link Intent#ACTION_SCREEN_OFF}
	 */
	public final String ACTION_SCREEN_OFF = "android.intent.action.SCREEN_OFF";

}