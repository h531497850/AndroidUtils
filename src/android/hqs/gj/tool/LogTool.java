package android.hqs.gj.tool;

import android.os.Build;
//import android.os.SystemProperties;
import android.util.Log;

/**
 * 1、tee使用 功能说明：读取标准输入的数据，并将其内容输出成文件。 
 * 语 法：tee [-ai][--help][--version][文件...]
 * 补充说明：tee指令会从标准输入设备读取数据，将其内容输出到标准输出设备，同时保存成文件。 
 * 参 数：
 *  -a或--append 附加到既有文件的后面，而非覆盖它。 
 *  -i-i或--ignore-interrupts 忽略中断信号。 
 *  --help 在线帮助。 
 *  --version 显示版本信息。
 * 
 * 2、grep使用 
 * 3、Logcat、tee和grep结合使用
 * 三者结合到一起可以实现：log日志同时输出到终端和本地文件，对于输出到终端的log按照grep的要求进行过滤显示。 
 * 比如：sudo ./adb logcat -v time | tee /home/Linux/main.txt |grep 'accept'
 * ——输入Android应用层日志到屏幕和main.txt文件，同时过滤屏幕的日志显示包含字符串“accept”的行，以关注自己需要的信息。
 * 
 * 4、优先级是下面的字符，顺序是从低到高：
 *  V — 明细 verbose(最低优先级) 
 *  D — 调试 debug 
 *  I — 信息 info 
 *  W — 警告 warn 
 *  E — 错误 error 
 *  F — 严重错误 fatal 
 *  S — 无记载 silent(最高优先级)
 *  
 * @author 胡青松
 */
public class LogTool {

    // Do you want to open the debug switch, user (doesn't open), eng (open)?
    private static final boolean IS_ENG = Build.TYPE.equals("eng");
//    private static final boolean IS_DEBUG = SystemProperties.getBoolean("debug.secure.debug", false);
    public static final boolean DEBUG = /*IS_DEBUG*/ true || isLogOpen() || IS_ENG;

    /**
     * 创建打印日志的标签
     * 
     * @param clazz 类对象
     * @return 企业内不准用开发者用自己的名字等标签
     */
    public static final String makeTag(Class<?> clazz) {
        // UserDataCollector
        return "UDC_" + clazz.getSimpleName();
    }

    /**
     * 在文件被混淆时，调用该方法直接设置tag
     */
    public static final String makeTag(String className) {
        // UserDataCollector
        return "UDC_" + className;
    }

    /**
     * 调试日志开关是否打开，可以监听广播，"android.vivo.bbklog.action.CHANGED"
     */
    private static boolean isLogOpen() {
        // return "yes".equals(SystemProperties.get("persist.sys.log.ctrl", "no"));
        return true;
    }

    /** 蓝色，调试信息 */
    public static final void debug(String tag, Object obj) {
        if (DEBUG)
            Log.d(tag, String.valueOf(obj));
    }

    public static final void debug(String tag, Throwable tr) {
        if (DEBUG)
            Log.d(tag, "", tr);
    }

    public static final void debug(String tag, Object obj, Throwable tr) {
        if (DEBUG)
            Log.d(tag, String.valueOf(obj), tr);
    }

    /** 绿色，正常信息 */
    public static final void info(String tag, Object obj) {
        Log.i(tag, String.valueOf(obj));
    }

    /** 绿色，正常信息 */
    public static final void info(String tag, Throwable tr) {
        Log.i(tag, "", tr);
    }

    public static final void info(String tag, Object obj, Throwable tr) {
        Log.i(tag, String.valueOf(obj), tr);
    }

    /** 黑色，冗长信息 */
    public static final void verbose(String tag, Object obj) {
        Log.v(tag, String.valueOf(obj));
    }

    public static final void verbose(String tag, Object obj, Throwable tr) {
        Log.v(tag, String.valueOf(obj), tr);
    }

    /** 红色，错误信息 */
    public static final void error(String tag, Object obj) {
        Log.e(tag, String.valueOf(obj));
    }

    public static final void error(String tag, Throwable tr) {
        Log.e(tag, "", tr);
    }

    public static final void error(String tag, Object obj, Throwable tr) {
        Log.e(tag, String.valueOf(obj), tr);
    }

    /** 红色，错误信息 */
    public static final void warn(String tag, Object obj) {
        Log.w(tag, String.valueOf(obj));
    }

    public static final void warn(String tag, Throwable tr) {
        Log.w(tag, "", tr);
    }

    public static final void warn(String tag, Object obj, Throwable tr) {
        Log.w(tag, String.valueOf(obj), tr);
    }

}
