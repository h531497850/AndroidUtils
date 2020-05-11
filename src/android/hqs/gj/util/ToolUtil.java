package android.hqs.gj.util;

import java.util.Calendar;
import java.util.Locale;

public class ToolUtil {

	public static String getLanguage() {
		return Locale.getDefault().getLanguage();
	}

	/** 获取当天凌晨的日期 */
	public static Calendar getMidNeightCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}

	/** 设置并获取当天，该时刻所代表的日期 */
	public static Calendar getCalendar(int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}

	/** 设置并获取当天，该时刻所代表的日期 */
	public static Calendar getCalendar(int hour, int minute, int second, int milliSecond) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, milliSecond);
		return calendar;
	}

	/** 设置并获取（当月）（要设置的时间）的日期 */
	public static Calendar getCalendar(int day, int hour, int minute, int second, int milliSecond) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		setCalendar(calendar, year, month, day, hour, minute, second, milliSecond);
		return calendar;
	}

	public static Calendar getCalendar(int year, int month, int day, int hour, int minute, int second,
			int milliSecond) {
		Calendar calendar = Calendar.getInstance();
		setCalendar(calendar, year, month, day, hour, minute, second, milliSecond);
		return calendar;
	}

	public static void setCalendar(Calendar calendar, int year, int month, int day, int hour, int minute, int second,
			int milliSecond) {
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, milliSecond);
	}

	public static long getMidNeightTime() {
		return getMidNeightCalendar().getTimeInMillis();
	}

	/** 设置并获取当天，该时刻所代表的时间(单位：毫秒) */
	public static long getTime(int hour, int minute) {
		return getCalendar(hour, minute).getTimeInMillis();
	}

	/** 设置并获取该日期所代表的时间(单位：毫秒) */
	public static long getTime(int hour, int minute, int second, int milliSecond) {
		return getCalendar(hour, minute, second, milliSecond).getTimeInMillis();
	}

	/** 设置并获取（当月）（要设置的时间）的日期代表的时间(单位：毫秒) */
	public static long getTime(int day, int hour, int minute, int second, int milliSecond) {
		return getCalendar(day, hour, minute, second, milliSecond).getTimeInMillis();
	}

}
