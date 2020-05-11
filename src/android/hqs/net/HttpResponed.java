package android.hqs.net;

import java.net.HttpURLConnection;

public interface HttpResponed {
	public static final int START_TO_CONNECT = 100;
	
	public static final int HTTP_OK = 200;
	
	public static final int CONNECT_FAILED_CLIENT_ERROR = 201;
	public static final int CONNECT_FAILED_NETWORK_ERROR = 202;
	public static final int CONNECT_FAILED_NO_RESPONSE = 203;
	public static final int CONNECT_FAILED_NO_STATUS_CODE = 204;
	public static final int CONNECT_FAILED_WRONG_STATUS_CODE = 205;
	public static final int CONNECT_FAILED_GET_CONTENT_ERROR = 206;
	public static final int CONNECT_FAILED_DISCONNECTION = 207;

	/**
	 * 服务响应成功，并且有返回数据
	 */
	public void onSuccess(HttpResponser responser);

	/**
	 * 服务响应失败
	 */
	public void onError(int responseCode);
	/**
	 * 服务连接成功，但服务器不接受客户端数据等
	 */
	public void onError(int responseCode, HttpResponser responser);

	/**
	 * 不知道不服务器的状态
	 * 
	 * @param urlConn
	 *            用完后请断开连接
	 */
	public void onFinish(HttpURLConnection urlConn);
	
}