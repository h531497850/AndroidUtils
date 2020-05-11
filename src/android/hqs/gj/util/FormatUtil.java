
package android.hqs.gj.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FormatUtil {

    /**
     * Sat Mar 19 06:59:41 CST 2016
     */
    public static final DateFormat EN = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'CST' yyyy", Locale.ENGLISH);
    /**
     * 2016-03-19
     */
    private static final DateFormat DAY = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    /**
     * 2016-03-19 06:59:41
     */
    public static final DateFormat NORMAL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    /**
     * 2016-03-19-06-59-41
     */
    public static final DateFormat HORIZONTAL = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.ENGLISH);
    /**
     * 2016-03-19-06-59-41-981
     */
    public static final DateFormat HORIZONTAL_MILL = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.ENGLISH);
    /**
     * 2016_0319_065941
     */
    private static final DateFormat CONN = new SimpleDateFormat("yyyy_MMdd_HHmmss", Locale.ENGLISH);
    /**
     * 2016年03月19日 06时59分41秒
     */
    private static final DateFormat ZH = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒", Locale.CHINESE);
    /**
     * 2016年03月19日
     */
    private static final DateFormat ZH_DAY = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINESE);

    public static final long KB = 1024;
    public static final long MB = 1024 * KB;
    public static final long GB = 1024 * MB;
    public static final long TB = 1024 * GB;
    public static final long PB = 1024 * TB;

    /**
     * Format the given data size(B/KB/MB/GB/TB)
     * 
     * @return The formatted size such as 4.52 MB or 245 KB or 332 bytes
     */
    public static String fileSize(double bytes) {
        // TODO: I18N
        if (bytes > TB) {
            return String.format(Locale.ENGLISH, "%.2f TB", (bytes / TB));
        } else if (bytes > GB) {
            return String.format(Locale.ENGLISH, "%.2f GB", (bytes / GB));
        } else if (bytes > MB) {
            return String.format(Locale.ENGLISH, "%.2f MB", (bytes / MB));
        } else if (bytes > KB) {
            return String.format(Locale.ENGLISH, "%.2f KB", (bytes / KB));
        } else {
            return String.format(Locale.ENGLISH, "%d B", (int) bytes);
        }
    }

    /**
     * Format the given data size(B/KB/MB/GB/TB),More than 1024TB will no longer compute.
     * 
     * @return Formatted strings, such as: 100.02GB or 4.52MB or 245KB or 332B
     */
    public static String fileSize(float bytes) {
        DecimalFormat format = new DecimalFormat("###,###,##0.00");
        if (bytes < KB) {
            format.applyPattern("###,###,##0.00B");
        } else if (bytes < MB) {
            bytes /= KB;
            format.applyPattern("###,###,##0.00KB");
        } else if (bytes < GB) {
            bytes /= MB;
            format.applyPattern("###,###,##0.00MB");
        } else if (bytes < TB) {
            bytes /= GB;
            format.applyPattern("###,###,##0.00GB");
        } else if (bytes < PB) {
            bytes /= TB;
            format.applyPattern("###,###,##0.00TB");
        }
        return format.format(bytes);
    }

    /**
     * @param pattern Format: yyyy-MM-dd, yyyy-MM-dd HH:mm:ss:SSS
     */
    public static SimpleDateFormat getDataFormat(String pattern) {
        return new SimpleDateFormat(pattern, Locale.ENGLISH);
    }

    public static String getTime(String pattern) {
        return getDataFormat(pattern).format(new Date(System.currentTimeMillis()));
    }

    /**
     * Format current time
     * 
     * @param pattern Format: yyyy-MM-dd
     */
    public static String getTimeDay() {
        return DAY.format(new Date(System.currentTimeMillis()));
    }

    /**
     * yyyy-MM-dd
     */
    public static String getTimeDay(long time) {
        return DAY.format(new Date(time));
    }

    /**
     * EEE MMM dd HH:mm:ss 'CST' yyyy
     */
    public static String getTimeEn() {
        return EN.format(new Date(System.currentTimeMillis()));
    }

    /**
     * EEE MMM dd HH:mm:ss 'CST' yyyy
     */
    public static String getTimeEn(long time) {
        return EN.format(new Date(time));
    }

    /**
     * yyyy-MM-dd-HH-mm-ss
     */
    public static String getTimeHori() {
        return HORIZONTAL.format(new Date(System.currentTimeMillis()));
    }

    /**
     * yyyy-MM-dd-HH-mm-ss
     */
    public static String getTimeHori(long time) {
        return HORIZONTAL.format(new Date(time));
    }

    /**
     * Formatting incoming time
     * 
     * @param time format: yyyy_MMdd_HHmmss
     * @return yyyy-MM-dd-HH-mm-ss
     */
    public static String getTimeHori(String time) {
        try {
            return HORIZONTAL.format(CONN.parse(time));
        } catch (Exception e) {
            return "unknown";
        }
    }

    /**
     * Gets the timestamp of the incoming date
     * 
     * @param date format: yyyy_MMdd_HHmmss
     * @return yyyy-MM-dd-HH-mm-ss
     */
    public static long getLTime(String date) {
        try {
            return CONN.parse(date).getTime();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Formatting incoming time
     * 
     * @param time format: 1496499093242
     * @return yyyy-MM-dd-HH-mm-ss-SSS
     */
    public static String getTimeHoriMill(String time) {
        try {
            return HORIZONTAL_MILL.format(Long.parseLong(time));
        } catch (Exception e) {
            return HORIZONTAL_MILL.format(new Date(System.currentTimeMillis()));
        }
    }

    /**
     * Formatting incoming time
     * 
     * @param time format: 1496499093242
     * @return yyyy-MM-dd-HH-mm-ss-SSS
     */
    public static String getTimeHoriMill2(String time) {
        try {
            return HORIZONTAL_MILL.format(Long.parseLong(time));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Formatting incoming time
     * 
     * @param time format: 1496499093242
     * @return yyyy-MM-dd-HH-mm-ss-SSS
     */
    public static String getTimeHoriMill(long time) {
        try {
            return HORIZONTAL_MILL.format(new Date(time));
        } catch (Exception e) {
            return HORIZONTAL_MILL.format(new Date(System.currentTimeMillis()));
        }
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static String getTimeNor() {
        return NORMAL.format(new Date(System.currentTimeMillis()));
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static String getTimeNor(long time) {
        return NORMAL.format(new Date(time));
    }

    /**
     * @param date yyyy-MM-dd HH:mm:ss
     */
    public static long getTimeNor(String date) {
        try {
            return NORMAL.parse(date).getTime();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * yyyy年MM月dd日 HH时mm分ss秒
     */
    public static String getTimeZh() {
        return ZH.format(new Date(System.currentTimeMillis()));
    }

    /**
     * yyyy年MM月dd日 HH时mm分ss秒
     */
    public static String getTimeZh(long time) {
        return ZH.format(new Date(time));
    }

    /**
     * yyyy年MM月dd日
     */
    public static String getTimeZhDay() {
        return ZH_DAY.format(new Date(System.currentTimeMillis()));
    }

    /**
     * yyyy年MM月dd日
     */
    public static String getTimeZhDay(long time) {
        return ZH_DAY.format(new Date(time));
    }

    /**
     * 要显示的元素有多少字节格式化的字母就可以写几位：2050（yyyy），其他同理。</br>
     * 例：new SimpleDateFormat("现在是yyyy年MM月dd日 HH(hh)时 mm分 ss秒 S毫秒 星期E 今年的第D天 这个月的第F星期 今年的第w个星期
     * 这个月的第W个星期 今天的a k1~24制时间 K0-11小时制时间 z时区"); <blockquote>
     * <table border=0 cellspacing=3 cellpadding=0 summary="Chart shows pattern letters, date/time
     * component, presentation, and examples.">
     * <tr bgcolor="#ccccff">
     * <th align=left>字母
     * <th align=left>日期或时间元素
     * <th align=left>表示
     * <th align=left>示例
     * <tr>
     * <td><code>G</code>
     * <td>Era designator
     * <td><a href="#text">Text</a>
     * <td><code>AD</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>y</code>
     * <td>年
     * <td><a href="#year">Year</a>
     * <td><code>1996</code>; <code>96</code>
     * <tr>
     * <td><code>Y</code>
     * <td>Week year
     * <td><a href="#year">Year</a>
     * <td><code>2009</code>; <code>09</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>M</code>
     * <td>年中的月份
     * <td><a href="#month">Month</a>
     * <td><code>July</code>; <code>Jul</code>; <code>07</code>
     * <tr>
     * <td><code>w</code>
     * <td>年中的周数
     * <td><a href="#number">Number</a>
     * <td><code>27</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>W</code>
     * <td>月份中的周数
     * <td><a href="#number">Number</a>
     * <td><code>2</code>
     * <tr>
     * <td><code>D</code>
     * <td>年中的天数
     * <td><a href="#number">Number</a>
     * <td><code>189</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>d</code>
     * <td>月份中的天数
     * <td><a href="#number">Number</a>
     * <td><code>10</code>
     * <tr>
     * <td><code>F</code>
     * <td>月份中的星期
     * <td><a href="#number">Number</a>
     * <td><code>2</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>E</code>
     * <td>星期中的天数
     * <td><a href="#text">Text</a>
     * <td><code>Tuesday</code>; <code>Tue</code>
     * <tr>
     * <td><code>u</code>
     * <td>星期中的天数 (1 = Monday, ..., 7 = Sunday)
     * <td><a href="#number">Number</a>
     * <td><code>1</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>a</code>
     * <td>am/pm 标记
     * <td><a href="#text">Text</a>
     * <td><code>PM</code>
     * <tr>
     * <td><code>H</code>
     * <td>一天中的小时数（0-23）
     * <td><a href="#number">Number</a>
     * <td><code>0</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>k</code>
     * <td>一天中的小时数（1-24）
     * <td><a href="#number">Number</a>
     * <td><code>24</code>
     * <tr>
     * <td><code>K</code>
     * <td>am/pm 中的小时数（0-11）
     * <td><a href="#number">Number</a>
     * <td><code>0</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>h</code>
     * <td>am/pm 中的小时数（1-12）
     * <td><a href="#number">Number</a>
     * <td><code>12</code>
     * <tr>
     * <td><code>m</code>
     * <td>小时中的分钟数
     * <td><a href="#number">Number</a>
     * <td><code>30</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>s</code>
     * <td>分钟中的秒数
     * <td><a href="#number">Number</a>
     * <td><code>55</code>
     * <tr>
     * <td><code>S</code>
     * <td>毫秒数
     * <td><a href="#number">Number</a>
     * <td><code>978</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>z</code>
     * <td>时区
     * <td><a href="#timezone">General time zone</a>
     * <td><code>Pacific Standard Time</code>; <code>PST</code>; <code>GMT-08:00</code>
     * <tr>
     * <td><code>Z</code>
     * <td>时区
     * <td><a href="#rfc822timezone">RFC 822 time zone</a>
     * <td><code>-0800</code>
     * <tr bgcolor="#eeeeff">
     * <td><code>X</code>
     * <td>时区
     * <td><a href="#iso8601timezone">ISO 8601 time zone</a>
     * <td><code>-08</code>; <code>-0800</code>; <code>-08:00</code>
     * </table>
     * </blockquote>
     *
     * @param time
     * @param pattern 就是上面参数的结合
     * @return
     */
    public static String getTime(long time, String pattern) {
        return getDataFormat(pattern).format(new Date(time));
    }

    /**
     * @param pattern yyyy-MM-dd HH:mm:ss:SSS
     */
    public static String getChinaTime(long time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        return sdf.format(new Date(time));
    }

    /**
     * @param pattern yyyy-MM-dd HH:mm:ss:SSS
     */
    public static String getIndianTime(long time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));
        // String cleanId = String.format(Locale.ROOT, "GMT%c%02d:%02d", '+', 5, 30);
        return sdf.format(new Date(time));
    }

    public static String getAreaTime() {
        int style = DateFormat.MEDIUM;
        // Also try with style = DateFormat.FULL and DateFormat.SHORT
        Date date = new Date(System.currentTimeMillis());
        DateFormat df = DateFormat.getDateInstance(style, Locale.UK);
        return df.format(date);
    }

    /**
     * amount/total, then format to percentage.
     */
    public static String percentage(long amount, long total) {
        return percentage(((double) amount) / total);
    }

    /**
     * 0..100/100.0, then format to percentage.
     * 
     * @param percentage 0..100
     */
    public static String percentage(int percentage) {
        return percentage(((double) percentage) / 100.0);
    }

    /**
     * 0.0~1.0, then format to percentage.
     * 
     * @param percentage 0.0~1.0
     */
    private static String percentage(double percentage) {
        /*
         * Notice: after 4.4, not use android.support.v4.text.BidiFormatter, use
         * android.text.BidiFormatter directly
         */
        android.support.v4.text.BidiFormatter bf = android.support.v4.text.BidiFormatter.getInstance();
        return bf.unicodeWrap(NumberFormat.getPercentInstance().format(percentage));
    }

}
