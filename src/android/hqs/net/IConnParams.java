package android.hqs.net;

import java.net.HttpURLConnection;

/**
 * 如果要添加其他连接参数，请实现该接口
 * 
 * @author 胡青松
 */
public interface IConnParams {
	/**
	 * 请给urlConn添加其他连接属性，下面的是一些事例
	 * <p>
	 * 1、urlConn.addRequestProperty("Accept", " ")<br>
	 * 2、urlConn.addRequestProperty("Accept-Language", "zh-cn")<br>
	 * 3、POST数据的长度 :<br>
	 * urlConn.addRequestProperty("Content-Length", "")
	 * 
	 * @param urlConn
	 */
	public void addYourConnParams(HttpURLConnection urlConn);
}
