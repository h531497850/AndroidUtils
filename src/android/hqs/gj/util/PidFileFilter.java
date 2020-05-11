package android.hqs.gj.util;

import java.io.File;
import java.io.FileFilter;

public class PidFileFilter implements FileFilter{

	public boolean accept(File paramFile){
		boolean isPid = false;
		try{
			Integer.parseInt(paramFile.getName());
			isPid = true;
		}catch (NumberFormatException localNumberFormatException){
			isPid = false;
		}
		return isPid;
	}
}
