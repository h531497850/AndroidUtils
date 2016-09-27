package android.hqs.tool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

/**
 * 网络工具
 * @author 胡青松
 */
public class NetTool {
	
	/**
	 * 网络是否有效
	 * @return 如果没有网，返回false；如果有网且连接上，返回true；否则返回false。
	 */
	public static boolean isVaild(Context context){
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		}
	    NetworkInfo info = cm.getActiveNetworkInfo();
	    if (info != null && info.isConnected()) {
            return true;
        }else{
            return false;
        }
	}
	
	/**
	 * 网络是否有效
	 */
	public static boolean isVaild(Context context, String type) {
		boolean isValid = false;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		}
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (null != networkInfo && networkInfo.isAvailable() && networkInfo.isConnectedOrConnecting()) {
			if (!TextUtils.isEmpty(type)) {
				if (type.equalsIgnoreCase((networkInfo.getTypeName()))) {
					isValid = true;
				}
			} else {
				isValid = true;
			}
		}
		return isValid;
	}
	
	public static String getType(Context context){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (null != networkInfo && networkInfo.isAvailable() && networkInfo.isConnectedOrConnecting()){
			return networkInfo.getTypeName();
		}				
		return null;
	}

}
