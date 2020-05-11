package android.hqs.db;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.hqs.gj.tool.LogTool;
import android.text.TextUtils;
import android.util.Log;

/**
 * 用于支持对存储在SD卡上的数据库的访问
 * @author hqs2063594
 */
public abstract class MediaSQLHelper extends BaseSQLHelper {
	
	public MediaSQLHelper(Context context, String sqlName){  
        this(context, null, sqlName);  
    }
	
	/**
	 * @param context   上下文对象 	不能为空
     * @param sqlName   数据库名称 	不能为空
	 * @param clazz 类名，用于生成打印日志的标签，不能为空
	 */
	public MediaSQLHelper(Context context, String dirName, String sqlName){  
        this(context, dirName, sqlName, 1);  
    }
	
	/**
	 * @param context   上下文对象 	不能为空
     * @param sqlName   数据库名称 	不能为空
	 * @param clazz 类名，用于生成打印日志的标签，不能为空
	 * @param version
	 */
	public MediaSQLHelper(Context context, String dirName, String sqlName, int version){  
        this(context, dirName, sqlName, version, null);  
    }
	
	/** 
     * 在{@link #SQLiteOpenHelper}的子类当中，必须有该构造函数 
     * @param context   上下文对象 	不能为空
     * @param sqlName   数据库名称 	不能为空
	 * @param clazz 类名，用于生成打印日志的标签，不能为空
     * @param version   当前数据库的版本，值必须是整数并且是递增的状态 
     * @param factory 
     */  
    public MediaSQLHelper(Context context, String dirName, String sqlName, int version, CursorFactory factory) {  
        //必须通过super调用父类当中的构造函数
    	super(new MediaSQLContext(context, dirName), sqlName, version, factory);
        // 以写的方式打开数据库对应的SQLiteDatabase对象 ，这个时候创建数据库
        //getWritableDatabase(); 在这里会调用发生死锁
    }
    
    /**
     * 用于支持对存储在SD卡上的数据库的访问
     * @author hqs2063594
     */
    public static final class MediaSQLContext extends ContextWrapper {
		private static final String TAG = LogTool.makeTag(MediaSQLHelper.class,
				"MediaSQLContext");
    	
    	private String dirName;
    	
    	public MediaSQLContext(Context base) {
    		this(base, "");
    	}
    	
    	/**
    	 * 
    	 * @param base 
    	 * @param dirName 指定数据库的相对路径，如果为空，则以包名代替。
    	 */
    	public MediaSQLContext(Context base, String dirName) {
    	    super(base);
            this.dirName = dirName;
    	}
    	
    	@Override
    	public File getDatabasePath(String name) {
    		// 判断是否存在sd卡
    		boolean sdExist = android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
    		if (!sdExist) {	// 如果不存在,
    			Log.e(TAG, "SD card management: SD card does not exist, please load the SD card.");
    			return null;
    		} else {		// 如果存在
    			// 获取sd卡路径
    			String dbDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    			// 数据库所在目录
    			dbDir += "/" + ((TextUtils.isEmpty(dirName)) ? getPackageName() : dirName);
    			String dbPath = dbDir + "/" + name;// 数据库路径
    			// 判断目录是否存在，不存在则创建该目录
    			File dirFile = new File(dbDir);
    			if (!dirFile.exists()){
    				dirFile.mkdirs();
    			}

    			// 数据库文件是否创建成功
    			boolean isFileCreateSuccess = false;
    			// 判断文件是否存在，不存在则创建该文件
    			File dbFile = new File(dbPath);
    			if (!dbFile.exists()) {
    				try {
    					isFileCreateSuccess = dbFile.createNewFile();// 创建文件
    				} catch (IOException e) {
    					Log.e(TAG, "create db file failed!", e);
    				}
    			} else {
    				isFileCreateSuccess = true;
    			}

    			// 返回数据库文件对象
    			if (isFileCreateSuccess){
    				return dbFile;
    			} else {
    				return null;
    			}
    		}
    	}
    	
    	/**
    	 * 重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
    	 */
    	@Override
    	public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
    		return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
    	}
    	
    	/**
    	 * 重载这个方法，是用来打开SD卡上的数据库的，Android 4.0会调用此方法获取数据库。
    	 */
    	@Override
    	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
    			CursorFactory factory, DatabaseErrorHandler errorHandler) {
    		return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
    	}

    }
	
}