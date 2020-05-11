package android.hqs.net;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.hqs.gj.tool.LogTool;
import android.hqs.gj.tool.TextTool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * 网络工具
 * @author 胡青松
 */
public class NetHttpTool {
	private static final String TAG = LogTool.makeTag("NetworkTool");
	
	public interface Type {
		/** 没有网络 */
		public static final int INVALID = -1;
		/** wifi网络 */
		public static final int WIFI = 0;
		/** wap网络 */
		public static final int MOBILE_WAP = 1;
		/** 2G网络 */
		public static final int MOBILE_2G = 2;
		/** 3G和3G以上网络，或统称为快速网络 */
		public static final int MOBILE_3G = 3;
	}
	
	/**
	 * 网络是否有效
	 * @return 如果没有网，返回false；如果有网且连接上，返回true；否则返回false。
	 */
	public static boolean isVaild(Context context){
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo info = cm.getActiveNetworkInfo();
	    if (info != null && info.isConnected()) {
            return true;
        }
	    return false;
	}
	
	/**
	/**
	 * 网络是否有效
	 * @param context
	 * @param type 不能为null
	 * @return 如果没有网或者网络不一样，返回false；如果有网且连接上，返回true；否则返回false。
	 */
	public static boolean isVaild(Context context, String type) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (TextUtils.isEmpty(type)) {
			return false;
		}
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (null != info && info.isAvailable() && info.isConnected()) {
			if (type.equalsIgnoreCase((info.getTypeName()))) {
				return true;
			}
			return false;
		}
		return false;
	}
	
	/**
	 * @param context
	 * @return "WIFI"、"MOBILE"或null
	 */
	public static String getTypeStr(Context context){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (null != info && info.isAvailable() && info.isConnectedOrConnecting()){
			return info.getTypeName();
		}				
		return null;
	}
	
	/**
	 * 获取网络状态，wifi,wap,2g,3g.
	 *
	 * @param context
	 *            上下文
	 * @return int 网络状态 {@link Type#MOBILE_2G},{@link Type#MOBILE_3G},
	 *         {@link Type#INVALID},{@link Type#MOBILE_WAP},{@link Type#WIFI}
	 */
	public static int getType(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			switch (info.getType()) {
			case ConnectivityManager.TYPE_WIFI:
				return Type.WIFI;
			case ConnectivityManager.TYPE_MOBILE:
				String host = System.getProperty("http.proxyHost");
				return TextUtils.isEmpty(host) ? (isFastMobileNetwork(context) ? Type.MOBILE_3G : Type.MOBILE_2G) : Type.MOBILE_WAP;
			default:
				break;
			}
		}
		return Type.INVALID;
	}

	private static boolean isFastMobileNetwork(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		switch (tm.getNetworkType()) {
		case TelephonyManager.NETWORK_TYPE_1xRTT:	// ~ 50-100 kbps
			return false;
		case TelephonyManager.NETWORK_TYPE_CDMA:	// ~ 14-64 kbps
			return false;
		case TelephonyManager.NETWORK_TYPE_EDGE:	// ~ 50-100 kbps
			return false;
		case TelephonyManager.NETWORK_TYPE_EVDO_0:	// ~ 400-1000 kbps
			return true;
		case TelephonyManager.NETWORK_TYPE_EVDO_A:	// ~ 600-1400 kbps
			return true;
		case TelephonyManager.NETWORK_TYPE_GPRS:	// ~ 100 kbps
			return false;
		case TelephonyManager.NETWORK_TYPE_HSDPA:	// ~ 2-14 Mbps
			return true;
		case TelephonyManager.NETWORK_TYPE_HSPA:	// ~ 700-1700 kbps
			return true;
		case TelephonyManager.NETWORK_TYPE_HSUPA:	// ~ 1-23 Mbps
			return true;
		case TelephonyManager.NETWORK_TYPE_UMTS:	// ~ 400-7000 kbps
			return true;
		case TelephonyManager.NETWORK_TYPE_EHRPD:	// ~ 1-2 Mbps
			return true;
		case TelephonyManager.NETWORK_TYPE_EVDO_B:	// ~ 5 Mbps
			return true;
		case TelephonyManager.NETWORK_TYPE_HSPAP:	// ~ 10-20 Mbps
			return true;
		case TelephonyManager.NETWORK_TYPE_IDEN:	// ~ 25 kbps
			return false;
		case TelephonyManager.NETWORK_TYPE_LTE:		// ~ 10+ Mbps
			return true;
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return false;
		default:
			return false;
		}
	}
	
	/**
	 * 组织HttpGet参数和对参数进行URL编码
	 * @param mBaseUrl 不能为空
	 * @param baseParams 
	 * @return 编码失败，返回mBaseUrl
	 */
	public static String encodeHttpGetParams(String mBaseUrl, Map<String, String> baseParams) {
		StringBuffer paramSb = new StringBuffer();
		try {
			Iterator<Entry<String, String>> iter = baseParams.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> entry = iter.next();
				String key = entry.getKey();
				String val = entry.getValue();
				if (key != null) {
					if (key.equals("city")) {
						key = URLEncoder.encode(key, "gbk");
						if (val != null) {
							val = URLEncoder.encode(val, "gbk");
						}
					}else {
						key = URLEncoder.encode(key, "UTF-8");	
						if (val != null) {
							val = URLEncoder.encode(val, "UTF-8");
						}
					}
				}
				paramSb.append("&" + key + "=" + val);
			}
			String paramStr = paramSb.toString(); 
			if (!TextTool.isEmpty(paramStr)) {
				paramStr = paramStr.replaceFirst("&", "?");
				// 将URL与参数拼接
				mBaseUrl += paramStr;
			}
		} catch (java.io.UnsupportedEncodingException e) {
			Log.e(TAG, "encode url failed.", e);
		}	
		return mBaseUrl;
	}

}
