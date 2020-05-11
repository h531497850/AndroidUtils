package android.hqs.gj.tool;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamTool {

	/**
	 * 从输入流读取数据
	 * @param is
	 * @return 字节数组
	 * @throws Exception 
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream is) throws Exception  {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while( (len = is.read(buffer)) !=-1 ){
			baos.write(buffer, 0, len);
		}
		baos.flush();
		baos.close();
		is.close();
		return baos.toByteArray();
	}
	
}

