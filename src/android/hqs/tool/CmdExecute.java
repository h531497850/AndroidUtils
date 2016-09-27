package android.hqs.tool;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CmdExecute {

	/**
	 * run("pm", "install", "-f", filePath);//安装apk，filePath为apk文件路径。<br/>
	 * run("pm", "uninstall",packageName);//卸载apk，packageName为包名，如com.example.android.apis
	 * @param cmd
	 * @param workDirectory
	 * @return
	 */
    public synchronized String run(String [] cmd, String workDirectory) {
        String result = "";
        InputStream in = null;
        Process process = null;
        try {
        	ProcessBuilder builder = new ProcessBuilder(cmd);
        	//ProcessBuilder builder = new ProcessBuilder().command(cmd);
            //设置一个路径
            if (workDirectory != null) {
                builder.directory(new File(workDirectory));
                builder.redirectErrorStream(true);
                process = builder.start();
                in = process.getInputStream();
                byte[] re = new byte[1024];
                while (in.read(re) != -1){
                    result = result + new String(re);
                }
            }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
                try {
					in.close();
				} catch (IOException e) {}    
            }
			if (process != null) {
				process.destroy();
			}
		}
        return result;
    }

}