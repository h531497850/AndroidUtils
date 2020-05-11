package com.vivo.android.tool;

import java.io.File;
import java.util.Comparator;

/**
 * 判断文件修改先后
 * @author 胡青松
 */
public class FileComparator implements Comparator<File> {

	@Override
	public int compare(File src, File dest) {
		long diff = src.lastModified() - dest.lastModified();
		if (diff > 0) {
			return 1;
		} else if (diff == 0) {
			return 0;
		} else {
			return -1;
		}
	}
}