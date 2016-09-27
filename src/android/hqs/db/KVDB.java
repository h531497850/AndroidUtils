package android.hqs.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hqs.basic.BasicSQL;

/**
 * 基本键值对数据库
 * @author 胡青松
 */
public abstract class KVDB extends BasicSQL {
	
	/**存放字符串键值信息*/
	private static final String TABLE_STRING = "mString";
	/**存放整形键值信息*/
	private static final String TABLE_INT = "mInt";
	/**存放整形键值信息*/
	private static final String TABLE_BOOL = "mBool";
	
	private static final String KEY = "mKey";
	private static final String VALUE = "mValue";
	private static final String[] RESULT = new String[]{KEY, VALUE};
	
	private boolean isNewDB;
	/** 数据库是否在创建或升级 */
	public boolean isNewDB(){
		return isNewDB;
	}
	
	public KVDB(Context context, String sqlName, int version){  
        super(context, sqlName, version, null);  
    }
    
	/**
	 * 请在这里创建您的表单。
	 */
    @Override
    public void onCreate(SQLiteDatabase db) {
    	info("onCreate()", "VERSION = " + db.getVersion());
		isNewDB = true;
   	
    	create(db, TABLE_STRING, KEY + " nvarchar primary key, " + VALUE + " nvarchar");
    	create(db, TABLE_INT, KEY + " nvarchar primary key, " + VALUE + " integer");
    	create(db, TABLE_BOOL, KEY + " nvarchar primary key, " + VALUE + " char(5)");
    }
    
    /**
     * 请在这里升级您的表单。
     */
	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	info("onUpgrade()", "oldVersion = " + oldVersion + ", newVersion = " + newVersion);
		isNewDB = true;
   	
    	for (int j = oldVersion; j <= newVersion; j++) {
    		if (j > 1 && j > oldVersion) {
    			drop(db, TABLE_STRING);
    			drop(db, TABLE_INT);
    			drop(db, TABLE_BOOL);
            	onCreate(db);
			}
    	}
    }
    
	// ========================================================================================================
	// ============================= TODO 下面是公开的方法，注意：数据库已经创建完成 ================================
	// ========================================================================================================
    /**
     * 保存字符串数据<br>
     * <b>该操作比较耗时，最好不要在主线程调用该方法。</b>
     * @param key 
     * @param value
     * @return true 插入或更新数据成功；false 插入或更新数据失败
     */
	public final boolean setString(String key, String value) {
		Cursor cur = null;
		ContentValues cv = null;
		try {
			final SQLiteDatabase mydb = getWritableDatabase();
			cur = mydb.query(TABLE_STRING, RESULT, KEY + "=?", new String[]{key}, null, null, null);
			if (cur != null) {
				cv = new ContentValues();
				cv.put(KEY, key);
				cv.put(VALUE, value);
				if(cur.getCount() > 0) { 	// 如果已保存，更新
					mydb.update(TABLE_STRING, cv, KEY + "=?", new String[]{key});
				} else {					// 如果未保存，直接保存
					mydb.insert(TABLE_STRING, null, cv);
				}
				return true;
			}
		} catch (Exception e) {
			error("setString", "保存字符串失败！", e);
		} finally {
			if (cv != null) { // 保证能清除HashMap的数据
				cv.clear();
				cv = null;
			}
			if (cur != null && !cur.isClosed()) {
				cur.close();
			}
		}
		return false;
	}
	
	/**
	 * 根据键值获取数据库内的字符串数据<br>
     * <b>该操作比较耗时，最好不要在主线程调用该方法。</b>
	 * @param key
	 * @return 如果key存在，返回保存的字符串数据；否则，返回null。
	 */
	public final String getString(String key) {
		String value = null;
		Cursor cur = null;
		try {
			final SQLiteDatabase mydb = getReadableDatabase();
			cur = mydb.query(TABLE_STRING, RESULT, KEY + "=?", new String[]{key}, null, null, null);
			if (cur != null && cur.getCount() > 0) {
				cur.moveToFirst();
				value = cur.getString(1);
			}
		} catch (Exception e) {
			error("getString", "获取字符串失败！", e);
		} finally {
			if (cur != null && !cur.isClosed()) {
				cur.close();
			}
		}
		return value;
	}
	
	/**
	 * 保存整形数据<br>
     * <b>该操作比较耗时，最好不要在主线程调用该方法。</b>
	 * @param key
	 * @param value
	 * @return true 插入或更新数据成功；false 插入或更新数据失败
	 */
	public final boolean setInt(String key, int value) {
		Cursor cur = null;
		ContentValues cv = null;
		try {
			final SQLiteDatabase mydb = getWritableDatabase();
			cur = mydb.query(TABLE_INT, RESULT, KEY + "=?", new String[]{key}, null, null, null);
			if (cur != null) {
				cv = new ContentValues();
				cv.put(KEY, key);
				cv.put(VALUE, value);
				if(cur.getCount() > 0) {
					mydb.update(TABLE_INT, cv, KEY + "=?", new String[]{key});
				} else {
					mydb.insert(TABLE_INT, null, cv);
				}
				return true;
			}
		} catch (Exception e) {
			error("setInt", "保存整数失败！", e);
		} finally {
			if (cv != null) { // 保证能清除HashMap的数据
				cv.clear();
				cv = null;
			}
			if (cur != null && !cur.isClosed()) {
				cur.close();
			}
		}
		return false;
	}
	
	/**
	 * 根据键值获取数据库内的整形数据<br>
     * <b>该操作比较耗时，最好不要在主线程调用该方法。</b>
	 * @param key
	 * @return 如果key存在，返回保存的整形数据；否则，返回0。
	 */
	public final int getInt(String key) {
		int value = 0;
		Cursor cur = null;
		try {
			final SQLiteDatabase mydb = getReadableDatabase();
			cur = mydb.query(TABLE_INT, RESULT, KEY + "=?", new String[]{key}, null, null, null);
			if (cur != null && cur.getCount() > 0) {
				cur.moveToFirst();
				value = cur.getInt(1);
			}
		} catch (Exception e) {
			error("getString", "获取整数失败！", e);
		} finally {
			if (cur != null && !cur.isClosed()) {
				cur.close();
			}
		}
		return value;
	}
	/**
	 * 保存布尔变量<br>
     * <b>该操作比较耗时，最好不要在主线程调用该方法。</b>
	 * @param key
	 * @param value
	 * @return
	 */
	public final boolean setBool(String key, boolean value) {
		Cursor cur = null;
		ContentValues cv = null;
		try {
			final SQLiteDatabase mydb = getWritableDatabase();
			cur = mydb.query(TABLE_BOOL, RESULT, KEY + "=?", new String[]{key}, null, null, null);
			if (cur != null) {
				cv = new ContentValues();
				cv.put(KEY, key);
				cv.put(VALUE, String.valueOf(value));
				if(cur.getCount() > 0) {
					mydb.update(TABLE_BOOL, cv, KEY + "=?", new String[]{key});
				} else {
					mydb.insert(TABLE_BOOL, null, cv);
				}
				return true;
			}
		} catch (Exception e) {
			error("setBool", "保存布尔失败！", e);
		} finally {
			if (cv != null) { // 保证能清除HashMap的数据
				cv.clear();
				cv = null;
			}
			if (cur != null && !cur.isClosed()) {
				cur.close();
			}
		}
		return false;
	}
	/**
	 * 根据键值获取数据库内的布尔数据<br>
     * <b>该操作比较耗时，最好不要在主线程调用该方法。</b>
	 * @param key
	 * @return 如果key存在，返回保存的布尔值；否则，返回false。
	 */
	public final boolean getBool(String key) {
		String value = null;
		Cursor cur = null;
		try {
			final SQLiteDatabase mydb = getReadableDatabase();
			cur = mydb.query(TABLE_BOOL, RESULT, KEY + "=?", new String[]{key}, null, null, null);
			if (cur != null && cur.getCount() > 0) {
				cur.moveToFirst();
				value = cur.getString(1);
			}
		} catch (Exception e) {
			error("getBool", "获取布尔失败！", e);
		} finally {
			if (cur != null && !cur.isClosed()) {
				cur.close();
			}
		}
		return Boolean.valueOf(value);
	}
	
}