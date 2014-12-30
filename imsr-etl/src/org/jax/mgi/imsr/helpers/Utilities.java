package org.jax.mgi.imsr.helpers;

import java.io.File;


public class Utilities {
	public static String pluralSuffix(Integer count, String singular, String plural) {
		return (count == 1) ? singular : plural;
	}
	
	public static File newerFile(File file1, File file2) {
		if (file1 == null && file2 == null) {
			return null;
		}
		if (file1 == null) {
			return file2;
		}
		if (file2 == null) {
			return file1;
		}
		
		return file1.lastModified() > file2.lastModified() ? file1 : file2;
	}
	
}
