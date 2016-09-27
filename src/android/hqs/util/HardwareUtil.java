package android.hqs.util;

import android.app.ActivityManager;
import android.content.Context;
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
		String result = null;
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String str = " ";
		// 获取DeviceId信息
		str += "DeviceId(IMEI) = " + tm.getDeviceId() + "\n";
		// 获取设备的软件版本信息等
		str += "DeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion() + "\n";
		// TODO: Do something ...
		int mcc = context.getResources().getConfiguration().mcc;
		int mnc = context.getResources().getConfiguration().mnc;
		str += "IMSI MCC (Mobile Country Code): " + String.valueOf(mcc) + "\n";
		str += "IMSI MNC (Mobile Network Code): " + String.valueOf(mnc) + "\n";
		result = str;
		return result;
	}
	
	public static String getTelStatusInfo(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String networkType = String.valueOf(tm.getNetworkType()); // 获取网络类型
		String model = Build.MODEL; // 手机型号
		String number = tm.getLine1Number(); // 本机电话号码
		String sdk = String.valueOf(Build.VERSION.SDK_INT); // SDK版本号
		String os = Build.VERSION.RELEASE; // Firmware/OS 版本号
		return networkType + "\n" + model + "\n" + number + "\n" + sdk + "\n" + os;
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
	    String str = "";
	    DisplayMetrics dm = new DisplayMetrics();
	    dm = context.getApplicationContext().getResources().getDisplayMetrics();
	    int screenWidth = dm.widthPixels;
	    int screenHeight = dm.heightPixels;
	    float density = dm.density;
	    float xdpi = dm.xdpi;
	    float ydpi = dm.ydpi;
	    str += "The absolute width: " + String.valueOf(screenWidth) + "pixels\n";
	    str += "The absolute heightin: " + String.valueOf(screenHeight) + "pixels\n";
	    str += "The logical density of the display. : " + String.valueOf(density) + "\n";
	    str += "X dimension : " + String.valueOf(xdpi) +"pixels per inch\n";
	    str += "Y dimension : " + String.valueOf(ydpi) +"pixels per inch\n";
	    return str;
	}

}
