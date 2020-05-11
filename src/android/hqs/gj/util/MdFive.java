package android.hqs.gj.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.vivo.android.tool.FileTool;
import com.vivo.android.util.LogUtil;

import android.util.Log;

public class MdFive {

	// =======================================
	// Constants
	// =======================================
	
	// 原来是VivoGame.Updater
	private static final String TAG = LogUtil.makeTag("MdFive");

	// =======================================
	// Methods
	// =======================================

	public static boolean checkMdFive(String mdFive, File updateFile, boolean checkHash) {
		String digest = null;
		if (mdFive == null || mdFive.equals("") || updateFile == null) {
			Log.e(TAG, "md5 = " + mdFive + ", updateFile = " + updateFile);
			return false;
		}

		if (checkHash) {
			long id = getFileMD5Hash(updateFile);
			if (id != -1) {
				digest = String.valueOf(id);
			}
		} else {
			digest = calculateMdFive(updateFile);
		}

		if (digest == null) {
			Log.e(TAG, "md5 calculatedDigest NULL");
			return false;
		}

		Log.d(TAG, "md5  Calculated digest: " + digest + ", Provided = " + mdFive);

		return digest.equalsIgnoreCase(mdFive);
	}

	public static String calculateMdFive(File updateFile) {
		if (!updateFile.isFile()) {
			return null;
		}

		MessageDigest digest;
		try {
			// hashType的值："MD5"，"SHA1"，"SHA-256"，"SHA-384"，"SHA-512"
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			Log.e(TAG, "while getting Digest", e);
			return null;
		}

		InputStream is = null;
		byte[] buffer = new byte[8192];
		int read;
		try {
			is = new FileInputStream(updateFile);
			while ((read = is.read(buffer)) > 0) {
				digest.update(buffer, 0, read);
			}
			byte[] mdFiveSum = digest.digest();
			BigInteger bigInt = new BigInteger(1, mdFiveSum);
			String output = bigInt.toString(16);
			// Fill to 32 chars
			output = String.format("%32s", output).replace(' ', '0');
			return output;
		} catch (IOException e) {
			Log.e(TAG, "read update file failed!", e);
		} finally {
			FileTool.close(is);
		}
		return null;
	}

	public static long MD5Hash(String str) {
		String strMd5 = getMD5Str(str); // MD5 加密
		String sign = strMd5.substring(8, 8 + 16);
		long id1 = 0;
		long id2 = 0;
		for (int i = 0; i < 8; i++) {
			id2 *= 16;
			id2 += Integer.parseInt(sign.substring(i, i + 1), 16);
		}
		for (int i = 8; i < sign.length(); i++) {
			id1 *= 16;
			id1 += Integer.parseInt(sign.substring(i, i + 1), 16);
		}
		return (id1 + id2) & 0xffffffffL;
	}

	public static String getMD5Str(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			Log.e(TAG, "Parse string failed", e);
			return null;
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "unsupported UTF-8 encodeing", e);
		}

		byte[] byteArr = messageDigest.digest();
		StringBuffer md5Buff = new StringBuffer();
		for (int i = 0; i < byteArr.length; i++) {
			if (Integer.toHexString(0xFF & byteArr[i]).length() == 1) {
				md5Buff.append("0").append(Integer.toHexString(0xFF & byteArr[i]));
			} else {
				md5Buff.append(Integer.toHexString(0xFF & byteArr[i]));
			}
		}
		return md5Buff.toString();
	}

	public static long getFileMD5Hash(File file) {
		if (!file.isFile()) {
			return -1;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
		} catch (Exception e) {
			return -1;
		} finally {
			FileTool.close(in);
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		String strMd5 = bigInt.toString(16); // MD5 加密
		String sign = strMd5.substring(8, 8 + 16);
		long id1 = 0;
		long id2 = 0;
		for (int i = 0; i < 8; i++) {
			id2 *= 16;
			id2 += Integer.parseInt(sign.substring(i, i + 1), 16);
		}

		for (int i = 8; i < sign.length(); i++) {
			id1 *= 16;
			id1 += Integer.parseInt(sign.substring(i, i + 1), 16);
		}
		return (id1 + id2) & 0xffffffffL;
	}
}
