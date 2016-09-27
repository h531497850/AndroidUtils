package android.hqs.db;

import android.database.ContentObserver;
import android.hqs.basic.BasicActivity;
import android.hqs.db.ProviderDB.HqsTable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ProviderActivity extends BasicActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setDebug(true);
		getContentResolver().registerContentObserver(HqsTable.URI, true, mObserver);
	}
	
	private Handler mHandler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			return true;
		}
	});
	
	private ContentObserver mObserver = new ContentObserver(mHandler) {
		@Override
		public void onChange(boolean selfChange) {
			// 此处可以进行相应的业务处理
			
		}

		@Override
		public void onChange(boolean selfChange, Uri uri) {
			// 此处可以进行相应的业务处理
			
		}
	};

}
