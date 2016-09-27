package android.hqs.helper.constant;

import android.os.Environment;

/**
 * 路径信息
 * @author hqs2063594
 *
 */
public class PathStr {
	
	/** 系统路径 "/system" */
	public static final String getLocalSystemPath(){
		return Environment.getRootDirectory().getAbsolutePath();
	}
	/** app数据等信息的路径*/
	public static final String getLocalDataPath(){
		return Environment.getDownloadCacheDirectory().getAbsolutePath();
	}
	/**默认的sdcard0的路径*/
	public static final String getLocalSD0Path(){
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}
	
}
