package android.hqs.gj.util;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

/**
 * 界面跳转，服务启动停止工具类。
 * @author hqs2063594
 *
 */
public class ComponentsUtil {

	// ========================================================================================================
	// ==================================== TODO 下面是界面跳转方法 ==============================================
	// ========================================================================================================
	public static void newTask(Context context, Class<?> cls) {
		Intent i = new Intent(context, cls);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}
	
	public static void newTask(Context context, Class<?> cls, Bundle bundle) {
		Intent i = new Intent(context, cls);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	
	/**
	 * 从后台运行跳转到前台运行
	 * @param context
	 * @param cls
	 */
	public static void front(Context context, Class<?> cls){
		Intent i = new Intent(context, cls);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		// 如果该任务正在运行，那么把历史记录堆栈移到前面
		i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		i.setAction(Intent.ACTION_MAIN);
		i.addCategory(Intent.CATEGORY_LAUNCHER);
		context.startActivity(i);
	}
	
	public static void clearTop(Context context, Class<?> cls) {
		Intent i = new Intent(context, cls);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(i);
	}

	public static void finish(Context context, Class<?> cls) {
		Intent i = new Intent(context, cls);
		context.startActivity(i);
		((Activity) context).finish();
	}

	public static void clearTopAndFinish(Context context, Class<?> cls) {
		Intent i = new Intent(context, cls);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(i);
		((Activity) context).finish();
	}
	
	/**返回桌面*/
	public static void deskUp(Context context){
		Intent i = new Intent(Intent.ACTION_MAIN);
	    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);  
	    i.addCategory(Intent.CATEGORY_HOME);
	    context.startActivity(i);
	}
	
	public static void jumpByPackageName(Context context, String packageName){
		PackageInfo pi = null;
		try {
			pi = context.getPackageManager().getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {e.printStackTrace();}
		 
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(pi.packageName);
		 
		List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(resolveIntent, 0);
		ResolveInfo ri = apps.iterator().next();
		if (ri != null ) {
			String packageName1 = ri.activityInfo.packageName;
			String className = ri.activityInfo.name;
			 
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			ComponentName cn = new ComponentName(packageName1, className);
			intent.setComponent(cn);
			context.startActivity(intent);
		}
	}
	
	// ========================================================================================================
	// ============================== TODO 下面是根据包名获取应用的运行状态方法 ====================================
	// ========================================================================================================
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
	
	// ========================================================================================================
	// ============================== TODO 下面是各种启动关闭服务方法 =========================================
	// ========================================================================================================
	public static void startService(Context context, Class<?> cls) {
		Intent i = new Intent(context, cls);		
		context.startService(i);
	}
	
	public static void startService(Context context, String targetPkgName, String action) {
		Intent i = new Intent(action);
		i.setPackage(targetPkgName);
		context.startService(i);
		/*Intent ii = new Intent();
		ComponentName cn = new ComponentName(targetPkgName, action);
		ii.setComponent(cn);
		context.startService(ii);*/
	}
	
	public static void stopService(Context context, Class<?> cls){
		Intent i = new Intent(context, cls);
		context.stopService(i);
	}
	
	public static void stopService(Context context, String targetPkgName, String action) {
		Intent i = new Intent(action);
		i.setPackage(targetPkgName);
		context.stopService(i);
	}
	
	public static void bindService(Context context, Class<?> cls, ServiceConnection conn){
		Intent i = new Intent(context, cls);
		context.bindService(i, conn, Context.BIND_AUTO_CREATE);
	}
	
	public static void bindService(Context context, Class<?> cls, Bundle bundle, ServiceConnection conn){
		Intent i = new Intent(context, cls);
		i.putExtras(bundle);
		context.bindService(i, conn, Context.BIND_AUTO_CREATE);
	}
	
	public static void bindService(Context context, String action, ServiceConnection conn){
		Intent i = new Intent(action);
		context.bindService(i, conn, Context.BIND_AUTO_CREATE);
	}
	
	public static void bindService(Context context, String action, Bundle bundle, ServiceConnection conn){
		Intent i = new Intent(action);
		i.putExtras(bundle);
		context.bindService(i, conn, Context.BIND_AUTO_CREATE);
	}
	
	public static void unbindService(Context context, ServiceConnection conn){
		context.unbindService(conn);
	}
	
    /**
	 * Cancel the alarm clock set to collect static data timing task.
	 * @param context
	 */
	public static void cancelTimeService(Context context, Class<?> cls) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, cls);
		PendingIntent alarm = PendingIntent.getService(context, 0, intent, 0);
		if (alarm != null) {
			am.cancel(alarm);
		}
	}
	
	/**
	 * Use the alarm clock set to collect static data timing tasks, 
	 * pay attention: before you set up the task to clear the exist tasks.
	 * @param context
	 * @param startTime service start time, unit: ms
	 * @return Alarm start time, unit: seconds
	 */
	public static long setTimeService(Context context, Class<?> cls, long startTime) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, cls);
		PendingIntent pi = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
		if (pi != null) {
			am.cancel(pi);
		}
		pi = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		// 触发时间 = 要开启任务的时间 - 开机那一时刻的时间
		long triggerTime = startTime - System.currentTimeMillis();
		if (triggerTime < 0) {
			// 一天
			triggerTime += 24 * 60 * 60 * 1000;
		}
		Log.i(cls.getSimpleName(), "Start the service in " + (triggerTime / 1000) + " seconds");
		// 返回的是系统从启动到现在的时间
		triggerTime += SystemClock.elapsedRealtime();
		am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pi);
		// am.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pi);
		// am.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pi);
		return triggerTime / 1000;
	}
	
	
	// ========================================================================================================
	// ============================== TODO 下面是各种发送广播的方法 ===========================================
	// ========================================================================================================
	public static void sendBroadcast(Context context, String action){
		Intent i = new Intent(action);
		context.sendBroadcast(i);
	}
	
	public static void sendBroadcast(Context context, String action, Bundle bundle){
		Intent i = new Intent(action);
		i.putExtras(bundle);
		context.sendBroadcast(i);
	}
	
}
