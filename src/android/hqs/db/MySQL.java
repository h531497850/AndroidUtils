package android.hqs.db;

import android.net.Uri;

/**
 * 存储类 描述</br>
 * NULL 值是一个 NULL 值。</br>
 * INTEGER 值是一个带符号的整数，根据值的大小存储在 1、2、3、4、6 或 8 字节中。</br>
 * REAL 值是一个浮点值，存储为 8 字节的 IEEE 浮点数字。</br>
 * TEXT 值是一个文本字符串，使用数据库编码（UTF-8、UTF-16BE 或 UTF-16LE）存储。</br>
 * BLOB 值是一个 blob 数据，完全根据它的输入存储。
 * <p>
 * SQLite 的存储类稍微比数据类型更普遍。INTEGER 存储类，包含 6 种不同的不同长度的整数数据类型。</br>
 * 
 * @author 胡青松
 */
public class MySQL {
	
	public static final String AUTHORITY = "你自己随便定义，但必须确保其唯一性，所以一般是模块名+后缀";
	
	// ========================================================================================================
	// ======================== TODO Here are some of the parameter information ===============================
	// ========================================================================================================
	public interface Key {
		public static final String NAME = "name";
	}
	
	public interface Param {
		public static final String AGE = "age";
	}

	// ========================================================================================================
	// ========================= TODO The following is the table structure information ========================
	// ========================================================================================================
	public interface AppTable {
		/**
		 * "content://" Protocol, the standard prefix, used to indicate that a
		 * ContentProvider control of the data can not be changed
		 */
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/aaa");
		public static final String TABLE_NAME = "aaa";
		/**data type INTEGER(int)*/
		public static final String KEY_ID = "_id";
		/**data type varchar[20]*/
		public static final String KEY_EVENT_ID = "event_id";
		/**data type varchar[20]*/
		public static final String KEY_EVENT_LABEL = "event_label";
		/**data type INTEGER DEFAULT 0(long)*/
		public static final String KEY_STIME = "start_time";
		/**data type INTEGER DEFAULT 0(long)*/
		public static final String KEY_ETIME = "end_time";
		/**data type INTEGER DEFAULT 0(long)*/
		public static final String KEY_DURATION = "duration";
		/**data type INTEGER DEFAULT 0(int)*/
		public static final String KEY_LUNCH_COUNT = "lunchcount";
		/**data type varchar[50]*/
		public static final String KEY_EVENT_VALUE = "event_value";
		/**data type INTEGER(long)*/
		public static final String KEY_ANALYSIS_DATE = "analysisdate";

		public static final String DEFAULT_SORT_ORDER = "analysisdate DESC";
		
		public static final String CREATE_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_EVENT_ID + " varchar[20],"
				+ KEY_EVENT_LABEL + " varchar[20],"
				+ KEY_STIME + " INTEGER DEFAULT 0,"
				+ KEY_ETIME + " INTEGER DEFAULT 0,"
				+ KEY_DURATION + " INTEGER DEFAULT 0,"
				+ KEY_LUNCH_COUNT + " INTEGER DEFAULT 0,"
				+ KEY_EVENT_VALUE + " varchar[50],"
				+ KEY_ANALYSIS_DATE + " INTEGER" + ");";

		/**Query the SQL statement for each column of a row*/
		public static final String SQL = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = ? AND " + KEY_EVENT_ID
				+ " = ? AND " + KEY_EVENT_LABEL + " = ? AND " + KEY_STIME + " = ? AND " + KEY_ETIME + " = ? AND "
				+ KEY_DURATION + " = ? AND " + KEY_LUNCH_COUNT + " = ? AND " + KEY_EVENT_VALUE + " = ? AND "
				+ KEY_ANALYSIS_DATE + " = ?";
	}

	/**
	 * abandoned.
	 * @author 胡青松
	 */
	public interface AppParamTable {
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/aap");
		public static final String TABLE_NAME = "aap";
		public static final String KEY_ID = "_id";
		public static final String KEY_APP_ID = "app_id";
		public static final String KEY_PARAM_NAME = "param_name";
		public static final String KEY_PARAM_VALUE = "param_value";
		public static final String KEY_ANALYSIS_DATE = "analysisdate";

		public static final String DEFAULT_SORT_ORDER = "analysisdate DESC";
		
		public static final String SQL = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = ? AND " + KEY_APP_ID
				+ " = ? AND " + KEY_PARAM_NAME + " = ? AND " + KEY_PARAM_VALUE + " = ? AND " + KEY_ANALYSIS_DATE + " = ?";
	}

	public interface TempTable {
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/ttt");
		public static final String TABLE_NAME = "ttt";
		/**data type INTEGER(int)*/
		public static final String KEY_ID = "_id";
		/**data type varchar[20]*/
		public static final String KEY_EVENT_ID = "event_id";
		/**data type INTEGER DEFAULT 0(long)*/
		public static final String KEY_STIME = "start";
		/**data type INTEGER DEFAULT -1(int)*/
		public static final String KEY_FLAG = "flag";

		public static final String DEFAULT_SORT_ORDER = "_id ASC";
		
		public static final String CREATE_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_EVENT_ID + " varchar[20],"
				+ KEY_STIME + " INTEGER DEFAULT 0,"
				+ KEY_FLAG + " INTEGER DEFAULT -1" + ");";

		/**Query the SQL statement for each column of a row*/
		public static final String SQL = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = ? AND " + KEY_EVENT_ID
				+ " = ? AND " + KEY_STIME + " = ? AND " + KEY_FLAG + " = ?";
	}

	public interface SettingTable {
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/sss");
		public static final String TABLE_NAME = "sss";
		/**data type INTEGER(int)*/
		public static final String KEY_ID = "_id";
		/**data type varchar[20]*/
		public static final String KEY_EVENT_ID = "event_id";
		/**data type varchar[20]*/
		public static final String KEY_PARAM_NAME = "param_name";
		/**data type INTEGER(int)*/
		public static final String KEY_PARAM_VALUE = "param_value";
		/**data type INTEGER(long)*/
		public static final String KEY_ANALYSIS_DATE = "analysisdate";

		public static final String DEFAULT_SORT_ORDER = "analysisdate DESC";
		
		public static final String CREATE_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_EVENT_ID + " varchar[20],"
				+ KEY_PARAM_NAME + " varchar[20],"
				+ KEY_PARAM_VALUE + " INTEGER,"
				+ KEY_ANALYSIS_DATE + " INTEGER" + ");";

		/**Query the SQL statement for each column of a row*/
		public static final String SQL = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = ? AND " + KEY_EVENT_ID
				+ " = ? AND " + KEY_PARAM_NAME + " = ? AND " + KEY_PARAM_VALUE + " = ? AND " + KEY_ANALYSIS_DATE
				+ " = ?";
	}

	public interface BatteryCpuInfoTable {
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/bbb");
		public final static String TABLE_NAME = "bbb";
		/**data type INTEGER(int)*/
		public static final String KEY_ID = "_id";
		/**data type varchar[50]*/
		public static final String KEY_NAME = "name";
		/**data type INTEGER DEFAULT 0(long)*/
		public static final String KEY_CPU_TIME = "cpuTime";
		/**data type INTEGER DEFAULT 0(long)*/
		public static final String KEY_CPU_FG_TIME = "cpuFgTime";
		/**data type INTEGER DEFAULT 0(long)*/
		public static final String KEY_GPS_TIME = "gpsTime";
		/**data type INTEGER DEFAULT 0(int)*/
		public static final String KEY_TCP_BYTES_RECEIVED = "tcpBytesReceived";
		/**data type INTEGER DEFAULT 0(int)*/
		public static final String KEY_TCP_BYTES_SENT = "tcpBytesSent";
		/**data type INTEGER DEFAULT 0(long)*/
		public static final String KEY_WAKE_LOCK_TIME = "wakeLockTime";
		/**data type INTEGER DEFAULT 0(long)*/
		public static final String KEY_WIFI_RUNNING_TIME = "wifiRunningTime";
		/**data type INTEGER DEFAULT 0(int)*/
		public static final String KEY_POWER_VALUE = "powervalue";
		/**data type INTEGER DEFAULT 0(int)*/
		public static final String KEY_PERCENT = "percent";
		/**data type INTEGER(long)*/
		public static final String KEY_ANALYSIS_DATE = "analysisdate";

		public static final String DEFAULT_SORT_ORDER = "percent DESC";
		
		public static final String CREATE_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_NAME + " varchar[50],"
				+ KEY_CPU_TIME + " INTEGER DEFAULT 0,"
				+ KEY_CPU_FG_TIME + " INTEGER DEFAULT 0,"
				+ KEY_GPS_TIME + " INTEGER DEFAULT 0,"
				+ KEY_TCP_BYTES_RECEIVED + " INTEGER DEFAULT 0,"
				+ KEY_TCP_BYTES_SENT + " INTEGER DEFAULT 0,"
				+ KEY_WAKE_LOCK_TIME + " INTEGER DEFAULT 0,"
				+ KEY_WIFI_RUNNING_TIME + " INTEGER DEFAULT 0,"
				+ KEY_POWER_VALUE + " INTEGER DEFAULT 0,"
				+ KEY_PERCENT	 + " INTEGER DEFAULT 0,"
				+ KEY_ANALYSIS_DATE + " INTEGER" + ");";

		/**Query the SQL statement for each column of a row*/
		public static final String SQL = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = ? AND " + KEY_NAME
				+ " = ? AND " + KEY_CPU_TIME + " = ? AND " + KEY_CPU_FG_TIME + " = ? AND " + KEY_GPS_TIME
				+ " = ? AND " + KEY_TCP_BYTES_RECEIVED + " = ? AND " + KEY_TCP_BYTES_SENT + " = ? AND " + KEY_WAKE_LOCK_TIME
				+ " = ? AND " + KEY_WIFI_RUNNING_TIME + " = ? AND " + KEY_POWER_VALUE + " = ? AND " + KEY_PERCENT
				+ " = ? AND " + KEY_ANALYSIS_DATE + " = ?";
	}

	public interface AppTimeRecordTable {
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/atr");
		public static final String TABLE_NAME = "atr";
		/**data type INTEGER(int)*/
		public static final String KEY_ID = "_id";
		/**data type varchar[50]*/
		public static final String KEY_PACKAGE_NAME = "p_n";
		/**data type INTEGER(long)*/
		public static final String KEY_RESUME_TIME = "r_t";
		/**data type INTEGER(long)*/
		public static final String KEY_ELAPSE_TIME = "e_t";
		// The following 3 keys are not used
		public static final String KEY_TIME_DURATION = "t_d";
		public static final String KEY_PACKAGE_TITLE = "p_t";
		public static final String KEY_PACKAGE_VERSION = "p_v";
		
		public static final String CREATE_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_PACKAGE_NAME + " varchar[50],"
				+ KEY_RESUME_TIME + " INTEGER,"
				+ KEY_ELAPSE_TIME + " INTEGER" + ");";

		/**Query the SQL statement for each column of a row*/
		public static final String SQL = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = ? AND "
				+ KEY_PACKAGE_NAME + " = ? AND " + KEY_RESUME_TIME + " = ? AND " + KEY_ELAPSE_TIME + " = ?";
	}

	public interface EventIdControlTable {
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/eic");
		public static final String TABLE_NAME = "eic";
		/**data type INTEGER(int)*/
		public static final String KEY_ID = "_id";
		/**data type varchar[20]*/
		public static final String KEY_EVENT_ID = "event_id";
		/**data type varchar[20]*/
		public static final String KEY_EVENT_STATUS = "event_status";
		
		public static final String CREATE_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_EVENT_ID + " varchar[20],"
				+ KEY_EVENT_STATUS + " varchar[20]" + ");";

		/**Query the SQL statement for each column of a row*/
		public static final String SQL = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = ? AND " + KEY_EVENT_ID
				+ " = ? AND " + KEY_EVENT_STATUS + " = ?";
	}

	public interface MapTable {
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/mkv");
		// m:map, k:key, v:value
		public static final String TABLE_NAME = "mkv";

		/**data type nvarchar*/
		public static final String KEY_ID = "_id";
		/**data type text*/
		public static final String KEY_VALUE = "params";
		
		public static final String CREATE_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
				+ KEY_ID + " nvarchar PRIMARY KEY, "
				+ KEY_VALUE + " TEXT" + ");";

		/**Query the SQL statement for each column of a row*/
		public static final String SQL = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = ? AND " + KEY_VALUE + " = ?";
		
		public static final String[] RESULT = new String[] { KEY_ID, KEY_VALUE };
	}

}
