package android.hqs.util;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

/**
 * 界面跳转工具类。
 * @author hqs2063594
 *
 */
public class ActivityJumpUtil {

	public static void front(Context packageContext, Class<?> cls) {
		Intent intent = new Intent(packageContext, cls);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		packageContext.startActivity(intent);
		intent = null;
	}

	public static void newTask(Context packageContext, Class<?> cls, Bundle bundle) {
		Intent intent = new Intent(packageContext, cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtras(bundle);
		packageContext.startActivity(intent);
	}

	public static void clearTop(Context packageContext, Class<?> cls) {
		Intent intent = new Intent(packageContext, cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		packageContext.startActivity(intent);
	}

	public static void finish(Context packageContext, Class<?> cls) {
		Intent intent = new Intent(packageContext, cls);
		packageContext.startActivity(intent);
		((Activity) packageContext).finish();
	}

	public static void clearTopAndFinish(Context packageContext, Class<?> cls) {
		Intent intent = new Intent(packageContext, cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		packageContext.startActivity(intent);
		((Activity) packageContext).finish();
	}
	
	/**返回桌面*/
	public static void deskUp(Context context){
		Intent intent = new Intent(Intent.ACTION_MAIN);
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    intent.addCategory(Intent.CATEGORY_HOME);
	    intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);  
	    context.startActivity(intent);
	    intent = null;
	}
	
	/**
	 * 从后台运行跳转到前台运行
	 * @param packageContext
	 * @param cls
	 */
	public static void jumpFront(Context packageContext, Class<?> cls){
		Intent intent = new Intent(packageContext, cls);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setAction(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);		
		
		//intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		packageContext.startActivity(intent);
		intent = null;
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
	
	public static void jumpFSExplorer(Context context){
		/*Intent intent = new Intent(context, FSExplorer.class);
		//intent.setClass(context, FSExplorer.class);
		context.startActivity(intent);*/
	}
	
}
