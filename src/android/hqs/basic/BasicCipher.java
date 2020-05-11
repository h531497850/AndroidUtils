package android.hqs.basic;

import java.io.File;
import java.util.List;
import java.util.Set;

import com.vivo.android.tool.TextTool;
import com.vivo.android.util.LogUtil;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabase.CursorFactory;
import net.sqlcipher.database.SQLiteOpenHelper;

/**
 * Cryptographic database base class
 * 
 * @author huqingsong
 */
public abstract class BasicCipher extends SQLiteOpenHelper {
	private static final String TAG = LogUtil.makeTag("BasicCipher");
	private final String sqlName;

	private final String Tag = LogUtil.makeTag(getClass());

	/**
	 * Subclass implementation, the same name method can be passed through the
	 * password.
	 */
	public abstract SQLiteDatabase getReadableDatabase();

	/**
	 * Subclass implementation, the same name method can be passed through the
	 * password.
	 * <p>
	 * Note that if the database does not exist, it will be created directly, so
	 * you should call this method carefully when the database does not exist
	 * and does not want to create the database.
	 */
	public abstract SQLiteDatabase getWritableDatabase();

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
	public BasicCipher(Context context, String sqlName) {
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
	public BasicCipher(Context context, String sqlName, int version) {
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
	public BasicCipher(Context context, String sqlName, int version, CursorFactory factory) {
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

	protected void resetBaseTable(SQLiteDatabase db) {
		drop(db, MapTable.TABLE_NAME);
		create(db, MapTable.CREATE_SQL);
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
	 * @return
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