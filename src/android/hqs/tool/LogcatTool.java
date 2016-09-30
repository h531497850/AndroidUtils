package android.hqs.tool;

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
public class LogcatTool {
	
	/**
	 * 调试开关统一在这里设置，在每个类里单独设置，类一多不方便管理
	 */
	public static final boolean DEBUG = true;
	
	/**
	 * 统一创建日志标签：前缀“hqs.”，这样好帅选日志，不用在用adb loagcat获取日志是每个类都加一个，类一多，很麻烦，还容易出错。
	 * 
	 * adb logcat     //显示全部日志
     * adb logcat > E:\A_Projects\log\test.log //将日志保存到文件test.log
	 *
	 * 根据tag标记和级别过滤日志输出：
	 * 仅输出标记为“ActivityManager”且优先级大于等于“Info”和标记为“PowerManagerService”并且优先级大于等于“Debug”的日志：
	 * adb logcat ActivityManager:I PowerManagerService:D *:S
	 * 注：*:S用于设置所有标记的日志优先级为S，这样可以确保仅输出符合条件的日志。
	 * 
	 * @Desription linux平台：adb logcat | grep "hqs."
	 * 			   Windows平台：adb logcat -v time | findstr "hqs."
	 * 
	 * @param clazz
	 *            你的类对象
	 * @return hqs.类名
	 */
	public static final String makeTag(Class<?> clazz){
		return "hqs." + clazz.getSimpleName();
	}

	// 命令：LogCat [options] [filterspecs]
	public static final String SELECT = "logcat";
	
	// 下面是options（选项）
	/**
	 * 设置过滤器，如指定 '*:s'，默认设置过滤为无声的
	 */
	public static final String SILENT = "-s";
	/**
	 * <filename> 输出到文件，默认情况是标准输出
	 */
	public static final String FILE = "-f <filename>";
	/**
	 * [<kbytes>] 设置环形日志缓冲区的kbytes（默认16），需要 -f一起使用
	 */
	public static final String ROTATE = "-r [<kbytes>]";
	/**
	 * <count> 设置环形日志缓冲区的最大数目<计数>，默认值4，需要和 -r 选项一起使用。
	 */
	public static final String NUMBER = "-n <count>";
	
	/**
	 * 1) 只输出指定 标签 和 类型 的日志 <p>
	 * 格式： <p>
	 * adb logcat <日志标签>:<日志类型标识符> <日志标签>:<日志类型标识符> ...*:S <p>
	 * 注：<p>
	 * 	1. 可以写多个 <日志标签>:<日志类型标识符> 之间用空格分隔；<p>
	 * 	2. 最后必须是 *:S ，表示其它的都不要显示出来<p>
	 * 例如：<p>
	 * $ adb logcat dalvikvm:D Checkin:W *:S<p>
	 * 注：adb logcat Checkin *:S =等同于=> adb logcat Checkin:V *:S <p>
	 * 注：以上命令均没加 -v来指出日志格式，即默认为: ANDROID_PRINTF_LOG 或 brief 格式集。<p>
	 * 
	 * 2) 输出指定 标签 和 类型 的带有格式的日志 <p>
	 * 注：以下测试日志内容为：test log format，即 eclipse 中的logcat 图形界面里的 Text 中的内容！<p>
	 *
	 * <format> 设置log的打印格式（见下面的格式打印格式）(不能组合使用)<p>
	 *  
	 * 1. brief - 日志类型/日志标签(进程ID): 日志内容<p>
	 * 	例如：$ adb logcat -v brief Checkin *:S I/Checkin(24713): test log format<p>
	 *  
	 * 2. process - 日志类型(进程ID) 日志内容 (日志标签) <p>
	 * 	例如：$ adb logcat -v process Checkin *:S I(24713) test log format (Checkin)<p>
	 * 
	 * 3. tag - 日志类型/日志标签: 日志内容 <p>
	 * 	例如：$ adb logcat -v tag Checkin *:S I/Checkin: test log format<p>
	 * 
	 * 4. thread - 日志类型(进程ID:线程ID) <p>
	 * 	例如：$ adb logcat -v thread Checkin *:S I(24713:0x6089) test log format<p>
	 * 
	 * 5. raw - 日志内容 例如：$ adb logcat -v raw Checkin *:S test log format<p>
	 * 
	 * 6. time - 日期 调用时间 日志类型/日志标签(进程ID): 日志内容 <p>
	 * 例如：$ adb logcat -v time Checkin *:S 05-27 11:25:33.854 I/Checkin(24713): test log format<p>
	 * 
	 * 7. threadtime - 日期 调用时间 进程ID 线程ID 日志类型 日志标签: 日志内容 <p>
	 * 例如：$ adb logcat -v time Checkin *:S 05-27 11:25:33.854 24713 24713 I Checkin: test log format<p>
	 * 注：只有此种格式时 线程ID为十进制数。<p>
	 * 
	 * 8. long - [ 日期 调用时间 进程ID:线程ID 日志类型/日志标签 ] 转行显示 日志内容 <p>
	 * 例如：$ adb logcat -v long Checkin *:S [ 05-27 11:25:33.854 24713:0x6089 I/Checkin ] test log format<p>
	 */
	public static final String FORMAT = "-v <format>";
	/**
	 * 清除所有log并退出
	 */
	public static final String CLEAR = "-c";
	/**
	 * 得到所有log并退出 (不阻塞)
	 */
	public static final String DUMP = "-d";
	/**
	 * <count>仅打印最近的由参数 count 指出的行数(必然包含 -d)
	 */
	public static final String T = "-t <count>";
	/**
	 * 得到环形缓冲区的大小并退出
	 */
	public static final String GET = "-g";
	/**
	 * <buffer> 请求不同的环形缓冲区('main' (默认), 'radio', 'events'，'system')。
	 *  多个 -b 参数是被允许，并且结果是交错输出的。-b main -b system 是默认的。
	 */
	public static final String BUFFER = "-b <buffer>";
	/**
	 * 输出log到二进制中
	 */
	public static final String BINARY = "-B";
	
	/*
	 * 过滤器的格式是一个这样的串： 
	 * <tag>[:priority] 其中 <tag> 表示log的component， tag (或者使用 *表示所有) ， priority 如下所示: 
	 * tag 是 eclipse 中 logcat 图形界面中 Tag 的内容(或者有 *表示全部)，它之后的冒号(:)后面跟优先级： 
	 * '*' means '*:d' and <tag> by itself means <tag>:v
	 */
	public static final int fatal = 8;
	public static final int silent  = 9;
	
	/**
	 * 根据优先级获取字段
	 * @param priorty
	 * @return 字段
	 */
	public static String priortyValue(int priorty) {
		switch (priorty) {
		case Log.VERBOSE:
			return "verbose";
		case Log.DEBUG:
			return "debug";
		case Log.INFO:
			return "info";
		case Log.WARN:
			return "warn";
		case Log.ERROR:
			return "error";
		case Log.ASSERT:
			return "assert";
		case LogcatTool.fatal:
			return "fatal";
		case LogcatTool.silent:
			return "silent";
		default:
			return null;
		}
	}
	
	/**
	 * 根据优先级获取命令
	 * @param priorty
	 * @return 命令
	 */
	public static String priortyCmd(int priorty) {
		switch (priorty) {
		case Log.VERBOSE:
			return "v";
		case Log.DEBUG:
			return "d";
		case Log.INFO:
			return "i";
		case Log.WARN:
			return "w";
		case Log.ERROR:
			return "e";
		case Log.ASSERT:
			return "a";
		case LogcatTool.fatal:
			return "f";
		case LogcatTool.silent:
			return "s";
		default:
			return null;
		}
	}

}
