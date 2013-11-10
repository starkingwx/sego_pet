package com.richitec.util;

import java.io.File;

import org.apache.commons.fileupload.FileItem;

import com.imeeting.framework.ContextLoader;

public class FileUtil {
	/**
	 * make directories by path
	 * 
	 * @param path
	 */
	public static void makeDirectories(String path) {
		File filePath = new File(path); 
		if (!filePath.exists())
			filePath.mkdirs();
	}

	
	/**
	 * trim the extension tag of file name
	 * @param fileName
	 * @return
	 */
	public static String trimFileExtension(String fileName) {
		String noExtFile = null;
		int index = fileName.lastIndexOf(".");
		if (index > 0) {
			noExtFile = fileName.substring(0, index);
		} else {
			noExtFile = fileName;
		}
		return noExtFile;
	}
	
	/**
	 * delete file
	 * @param file
	 */
	public static void deleteFile(String file) {
		File f = new File(file);
		f.delete();
	}
	
	public static void saveFile(String fileName, FileItem fileItem) throws Exception {
		if (fileItem == null) {
			return;
		}
		String uploadPath = ContextLoader.getConfiguration().getUploadDir();
		FileUtil.makeDirectories(uploadPath);
		File uploadFile = new File(uploadPath + File.separator
				+ fileName);
		fileItem.write(uploadFile); // save file
	}
}
