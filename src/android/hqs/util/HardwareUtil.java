package android.hqs.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.hqs.tool.CmdExecute;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;


/**
 * 获取cpu 硬盘 内存 网络设置 等硬件信息
 * @author hqs2063594
 *
 */
public class HardwareUtil {

	/**
	 * 获取运营商信息。<br>
	 * 运营商信息中包含IMEI、手机号码等，在Android中提供了运营商管理类（{@link #TelephonyManager}），
	 * 可以通过它来获取运营商相关的信息
	 * @param context
	 * @return
	 */
	public static String fetchTelStatusInfo(Context context) {
		StringBuffer result = new StringBuffer();
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		Configuration config = context.getResources().getConfiguration();
		// 获取DeviceId信息
		result.append("DeviceId(IMEI) = " + tm.getDeviceId() + "\n")
		// 获取设备的软件版本信息等
		.append("DeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion() + "\n")
		.append("IMSI MCC (Mobile Country Code): " + String.valueOf(config.mcc) + "\n")
		.append("IMSI MNC (Mobile Network Code): " + String.valueOf(config.mnc) + "\n");
		return result.toString();
	}
	
	public static String getTelStatusInfo(Context context) {
		StringBuffer result = new StringBuffer();
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		// 获取网络类型
		result.append(String.valueOf(tm.getNetworkType()) + "\n")
		// 手机型号
		.append(Build.MODEL + "\n")
		// 本机电话号码
		.append(tm.getLine1Number() + "\n")
		// SDK版本号
		.append(String.valueOf(Build.VERSION.SDK_INT) + "\n")
		// Firmware/OS 版本号
		.append(Build.VERSION.RELEASE);
		return result.toString();
	}
	
	/**
	 * 获取CPU信息。<br>
	 * 可以在手机设备的/proc/cpuinfo中获取CPU信息，调用{@link CmdExecute#run(String[], String)}执行系统的cat的命令，读取/proc/cpuinfo的内容。
	 * @return
	 */
	public static String fetchCpuInfo() {
		CmdExecute cmdexe = new CmdExecute();
		String[] args = { "/system/bin/cat", "/proc/cpuinfo" };
		return cmdexe.run(args, "/system/bin/");
	}
	
	/**
	 * 获取内存信息。<br>
	 * 可以读取/proc/meminfo信息，另外还可以通过{@link Context#getSystemService(String)}
	 * 获取{@link ActivityManager.MemoryInfo}对象，进而获取可用内存信息
	 * @param context
	 * @return
	 */
	 public static String getMemoryInfo(Context context) {
	    StringBuffer memoryInfo = new StringBuffer();
	    
	    final ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
	    manager.getMemoryInfo(outInfo);
	    
	    memoryInfo.append("\nTotal Available Memory :").append(outInfo.availMem >> 10).append("k");
	    memoryInfo.append("\nTotal Available Memory :").append(outInfo.availMem >> 20).append("k");
	    memoryInfo.append("\nIn low memory situation:").append(outInfo.lowMemory);
	    
		String result = null;
		CmdExecute cmdexe = new CmdExecute();
		String[] args = { "/system/bin/cat", "/proc/meminfo" };
		result = cmdexe.run(args, "/system/bin/");
	    return (memoryInfo.toString() + "\n\n" + result);
	}
	 
	/**
	 * 获取磁盘信息。<br>
	 * 手机设备的磁盘信息可以通过df命令获取，所以，这里获取磁盘信息的方法和前面类似，
	 * 惟一不同的是，这个是直接执行命令，获取其命令的返回就可以了。
	 * @return
	 */
	public static String fetchDiskInfo() {
		CmdExecute cmdexe = new CmdExecute();
		String[] args = { "/system/bin/df" };
		return cmdexe.run(args, "/system/bin/");
	}
	
	/**
	 * 获取网络信息。<br>
	 * 要获取手机设备的网络信息，只要读取/system/bin/netcfg中的信息就可以了。
	 * @return
	 */
	public static String fetchNetcfgInfo() {
		CmdExecute cmdexe = new CmdExecute();
		String[] args = { "/system/bin/netcfg" };
		return cmdexe.run(args, "/system/bin/");
	}
	
	/**
	 * 获取显示频信息。
	 * @param context
	 * @return
	 */
	public static String getDisplayMetricsInfo(Context context) {
		StringBuffer result = new StringBuffer();
	    DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
	    // 屏宽
	    result.append("The absolute width: " + String.valueOf(dm.widthPixels) + "pixels\n")
	    // 屏高
	    .append("The absolute heightin: " + String.valueOf(dm.heightPixels) + "pixels\n")
	    .append("The logical density of the display: " + String.valueOf(dm.density) + "\n")
	    .append("X dimension : " + String.valueOf(dm.xdpi) +"pixels per inch\n")
	    .append("Y dimension : " + String.valueOf(dm.ydpi) +"pixels per inch");
	    return result.toString();
	}

}
