package android.hqs.gj.tool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import android.util.Log;

/**
 * 通用文件处理工具
 * @author 胡青松
 */
public class FileTool {
	private static final String TAG = LogTool.makeTag("FileTool");
	
	/** 压缩文件 */
	public static byte[] compress(byte[] data) throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		compress(bais, baos);
		byte[] output = baos.toByteArray();
		baos.flush();
		baos.close();
		bais.close();
		return output;
	}

	/** 压缩文件 */
	public static void compress(InputStream is, OutputStream os) throws Exception {
		GZIPOutputStream gos = new GZIPOutputStream(os);
		int count;
		byte data[] = new byte[1024];
		while ((count = is.read(data, 0, data.length)) != -1) {
			gos.write(data, 0, count);
		}
		gos.flush();
		gos.close();
	}

	/**
	 * 删除文件。
	 * 1、如果是文件，直接删除；
	 * 2、如果是目录（文件夹）删除目录以及目录下的所有目录和文件。
	 * @param path 被删除文件的绝对路径（不用管最后是否添加{@link File#separator}）
	 * @return 全部删除成功返回true，否则返回false
	 */
	public static boolean delete(String path) {
		if (path == null || path.trim().length() <= 0) {
			Log.i(TAG, "Please enter a file path.");
			return false;
		}
		File tFile = new File(path);
		// 判断目录或文件是否存在
		if (!tFile.exists()) { // 不存在返回 false
			return false;
		} else {
			// 判断是否为文件
			if (tFile.isFile()) { // 为文件时调用删除文件方法
				return deleteFile(path);
			} else { // 为目录时调用删除目录方法
				return deleteDirectory(path);
			}
		}
	}
	
	/**
	 * 删除单个文件
	 * 
	 * @param path
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String path) {
		if (path == null || path.trim().length() <= 0) {
			Log.i(TAG, "Please enter a file path.");
			return false;
		}
		File tFile = new File(path);
		// 路径为文件且不为空则进行删除
		return tFile.exists() && tFile.isFile() && tFile.delete();
	}
	
	/**
	 * 删除单个文件
	 * 
	 * @param path
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(File file) {
		return file != null && file.exists() && file.isFile() && file.delete();
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param path
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String path) {
		if (path == null || path.trim().length() <= 0) {
			Log.i(TAG, "Please enter a file path.");
			return false;
		}
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!path.endsWith(File.separator)) {
			path += File.separator;
		}
		File tFile = new File(path);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!tFile.exists() || !tFile.isDirectory()) {
			return false;
		}
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = tFile.listFiles();
		if (files == null || files.length <= 0) {
			return true;
		}
		boolean flag = true;
        for (File file : files) {
			if (file != null && file.exists()) {
				if (file.isFile()) { // 删除子文件
                    flag = file.delete();
					if (!flag) {
						// 有一个文件没有删除成功
						break;
					}
				} else {				// 删除子目录
					flag = deleteDirectory(file.getAbsolutePath());
					if (!flag) {
						break;
					}
				}
			}
		}
		if (!flag) {
			return false;
		}
		// 删除当前目录
		return tFile.delete();
	}
	
	public static void deleteByLimitSize (final String path, final long limitSize){
		if (path == null || path.trim().length() <= 0) {
			Log.i(TAG, "Please enter a file path.");
			return;
		}
		File tFile = new File(path);
		if (!tFile.exists()) {
			Log.i(TAG, "No file can be deleted.");
			return;
		}
		long size = 0;
		if (tFile.isFile()) {
			size = tFile.length();
			Log.i(TAG, "It is a file size = " + size);
			if (size >= limitSize || size == 0) {
				tFile.delete();
			}
		} else {
			size = dirSize(tFile);
			Log.i(TAG, "All " + tFile.getAbsolutePath() + " size = " + size);
			if (size == 0) {
				tFile.delete();
			} else if (size >= limitSize) {
				File[] files = tFile.listFiles();
				if (files == null || files.length <= 0) {
					return;
				}
				try {
                    // 某些file为null，将抛出异常
					Arrays.sort(files, new FileComparator());
					// 循环删除本地文件，直到小于阈值为止
                    for (File file : files) {
						if (file.isFile()) {
                            file.delete();
						} else {
							deleteDirectory(file.getAbsolutePath());
						}
						size = dirSize(tFile);
						Log.i(TAG, "After delete size = " + size);
						if (size <= limitSize) {
							break;
						}
					}
				} catch (Exception e) {
					Log.e(TAG, "deleteByLimitSize", e);
				}
			}
		}
	}
	
	/**
	 * 通过获取文件名并对各文件名进行排序，将排在前面的文件删除
	 * @param path
	 * @param limitCount 被保留的文件个数，不能小于0
	 */
	public static void deleteByNameAndLimitCount(String path, int limitCount) {
		if (path == null || path.trim().length() <= 0) {
			Log.i(TAG, "Please enter a file path.");
			return;
		}
		File tFile = new File(path);
		if (!tFile.exists()) {
			Log.i(TAG, "This file does not exist.");
			return;
		}
		if (tFile.isFile()) {
			tFile.delete();
		} else {
			File[] files = tFile.listFiles();
			if (files != null && files.length > limitCount) {
				String[] fileNames = new String[files.length];
				for (int i = 0; i < files.length; i++) {
					fileNames[i] = files[i].getAbsolutePath();
				}
                // 某些fileName为null，将抛出异常
                try {
                    Arrays.sort(fileNames);
                    for (int i = 0; i < fileNames.length - limitCount; i++) {
                        delete(fileNames[i]);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "deleteByNameAndLimitCount", e);
                }
            }
		}
	}
	
	public static boolean isExists(String path){
		if (path == null || path.trim().length() <= 0) {
			Log.i(TAG, "Please enter a file path.");
			return false;
		}
		File tFile = new File(path);
		return tFile.exists();
	}
	
	public static boolean isZip(String s) {
        return !(TextTool.isEmpty(s) || s.length() < 4) && s.substring(s.length() - 4, s.length()).equals(".zip");
	}
	
	/**
	 * 将所给路径文件读取出来
	 * @param absName
	 * @return 十六进制字符串
	 */
	public static String convertToHexStr(String absName) { 
		if (absName == null || absName.trim().length() <= 0) {
			Log.i(TAG, "Please enter a file name.");
			return null;
		}
		String data = null;
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(absName);
			os = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			os.flush();
			data = os.toString();
		} catch (FileNotFoundException e) {
			Log.e(TAG, "The File doesn't not exist.", e);
		} catch (IOException e) {
			Log.e(TAG, "Read file fail.", e);
		} finally {
			close(os);
			close(is);
		}
		return data;
	}
	
	public static String convertStreamToStr(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		try {
            String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} catch (IOException e) {
			Log.e(TAG, "read file fail.", e);
		} finally {
			close(is);
		}
		return sb.toString();
	}
	
	public static void copy(File sourceFile, File targetFile) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			// 新建文件输入流并对它进行缓冲
			bis = new BufferedInputStream(new FileInputStream(sourceFile));
			// 新建文件输出流并对它进行缓冲
			bos = new BufferedOutputStream(new FileOutputStream(targetFile));
			// 缓冲数组
			byte[] b = new byte[1024 * 1];
			int len;
			while ((len = bis.read(b)) != -1) {
				bos.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			bos.flush();
		} catch (FileNotFoundException e) {
			Log.e(TAG, "The File doesn't not exist.", e);
		} catch (IOException e) {
			Log.e(TAG, "read file fail.", e);
		}  finally {
			// 关闭流
			close(bis);
			close(bos);
		}
	}
	
	public static boolean copy(String fromFile, String toPath, String toFile) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		boolean result = false;
		try {
			if (!toPath.endsWith(File.separator)){
				toPath += File.separator;
			}
			File path = new File(toPath);
			if (!path.exists()) {
				path.mkdirs();
			}
			fis = new FileInputStream(fromFile);
			fos = new FileOutputStream(toPath + toFile);
			byte buffer[] = new byte[1024];
			int len;
			while ((len = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
			fos.flush();
			result = true;
		} catch (IOException e) {
			Log.e(TAG, "copy", e);
		} finally {
			close(fis);
			close(fos);
		}
		return result;
	}
	
	public static void copy(File fromFile, File toFile, boolean rewrite) {
		if (!fromFile.exists() || !fromFile.isFile() || !fromFile.canRead()) {
			return;
		}
		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}
		if (toFile.exists() && rewrite) {
			toFile.delete();
		}
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(fromFile);
			fos = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fis.read(bt)) > 0) {
				fos.write(bt, 0, c);
			}
			fos.flush();
		} catch (IOException e) {
			Log.e(TAG, "copy", e);
		} finally {
			FileTool.close(fis);
			FileTool.close(fos);
		}
	}
	
	/**
	 * 获取当前文件夹下的文件或目录
	 * @param path 文件夹路径
	 * @return 如果文件不存在，或文件夹下面没有文件，返回 null
	 */
	public static File[] getFiles(String path){
		List<File> tFiles = getFileList(path);
		if (tFiles == null) {
			return null;
		}
		File[] files = new File[tFiles.size()];
		tFiles.toArray(files);
		return files;
	}
	
	/**
	 * 获取当前文件夹下的文件或目录
	 * @param path 文件夹路径
	 * @return 如果文件不存在，或文件夹下面没有文件，返回 null
	 */
	public static List<File> getFileList(String path){
		if (path == null || path.trim().length() <= 0) {
			Log.i(TAG, "Please enter a file path.");
			return null;
		}
		return getFileList(new File(path));
	}
	
	/**
	 * 获取当前文件
	 * @param srcFile 文件夹或文件
	 * @return 如果文件不存在，或文件夹下面没有文件，返回 null
	 */
	public static List<File> getFileList(File srcFile) {
        if (srcFile == null || !srcFile.exists()) {
            Log.e(TAG, "There is no such file or directory.");
            return null;
        }
        List<File> files = new ArrayList<File>();
        if (srcFile.isFile()) {
            files.add(srcFile);
        } else {
            File[] fs = srcFile.listFiles();
            if (fs == null || fs.length <= 0) {
                Log.e(TAG, "1'This folder does not have any files, it is blank!");
                return null;
            }
            for (File child : fs) {
                if (child.isFile()) {
                    files.add(child);
                } else {
                    List<File> childFiles = getFileList(child);
                    if (childFiles != null) {
                        files.addAll(childFiles);
                    }
                }
            }
        }
        if (files.size() <= 0) {
            Log.e(TAG, "2'This folder does not have any files, it is blank!");
            return null;
        }
        return files;
    }

    /* added by dengqiang for directory zip 2013-09-06 */
	public static void zip(ZipOutputStream out, String zipPath, String rootPath) throws Exception{
		File zipFile = new File(zipPath);
		if (zipFile.isFile() && (!isZip(zipFile.getName()))) {
			Log.i(TAG, "Not zip file.");
			FileInputStream fis = null;
			try {
				byte[] buffer = new byte[1024];
				fis = new FileInputStream(zipFile);
				if (!rootPath.endsWith(File.separator)) {
					rootPath +=File.separator;
				}
				out.putNextEntry(new ZipEntry(rootPath + zipFile.getName()));
				int len;
				while ((len = fis.read(buffer)) > 0) {
					out.write(buffer, 0, len);
					out.flush();
				}
				Log.i(TAG, "Compression success!!!!");
			} catch (Exception e) {				
				Log.i(TAG, "Compression failure!!!!");
				throw e;
			} finally {
				out.closeEntry();
				// 不管压缩成功（不再需要了）还是失败（不然后面还是不会压缩成功），直接将其删除
				zipFile.delete();
				close(fis);
			}			
		} else if (zipFile.isDirectory()) {
			Log.i(TAG, "Is folder.");
			File[] files = zipFile.listFiles();
			if (files != null && files.length > 0) {
				for (File f : files) {
					if (f != null) {
						Log.i(TAG, "Prepare folder.");
						zip(out, zipPath + "/" + f.getName(), rootPath + "/" + zipFile.getName());
					}
				}
			}
			zipFile.delete();
		}
	}
	/* added end */

	/**
	 * 压缩文件。
	 * @param zipName 压缩后的文件名，绝对路径，如：<b>"E:/hqs.zip"</b>。
	 * @param path 将要被压缩的文件或文件夹，绝对路径。
	 * @return 每个文件都压缩成功，返回true，否则false。
	 */
	public static boolean zip(String zipName, String... path) throws Exception {
		boolean success = false;
		ZipOutputStream zos = null;
		FileOutputStream fos = new FileOutputStream(new File(zipName));
		CheckedOutputStream cos = new CheckedOutputStream(fos, new CRC32());
		zos = new ZipOutputStream(cos);
        for (String pt : path) {
			success = zip(new File(pt), zos, "");
		}
		zos.close();
		return success;
	}

	/**
	 * 压缩文件。
	 * @param zipName 压缩后的文件名，绝对路径，如：<b>"E:/hqs.zip"</b>。
	 * @param srcPath 将要被压缩的文件或文件夹，绝对路径。
	 * @return 每个文件都压缩成功，返回true，否则false。
	 */
	public static boolean zip(String zipName, String srcPath) throws Exception {
		File file = new File(srcPath);
		if (!file.exists()) {
			throw new RuntimeException(srcPath + " does not exists.");
		}
		FileOutputStream fos = new FileOutputStream(new File(zipName));
		CheckedOutputStream cos = new CheckedOutputStream(fos, new CRC32());
		ZipOutputStream zos = new ZipOutputStream(cos);
        boolean success = zip(file, zos, "");
		zos.flush();
		zos.closeEntry();
		zos.close();
		return success;
	}

	private static boolean zip(File file, ZipOutputStream out, String baseDir) throws Exception {
		boolean success = false;
		/* 判断是目录还是文件 */
		if (file.isDirectory()) {
			success = zipDirectory(file, out, baseDir);
		} else if (file.isFile()){
			success = zipFile(file, out, baseDir);
		}
		return success;
	}

	/** 压缩一个目录  */
	private static boolean zipDirectory(File dir, ZipOutputStream out, String baseDir) throws Exception {
		if (!dir.exists()) {
			Log.w(TAG, dir.getAbsolutePath() + " does not exists.");
			return false;
		}
		boolean success = false;
		File[] files = dir.listFiles();
		if (files != null && files.length > 0) {
            for (File file : files) {
				/* 递归 */
				success = zip(file, out, baseDir + dir.getName() + "/");
			}
		}
		return success;
	}
	/** 压缩一个文件 */
	private static boolean zipFile(File file, ZipOutputStream out, String baseDir) throws Exception {
		if (!file.exists()) {
			Log.w(TAG, file.getAbsolutePath() + " does not exists.");
			return false;
		}
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		ZipEntry entry = new ZipEntry(baseDir + file.getName());
		out.putNextEntry(entry);
		int count;
		byte data[] = new byte[8192];
		// 这里出现过死循环
		while ((count = bis.read(data, 0, data.length)) != -1) {
			out.write(data, 0, count);
		}
		out.flush();
		bis.close();
		return true;
	}
	
	public static boolean writeToDisk(String dir, String fileName, String src) {
		try {
			return writeToDisk(dir, fileName, src.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "writeToDisk", e);
			return false;
		}
	}
	
	public static boolean writeToDisk(String dir, String fileName, byte[] src) {
		return writeToDisk(dir, fileName, src, false);
	}
	
	public static boolean writeToDisk(String dir, String fileName, byte[] src, boolean append) {
		if (dir == null || dir.trim().length() <= 0) {
			return false;
		}
		File parent = new File(dir);
		if (!parent.exists()) {
			parent.mkdirs();
		}
		if (!dir.endsWith(File.separator)) {
			dir += File.separator;
		}
		File targetFile = new File(dir + fileName);
		FileOutputStream fos = null;
		try {
			if (!targetFile.exists()) {
				targetFile.createNewFile();
			}
			Log.i(TAG, "fileName = " + fileName);
			fos = new FileOutputStream(targetFile, append);
			fos.write(src);
			fos.flush();
			return true;
		} catch (IOException e) {
			Log.e(TAG, "writeToDisk", e);
			return false;
		} finally {
			FileTool.close(fos);
		}
	}
	
	/**
	 * 递归 取得文件或文件夹大小
	 * @param f 文件或文件夹
	 * @return 文件大小
	 */
	public static long size(File f) {
		if (f == null) {
			return 0;
		} else {
			if (f.exists()) {
				if (f.isFile()) {
					return f.length();
				}
				return dirSize(f);
			}
			return 0;
		}
	}
	
	/**
	 * 递归 取得文件夹大小
	 * @param f 文件夹
	 * @return 整个文件夹的大小
	 */
	public static long dirSize(File f) {
		if (f == null || !f.exists()) {
			return 0;
		}
		long size = 0;
		File files[] = f.listFiles();
		if (files != null && files.length > 0) {
            for (File file : files) {
				if (file != null && file.exists()) {
					if(file.isFile()){
						size = size + file.length();
					} else if (file.isDirectory()) {
						size = size + dirSize(file);
					}
				}
			}
		}
		return size;
	}
	
	public static void close(Closeable file){
		if (file != null) {
			try {
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
