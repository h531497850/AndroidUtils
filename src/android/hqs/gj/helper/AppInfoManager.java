
package com.vivo.sdk.appinfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.vivo.sdk.utils.PackageUtils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: AppInfoManager
 * @Description: TODO
 * @author: xushenghua
 * @date: 2017年3月22日 下午2:35:40
 * @version 1.0
 * @Copyright © 2017 广东维沃移动通信有限公司. All rights reserved.
 */
public class AppInfoManager {

    private static final String TAG = "AppInfoManager";
    /**
     * @Description: 单例对象
     */
    private static AppInfoManager sAppInfoManager;
    /**
     * @Description: TODO
     */
    private Context mContext;
    /**
     * @Description: 应用列表的软引用
     */
    private SoftReference<ArrayList<AppInfo>> mAppListRef;
    /**
     * @Description: 同步锁
     */
    private final Object mLock = new Object();
    /**
     * @Description: 应用缓存
     */
    private Map<String, AppInfo> mAppCache;
    /**
     * @Description: TODO
     */
    private PackageManager mPackageManager;

    /**
     * @Description:构造函数
     * @param context
     */
    private AppInfoManager(Context context) {
        mContext = context.getApplicationContext();
        mPackageManager = mContext.getPackageManager();
        mAppListRef = new SoftReference<ArrayList<AppInfo>>(null);
        mAppCache = new ConcurrentHashMap<String, AppInfo>();
    }

    public static synchronized void init(Context context) {
        if (sAppInfoManager == null) {
            sAppInfoManager = new AppInfoManager(context);
        }
    }

    /**
     * @Title: getInstance
     * @Description: 获取单例对象
     * @return
     * @return: AppInfoManager
     */
    public static synchronized AppInfoManager getInstance() {
        if (sAppInfoManager == null) {
            throw new RuntimeException("please init AppInfoManager first");
        }
        return sAppInfoManager;
    }

    /**
     * @Title: getAppInfoByPackageName
     * @Description: 根据包名获取AppInfo对象
     * @param context
     * @param pkgName
     * @return
     * @return: AppInfo
     */
    private static AppInfo getAppInfoByPackageName(Context context, String pkgName) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkg = PackageUtils.getPackageInfoByPackageName(context, pkgName);
        if (pkg != null) {
            AppInfo appInfo = new AppInfo();
            appInfo.setUid(pkg.applicationInfo.uid);
            appInfo.setAppName(pkg.applicationInfo.loadLabel(pm).toString());
            appInfo.setPkgName(pkgName);
            appInfo.setSystemFlag(
                    (pkg.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
            appInfo.setVersionCode(pkg.versionCode);
            appInfo.setVersionName(pkg.versionName);
            appInfo.setIcon(pkg.applicationInfo.loadIcon(pm));
            return appInfo;
        }
        return null;
    }

    /**
     * @Title: clearCache
     * @Description: 清除软引用缓存
     * @return: void
     */
    public void clearAppListCache() {
        synchronized (mLock) {
            mAppListRef.clear();
        }
    }

    public void removeCacheByPackageName(String pkgName) {
        mAppCache.remove(pkgName);
    }

    /**
     * @Title: getInstalledAppList
     * @Description: 获取所有的应用列表并缓存，防止多线程调用getInstalledPackages时，返回数据量过大而出现TransactionTooLargeException
     * @return
     * @return: ArrayList<AppInfo>
     */
    public List<AppInfo> getInstalledAppList() {
        synchronized (mLock) {
            List<AppInfo> refList = mAppListRef.get();
            if (refList != null && !refList.isEmpty()) {
                return refList;
            }
            List<PackageInfo> pkiList = mPackageManager.getInstalledPackages(0);
            ArrayList<AppInfo> appList = new ArrayList<>();
            if (pkiList != null) {
                for (PackageInfo pkg : pkiList) {
                    AppInfo appInfo = new AppInfo();
                    appInfo.setUid(pkg.applicationInfo.uid);
                    appInfo.setAppName(pkg.applicationInfo.loadLabel(mPackageManager).toString());
                    appInfo.setPkgName(pkg.packageName);
                    appInfo.setVersionName(pkg.versionName);
                    appInfo.setVersionCode(pkg.versionCode);
                    appInfo.setSystemFlag(
                            (pkg.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
                    appInfo.setIcon(pkg.applicationInfo.loadIcon(mPackageManager));
                    appList.add(appInfo);
                }
            }
            mAppListRef = new SoftReference<ArrayList<AppInfo>>(appList);
            return appList;
        }
    }

    /**
     * @Title: getSystemAppList
     * @Description: 获取系统应用列表
     * @return
     * @return: ArrayList<AppInfo>
     */
    public List<AppInfo> getSystemAppList() {
        List<AppInfo> appList = getInstalledAppList();
        List<AppInfo> systemList = new ArrayList<>();
        if (appList != null) {
            for (AppInfo appInfo : appList) {
                if (appInfo.isSystemFlag()) {
                    systemList.add(appInfo);
                }
            }
        }
        return systemList;
    }

    /**
     * @Description 获取系统应用列表
     * @return List<String> 应用包名列表
     * @author LiYangjie
     */
    public List<String> getSystemAppPkgNameList() {
        List<AppInfo> appList = getInstalledAppList();
        List<String> systemList = new ArrayList<String>();
        if (appList != null) {
            for (AppInfo appInfo : appList) {
                if (appInfo.isSystemFlag()) {
                    systemList.add(appInfo.getPkgName());
                }
            }
        }
        return systemList;
    }

    /**
     * @Title: getThirdPartyAppList
     * @Description: 获取第三方引用列表
     * @return
     * @return: ArrayList<AppInfo>
     */
    public List<AppInfo> getThirdPartyAppList() {
        List<AppInfo> appList = getInstalledAppList();
        List<AppInfo> thirdPartyList = new ArrayList<>();
        if (appList != null) {
            for (AppInfo appInfo : appList) {
                if (!appInfo.isSystemFlag()) {
                    thirdPartyList.add(appInfo);
                }
            }
        }
        return thirdPartyList;
    }

    /**
     * @Title: getAppInfo
     * @Description: 根据包名获取应用信息并缓存
     * @param packageName
     * @return
     * @return: AppInfo
     */
    public AppInfo getAppInfo(String packageName) {
        if (!TextUtils.isEmpty(packageName)) {
            AppInfo appInfo = mAppCache.get(packageName);
            if (appInfo == null) {
                appInfo = getAppInfoByPackageName(mContext, packageName);
                if (appInfo != null) {
                    mAppCache.put(packageName, appInfo);
                }
            }
            return appInfo;
        }
        return null;
    }

}
