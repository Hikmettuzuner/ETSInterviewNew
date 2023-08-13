package com.dev.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dev.model.FileInformation;

public class ClientEtsFileDaoImp implements ClientEtsFileDao {

	private static final String FIND_ALL_STATEMENT = "SELECT * FROM EtsFile ORDER BY id ASC;";
	private static final String FIND_FILE_VERSION = "select fileVersion from EtsFile where fileName=?;";
	private static final String FIND_FILE_DETAIL = "select * from EtsFile where fileName=?;";
	private static final String UPDATE_STATEMENT = "UPDATE EtsFile SET fileName=?"
			+ ",fileType=?,filePath=?,fileSize=?, fileVersion=? WHERE fileName=?;";
	private static final String SAVE_STATEMENT = "INSERT INTO EtsFile(fileName,fileType,filePath,fileSize,fileVersion) VALUES (?,?,?,?,?)";
	private static final String DELETE_STATEMENT = "Delete From EtsFile Where fileName=?;";

	public static Connection getConnection() {
		String connectionString = "jdbc:sqlserver://localhost:1433;databaseName=ETS;user=sa;password=admin;encrypt=true;trustServerCertificate=true";
		try {
			DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
			return DriverManager.getConnection(connectionString);
		} catch (SQLException e) {
			System.out.print("Unfortunatelly : " + e.getMessage());
		}
		return null;
	}

	public static void close(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			System.out.print("Unfortunatelly : " + e.getMessage());
		}

	}

	@Override
	public List<FileInformation> findAll() {
		List<FileInformation> webusers = new ArrayList<FileInformation>();
		Connection connection = getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_STATEMENT);

			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				FileInformation fileInf = new FileInformation();
				fileInf.setId(rs.getInt("id"));
				fileInf.setFileName(rs.getString("fileName"));
				fileInf.setFileSize(rs.getInt("fileSize"));
				fileInf.setFilePath(rs.getString("filePath"));
				fileInf.setFileType(rs.getString("fileType"));
				webusers.add(fileInf);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.print("Unfortunatelly : " + e.getMessage());
			close(connection);
		}
		close(connection);
		return webusers;
	}

	@Override
	public Boolean update(FileInformation fileInformation) {

		Connection connection = getConnection();

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STATEMENT);
			preparedStatement.setString(1, fileInformation.getFileName());
			preparedStatement.setString(2, fileInformation.getFileType());
			preparedStatement.setString(3, fileInformation.getFilePath());
			preparedStatement.setLong(4, fileInformation.getFileSize());
			preparedStatement.setInt(5, fileInformation.getFileVersion());
			preparedStatement.setString(6, fileInformation.getFileName());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.print("Unfortunatelly : " + e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public Boolean delete(String fileName) {
		Connection connection = getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(DELETE_STATEMENT);
			preparedStatement.setString(1, fileName);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.print("Unfortunatelly : " + e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public int findFileVersion(String fileName) {
		Connection connection = getConnection();
		int guideVersion = 0;

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(FIND_FILE_VERSION);
			preparedStatement.setString(1, fileName);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				guideVersion = rs.getInt("fileVersion");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.print("Unfortunatelly : " + e.getMessage());
			close(connection);
			return guideVersion;
		}
		close(connection);
		return guideVersion;

	}

	@Override
	public Boolean save(FileInformation fileInformation) {
		Connection connection = getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(SAVE_STATEMENT);
			preparedStatement.setString(1, fileInformation.getFileName());
			preparedStatement.setString(2, fileInformation.getFileType());
			preparedStatement.setString(3, fileInformation.getFilePath());
			preparedStatement.setLong(4, fileInformation.getFileSize());
			preparedStatement.setInt(5, fileInformation.getFileVersion());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.print("Unfortunatelly : " + e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public FileInformation findFileByName(String fileName) {
		FileInformation fileInformation = new FileInformation();
		Connection connection = getConnection();

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(FIND_FILE_DETAIL);
			preparedStatement.setString(1, fileName);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				fileInformation.setFileName(rs.getString("fileName"));
				fileInformation.setFileType(rs.getString("fileType"));
				fileInformation.setFilePath(rs.getString("filePath"));
				fileInformation.setFileSize(rs.getLong("fileSize"));
				fileInformation.setFileVersion(rs.getInt("fileVersion"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.print("Unfortunatelly : " + e.getMessage());
			close(connection);
			return fileInformation;
		}
		close(connection);
		return fileInformation;

	}
}
