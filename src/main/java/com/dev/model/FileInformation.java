package com.dev.model;

public class FileInformation {

	private int id;
	private String fileName;
	private String fileType;
	private String filePath;
	private long fileSize;
	private String fileStatu;
	private int fileVersion;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileStatu() {
		return fileStatu;
	}

	public void setFileStatu(String fileStatu) {
		this.fileStatu = fileStatu;
	}

	public int getFileVersion() {
		return fileVersion;
	}

	public void setFileVersion(int fileVersion) {
		this.fileVersion = fileVersion;
	}

	public FileInformation(int id, String fileName, String fileType, String filePath, long fileSize, String fileStatu,
			int fileVersion) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.fileType = fileType;
		this.filePath = filePath;
		this.fileSize = fileSize;
		this.fileStatu = fileStatu;
		this.fileVersion = fileVersion;
	}

	public FileInformation(String fileStatu) {
		super();
		this.fileStatu = fileStatu;

	}

	public FileInformation() {
		super();
		// TODO Auto-generated constructor stub
	}

}
