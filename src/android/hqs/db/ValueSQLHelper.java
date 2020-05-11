package android.hqs.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hqs.gj.tool.TextTool;

/**
 * 基本键值对数据库
 * @author 胡青松
 */
public final class ValueSQLHelper extends BaseSQLHelper {
	
	/**存放字符串键值信息*/
	private static final String TABLE_STRING = "b_zfc";
	/**存放整形键值信息*/
	private static final String TABLE_INT = "b_zx";
	/**存放整形键值信息*/
	private static final String TABLE_BOOL = "b_buer";
	
	private static final String KEY = "m_key";
	private static final String VALUE = "m_value";
	
	private static final String[] RESULT = new String[]{KEY, VALUE};
	
	public ValueSQLHelper(Context context, String sqlName, int version){  
        super(context, sqlName, version, null);  
    }
    
	/**
	 * 请在这里创建您的表单。
	 */
    @Override
    public void onCreate(SQLiteDatabase db) {
    	info("VERSION = " + db.getVersion());
   	
    	create(db, TABLE_STRING, KEY + " nvarchar primary key, " + VALUE + " nvarchar");
    	create(db, TABLE_INT, KEY + " nvarchar primary key, " + VALUE + " integer");
    	create(db, TABLE_BOOL, KEY + " nvarchar primary key, " + VALUE + " char(5)");
    }
    
    /**
     * 请在这里升级您的表单。
     */
	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	info("oldVersion = " + oldVersion + ", newVersion = " + newVersion);
   	
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
     * @param key 钥匙，不能为空
     * @param value 要保存的数据，可为空
     * @return true 插入或更新数据成功；false 插入或更新数据失败
     */
	public final boolean setString(String key, String value) {
		if (TextTool.isEmpty(key)) {
			return false;
		}
		Cursor cur = null;
		try {
			final SQLiteDatabase mydb = getWritableDatabase();
			cur = mydb.query(TABLE_STRING, RESULT, KEY + "=?", new String[]{key}, null, null, null);
			ContentValues cv = new ContentValues();
			cv.put(KEY, key);
			cv.put(VALUE, value);
			if(cur != null && cur.getCount() > 0) { 	// 如果已保存，更新
				mydb.update(TABLE_STRING, cv, KEY + "=?", new String[]{key});
			} else {									// 如果未保存，直接保存
				mydb.insert(TABLE_STRING, null, cv);
			}
			return true;
		} catch (Exception e) {
			error("Save string data failed.", e);
		} finally {
			if (cur != null && !cur.isClosed()) {
				cur.close();
			}
		}
		return false;
	}
	
	/**
	 * 根据键值获取数据库内的字符串数据<br>
     * <b>该操作比较耗时，最好不要在主线程调用该方法。</b>
	 * @param key 可为空
	 * @return 如果key存在，返回保存的字符串数据；否则，返回null。
	 */
	public final String getString(String key) {
		if (TextTool.isEmpty(key)) {
			return null;
		}
		String value = null;
		Cursor cur = null;
		try {
			final SQLiteDatabase mydb = getReadableDatabase();
			cur = mydb.query(TABLE_STRING, RESULT, KEY + "=?", new String[]{key}, null, null, null);
			if (cur != null && cur.getCount() > 0 && cur.moveToFirst()) {
				value = cur.getString(1);
			}
		} catch (Exception e) {
			error("Failed to get string data.", e);
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
		if (TextTool.isEmpty(key)) {
			return false;
		}
		Cursor cur = null;
		try {
			final SQLiteDatabase mydb = getWritableDatabase();
			cur = mydb.query(TABLE_INT, RESULT, KEY + "=?", new String[] { key }, null, null, null);
			ContentValues cv = new ContentValues();
			cv.put(KEY, key);
			cv.put(VALUE, value);
			if (cur != null && cur.getCount() > 0) {
				mydb.update(TABLE_INT, cv, KEY + "=?", new String[] { key });
			} else {
				mydb.insert(TABLE_INT, null, cv);
			}
			return true;
		} catch (Exception e) {
			error("Save integer data failed.", e);
		} finally {
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
		if (TextTool.isEmpty(key)) {
			return value;
		}
		Cursor cur = null;
		try {
			final SQLiteDatabase mydb = getReadableDatabase();
			cur = mydb.query(TABLE_INT, RESULT, KEY + "=?", new String[]{key}, null, null, null);
			if (cur != null && cur.getCount() > 0 && cur.moveToFirst()) {
				value = cur.getInt(1);
			}
		} catch (Exception e) {
			error("Failed to get integer data.", e);
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
		if (TextTool.isEmpty(key)) {
			return false;
		}
		Cursor cur = null;
		try {
			final SQLiteDatabase mydb = getWritableDatabase();
			cur = mydb.query(TABLE_BOOL, RESULT, KEY + "=?", new String[] { key }, null, null, null);
			ContentValues cv = new ContentValues();
			cv.put(KEY, key);
			cv.put(VALUE, String.valueOf(value));
			if (cur != null && cur.getCount() > 0) {
				mydb.update(TABLE_BOOL, cv, KEY + "=?", new String[] { key });
			} else {
				mydb.insert(TABLE_BOOL, null, cv);
			}
			return true;
		} catch (Exception e) {
			error("Save bool data failed.", e);
		} finally {
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
		if (TextTool.isEmpty(key)) {
			return false;
		}
		String value = null;
		Cursor cur = null;
		try {
			final SQLiteDatabase mydb = getReadableDatabase();
			cur = mydb.query(TABLE_BOOL, RESULT, KEY + "=?", new String[]{key}, null, null, null);
			if (cur != null && cur.getCount() > 0 && cur.moveToFirst()) {
				value = cur.getString(1);
			}
		} catch (Exception e) {
			error("Failed to get bool data.", e);
		} finally {
			if (cur != null && !cur.isClosed()) {
				cur.close();
			}
		}
		return Boolean.valueOf(value);
	}
	
	public List<String> getPostions(){
		Cursor cur = null;
		try {
			final SQLiteDatabase mydb = getReadableDatabase();
			cur = mydb.rawQuery("SELECT * FROM " + TABLE_BOOL, null);
			
			if (cur != null && cur.getCount() > 0 && cur.moveToFirst()) {
				// 缓存数据
				List<String> sqLbeanlist = new ArrayList<String>(200);
				do {
					sqLbeanlist.add(cur.getString(cur.getColumnIndex(VALUE)));
				} while (cur.moveToNext());
				return sqLbeanlist;
			}
		} catch (Exception e) {
			error("Failed to get bool data.", e);
		} finally {
			if (cur != null && !cur.isClosed()) {
				cur.close();
			}
		}
		return null;
	}
	
}