package android.hqs.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.hqs.basic.BasicSQL;

/**
 * 用于支持对存储在SD卡上的数据库的访问
 * @author hqs2063594
 */
public abstract class SdcardDB extends BasicSQL {
	
	public SdcardDB(Context context, String sqlName){  
        this(context, null, sqlName);  
    }
	
	/**
	 * @param context   上下文对象 	不能为空
     * @param sqlName   数据库名称 	不能为空
	 * @param clazz 类名，用于生成打印日志的标签，不能为空
	 */
	public SdcardDB(Context context, String dirName, String sqlName){  
        this(context, dirName, sqlName, 1);  
    }
	
	/**
	 * @param context   上下文对象 	不能为空
     * @param sqlName   数据库名称 	不能为空
	 * @param clazz 类名，用于生成打印日志的标签，不能为空
	 * @param version
	 */
	public SdcardDB(Context context, String dirName, String sqlName, int version){  
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
    public SdcardDB(Context context, String dirName, String sqlName, int version, CursorFactory factory) {  
        //必须通过super调用父类当中的构造函数
    	super(new DBContext(context, dirName), sqlName, version, factory);
        // 以写的方式打开数据库对应的SQLiteDatabase对象 ，这个时候创建数据库
        //getWritableDatabase(); 在这里会调用发生死锁
    }
	
}