package android.hqs.gj.helper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.hqs.gj.tool.FileTool;
import android.hqs.gj.tool.LogTool;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

public class MediaHelper {
	private static final String TAG = LogTool.makeTag(MediaHelper.class);
	
	/**
	 * 内置sdcard是否已挂载
	 * @return
	 */
	public static final boolean isMediaMounted(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	public String getPrimaryStoragePath(Context context) {
        try {
            //StorageManager sm = (StorageManager)context.getSystemService(Context.STORAGE_SERVICE);
            Class<?> sm = Class.forName("android.os.storage.StorageManager");
            // 获取sdcard的路径：外置和内置
            // 3.0以上可以通过反射获取：
            Method m = sm.getDeclaredMethod("getVolumePaths", String.class, String.class);
            String[] paths = (String[])m.invoke(sm, null);
            // first element in paths[] is primary storage path
            return paths[0];
        } catch (Exception e) {
            Log.v(TAG, "failed", e);
        }
        return null;
    }
	
	/**
	 * 获取内置SD卡路径
	 */
	public static final String getInnerSDPath(){
		return Environment.getExternalStorageDirectory().getPath();
	}
	
	/**
	 * 获取外置SD卡路径
	 */
	public static ArrayList<String> getExternalSDPaths() {
		ArrayList<String> lResult = new ArrayList<String>();
		Runtime rt = null;
		try {
			rt = Runtime.getRuntime();
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "get runtime failed.", e);
			return null;
		}
		Process proc = null;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			proc = rt.exec("mount");
			is = proc.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				// 有待进一步验证！ 
				if (line.contains("extSdCard")) {
					String[] arr = line.split(" ");
					String path = arr[1];
					File file = new File(path);
					if (file.isDirectory()) {
						lResult.add(path);
					}
				}
			}
		} catch (Exception e) {
		} finally {
			FileTool.close(br);
			FileTool.close(isr);
			FileTool.close(is);
			if (proc != null) {
				proc.destroy();
			}
			rt.gc();
		}
		return lResult;
	}
	
	/**
	 * 获取外置SD卡路径
	 */
	public static ArrayList<String> getExtSDPaths() {
		ArrayList<String> sdcardPaths = new ArrayList<String>();
		String cmd = "cat /proc/mounts";
		Runtime rt = null;
		try {
			rt = Runtime.getRuntime();
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "get runtime failed.", e);
			return null;
		}
		Process p = null;
		BufferedInputStream bis = null;
		BufferedReader br = null;
		try {
			p = rt.exec(cmd);// 启动另一个进程来执行命令
			bis = new BufferedInputStream(p.getInputStream());
			br = new BufferedReader(new InputStreamReader(bis));

			String lineStr;
			while ((lineStr = br.readLine()) != null) {
				// 获得命令执行后在控制台的输出信息
				Log.i(TAG, "lineStr = " + lineStr);

				String[] temp = TextUtils.split(lineStr, " ");
				// 得到的输出的第二个空格后面是路径
				String result = temp[1];
				File file = new File(result);
				if (file.isDirectory() && file.canRead() && file.canWrite()) {
					Log.d(TAG, file.getAbsolutePath() + " can read and write.");
					// 可读可写的文件夹未必是sdcard，我的手机的sdcard下的Android/obb文件夹也可以得到
					sdcardPaths.add(result);
				}

				// 检查命令是否执行失败。
				if (p.waitFor() != 0 && p.exitValue() == 1) {
					// p.exitValue()==0表示正常结束，1：非正常结束
					Log.e(TAG, "execute cmd fail.");
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "can not find external sdcard!", e);
			sdcardPaths.add(Environment.getExternalStorageDirectory().getAbsolutePath());
		} finally {
			FileTool.close(br);
			FileTool.close(bis);
			if (p != null) {
				p.destroy();
			}
			rt.gc();
		}

		optimize(sdcardPaths);
		for (Iterator<String> iterator = sdcardPaths.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			Log.e(TAG, "after clear string = " + string);
		}
		return sdcardPaths;
	}

	private static void optimize(List<String> sdcaredPaths) {
		if (sdcaredPaths.size() == 0) {
			return;
		}
		int index = 0;
		while (true) {
			if (index >= sdcaredPaths.size() - 1) {
				String lastItem = sdcaredPaths.get(sdcaredPaths.size() - 1);
				for (int i = sdcaredPaths.size() - 2; i >= 0; i--) {
					if (sdcaredPaths.get(i).contains(lastItem)) {
						sdcaredPaths.remove(i);
					}
				}
				return;
			}

			String containsItem = sdcaredPaths.get(index);
			for (int i = index + 1; i < sdcaredPaths.size(); i++) {
				if (sdcaredPaths.get(i).contains(containsItem)) {
					sdcaredPaths.remove(i);
					i--;
				}
			}

			index++;
		}
	}

}
