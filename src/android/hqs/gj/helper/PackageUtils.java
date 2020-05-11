
package com.vivo.sdk.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManagerNative;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

/**
 * @ClassName: PackageUtils
 * @Description: TODO
 * @author: xushenghua
 * @date: 2017年3月31日 上午9:32:33
 * @version 1.0
 * @Copyright © 2017 广东维沃移动通信有限公司. All rights reserved.
 */
public class PackageUtils {

    /**
     * @Description 冻结接口的返回值
     */
    public static final int FROZEN_SUCCESS = 1;
    public static final int FROZEN_FAILED = -1;
    public static final int FROZEN_MULTIPLE = 0;
    private static final String TAG = "PackageUtils";

    /**
     * @Title: getPackageListByPid
     * @Description: 根据pid获取应用的包名列表，由于多个应用可能共享进程，所以返回的是一个包名数组
     * @param pid
     * @return
     * @return: String[]
     */
    public static String[] getPackageListByPid(Context context, int pid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> processList = am.getRunningAppProcesses();
        for (RunningAppProcessInfo process : processList) {
            if (process.pid == pid) {
                return process.pkgList;
            }
        }
        return null;
    }

    /**
     * @Title: startExternalActivity
     * @Description: 启动其它应用Activity，启动前判断intent是否能找到对应的页面，否则有可能出现ActivityNotFound的异常。
     * @param context
     * @param intent
     * @return
     * @return: boolean
     */
    public static boolean startExternalActivity(Context context, Intent intent) {
        List<ResolveInfo> infoList = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES);
        if (infoList != null && !infoList.isEmpty()) {
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * @Title: getAppInfoByPackageName
     * @Description: 根据包名获取PackageInfo，建议使用AppInfoManager.getAppInfo来获取应用信息
     * @param context
     * @param pkgName
     * @return
     * @return: PackageInfo
     */
    public static PackageInfo getPackageInfoByPackageName(Context context, String pkgName) {
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getPackageInfo(pkgName, 0);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return null;
    }

    /**
     * @Description 根据应用包名杀死应用
     * @param packageName
     * @return void
     * @author LiYangjie
     */
    public static void forceStopPackage(Context context, final String packageName) {
        try {
            ActivityManager am = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            ReflectUtils.callObjectMethod(am, "forceStopPackage", new Class<?>[] {
                    String.class
            }, packageName);
            LogUtils.i("forceStop : " + packageName);
        } catch (Exception e) {
            LogUtils.e(Log.getStackTraceString(e));
        }
    }

    public static void killBackGroundProcesses(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        am.killBackgroundProcesses(packageName);
    }

    /**
     * @Description 根据应用的pid杀死应用
     * @param pid
     * @return void
     * @author LiYangjie
     */
    public static void killProcessByPid(final int pid) {
        try {
            android.os.Process.killProcess(pid);
        } catch (Exception e) {
            LogUtils.e(Log.getStackTraceString(e));
        }
    }

    /**
     * @Description 根据进程名杀死进程
     * @param 进程名
     * @return void
     * @author LiYangjie
     */
    public static void killProcess(final String processName) {
        String command = "pkill " + processName + "\n";
        try {
            Class<?> clazz = Class.forName("com.vivo.services.daemon.VivoDmServiceProxy");
            Object object = ReflectUtils.callStaticObjectMethod(clazz, "asInterface",
                    new Class<?>[] {
                            IBinder.class
                    }, ServiceManager.getService("vivo_daemon.service"));
            ReflectUtils.callObjectMethod(object, "runDaemonShell", new Class<?>[] {
                    String.class
            }, command);
            LogUtils.d("kill..." + processName);
        } catch (Exception e) {
            LogUtils.e(Log.getStackTraceString(e));
        }
    }

    /**
     * @Description 强制冻结运行的应用
     * @param packageName 强制冻结运行的应用
     * @return boolean 返回冻结的结果，true代表冻结成功，false代表冻结不成功
     */
    public static boolean forceFreezePackage(Context context, final String packageName) {
        try {
            ActivityManager am = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            int result = ReflectUtils.callObjectMethod(am, int.class, "forceFreezePackage",
                    new Class<?>[] {
                            String.class
                    }, packageName);
            if (result == FROZEN_SUCCESS) {
                LogUtils.d("forceFreezePackage " + packageName);
                return true;
            } else if (result == FROZEN_MULTIPLE) {
                return true;
            }
        } catch (Exception e) {
            LogUtils.e(Log.getStackTraceString(e));
        }
        return false;
    }

    /**
     * @Description 跳转到卸载应用的界面
     * @param 进程名
     * @return void
     * @author LiYangjie
     */
    public static void unInstallApp(Context context, final String packageName) {
        Uri packageUri = Uri.parse("package:" + packageName);
        LogUtils.d("package:" + packageName);
        Intent uninstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE,
                packageUri);
        uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(uninstallIntent);
    }

    /**
     * @Description 跳转到更新应用的界面
     * @param packageName
     * @return void
     * @author LiYangjie
     */
    public static void checkForUpdateApp(Context context, final String packageName) {
        Uri uri = Uri.parse("vivoMarket://details?id=" + packageName);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.bbk.appstore");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * @Description 清除用户数据
     * @param context
     * @param packageName
     * @return void
     * @author LiYangjie
     */
    public static void clearAppUserData(Context context, final String packageName) {
        LogUtils.d("clearAppUserData...");
        try {
            ActivityManager am = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            ReflectUtils.callObjectMethod(am, "clearApplicationUserData", new Class<?>[] {
                    String.class, IPackageDataObserver.class
            }, packageName, new IPackageDataObserver.Stub() {
                @Override
                public void onRemoveCompleted(String packageName, boolean succeeded)
                        throws RemoteException {
                    if (succeeded) {
                        LogUtils.d("clear data success. p = " + packageName);
                    }
                }
            });
        } catch (Exception e) {
            LogUtils.e(Log.getStackTraceString(e));
        }
    }

    /**
     * @Title: getForeGroundComponentName
     * @Description: 获取前台activity的应用信息
     * @return
     * @return: AppInfo
     */
    public static ComponentName getForeGroundComponentName() {
        String response = ShellUtils.runNormalShell("dumpsys activity | grep mFocusedActivity",
                false).responseMsg;
        if (!TextUtils.isEmpty(response)) {
            int start = response.indexOf("{");
            int end = response.lastIndexOf("}");
            String recordContent = response.substring(start + 1, end);
            String[] splitArr = recordContent.split(" ");
            if (splitArr.length > 2) {
                String component = splitArr[2];
                return ComponentName.unflattenFromString(component);
            }
        }
        return null;
    }

    public static long getPidMemoryInKB(int pid) throws RemoteException {
        return getPidListMemmoryInKB(new int[] {
                pid
        })[0];
    }

    public static long[] getPidListMemmoryInKB(int[] pids) throws RemoteException {
        return ActivityManagerNative.getDefault().getProcessPss(pids);
    }
}
