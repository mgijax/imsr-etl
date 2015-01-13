package org.jax.mgi.imsr.helpers;

import java.io.File;


public class Utilities {
	/**
	 * Returns string with correct (singular or plural) suffix based on the count.
	 * 
	 * @param count		- number used to determine singular or plural suffix
	 * @param singular	- singular suffix to return
	 * @param plural	- plural suffix to return
	 * @return	string with correct suffix based on count
	 */
	public static String pluralSuffix(Integer count, String singular, String plural) {
		return (count == 1) ? singular : plural;
	}
	
	/**
	 * Returns the most recent of two files.
	 * 
	 * @param file1 	- first file to compare
	 * @param file2		- second file to compare
	 * @return			file with the most recent last modified date
	 * @see				File
	 */
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
