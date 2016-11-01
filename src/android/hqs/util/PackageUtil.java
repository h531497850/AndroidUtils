package android.hqs.util;

import java.io.File;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.hqs.tool.LogcatTool;
import android.hqs.tool.TextTool;
import android.hqs.util.ShellUtils.CmdResult;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;


/**
 * 根据包安装卸载应用等的工具类。
 * <ul>
 * <strong>安装软件包</strong>
 * <li>{@link PackageUtil#installNormal(Context, String)}</li>
 * <li>{@link PackageUtil#installSilent(Context, String)}</li>
 * <li>{@link PackageUtil#install(Context, String)}</li>
 * </ul>
 * <ul>
 * <strong>卸载软件包</strong>
 * <li>{@link PackageUtil#uninstallNormal(Context, String)}</li>
 * <li>{@link PackageUtil#uninstallSilent(Context, String)}</li>
 * <li>{@link PackageUtil#uninstall(Context, String)}</li>
 * </ul>
 * <ul>
 * <strong>是否系统应用</strong>
 * <li>{@link PackageUtil#isSystemApplication(Context)}</li>
 * <li>{@link PackageUtil#isSystemApplication(Context, String)}</li>
 * <li>{@link PackageUtil#isSystemApplication(PackageManager, String)}</li>
 * </ul>
 * <ul>
 * <strong>其他</strong>
 * <li>{@link PackageUtil#getInstallLocation()} 获得系统安装位置</li>
 * <li>{@link PackageUtil#isTopActivity(Context, String)} 应用程序包名是否在栈顶</li>
 * <li>{@link PackageUtil#startInstalledAppDetails(Context, String)} 启动 InstalledAppDetails Activity</li>
 * </ul>
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-5-15
 */
public class PackageUtil {
    private static final String TAG = LogcatTool.makeTag(PackageUtil.class);
    /**
     * 应用程序的安装位置设置值，与 {@link #PackageHelper}相同。
     */
    public static final int    APP_INSTALL_AUTO     = 0;
    public static final int    APP_INSTALL_INTERNAL = 1;
    public static final int    APP_INSTALL_EXTERNAL = 2;

    /**
     * 根据给定条件安装
     * <ul>
     * <li>如果是系统应用或者有root权限，查看 {@link #installSilent(Context, String)}</li>
     * <li>或者 {@link #installNormal(Context, String)}</li>
     * </ul>
     * 
     * @param context
     * @param filePath
     * @return
     */
    public static final int install(Context context, String filePath) {
        if (PackageUtil.isSystemApplication(context) || ShellUtils.checkRootPermission()) {
            return installSilent(context, filePath);
        }
        return installNormal(context, filePath) ? INSTALL_SUCCEEDED : INSTALL_FAILED_INVALID_URI;
    }
    
	public static void install(Context context, File file){
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}
	
    /**
     * 调用系统intent安装
     * 
     * @param context
     * @param filePath apk文件路径
     * @return whether apk是否存在
     */
    public static boolean installNormal(Context context, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        if (file == null || !file.exists() || !file.isFile() || file.length() <= 0) {
            return false;
        }

        i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return true;
    }

    /**
     * 使用root静默安装。
     * <ul>
     * <strong>注意事项:</strong>
     * <li>在UI线程内不要调用该耗时操作。</li>
     * <li>如果你是系统应用就不需要申请root权限，但你需要在manifest内添加 <strong>android.permission.INSTALL_PACKAGES</strong>。</li>
     * <li>默认 pm 安装参数是 "-r".</li>
     * </ul>
     * 
     * @param context
     * @param filePath 包文件路径
     * @return {@link PackageUtil#INSTALL_SUCCEEDED} 安装成功，其他的安装失败。 详情查看
     *         {@link PackageUtil}.INSTALL_FAILED_*. 与 {@link PackageManager}.INSTALL_*相同。
     * @see #installSilent(Context, String, String)
     */
    public static int installSilent(Context context, String filePath) {
        return installSilent(context, filePath, " -r " + getInstallLocationParams());
    }

    /**
     * 使用root静默安装。
     * <ul>
     * <strong>注意事项:</strong>
     * <li>在UI线程内不要调用该耗时操作。</li>
     * <li>如果你是系统应用就不需要申请root权限，但你需要在manifest内添加 <strong>android.permission.INSTALL_PACKAGES</strong>。</li>
     * </ul>
     * 
     * @param context
     * @param filePath 包文件路径
     * @param pmParams  pm 安装参数
     * @return {@link PackageUtil#INSTALL_SUCCEEDED} 安装成功，其他的安装失败。 详情查看
     *         {@link PackageUtil}.INSTALL_FAILED_*. 与 {@link PackageManager}.INSTALL_*相同。
     */
    public static int installSilent(Context context, String filePath, String pmParams) {
        if (filePath == null || filePath.length() == 0) {
            return INSTALL_FAILED_INVALID_URI;
        }

        File file = new File(filePath);
        if (file == null || file.length() <= 0 || !file.exists() || !file.isFile()) {
            return INSTALL_FAILED_INVALID_URI;
        }

        /*
         * 如果你是系统应用就不需要申请root权限，但你需要在manifest内添加 <strong>android.permission.INSTALL_PACKAGES</strong>。
         */
        StringBuilder command = new StringBuilder().append("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install ")
                .append(pmParams == null ? "" : pmParams).append(" ").append(filePath.replace(" ", "\\ "));
        CmdResult commandResult = ShellUtils.execCmd(command.toString(), !isSystemApplication(context), true);
        if (commandResult.successMsg != null
                && (commandResult.successMsg.contains("Success") || commandResult.successMsg.contains("success"))) {
            return INSTALL_SUCCEEDED;
        }

        Log.e(TAG,
                new StringBuilder().append("installSilent successMsg:").append(commandResult.successMsg)
                        .append(", ErrorMsg:").append(commandResult.errorMsg).toString());
        if (commandResult.errorMsg == null) {
            return INSTALL_FAILED_OTHER;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_ALREADY_EXISTS")) {
            return INSTALL_FAILED_ALREADY_EXISTS;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_INVALID_APK")) {
            return INSTALL_FAILED_INVALID_APK;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_INVALID_URI")) {
            return INSTALL_FAILED_INVALID_URI;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_INSUFFICIENT_STORAGE")) {
            return INSTALL_FAILED_INSUFFICIENT_STORAGE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_DUPLICATE_PACKAGE")) {
            return INSTALL_FAILED_DUPLICATE_PACKAGE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_NO_SHARED_USER")) {
            return INSTALL_FAILED_NO_SHARED_USER;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_UPDATE_INCOMPATIBLE")) {
            return INSTALL_FAILED_UPDATE_INCOMPATIBLE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_SHARED_USER_INCOMPATIBLE")) {
            return INSTALL_FAILED_SHARED_USER_INCOMPATIBLE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_MISSING_SHARED_LIBRARY")) {
            return INSTALL_FAILED_MISSING_SHARED_LIBRARY;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_REPLACE_COULDNT_DELETE")) {
            return INSTALL_FAILED_REPLACE_COULDNT_DELETE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_DEXOPT")) {
            return INSTALL_FAILED_DEXOPT;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_OLDER_SDK")) {
            return INSTALL_FAILED_OLDER_SDK;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_CONFLICTING_PROVIDER")) {
            return INSTALL_FAILED_CONFLICTING_PROVIDER;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_NEWER_SDK")) {
            return INSTALL_FAILED_NEWER_SDK;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_TEST_ONLY")) {
            return INSTALL_FAILED_TEST_ONLY;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_CPU_ABI_INCOMPATIBLE")) {
            return INSTALL_FAILED_CPU_ABI_INCOMPATIBLE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_MISSING_FEATURE")) {
            return INSTALL_FAILED_MISSING_FEATURE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_CONTAINER_ERROR")) {
            return INSTALL_FAILED_CONTAINER_ERROR;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_INVALID_INSTALL_LOCATION")) {
            return INSTALL_FAILED_INVALID_INSTALL_LOCATION;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_MEDIA_UNAVAILABLE")) {
            return INSTALL_FAILED_MEDIA_UNAVAILABLE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_VERIFICATION_TIMEOUT")) {
            return INSTALL_FAILED_VERIFICATION_TIMEOUT;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_VERIFICATION_FAILURE")) {
            return INSTALL_FAILED_VERIFICATION_FAILURE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_PACKAGE_CHANGED")) {
            return INSTALL_FAILED_PACKAGE_CHANGED;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_UID_CHANGED")) {
            return INSTALL_FAILED_UID_CHANGED;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_NOT_APK")) {
            return INSTALL_PARSE_FAILED_NOT_APK;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_BAD_MANIFEST")) {
            return INSTALL_PARSE_FAILED_BAD_MANIFEST;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION")) {
            return INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_NO_CERTIFICATES")) {
            return INSTALL_PARSE_FAILED_NO_CERTIFICATES;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES")) {
            return INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING")) {
            return INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME")) {
            return INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID")) {
            return INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_MANIFEST_MALFORMED")) {
            return INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_MANIFEST_EMPTY")) {
            return INSTALL_PARSE_FAILED_MANIFEST_EMPTY;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_INTERNAL_ERROR")) {
            return INSTALL_FAILED_INTERNAL_ERROR;
        }
        return INSTALL_FAILED_OTHER;
    }

    /**
     * 根据包名卸载应用。
     * <ul>
     * <li>如果是系统应用或者有root权限, 查看 {@link #uninstallSilent(Context, String)}</li>
     * <li>和 {@link #uninstallNormal(Context, String)}</li>
     * </ul>
     * 
     * @param context
     * @param packageName 应用包名
     * @return 包名是否为空
     * @return
     */
    public static final int uninstall(Context context, String packageName) {
        if (isSystemApplication(context) || ShellUtils.checkRootPermission()) {
            return uninstallSilent(context, packageName);
        }
        return uninstallNormal(context, packageName) ? DELETE_SUCCEEDED : DELETE_FAILED_INVALID_PACKAGE;
    }
    /**
     * 通过系统intent卸载应用。
     * 
     * @param context
     * @param packageName 应用包名
     * @return 包名是否为空
     */
    public static boolean uninstallNormal(Context context, String packageName) {
        if (packageName == null || packageName.length() == 0) {
            return false;
        }
        //Uri uri = Uri.parse("package:" + packageName);
        Uri uri = Uri.parse(new StringBuilder(32).append("package:").append(packageName).toString());
		Intent intent = new Intent(Intent.ACTION_DELETE, uri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        //final int REQUEST_CODE_DELETE = 0;
        //((Activity) context).startActivityForResult(i, REQUEST_CODE_DELETE);
        return true;
    }

    /**
     * 通过root权限静默卸载应用并清除应用的数据
     * 
     * @param context
     * @param packageName 应用包名
     * @return
     * @see #uninstallSilent(Context, String, boolean)
     */
    public static int uninstallSilent(Context context, String packageName) {
        return uninstallSilent(context, packageName, true);
    }

    /**
     * 通过root权限静默卸载应用
     * <ul>
     * <strong>注意事项:</strong>
     * <li>在UI线程内不要调用该耗时操作。</li>
     * <li>如果你是系统应用就不需要申请root权限，但你需要在manifest内添加 <strong>android.permission.INSTALL_PACKAGES</strong>。</li>
     * </ul>
     * 
     * @param context 包文件路径
     * @param packageName 应用包名
     * @param isKeepData 当应用卸载后，是否保持数据和缓存目录。
     * @return <ul>
     *         <li>{@link #DELETE_SUCCEEDED} 卸载成功</li>
     *         <li>{@link #DELETE_FAILED_INTERNAL_ERROR} 内部错误</li>
     *         <li>{@link #DELETE_FAILED_INVALID_PACKAGE} 包名错误</li>
     *         <li>{@link #DELETE_FAILED_PERMISSION_DENIED} 权限被拒绝</li>
     */
    public static int uninstallSilent(Context context, String packageName, boolean isKeepData) {
        if (packageName == null || packageName.length() == 0) {
            return DELETE_FAILED_INVALID_PACKAGE;
        }

        /*
         * 如果你是系统应用就不需要申请root权限，但你需要在manifest内添加 <strong>android.permission.INSTALL_PACKAGES</strong>。
         */
        StringBuilder command = new StringBuilder().append("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm uninstall")
                .append(isKeepData ? " -k " : " ").append(packageName.replace(" ", "\\ "));
        CmdResult commandResult = ShellUtils.execCmd(command.toString(), !isSystemApplication(context), true);
        if (commandResult.successMsg != null
                && (commandResult.successMsg.contains("Success") || commandResult.successMsg.contains("success"))) {
            return DELETE_SUCCEEDED;
        }
        Log.e(TAG,
                new StringBuilder().append("uninstallSilent successMsg:").append(commandResult.successMsg)
                        .append(", ErrorMsg:").append(commandResult.errorMsg).toString());
        if (commandResult.errorMsg == null) {
            return DELETE_FAILED_INTERNAL_ERROR;
        }
        if (commandResult.errorMsg.contains("Permission denied")) {
            return DELETE_FAILED_PERMISSION_DENIED;
        }
        return DELETE_FAILED_INTERNAL_ERROR;
    }
    
    public static PackageInfo getPackageInfo(Context context) throws NameNotFoundException {
		return getPackageInfo(context.getPackageManager(), context.getPackageName());
	}
    
    public static PackageInfo getPackageInfo(Context context, String packageName) throws NameNotFoundException {
		return getPackageInfo(context.getPackageManager(), packageName);
	}
	
	public static PackageInfo getPackageInfo(PackageManager manager, String packageName) throws NameNotFoundException {
		return getPackageInfo(manager, packageName, PackageManager.GET_ACTIVITIES);
	}
	
	public static PackageInfo getPackageInfo(Context context, String packageName, int flags) throws NameNotFoundException{
		return context.getPackageManager().getPackageInfo(packageName, flags);
	}
	public static PackageInfo getPackageInfo(PackageManager manager, String packageName, int flags) 
			throws NameNotFoundException{
		return manager.getPackageInfo(packageName, flags);
	}
	
	public static PackageInfo getPackageArchiveInfo(Context context, String packageName) {
		return context.getPackageManager().getPackageArchiveInfo(packageName, PackageManager.GET_ACTIVITIES);
	}
	
	public static String getMetaData (Context context, String packageName) {
		try {
        	PackageManager pm = context.getPackageManager();
            return pm.getApplicationLabel(pm.getApplicationInfo(packageName,PackageManager.GET_META_DATA)).toString();
        } catch (Exception e) {
        	Log.e(TAG, "get title fail.", e);
        	return "";
        }
	}

    /**
     * context是否是系统应用
     * 
     * @param context
     * @return
     */
    public static boolean isSystemApplication(Context context) {
        if (context == null) {
            return false;
        }
        return isSystemApplication(context, context.getPackageName());
    }
    /**
     * packageName是否是系统应用
     * 
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isSystemApplication(Context context, String packageName) {
        if (context == null) {
            return false;
        }
        return isSystemApplication(context.getPackageManager(), packageName);
    }
    /**
     * packageName是否是系统应用
     * 
     * @param manager
     * @param packageName
     * @return <ul>
     *         <li>如果packageManager为null, 返回false</li>
     *         <li>如果packageName为null或empty, 返回false</li>
     *         <li>如果packageName不存在, 返回false</li>
     *         <li>如果packageName, 但不是系统应用, 返回false</li>
     *         <li>其他返回true</li>
     *         </ul>
     */
    public static boolean isSystemApplication(PackageManager manager, String packageName) {
        if (manager == null || TextTool.isNull(packageName)) {
            return false;
        }

        try {
            ApplicationInfo app = manager.getApplicationInfo(packageName, 0);
            return (app != null && (app.flags & ApplicationInfo.FLAG_SYSTEM) > 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 应用包名是否在栈顶
     * <ul>
     * <strong>注意事项:</strong>
     * <li>你需要在manifest内添加<strong>android.permission.GET_TASKS</strong></li>
     * </ul>
     * 
     * @param context
     * @param packageName
     * @return 如果参数错误或任务栈为null，返回null，否则返回应用是否在栈顶。
     */
    public static Boolean isTopActivity(Context context, String packageName) {
        if (context == null || TextUtils.isEmpty(packageName)) {
            return null;
        }

        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        
        if (tasksInfo == null || tasksInfo.isEmpty()) {
            return null;
        }
        try {
            return packageName.equals(tasksInfo.get(0).topActivity.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取系统安装路径<br/>
     * 可以通过系统菜单设置 Setting->Storage->Preferred install location（首选安装位置）
     * 
     * @return
     * @see {@link IPackageManager#getInstallLocation()}
     */
    public static int getInstallLocation() {
        CmdResult commandResult = ShellUtils.execCmd(
                "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm get-install-location", false, true);
        if (commandResult.result == 0 && commandResult.successMsg != null && commandResult.successMsg.length() > 0) {
            try {
                int location = Integer.parseInt(commandResult.successMsg.substring(0, 1));
                switch (location) {
                    case APP_INSTALL_INTERNAL:
                        return APP_INSTALL_INTERNAL;
                    case APP_INSTALL_EXTERNAL:
                        return APP_INSTALL_EXTERNAL;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Log.e(TAG, "pm get-install-location error");
            }
        }
        return APP_INSTALL_AUTO;
    }

    /**
     * 通过 pm 获取安装路径参数。
     * 
     * @return
     */
    private static String getInstallLocationParams() {
        int location = getInstallLocation();
        switch (location) {
            case APP_INSTALL_INTERNAL:
                return "-f";
            case APP_INSTALL_EXTERNAL:
                return "-s";
        }
        return "";
    }

    /**
     * 启动InstalledAppDetails Activity
     * 
     * @param context
     * @param packageName
     */
    public static void startInstalledAppDetails(Context context, String packageName) {
        Intent intent = new Intent();
        int sdkVersion = Build.VERSION.SDK_INT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", packageName, null));
        } else {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra((sdkVersion == Build.VERSION_CODES.FROYO ? "pkg"
                    : "com.android.settings.ApplicationPkgName"), packageName);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 安装时的返回码。<br/>
     * 安装成功
     */
    public static final int INSTALL_SUCCEEDED                              = 1;
    /**
     * 安装时的返回码。<br/>
     * 已安装不再安装
     */
    public static final int INSTALL_FAILED_ALREADY_EXISTS                  = -1;

    /**
      * 安装时的返回码。<br/>
      * 包归档文件无效
     */
    public static final int INSTALL_FAILED_INVALID_APK                     = -2;

    /**
     * 安装时的返回码。<br/>
     * 无效URI
     */
    public static final int INSTALL_FAILED_INVALID_URI                     = -3;

    /**
     * 安装时的返回码。<br/>
     * 包管理器服务发现该设备没有足够的存储空间来安装应用程序。
     */
    public static final int INSTALL_FAILED_INSUFFICIENT_STORAGE            = -4;

    /**
     * 安装时的返回码。<br/>
     * 已经安装了相同的包名的程序。
     */
    public static final int INSTALL_FAILED_DUPLICATE_PACKAGE               = -5;

    /**
     * 安装时的返回码。<br/>
     * 所请求的共享用户不存在。
     */
    public static final int INSTALL_FAILED_NO_SHARED_USER                  = -6;

    /**
     * 安装时的返回码。<br/>
     * 已安装的包名与新包（且旧包的数据没有被删除）签名不同。
     */
    public static final int INSTALL_FAILED_UPDATE_INCOMPATIBLE             = -7;

    /**
     * 安装时的返回码。<br/>
     * 新安装包请求的共享用户已安装在该设备上，但签名不匹配。
     */
    public static final int INSTALL_FAILED_SHARED_USER_INCOMPATIBLE        = -8;

    /**
     * 安装时的返回码。<br/>
     * 新安装包使用了一个不可用的共享库。
     */
    public static final int INSTALL_FAILED_MISSING_SHARED_LIBRARY          = -9;

    /**
     * 安装时的返回码。<br/>
     * 新安装包使用了一个不可用的共享库。
     */
    public static final int INSTALL_FAILED_REPLACE_COULDNT_DELETE          = -10;

    /**
     * 安装时的返回码。<br/>
     * 由于没有足够的存储空间或者验证失败，当优化和验证该安装包的dex文件时失败。
     */
    public static final int INSTALL_FAILED_DEXOPT                          = -11;

    /**
     * 安装时的返回码。<br/>
     * 当前版本比安装包所需SDK低，安装失败。
     */
    public static final int INSTALL_FAILED_OLDER_SDK                       = -12;

    /**
     * 安装时的返回码。<br/>
     * 安装包的content provider已安装在系统，安装失败。
     */
    public static final int INSTALL_FAILED_CONFLICTING_PROVIDER            = -13;

    /**
     * 安装时的返回码。<br/>
     * 当前版本比安装包所需SDK比高，安装失败。
     */
    public static final int INSTALL_FAILED_NEWER_SDK                       = -14;

    /**
     * 安装时的返回码。<br/>
     * 该包被指定为测试包并且caller没有提供{@link #INSTALL_ALLOW_TEST} flag，导致安装失败。
     */
    public static final int INSTALL_FAILED_TEST_ONLY                       = -15;

    /**
     * 安装时的返回码。<br/>
     * 安装包包含本机代码，但与设备的CPU_ABI不兼容。
     */
    public static final int INSTALL_FAILED_CPU_ABI_INCOMPATIBLE            = -16;

    /**
     * 安装时的返回码。<br/>
     * 新的包使用了不可用的功能。
     */
    public static final int INSTALL_FAILED_MISSING_FEATURE                 = -17;

    /**
     * 安装时的返回码。<br/>
     * 安全容器的嵌入点不能访问外部媒体。
     */
    public static final int INSTALL_FAILED_CONTAINER_ERROR                 = -18;

    /**
     * 安装时的返回码。<br/>
     * 新包无法安装在指定位置。
     */
    public static final int INSTALL_FAILED_INVALID_INSTALL_LOCATION        = -19;

    /**
     * 安装时的返回码。<br/>
     * 由于媒体不可用，新包无法安装在指定位置。
     */
    public static final int INSTALL_FAILED_MEDIA_UNAVAILABLE               = -20;

    /**
     * 安装时的返回码。<br/>
     * 安装验证超时。
     */
    public static final int INSTALL_FAILED_VERIFICATION_TIMEOUT            = -21;

    /**
     * 安装时的返回码。<br/>
     * 安装验证失败。
     */
    public static final int INSTALL_FAILED_VERIFICATION_FAILURE            = -22;

    /**
     * 安装时的返回码。<br/>
     * 调用程序所期望的安装包改变了。
     */
    public static final int INSTALL_FAILED_PACKAGE_CHANGED                 = -23;

    /**
     * 安装时的返回码。<br/>
     * 安装包的UID与先前的不同。
     */
    public static final int INSTALL_FAILED_UID_CHANGED                     = -24;

    /**
     * 安装时的返回码。<br/>
     * 给定的路径不是文件或不是“.apk”的拓展，解析失败。
     */
    public static final int INSTALL_PARSE_FAILED_NOT_APK                   = -100;

    /**
     * 安装时的返回码。<br/>
     * 解析器无法检索AndroidManifest.xml文件。
     */
    public static final int INSTALL_PARSE_FAILED_BAD_MANIFEST              = -101;

    /**
     * 安装时的返回码。<br/>
     * 解析时发生意外异常。
     */
    public static final int INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION      = -102;

    /**
     * 安装时的返回码。<br/>
     * 在.apk文件内没发现任何证书。
     */
    public static final int INSTALL_PARSE_FAILED_NO_CERTIFICATES           = -103;

    /**
     * 安装时的返回码。<br/>
     * 在.apk文件内发现证书不一致。
     */
    public static final int INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES = -104;

    /**
     * 安装时的返回码。<br/>
     * 安装失败，如果解析器在一个apk文件中遇到CertificateEncodingException
     */
    public static final int INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING      = -105;

    /**
     * 安装时的返回码。<br/>
     * 安装失败，如果解析器在清单中遇到不好的或遗失的包名
     */
    public static final int INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME          = -106;

    /**
     * 安装时的返回码。<br/>
     * 安装失败，如果解析器在清单中遇到不好的sharedUserId
     */
    public static final int INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID        = -107;

    /**
     * 安装时的返回码。<br/>
     * 安装失败，如果解析器在清单中遇到了一些结构性问题。
     */
    public static final int INSTALL_PARSE_FAILED_MANIFEST_MALFORMED        = -108;

    /**
     * 安装时的返回码。<br/>
     * 安装失败，如果parser（解析器）在manifest（清单）中没有找到任何可操作的标签（instrumentation或应用程序）。
     */
    public static final int INSTALL_PARSE_FAILED_MANIFEST_EMPTY            = -109;

    /**
     * 安装时的返回码。<br/>
     * 安装失败，系统原因导致
     */
    public static final int INSTALL_FAILED_INTERNAL_ERROR                  = -110;
    /**
     * 安装时的返回码。<br/>
     * 安装失败，其他原因导致
     */
    public static final int INSTALL_FAILED_OTHER                           = -1000000;

    /**
     * 卸载时的返回码<br/>
     * 卸载成功
     */
    public static final int DELETE_SUCCEEDED                               = 1;

    /**
     * 卸载时的返回码<br/>
     * 卸载失败，由于不明原因系统删除安装包失败
     */
    public static final int DELETE_FAILED_INTERNAL_ERROR                   = -1;

    /**
     * 卸载时的返回码<br/>
     * 卸载失败，由于该package是活动的DevicePolicy管理器.
     */
    public static final int DELETE_FAILED_DEVICE_POLICY_MANAGER            = -2;

    /**
     * 卸载时的返回码<br/>
     * 卸载失败，包名无效
     */
    public static final int DELETE_FAILED_INVALID_PACKAGE                  = -3;

    /**
     * 卸载时的返回码<br/>
     * 卸载失败，权限被拒绝
     */
    public static final int DELETE_FAILED_PERMISSION_DENIED                = -4;
}

