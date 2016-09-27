package android.hqs.tool;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import android.util.Log;

public class FileTool {
	
	private static final String TAG = "FileTool";
	
	public static void deleteFolder(File dir) {
        File to = new File(dir.getAbsolutePath() + System.currentTimeMillis());
        dir.renameTo(to);
        if (to.isDirectory()) {
            String[] children = to.list();
            for (int i = 0; i < children.length; i++) {
                File temp = new File(to, children[i]);
                if (temp.isDirectory()) {
                    deleteFolder(temp);
                } else {
                    if (!temp.delete()) {
                        Log.e(TAG, "deleteFmeaFolder, DELETE FAIL");
                    }
                }
            }
            to.delete();
        }
    }
	
	public static boolean isFileExists(String path){
		File f = new File(path);
		return f.exists();
	}
	
	/**
	 * 读取文件节点内的数据
	 * @param fileName 所要读写文件的绝对路径（包括文件名）
	 * @return 文件内的数据
	 */
	public static String readNote(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			Log.d(TAG, "The File doesn't not exist.");
			return null;
		}
		if (file.isDirectory()) {
			Log.d(TAG, "The File is a dir.");
			return null;
		}
		
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			StringBuffer readLines = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				readLines.append(line);
			}
			if (readLines.length() > 0) {
				return readLines.toString();
			} else {
				return null;
			}
		} catch (FileNotFoundException e) {
			Log.d(TAG, "readNote --> The File doesn't not exist.", e);
		} catch (IOException e) {
			Log.d(TAG, "readNote --> Read file fail.", e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {}
			}
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {}
			}
		}
		return null;
	}
	
	public static String readNoteToHexStr(String fileName){
		File file = new File(fileName);
		if (!file.exists()) {
			Log.d(TAG, "The File doesn't not exist.");
			return null;
		}
		if (file.isDirectory()) {
			Log.d(TAG, "The File is a dir.");
			return null;
		}

		//String dd = "storage/emulated/0"; 内置sdcard路径
		
		String data = null;
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			os = new ByteArrayOutputStream();
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			data = os.toString();
		} catch (FileNotFoundException e) {
			Log.d(TAG, "readNoteToHexStr --> The File doesn't not exist.", e);
		} catch (IOException e) {
			Log.d(TAG, "readNoteToHexStr --> Read file fail.", e);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {}
			}
		}
		Log.i(TAG, "The data in this file is " + data);
		return data;
	}

	/**
	 * 根据指定路径获取TXT文档
	 * @param path 文件所在路径
	 * @return 字符串list
	 */
	public static ArrayList<String> readTxt(String path){
        ArrayList<String> list = new ArrayList<String>();
        File file = new File(path);
        if (file.isDirectory()){
            Log.d(TAG, "The File doesn't not exist.");
        }else{
        	InputStream is = null;
        	InputStreamReader isr = null;
        	BufferedReader br = null;
            try {
                is = new FileInputStream(file);
                if (is != null) {
                    isr = new InputStreamReader(is);
                    br = new BufferedReader(isr);
                    String line;
                    while (( line = br.readLine()) != null) {
                        String[] app = line.split("\\s+");
                        if (app != null) {
                            list.add(app[0]);
                        }
                    }                
                }
            }
            catch (FileNotFoundException e){
            	Log.e(TAG, "The File doesn't not exist.", e);
            }catch (IOException e){
            	Log.e(TAG, "read file fail.", e);
            } finally {
            	if (br != null) {
					try {
						br.close();
					} catch (IOException e) {}
				}
            	if (isr != null) {
            		try {
						isr.close();
					} catch (IOException e) {}
				}
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {}
				}
			}
        }
        return list;
    }
	
	/**
	 * 将所给路径文件读取出来
	 * @param path
	 * @return 十六进制字符串
	 */
	public static String convertFileToHexStr(String path) { 
		String data = null;
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(path);
			byte[] buffer = new byte[1024];
			os = new ByteArrayOutputStream();
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			data = os.toString();
		} catch (FileNotFoundException e) {
			Log.e(TAG, "The File doesn't not exist.", e);
		} catch (IOException e) {
			Log.e(TAG, "read file fail.", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {}
			}
		}
		return data;
	}
	
	public static int convertFileToInt(String path) {
		String data = convertFileToHexStr(path);
		if (null == data) {
			return 0;
		} else {
			return Integer.valueOf(data.replaceAll("\n", ""));
		}
	}

}
