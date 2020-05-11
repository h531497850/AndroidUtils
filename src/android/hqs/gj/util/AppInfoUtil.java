package android.hqs.gj.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.hqs.gj.tool.CmdExecute;
import android.util.Log;
import android.util.SparseArray;

/**
 * 使用{@link #PackageManager}获取应用信息，通过{@link #ActivityManager}获取运行时信息
 * @author hqs2063594
 *
 */
public class AppInfoUtil {
	
	/**
	 * 查看已安装软件信息。<br/>
	 * Android提供了{@link Context#getPackageManager()}、{@link PackageManager#getInstalledApplications(int)}方法，
	 * 这里int=0，可以直接返回全部已经安装的应用列表。<br>
	 * 如果安装的软件比较多，那么获取信息所花费的时间会比较多，请另起线程加载。
	 * @param context
	 * @return
	 */
	public static ArrayList<?> fetchInstalledApps(Context context) {
		PackageManager pm = context.getPackageManager();
		List<ApplicationInfo> appInfos = pm.getInstalledApplications(0);
		ArrayList<HashMap<String, Object>> appList = new ArrayList<HashMap<String, Object>>(appInfos.size());
		Iterator<ApplicationInfo> l = appInfos.iterator();
		while (l.hasNext()) {
			ApplicationInfo app = (ApplicationInfo) l.next();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", app.name);
			map.put("packageName", app.packageName);
			map.put("className", app.className);
			map.put("icon", app.icon);
			map.put("processName", app.processName);
			map.put("uid", app.uid);
			String label = " ";
			try {
				label = pm.getApplicationLabel(app).toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			map.put("label", label);
			appList.add(map);
		}
		return appList;
	}
	
	/**
	 * 查看已安装软件信息。<br/>
	 * Android提供了{@link Context#getPackageManager()}、{@link PackageManager#getInstalledPackages(int)}方法，
	 * 这里int=0，可以直接返回全部已经安装的应用列表。<br>
	 * 如果安装的软件比较多，那么获取信息所花费的时间会比较多，请另起线程加载。
	 * @param context
	 * @return
	 */
	public static List<?> fetchInstalledAppList(Context context) {
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packages = pm.getInstalledPackages(0);
		List<HashMap<String, Object>> appList = new ArrayList<HashMap<String, Object>>(packages.size());
		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("appName", packageInfo.applicationInfo.loadLabel(pm).toString());
			map.put("packageName", packageInfo.packageName);
			map.put("versionName", packageInfo.versionName);
			map.put("versionCode", packageInfo.versionCode);
			map.put("appIcon", packageInfo.applicationInfo.loadIcon(pm));
			map.put("apkSize", new File(packageInfo.applicationInfo.publicSourceDir).length());
			appList.add(map);
		}
		return appList;
	}
	
	/**
	 * 获取已安装应用的包名
	 * @param context
	 */
	public static SparseArray<?> fetchInstalledAppPkName(Context context){
		SparseArray<String> mUidList = new SparseArray<String>();
        List<PackageInfo> packages =context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo info = packages.get(i);
            if (info != null && info.packageName != null) {
                int uid = info.applicationInfo.uid;
                if (uid != 0) {
                    mUidList.put(uid, info.packageName);
                }
            }
        }
        return mUidList;
    }
	
	/**
	 * 获取已安装应用的包名
	 * @param context
	 */
	public static void fetchInstalledAppPkName(Context context, SparseArray<String> arr){
        List<PackageInfo> packages =context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo info = packages.get(i);
            if (info != null && info.packageName != null) {
                int uid = info.applicationInfo.uid;
                if (uid != 0) {
                    arr.put(uid, info.packageName);
                }
            }
        }
    }
	
	public static String getProcessNameByPid(Context context, int pId){
	    String processName = "";
	    ActivityManager am = (ActivityManager)context.getSystemService(Activity.ACTIVITY_SERVICE);
	    //PackageManager pm = context.getPackageManager();

	    List<RunningAppProcessInfo> l = am.getRunningAppProcesses();
	    Iterator<RunningAppProcessInfo> i = l.iterator();
		while (i.hasNext()) {
			RunningAppProcessInfo info = i.next();
			try {
				if (info.pid == pId) {
					// CharSequence c =
					// pm.getApplicationLabel(pm.getApplicationInfo(info.processName,
					// PackageManager.GET_META_DATA));
					// Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
					// info.processName +"  Label: "+c.toString());
					// processName = c.toString();
					processName = info.processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}
	
	/**
	 * 获取当前应用的版本名
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		String version = "0.0";
		PackageInfo packInfo = null;
		try {
			packInfo = PackageUtil.getPackageInfo(context, context.getPackageName(), 0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			Log.e("getCurrVersionName", "version = 0.0", e);
		}
		return version;
	}
	public static String getVersionName(Context context, String packageName) {
		String version = "0.0";
		PackageInfo packInfo = null;
		try {
			packInfo = PackageUtil.getPackageInfo(context, packageName, 0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			Log.e("getVersionName", "version = 0.0", e);
		}
		return version;
	}
	/**
	 * 获取当前应用的版本名
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		int version = 0;
		PackageInfo packInfo = null;
		try {
			packInfo = PackageUtil.getPackageInfo(context, context.getPackageName(), 0);
			version = packInfo.versionCode;
		} catch (NameNotFoundException e) {
			Log.e("getCurrAppVersionCode", "version = 0", e);
		}
		return version;
	}
	
	/**
	 * 通过包名获取应用版本号
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static int getVersionCode(Context context, String packageName) {
		int verCode = 0;
		PackageInfo info;
		try {
			info = PackageUtil.getPackageInfo(context, packageName, PackageManager.GET_ACTIVITIES);
			verCode = info.versionCode;
		} catch (NameNotFoundException e) {
			Log.e("getAppVersionCode", "version = 0", e);
		}	
		return verCode;
	}
	
	/**
	 * 根据包名检查应用是否已安装。
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean checkAppInstallState(Context context, String packageName) {
		PackageInfo packageInfo;
		try {
			packageInfo = PackageUtil.getPackageInfo(context, packageName, 0);
			return packageInfo != null;
		} catch (NameNotFoundException e) {
			// 默认应用未安装。
			Log.e("checkAppInstallState", "Default application not installed.", e);
			return false;
		}
	}
	
	/**
	 * 获取正在运行的Service信息列表
	 * @param context
	 * @return
	 */
	public static String getRunningServicesInfo(Context context) {
	    StringBuffer serviceInfo = new StringBuffer();
	    final ActivityManager am = (ActivityManager) context.getSystemService(Context. ACTIVITY_SERVICE);
	    // 获取正在运行的服务，并依次遍历得到每个服务对应的pid,进程等信息
	    List<RunningServiceInfo> services = am.getRunningServices(100);

	    Iterator<RunningServiceInfo> l = services.iterator();
	    while (l.hasNext()) {
	        RunningServiceInfo si = (RunningServiceInfo) l.next();
	        serviceInfo.append("pid: ").append(si.pid);
	        serviceInfo.append("\nprocess: ").append(si. process);
	        serviceInfo.append("\nservice: ").append(si. service);
	        serviceInfo.append("\ncrashCount: ").append(si. crashCount);
	        serviceInfo.append("\nclicentCount: ").append(si.clientCount);
	        //serviceInfo.append("\nactiveSince:").append(ToolHelper.formatData(si.activeSince));
	        //serviceInfo.append("\nlastActivityTime: ").append(ToolHelper.formatData(si.lastActivityTime));
	        serviceInfo.append("\n\n ");
	    }
	    return serviceInfo.toString();
	}
	
	/**
	 * 获取正在运行的Task信息
	 * @param context
	 * @return
	 */
	public static String getRunningTaskInfo(Context context) {
	    StringBuffer taskInfo = new StringBuffer();
	    final ActivityManager am = (ActivityManager) context.getSystemService(Context. ACTIVITY_SERVICE);
	    List<RunningTaskInfo> tasks = am.getRunningTasks(100);
	    Iterator<RunningTaskInfo> l = tasks.iterator();
	    while (l.hasNext()) {
	        RunningTaskInfo ti = (RunningTaskInfo) l.next();
	        taskInfo.append("id: ").append(ti.id);
	        taskInfo.append("\nbaseActivity: ").append(ti. baseActivity.flattenToString());
	        taskInfo.append("\nnumActivities: ").append(ti. numActivities);
	        taskInfo.append("\nnumRunning: ").append(ti. numRunning);
	        taskInfo.append("\ndescription: ").append(ti. description);
	        taskInfo.append("\n\n");
	    }
	    return taskInfo.toString();
	}

	/**
	 * 获取正在运行的进程信息
	 * @return
	 */
	public static String fetcRunningProcessInfo() {
		CmdExecute cmdexe = new CmdExecute();
		String[] args = { "/system/bin/top", "-n", "1" };
		return cmdexe.run(args, "/system/bin/");
	}
	
}
