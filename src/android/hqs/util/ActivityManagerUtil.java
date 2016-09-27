package android.hqs.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;

/**
 * 根据包名获取应用的运行状态。
 * 由于{@link #ActivityManager}不能由静态方法获取，所以该类里的方法不是静态方法。
 * @author hqs2063594
 *
 */
public class ActivityManagerUtil {
	
	/**
	 * 是否在前台运行
	 * @param packageName
	 * @return
	 */
	public static boolean isFront(Context context, String packageName){
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		// 只取第一位，后面的都不相关
		List<RunningTaskInfo> taskinfo = am.getRunningTasks(1);
		if (taskinfo != null && !taskinfo.isEmpty()) {
			ComponentName topActivity = taskinfo.get(0).topActivity;
			if (packageName.equals(topActivity.getPackageName())) {
				//black("isRadioActivityOn = true");
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否在后台运行
	 * @param packageName
	 * @return
	 */
	public static boolean isBackstage(Context context, String packageName){
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> taskinfo = am.getRunningTasks(100);
		if (taskinfo != null && !taskinfo.isEmpty()) {
			int size = taskinfo.size();
			// 0是当前正在运行的应用，排除它，向后找，如果找到，则该应用在后台运行
			for (int i = 1; i < size; i++) {
				ComponentName topActivity = taskinfo.get(i).topActivity;
				if (packageName.equals(topActivity.getPackageName())) {
					//black("isRadioActivityOn = true");
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 是否已关闭
	 * @param packageName
	 * @return
	 */
	public static boolean isFinish(Context context, String packageName){
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> taskinfo = am.getRunningTasks(100);
		if (taskinfo != null && !taskinfo.isEmpty()) {
			int size = taskinfo.size();
			for (int i = 0; i < size; i++) {
				ComponentName topActivity = taskinfo.get(i).topActivity;
				if (packageName.equals(topActivity.getPackageName())) {
					//black("isRadioActivityOn = true");
					return false;
				}
			}
		}
		return true;
	}
	
	public static int getTaskId(Context context, String packageName){
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> taskinfo = am.getRunningTasks(100);
		if (taskinfo != null && !taskinfo.isEmpty()) {
			int size = taskinfo.size();
			for (int i = 0; i < size; i++) {
				ComponentName topActivity = taskinfo.get(i).topActivity;
				if (packageName.equals(topActivity.getPackageName())) {
					return taskinfo.get(i).id;
				}
			}
		}
		return -1;
	}

}
