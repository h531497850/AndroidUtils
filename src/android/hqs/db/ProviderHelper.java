package android.hqs.db;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hqs.basic.BasicProvider;
import android.hqs.db.ProviderDB.HqsTable;
import android.net.Uri;

/**
 * 为外部应用提高访问该本应用数据库的接口。
 * 使用{@link android.content.ContentResolver#insert(Uri, ContentValues)}等方法来操作该provider，进而达到操作数据库的目的。
 * @author 胡青松
 */
public class ProviderHelper extends BasicProvider {

	private ProviderDB mDb;
	
	/**匹配用户传递过来的Uri，常量UriMatcher.NO_MATCH表示不匹配任何路径的返回码 */
	private static final UriMatcher SUM = new UriMatcher(UriMatcher.NO_MATCH);;
	
	static {
		// 添加其他URI匹配路径
		// 如果用UriMatcher#match(Uri)匹配该URI，返回匹配码为1(返回码必须为正数)
		SUM.addURI(ProviderDB.AUTHORITY, HqsTable.TABLE_NAME, HqsTable.CODE);
		SUM.addURI(ProviderDB.AUTHORITY, HqsTable.TABLE_ID, HqsTable.CODE_ID);
		SUM.addURI(ProviderDB.AUTHORITY, HqsTable.TABLE_TXT, HqsTable.CODE_TXT);
	}
	
	/**
	 * Android开机后，ContentProvider被第一次访问时被创建，创建后调用该方法。
	 */
	@Override
	public boolean onCreate() {
		// 初始化数据库
		mDb = new ProviderDB(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// 
		ContentUris.parseId(uri);
		
		String table = null;
		switch (SUM.match(uri)) {  
        case HqsTable.CODE:
        	table = HqsTable.TABLE_NAME;
            break;
        case HqsTable.CODE_ID:
        case HqsTable.CODE_TXT:
        	table = HqsTable.TABLE_NAME;
            break;
        default:  // 如果发送的uri不匹配，抛出，不再向下执行。
            throw new IllegalArgumentException("A unknown or null uri: " + uri);
        }
		SQLiteDatabase db = mDb.getReadableDatabase();
		return db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
	}

	/**
	 * 返回当前Url所代表数据的MIME类型。
	 * 如果操作的数据属于集合类型，那么MIME类型字符串应该以vnd.android.cursor.dir/开头;
	 * 如果要操作的数据属于非集合类型数据，那么MIME类型字符串应该以vnd.android.cursor.item/开头。
	 */
	@Override
	public String getType(Uri uri) {
		switch (SUM.match(uri)) {  
        case HqsTable.CODE:  
            return HqsTable.MIME_TYPE;  
        case HqsTable.CODE_ID:  
        case HqsTable.CODE_TXT:  
            return HqsTable.MIME_ITEM_TYPE;  
        default:  
            throw new IllegalArgumentException("A unknown or null uri: " + uri);
        }
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		String table = null;
		String col = null;
		switch (SUM.match(uri)) {
		case HqsTable.CODE:
			table = HqsTable.TABLE_NAME;
			//col = HqsTable.KEY1;
			break;
		case HqsTable.CODE_ID:
			table = HqsTable.TABLE_NAME;
			//col = HqsTable.KEY2;
			break;
		case HqsTable.CODE_TXT:
			table = HqsTable.TABLE_NAME;
			//col = HqsTable.KEY3;
			break;
		default:
			throw new IllegalArgumentException("A unknown or null uri: " + uri);
		}
		
		SQLiteDatabase db = mDb.getWritableDatabase();
		long rowId = db.insert(table, col, values);
		if (rowId > 0) {
			// 用于为路径加上ID部分：
			Uri resultUri = ContentUris.withAppendedId(uri, rowId);
			// 通知注册在此URI上的访问者，在需要该数据的地方注册。
			getContext().getContentResolver().notifyChange(resultUri, null);				
			debug("insert success, result = " + resultUri);
			return resultUri;
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		String table = null;
		switch (SUM.match(uri)) {  
        case HqsTable.CODE:
        	table = HqsTable.TABLE_NAME;
            break;
        case HqsTable.CODE_ID:
        case HqsTable.CODE_TXT:
        	table = HqsTable.TABLE_NAME;
            break;
        default:  // 如果发送的uri不匹配，抛出，不再向下执行。
            throw new IllegalArgumentException("A unknown or null uri: " + uri);
        }
		SQLiteDatabase db = mDb.getWritableDatabase();
		return db.delete(table, selection, selectionArgs);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		String table = null;
		switch (SUM.match(uri)) {  
        case HqsTable.CODE:
        	table = HqsTable.TABLE_NAME;
            break;
        case HqsTable.CODE_ID:
        case HqsTable.CODE_TXT:
        	table = HqsTable.TABLE_NAME;
            break;
        default:  // 如果发送的uri不匹配，抛出，不再向下执行。
            throw new IllegalArgumentException("A unknown or null uri: " + uri);
        }
		SQLiteDatabase db = mDb.getWritableDatabase();
		return db.update(table, values, selection, selectionArgs);
	}
	
}
