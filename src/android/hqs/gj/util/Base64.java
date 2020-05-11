/**
 * 
 */
package android.hqs.gj.util;

import java.io.IOException;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class Base64 {

	public static String base64(String source) throws Exception {
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(source.getBytes("UTF-8"));
	}
	
	public static String encode64(byte[] bytes){
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(bytes);
	}
	
	public static byte[] decode64(String source)throws IOException{
		BASE64Decoder decoder = new BASE64Decoder();
		return decoder.decodeBuffer(source);
	}
}
