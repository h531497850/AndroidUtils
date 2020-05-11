package android.hqs.gj.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.hqs.gj.tool.CmdExecute;
import android.hqs.gj.tool.LogTool;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;


/**
 * 获取cpu 硬盘 内存 网络设置 等硬件信息
 * @author hqs2063594
 *
 */
public class HardwareUtil {
	private static final String TAG = LogTool.makeTag(HardwareUtil.class);

	// ========================================================================================================
	// ==================================== TODO 下面是字母F开头的方法 ===========================================
	// ========================================================================================================
	/**
	 * 获取配置信息。
	 */
	public static String fetchTelStatusInfo(Context context) {
		StringBuffer result = new StringBuffer();
		Configuration config = context.getResources().getConfiguration();
		result.append("IMSI MCC (Mobile Country Code): " + config.mcc)
		.append("\nIMSI MNC (Mobile Network Code): " + config.mnc);
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
	
	// ========================================================================================================
	// ==================================== TODO 下面是字母G开头的方法 ===========================================
	// ========================================================================================================
	/** 
     * 获取电话号码 
     */  
	public static String getNativePhoneNumber(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getLine1Number();
	}
	
	/** 
     * 获取电话号码 
     */  
	public static String getNativePhoneNumber(TelephonyManager tm) {
		return tm.getLine1Number();
	}
	
	/** 
     * 获取手机服务商信息 
     */  
	public static String getProvidersName(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return getProvidersName(tm);
	}
	
	/** 
     * 获取手机服务商信息 
     */  
	public static String getProvidersName(TelephonyManager tm) {
		// 国际移动用户识别码
		String IMSI;
		String providersName = "N/A";
		try {
			IMSI = tm.getSubscriberId();
			// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
			System.out.println(IMSI);
			if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
				providersName = "中国移动";
			} else if (IMSI.startsWith("46001")) {
				providersName = "中国联通";
			} else if (IMSI.startsWith("46003")) {
				providersName = "中国电信";
			}
		} catch (Exception e) {
			Log.e(TAG, "IMSI is null.", e);
		}
		return providersName;
	}
	
	/**
	 * 信息中包含IMEI、手机号码等，在Android中提供了运营商管理类（{@link #TelephonyManager}），
	 * 可以通过它来获取运营商相关的信息
	 */
	public static String getPhoneInfo(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		StringBuilder sb = new StringBuilder();
		sb.append("DeviceId(IMEI) = " + tm.getDeviceId())						// imei码
		.append("\nPhoneModel = " + Build.MODEL)								// 手机型号
		.append("\nAndroidVersionNumber = " + Build.VERSION.RELEASE)			// Firmware/OS/Android 版本号，如5.0
		.append("\nSDK Version = " + Build.VERSION.SDK_INT)						// SDK版本号，如19
		.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion())	// 软件版本号
		.append("\nLine1Number = " + tm.getLine1Number())						// 手机号：sim1卡
		.append("\nSimCountryIso = " + tm.getSimCountryIso())					// 下面几项是关于手机SIM卡的一些详细信息
		.append("\nSimOperator = " + tm.getSimOperator())
		.append("\nSimOperatorName = " + tm.getSimOperatorName())
		.append("\nSimSerialNumber = " + tm.getSimSerialNumber())
		.append("\nSimState = " + tm.getSimState())
		.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso())
		.append("\nPhoneType = " + tm.getPhoneType())
		.append("\nNetworkType = " + tm.getNetworkType())						// 获取网络类型
		.append("\nNetworkOperator = " + tm.getNetworkOperator())				// 移动运营商编号
		.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName())		// 移动运营商名称
		.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId())				// 国际移动用户识别码
		.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());
		return sb.toString();
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
