package android.hqs.helper;

import java.util.Locale;

import android.content.Context;

/**
 * 语音辅助类
 * @author 胡青松
 */
public class LangHelper {

	public static boolean isZh(Context context){
		Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh")){
            return true;
        }
		return false;
	}
	
	public static boolean isEN(Context context){
		Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("en")){
            return true;
        }
		return false;
	}
	
	public static String getLanguage() {
		return Locale.getDefault().getLanguage();
	}
	
}
