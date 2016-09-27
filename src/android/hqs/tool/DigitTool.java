package android.hqs.tool;

import java.util.regex.Pattern;

public class DigitTool {

	/**
	 * 是否是0~9的数字
	 * @param c
	 * @return
	 */
	public static final boolean isDigit(char c) {
		if (c >= '0' && c <= '9') {
			return true;
		}
		return false;
	}
	
	/**
	 * 是否是0~9的数字
	 * @param target
	 * @return
	 */
	public static boolean isDigit(String target) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(target).matches();
	}
	
}
