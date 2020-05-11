package android.hqs.basic;

import java.io.File;
import java.util.List;
import java.util.Set;

import com.vivo.android.tool.TextTool;
import com.vivo.android.util.LogUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * {@link #SQLiteOpenHelper}是一个辅助类，用来管理数据库的创建和版本他，它提供两个方面的功能 :
 * <ul>
 * <li>{@link #getReadableDatabase()}、{@link #getWritableDatabase()}可以获得
 * {@link #SQLiteDatabase}对象，通过该对象可以对数据库进行操作</li>
 * <li>提供了{@link #onCreate()}、{@link #onUpgrade()}两个回调函数，允许我们再创建和升级数据库时，进行自己的操作</li>
 * </ul>
 * 
 * {@link #onCreate(SQLiteDatabase)}第一次创建数据库调用；</br>
 * {@link #onUpgrade(SQLiteDatabase, int, int)}数据库升级调用
 * <p>
 * 
 * <ul>
 * 数据类型
 * <li>CHAR。CHAR存储定长数据很方便，CHAR字段上的索引效率级高，比如定义char(10)，那么不论你存储的数据是否达到了10个字节，
 * 都要占去10个字节的空间,不足的自动用空格填充。</li>
 * <li>VARCHAR。存储变长数据，但存储效率没有CHAR高。如果一个字段可能的值是不固定长度的，我们只知道它不可能超过10个字符，把它定义为
 * VARCHAR
 * (10)是最合算的。VARCHAR类型的实际长度是它的值的实际长度+1。为什么“+1”呢？这一个字节用于保存实际使用了多大的长度。从空间上考虑
 * ，用varchar合适；从效率上考虑，用char合适，关键是根据实际情况找到权衡点。</li>
 * <li>TEXT。text存储可变长度的非Unicode数据，最大长度为2^31-1(2,147,483,647)个字符。</li>
 * <li>NCHAR、NVARCHAR、NTEXT。这三种从名字上看比前面三种多了个“N”。它表示存储的是Unicode数据类型的字符。我们知道字符中，
 * 英文字符只需要一个字节存储就足够了
 * ，但汉字众多，需要两个字节存储，英文与汉字同时存在时容易造成混乱，Unicode字符集就是为了解决字符集这种不兼容的问题而产生的
 * ，它所有的字符都用两个字节表示
 * ，即英文字符也是用两个字节表示。nchar、nvarchar的长度是在1到4000之间。和char、varchar比较起来，nchar
 * 、nvarchar则最多存储4000个字符
 * ，不论是英文还是汉字；而char、varchar最多能存储8000个英文，4000个汉字。可以看出使用nchar、
 * nvarchar数据类型时不用担心输入的字符是英文还是汉字，较为方便，但在存储英文时数量上有些损失。</li>
 * <li>所以一般来说，如果含有中文字符，用nchar/nvarchar，如果纯英文和数字，用char/varchar。
 * 
 * SQLite最大的特点在于其数据类型为无数据类型(typelessness)。这意味着可以保存任何类型的数据到所想要保存的任何表的任何列中，
 * 无论这列声明的数据类型是什么
 * 。虽然在生成表结构的时候，要声明每个域的数据类型，但SQLite并不做任何检查。开发人员要靠自己的程序来控制输入与读出数据的类型
 * 。这里有一个例外，就是当主键为整型值时，如果要插入一个非整型值时会产生异常。
 * 
 * 虽然，SQLite允许忽略数据类型，但是，仍然建议在Create
 * Table语句中指定数据类型，因为数据类型有利于增强程序的可读性。另外，虽然在插入或读出数据的时候是不区分类型的，但在比较的时候，不同数据类型是有区别的</li>
 * </ul>
 * 
 * <ul>
 * <strong>由于数据库不能异步操作，该类中的方法都用到同步。synchronized当它用来修饰一个方法或者一个代码块的时候，
 * 能够保证在同一时刻最多只有一个线程执行该段代码。</strong>
 * <li>当两个并发线程访问同一个对象object中的这个synchronized(this)同步代码块时，一个时间内只能有一个线程得到执行。
 * 另一个线程必须等待当前线程执行完这个代码块以后才能执行该代码块。</li>
 * <li>然而，当一个线程访问object的一个synchronized(this)同步代码块时，另一个线程仍然可以访问该object中的非synchronized(this)同步代码块。</li>
 * <li>尤其关键的是，当一个线程访问object的一个synchronized(this)同步代码块时，其他线程对object中所有其它synchronized(this)同步代码块的访问将被阻塞。</li>
 * <li>当一个线程访问object的一个synchronized(this)同步代码块时，它就获得了这个object的对象锁。结果，其它线程对该object对象所有同步代码部分的访问都被暂时阻塞。</li>
 * </ul>
 * 
 * @author huqingsong
 * 
 */
public abstract class BasicSQL extends SQLiteOpenHelper {
	private static final String TAG = LogUtil.makeTag("BasicSQL");
	private final String sqlName;

	private final String Tag = LogUtil.makeTag(getClass());

	public static final class MapTable {
		/** m:map, k:key, v:value */
		public static final String TABLE_NAME = "mkv";

		/** data type INTEGER(int) */
		public static final String KEY_ID = "_id";
		/** data type text */
		public static final String KEY_NAME = "m_key";
		/** data type text */
		public static final String KEY_VALUE = "m_value";
		/** data type INTEGER(long) */
		public static final String KEY_ANALYSIS_DATE = "analysis_date";

		public static final String DEFAULT_SORT_ORDER = "analysis_date asc";

		public static final String CREATE_SQL = TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_NAME + " TEXT," + KEY_VALUE + " TEXT," + KEY_ANALYSIS_DATE + " INTEGER);";

		/** Query the SQL statement for each column of a row */
		// public static final String QUERY_SQL = "SELECT * FROM " + TABLE_NAME
		// + " WHERE " + KEY_ID + "=? AND " + KEY_NAME
		// + "=? AND " + KEY_VALUE + "=? AND " + KEY_ANALYSIS_DATE + "=?";
	}

	private boolean close = true;

	private boolean debug = false;

	protected Context context;

	/**
	 * @param context
	 *            上下文对象 不能为空
	 * @param sqlName
	 *            数据库名称 不能为空
	 * @param clazz
	 *            类名，用于生成打印日志的标签，不能为空
	 */
	public BasicSQL(Context context, String sqlName) {
		this(context, sqlName, 1);
	}

	/**
	 * @param context
	 *            上下文对象 不能为空
	 * @param sqlName
	 *            数据库名称 不能为空
	 * @param clazz
	 *            类名，用于生成打印日志的标签，不能为空
	 * @param version
	 *            数据库版本号
	 */
	public BasicSQL(Context context, String sqlName, int version) {
		this(context, sqlName, version, null);
	}

	/**
	 * 在{@link #SQLiteOpenHelper}的子类当中，必须有该构造函数
	 * 
	 * @param context
	 *            上下文对象 不能为空
	 * @param sqlName
	 *            数据库名称 不能为空
	 * @param clazz
	 *            类名，用于生成打印日志的标签，不能为空
	 * @param version
	 *            当前数据库的版本，值必须是整数并且是递增的状态
	 */
	public BasicSQL(Context context, String sqlName, int version, CursorFactory factory) {
		// 必须通过super调用父类当中的构造函数
		super(context, sqlName, factory, version);
		if (context == null) {
			throw new NullPointerException("context is null.");
		}
		this.context = context;
		this.sqlName = sqlName;
	}

	// =================================================================
	// ======= TODO These methods are visible to the subclass ==========
	// =================================================================

	protected void createBasicTable(SQLiteDatabase db) {
		create(db, MapTable.CREATE_SQL);
	}

	protected void dropBasicTable(SQLiteDatabase db) {
		drop(db, MapTable.TABLE_NAME);
	}

	/**
	 * 为表添加列
	 * 
	 * @param column
	 *            注意：<b>仅一列</b>
	 */
	protected void addColumn(SQLiteDatabase db, String table, String column) {
		try {
			db.execSQL("ALTER TABLE " + table + " ADD COLUMN " + column);
		} catch (SQLException e) {
			if (debug) {
				Log.d(TAG, "Add column failed.", e);
			}
		}
	}

	public int bulkInsert(String table, List<ContentValues> values) {
		info("bulkInsert table = " + table);
		if (values == null || values.size() <= 0) {
			return 0;
		}
		int numValues = values.size();
		SQLiteDatabase db = getWritableDatabase();
		try {
			info("Start insert to " + table + " table " + numValues + " data.");
			db.beginTransaction();
			for (ContentValues cv : values) {
				db.insert(table, null, cv);
			}
			db.setTransactionSuccessful();
			info("Insert to " + table + " table " + numValues + " data success.");
			return numValues;
		} catch (Exception e) {
			if (debug) {
				Log.w(TAG, "Bulk insert ", e);
			}
			return -1;
		} finally {
			db.endTransaction();
			if (close) {
				db.close();
			}
		}
	}

	/**
	 * 创建表的方法，如果没有该表，请创建。
	 * 
	 * @param tableSQL
	 *            表名 + 列数据
	 */
	protected void create(SQLiteDatabase db, String tableSQL) {
		try {
			db.execSQL("CREATE TABLE IF NOT EXISTS " + tableSQL);
		} catch (SQLException e) {
			if (debug) {
				Log.d(TAG, "Create table failed.", e);
			}
		}
	}

	/**
	 * 删除表的方法。在数据（比如表结构等）有更新时，将以前老版的数据取出， 然后删除老版的各个表，然后重新创建表， 最后把以前的数据重新加入新表下。
	 * 如果该表存在，删除。
	 * 
	 * @param table
	 *            表名
	 */
	protected void drop(SQLiteDatabase db, String table) {
		try {
			db.execSQL("DROP TABLE IF EXISTS " + table);
		} catch (SQLException e) {
			if (debug) {
				Log.d(TAG, "Drop table failed.", e);
			}
		}
	}

	// =================================================================
	// ======================= TODO public method ======================
	// =================================================================
	/**
	 * <b>This operation is time-consuming and it is better not to call this
	 * method on the primary thread.</b>
	 */
	public int delete(String table) {
		return delete(table, null, null);
	}

	/**
	 * <b>This operation is time-consuming and it is better not to call this
	 * method on the primary thread.</b>
	 */
	public void delete(Set<String> tables) {
		SQLiteDatabase db = null;
		try {
			db = getWritableDatabase();
			db.beginTransaction();
			for (String table : tables) {
				db.delete(table, null, null);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			if (debug) {
				Log.w(TAG, "Bulk delete tablbs failed.", e);
			}
		} finally {
			if (db != null) {
				db.endTransaction();
				if (close) {
					db.close();
				}
			}
		}
	}

	/**
	 * <b>This operation is time-consuming and it is better not to call this
	 * method on the primary thread.</b>
	 */
	public int delete(String table, String whereClause, String[] whereArgs) {
		SQLiteDatabase db = null;
		try {
			db = getWritableDatabase();
			return db.delete(table, whereClause, whereArgs);
		} catch (Exception e) {
			if (debug) {
				Log.w(TAG, "Delete data failed.", e);
			}
			return -1;
		} finally {
			if (close && db != null) {
				db.close();
			}
		}
	}

	/** Gets the class name of the instance */
	public String getClsName() {
		return getClass().getSimpleName();
	}

	public File getDatabaseFile() {
		return context.getDatabasePath(sqlName);
	}

	public String getDatabasePath() {
		return getDatabaseFile().getAbsolutePath();
	}

	/** Gets the amount of data saved in the current database. */
	public long getDatabaseSize() {
		return getDatabaseFile().length();
	}

	public boolean isDatabaseExists() {
		return getDatabaseFile().exists();
	}

	public long insert(String table, ContentValues values) {
		return insert(table, null, values);
	}

	public long insert(String table, String nullColumnHack, ContentValues values) {
		SQLiteDatabase db = null;
		try {
			db = getWritableDatabase();
			return db.insertWithOnConflict(table, nullColumnHack, values, SQLiteDatabase.CONFLICT_NONE);
		} catch (SQLException e) {
			if (debug) {
				Log.w(TAG, "Error inserting " + values, e);
			}
			return -1;
		} finally {
			if (close && db != null) {
				db.close();
			}
		}
	}

	public Cursor query(String table, String[] columns) {
		return query(table, columns, null, null, null, null, null);
	}

	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs) {
		return query(table, columns, selection, selectionArgs, null, null, null);
	}

	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String orderBy) {
		return query(table, columns, selection, selectionArgs, null, null, orderBy);
	}

	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy) {
		SQLiteDatabase db = null;
		try {
			db = getWritableDatabase();
			return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		} catch (Exception e) {
			if (debug) {
				Log.w(TAG, "Query data failed.", e);
			}
			return null;
		} finally {
			if (close && db != null) {
				db.close();
			}
		}
	}

	/**
	 * Update row<br>
	 * <b>This operation is time-consuming and it is better not to call this
	 * method on the primary thread.</b>
	 * 
	 * @param whereArgs
	 *            Columns to update
	 * @param values
	 *            Update to this value
	 * @param whereClause
	 *            = to update the column identifier + "=?"
	 */
	public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
		SQLiteDatabase db = null;
		try {
			db = getWritableDatabase();
			return db.updateWithOnConflict(table, values, whereClause, whereArgs, SQLiteDatabase.CONFLICT_NONE);
		} catch (Exception e) {
			if (debug) {
				Log.w(TAG, "Update data failed.", e);
			}
			return -1;
		} finally {
			if (close && db != null) {
				db.close();
			}
		}
	}

	/**
	 * TODO Save data to {@link MapTable}.
	 * <p>
	 * <b>This operation is time-consuming, it is best not to call the method in
	 * the main thread.</b>
	 * 
	 * @param name
	 *            Key, can not be empty.
	 * @param value
	 *            To save the data, no matter what type of data, first converted
	 *            to a string, and converted to the required type when take it
	 *            out of the database.
	 */
	public boolean setValue(String name, String value) {
		if (TextTool.isEmpty(name)) {
			return false;
		}
		SQLiteDatabase db = null;
		Cursor cur = null;
		try {
			String[] columns = { MapTable.KEY_NAME };
			String[] selectionArgs = { name };
			db = getWritableDatabase();
			cur = db.query(MapTable.TABLE_NAME, columns, MapTable.KEY_NAME + "=?", selectionArgs, null, null, null);
			ContentValues cv = new ContentValues();
			cv.put(MapTable.KEY_NAME, name);
			cv.put(MapTable.KEY_VALUE, value);
			cv.put(MapTable.KEY_ANALYSIS_DATE, System.currentTimeMillis());
			long rowId;
			if (cur != null && cur.getCount() > 0) {
				// If saved, update
				rowId = db.update(MapTable.TABLE_NAME, cv, MapTable.KEY_NAME + "=?", selectionArgs);
			} else {
				// If not saved, save directly
				rowId = db.insert(MapTable.TABLE_NAME, null, cv);
			}
			return rowId > -1;
		} catch (Exception e) {
			if (debug) {
				Log.w(TAG, "Save data failed.", e);
			}
			return false;
		} finally {
			if (cur != null && !cur.isClosed()) {
				cur.close();
			}
			if (close && db != null) {
				db.close();
			}
		}
	}

	/**
	 * Value added 1
	 */
	public boolean setValue(String name) {
		if (TextTool.isEmpty(name)) {
			return false;
		}
		SQLiteDatabase db = null;
		Cursor cur = null;
		try {
			String[] columns = { MapTable.KEY_VALUE };
			String[] selectionArgs = { name };
			db = getWritableDatabase();
			cur = db.query(MapTable.TABLE_NAME, columns, MapTable.KEY_NAME + "=?", selectionArgs, null, null, null);
			ContentValues cv = new ContentValues();
			cv.put(MapTable.KEY_NAME, name);
			cv.put(MapTable.KEY_ANALYSIS_DATE, System.currentTimeMillis());
			long rowId;
			if (cur != null && cur.getCount() > 0 && cur.moveToFirst()) {
				// If saved, update
				cv.put(MapTable.KEY_VALUE, cur.getInt(cur.getColumnIndex(MapTable.KEY_VALUE)) + 1);
				rowId = db.update(MapTable.TABLE_NAME, cv, MapTable.KEY_NAME + "=?", selectionArgs);
			} else {
				// If not saved, save directly
				cv.put(MapTable.KEY_VALUE, 1);
				rowId = db.insert(MapTable.TABLE_NAME, null, cv);
			}
			return rowId > -1;
		} catch (Exception e) {
			if (debug) {
				Log.w(TAG, "Save data failed.", e);
			}
			return false;
		} finally {
			if (cur != null && !cur.isClosed()) {
				cur.close();
			}
			if (close && db != null) {
				db.close();
			}
		}
	}

	/**
	 * TODO Get string data in {@link MapTable} based on key.
	 * <p>
	 * <b>This operation is time-consuming, it is best not to call the method in
	 * the main thread.</b>
	 * 
	 * @param name
	 * @return If key exists, returns the saved string data; otherwise, returns
	 *         defValue.
	 */
	public String getString(String name, String defValue) {
		if (TextTool.isEmpty(name)) {
			return defValue;
		}
		SQLiteDatabase db = null;
		Cursor cur = null;
		try {
			// 返回列
			String[] columns = { MapTable.KEY_VALUE };
			String[] selectionArgs = { name };
			db = getReadableDatabase();
			cur = db.query(MapTable.TABLE_NAME, columns, MapTable.KEY_NAME + "=?", selectionArgs, null, null, null);
			// Cursor initialization position is not necessarily 0
			if (cur != null && cur.getCount() > 0 && cur.moveToFirst()) {
				return cur.getString(0);
			}
		} catch (Exception e) {
			if (debug) {
				Log.w(TAG, "Failed to get data.", e);
			}
		} finally {
			if (cur != null && !cur.isClosed()) {
				cur.close();
			}
			if (close && db != null) {
				db.close();
			}
		}
		return defValue;
	}

	public int getInt(String name, int defValue) {
		if (TextTool.isEmpty(name)) {
			return defValue;
		}
		SQLiteDatabase db = null;
		Cursor cur = null;
		try {
			// 返回列
			String[] columns = { MapTable.KEY_VALUE };
			String[] selectionArgs = { name };
			db = getReadableDatabase();
			cur = db.query(MapTable.TABLE_NAME, columns, MapTable.KEY_NAME + "=?", selectionArgs, null, null, null);
			// Cursor initialization position is not necessarily 0
			if (cur != null && cur.getCount() > 0 && cur.moveToFirst()) {
				return cur.getInt(0);
			}
		} catch (Exception e) {
			if (debug) {
				Log.w(TAG, "Failed to get data.", e);
			}
		} finally {
			if (cur != null && !cur.isClosed()) {
				cur.close();
			}
			if (close && db != null) {
				db.close();
			}
		}
		return defValue;
	}

	public long getLong(String name, long defValue) {
		if (TextTool.isEmpty(name)) {
			return defValue;
		}
		SQLiteDatabase db = null;
		Cursor cur = null;
		try {
			// 返回列
			String[] columns = { MapTable.KEY_VALUE };
			String[] selectionArgs = { name };
			db = getReadableDatabase();
			cur = db.query(MapTable.TABLE_NAME, columns, MapTable.KEY_NAME + "=?", selectionArgs, null, null, null);
			// Cursor initialization position is not necessarily 0
			if (cur != null && cur.getCount() > 0 && cur.moveToFirst()) {
				return cur.getLong(0);
			}
		} catch (Exception e) {
			if (debug) {
				Log.w(TAG, "Failed to get data.", e);
			}
		} finally {
			if (cur != null && !cur.isClosed()) {
				cur.close();
			}
			if (close && db != null) {
				db.close();
			}
		}
		return defValue;
	}

	/**
	 * 获取该条数据上一次保存的时间，默认返回0
	 */
	public long getValueDate(String name) {
		if (TextTool.isEmpty(name)) {
			return 0;
		}
		SQLiteDatabase db = null;
		Cursor cur = null;
		try {
			String[] columns = { MapTable.KEY_ANALYSIS_DATE };
			String[] selectionArgs = { name };
			db = getReadableDatabase();
			cur = db.query(MapTable.TABLE_NAME, columns, MapTable.KEY_NAME + "=?", selectionArgs, null, null, null);
			// Cursor initialization position is not necessarily 0
			if (cur != null && cur.getCount() > 0 && cur.moveToFirst()) {
				return cur.getLong(0);
			}
		} catch (Exception e) {
			if (debug) {
				Log.w(TAG, "Failed to get data.", e);
			}
		} finally {
			if (cur != null && !cur.isClosed()) {
				cur.close();
			}
			if (close && db != null) {
				db.close();
			}
		}
		return 0;
	}

	/**
	 * @param max
	 *            How many data can be stored maximum?
	 * @param limit
	 *            How many data can be left when deleting?
	 * @return Whether to delete successfully or not to maximum limit
	 */
	public boolean delete(String table, String dateColumn, String order, int max, int limit) {
		SQLiteDatabase db = null;
		Cursor cur = null;
		try {
			String[] columns = { dateColumn };
			db = getReadableDatabase();
			cur = db.query(table, columns, null, null, null, null, order);
			if (cur == null) {
				return true;
			}
			int count = cur.getCount();
			info("mkv has " + count + " datas.");
			// Number of data entries to delete
			if (count - max <= 0) {
				return true;
			}
			int delete = count - limit;
			info("mkv to delete the number of entries: " + delete);
			// Move to the position of the next data that reaches the
			// limit, and then get the time for this.
			cur.moveToPosition(delete);
			long limitTime = cur.getLong(cur.getColumnIndex(dateColumn));
			// Delete data before this time
			String where = dateColumn + "<?";
			String[] whereArgs = new String[] { String.valueOf(limitTime) };
			db.delete(table, where, whereArgs);
			info("mkv to delete data success.");
			return true;
		} catch (Exception e) {
			if (debug) {
				Log.w(TAG, "Delete map table data failed.", e);
			}
			return false;
		} finally {
			if (cur != null && !cur.isClosed()) {
				cur.close();
			}
			if (close && db != null) {
				db.close();
			}
		}
	}

	/**
	 * @param max
	 *            How many data can be stored maximum?
	 * @param limit
	 *            How many data can be left when deleting?
	 * @return Whether to delete successfully or not to maximum limit
	 */
	public boolean delete(int max, int limit) {
		return delete(MapTable.TABLE_NAME, MapTable.KEY_ANALYSIS_DATE, MapTable.DEFAULT_SORT_ORDER, max, limit);
	}

	/**
	 * @return Is the database closed after use? Default close.
	 */
	public boolean isClose() {
		return close;
	}

	/**
	 * @param close
	 *            Do you want to close the database? Multi process,
	 *            multi-threaded, do not close the database.Default close.
	 */
	protected void setClose(boolean close) {
		this.close = close;
	}

	/**
	 * @param debug
	 *            Do you want to print error stack info? Default not print.
	 */
	protected void setDebug(boolean debug) {
		this.debug = debug;
	}

	// =================================================================
	// ====================== TODO print log method ====================
	// =================================================================
    /** Blue, debug information */
    protected final void debug(Object obj) {
        LogUtil.debug(Tag, obj);
    }

    protected final void debug(Throwable tr) {
        LogUtil.debug(Tag, tr);
    }

    protected final void debug(Object obj, Throwable tr) {
        LogUtil.debug(Tag, obj, tr);
    }

	/** Green, normal information */
	protected final void info(Object obj) {
		LogUtil.info(Tag, obj);
	}

	protected final void info(Object obj, Throwable tr) {
		LogUtil.info(Tag, obj, tr);
	}

	/** Black, long message */
	protected final void verbose(Object obj) {
		LogUtil.verbose(Tag, obj);
	}

	protected final void verbose(Object obj, Throwable tr) {
		LogUtil.verbose(Tag, obj, tr);
	}

	/** Red, error message */
	protected final void error(Object obj) {
		LogUtil.error(Tag, obj);
	}

	protected final void error(Object obj, Throwable tr) {
		LogUtil.error(Tag, obj, tr);
	}

}