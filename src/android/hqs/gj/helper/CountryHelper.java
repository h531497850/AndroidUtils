package android.hqs.gj.helper;

import java.util.Locale;

import android.content.Context;

public class CountryHelper {
	
	/** 中国大陆（语言简体）*/
	public static boolean isCN(Context context){
		Locale locale = context.getResources().getConfiguration().locale;
        String country = locale.getCountry();
        if (country.equalsIgnoreCase("cn")){
            return true;
        }
		return false;
	}
	
	/** 中国台湾（语言繁体）*/
	public static boolean isTW(Context context){
		Locale locale = context.getResources().getConfiguration().locale;
        String country = locale.getCountry();
        if (country.equalsIgnoreCase("tw")){
            return true;
        }
		return false;
	}
	
	/** 英国（语言英式）*/
	public static boolean isUK(Context context){
		Locale locale = context.getResources().getConfiguration().locale;
        String country = locale.getCountry();
        if (country.equalsIgnoreCase("uk")){
            return true;
        }
		return false;
	}
	
	/** 美国（语言美式）*/
	public static boolean isUS(Context context){
		Locale locale = context.getResources().getConfiguration().locale;
        String country = locale.getCountry();
        if (country.equalsIgnoreCase("us")){
            return true;
        }
		return false;
	}
	
}
