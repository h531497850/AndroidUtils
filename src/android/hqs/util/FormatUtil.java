package android.hqs.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatUtil {
	
	/**
	 * 格式化所给数据大小（B/KB/MB/GB/TB）
	 * 
	 * @param bytes 数据字节大小
	 * @return the formatted size such as 4.52 MB or 245 KB or 332 bytes
	 */
	public static String fileSize(double bytes) {
		// TODO: I18N
		if (bytes > 1024 * 1024 * 1024 * 1024) {
			return String.format(Locale.getDefault(), "%.2f TB", (bytes / (1024 * 1024 * 1024 * 1024)));
		} else if (bytes > 1024 * 1024 * 1024) {
			return String.format(Locale.getDefault(), "%.2f GB", (bytes / (1024 * 1024 * 1024)));
		} else if (bytes > 1024 * 1024) {
			return String.format(Locale.getDefault(), "%.2f MB", (bytes / (1024 * 1024)));
		} else if (bytes > 1024) {
			return String.format(Locale.getDefault(), "%.2f KB", (bytes / 1024));
		} else {
			return String.format(Locale.getDefault(), "%d B", (int) bytes);
		}
	}

	/**
	 * 格式化所给数据大小（B/KB/MB/GB/TB），超过1024TB将不再计算
	 * @param bSize 字节大小
	 * @return 格式化的字符串如：100.02GB、4.52MB or 245KB or 332B
	 */
	public static String fileSize(float bSize) {
		DecimalFormat format = new DecimalFormat("###,###,##0.00");
		if (bSize < 1024) {
			format.applyPattern("###,###,##0.00B");
		} else if (bSize >= 1024 && bSize < 1024 * 1024) {
			bSize /= 1024;
			format.applyPattern("###,###,##0.00KB");
		} else if (bSize >= 1024 * 1024 && bSize < 1024 * 1024 * 1024) {
			bSize /= (1024 * 1024);
			format.applyPattern("###,###,##0.00MB");
		} else if (bSize >= 1024 * 1024 * 1024 && bSize < 1024 * 1024 * 1024 * 1024) {
			bSize /= (1024 * 1024 * 1024);
			format.applyPattern("###,###,##0.00GB");
		} else if (bSize >= 1024 * 1024 * 1024 * 1024 && bSize < 1024 * 1024 * 1024 * 1024 * 1024) {
			bSize /= (1024 * 1024 * 1024 * 1024);
			format.applyPattern("###,###,##0.00TB");
		}
		return format.format(bSize);
	}
	
	/**
	 * 时间日期格式<br/>
	 * 时间日期标识符：<br/>
	 * yyyy：年<br/>
	 * MM：月<br/>
	 * dd：日<br/>
	 * hh：1~12小时制(1-12)<br/>
	 * HH：24小时制(0-23)<br/>
	 * mm：分<br/>
	 * ss：秒<br/>
	 * S：毫秒<br/>
	 * E：星期几<br/>
	 * D：一年中的第几天<br/>
	 * F：一月中的第几个星期(会把这个月总共过的天数除以7)<br/>
	 * w：一年中的第几个星期<br/>
	 * W：一月中的第几星期(会根据实际情况来算)<br/>
	 * a：上下午标识<br/>
	 * k：和HH差不多，表示一天24小时制(1-24)。<br/>
	 * K：和hh <strong>style="font-family: Arial, Helvetica, sans-serif;">差不多，</strong>
	 * <strong>style="font-family: Arial, Helvetica, sans-serif;"</strong>，表示一天12小时制<br/>
	 * z：表示时区<br/>
	 * 例：SimpleDateFormat sdf = new SimpleDateFormat("现在是yyyy年MM月dd日 HH(hh)时   mm分 ss秒 S毫秒   星期E 今年的第D天  这个月的第F星期   今年的第w个星期   这个月的第W个星期  今天的a k1~24制时间 K0-11小时制时间 z时区"); 
	 * 
	 * @param time
	 * @param pattern 就是上面参数的结合
	 *            
	 * @return
	 */
	public static String time(long time, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
		return dateFormat.format(new Date(time));
	}
	
	public static String time(String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
		return dateFormat.format(new Date());
	}

}
