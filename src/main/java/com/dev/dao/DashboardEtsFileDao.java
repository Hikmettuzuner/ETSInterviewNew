package com.dev.dao;

import java.util.List;

import com.dev.model.FileInformation;

public interface DashboardEtsFileDao {
	List<FileInformation> findAll();

	Boolean update(FileInformation fileInformation);

	Boolean save(FileInformation fileInformation);

	Boolean delete(String fileName);

	int findFileVersion(String fileName);

}
