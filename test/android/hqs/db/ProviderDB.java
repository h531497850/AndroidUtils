package android.hqs.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hqs.basic.BasicSQL;
import android.net.Uri;

/**
 * 外部应用可访问的数据库。
 * @author 胡青松
 */
public class ProviderDB extends BasicSQL {
	private static ProviderDB mInstance = null;

	public static final String DB_NAME = "hqs_provider.db";
	private static final int VERSION = 1;
	
	/**
	 * 用于唯一标识这个ContentProvider，外部调用者可以根据这个标识来找到它。它定义了是哪个ContentProvider提供这些数据。
	 * 对于第三方应用程序，为了保证URI标识的唯一性，它必须是一个完整的、小写的类名。这个标识在 元素的 authorities属性中说明：
	 * 一般是定义该ContentProvider的包.类的名称
	 */
	public static final String AUTHORITY = "android.hqs.db.providerdb";
	
	public static final class HqsTable {
		public static final String TABLE_NAME = "people";
		/**
		 * 1、"content://" --> 是协议，标准前缀，用来说明是一个ContentProvider控制这些数据，无法改变的；</br>
		 * 2、AUTHORITY；</br>
		 * 3、"/hqs" --> 路径（path），通俗的讲就是你要操作的数据库中表的名字；也可自定义，但使用的时要保持一致；</br>
		 * 4、如果URI中包含需要获取记录的ID（可以是数字，也可以是文本），则返回该id对应的数据；否则，返回全部。
		 */
		public static final Uri URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);
		
		public static final String MIME_TYPE = "vnd.android.cursor.dir/vnd.android.peoples";
		public static final String MIME_ITEM_TYPE = "vnd.android.cursor.item/vnd.android.people";
		/**（#为数字通配符）*/
		public static final String TABLE_ID = TABLE_NAME+"/#";
		/**（*为文本通配符）*/
		public static final String TABLE_TXT = TABLE_NAME+"/*";
		/** 为ContentProvider提供的Uri匹配码 */
		public static final int CODE = 1;
		/** 为ContentProvider提供的Uri匹配码 */
		public static final int CODE_ID = 2;
		/** 为ContentProvider提供的Uri匹配码 */
		public static final int CODE_TXT = 3;

	}

	private ProviderDB(Context context) {
		super(context, DB_NAME, VERSION);
	}
	
	public static ProviderDB getInstance(Context context) {
	    // Use the application context, which will ensure that you 
	    // don't accidentally leak an Activity's context.
	    // See this article for more information: http://bit.ly/6LRzfx
	    if (mInstance == null) {
	      mInstance = new ProviderDB(context.getApplicationContext());
	    }
	    return mInstance;
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
