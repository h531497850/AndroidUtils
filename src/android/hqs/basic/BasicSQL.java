package android.hqs.basic;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.hqs.tool.LogcatTool;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * {@link #SQLiteOpenHelper}是一个辅助类，用来管理数据库的创建和版本他，它提供两个方面的功能 :
 * <ul>
 * <li>{@link #getReadableDatabase()}、{@link #getWritableDatabase()}可以获得
 * {@link #SQLiteDatabase}对象，通过该对象可以对数据库进行操作</li>
 * <li>提供了{@link #onCreate()}、{@link #onUpgrade()}两个回调函数，允许我们再创建和升级数据库时，进行自己的操作</li>
 * </ul>
 * 
 * {@link #onCreate(SQLiteDatabase)}第一次创建数据库调用；</br>
 * {@link #onUpgrade(SQLiteDatabase, int, int)}数据库升级调用<p>
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
 * @author hqs2063594
 * 
 */
public abstract class BasicSQL extends SQLiteOpenHelper {
	private final String Tag = LogcatTool.makeTag(getClass());
	
	/** 在子类中通过该方法获取父类的对象，来通知子类和父类，以达到线程安全的目的（由于数据库不能异步操作）。
	 * read时不锁，提高效率，write时一定要锁；不过SQLiteOpenHelper已经锁住了。*/
	protected final byte[] lock = new byte[0];
	
	/**
	 * @param context   上下文对象 	不能为空
     * @param sqlName   数据库名称 	不能为空
	 * @param clazz 类名，用于生成打印日志的标签，不能为空
	 */
	public BasicSQL(Context context, String sqlName){  
        this(context, sqlName, 1);  
    }
	
	/**
	 * @param context   上下文对象 	不能为空
     * @param sqlName   数据库名称 	不能为空
	 * @param clazz 类名，用于生成打印日志的标签，不能为空
	 * @param version
	 */
	public BasicSQL(Context context, String sqlName, int version){  
        this(context, sqlName, version, null);  
    }
	
	/** 
     * 在{@link #SQLiteOpenHelper}的子类当中，必须有该构造函数 
     * @param context   上下文对象 	不能为空
     * @param sqlName   数据库名称 	不能为空
	 * @param clazz 类名，用于生成打印日志的标签，不能为空
     * @param version   当前数据库的版本，值必须是整数并且是递增的状态 
     * @param factory 
     */  
    public BasicSQL(Context context, String sqlName, int version, CursorFactory factory) {  
        //必须通过super调用父类当中的构造函数  
        super(context, sqlName, factory, version);
        // 以写的方式打开数据库对应的SQLiteDatabase对象 ，这个时候创建数据库
        //getWritableDatabase();
    }
    
	// ========================================================================================================
	// ======================================= TODO 下面是公布给子类的方法 =======================================
	// ========================================================================================================
	/**
     * 创建表的方法，如果没有该表，请创建。
     * @param table 表名
     * @param columnsSql 列Sql语句
     */
    protected final void create(SQLiteDatabase db, String table, String columnsSql) {
    	db.execSQL("create table if not exists " + table + " (" + columnsSql + ")");
	}
    /**
     * 为表添加列
     * @param db
     * @param table
     * @param columnSql 注意：<b>仅一列</b>
     */
    protected final void addColumn(SQLiteDatabase db, String table, String columnSql) {
    	db.execSQL("ALTER TABLE " + table + " ADD COLUMN "+ columnSql);
    }
    
    /**
	 * 删除表的方法。在数据（比如表结构等）有更新时，将以前老版的数据取出，然后删除老版的各个表，然后重新创建表，
	 * 最后把以前的数据重新加入新表下。 如果该表存在，删除。
	 * @param table 表名
	 */
    protected final void drop(SQLiteDatabase db, String table) {
    	db.execSQL("DROP TABLE IF EXISTS " + table);
	}
    
    
    protected final void deleteRows(SQLiteDatabase db, String table) {
    	db.delete(table, null, null);
	}
    
    protected final void deleteRow(SQLiteDatabase db, String table, String whereClause, String[] whereArgs) {
    	db.delete(table, whereClause, whereArgs);
	}
    
	// ========================================================================================================
	// ============================= TODO 下面是公开的方法，注意：数据库已经创建完成 ================================
	// ========================================================================================================
    /**
     * 清空所选表内的所有数据。同步方法是同步块(this)的简写形式，this为当前实例对象。<br>
     * <b>该操作比较耗时，最好不要在主线程调用该方法。</b>
     * @param table
     */
    public final void deleteRows(String table) {
    	getWritableDatabase().delete(table, null, null);
	}
    /**
     * <b>该操作比较耗时，最好不要在主线程调用该方法。</b>
     * @param table
     * @param whereClause
     * @param whereArgs
     */
    public final void deleteRow(String table, String whereClause, String[] whereArgs) {
    	getWritableDatabase().delete(table, whereClause, whereArgs);
	}
    /**
     * 更新行<br>
     * <b>该操作比较耗时，最好不要在主线程调用该方法。</b>
     * @param table
     * @param key 要更新的列
     * @param value 更新为该值
     * @param whereClause = 要更新的列标识 + "=?"
     * @param whereArgs {}
     */
    public final void updateRow(String table, String key, String value, String whereClause, String[] whereArgs){
    	ContentValues cv = null;
		try {
			final SQLiteDatabase mydb = getWritableDatabase();
			cv = new ContentValues();
			cv.put(key, value);
			mydb.update(table, cv, whereClause, whereArgs);
		} catch (Exception e) {
			error("updateRow", "Update row fail!", e);
		} finally {
			if (cv != null) { // 保证能清除HashMap的数据
				cv.clear();
				cv = null;
			}
		}
    }

	// ========================================================================================================
	// ==================================== TODO 下面是公开的方法 ===============================================
	// ========================================================================================================
	/** 获取实例类名 */
	public final String getClsName() {
		return getClass().getSimpleName();
	}
	
	// ========================================================================================================
	// ==================================== TODO 下面是打印日志的方法 ============================================
	// ========================================================================================================
	/**蓝色，调试信息*/
	protected final void debug(Object obj) {
		LogcatTool.debug(Tag, obj);
	}
	protected final void debug(String methodName, Object obj) {
		LogcatTool.debug(Tag, methodName, obj);
	}
	protected final void debug(String methodName, Throwable tr) {
		LogcatTool.debug(Tag, methodName, tr);
	}
	
	/** 绿色，正常信息 */
	protected final void info(Object obj) {
		LogcatTool.info(Tag, obj);
	}
	protected final void info(String methodName, Object obj) {
		LogcatTool.info(Tag, methodName, obj);
	}
	protected final void info(String methodName, Throwable tr) {
		LogcatTool.info(Tag, methodName, tr);
	}
	protected void info(String listName, byte[] list){
		LogcatTool.info(Tag, listName, list);
	}
	protected final void info(String methodName, String listName, byte[] list) {
		LogcatTool.info(Tag, methodName, listName, list);
	}
	protected void info(String listName, int[] list){
		LogcatTool.info(Tag, listName, list);
	}
	protected final void info(String methodName, String listName, int[] list) {
		LogcatTool.info(Tag, methodName, listName, list);
	}
	
	/**黑色，冗长信息*/
	protected final void verbose(Object obj) {
		LogcatTool.verbose(Tag, obj);
	}
	protected final void verbose(String methodName, Object obj) {
		LogcatTool.verbose(Tag, methodName, obj);
	}
	protected final void verbose(String methodName, Throwable tr) {
		LogcatTool.verbose(Tag, methodName, tr);
	}
	
	/**红色，错误信息*/
	protected final void error(Object obj) {
		LogcatTool.error(Tag, obj);
	}
	protected final void error(String methodName, Object obj) {
		LogcatTool.error(Tag, methodName, obj);
	}
	protected final void error(String methodName, Throwable tr) {
		LogcatTool.error(Tag, methodName, tr);
	}
	protected final void error(String methodName, Object obj, Throwable tr) {
		LogcatTool.error(Tag, methodName, obj, tr);
	}
	
	/**紫色，不应发生的信息*/
	protected final void wtf(Object obj) {
		LogcatTool.wtf(Tag, obj);
	}
	protected final void wtf(String methodName, Object obj) {
		LogcatTool.wtf(Tag, methodName, obj);
	}
	protected final void wtf(String methodName, Throwable tr) {
		LogcatTool.wtf(Tag, methodName, tr);
	}
	
}