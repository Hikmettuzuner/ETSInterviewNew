package com.dev.utility;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public class ConversationUtil {

	public static String getFileExtension(MultipartFile etsFile) {
		String originalFileName = etsFile.getOriginalFilename();
		return originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
	}

	public static Boolean getFileSize(MultipartFile etsFile) {
		long sizeBayt = etsFile.getSize();
		long sizeMB = sizeBayt / (1024 * 1024);
		if (sizeMB > 5) {
			return false;
		} else {
			return true;
		}
	}

	public static Long calculateFileSize(MultipartFile etsFile) {
		long sizeBayt = etsFile.getSize();
		long sizeMB = sizeBayt / (1024 * 1024);
		return sizeMB;
	}

	public static boolean deleteFolder(File folder) {
		if (folder.exists() && folder.isDirectory()) {
			File[] files = folder.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isDirectory()) {
						// recursively delete subdirectories
						deleteFolder(file);
					} else {
						file.delete();
					}
				}
			}
			folder.delete();
			return true;
		}
		return true;
	}

}
