package android.hqs.gj.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TextTool {
	 
    public static boolean isEmpty(final CharSequence s) {
        if (s == null) {
            return true;
        }
        return s.length() <= 0;
    }
 
    public static boolean isBlank(final CharSequence s) {
        if (s == null) {
            return true;
        }
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isNull(final String s){
		return s == null || s.trim().equals("") || s.trim().equals("null");
	}
    
    // 提取字符串中的数字
	public static String pickDigit(String content) {
		if (content != null) {
			content = content.trim();
			if (!"".equals(content)) {
				StringBuffer tempValue = new StringBuffer();
				for (int i = 0; i < content.length(); i++) {
					if (content.charAt(i) >= 48 && content.charAt(i) <= 57) {
						tempValue.append(content.charAt(i));
					}
				}
				return tempValue.toString();
			}
		}
		return "";
	}
	
	public static String splitDigit(String content) {
		if (content != null) {
			String regEx="[^0-9]";   
			Pattern p = Pattern.compile(regEx);   
			Matcher m = p.matcher(content);  
			return m.replaceAll("").trim();
		}
		return "";
	}
	
	/**
	 * 截取数字
	 * @param content
	 * @return
	 */
	public String getNumbers(String content) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}

	/**
	 * 截取非数字
	 * @param content
	 * @return
	 */
	public String splitNotNumber(String content) {
		Pattern pattern = Pattern.compile("\\D+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}
 
}
