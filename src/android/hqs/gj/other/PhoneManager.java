package android.hqs.gj.other;

import java.util.List;

import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;

public class PhoneManager {
	
	public interface ProvidersName {
		/**中国移动(CMCC)*/
		public static final int CHINA_CMCC1 = 46000;
		/**中国联通(CUCC)*/
		public static final int CHINA_CUCC = 46001;
		/**中国移动(CMCC)*/
		public static final int CHINA_CMCC2 = 46002;
		/**中国电信(CTCC)*/
		public static final int CHINA_CTCC = 46003;
	}
	
	private TelephonyManager mTManager;
	
	private final String IMEI;
	private final String IMSI;
	
	public PhoneManager(Context context) {
		mTManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		IMSI = mTManager.getSubscriberId();
		IMEI = mTManager.getDeviceId();
	}
	
	public String getAllInfo(){
		StringBuffer info = new StringBuffer();
		info.append("IMEI码: " + IMEI)
		.append("\nIMSI码: " + IMSI)
		.append("\n位置：" + getLocation())
		.append("\n支持的SIM卡个数：" + getSimCount())
		.append("\nSIM卡状态: " + getSimStateStr())
		.append("\n电话号码：" + getPhoneNumber())
		.append("\n运营商：" + getProvidersName())
		.append("\n运营商编号: " + getNetworkOperator())					// 运营商编号
		.append("\n手机制式编号: " + getSimOperator())						// 手机制式编号
		.append("\n手机制式: " + getSimOperatorName())					// 手机制式
		.append("\n电话类型: " + getPhoneType())
		.append("\n设备软件版本: " + getDeviceSoftwareVersion())
		.append("\n")
		.append("\nMmsUserAgent: " + getMmsUserAgent())
		.append("\nSimSerialNumber: " + getSimSerialNumber())
		.append("\nCallState: " + getCallState())
		.append("\nDataState: " + getDataState())
		.append("\nGroupIdLevel1: " + getGroupIdLevel1())
		.append("\n")
		.append("\nAllCellInfo: " + getAllCellInfo());
		return info.toString();
	}
	
	/**
	 * @return 
	 */
	public String getAllCellInfo(){
		List<CellInfo> infos = mTManager.getAllCellInfo();
		StringBuffer sb = new StringBuffer();
		if (infos != null && infos.size() > 0) {
			for (CellInfo cellInfo : infos) {
				sb.append(cellInfo.toString() + "\n");
			}
		}
		return sb.toString();
	}
	
	/**
	 * @return 
	 */
	public String getGroupIdLevel1(){
		return mTManager.getGroupIdLevel1();
	}
	
	/**
	 * @return 
	 */
	public String getDeviceSoftwareVersion(){
		return mTManager.getDeviceSoftwareVersion();
	}
	
	/**
	 * @return 
	 */
	public int getDataState(){
		return mTManager.getDataState();
	}
	
	/**
	 * @return 
	 */
	public int getCallState(){
		return mTManager.getCallState();
	}
	
	/**
	 * @return 
	 */
	public String getSimSerialNumber(){
		return mTManager.getSimSerialNumber();
	}
	
	/**
	 * @return 
	 */
	public String getMmsUserAgent(){
		return mTManager.getMmsUserAgent();
	}
	
	/**
	 * @return 电话类型
	 */
	public int getPhoneType(){
		return mTManager.getPhoneType();
	}
	
	/**
	 * @return 手机制式
	 */
	public String getSimOperator(){
		return mTManager.getSimOperator();
	}
	/**
	 * @return 手机制式编号
	 */
	public String getSimOperatorName(){
		return mTManager.getSimOperatorName();
	}
	
	/**
	 * 唯一地识别一个移动台设备的编码，为一个15位的十进制数数字，其结构是：</br>
	 * TAC        FAC         SNR        SP</br>
	 * 6位数字    2位数字    6位数字    l位数字</br>
	 * TAC＝型号批准码，由欧洲型号认证中心分配。</br>
	 * FAC＝工厂装配码，由厂家编码，表示生产厂家及其装配地。</br>
	 * SNR＝序号码，由厂家分配。识别每个TAC和FAC中的某个设备的。</br>
	 * SP ＝备用，备作将来使用。
	 * @return 国际移动台设备识别码(IMEI)。
	 */
	public String getIMEI(){
		return IMEI;
	}
	
	/**
	 * 为了在无线路径和整个GSM移动通信网上正确地识别某个移动客户，就必须给移动客户分配一个特定的识别码。</br>
	 * 用于GSM移动通信网所有信令中，存储在客户识别模块(SIM)、HLR、VLR中。</br>
	 * IMSI号码结构为：</br>
	 * MCC	MNC	MSIN |------------国际移动客户识别 ------------|	|--国内移动客户识别 --|</br>
	 * MCC＝移动国家号码，由3位数字组成，唯一地识别移动客户所属的国家。</br>
	 * 我国为460。</br>
	 * MNC＝移动网号，由2位数字组成，用于识别移动客户所归属的移动网。</br>
	 * 中国移动公司GSM PLMN网为00、02；</br>
	 * 中国联通公司GSM PLMN网为0l；</br>
	 * 中国电信公司GSM PLMN网为03。</br>
	 * MSIN＝移动客户识别码，采用等长11位数字构成。唯一地识别国内GSM移动通信网中移动客户。
	 * @return 国际移动客户识别码(IMSI)。
	 */
	public String getIMSI(){
		return IMSI;
	}
	
	/** SIM卡归属国家或地区 */
	public int getCountry(){
		try {
			// 取前3位
			return Integer.parseInt(IMSI.substring(0, 3));
		} catch (NumberFormatException e) {
			return Integer.MIN_VALUE;
		}
	}
	
	public int getMNC(){
		try {
    		// 取4~5位
    		return Integer.parseInt(IMSI.substring(3, 5));
    	} catch (NumberFormatException e) {
    		return Integer.MIN_VALUE;
    	}
	}
	
	/**
	 * 添加权限：android.permission.ACCESS_COARSE_LOCATION
	 * @return 当前地理位置
	 */
	public String getLocation(){
		CellLocation coordinate = mTManager.getCellLocation();
		return coordinate != null ? coordinate.toString() : "null";
	}
	
	/**
	 * @return 支持的SIM卡个数。
	 */
	public int getSimCount(){
		return mTManager.getPhoneCount();
	}
	
	/**
	 * @return 当前电话号码
	 */
	public String getPhoneNumber() { 
		return mTManager.getLine1Number();
	}
	
	public int getSimState() { 
        return mTManager.getSimState(); 
    }
	
    /**
	 * @return 网络运营商编号
	 */
	public String getNetworkOperator(){
		return mTManager.getNetworkOperator();
	}
	
	/**
	 * @return 网络运营商名称
	 */
	public String getNetworkOperatorName(){
		return mTManager.getNetworkOperatorName();
	}
	
	/**
     * @return 获取运营商名称
     */
    public String getProvidersName() { 
    	String providers = null;
    	int provider;
    	try {
			// 取前3位
    		provider = Integer.parseInt(IMSI.substring(0, 3));
		} catch (NumberFormatException e) {
			provider = Integer.MIN_VALUE;
		}
    	
    	switch (provider) {
		case ProvidersName.CHINA_CMCC1:
		case ProvidersName.CHINA_CMCC2:
			providers = "中国移动";
			break;
		case ProvidersName.CHINA_CUCC:
			providers = "中国联通";
			break;
		case ProvidersName.CHINA_CTCC:
			providers = "中国电信";
			break;
		default:
			providers = "无法识别";
			break;
		}
        return providers; 
    }
    
    public String getSimStateStr() { 
        switch (getSimState()) {
		case TelephonyManager.SIM_STATE_READY:
			return "有卡";
		default:
			return "当前状态无法识别";
		}
    }

}
	
