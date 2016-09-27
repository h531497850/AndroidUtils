package android.hqs.tool;

public final class TextTool {
	 
    public static boolean isEmpty(final CharSequence s) {
        if (s == null) {
            return true;
        }
        return s.length() == 0;
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
 
}
